package com.BrainBlitz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BrainBlitz.entity.UserWeakArea;

@Repository
public interface UserWeakAreaRepository 
    extends JpaRepository<UserWeakArea, Long> {

    // Fetch all weak areas for a user
    List<UserWeakArea> findByUserId(Long userId);

    // Fetch only weak topics for a user
    // Used by question selection engine
    List<UserWeakArea> findByUserIdAndIsWeakTrue(Long userId);

    // Fetch only slow topics for a user
    List<UserWeakArea> findByUserIdAndIsSlowTrue(Long userId);

    // Fetch both weak and slow topics
    List<UserWeakArea> findByUserIdAndIsWeakTrueOrIsSlowTrue(
        Long userId);

    // Fetch specific subject weak areas for a user
    // Used when mock is for specific subject
    List<UserWeakArea> findByUserIdAndSubjectAndIsWeakTrue(
        Long userId, String subject);

    // Fetch specific topic for a user
    // Used to update existing record after mock
    Optional<UserWeakArea> findByUserIdAndSubjectAndTopic(
        Long userId, String subject, String topic);

    // Check if record exists before creating new one
    boolean existsByUserIdAndSubjectAndTopic(
        Long userId, String subject, String topic);

    // Fetch weak areas ordered by accuracy
    // Lowest accuracy = most weak = highest priority
    List<UserWeakArea> findByUserIdAndIsWeakTrueOrderByAccuracyPercentageAsc(
        Long userId);
}
