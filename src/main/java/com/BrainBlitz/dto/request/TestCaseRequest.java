// src/main/java/com/BrainBlitz/dto/request/TestCaseRequest.java

package com.BrainBlitz.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

public class TestCaseRequest {

    @NotBlank(message = "Input is required")
    private String input;

    @NotBlank(message = "Expected output is required")
    private String expectedOutput;

    private Boolean isSample = false;
    private Boolean isHidden = false;
    private String explanation;

    @Min(value = 1, message = "Weightage must be at least 1")
    private Integer weightage = 1;

	public TestCaseRequest(@NotBlank(message = "Input is required") String input,
			@NotBlank(message = "Expected output is required") String expectedOutput, Boolean isSample,
			Boolean isHidden, String explanation,
			@Min(value = 1, message = "Weightage must be at least 1") Integer weightage) {
		super();
		this.input = input;
		this.expectedOutput = expectedOutput;
		this.isSample = isSample;
		this.isHidden = isHidden;
		this.explanation = explanation;
		this.weightage = weightage;
	}

	public TestCaseRequest() {
		super();
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}

	public Boolean getIsSample() {
		return isSample;
	}

	public void setIsSample(Boolean isSample) {
		this.isSample = isSample;
	}

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public Integer getWeightage() {
		return weightage;
	}

	public void setWeightage(Integer weightage) {
		this.weightage = weightage;
	}
    
    
}