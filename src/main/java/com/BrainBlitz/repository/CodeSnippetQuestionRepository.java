// src/main/java/com/BrainBlitz/repository/CodeSnippetQuestionRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.CodeSnippetQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeSnippetQuestionRepository extends JpaRepository<CodeSnippetQuestion, Long> {

    Optional<CodeSnippetQuestion> findByQuestionId(Long questionId);
}