package com.BrainBlitz.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BrainBlitz.entity.UserSeenQuestion;
import com.BrainBlitz.enums.AnswerResult;

@Repository
public interface UserSeenQuestionRepository 
    extends JpaRepository<UserSeenQuestion, Long> {

    // Fetch all seen question IDs for a user
    // Used by question selection engine to exclude already seen questions
    List<UserSeenQuestion> findByUserId(Long userId);

    // Fetch seen questions within cooldown period
    // CORRECT → 30 days, WRONG → 7 days, SKIPPED → 3 days
    List<UserSeenQuestion> findByUserIdAndSeenAtAfter(
        Long userId, LocalDateTime cooldownDate);

    // Fetch seen question IDs by result within cooldown
    // Used to apply different cooldown per result type
    List<UserSeenQuestion> findByUserIdAndResultAndSeenAtAfter(
        Long userId, AnswerResult result, LocalDateTime cooldownDate);

    // Check if user has already seen a specific question
    boolean existsByUserIdAndQuestionId(Long userId, Long questionId);

    // Fetch specific record to update seenAt when question repeats
    Optional<UserSeenQuestion> findByUserIdAndQuestionId(
        Long userId, Long questionId);

    // Fetch all seen questions for a specific session
    // Used when session is abandoned — cleanup if needed
    List<UserSeenQuestion> findByMockSessionId(Long sessionId);

    // Count total unique questions seen by user
    int countByUserId(Long userId);
}
