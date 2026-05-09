// src/main/java/com/BrainBlitz/repository/SolutionStepRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.SolutionStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionStepRepository extends JpaRepository<SolutionStep, Long> {

    // Always fetch in order
    List<SolutionStep> findByCodingQuestionIdOrderByStepNumber(Long codingQuestionId);

    void deleteAllByCodingQuestionId(Long codingQuestionId);
}