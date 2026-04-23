// src/main/java/com/BrainBlitz/service/TestCaseService.java

package com.BrainBlitz.service;

import com.BrainBlitz.dto.request.SolutionStepRequest;
import com.BrainBlitz.dto.request.TestCaseRequest;
import com.BrainBlitz.dto.response.QuestionResponse;

import java.util.List;

public interface TestCaseService {

    // ── Test Cases ────────────────────────────────────────────────────
    QuestionResponse addTestCases(Long questionId, List<TestCaseRequest> requests);
    QuestionResponse bulkImportTestCases(Long questionId, List<TestCaseRequest> requests);
    QuestionResponse updateTestCase(Long questionId, Long testCaseId, TestCaseRequest request);
    void deleteTestCase(Long questionId, Long testCaseId);

    // ── Solution Steps ────────────────────────────────────────────────
    QuestionResponse addSolutionSteps(Long questionId, List<SolutionStepRequest> requests);
    QuestionResponse updateSolutionStep(Long questionId, Long stepId, SolutionStepRequest request);
    void deleteSolutionStep(Long questionId, Long stepId);
}