package com.thien.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret",
                "ZGVtby1qd3Qtc2VjcmV0LWtleS1jaGFuZ2UtdGhpcy10by1hLXN0cm9uZy1iYXNlNjQta2V5");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 60000L);
    }

    @Test
    void generateAndValidateToken_success() {
        var userDetails = User.withUsername("testuser")
                .password("encoded")
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("testuser", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}