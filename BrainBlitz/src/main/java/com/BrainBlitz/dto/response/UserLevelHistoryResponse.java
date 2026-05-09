package com.BrainBlitz.dto.response;

import com.BrainBlitz.enums.*;
import java.time.LocalDateTime;

public class UserLevelHistoryResponse {

    private Long id;

    private Long userId;

    private Integer previousLevel;

    private Integer newLevel;

    private LevelChangeReason reason;

    private Long sessionId;

    private Double triggerScore;

    private String description;

    private LocalDateTime changedAt;

	public UserLevelHistoryResponse(Long id, Long userId, Integer previousLevel, Integer newLevel,
			LevelChangeReason reason) {
		super();
		this.id = id;
		this.userId = userId;
		this.previousLevel = previousLevel;
		this.newLevel = newLevel;
		this.reason = reason;
	}

	public UserLevelHistoryResponse() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getPreviousLevel() {
		return previousLevel;
	}

	public void setPreviousLevel(Integer previousLevel) {
		this.previousLevel = previousLevel;
	}

	public Integer getNewLevel() {
		return newLevel;
	}

	public void setNewLevel(Integer newLevel) {
		this.newLevel = newLevel;
	}

	public LevelChangeReason getReason() {
		return reason;
	}

	public void setReason(LevelChangeReason reason) {
		this.reason = reason;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
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