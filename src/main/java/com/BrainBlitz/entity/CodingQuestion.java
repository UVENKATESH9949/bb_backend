package com.BrainBlitz.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "coding_questions")
public class CodingQuestion {

    // ─────────────────────────────────────────────
    // PRIMARY KEY
    // ─────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─────────────────────────────────────────────
    // PARENT REFERENCE
    // ─────────────────────────────────────────────

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    @JsonIgnore
    private Question question;

    // ─────────────────────────────────────────────
    // PROBLEM STATEMENT
    // ─────────────────────────────────────────────

    // Full scenario-based problem description
    // Supports markdown — can include bold, code blocks, lists
    // e.g. "A company has N employees. Each employee has a
    //       salary stored in an array. Find the second highest
    //       salary without using sorting..."
    @Column(nullable = false, columnDefinition = "TEXT")
    private String problemStatement;

    @Column(columnDefinition = "TEXT")
    private String problemStatementHindi;

    // ─────────────────────────────────────────────
    // INPUT / OUTPUT FORMAT
    // ─────────────────────────────────────────────

    // Describes how input will be provided
    // e.g. "First line contains N (size of array)
    //       Second line contains N space-separated integers"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String inputFormat;

    // Describes expected output format
    // e.g. "Print a single integer — the second highest salary"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String outputFormat;

    // ─────────────────────────────────────────────
    // CONSTRAINTS
    // ─────────────────────────────────────────────

    // Mathematical constraints on input values
    // e.g. "1 ≤ N ≤ 10^5
    //       1 ≤ salary[i] ≤ 10^9
    //       All salaries are distinct"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String constraints;

    // ─────────────────────────────────────────────
    // CODE STARTER SNIPPET
    // ─────────────────────────────────────────────

    // Pre-written starter code given to user
    // JSON map of language → starter code
    // e.g. {
    //   "JAVA": "import java.util.*;\npublic class Solution {\n    public static void main(String[] args) {\n        // Write your code here\n    }\n}",
    //   "PYTHON": "def solution(arr):\n    # Write your code here\n    pass",
    //   "CPP": "#include<bits/stdc++.h>\nusing namespace std;\nint main() {\n    // Write your code here\n}"
    // }
    @Column(columnDefinition = "TEXT")
    private String starterCodeJson;

    // ─────────────────────────────────────────────
    // SOLUTION CODE (hidden from user)
    // ─────────────────────────────────────────────

    // Admin's reference solution — never shown to user during attempt
    // Shown only after submission or when viewing explanation
    // JSON map of language → solution code
    @Column(columnDefinition = "TEXT")
    private String solutionCodeJson;

    // ─────────────────────────────────────────────
    // SUPPORTED LANGUAGES
    // ─────────────────────────────────────────────

    // JSON array of supported programming languages
    // e.g. ["JAVA", "PYTHON", "CPP", "C"]
    @Column(nullable = false, columnDefinition = "TEXT")
    private String supportedLanguagesJson;

    // ─────────────────────────────────────────────
    // COMPLEXITY
    // ─────────────────────────────────────────────

    // Expected time complexity of optimal solution
    // e.g. "O(n log n)" or "O(n)"
    @Column
    private String expectedTimeComplexity;

    // Expected space complexity
    // e.g. "O(1)" or "O(n)"
    @Column
    private String expectedSpaceComplexity;

    // ─────────────────────────────────────────────
    // BRAINBLITZ USP FIELDS
    // ─────────────────────────────────────────────

    // Why a specific data structure was chosen
    // e.g. "We use a HashSet here because we need O(1)
    //       lookup time. A simple array would give O(n)
    //       lookup making the overall solution O(n²)"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String whyThisDataStructure;

    // Why this specific approach was chosen over others
    // e.g. "We use sliding window instead of brute force
    //       because brute force would be O(n²) while
    //       sliding window gives us O(n)"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String whyThisApproach;

    // JSON array of alternate approaches with complexity
    // e.g. [
    //   {
    //     "approach": "Brute Force",
    //     "description": "Check all pairs",
    //     "timeComplexity": "O(n²)",
    //     "spaceComplexity": "O(1)",
    //     "whyNotIdeal": "Too slow for large inputs"
    //   },
    //   {
    //     "approach": "Sorting",
    //     "description": "Sort and pick second last",
    //     "timeComplexity": "O(n log n)",
    //     "spaceComplexity": "O(1)",
    //     "whyNotIdeal": "Modifies original array"
    //   }
    // ]
    @Column(columnDefinition = "TEXT")
    private String alternateApproachesJson;

    // ─────────────────────────────────────────────
    // CODE DEBUG SPECIFIC
    // ─────────────────────────────────────────────

    // For CODE_DEBUG type only
    // The buggy version of the code shown to user
    // JSON map of language → buggy code
    @Column(columnDefinition = "TEXT")
    private String buggyCodeJson;

    // Description of what bug exists
    // e.g. "The loop boundary condition is wrong causing
    //       ArrayIndexOutOfBoundsException for edge cases"
    @Column(columnDefinition = "TEXT")
    private String bugDescription;

    // ─────────────────────────────────────────────
    // CHILD RELATIONSHIPS
    // ─────────────────────────────────────────────

    // Test cases — added separately after question creation
    @OneToMany(mappedBy = "codingQuestion",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @OrderBy("id ASC")
    private List<TestCase> testCases;

    // Step by step solution — added separately
    @OneToMany(mappedBy = "codingQuestion",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @OrderBy("stepNumber ASC")
    private List<SolutionStep> solutionSteps;

	public CodingQuestion(Long id, Question question, String problemStatement, String problemStatementHindi,
			String inputFormat, String outputFormat, String constraints, String starterCodeJson,
			String solutionCodeJson, String supportedLanguagesJson, String expectedTimeComplexity,
			String expectedSpaceComplexity, String whyThisDataStructure, String whyThisApproach,
			String alternateApproachesJson, String buggyCodeJson, String bugDescription, List<TestCase> testCases,
			List<SolutionStep> solutionSteps) {
		super();
		this.id = id;
		this.question = question;
		this.problemStatement = problemStatement;
		this.problemStatementHindi = problemStatementHindi;
		this.inputFormat = inputFormat;
		this.outputFormat = outputFormat;
		this.constraints = constraints;
		this.starterCodeJson = starterCodeJson;
		this.solutionCodeJson = solutionCodeJson;
		this.supportedLanguagesJson = supportedLanguagesJson;
		this.expectedTimeComplexity = expectedTimeComplexity;
		this.expectedSpaceComplexity = expectedSpaceComplexity;
		this.whyThisDataStructure = whyThisDataStructure;
		this.whyThisApproach = whyThisApproach;
		this.alternateApproachesJson = alternateApproachesJson;
		this.buggyCodeJson = buggyCodeJson;
		this.bugDescription = bugDescription;
		this.testCases = testCases;
		this.solutionSteps = solutionSteps;
	}

	public CodingQuestion() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getProblemStatement() {
		return problemStatement;
	}

	public void setProblemStatement(String problemStatement) {
		this.problemStatement = problemStatement;
	}

	public String getProblemStatementHindi() {
		return problemStatementHindi;
	}

	public void setProblemStatementHindi(String problemStatementHindi) {
		this.problemStatementHindi = problemStatementHindi;
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

	public String getStarterCodeJson() {
		return starterCodeJson;
	}

	public void setStarterCodeJson(String starterCodeJson) {
		this.starterCodeJson = starterCodeJson;
	}

	public String getSolutionCodeJson() {
		return solutionCodeJson;
	}

	public void setSolutionCodeJson(String solutionCodeJson) {
		this.solutionCodeJson = solutionCodeJson;
	}

	public String getSupportedLanguagesJson() {
		return supportedLanguagesJson;
	}

	public void setSupportedLanguagesJson(String supportedLanguagesJson) {
		this.supportedLanguagesJson = supportedLanguagesJson;
	}

	public String getExpectedTimeComplexity() {
		return expectedTimeComplexity;
	}

	public void setExpectedTimeComplexity(String expectedTimeComplexity) {
		this.expectedTimeComplexity = expectedTimeComplexity;
	}

	public String getExpectedSpaceComplexity() {
		return expectedSpaceComplexity;
	}

	public void setExpectedSpaceComplexity(String expectedSpaceComplexity) {
		this.expectedSpaceComplexity = expectedSpaceComplexity;
	}

	public String getWhyThisDataStructure() {
		return whyThisDataStructure;
	}

	public void setWhyThisDataStructure(String whyThisDataStructure) {
		this.whyThisDataStructure = whyThisDataStructure;
	}

	public String getWhyThisApproach() {
		return whyThisApproach;
	}

	public void setWhyThisApproach(String whyThisApproach) {
		this.whyThisApproach = whyThisApproach;
	}

	public String getAlternateApproachesJson() {
		return alternateApproachesJson;
	}

	public void setAlternateApproachesJson(String alternateApproachesJson) {
		this.alternateApproachesJson = alternateApproachesJson;
	}

	public String getBuggyCodeJson() {
		return buggyCodeJson;
	}

	public void setBuggyCodeJson(String buggyCodeJson) {
		this.buggyCodeJson = buggyCodeJson;
	}

	public String getBugDescription() {
		return bugDescription;
	}

	public void setBugDescription(String bugDescription) {
		this.bugDescription = bugDescription;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public List<SolutionStep> getSolutionSteps() {
		return solutionSteps;
	}

	public void setSolutionSteps(List<SolutionStep> solutionSteps) {
		this.solutionSteps = solutionSteps;
	}
    
    
}