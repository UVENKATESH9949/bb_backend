// src/main/java/com/BrainBlitz/dto/request/ArrangementRequest.java

package com.BrainBlitz.dto.request;

import com.BrainBlitz.entity.QuestionExplanation;
import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;


public class ArrangementRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Arrangement type is required")
    private ArrangementType arrangementType;    // SENTENCE or PARAGRAPH

    @NotNull(message = "Exam category is required")
    private ExamCategory examCategory;

    @NotNull(message = "Exam type is required")
    private ExamType examType;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String topic;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficultyLevel;

    @NotNull(message = "Language is required")
    private Language language;

    private Double marks = 1.0;
    private Double negativeMarks = 0.0;
    private String hint;
    private QuestionExplanation questionExplanation;
    private Boolean isAiGenerated = false;

    @NotEmpty(message = "Segments are required")
    @Size(min = 3, max = 6, message = "Segments must be between 3 and 6")
    private List<String> segments;          // ["P: text...", "Q: text...", ...]

    @NotBlank(message = "Correct order is required")
    private String correctOrder;  
    // e.g. "QPRS" or "PRQS"

    private String approachExplanation; 
    
	public ArrangementRequest(@NotBlank(message = "Question text is required") String questionText,
			@NotNull(message = "Arrangement type is required") ArrangementType arrangementType,
			@NotNull(message = "Exam category is required") ExamCategory examCategory,
			@NotNull(message = "Exam type is required") ExamType examType,
			@NotBlank(message = "Subject is required") String subject, String topic,
			@NotNull(message = "Difficulty level is required") DifficultyLevel difficultyLevel,
			@NotNull(message = "Language is required") Language language, Double marks, Double negativeMarks,
			String hint, QuestionExplanation questionExplanation, Boolean isAiGenerated,
			@NotEmpty(message = "Segments are required") @Size(min = 3, max = 6, message = "Segments must be between 3 and 6") List<String> segments,
			@NotBlank(message = "Correct order is required") String correctOrder) {
		super();
		this.questionText = questionText;
		this.arrangementType = arrangementType;
		this.examCategory = examCategory;
		this.examType = examType;
		this.subject = subject;
		this.topic = topic;
		this.difficultyLevel = difficultyLevel;
		this.language = language;
		this.marks = marks;
		this.negativeMarks = negativeMarks;
		this.hint = hint;
		this.questionExplanation = questionExplanation;
		this.isAiGenerated = isAiGenerated;
		this.segments = segments;
		this.correctOrder = correctOrder;
	}
	
	public String getApproachExplanation() { return approachExplanation; }
	public void setApproachExplanation(String approachExplanation) { this.approachExplanation = approachExplanation; }
	
	public ArrangementRequest() {
		super();
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public ArrangementType getArrangementType() {
		return arrangementType;
	}
	public void setArrangementType(ArrangementType arrangementType) {
		this.arrangementType = arrangementType;
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
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	public QuestionExplanation getQuestionExplanation() {
		return questionExplanation;
	}

	public void setQuestionExplanation(QuestionExplanation questionExplanation) {
		this.questionExplanation = questionExplanation;
	}

	public Boolean getIsAiGenerated() {
		return isAiGenerated;
	}
	public void setIsAiGenerated(Boolean isAiGenerated) {
		this.isAiGenerated = isAiGenerated;
	}
	public List<String> getSegments() {
		return segments;
	}
	public void setSegments(List<String> segments) {
		this.segments = segments;
	}
	public String getCorrectOrder() {
		return correctOrder;
	}
	public void setCorrectOrder(String correctOrder) {
		this.correctOrder = correctOrder;
	}
    
    
}