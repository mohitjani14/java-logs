package com.logdownloader.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String email;

    private String role;

    private boolean adUser;

    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdUser() {
        return adUser;
    }

    public void setAdUser(boolean adUser) {
        this.adUser = adUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}