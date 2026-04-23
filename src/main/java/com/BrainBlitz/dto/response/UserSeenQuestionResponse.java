package com.BrainBlitz.dto.response;

import com.BrainBlitz.enums.*;
import java.time.LocalDateTime;

public class UserSeenQuestionResponse {

    private Long id;

    private Long userId;

    private Long questionId;

    private Long sessionId;

    private AnswerResult result;

    private LocalDateTime seenAt;

	public UserSeenQuestionResponse(Long id, Long userId, Long questionId, Long sessionId, AnswerResult result) {
		super();
		this.id = id;
		this.userId = userId;
		this.questionId = questionId;
		this.sessionId = sessionId;
		this.result = result;
	}

	public UserSeenQuestionResponse() {
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

	public LocalDateTime getSeenAt() {
		return seenAt;
	}

	public void setSeenAt(LocalDateTime seenAt) {
		this.seenAt = seenAt;
	}
    
    
}