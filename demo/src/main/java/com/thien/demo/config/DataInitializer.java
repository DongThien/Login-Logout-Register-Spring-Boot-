package com.thien.demo.config;

import com.thien.demo.entity.Role;
import com.thien.demo.entity.User;
import com.thien.demo.repository.RoleRepository;
import com.thien.demo.repository.UserRepository;

import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        User admin = userRepository.findByUsername("admin").orElseGet(User::new);
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setEnabled(true);
        admin.setPassword(passwordEncoder.encode("Admin@123"));

        if (admin.getRoles() == null) {
            admin.setRoles(new HashSet<>());
        }
        admin.getRoles().add(roleUser);
        admin.getRoles().add(roleAdmin);

        userRepository.save(admin);
    }
}