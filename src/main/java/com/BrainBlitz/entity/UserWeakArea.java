package com.BrainBlitz.entity;

import java.time.LocalDateTime;

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

@Entity
@Table(name = "user_weak_areas")
public class UserWeakArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Subject and topic — matches exactly with 
    // subject and topic fields in questions table
    @Column(nullable = false)
    private String subject;       // e.g. Quantitative Aptitude, DSA

    @Column(nullable = false)
    private String topic;         // e.g. Time & Work, Binary Trees

    // Performance metrics
    @Column(nullable = false)
    private double accuracyPercentage;   // correct / attempted * 100

    @Column(nullable = false)
    private double avgTimeTaken;         // average seconds per question

    @Column(nullable = false)
    private int totalAttempted;          // how many questions attempted in this topic

    // Weak flag
    // true  = accuracy < 50% or avgTime very high
    // false = performing well, but still tracked
    @Column(nullable = false)
    private boolean isWeak = false;

    // Slow flag — separate from weak
    // true = avg time taken is above acceptable limit
    @Column(nullable = false)
    private boolean isSlow = false;

    // Last time this record was updated
    // Updated after every mock
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

	public UserWeakArea(Long id, User user, String subject, String topic, double accuracyPercentage,
			double avgTimeTaken, int totalAttempted, boolean isWeak, boolean isSlow, LocalDateTime lastUpdated) {
		super();
		this.id = id;
		this.user = user;
		this.subject = subject;
		this.topic = topic;
		this.accuracyPercentage = accuracyPercentage;
		this.avgTimeTaken = avgTimeTaken;
		this.totalAttempted = totalAttempted;
		this.isWeak = isWeak;
		this.isSlow = isSlow;
		this.lastUpdated = lastUpdated;
	}

	public UserWeakArea() {
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

	public double getAccuracyPercentage() {
		return accuracyPercentage;
	}

	public void setAccuracyPercentage(double accuracyPercentage) {
		this.accuracyPercentage = accuracyPercentage;
	}

	public double getAvgTimeTaken() {
		return avgTimeTaken;
	}

	public void setAvgTimeTaken(double avgTimeTaken) {
		this.avgTimeTaken = avgTimeTaken;
	}

	public int getTotalAttempted() {
		return totalAttempted;
	}

	public void setTotalAttempted(int totalAttempted) {
		this.totalAttempted = totalAttempted;
	}

	public boolean isWeak() {
		return isWeak;
	}

	public void setWeak(boolean isWeak) {
		this.isWeak = isWeak;
	}

	public boolean isSlow() {
		return isSlow;
	}

	public void setSlow(boolean isSlow) {
		this.isSlow = isSlow;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
    
    
}
