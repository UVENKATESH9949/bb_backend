package com.BrainBlitz.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.BrainBlitz.entity.Role;

public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private double score;
    private Role role;
    private boolean isEmailVerified;
    private List<String> targetExams;
    private int level;
    private int streak;
    private boolean isActive;
    private LocalDateTime lastActiveDate;
    private LocalDateTime lastMockDate;
    private int totalMocksAttempted;
    

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public Role getRole() { return role; }
    public void setRole(Role role2) { this.role = role2; }

    public boolean isEmailVerified() { return isEmailVerified; }
    public void setEmailVerified(boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }

    public List<String> getTargetExams() { return targetExams; }
    public void setTargetExams(List<String> targetExams) { this.targetExams = targetExams; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getLastActiveDate() { return lastActiveDate; }
    public void setLastActiveDate(LocalDateTime lastActiveDate) { this.lastActiveDate = lastActiveDate; }

    public LocalDateTime getLastMockDate() { return lastMockDate; }
    public void setLastMockDate(LocalDateTime lastMockDate) { this.lastMockDate = lastMockDate; }

    public int getTotalMocksAttempted() { return totalMocksAttempted; }
    public void setTotalMocksAttempted(int totalMocksAttempted) { this.totalMocksAttempted = totalMocksAttempted; }
	public UserResponseDto(Long id, String name, String email, double score, Role role, boolean isEmailVerified,
			List<String> targetExams, int level, int streak, boolean isActive, LocalDateTime lastActiveDate,
			LocalDateTime lastMockDate, int totalMocksAttempted) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.score = score;
		this.role = role;
		this.isEmailVerified = isEmailVerified;
		this.targetExams = targetExams;
		this.level = level;
		this.streak = streak;
		this.isActive = isActive;
		this.lastActiveDate = lastActiveDate;
		this.lastMockDate = lastMockDate;
		this.totalMocksAttempted = totalMocksAttempted;
	}
	public UserResponseDto() {
		super();
	}
    
    
}