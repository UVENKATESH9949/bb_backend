package com.BrainBlitz.controller;

import com.BrainBlitz.dto.ApiResponse;
import com.BrainBlitz.dto.request.ExamPatternRequest;
import com.BrainBlitz.dto.response.ExamPatternResponse;
import com.BrainBlitz.enums.ExamType;
import com.BrainBlitz.service.ExamPatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/exam-patterns")
public class ExamPatternController {

    @Autowired
    private ExamPatternService examPatternService;

    // ✅ Create new exam pattern
    // POST /api/admin/exam-patterns
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createExamPattern(
            @RequestBody ExamPatternRequest request) {
        try {
            ExamPatternResponse response = examPatternService
                .createExamPattern(request);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Exam pattern created successfully",
                    response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Get pattern by id
    // GET /api/admin/exam-patterns/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getExamPatternById(
            @PathVariable Long id) {
        try {
            ExamPatternResponse response = examPatternService
                .getExamPatternById(id);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Success", response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Get all patterns for an exam
    // GET /api/admin/exam-patterns/exam/{examType}
    @GetMapping("/exam/{examType}")
    public ResponseEntity<ApiResponse<?>> getPatternsByExamType(
            @PathVariable ExamType examType) {
        try {
            List<ExamPatternResponse> response = examPatternService
                .getPatternsByExamType(examType);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Success", response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Get patterns for exam + round
    // GET /api/admin/exam-patterns/exam/{examType}/round?roundName=Aptitude Round
    @GetMapping("/exam/{examType}/round")
    public ResponseEntity<ApiResponse<?>> getPatternsByExamTypeAndRound(
            @PathVariable ExamType examType,
            @RequestParam String roundName) {
        try {
            List<ExamPatternResponse> response = examPatternService
                .getPatternsByExamTypeAndRound(examType, roundName);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Success", response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Get compulsory patterns only
    // GET /api/admin/exam-patterns/exam/{examType}/compulsory
    @GetMapping("/exam/{examType}/compulsory")
    public ResponseEntity<ApiResponse<?>> getCompulsoryPatterns(
            @PathVariable ExamType examType) {
        try {
            List<ExamPatternResponse> response = examPatternService
                .getCompulsoryPatterns(examType);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Success", response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Update exam pattern
    // PUT /api/admin/exam-patterns/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateExamPattern(
            @PathVariable Long id,
            @RequestBody ExamPatternRequest request) {
        try {
            ExamPatternResponse response = examPatternService
                .updateExamPattern(id, request);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Exam pattern updated successfully",
                    response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Deactivate exam pattern
    // DELETE /api/admin/exam-patterns/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deactivateExamPattern(
            @PathVariable Long id) {
        try {
            examPatternService.deactivateExamPattern(id);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Exam pattern deactivated successfully",
                    null, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }
}