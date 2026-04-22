package com.thien.demo.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thien.demo.entity.Role;
import com.thien.demo.entity.User;
import com.thien.demo.repository.RoleRepository;
import com.thien.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthFlowIntegrationTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private RoleRepository roleRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;

        @BeforeEach
        void seedAdminIfMissing() {
                Role roleUser = roleRepository.findByName("ROLE_USER")
                                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
                Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

                userRepository.findByUsername("admin").orElseGet(() -> {
                        User admin = new User();
                        admin.setUsername("admin");
                        admin.setPassword(passwordEncoder.encode("Admin@123"));
                        admin.setEmail("admin@test.local");
                        admin.setEnabled(true);
                        admin.setRoles(Set.of(roleUser, roleAdmin));
                        return userRepository.save(admin);
                });
        }

        @Test
        void register_login_accessProtectedEndpoints() throws Exception {
                String username = "user" + System.currentTimeMillis();
                String email = username + "@mail.com";

                String registerBody = """
                                {
                                  "username": "%s",
                                  "password": "123456",
                                  "email": "%s"
                                }
                                """.formatted(username, email);

                final MediaType application_JSON2 = MediaType.APPLICATION_JSON;
                if (application_JSON2 != null) {
                        mockMvc.perform(post("/api/auth/register")
                                        .contentType(application_JSON2)
                                        .content(registerBody))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.token").isNotEmpty());
                } else {
                        // TODO handle null value
                }

                String loginBody = """
                                {
                                  "username": "%s",
                                  "password": "123456"
                                }
                                """.formatted(username);

                String loginResponse = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginBody))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                JsonNode loginJson = objectMapper.readTree(loginResponse);
                String token = loginJson.get("token").asText();

                mockMvc.perform(get("/api/users/me")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.username").value(username));

                mockMvc.perform(get("/api/admin/ping")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isForbidden());
        }

        @Test
        void adminCanAccessAdminEndpoint() throws Exception {
                String adminLoginBody = """
                                {
                                  "username": "admin",
                                  "password": "Admin@123"
                                }
                                """;

                String loginResponse = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(adminLoginBody))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                String token = objectMapper.readTree(loginResponse).get("token").asText();

                mockMvc.perform(get("/api/admin/ping")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Admin access granted"));
        }
}