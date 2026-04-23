package com.BrainBlitz.dto.response;

import com.BrainBlitz.enums.*;
import java.time.LocalDateTime;

public class MockAnswerResponse {

    private Long id;

    private Long sessionId;

    private Long questionId;

    private String subject;

    private String topic;

    private DifficultyLevel difficultyLevel;

    private String userAnswer;

    private AnswerResult result;

    private Double marksAwarded;

    private Integer timeTakenSeconds;

    private Boolean isAttempted;

    private Boolean isMarkedReview;

    // AI evaluation fields
    // null for non writing/speech questions
    private Integer aiScore;

    private String aiFeedback;

    private Boolean isAiEvaluated;

    private LocalDateTime createdAt;

	public MockAnswerResponse(Long id, Long sessionId, Long questionId, String subject, String topic,
			DifficultyLevel difficultyLevel, String userAnswer, Integer timeTakenSeconds, Boolean isAttempted,
			Boolean isMarkedReview, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.sessionId = sessionId;
		this.questionId = questionId;
		this.subject = subject;
		this.topic = topic;
		this.difficultyLevel = difficultyLevel;
		this.userAnswer = userAnswer;
		this.timeTakenSeconds = timeTakenSeconds;
		this.isAttempted = isAttempted;
		this.isMarkedReview = isMarkedReview;
		this.createdAt = createdAt;
	}

	public MockAnswerResponse() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public AnswerResult getResult() {
		return result;
	}

	public void setResult(AnswerResult result) {
		this.result = result;
	}

	public Double getMarksAwarded() {
		return marksAwarded;
	}

	public void setMarksAwarded(Double marksAwarded) {
		this.marksAwarded = marksAwarded;
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

	public Integer getAiScore() {
		return aiScore;
	}

	public void setAiScore(Integer aiScore) {
		this.aiScore = aiScore;
	}

	public String getAiFeedback() {
		return aiFeedback;
	}

	public void setAiFeedback(String aiFeedback) {
		this.aiFeedback = aiFeedback;
	}

	public Boolean getIsAiEvaluated() {
		return isAiEvaluated;
	}

	public void setIsAiEvaluated(Boolean isAiEvaluated) {
		this.isAiEvaluated = isAiEvaluated;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}