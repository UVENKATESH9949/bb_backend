// src/main/java/com/BrainBlitz/repository/TestCaseRepository.java

package com.BrainBlitz.repository;

import com.BrainBlitz.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findByCodingQuestionId(Long codingQuestionId);

    // Only sample test cases — shown to user
    List<TestCase> findByCodingQuestionIdAndIsSampleTrue(Long codingQuestionId);

    void deleteAllByCodingQuestionId(Long codingQuestionId);
}