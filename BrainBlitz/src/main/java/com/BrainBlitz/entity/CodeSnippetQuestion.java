// src/main/java/com/BrainBlitz/entity/CodeSnippetQuestion.java

package com.BrainBlitz.entity;

import com.BrainBlitz.enums.ProgrammingLanguage;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "code_snippet_questions")
public class CodeSnippetQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    @JsonIgnore
    private Question question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String codeSnippet;             // the code shown to the user

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgrammingLanguage programmingLanguage;

    // For sql_output — stores sample table data as JSON
    // e.g. [{"id":1,"name":"Alice"},{"id":2,"name":"Bob"}]
    @Column(columnDefinition = "TEXT")
    private String sqlTableDataJson;

    // For sql_output — the SQL query shown to user
    @Column(columnDefinition = "TEXT")
    private String sqlQuery;

    // For code_fill — which line number has the blank
    private Integer blankLineNumber;

    // For code_fill — accepted answers (JSON array of strings)
    // e.g. ["return dp[n]", "return dp[n-1]+dp[n-2]"]
    @Column(columnDefinition = "TEXT")
    private String acceptedAnswersJson;

    // For flowchart_mcq — image of the flowchart
    private String flowchartImageUrl;

	public CodeSnippetQuestion(Long id, Question question, String codeSnippet, ProgrammingLanguage programmingLanguage,
			String sqlTableDataJson, String sqlQuery, Integer blankLineNumber, String acceptedAnswersJson,
			String flowchartImageUrl) {
		super();
		this.id = id;
		this.question = question;
		this.codeSnippet = codeSnippet;
		this.programmingLanguage = programmingLanguage;
		this.sqlTableDataJson = sqlTableDataJson;
		this.sqlQuery = sqlQuery;
		this.blankLineNumber = blankLineNumber;
		this.acceptedAnswersJson = acceptedAnswersJson;
		this.flowchartImageUrl = flowchartImageUrl;
	}

	public CodeSnippetQuestion() {
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

	public String getAcceptedAnswersJson() {
		return acceptedAnswersJson;
	}

	public void setAcceptedAnswersJson(String acceptedAnswersJson) {
		this.acceptedAnswersJson = acceptedAnswersJson;
	}

	public String getFlowchartImageUrl() {
		return flowchartImageUrl;
	}

	public void setFlowchartImageUrl(String flowchartImageUrl) {
		this.flowchartImageUrl = flowchartImageUrl;
	}
    
    
}