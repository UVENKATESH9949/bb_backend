package com.BrainBlitz.entity;

import java.time.LocalDateTime;
import com.BrainBlitz.enums.AnswerResult;
import com.BrainBlitz.enums.DifficultyLevel;
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
@Table(name = "user_seen_questions")
public class UserSeenQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Which question was seen
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // Which mock session this question appeared in
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private MockSession mockSession;

    // When user saw this question
    @Column(nullable = false)
    private LocalDateTime seenAt;

    // Result of this question — copied for quick lookup
    // No need to join mock_answers just to check
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnswerResult result;    // CORRECT, WRONG, SKIPPED

    @PrePersist
    protected void onCreate() {
        this.seenAt = LocalDateTime.now();
    }

	public UserSeenQuestion(Long id, User user, Question question, MockSession mockSession, LocalDateTime seenAt,
			AnswerResult result) {
		super();
		this.id = id;
		this.user = user;
		this.question = question;
		this.mockSession = mockSession;
		this.seenAt = seenAt;
		this.result = result;
	}

	public UserSeenQuestion() {
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

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public MockSession getMockSession() {
		return mockSession;
	}

	public void setMockSession(MockSession mockSession) {
		this.mockSession = mockSession;
	}

	public LocalDateTime getSeenAt() {
		return seenAt;
	}

	public void setSeenAt(LocalDateTime seenAt) {
		this.seenAt = seenAt;
	}

	public AnswerResult getResult() {
		return result;
	}

	public void setResult(AnswerResult result) {
		this.result = result;
	}
    
    
}
