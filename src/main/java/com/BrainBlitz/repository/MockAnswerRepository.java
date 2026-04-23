package com.BrainBlitz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.BrainBlitz.entity.MockAnswer;
import com.BrainBlitz.enums.AnswerResult;

@Repository
public interface MockAnswerRepository 
    extends JpaRepository<MockAnswer, Long> {

    // Fetch all answers for a session
    // Main query — used for result calculation
    List<MockAnswer> findByMockSessionId(Long sessionId);

    // Fetch all answers for a session by result
    // e.g. fetch only WRONG answers for weak area calculation
    List<MockAnswer> findByMockSessionIdAndResult(
        Long sessionId, AnswerResult result);

    // Fetch all answers for a user across all mocks by topic
    // Used for weak area calculation per topic
    List<MockAnswer> findByMockSessionUserIdAndTopic(
        Long userId, String topic);

    // Fetch all answers for a user across all mocks by subject
    List<MockAnswer> findByMockSessionUserIdAndSubject(
        Long userId, String subject);

    // Count correct answers for a user in a specific topic
    int countByMockSessionUserIdAndTopicAndResult(
        Long userId, String topic, AnswerResult result);

    // Fetch all pending AI evaluation answers
    // Used by background job to send to Claude API
    List<MockAnswer> findByIsAiEvaluatedFalseAndResultNot(
        AnswerResult result);

    @Query("SELECT m FROM MockAnswer m WHERE m.result = :result AND m.isAiEvaluated = false")
    List<MockAnswer> findPendingAiEvaluation(@Param("result") AnswerResult result);
    
    // Check if answer already exists for session + question
    // Prevents duplicate answer entries
    boolean existsByMockSessionIdAndQuestionId(
        Long sessionId, Long questionId);
}
