package com.BrainBlitz.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "question_explanations")
public class QuestionExplanation {

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
    // LAYER 1 — FIXED BREAKDOWN (same for all question types)
    // ─────────────────────────────────────────────

    @Column(columnDefinition = "TEXT")
    private String whatIsAsked;     // What the question is asking for
                                    // Supports Markdown + LaTeX

    @Column(columnDefinition = "TEXT")
    private String whatIsGiven;     // Data, values, conditions provided
                                    // Supports Markdown + LaTeX

    @Column(columnDefinition = "TEXT")
    private String whatApproach;    // Which method or concept to apply
                                    // Supports Markdown + LaTeX

    @Column(columnDefinition = "TEXT")
    private String howToSolve;      // Full step-by-step execution
                                    // Supports Markdown + LaTeX

    // ─────────────────────────────────────────────
    // LAYER 2 — DYNAMIC APPROACH METHODS
    // ─────────────────────────────────────────────

    @OneToMany(
        mappedBy = "questionExplanation",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<ExplanationMethod> methods = new ArrayList<>();

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

	public QuestionExplanation(Long id, Question question, String whatIsAsked, String whatIsGiven, String whatApproach,
			String howToSolve, List<ExplanationMethod> methods, LocalDateTime createdAt, LocalDateTime updatedAt,
			String createdBy, String updatedBy) {
		super();
		this.id = id;
		this.question = question;
		this.whatIsAsked = whatIsAsked;
		this.whatIsGiven = whatIsGiven;
		this.whatApproach = whatApproach;
		this.howToSolve = howToSolve;
		this.methods = methods;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

	public QuestionExplanation() {
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

	public String getWhatIsAsked() {
		return whatIsAsked;
	}

	public void setWhatIsAsked(String whatIsAsked) {
		this.whatIsAsked = whatIsAsked;
	}

	public String getWhatIsGiven() {
		return whatIsGiven;
	}

	public void setWhatIsGiven(String whatIsGiven) {
		this.whatIsGiven = whatIsGiven;
	}

	public String getWhatApproach() {
		return whatApproach;
	}

	public void setWhatApproach(String whatApproach) {
		this.whatApproach = whatApproach;
	}

	public String getHowToSolve() {
		return howToSolve;
	}

	public void setHowToSolve(String howToSolve) {
		this.howToSolve = howToSolve;
	}

	public List<ExplanationMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<ExplanationMethod> methods) {
		this.methods = methods;
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
