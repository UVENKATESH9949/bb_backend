package com.BrainBlitz.dto.request;

import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;

public class MockSessionRequest {

    @NotNull(message = "Exam type is required")
    private ExamType examType;

    @NotNull(message = "Mock source is required")
    private MockSource mockSource;      // DB or AI

    // For placement exams — which round to start
    // null for govt exams
    private String roundName;

    // Total questions user wants in this mock
    // If null — system decides based on exam pattern
    @Min(value = 5, message = "Minimum 5 questions required")
    @Max(value = 100, message = "Maximum 100 questions allowed")
    private Integer totalQuestions;

	public MockSessionRequest(@NotNull(message = "Exam type is required") ExamType examType,
			@NotNull(message = "Mock source is required") MockSource mockSource, String roundName,
			@Min(value = 5, message = "Minimum 5 questions required") @Max(value = 100, message = "Maximum 100 questions allowed") Integer totalQuestions) {
		super();
		this.examType = examType;
		this.mockSource = mockSource;
		this.roundName = roundName;
		this.totalQuestions = totalQuestions;
	}

	public MockSessionRequest() {
		super();
	}

	public ExamType getExamType() {
		return examType;
	}

	public void setExamType(ExamType examType) {
		this.examType = examType;
	}

	public MockSource getMockSource() {
		return mockSource;
	}

	public void setMockSource(MockSource mockSource) {
		this.mockSource = mockSource;
	}

	public String getRoundName() {
		return roundName;
	}

	public void setRoundName(String roundName) {
		this.roundName = roundName;
	}

	public Integer getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
    
    
}