package com.thien.demo.dto;

import java.util.Set;

public class UserMeResponse {
    private Long id;
    private String username;
    private String email;
    private Boolean enabled;
    private Set<String> roles;

    public UserMeResponse() {
    }

    public UserMeResponse(Long id, String username, String email, Boolean enabled, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}