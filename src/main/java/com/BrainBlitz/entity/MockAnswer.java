package com.BrainBlitz.entity;

import java.time.LocalDateTime;
import com.BrainBlitz.enums.AnswerResult;
import com.BrainBlitz.enums.DifficultyLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "mock_answers")
public class MockAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which session this answer belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private MockSession mockSession;

    // Which question was answered
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private Question question;

    // Subject and topic — copied from question at time of attempt
    // We copy here because question might get edited later
    // We need original subject/topic for weak area calculation
    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String topic;

    // Difficulty level at time of attempt — copied from question
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficultyLevel;

    // ─────────────────────────────────────────────
    // USER ANSWER
    // ─────────────────────────────────────────────

    // Stored as TEXT because answer format varies per question type
    // MCQ          → "A" or "2" (option index)
    // Multi correct → "A,C" (comma separated)
    // Fill blank   → "Binary Search"
    // Code writing → full code as string
    // SQL          → SQL query string
    // Arrangement  → "PQRS"
    // Speech       → transcribed text
    // Mail writing → full written mail
    // null         → question was skipped
    @Column(columnDefinition = "TEXT")
    private String userAnswer;

    // ─────────────────────────────────────────────
    // RESULT
    // ─────────────────────────────────────────────

    // CORRECT, WRONG, SKIPPED, PARTIAL, PENDING
    // PARTIAL  → for multi correct — some options correct
    // PENDING  → for writing/speech — AI evaluation not done yet
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnswerResult result;

    @Column(nullable = false)
    private double marksAwarded;      // positive or negative

    // ─────────────────────────────────────────────
    // TIMING
    // ─────────────────────────────────────────────

    @Column(nullable = false)
    private int timeTakenSeconds;     // time spent on this question

    // ─────────────────────────────────────────────
    // FLAGS
    // ─────────────────────────────────────────────

    @Column(nullable = false)
    private boolean isAttempted;      // false = skipped

    @Column(nullable = false)
    private boolean isMarkedReview;   // user marked for review

    // ─────────────────────────────────────────────
    // AI EVALUATION (for writing and speech)
    // ─────────────────────────────────────────────

    // Score given by Claude AI — 0 to 100
    // null for non AI evaluated questions
    @Column
    private Integer aiScore;

    // Detailed feedback from Claude AI
    // e.g. "Good structure but grammar needs improvement"
    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    // Whether AI evaluation is done
    @Column(nullable = false)
    private boolean isAiEvaluated = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

	public MockAnswer(Long id, MockSession mockSession, Question question, String subject, String topic,
			DifficultyLevel difficultyLevel, String userAnswer, AnswerResult result, double marksAwarded,
			int timeTakenSeconds, boolean isAttempted, boolean isMarkedReview, Integer aiScore, String aiFeedback,
			boolean isAiEvaluated, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.mockSession = mockSession;
		this.question = question;
		this.subject = subject;
		this.topic = topic;
		this.difficultyLevel = difficultyLevel;
		this.userAnswer = userAnswer;
		this.result = result;
		this.marksAwarded = marksAwarded;
		this.timeTakenSeconds = timeTakenSeconds;
		this.isAttempted = isAttempted;
		this.isMarkedReview = isMarkedReview;
		this.aiScore = aiScore;
		this.aiFeedback = aiFeedback;
		this.isAiEvaluated = isAiEvaluated;
		this.createdAt = createdAt;
	}

	public MockAnswer() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MockSession getMockSession() {
		return mockSession;
	}

	public void setMockSession(MockSession mockSession) {
		this.mockSession = mockSession;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public AnswerResult getResult() {
		return result;
	}

	public void setResult(AnswerResult result) {
		this.result = result;
	}

	public double getMarksAwarded() {
		return marksAwarded;
	}

	public void setMarksAwarded(double marksAwarded) {
		this.marksAwarded = marksAwarded;
	}

	public int getTimeTakenSeconds() {
		return timeTakenSeconds;
	}

	public void setTimeTakenSeconds(int timeTakenSeconds) {
		this.timeTakenSeconds = timeTakenSeconds;
	}

	public boolean isAttempted() {
		return isAttempted;
	}

	public void setAttempted(boolean isAttempted) {
		this.isAttempted = isAttempted;
	}

	public boolean isMarkedReview() {
		return isMarkedReview;
	}

	public void setMarkedReview(boolean isMarkedReview) {
		this.isMarkedReview = isMarkedReview;
	}

	public Integer getAiScore() {
		return aiScore;
	}

	public void setAiScore(Integer aiScore) {
		this.aiScore = aiScore;
	}

	public String getAiFeedback() {
		return aiFeedback;
	}

	public void setAiFeedback(String aiFeedback) {
		this.aiFeedback = aiFeedback;
	}

	public boolean isAiEvaluated() {
		return isAiEvaluated;
	}

	public void setAiEvaluated(boolean isAiEvaluated) {
		this.isAiEvaluated = isAiEvaluated;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}
