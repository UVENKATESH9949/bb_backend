// src/main/java/com/BrainBlitz/dto/request/CodeSnippetRequest.java

package com.BrainBlitz.dto.request;

import com.BrainBlitz.dto.request.McqQuestionRequest.McqOptionRequest;
import com.BrainBlitz.entity.QuestionExplanation;
import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

public class CodeSnippetRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;      // CODE_OUTPUT, CODE_FILL, SQL_OUTPUT etc.

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
    private QuestionExplanation questionExplanation;
    private Boolean isAiGenerated = false;

    @NotBlank(message = "Code snippet is required")
    private String codeSnippet;

    @NotNull(message = "Programming language is required")
    private ProgrammingLanguage programmingLanguage;

    // SQL specific
    private String sqlTableDataJson;
    private String sqlQuery;

    // code_fill specific
    private Integer blankLineNumber;
    private List<String> acceptedAnswers;

    // flowchart_mcq specific
    private String flowchartImageUrl;

    // MCQ options — for code_output, sql_output, flowchart_mcq, code_debug (MCQ)
    private List<McqQuestionRequest.McqOptionRequest> options;

	public CodeSnippetRequest(@NotBlank(message = "Question text is required") String questionText,
			@NotNull(message = "Question type is required") QuestionType questionType,
			@NotNull(message = "Exam category is required") ExamCategory examCategory,
			@NotNull(message = "Exam type is required") ExamType examType,
			@NotBlank(message = "Subject is required") String subject, String topic,
			@NotNull(message = "Difficulty level is required") DifficultyLevel difficultyLevel, Double marks,
			Double negativeMarks, String hint, QuestionExplanation questionExplanation, Boolean isAiGenerated,
			@NotBlank(message = "Code snippet is required") String codeSnippet,
			@NotNull(message = "Programming language is required") ProgrammingLanguage programmingLanguage,
			String sqlTableDataJson, String sqlQuery, Integer blankLineNumber, List<String> acceptedAnswers,
			String flowchartImageUrl, List<McqOptionRequest> options) {
		super();
		this.questionText = questionText;
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
		this.codeSnippet = codeSnippet;
		this.programmingLanguage = programmingLanguage;
		this.sqlTableDataJson = sqlTableDataJson;
		this.sqlQuery = sqlQuery;
		this.blankLineNumber = blankLineNumber;
		this.acceptedAnswers = acceptedAnswers;
		this.flowchartImageUrl = flowchartImageUrl;
		this.options = options;
	}

	public CodeSnippetRequest() {
		super();
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

	public String getCodeSnippet() {
		return codeSnippet;
	}

	public void setCodeSnippet(String codeSnippet) {
		this.codeSnippet = codeSnippet;
	}

	public ProgrammingLanguage getProgrammingLanguage() {
		return programmingLanguage;
	}

	public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
		this.programmingLanguage = programmingLanguage;
	}

	public String getSqlTableDataJson() {
		return sqlTableDataJson;
	}

	public void setSqlTableDataJson(String sqlTableDataJson) {
		this.sqlTableDataJson = sqlTableDataJson;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public Integer getBlankLineNumber() {
		return blankLineNumber;
	}

	public void setBlankLineNumber(Integer blankLineNumber) {
		this.blankLineNumber = blankLineNumber;
	}

	public List<String> getAcceptedAnswers() {
		return acceptedAnswers;
	}

	public void setAcceptedAnswers(List<String> acceptedAnswers) {
		this.acceptedAnswers = acceptedAnswers;
	}

	public String getFlowchartImageUrl() {
		return flowchartImageUrl;
	}

	public void setFlowchartImageUrl(String flowchartImageUrl) {
		this.flowchartImageUrl = flowchartImageUrl;
	}

	public List<McqQuestionRequest.McqOptionRequest> getOptions() {
		return options;
	}

	public void setOptions(List<McqQuestionRequest.McqOptionRequest> options) {
		this.options = options;
	}
    
    
}