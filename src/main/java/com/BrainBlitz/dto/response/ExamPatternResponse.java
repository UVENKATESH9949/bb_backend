package com.BrainBlitz.dto.response;

import com.BrainBlitz.enums.*;
import java.time.LocalDateTime;

public class ExamPatternResponse {

    private Long id;

    private ExamType examType;

    private String roundName;

    private String subject;

    private String topic;

    private Integer questionsCount;

    private Double weightagePercent;

    private Integer easyCount;

    private Integer mediumCount;

    private Integer hardCount;

    private Double marksPerQuestion;

    private Double negativeMarks;

    private Integer timeLimitSeconds;

    private Boolean isCompulsory;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

	public ExamPatternResponse(Long id, ExamType examType, String subject, String topic, Integer questionsCount,
			Double weightagePercent, Integer easyCount, Integer mediumCount, Integer hardCount, Double marksPerQuestion,
			Double negativeMarks, Boolean isCompulsory, Boolean isActive, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.examType = examType;
		this.subject = subject;
		this.topic = topic;
		this.questionsCount = questionsCount;
		this.weightagePercent = weightagePercent;
		this.easyCount = easyCount;
		this.mediumCount = mediumCount;
		this.hardCount = hardCount;
		this.marksPerQuestion = marksPerQuestion;
		this.negativeMarks = negativeMarks;
		this.isCompulsory = isCompulsory;
		this.isActive = isActive;
		this.createdAt = createdAt;
	}

	public ExamPatternResponse() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ExamType getExamType() {
		return examType;
	}

	public void setExamType(ExamType examType) {
		this.examType = examType;
	}

	public String getRoundName() {
		return roundName;
	}

	public void setRoundName(String roundName) {
		this.roundName = roundName;
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

	public Integer getQuestionsCount() {
		return questionsCount;
	}

	public void setQuestionsCount(Integer questionsCount) {
		this.questionsCount = questionsCount;
	}

	public Double getWeightagePercent() {
		return weightagePercent;
	}

	public void setWeightagePercent(Double weightagePercent) {
		this.weightagePercent = weightagePercent;
	}

	public Integer getEasyCount() {
		return easyCount;
	}

	public void setEasyCount(Integer easyCount) {
		this.easyCount = easyCount;
	}

	public Integer getMediumCount() {
		return mediumCount;
	}

	public void setMediumCount(Integer mediumCount) {
		this.mediumCount = mediumCount;
	}

	public Integer getHardCount() {
		return hardCount;
	}

	public void setHardCount(Integer hardCount) {
		this.hardCount = hardCount;
	}

	public Double getMarksPerQuestion() {
		return marksPerQuestion;
	}

	public void setMarksPerQuestion(Double marksPerQuestion) {
		this.marksPerQuestion = marksPerQuestion;
	}

	public Double getNegativeMarks() {
		return negativeMarks;
	}

	public void setNegativeMarks(Double negativeMarks) {
		this.negativeMarks = negativeMarks;
	}

	public Integer getTimeLimitSeconds() {
		return timeLimitSeconds;
	}

	public void setTimeLimitSeconds(Integer timeLimitSeconds) {
		this.timeLimitSeconds = timeLimitSeconds;
	}

	public Boolean getIsCompulsory() {
		return isCompulsory;
	}

	public void setIsCompulsory(Boolean isCompulsory) {
		this.isCompulsory = isCompulsory;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    
    
    
}