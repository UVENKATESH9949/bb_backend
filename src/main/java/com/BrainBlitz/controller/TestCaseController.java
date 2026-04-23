// src/main/java/com/BrainBlitz/controller/TestCaseController.java

package com.BrainBlitz.controller;

import com.BrainBlitz.dto.request.SolutionStepRequest;
import com.BrainBlitz.dto.request.TestCaseRequest;
import com.BrainBlitz.dto.response.ApiResponse;
import com.BrainBlitz.dto.response.QuestionResponse;
import com.BrainBlitz.service.TestCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/questions/{questionId}")
@RequiredArgsConstructor
public class TestCaseController {

    private  TestCaseService testCaseService;


    // ═════════════════════════════════════════════════════════════════
    // TEST CASE ENDPOINTS
    // ═════════════════════════════════════════════════════════════════

    // Add multiple test cases in one call
    @PostMapping("/testcases")
    public ResponseEntity<ApiResponse<QuestionResponse>> addTestCases(
            @PathVariable Long questionId,
            @RequestBody List<@Valid TestCaseRequest> requests) {

        if (requests == null || requests.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("At least one test case is required"));

        QuestionResponse response = testCaseService.addTestCases(questionId, requests);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response,
                requests.size() + " test case(s) added successfully"));
    }

    // Bulk import test cases — from JSON array body
    // Skips duplicates automatically
    @PostMapping("/testcases/bulk")
    public ResponseEntity<ApiResponse<QuestionResponse>> bulkImportTestCases(
            @PathVariable Long questionId,
            @RequestBody List<@Valid TestCaseRequest> requests) {

        if (requests == null || requests.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Test cases list cannot be empty"));

        QuestionResponse response =
            testCaseService.bulkImportTestCases(questionId, requests);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response,
                "Bulk test case import completed successfully"));
    }

    // Update single test case
    @PutMapping("/testcases/{testCaseId}")
    public ResponseEntity<ApiResponse<QuestionResponse>> updateTestCase(
            @PathVariable Long questionId,
            @PathVariable Long testCaseId,
            @RequestBody TestCaseRequest request) {

        QuestionResponse response =
            testCaseService.updateTestCase(questionId, testCaseId, request);

        return ResponseEntity.ok(
            ApiResponse.success(response, "Test case updated successfully"));
    }

    // Delete single test case
    @DeleteMapping("/testcases/{testCaseId}")
    public ResponseEntity<ApiResponse<Void>> deleteTestCase(
            @PathVariable Long questionId,
            @PathVariable Long testCaseId) {

        testCaseService.deleteTestCase(questionId, testCaseId);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Test case deleted successfully"));
    }


    // ═════════════════════════════════════════════════════════════════
    // SOLUTION STEP ENDPOINTS
    // ═════════════════════════════════════════════════════════════════

    // Add multiple solution steps in one call
    @PostMapping("/steps")
    public ResponseEntity<ApiResponse<QuestionResponse>> addSolutionSteps(
            @PathVariable Long questionId,
            @RequestBody List<@Valid SolutionStepRequest> requests) {

        if (requests == null || requests.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("At least one solution step is required"));

        QuestionResponse response =
            testCaseService.addSolutionSteps(questionId, requests);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response,
                requests.size() + " solution step(s) added successfully"));
    }

    // Update single solution step
    @PutMapping("/steps/{stepId}")
    public ResponseEntity<ApiResponse<QuestionResponse>> updateSolutionStep(
            @PathVariable Long questionId,
            @PathVariable Long stepId,
            @RequestBody SolutionStepRequest request) {

        QuestionResponse response =
            testCaseService.updateSolutionStep(questionId, stepId, request);

        return ResponseEntity.ok(
            ApiResponse.success(response, "Solution step updated successfully"));
    }

    // Delete single solution step
    @DeleteMapping("/steps/{stepId}")
    public ResponseEntity<ApiResponse<Void>> deleteSolutionStep(
            @PathVariable Long questionId,
            @PathVariable Long stepId) {

        testCaseService.deleteSolutionStep(questionId, stepId);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Solution step deleted successfully"));
    }
}