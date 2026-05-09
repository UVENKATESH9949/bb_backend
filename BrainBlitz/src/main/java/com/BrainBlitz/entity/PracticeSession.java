package com.BrainBlitz.entity;

import java.time.LocalDateTime;

import com.BrainBlitz.enums.ExamCategory;
import com.BrainBlitz.enums.ExamType;

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
@Table(name = "practice_sessions")
public class PracticeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // What was practiced
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamCategory examCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamType examType;

    @Column(nullable = false)
    private String subject;

    // Practice mode
    // TOPIC → topic wise, no timer
    // QUICK → quick quiz with timer
    @Column(nullable = false)
    private String mode;

    // Performance
    @Column(nullable = false)
    private int totalQuestions;

    @Column(nullable = false)
    private int correct;

    @Column(nullable = false)
    private int wrong;

    @Column(nullable = false)
    private int skipped;

    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    private double accuracyPercentage;

    // Time
    @Column
    private Integer durationSeconds;

    @Column(nullable = false, updatable = false)
    private LocalDateTime practicedAt;

    
    public PracticeSession(Long id, User user, ExamCategory examCategory, ExamType examType, String subject,
			String mode, int totalQuestions, int correct, int wrong, int skipped, double score,
			double accuracyPercentage, Integer durationSeconds, LocalDateTime practicedAt) {
		super();
		this.id = id;
		this.user = user;
		this.examCategory = examCategory;
		this.examType = examType;
		this.subject = subject;
		this.mode = mode;
		this.totalQuestions = totalQuestions;
		this.correct = correct;
		this.wrong = wrong;
		this.skipped = skipped;
		this.score = score;
		this.accuracyPercentage = accuracyPercentage;
		this.durationSeconds = durationSeconds;
		this.practicedAt = practicedAt;
	}
    
    

	public PracticeSession() {
		super();
	}


	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public ExamCategory getExamCategory() {
		return examCategory;
	}



	public void setExamCategory(ExamCategory examCategory) {
		this.examCategory = examCategory;
	}



	public ExamType getExamType() {
		return examType;
	}



	public void setExamType(ExamType examType) {
		this.examType = examType;
	}



	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getMode() {
		return mode;
	}



	public void setMode(String mode) {
		this.mode = mode;
	}



	public int getTotalQuestions() {
		return totalQuestions;
	}



	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}



	public int getCorrect() {
		return correct;
	}



	public void setCorrect(int correct) {
		this.correct = correct;
	}



	public int getWrong() {
		return wrong;
	}



	public void setWrong(int wrong) {
		this.wrong = wrong;
	}



	public int getSkipped() {
		return skipped;
	}



	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}



	public double getScore() {
		return score;
	}



	public void setScore(double score) {
		this.score = score;
	}



	public double getAccuracyPercentage() {
		return accuracyPercentage;
	}



	public void setAccuracyPercentage(double accuracyPercentage) {
		this.accuracyPercentage = accuracyPercentage;
	}



	public Integer getDurationSeconds() {
		return durationSeconds;
	}



	public void setDurationSeconds(Integer durationSeconds) {
		this.durationSeconds = durationSeconds;
	}



	public LocalDateTime getPracticedAt() {
		return practicedAt;
	}



	public void setPracticedAt(LocalDateTime practicedAt) {
		this.practicedAt = practicedAt;
	}



	@PrePersist
    protected void onCreate() {
        this.practicedAt = LocalDateTime.now();
    }
}