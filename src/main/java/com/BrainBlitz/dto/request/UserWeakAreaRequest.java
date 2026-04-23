package com.BrainBlitz.dto.request;

import jakarta.validation.constraints.*;

public class UserWeakAreaRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Topic is required")
    private String topic;

    @NotNull(message = "Accuracy percentage is required")
    @DecimalMin(value = "0.0", message = "Accuracy cannot be negative")
    @DecimalMax(value = "100.0", message = "Accuracy cannot exceed 100")
    private Double accuracyPercentage;

    @NotNull(message = "Average time taken is required")
    @DecimalMin(value = "0.0", message = "Average time cannot be negative")
    private Double avgTimeTaken;

    @NotNull(message = "Total attempted is required")
    @Min(value = 0, message = "Total attempted cannot be negative")
    private Integer totalAttempted;

	public UserWeakAreaRequest(@NotNull(message = "User ID is required") Long userId,
			@NotBlank(message = "Subject is required") String subject,
			@NotBlank(message = "Topic is required") String topic,
			@NotNull(message = "Accuracy percentage is required") @DecimalMin(value = "0.0", message = "Accuracy cannot be negative") @DecimalMax(value = "100.0", message = "Accuracy cannot exceed 100") Double accuracyPercentage,
			@NotNull(message = "Average time taken is required") @DecimalMin(value = "0.0", message = "Average time cannot be negative") Double avgTimeTaken,
			@NotNull(message = "Total attempted is required") @Min(value = 0, message = "Total attempted cannot be negative") Integer totalAttempted) {
		super();
		this.userId = userId;
		this.subject = subject;
		this.topic = topic;
		this.accuracyPercentage = accuracyPercentage;
		this.avgTimeTaken = avgTimeTaken;
		this.totalAttempted = totalAttempted;
	}

	public UserWeakAreaRequest() {
		super();
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
    
    
}