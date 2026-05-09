package com.BrainBlitz.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.BrainBlitz.entity.PracticeSession;
import com.BrainBlitz.entity.User;

@Repository
public interface PracticeSessionRepository
    extends JpaRepository<PracticeSession, Long> {

    // All practice sessions for a user
    List<PracticeSession> findByUserIdOrderByPracticedAtDesc(Long userId);

    // Sessions for specific subject
    List<PracticeSession> findByUserIdAndSubject(Long userId, String subject);

    // Count total practice sessions
    int countByUserId(Long userId);

    // Recent sessions — last 7 days
    List<PracticeSession> findByUserIdAndPracticedAtAfter(
        Long userId, LocalDateTime date);
    
    @Query("SELECT p FROM PracticeSession p WHERE p.user = :user AND p.subject = :subject ORDER BY p.practicedAt DESC")
    List<PracticeSession> findTopByUserAndSubjectOrderByCreatedAtDesc(
        @Param("user") User user, 
        @Param("subject") String subject, 
        PageRequest pageRequest);
}