// src/main/java/com/BrainBlitz/repository/ArrangementQuestionRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.ArrangementQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArrangementQuestionRepository extends JpaRepository<ArrangementQuestion, Long> {

    Optional<ArrangementQuestion> findByQuestionId(Long questionId);
}