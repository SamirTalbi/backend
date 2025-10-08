package com.example.project.dto;

import javax.management.relation.Role;

public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private Role role;

    public UserDTO(Long id, String email, String fullName, Role role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
}
