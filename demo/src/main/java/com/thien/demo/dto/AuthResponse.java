package com.thien.demo.dto;

import java.util.Set;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private Set<String> roles;

    public AuthResponse() {}

    public AuthResponse(String token, String username, Set<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    public String getToken() { return token; }
    public String getType() { return type; }
    public String getUsername() { return username; }
    public Set<String> getRoles() { return roles; }

    public void setToken(String token) { this.token = token; }
    public void setType(String type) { this.type = type; }
    public void setUsername(String username) { this.username = username; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}