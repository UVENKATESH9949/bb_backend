
package com.BrainBlitz.repository;

import com.BrainBlitz.entity.WritingQuestion;
import com.BrainBlitz.enums.WritingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WritingQuestionRepository extends JpaRepository<WritingQuestion, Long> {

    Optional<WritingQuestion> findByQuestionId(Long questionId);

    // Fetch all pending AI grading — useful for Phase 3 batch grading job
    List<WritingQuestion> findByGradingStatus(String gradingStatus);

    List<WritingQuestion> findByWritingType(WritingType writingType);
}