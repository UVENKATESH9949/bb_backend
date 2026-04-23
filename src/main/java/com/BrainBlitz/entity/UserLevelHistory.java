package com.BrainBlitz.entity;

import java.time.LocalDateTime;
import com.BrainBlitz.enums.AnswerResult;
import com.BrainBlitz.enums.DifficultyLevel;
import com.BrainBlitz.enums.LevelChangeReason;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;


@Entity
@Table(name = "user_level_history")
public class UserLevelHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Level before and after change
    @Column(nullable = false)
    private int previousLevel;

    @Column(nullable = false)
    private int newLevel;

    // Why did level change
    // MOCK_PERFORMANCE  → good or bad mock result
    // INACTIVITY        → user was inactive for X days
    // STREAK_BROKEN     → daily streak broken
    // STREAK_BONUS      → long streak reward
    // MANUAL_RESET      → admin manually reset
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LevelChangeReason reason;

    // Which mock caused this change
    // null if reason is INACTIVITY or STREAK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private MockSession mockSession;

    // Score that triggered this change
    // null if reason is INACTIVITY or STREAK
    @Column
    private Double triggerScore;

    // Human readable description
    // e.g. "Level increased from 3 to 4 after scoring 85% in SSC CGL mock"
    // e.g. "Level decreased from 4 to 3 due to 10 days inactivity"
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @PrePersist
    protected void onCreate() {
        this.changedAt = LocalDateTime.now();
    }

	public UserLevelHistory(Long id, User user, int previousLevel, int newLevel, LevelChangeReason reason,
			MockSession mockSession, Double triggerScore, String description, LocalDateTime changedAt) {
		super();
		this.id = id;
		this.user = user;
		this.previousLevel = previousLevel;
		this.newLevel = newLevel;
		this.reason = reason;
		this.mockSession = mockSession;
		this.triggerScore = triggerScore;
		this.description = description;
		this.changedAt = changedAt;
	}

	public UserLevelHistory() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getPreviousLevel() {
		return previousLevel;
	}

	public void setPreviousLevel(int previousLevel) {
		this.previousLevel = previousLevel;
	}

	public int getNewLevel() {
		return newLevel;
	}

	public void setNewLevel(int newLevel) {
		this.newLevel = newLevel;
	}

	public LevelChangeReason getReason() {
		return reason;
	}

	public void setReason(LevelChangeReason reason) {
		this.reason = reason;
	}

	public MockSession getMockSession() {
		return mockSession;
	}

	public void setMockSession(MockSession mockSession) {
		this.mockSession = mockSession;
	}

	public Double getTriggerScore() {
		return triggerScore;
	}

	public void setTriggerScore(Double triggerScore) {
		this.triggerScore = triggerScore;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(LocalDateTime changedAt) {
		this.changedAt = changedAt;
	}
    
    
}
