package com.BrainBlitz.entity;

import com.BrainBlitz.enums.ArrangementType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "arrangement_questions")
public class ArrangementQuestion {

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
    // TYPE
    // ─────────────────────────────────────────────

    // SENTENCE  → arrange individual sentences P, Q, R, S
    // PARAGRAPH → arrange full paragraphs (para jumble)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArrangementType arrangementType;

    // ─────────────────────────────────────────────
    // SEGMENTS
    // ─────────────────────────────────────────────

    // JSON array of segments to be arranged
    // Each segment has a label (P/Q/R/S) and text
    // Example:
    // [
    //   {"label": "P", "text": "He went to the market"},
    //   {"label": "Q", "text": "He bought some vegetables"},
    //   {"label": "R", "text": "He came back home"},
    //   {"label": "S", "text": "He cooked dinner"}
    // ]
    @Column(nullable = false, columnDefinition = "TEXT")
    private String segmentsJson;

    // Hindi version of segments (for bilingual questions)
    @Column(columnDefinition = "TEXT")
    private String segmentsJsonHindi;

    // ─────────────────────────────────────────────
    // CORRECT ORDER
    // ─────────────────────────────────────────────

    // Correct arrangement as a string
    // e.g. "PQRS" or "QPSR" or "RSPQ"
    // Validated by comparing user's answer string
    @Column(nullable = false)
    private String correctOrder;

    // ─────────────────────────────────────────────
    // ALTERNATE CORRECT ORDERS
    // ─────────────────────────────────────────────

    // Some questions have more than one valid arrangement
    // JSON array of accepted orders
    // e.g. ["PQRS", "PQSR"] — both are valid
    // null if only one correct order exists
    @Column(columnDefinition = "TEXT")
    private String alternateCorrectOrders;  // stored as JSON array string

    // ─────────────────────────────────────────────
    // OPENING / CLOSING SENTENCE (optional)
    // ─────────────────────────────────────────────

    // Some arrangement questions give a fixed
    // first sentence and/or last sentence
    // User only arranges the middle segments
    // e.g. "Sentence 1 is fixed: 'Once upon a time...'"
    @Column(columnDefinition = "TEXT")
    private String fixedOpeningSentence;    // shown before segments, not to be arranged

    @Column(columnDefinition = "TEXT")
    private String fixedClosingSentence;    // shown after segments, not to be arranged

    @Column(columnDefinition = "TEXT")
    private String fixedOpeningSentenceHindi;

    @Column(columnDefinition = "TEXT")
    private String fixedClosingSentenceHindi;

    // ─────────────────────────────────────────────
    // DISPLAY FORMAT
    // ─────────────────────────────────────────────

    // true  → user drags and drops segments (UI renders drag-drop)
    // false → user types the correct order string e.g. "PQRS"
    @Column(nullable = false)
    private boolean isDragDrop = true;

	public ArrangementQuestion(Long id, Question question, ArrangementType arrangementType, String segmentsJson,
			String segmentsJsonHindi, String correctOrder, String alternateCorrectOrders, String fixedOpeningSentence,
			String fixedClosingSentence, String fixedOpeningSentenceHindi, String fixedClosingSentenceHindi,
			boolean isDragDrop) {
		super();
		this.id = id;
		this.question = question;
		this.arrangementType = arrangementType;
		this.segmentsJson = segmentsJson;
		this.segmentsJsonHindi = segmentsJsonHindi;
		this.correctOrder = correctOrder;
		this.alternateCorrectOrders = alternateCorrectOrders;
		this.fixedOpeningSentence = fixedOpeningSentence;
		this.fixedClosingSentence = fixedClosingSentence;
		this.fixedOpeningSentenceHindi = fixedOpeningSentenceHindi;
		this.fixedClosingSentenceHindi = fixedClosingSentenceHindi;
		this.isDragDrop = isDragDrop;
	}

	public ArrangementQuestion() {
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

	public ArrangementType getArrangementType() {
		return arrangementType;
	}

	public void setArrangementType(ArrangementType arrangementType) {
		this.arrangementType = arrangementType;
	}

	public String getSegmentsJson() {
		return segmentsJson;
	}

	public void setSegmentsJson(String segmentsJson) {
		this.segmentsJson = segmentsJson;
	}

	public String getSegmentsJsonHindi() {
		return segmentsJsonHindi;
	}

	public void setSegmentsJsonHindi(String segmentsJsonHindi) {
		this.segmentsJsonHindi = segmentsJsonHindi;
	}

	public String getCorrectOrder() {
		return correctOrder;
	}

	public void setCorrectOrder(String correctOrder) {
		this.correctOrder = correctOrder;
	}

	public String getAlternateCorrectOrders() {
		return alternateCorrectOrders;
	}

	public void setAlternateCorrectOrders(String alternateCorrectOrders) {
		this.alternateCorrectOrders = alternateCorrectOrders;
	}

	public String getFixedOpeningSentence() {
		return fixedOpeningSentence;
	}

	public void setFixedOpeningSentence(String fixedOpeningSentence) {
		this.fixedOpeningSentence = fixedOpeningSentence;
	}

	public String getFixedClosingSentence() {
		return fixedClosingSentence;
	}

	public void setFixedClosingSentence(String fixedClosingSentence) {
		this.fixedClosingSentence = fixedClosingSentence;
	}

	public String getFixedOpeningSentenceHindi() {
		return fixedOpeningSentenceHindi;
	}

	public void setFixedOpeningSentenceHindi(String fixedOpeningSentenceHindi) {
		this.fixedOpeningSentenceHindi = fixedOpeningSentenceHindi;
	}

	public String getFixedClosingSentenceHindi() {
		return fixedClosingSentenceHindi;
	}

	public void setFixedClosingSentenceHindi(String fixedClosingSentenceHindi) {
		this.fixedClosingSentenceHindi = fixedClosingSentenceHindi;
	}

	public boolean isDragDrop() {
		return isDragDrop;
	}

	public void setDragDrop(boolean isDragDrop) {
		this.isDragDrop = isDragDrop;
	}
    
    
}