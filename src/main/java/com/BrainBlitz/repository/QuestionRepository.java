// src/main/java/com/BrainBlitz/repository/QuestionRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.Question;
import com.BrainBlitz.enums.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Main filter query — all params optional
    @Query("""
        SELECT q FROM Question q
        WHERE (:questionType IS NULL OR q.questionType = :questionType)
        AND (:examCategory IS NULL OR q.examCategory = :examCategory)
        AND (:examType IS NULL OR q.examType = :examType)
        AND (:subject IS NULL OR LOWER(q.subject) = LOWER(CAST(:subject AS string)))
    	AND (:topic IS NULL OR LOWER(q.topic) = LOWER(CAST(:topic AS string)))
        AND (:difficultyLevel IS NULL OR q.difficultyLevel = :difficultyLevel)
        AND (:language IS NULL OR q.language = :language)
        AND (:isActive IS NULL OR q.isActive = :isActive)
        AND (:isAiGenerated IS NULL OR q.isAiGenerated = :isAiGenerated)
    """)
    Page<Question> findAllWithFilters(
        @Param("questionType") QuestionType questionType,
        @Param("examCategory") ExamCategory examCategory,
        @Param("examType") ExamType examType,
        @Param("subject") String subject,
        @Param("topic") String topic,
        @Param("difficultyLevel") DifficultyLevel difficultyLevel,
        @Param("language") Language language,
        @Param("isActive") Boolean isActive,
        @Param("isAiGenerated") Boolean isAiGenerated,
        Pageable pageable
    );

	 // Fetch active questions for question selection engine
	 // Used by MockSessionService to pick questions per topic
	 @Query("""
	     SELECT q FROM Question q
	     WHERE q.examType = :examType
	     AND LOWER(q.subject) = LOWER(:subject)
	     AND LOWER(q.topic) = LOWER(:topic)
	     AND q.isActive = true
	     AND q.difficultyLevel IN :difficultyLevels
	     ORDER BY FUNCTION('RANDOM')
	 """)
	 List<Question> findQuestionsForMock(
	     @Param("examType") ExamType examType,
	     @Param("subject") String subject,
	     @Param("topic") String topic,
	     @Param("difficultyLevels") List<DifficultyLevel> difficultyLevels
	 );
    // Count active questions per exam type — useful for admin dashboard
    @Query("SELECT COUNT(q) FROM Question q WHERE q.examType = :examType AND q.isActive = true")
    Long countActiveByExamType(@Param("examType") ExamType examType);

    // Count by question type — for stats
    @Query("SELECT COUNT(q) FROM Question q WHERE q.questionType = :questionType AND q.isActive = true")
    Long countActiveByQuestionType(@Param("questionType") QuestionType questionType);
    
    // ── NEW: needed by AdminServiceImpl ──────────────────────
    
    // Count only active questions (isActive = true)
    // Used for "active questions" dashboard stat
    long countByIsActiveTrue();
 
    // Count AI-generated questions
    // Used for "AI questions" dashboard stat
    long countByIsAiGeneratedTrue();
    
}