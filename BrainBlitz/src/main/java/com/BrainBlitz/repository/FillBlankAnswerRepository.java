// src/main/java/com/BrainBlitz/repository/FillBlankAnswerRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.FillBlankAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FillBlankAnswerRepository extends JpaRepository<FillBlankAnswer, Long> {

    List<FillBlankAnswer> findByQuestionIdOrderByBlankPosition(Long questionId);

    void deleteAllByQuestionId(Long questionId);
}