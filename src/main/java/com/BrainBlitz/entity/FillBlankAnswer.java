package com.BrainBlitz.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "fill_blank_answers")
public class FillBlankAnswer {

    // ─────────────────────────────────────────────
    // PRIMARY KEY
    // ─────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─────────────────────────────────────────────
    // PARENT REFERENCE
    // ─────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private Question question;

    // ─────────────────────────────────────────────
    // BLANK POSITION
    // ─────────────────────────────────────────────

    // Which blank this answer belongs to
    // e.g. "The ___ of ___ is ___"
    // blankPosition = 1 → first blank
    // blankPosition = 2 → second blank
    // blankPosition = 3 → third blank
    // For single blank questions → always 1
    // For TEXT_ANSWER type → always 1
    @Column(nullable = false)
    private int blankPosition;

    // ─────────────────────────────────────────────
    // CORRECT ANSWER
    // ─────────────────────────────────────────────

    // Primary correct answer
    // e.g. "O(log n)" or "Binary Search" or "Bubble Sort"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String correctAnswer;

    // ─────────────────────────────────────────────
    // ALTERNATE ACCEPTED ANSWERS
    // ─────────────────────────────────────────────

    // JSON array of alternate accepted answers
    // e.g. ["O(log n)", "O(log(n))", "log n", "logn"]
    // All these are correct for the same blank
    // CRITICAL — without this, auto-grading fails
    // because users write answers differently
    @Column(columnDefinition = "TEXT")
    private String alternateAnswers;   // stored as JSON string

    // ─────────────────────────────────────────────
    // VALIDATION FLAGS
    // ─────────────────────────────────────────────

    // true  → "Java" and "java" are different answers
    // false → "Java" and "java" are same (default)
    @Column(nullable = false)
    private boolean caseSensitive = false;

    // true  → user answer must match exactly (trimmed)
    // false → partial match accepted
    // e.g. if correct = "Binary Search Tree"
    // and user types "binary search tree" → accepted if caseSensitive=false
    @Column(nullable = false)
    private boolean exactMatch = true;

    // ─────────────────────────────────────────────
    // DISPLAY HINT FOR THIS BLANK
    // ─────────────────────────────────────────────

    // Optional hint specific to this blank
    // e.g. "Hint: Think about divide and conquer"
    @Column(columnDefinition = "TEXT")
    private String blankHint;

    // Expected answer length hint shown to user
    // e.g. "Answer has 6 characters"
    // null = no length hint
    @Column
    private Integer expectedAnswerLength;

	public FillBlankAnswer(Long id, Question question, int blankPosition, String correctAnswer, String alternateAnswers,
			boolean caseSensitive, boolean exactMatch, String blankHint, Integer expectedAnswerLength) {
		super();
		this.id = id;
		this.question = question;
		this.blankPosition = blankPosition;
		this.correctAnswer = correctAnswer;
		this.alternateAnswers = alternateAnswers;
		this.caseSensitive = caseSensitive;
		this.exactMatch = exactMatch;
		this.blankHint = blankHint;
		this.expectedAnswerLength = expectedAnswerLength;
	}

	public FillBlankAnswer() {
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

	public int getBlankPosition() {
		return blankPosition;
	}

	public void setBlankPosition(int blankPosition) {
		this.blankPosition = blankPosition;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getAlternateAnswers() {
		return alternateAnswers;
	}

	public void setAlternateAnswers(String alternateAnswers) {
		this.alternateAnswers = alternateAnswers;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isExactMatch() {
		return exactMatch;
	}

	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

	public String getBlankHint() {
		return blankHint;
	}

	public void setBlankHint(String blankHint) {
		this.blankHint = blankHint;
	}

	public Integer getExpectedAnswerLength() {
		return expectedAnswerLength;
	}

	public void setExpectedAnswerLength(Integer expectedAnswerLength) {
		this.expectedAnswerLength = expectedAnswerLength;
	}
    
    
}