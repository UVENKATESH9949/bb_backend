package com.BrainBlitz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.BrainBlitz.entity.QuestionExplanation;

@Repository
public interface QuestionExplanationRepository
        extends JpaRepository<QuestionExplanation, Long> {

}