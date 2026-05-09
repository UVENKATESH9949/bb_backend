package com.BrainBlitz.dto.response;

import java.time.LocalDateTime;

public class UserSummaryResponse {

    private Long id;
    private String name;
    private String email;
    private String role;

    private int currentLevel;
    private int currentStreak;

    private boolean isEmailVerified;
    private boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    // 🔹 Private constructor (used by Builder)
    private UserSummaryResponse(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.role = builder.role;
        this.currentLevel = builder.currentLevel;
        this.currentStreak = builder.currentStreak;
        this.isEmailVerified = builder.isEmailVerified;
        this.isActive = builder.isActive;
        this.createdAt = builder.createdAt;
        this.lastLoginAt = builder.lastLoginAt;
    }

    // 🔹 Static builder() method
    public static Builder builder() {
        return new Builder();
    }

    // 🔹 Builder Class
    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private String role;

        private int currentLevel;
        private int currentStreak;

        private boolean isEmailVerified;
        private boolean isActive;

        private LocalDateTime createdAt;
        private LocalDateTime lastLoginAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder currentLevel(int currentLevel) {
            this.currentLevel = currentLevel;
            return this;
        }

        public Builder currentStreak(int currentStreak) {
            this.currentStreak = currentStreak;
            return this;
        }

        public Builder isEmailVerified(boolean isEmailVerified) {
            this.isEmailVerified = isEmailVerified;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        // 🔹 Build method
        public UserSummaryResponse build() {
            return new UserSummaryResponse(this);
        }
    }

    // 🔹 Getters (keep yours as-is or generate)

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public int getCurrentLevel() { return currentLevel; }
    public int getCurrentStreak() { return currentStreak; }
    public boolean isEmailVerified() { return isEmailVerified; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
}