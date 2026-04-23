// src/main/java/com/BrainBlitz/repository/ImageQuestionRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.ImageQuestion;
import com.BrainBlitz.enums.ImageQuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageQuestionRepository extends JpaRepository<ImageQuestion, Long> {

    Optional<ImageQuestion> findByQuestionId(Long questionId);

    List<ImageQuestion> findByImageQuestionType(ImageQuestionType type);
}