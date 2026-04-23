package com.BrainBlitz.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BrainBlitz.entity.MockSession;
import com.BrainBlitz.enums.ExamType;
import com.BrainBlitz.enums.MockStatus;

@Repository
public interface MockSessionRepository 
    extends JpaRepository<MockSession, Long> {

    // Fetch all mocks for a user
    List<MockSession> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Fetch all completed mocks for a user
    List<MockSession> findByUserIdAndStatusOrderByCreatedAtDesc(
        Long userId, MockStatus status);

    // Fetch all mocks for a user by exam type
    List<MockSession> findByUserIdAndExamTypeOrderByCreatedAtDesc(
        Long userId, ExamType examType);

    // Check if user has any IN_PROGRESS mock
    // User should not start new mock if one is already in progress
    Optional<MockSession> findByUserIdAndStatus(
        Long userId, MockStatus status);

    // Count total mocks completed by user
    int countByUserIdAndStatus(Long userId, MockStatus status);

    // Fetch last N mocks for a user
    // Used for recent performance analysis
    List<MockSession> findTop5ByUserIdAndStatusOrderByCreatedAtDesc(
        Long userId, MockStatus status);

    // Fetch mocks where performance is not yet processed
    // Used by background job to process pending results
    List<MockSession> findByIsPerformanceProcessedFalseAndStatus(
        MockStatus status);
    
 // ── NEW: needed by AdminServiceImpl ──────────────────────
    
    // Count sessions started between two timestamps
    // Used for "mocks today" dashboard stat
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
 
    // Count distinct users who started a mock after a given timestamp
    // Used for "active users this week" dashboard stat
    @Query("SELECT COUNT(DISTINCT m.user.id) FROM MockSession m WHERE m.createdAt >= :since")
    long countDistinctUserIdSince(@Param("since") LocalDateTime since);
 
    // Average accuracy across all COMPLETED mock sessions
    // Returns null if no completed mocks exist yet — handled in service
    @Query("SELECT AVG(m.accuracyPercentage) FROM MockSession m WHERE m.status = com.BrainBlitz.enums.MockStatus.COMPLETED")
    Double findAverageAccuracy();
    
    
}
