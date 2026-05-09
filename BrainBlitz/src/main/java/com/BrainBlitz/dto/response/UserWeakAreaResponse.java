package com.BrainBlitz.dto.response;

import java.time.LocalDateTime;

public class UserWeakAreaResponse {

    private Long id;

    private Long userId;

    private String subject;

    private String topic;

    private Double accuracyPercentage;

    private Double avgTimeTaken;

    private Integer totalAttempted;

    private Boolean isWeak;

    private Boolean isSlow;

    private LocalDateTime lastUpdated;

	public UserWeakAreaResponse(Long id, Long userId, String subject, String topic, Double accuracyPercentage,
			Double avgTimeTaken, Integer totalAttempted) {
		super();
		this.id = id;
		this.userId = userId;
		this.subject = subject;
		this.topic = topic;
		this.accuracyPercentage = accuracyPercentage;
		this.avgTimeTaken = avgTimeTaken;
		this.totalAttempted = totalAttempted;
	}

	public UserWeakAreaResponse() {
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Double getAccuracyPercentage() {
		return accuracyPercentage;
	}

	public void setAccuracyPercentage(Double accuracyPercentage) {
		this.accuracyPercentage = accuracyPercentage;
	}

	public Double getAvgTimeTaken() {
		return avgTimeTaken;
	}

	public void setAvgTimeTaken(Double avgTimeTaken) {
		this.avgTimeTaken = avgTimeTaken;
	}

	public Integer getTotalAttempted() {
		return totalAttempted;
	}

	public void setTotalAttempted(Integer totalAttempted) {
		this.totalAttempted = totalAttempted;
	}

	public Boolean getIsWeak() {
		return isWeak;
	}

	public void setIsWeak(Boolean isWeak) {
		this.isWeak = isWeak;
	}

	public Boolean getIsSlow() {
		return isSlow;
	}

	public void setIsSlow(Boolean isSlow) {
		this.isSlow = isSlow;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
    
    
}