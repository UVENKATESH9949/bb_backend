package com.BrainBlitz.entity;

import com.BrainBlitz.enums.WritingType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "writing_questions")
public class WritingQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    @JsonIgnore
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WritingType writingType;        // MAIL, ESSAY, SPEECH, LISTENING

    @Column(columnDefinition = "TEXT", nullable = false)
    private String prompt;                  // the writing task shown to user

    private Integer minWords;
    private Integer maxWords;

    // JSON — grading criteria stored for later AI use
    // e.g. {"tone":"formal","format":"subject+body","grammar":true}
    @Column(columnDefinition = "TEXT")
    private String evaluationCriteriaJson;

    @Column(columnDefinition = "TEXT")
    private String sampleAnswer;            // shown after submission in Phase 1

    // For listening_comp — Cloudinary audio URL
    private String audioUrl;

    // For speech_round — max speaking duration in seconds
    private Integer speechDurationSeconds;

    // Grading status — for Phase 3 AI grading
    // Values: PENDING, AI_GRADED, MANUALLY_REVIEWED
    private String gradingStatus = "PENDING";

	public WritingQuestion(Long id, Question question, WritingType writingType, String prompt, Integer minWords,
			Integer maxWords, String evaluationCriteriaJson, String sampleAnswer, String audioUrl,
			Integer speechDurationSeconds, String gradingStatus) {
		super();
		this.id = id;
		this.question = question;
		this.writingType = writingType;
		this.prompt = prompt;
		this.minWords = minWords;
		this.maxWords = maxWords;
		this.evaluationCriteriaJson = evaluationCriteriaJson;
		this.sampleAnswer = sampleAnswer;
		this.audioUrl = audioUrl;
		this.speechDurationSeconds = speechDurationSeconds;
		this.gradingStatus = gradingStatus;
	}

	public WritingQuestion() {
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

	public WritingType getWritingType() {
		return writingType;
	}

	public void setWritingType(WritingType writingType) {
		this.writingType = writingType;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public Integer getMinWords() {
		return minWords;
	}

	public void setMinWords(Integer minWords) {
		this.minWords = minWords;
	}

	public Integer getMaxWords() {
		return maxWords;
	}

	public void setMaxWords(Integer maxWords) {
		this.maxWords = maxWords;
	}

	public String getEvaluationCriteriaJson() {
		return evaluationCriteriaJson;
	}

	public void setEvaluationCriteriaJson(String evaluationCriteriaJson) {
		this.evaluationCriteriaJson = evaluationCriteriaJson;
	}

	public String getSampleAnswer() {
		return sampleAnswer;
	}

	public void setSampleAnswer(String sampleAnswer) {
		this.sampleAnswer = sampleAnswer;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public Integer getSpeechDurationSeconds() {
		return speechDurationSeconds;
	}

	public void setSpeechDurationSeconds(Integer speechDurationSeconds) {
		this.speechDurationSeconds = speechDurationSeconds;
	}

	public String getGradingStatus() {
		return gradingStatus;
	}

	public void setGradingStatus(String gradingStatus) {
		this.gradingStatus = gradingStatus;
	}
    
    
}