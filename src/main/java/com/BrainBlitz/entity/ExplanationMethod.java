package com.BrainBlitz.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "explanation_methods")
public class ExplanationMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─────────────────────────────────────────────
    // PARENT REFERENCE
    // ─────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_explanation_id", nullable = false)
    @JsonIgnore
    private QuestionExplanation questionExplanation;

    // ─────────────────────────────────────────────
    // CORE FIELDS (both mandatory)
    // ─────────────────────────────────────────────

    @Column(nullable = false)
    private String title;           // e.g. "Using Vedic Math", "Using Elimination"
                                    // Admin defined, fully flexible

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;     // Full solution content
                                    // Supports Markdown + LaTeX

    // ─────────────────────────────────────────────
    // CONTROL FIELDS
    // ─────────────────────────────────────────────

    @Column(nullable = false)
    private int displayOrder;       // Controls which method shows first
                                    // Admin sets 1, 2, 3 etc.
    @JsonProperty("isActive")
    @Column(nullable = false)
    private boolean isActive = true; // Hide without deleting

    // ─────────────────────────────────────────────
    // AUDIT
    // ─────────────────────────────────────────────

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

	public ExplanationMethod(Long id, QuestionExplanation questionExplanation, String title, String description,
			int displayOrder, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy,
			String updatedBy) {
		super();
		this.id = id;
		this.questionExplanation = questionExplanation;
		this.title = title;
		this.description = description;
		this.displayOrder = displayOrder;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	public ExplanationMethod() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public QuestionExplanation getQuestionExplanation() {
		return questionExplanation;
	}

	public void setQuestionExplanation(QuestionExplanation questionExplanation) {
		this.questionExplanation = questionExplanation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
    
    
}
