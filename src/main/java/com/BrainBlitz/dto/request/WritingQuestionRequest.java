// src/main/java/com/BrainBlitz/dto/request/WritingQuestionRequest.java

package com.BrainBlitz.dto.request;

import com.BrainBlitz.entity.QuestionExplanation;
import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

public class WritingQuestionRequest {

    @NotBlank(message = "Prompt is required")
    private String prompt;

    @NotNull(message = "Writing type is required")
    private WritingType writingType;        // MAIL, ESSAY, SPEECH, LISTENING

    @NotNull(message = "Exam category is required")
    private ExamCategory examCategory;

    @NotNull(message = "Exam type is required")
    private ExamType examType;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String topic;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficultyLevel;

    private Double marks = 1.0;
    private Double negativeMarks = 0.0;
    private String hint;
    private Boolean isAiGenerated = false;

    private Integer minWords;
    private Integer maxWords;

    // JSON — e.g. {"tone":"formal","format":"subject+body+closing"}
    private String evaluationCriteriaJson;

    private String sampleAnswer;

    // listening_comp only
    private String audioUrl;

    // speech_round only
    private Integer speechDurationSeconds;

    private QuestionExplanation questionExplanation;
    
	public WritingQuestionRequest(@NotBlank(message = "Prompt is required") String prompt,
			@NotNull(message = "Writing type is required") WritingType writingType,
			@NotNull(message = "Exam category is required") ExamCategory examCategory,
			@NotNull(message = "Exam type is required") ExamType examType,
			@NotBlank(message = "Subject is required") String subject, String topic,
			@NotNull(message = "Difficulty level is required") DifficultyLevel difficultyLevel, Double marks,
			Double negativeMarks, String hint, Boolean isAiGenerated, Integer minWords, Integer maxWords,
			String evaluationCriteriaJson, String sampleAnswer, String audioUrl, Integer speechDurationSeconds, QuestionExplanation questionExplanation) {
		super();
		this.questionExplanation = questionExplanation;
		this.prompt = prompt;
		this.writingType = writingType;
		this.examCategory = examCategory;
		this.examType = examType;
		this.subject = subject;
		this.topic = topic;
		this.difficultyLevel = difficultyLevel;
		this.marks = marks;
		this.negativeMarks = negativeMarks;
		this.hint = hint;
		this.isAiGenerated = isAiGenerated;
		this.minWords = minWords;
		this.maxWords = maxWords;
		this.evaluationCriteriaJson = evaluationCriteriaJson;
		this.sampleAnswer = sampleAnswer;
		this.audioUrl = audioUrl;
		this.speechDurationSeconds = speechDurationSeconds;
	}

	public WritingQuestionRequest() {
		super();
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public WritingType getWritingType() {
		return writingType;
	}

	public void setWritingType(WritingType writingType) {
		this.writingType = writingType;
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

	public Boolean getIsAiGenerated() {
		return isAiGenerated;
	}

	public void setIsAiGenerated(Boolean isAiGenerated) {
		this.isAiGenerated = isAiGenerated;
	}

	public Integer getMinWords() {
		return minWords;
	}

	public void setMinWords(Integer minWords) {
		this.minWords = minWords;
	}

	public Integer getMaxWords() {
		return maxWords;
	}

	public void setMaxWords(Integer maxWords) {
		this.maxWords = maxWords;
	}

	public String getEvaluationCriteriaJson() {
		return evaluationCriteriaJson;
	}

	public void setEvaluationCriteriaJson(String evaluationCriteriaJson) {
		this.evaluationCriteriaJson = evaluationCriteriaJson;
	}

	public String getSampleAnswer() {
		return sampleAnswer;
	}

	public void setSampleAnswer(String sampleAnswer) {
		this.sampleAnswer = sampleAnswer;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public Integer getSpeechDurationSeconds() {
		return speechDurationSeconds;
	}

	public void setSpeechDurationSeconds(Integer speechDurationSeconds) {
		this.speechDurationSeconds = speechDurationSeconds;
	}

	public QuestionExplanation getQuestionExplanation() {
		return questionExplanation;
	}

	public void setQuestionExplanation(QuestionExplanation questionExplanation) {
		this.questionExplanation = questionExplanation;
	}

	
    
    
}