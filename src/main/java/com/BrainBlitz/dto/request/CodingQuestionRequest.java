// src/main/java/com/BrainBlitz/dto/request/CodingQuestionRequest.java

package com.BrainBlitz.dto.request;

import com.BrainBlitz.entity.QuestionExplanation;
import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

public class CodingQuestionRequest {

    @NotBlank(message = "Problem statement is required")
    private String problemStatement;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;      // CODE_WRITE or CODE_DEBUG

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

    private String inputFormat;
    private String outputFormat;
    private String constraints;

    // Starter code shown to user in editor
    private String codeSnippet;

    // Admin's correct solution — not shown to user
    private String solutionCode;

    @NotEmpty(message = "At least one language is required")
    private List<ProgrammingLanguage> supportedLanguages;

    private String timeComplexity;          // e.g. "O(n log n)"
    private String spaceComplexity;         // e.g. "O(n)"

    // Explanation fields for the approach
    private QuestionExplanation questionExplanation;
    
	public CodingQuestionRequest(@NotBlank(message = "Problem statement is required") String problemStatement,
			@NotNull(message = "Question type is required") QuestionType questionType,
			@NotNull(message = "Exam category is required") ExamCategory examCategory,
			@NotNull(message = "Exam type is required") ExamType examType,
			@NotBlank(message = "Subject is required") String subject, String topic,
			@NotNull(message = "Difficulty level is required") DifficultyLevel difficultyLevel, Double marks,
			Double negativeMarks, String hint, Boolean isAiGenerated, String inputFormat,
			String outputFormat, String constraints, String codeSnippet, String solutionCode,
			@NotEmpty(message = "At least one language is required") List<ProgrammingLanguage> supportedLanguages,
			String timeComplexity, String spaceComplexity,QuestionExplanation questionExplanation) {
		super();
		this.problemStatement = problemStatement;
		this.questionType = questionType;
		this.examCategory = examCategory;
		this.examType = examType;
		this.subject = subject;
		this.topic = topic;
		this.difficultyLevel = difficultyLevel;
		this.marks = marks;
		this.negativeMarks = negativeMarks;
		this.hint = hint;
		this.questionExplanation = questionExplanation;
		this.isAiGenerated = isAiGenerated;
		this.inputFormat = inputFormat;
		this.outputFormat = outputFormat;
		this.constraints = constraints;
		this.codeSnippet = codeSnippet;
		this.solutionCode = solutionCode;
		this.supportedLanguages = supportedLanguages;
		this.timeComplexity = timeComplexity;
		this.spaceComplexity = spaceComplexity;
		
	}

	public CodingQuestionRequest() {
		super();
	}

	public String getProblemStatement() {
		return problemStatement;
	}

	public void setProblemStatement(String problemStatement) {
		this.problemStatement = problemStatement;
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

	public String getInputFormat() {
		return inputFormat;
	}

	public void setInputFormat(String inputFormat) {
		this.inputFormat = inputFormat;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public String getConstraints() {
		return constraints;
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}

	public String getCodeSnippet() {
		return codeSnippet;
	}

	public void setCodeSnippet(String codeSnippet) {
		this.codeSnippet = codeSnippet;
	}

	public String getSolutionCode() {
		return solutionCode;
	}

	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
	}

	public List<ProgrammingLanguage> getSupportedLanguages() {
		return supportedLanguages;
	}

	public void setSupportedLanguages(List<ProgrammingLanguage> supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}

	public String getTimeComplexity() {
		return timeComplexity;
	}

	public void setTimeComplexity(String timeComplexity) {
		this.timeComplexity = timeComplexity;
	}

	public String getSpaceComplexity() {
		return spaceComplexity;
	}

	public void setSpaceComplexity(String spaceComplexity) {
		this.spaceComplexity = spaceComplexity;
	}

	public QuestionExplanation getQuestionExplanation() {
		return questionExplanation;
	}

	public void setQuestionExplanation(QuestionExplanation questionExplanation) {
		this.questionExplanation = questionExplanation;
	}

	
    
    
}