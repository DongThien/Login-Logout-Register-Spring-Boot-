package com.thien.demo.service;

import com.thien.demo.dto.UserMeResponse;
import com.thien.demo.entity.User;
import com.thien.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserMeResponse getMe(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserMeResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getEnabled(),
                user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet()));
    }
}