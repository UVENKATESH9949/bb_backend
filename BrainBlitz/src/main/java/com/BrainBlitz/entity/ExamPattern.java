package com.BrainBlitz.entity;

import java.time.LocalDateTime;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "exam_patterns")
public class ExamPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which exam this pattern belongs to
    // e.g. SSC_CGL, SSC_CHSL, TCS_NQT, INFOSYS, UPSC etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamType examType;

    // Round name — for placement exams
    // e.g. "Aptitude Round", "Coding Round", "HR Round"
    // null for govt exams (they don't have rounds)
    @Column
    private String roundName;

    // Subject and topic — must exactly match
    // subject and topic in questions table
    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String topic;

    // How many questions from this topic in one mock
    @Column(nullable = false)
    private int questionsCount;

    // Weightage percentage of this topic in the exam
    // e.g. Quantitative Aptitude = 25% in SSC CGL
    @Column(nullable = false)
    private double weightagePercent;

    // Difficulty distribution for this topic
    // How many easy, medium, hard questions
    @Column(nullable = false)
    private int easyCount;        // LEVEL_1 to LEVEL_3

    @Column(nullable = false)
    private int mediumCount;      // LEVEL_4 to LEVEL_6

    @Column(nullable = false)
    private int hardCount;        // LEVEL_7 to LEVEL_10

    // Marks per correct answer for this topic
    @Column(nullable = false)
    private double marksPerQuestion;

    // Negative marks for this topic
    // 0 if no negative marking
    @Column(nullable = false)
    private double negativeMarks;

    // Time limit for this topic in seconds
    // null = no separate time limit (overall mock timer applies)
    @Column
    private Integer timeLimitSeconds;

    // Is this topic compulsory in every mock
    // true  = always include this topic
    // false = optional, include based on user weak areas
    @Column(nullable = false)
    private boolean isCompulsory = true;

    // Active flag — to disable a pattern without deleting
    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

	public ExamPattern(Long id, ExamType examType, String roundName, String subject, String topic, int questionsCount,
			double weightagePercent, int easyCount, int mediumCount, int hardCount, double marksPerQuestion,
			double negativeMarks, Integer timeLimitSeconds, boolean isCompulsory, boolean isActive,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.examType = examType;
		this.roundName = roundName;
		this.subject = subject;
		this.topic = topic;
		this.questionsCount = questionsCount;
		this.weightagePercent = weightagePercent;
		this.easyCount = easyCount;
		this.mediumCount = mediumCount;
		this.hardCount = hardCount;
		this.marksPerQuestion = marksPerQuestion;
		this.negativeMarks = negativeMarks;
		this.timeLimitSeconds = timeLimitSeconds;
		this.isCompulsory = isCompulsory;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public ExamPattern() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ExamType getExamType() {
		return examType;
	}

	public void setExamType(ExamType examType) {
		this.examType = examType;
	}

	public String getRoundName() {
		return roundName;
	}

	public void setRoundName(String roundName) {
		this.roundName = roundName;
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

	public int getQuestionsCount() {
		return questionsCount;
	}

	public void setQuestionsCount(int questionsCount) {
		this.questionsCount = questionsCount;
	}

	public double getWeightagePercent() {
		return weightagePercent;
	}

	public void setWeightagePercent(double weightagePercent) {
		this.weightagePercent = weightagePercent;
	}

	public int getEasyCount() {
		return easyCount;
	}

	public void setEasyCount(int easyCount) {
		this.easyCount = easyCount;
	}

	public int getMediumCount() {
		return mediumCount;
	}

	public void setMediumCount(int mediumCount) {
		this.mediumCount = mediumCount;
	}

	public int getHardCount() {
		return hardCount;
	}

	public void setHardCount(int hardCount) {
		this.hardCount = hardCount;
	}

	public double getMarksPerQuestion() {
		return marksPerQuestion;
	}

	public void setMarksPerQuestion(double marksPerQuestion) {
		this.marksPerQuestion = marksPerQuestion;
	}

	public double getNegativeMarks() {
		return negativeMarks;
	}

	public void setNegativeMarks(double negativeMarks) {
		this.negativeMarks = negativeMarks;
	}

	public Integer getTimeLimitSeconds() {
		return timeLimitSeconds;
	}

	public void setTimeLimitSeconds(Integer timeLimitSeconds) {
		this.timeLimitSeconds = timeLimitSeconds;
	}

	public boolean isCompulsory() {
		return isCompulsory;
	}

	public void setCompulsory(boolean isCompulsory) {
		this.isCompulsory = isCompulsory;
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
}
