// src/main/java/com/BrainBlitz/entity/SolutionStep.java

package com.BrainBlitz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "solution_steps")
public class SolutionStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coding_question_id", nullable = false)
    private CodingQuestion codingQuestion;

    @Column(nullable = false)
    private Integer stepNumber;             // ordering — 1, 2, 3...

    @Column(nullable = false)
    private String stepTitle;               // e.g. "Initialize the DP array"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String stepDescription;         // full explanation for this step

    @Column(columnDefinition = "TEXT")
    private String codeAtThisStep;          // partial/full code at this stage

    private String imageUrl;                // optional diagram for this step

	public SolutionStep(Long id, CodingQuestion codingQuestion, Integer stepNumber, String stepTitle,
			String stepDescription, String codeAtThisStep, String imageUrl) {
		super();
		this.id = id;
		this.codingQuestion = codingQuestion;
		this.stepNumber = stepNumber;
		this.stepTitle = stepTitle;
		this.stepDescription = stepDescription;
		this.codeAtThisStep = codeAtThisStep;
		this.imageUrl = imageUrl;
	}

	public SolutionStep() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CodingQuestion getCodingQuestion() {
		return codingQuestion;
	}

	public void setCodingQuestion(CodingQuestion codingQuestion) {
		this.codingQuestion = codingQuestion;
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