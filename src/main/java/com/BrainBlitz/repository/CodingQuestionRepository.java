// src/main/java/com/BrainBlitz/repository/CodingQuestionRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.CodingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, Long> {

    Optional<CodingQuestion> findByQuestionId(Long questionId);
}