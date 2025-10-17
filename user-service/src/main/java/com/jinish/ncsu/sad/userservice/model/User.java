package com.jinish.ncsu.sad.userservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails { // <-- Implement the interface

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING) // Stores the enum name ("ROLE_USER") in the DB
    private Role role;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- METHODS REQUIRED BY UserDetails INTERFACE ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // This is how Spring Security understands roles
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        // Our "username" is the email address.
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account is never expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials are never expired
    }

    @Override
    public boolean isEnabled() {
        return true; // Account is always enabled
    }
}