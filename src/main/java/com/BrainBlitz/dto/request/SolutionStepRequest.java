// src/main/java/com/BrainBlitz/dto/request/SolutionStepRequest.java

package com.BrainBlitz.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

public class SolutionStepRequest {

    @NotNull(message = "Step number is required")
    private Integer stepNumber;

    @NotBlank(message = "Step title is required")
    private String stepTitle;

    @NotBlank(message = "Step description is required")
    private String stepDescription;

    private String codeAtThisStep;
    private String imageUrl;
    
	public SolutionStepRequest(@NotNull(message = "Step number is required") Integer stepNumber,
			@NotBlank(message = "Step title is required") String stepTitle,
			@NotBlank(message = "Step description is required") String stepDescription, String codeAtThisStep,
			String imageUrl) {
		super();
		this.stepNumber = stepNumber;
		this.stepTitle = stepTitle;
		this.stepDescription = stepDescription;
		this.codeAtThisStep = codeAtThisStep;
		this.imageUrl = imageUrl;
	}

	public SolutionStepRequest() {
		super();
	}

	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}

	public String getStepTitle() {
		return stepTitle;
	}

	public void setStepTitle(String stepTitle) {
		this.stepTitle = stepTitle;
	}

	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	public String getCodeAtThisStep() {
		return codeAtThisStep;
	}

	public void setCodeAtThisStep(String codeAtThisStep) {
		this.codeAtThisStep = codeAtThisStep;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
    
    
}