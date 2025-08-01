package com.hb.cda.springholiday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDateTime expiresAt;

    @ManyToOne  // Pour avoir plusieurs sessions (PC, mobile...)
    private User user;

    public RefreshToken() {
    }

    public RefreshToken(String id, LocalDateTime expiresAt, User user) {
        this.id = id;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

}
