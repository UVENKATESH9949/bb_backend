// src/main/java/com/BrainBlitz/entity/TestCase.java

package com.BrainBlitz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_cases")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coding_question_id", nullable = false)
    private CodingQuestion codingQuestion;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String input;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String expectedOutput;

    @Column(nullable = false)
    private Boolean isSample = false;       // shown to user as example

    @Column(nullable = false)
    private Boolean isHidden = false;       // hidden test cases for grading

    @Column(columnDefinition = "TEXT")
    private String explanation;             // why this input gives this output

    @Column(nullable = false)
    private Integer weightage = 1;          // marks weight for this test case

	public TestCase(Long id, CodingQuestion codingQuestion, String input, String expectedOutput, Boolean isSample,
			Boolean isHidden, String explanation, Integer weightage) {
		super();
		this.id = id;
		this.codingQuestion = codingQuestion;
		this.input = input;
		this.expectedOutput = expectedOutput;
		this.isSample = isSample;
		this.isHidden = isHidden;
		this.explanation = explanation;
		this.weightage = weightage;
	}

	public TestCase() {
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