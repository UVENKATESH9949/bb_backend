package com.BrainBlitz.dto.request;

import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;

public class ExamPatternRequest {

    @NotNull(message = "Exam type is required")
    private ExamType examType;

    private String roundName;           // null for govt exams

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Topic is required")
    private String topic;

    @NotNull(message = "Questions count is required")
    @Min(value = 1, message = "At least 1 question required")
    private Integer questionsCount;

    @NotNull(message = "Weightage is required")
    @DecimalMin(value = "0.1", message = "Weightage must be greater than 0")
    private Double weightagePercent;

    @NotNull(message = "Easy count is required")
    @Min(value = 0)
    private Integer easyCount;

    @NotNull(message = "Medium count is required")
    @Min(value = 0)
    private Integer mediumCount;

    @NotNull(message = "Hard count is required")
    @Min(value = 0)
    private Integer hardCount;

    @NotNull(message = "Marks per question is required")
    @DecimalMin(value = "0.0")
    private Double marksPerQuestion;

    @NotNull(message = "Negative marks is required")
    @DecimalMin(value = "0.0")
    private Double negativeMarks;

    private Integer timeLimitSeconds;   // null = no separate time limit

    private Boolean isCompulsory = true;

	public ExamPatternRequest(@NotNull(message = "Exam type is required") ExamType examType,
			@NotBlank(message = "Subject is required") String subject,
			@NotBlank(message = "Topic is required") String topic,
			@NotNull(message = "Questions count is required") @Min(value = 1, message = "At least 1 question required") Integer questionsCount,
			@NotNull(message = "Weightage is required") @DecimalMin(value = "0.1", message = "Weightage must be greater than 0") Double weightagePercent,
			@NotNull(message = "Easy count is required") @Min(0) Integer easyCount,
			@NotNull(message = "Medium count is required") @Min(0) Integer mediumCount,
			@NotNull(message = "Hard count is required") @Min(0) Integer hardCount,
			@NotNull(message = "Marks per question is required") @DecimalMin("0.0") Double marksPerQuestion,
			@NotNull(message = "Negative marks is required") @DecimalMin("0.0") Double negativeMarks) {
		super();
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
	}

	public ExamPatternRequest() {
		super();
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
    
    
}