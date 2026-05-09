package com.BrainBlitz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BrainBlitz.entity.ExamPattern;
import com.BrainBlitz.enums.ExamType;

@Repository
public interface ExamPatternRepository 
    extends JpaRepository<ExamPattern, Long> {

    // Fetch all patterns for a specific exam
    List<ExamPattern> findByExamTypeAndIsActiveTrue(ExamType examType);

    // Fetch patterns for a specific exam and round
    // Used for placement mocks — each round has different pattern
    List<ExamPattern> findByExamTypeAndRoundNameAndIsActiveTrue(
        ExamType examType, String roundName);

    // Fetch compulsory topics only for an exam
    List<ExamPattern> findByExamTypeAndIsCompulsoryTrueAndIsActiveTrue(
        ExamType examType);

    // Check if pattern already exists for exam + subject + topic
    // Used during bulk import to avoid duplicates
    boolean existsByExamTypeAndSubjectAndTopic(
        ExamType examType, String subject, String topic);
}
