// src/main/java/com/BrainBlitz/controller/QuestionController.java

package com.BrainBlitz.controller;

import com.BrainBlitz.dto.request.*;
import com.BrainBlitz.dto.response.*;
import com.BrainBlitz.enums.*;
import com.BrainBlitz.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/questions")
public class QuestionController {

    private QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // ════════════════════════════════════════════════════════════════
    // CREATE ENDPOINTS
    // ═════════════════════════════════════════════════════════════════

    // Handles: mcq, multi_correct, true_false,
    //          synonym_antonym, error_spotting
    @PostMapping("/mcq")
    public ResponseEntity<ApiResponse<QuestionResponse>> createMcq(
            @Valid @RequestBody McqQuestionRequest request) {

        QuestionResponse response = questionService.createMcqQuestion(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "MCQ question created successfully"));
    }

    // Handles: fill_blank, text
    @PostMapping("/fill-blank")
    public ResponseEntity<ApiResponse<QuestionResponse>> createFillBlank(
            @Valid @RequestBody FillBlankRequest request) {

        QuestionResponse response = questionService.createFillBlank(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Fill blank question created successfully"));
    }

    // Handles: sentence_arrangement, para_jumble
    @PostMapping("/arrangement")
    public ResponseEntity<ApiResponse<QuestionResponse>> createArrangement(
            @Valid @RequestBody ArrangementRequest request) {

        QuestionResponse response = questionService.createArrangement(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Arrangement question created successfully"));
    }

    // Handles: all 7 image types
    @PostMapping("/image")
    public ResponseEntity<ApiResponse<QuestionResponse>> createImageQuestion(
            @Valid @RequestBody ImageQuestionRequest request) {

        QuestionResponse response = questionService.createImageQuestion(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Image question created successfully"));
    }

    // Handles: RC, cloze, all 5 DI types — creates parent group
    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<QuestionResponse>> createQuestionGroup(
            @Valid @RequestBody QuestionGroupRequest request) {

        QuestionResponse response = questionService.createQuestionGroup(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Question group created successfully"));
    }

    // Add MCQ questions under an existing group
    @PostMapping("/groups/{groupId}/questions")
    public ResponseEntity<ApiResponse<QuestionResponse>> addQuestionsToGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody McqQuestionRequest request) {

        QuestionResponse response = questionService.addQuestionsToGroup(groupId, request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Question added to group successfully"));
    }

    // Handles: code_write, code_debug (fix mode)
    @PostMapping("/coding")
    public ResponseEntity<ApiResponse<QuestionResponse>> createCodingQuestion(
            @Valid @RequestBody CodingQuestionRequest request) {

        QuestionResponse response = questionService.createCodingQuestion(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Coding question created successfully"));
    }

    // Handles: code_output, code_fill, sql_output,
    //          code_debug (MCQ), flowchart_mcq
    @PostMapping("/code-snippet")
    public ResponseEntity<ApiResponse<QuestionResponse>> createCodeSnippet(
            @Valid @RequestBody CodeSnippetRequest request) {

        QuestionResponse response = questionService.createCodeSnippet(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Code snippet question created successfully"));
    }

    // Handles: email_write, essay_write,
    //          speech_round, listening_comp
    @PostMapping("/writing")
    public ResponseEntity<ApiResponse<QuestionResponse>> createWritingQuestion(
            @Valid @RequestBody WritingQuestionRequest request) {

        QuestionResponse response = questionService.createWritingQuestion(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Writing question created successfully"));
    }


    // ═════════════════════════════════════════════════════════════════
    // READ ENDPOINTS
    // ═════════════════════════════════════════════════════════════════

    // Get all questions — paginated + filtered
    // All filter params are optional
    @GetMapping
    public ResponseEntity<ApiResponse<Page<QuestionSummaryResponse>>> getAllQuestions(

            @RequestParam(required = false) QuestionType questionType,
            @RequestParam(required = false) ExamCategory examCategory,
            @RequestParam(required = false) ExamType examType,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) DifficultyLevel difficultyLevel,
            @RequestParam(required = false) Language language,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isAiGenerated,

            // Pagination params
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<QuestionSummaryResponse> result = questionService.getAllQuestions(
            questionType, examCategory, examType, subject, topic,
            difficultyLevel, language, isActive, isAiGenerated, pageable
        );

        return ResponseEntity.ok(
            ApiResponse.success(result, "Questions fetched successfully"));
    }

    // Get single question — full detail with all child data
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestionById(
            @PathVariable Long id) {

        QuestionResponse response = questionService.getQuestionById(id);
        return ResponseEntity.ok(
            ApiResponse.success(response, "Question fetched successfully"));
    }


    // ═════════════════════════════════════════════════════════════════
    // UPDATE ENDPOINTS
    // ═════════════════════════════════════════════════════════════════

    // Update base fields — works for any question type
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> updateQuestion(
            @PathVariable Long id,
            @RequestBody McqQuestionRequest request) {

        QuestionResponse response = questionService.updateBaseQuestion(id, request);
        return ResponseEntity.ok(
            ApiResponse.success(response, "Question updated successfully"));
    }

    // Activate or deactivate single question
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {

        Boolean isActive = body.get("isActive");
        if (isActive == null)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("isActive field is required"));

        questionService.updateStatus(id, isActive);

        String msg = Boolean.TRUE.equals(isActive)
            ? "Question activated successfully"
            : "Question deactivated successfully";

        return ResponseEntity.ok(ApiResponse.success(null, msg));
    }

    // Bulk activate or deactivate
    @PatchMapping("/bulk/status")
    public ResponseEntity<ApiResponse<Void>> bulkUpdateStatus(
            @RequestBody Map<String, Object> body) {

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) body.get("questionIds");
        Boolean isActive = (Boolean) body.get("isActive");

        if (ids == null || ids.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("questionIds are required"));

        if (isActive == null)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("isActive field is required"));

        questionService.bulkUpdateStatus(ids, isActive);

        return ResponseEntity.ok(
            ApiResponse.success(null,
                ids.size() + " questions updated successfully"));
    }


    // ═════════════════════════════════════════════════════════════════
    // DELETE ENDPOINTS
    // ═════════════════════════════════════════════════════════════════

    // Delete single question — requires ?confirm=true
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean confirm) {

        if (!confirm)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                    "Add ?confirm=true to confirm deletion"));

        questionService.deleteQuestion(id);
        return ResponseEntity.ok(
            ApiResponse.success(null, "Question deleted successfully"));
    }

    // Bulk delete — requires ?confirm=true
    @DeleteMapping("/bulk")
    public ResponseEntity<ApiResponse<Void>> bulkDeleteQuestions(
            @RequestParam(defaultValue = "false") boolean confirm,
            @RequestBody Map<String, Object> body) {

        if (!confirm)
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                    "Add ?confirm=true to confirm bulk deletion"));

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) body.get("questionIds");

        if (ids == null || ids.isEmpty())
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("questionIds are required"));

        questionService.bulkDeleteQuestions(ids);
        return ResponseEntity.ok(
            ApiResponse.success(null,
                ids.size() + " questions deleted successfully"));
    }
}
