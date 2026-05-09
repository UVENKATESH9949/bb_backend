package com.BrainBlitz.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import com.BrainBlitz.converter.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "coding_questions")
public class CodingQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    @JsonIgnore
    private Question question;

    // ── Problem Definition ────────────────────────────────
    @Column(nullable = false, columnDefinition = "TEXT")
    private String problemStatement;

    @Column(columnDefinition = "TEXT")
    private String problemStatementHindi;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String inputFormat;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String outputFormat;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String constraints;

    // Real-world context — BrainBlitz differentiator
    // e.g. "Used in fraud detection, email deduplication"
    @Column(columnDefinition = "TEXT")
    private String realWorldContext;

    // ── Code Templates ────────────────────────────────────
    // JSON map of language → code
    // e.g. {"JAVA": "...", "PYTHON": "...", "CPP": "..."}
    @Column(columnDefinition = "TEXT")
    private String starterCodeJson;

    @Column(columnDefinition = "TEXT")
    private String solutionCodeJson;

    @Column(columnDefinition = "TEXT")
    private String driverCodeJson;       // hidden wrapper, never shown to user

    @Column(nullable = false, columnDefinition = "TEXT")
    private String supportedLanguagesJson;

    // ── Complexity ────────────────────────────────────────
    private String expectedTimeComplexity;
    private String expectedSpaceComplexity;

    // ── Approaches (replaces whyThisApproach, whyThisDataStructure, alternateApproachesJson)
    // JSON array — always brute force first, optimal last
    // Each approach:
    // {
    //   "order": 1,
    //   "type": "BRUTE_FORCE" | "BETTER" | "OPTIMAL",
    //   "title": "Nested Loop",
    //   "ahaMessage": "Check every pair — simple but slow",
    //   "intuition": "For each element scan the rest",
    //   "whenToUseThis": "Only when N < 100",
    //   "timeComplexity": "O(n²)",
    //   "spaceComplexity": "O(1)",
    //   "steps": ["step1", "step2"],
    //   "languageNotes": {"JAVA": "...", "PYTHON": "..."},
    //   "codePerLanguage": {"JAVA": "...", "PYTHON": "..."}
    // }
    @Column(columnDefinition = "TEXT")
    private String approachesJson;

    // ── Learning Aids ─────────────────────────────────────
    // 3 progressive hints — vague to specific
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> hintsJson;

    // Concepts student must know before attempting
    // e.g. ["Arrays", "HashMaps", "Hashing concept"]
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> prerequisitesJson;

    // Common mistakes tagged by type
    // [{"type": "BOUNDARY", "description": "Not handling empty array"},
    //  {"type": "LANGUAGE", "description": "Using == instead of .equals() in Java"}]
    @Column(columnDefinition = "TEXT")
    private String commonMistakesJson;

    // ── Company Tags (free access — BrainBlitz differentiator) ──
    // e.g. ["Amazon", "TCS", "Google", "Infosys"]
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> companyTagsJson;

    // ── CODE_DEBUG specific ───────────────────────────────
    // JSON map of language → buggy code shown to user
    @Column(columnDefinition = "TEXT")
    private String buggyCodeJson;

    @Column(columnDefinition = "TEXT")
    private String bugDescription;

    // ── Relationships ─────────────────────────────────────
    @OneToMany(mappedBy = "codingQuestion",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<TestCase> testCases;

    @OneToMany(mappedBy = "codingQuestion",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @OrderBy("stepNumber ASC")
    private List<SolutionStep> solutionSteps;

	public CodingQuestion(Long id, Question question, String problemStatement, String problemStatementHindi,
			String inputFormat, String outputFormat, String constraints, String realWorldContext,
			String starterCodeJson, String solutionCodeJson, String driverCodeJson, String supportedLanguagesJson,
			String expectedTimeComplexity, String expectedSpaceComplexity, String approachesJson,
			List<String> hintsJson, List<String> prerequisitesJson, String commonMistakesJson,
			List<String> companyTagsJson, String buggyCodeJson, String bugDescription, List<TestCase> testCases,
			List<SolutionStep> solutionSteps) {
		super();
		this.id = id;
		this.question = question;
		this.problemStatement = problemStatement;
		this.problemStatementHindi = problemStatementHindi;
		this.inputFormat = inputFormat;
		this.outputFormat = outputFormat;
		this.constraints = constraints;
		this.realWorldContext = realWorldContext;
		this.starterCodeJson = starterCodeJson;
		this.solutionCodeJson = solutionCodeJson;
		this.driverCodeJson = driverCodeJson;
		this.supportedLanguagesJson = supportedLanguagesJson;
		this.expectedTimeComplexity = expectedTimeComplexity;
		this.expectedSpaceComplexity = expectedSpaceComplexity;
		this.approachesJson = approachesJson;
		this.hintsJson = hintsJson;
		this.prerequisitesJson = prerequisitesJson;
		this.commonMistakesJson = commonMistakesJson;
		this.companyTagsJson = companyTagsJson;
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

	public String getRealWorldContext() {
		return realWorldContext;
	}

	public void setRealWorldContext(String realWorldContext) {
		this.realWorldContext = realWorldContext;
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

	public String getDriverCodeJson() {
		return driverCodeJson;
	}

	public void setDriverCodeJson(String driverCodeJson) {
		this.driverCodeJson = driverCodeJson;
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

	public String getApproachesJson() {
		return approachesJson;
	}

	public void setApproachesJson(String approachesJson) {
		this.approachesJson = approachesJson;
	}

	public List<String> getHintsJson() {
		return hintsJson;
	}

	public void setHintsJson(List<String> hintsJson) {
		this.hintsJson = hintsJson;
	}

	public List<String> getPrerequisitesJson() {
		return prerequisitesJson;
	}

	public void setPrerequisitesJson(List<String> prerequisitesJson) {
		this.prerequisitesJson = prerequisitesJson;
	}

	public String getCommonMistakesJson() {
		return commonMistakesJson;
	}

	public void setCommonMistakesJson(String commonMistakesJson) {
		this.commonMistakesJson = commonMistakesJson;
	}

	public List<String> getCompanyTagsJson() {
		return companyTagsJson;
	}

	public void setCompanyTagsJson(List<String> companyTagsJson) {
		this.companyTagsJson = companyTagsJson;
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

    // ── Getters & Setters ─────────────────────────────────
    // ... (keep all existing ones, add for new fields)
    
    
}