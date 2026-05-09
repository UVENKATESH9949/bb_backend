package com.BrainBlitz.dto.request;

import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;

public class UserLevelHistoryRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Previous level is required")
    @Min(value = 1, message = "Level cannot be less than 1")
    private Integer previousLevel;

    @NotNull(message = "New level is required")
    @Min(value = 1, message = "Level cannot be less than 1")
    private Integer newLevel;

    @NotNull(message = "Reason is required")
    private LevelChangeReason reason;

    // null if reason is INACTIVITY or STREAK
    private Long sessionId;

    // null if reason is INACTIVITY or STREAK
    private Double triggerScore;

    private String description;

	public UserLevelHistoryRequest(@NotNull(message = "User ID is required") Long userId,
			@NotNull(message = "Previous level is required") @Min(value = 1, message = "Level cannot be less than 1") Integer previousLevel,
			@NotNull(message = "New level is required") @Min(value = 1, message = "Level cannot be less than 1") Integer newLevel,
			@NotNull(message = "Reason is required") LevelChangeReason reason) {
		super();
		this.userId = userId;
		this.previousLevel = previousLevel;
		this.newLevel = newLevel;
		this.reason = reason;
	}

	public UserLevelHistoryRequest() {
		super();
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
    
    
}