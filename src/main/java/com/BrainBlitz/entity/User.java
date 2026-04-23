package com.BrainBlitz.entity;

import java.time.LocalDateTime;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private double score;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isEmailVerified;
    private int otp;
    private int otpNumberOfAttempts;
    private String passwordResetToken;
    private LocalDateTime passwordResetExpiry;
    private LocalDateTime otpExpiry;
    private int level;
    private int streak;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastActiveDate;
    private LocalDateTime lastMockDate;
    private int totalMocksAttempted;
    @Column
    private String currentToken;
    
    @ElementCollection
    private List<String> targetExams;

    // ✅ Manually written getters & setters — no Lombok needed
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;  // ✅ correct
    }

    public boolean isEmailVerified() { return isEmailVerified; }
    public void setEmailVerified(boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }

    public int getOtp() { return otp; }
    public void setOtp(int otp) { this.otp = otp; }

    public int getOtpNumberOfAttempts() { return otpNumberOfAttempts; }
    public void setOtpNumberOfAttempts(int otpNumberOfAttempts) { this.otpNumberOfAttempts = otpNumberOfAttempts; }

    public String getPasswordResetToken() { return passwordResetToken; }
    public void setPasswordResetToken(String passwordResetToken) { this.passwordResetToken = passwordResetToken; }

    public LocalDateTime getPasswordResetExpiry() { return passwordResetExpiry; }
    public void setPasswordResetExpiry(LocalDateTime passwordResetExpiry) { this.passwordResetExpiry = passwordResetExpiry; }

    public LocalDateTime getOtpExpiry() { return otpExpiry; }
    public void setOtpExpiry(LocalDateTime otpExpiry) { this.otpExpiry = otpExpiry; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<String> getTargetExams() { return targetExams; }
    public void setTargetExams(List<String> targetExams) { this.targetExams = targetExams; }

    
    public LocalDateTime getLastActiveDate() {
		return lastActiveDate;
	}
	public void setLastActiveDate(LocalDateTime lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}
	public LocalDateTime getLastMockDate() {
		return lastMockDate;
	}
	public void setLastMockDate(LocalDateTime lastMockDate) {
		this.lastMockDate = lastMockDate;
	}
	public int getTotalMocksAttempted() {
		return totalMocksAttempted;
	}
	public void setTotalMocksAttempted(int totalMocksAttempted) {
		this.totalMocksAttempted = totalMocksAttempted;
	}
	
	
	public String getCurrentToken() {
		return currentToken;
	}
	public void setCurrentToken(String currentToken) {
		this.currentToken = currentToken;
	}
	// ✅ Constructor
    public User() {}
	public User(String name, String email, String password, List<String> targetExams) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.targetExams = targetExams;
	}

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
