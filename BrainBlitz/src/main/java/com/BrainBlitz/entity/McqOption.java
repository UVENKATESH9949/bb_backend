package com.BrainBlitz.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "mcq_options")
public class McqOption {

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
    // OPTION CONTENT
    // ─────────────────────────────────────────────

    // Option text — e.g. "A) Paris", "B) London"
    @Column(columnDefinition = "TEXT")
    private String optionText;

    // Hindi version of option text (for bilingual questions)
    @Column(columnDefinition = "TEXT")
    private String optionTextHindi;

    // Image URL — for image options (image_options type)
    // null for text-only options
    @Column
    private String optionImageUrl;

    // ─────────────────────────────────────────────
    // ANSWER FLAG
    // ─────────────────────────────────────────────

    // true = this option is correct
    // For MCQ single → only one option has isCorrect = true
    // For multi_correct → multiple options have isCorrect = true
    // For true_false → one of two options has isCorrect = true
    @Column(nullable = false)
    private boolean isCorrect = false;

    // ─────────────────────────────────────────────
    // DISPLAY ORDER
    // ─────────────────────────────────────────────

    // 1 = A, 2 = B, 3 = C, 4 = D
    // Used to maintain consistent display order
    @Column(nullable = false)
    private int optionOrder;

    // ─────────────────────────────────────────────
    // EXPLANATION (optional per option)
    // ─────────────────────────────────────────────

    // Why this option is wrong (for incorrect options)
    // Why this option is right (for correct option)
    // Shown after user submits answer
    @Column(columnDefinition = "TEXT")
    private String optionExplanation;

    
	public McqOption(Long id, Question question, String optionText, String optionTextHindi, String optionImageUrl,
			boolean isCorrect, int optionOrder, String optionExplanation) {
		super();
		this.id = id;
		this.question = question;
		this.optionText = optionText;
		this.optionTextHindi = optionTextHindi;
		this.optionImageUrl = optionImageUrl;
		this.isCorrect = isCorrect;
		this.optionOrder = optionOrder;
		this.optionExplanation = optionExplanation;
	}

	
	public McqOption() {
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

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public String getOptionTextHindi() {
		return optionTextHindi;
	}

	public void setOptionTextHindi(String optionTextHindi) {
		this.optionTextHindi = optionTextHindi;
	}

	public String getOptionImageUrl() {
		return optionImageUrl;
	}

	public void setOptionImageUrl(String optionImageUrl) {
		this.optionImageUrl = optionImageUrl;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public int getOptionOrder() {
		return optionOrder;
	}

	public void setOptionOrder(int optionOrder) {
		this.optionOrder = optionOrder;
	}

	public String getOptionExplanation() {
		return optionExplanation;
	}

	public void setOptionExplanation(String optionExplanation) {
		this.optionExplanation = optionExplanation;
	}
    
    
}