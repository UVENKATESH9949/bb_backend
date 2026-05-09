package com.BrainBlitz.dto.request;

import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;

public class UserSeenQuestionRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotNull(message = "Result is required")
    private AnswerResult result;    // CORRECT, WRONG, SKIPPED

	public UserSeenQuestionRequest(@NotNull(message = "User ID is required") Long userId,
			@NotNull(message = "Question ID is required") Long questionId,
			@NotNull(message = "Session ID is required") Long sessionId,
			@NotNull(message = "Result is required") AnswerResult result) {
		super();
		this.userId = userId;
		this.questionId = questionId;
		this.sessionId = sessionId;
		this.result = result;
	}

	public UserSeenQuestionRequest() {
		super();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public AnswerResult getResult() {
		return result;
	}

	public void setResult(AnswerResult result) {
		this.result = result;
	}
    
    
}