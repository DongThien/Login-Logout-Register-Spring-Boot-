package com.thien.demo.controller;

import com.thien.demo.dto.UserMeResponse;
import com.thien.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> me(Authentication authentication) {
        return ResponseEntity.ok(userService.getMe(authentication.getName()));
    }
}