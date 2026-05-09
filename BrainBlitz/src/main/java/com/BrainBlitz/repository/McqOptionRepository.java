// src/main/java/com/BrainBlitz/repository/McqOptionRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.McqOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface McqOptionRepository extends JpaRepository<McqOption, Long> {

    List<McqOption> findByQuestionIdOrderByOptionOrder(Long questionId);

    void deleteAllByQuestionId(Long questionId);
}