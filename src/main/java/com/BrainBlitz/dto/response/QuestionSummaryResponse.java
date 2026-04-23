// src/main/java/com/BrainBlitz/dto/response/QuestionSummaryResponse.java

package com.BrainBlitz.dto.response;

import com.BrainBlitz.entity.QuestionExplanation;
import com.BrainBlitz.enums.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


public class QuestionSummaryResponse {

    private Long id;
    private String questionText;            // truncated on frontend if too long
    private QuestionType questionType;
    private ExamCategory examCategory;
    private ExamType examType;
    private String subject;
    private String topic;
    private DifficultyLevel difficultyLevel;
    private Language language;
    private Double marks;
    private Double negativeMarks;
    private Boolean isActive;
    private Boolean isAiGenerated;
    private Long groupId;                   // non-null if part of RC/DI group
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer timeLimitSeconds;
    private List<String> options;        // MCQ choices
    private List<Boolean> correctOptions; // correct option indexes
    private QuestionExplanation explanation;
    private String hint;
    
	public QuestionSummaryResponse(Long id, String questionText, QuestionType questionType, ExamCategory examCategory,
			ExamType examType, String subject, String topic, DifficultyLevel difficultyLevel, Language language,
			Double marks, Double negativeMarks, Boolean isActive, Boolean isAiGenerated, Long groupId,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.questionText = questionText;
		this.questionType = questionType;
		this.examCategory = examCategory;
		this.examType = examType;
		this.subject = subject;
		this.topic = topic;
		this.difficultyLevel = difficultyLevel;
		this.language = language;
		this.marks = marks;
		this.negativeMarks = negativeMarks;
		this.isActive = isActive;
		this.isAiGenerated = isAiGenerated;
		this.groupId = groupId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public QuestionSummaryResponse() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public QuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	public ExamCategory getExamCategory() {
		return examCategory;
	}
	public void setExamCategory(ExamCategory examCategory) {
		this.examCategory = examCategory;
	}
	public ExamType getExamType() {
		return examType;
	}
	public void setExamType(ExamType examType) {
		this.examType = examType;
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
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public Double getMarks() {
		return marks;
	}
	public void setMarks(Double marks) {
		this.marks = marks;
	}
	public Double getNegativeMarks() {
		return negativeMarks;
	}
	public void setNegativeMarks(Double negativeMarks) {
		this.negativeMarks = negativeMarks;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsAiGenerated() {
		return isAiGenerated;
	}
	public void setIsAiGenerated(Boolean isAiGenerated) {
		this.isAiGenerated = isAiGenerated;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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
    
	// Add getter
	public Integer getTimeLimitSeconds() {
	    return timeLimitSeconds;
	}

	// Add setter
	public void setTimeLimitSeconds(Integer timeLimitSeconds) {
	    this.timeLimitSeconds = timeLimitSeconds;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public List<Boolean>  getCorrectOptions() {
		return correctOptions;
	}
	public void setCorrectOptions(List<Boolean> correctOptions) {
		this.correctOptions = correctOptions;
	}
	public QuestionExplanation getExplanation() {
		return explanation;
	}
	public void setExplanation(QuestionExplanation explanation) {
		this.explanation = explanation;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	
    
}