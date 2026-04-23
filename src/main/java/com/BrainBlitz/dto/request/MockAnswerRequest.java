package com.BrainBlitz.dto.request;

import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;

public class MockAnswerRequest {

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotNull(message = "Question ID is required")
    private Long questionId;

    // null = question was skipped
    private String userAnswer;

    @NotNull(message = "Time taken is required")
    @Min(value = 0, message = "Time taken cannot be negative")
    private Integer timeTakenSeconds;

    @NotNull(message = "Attempted flag is required")
    private Boolean isAttempted;

    private Boolean isMarkedReview = false;

	public MockAnswerRequest(@NotNull(message = "Session ID is required") Long sessionId,
			@NotNull(message = "Question ID is required") Long questionId,
			@NotNull(message = "Time taken is required") @Min(value = 0, message = "Time taken cannot be negative") Integer timeTakenSeconds,
			@NotNull(message = "Attempted flag is required") Boolean isAttempted) {
		super();
		this.sessionId = sessionId;
		this.questionId = questionId;
		this.timeTakenSeconds = timeTakenSeconds;
		this.isAttempted = isAttempted;
	}

	public MockAnswerRequest() {
		super();
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public Integer getTimeTakenSeconds() {
		return timeTakenSeconds;
	}

	public void setTimeTakenSeconds(Integer timeTakenSeconds) {
		this.timeTakenSeconds = timeTakenSeconds;
	}

	public Boolean getIsAttempted() {
		return isAttempted;
	}

	public void setIsAttempted(Boolean isAttempted) {
		this.isAttempted = isAttempted;
	}

	public Boolean getIsMarkedReview() {
		return isMarkedReview;
	}

	public void setIsMarkedReview(Boolean isMarkedReview) {
		this.isMarkedReview = isMarkedReview;
	}
    
    
}