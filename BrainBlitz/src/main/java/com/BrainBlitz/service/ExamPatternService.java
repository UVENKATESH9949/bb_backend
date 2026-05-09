package com.BrainBlitz.service;

import com.BrainBlitz.dto.request.ExamPatternRequest;
import com.BrainBlitz.dto.response.ExamPatternResponse;
import com.BrainBlitz.enums.ExamType;
import java.util.List;

public interface ExamPatternService {

    // Create new exam pattern
    ExamPatternResponse createExamPattern(ExamPatternRequest request);

    // Get all patterns for an exam
    List<ExamPatternResponse> getPatternsByExamType(ExamType examType);

    // Get patterns for specific exam and round
    // Used for placement mocks
    List<ExamPatternResponse> getPatternsByExamTypeAndRound(
        ExamType examType, String roundName);

    // Get only compulsory patterns for an exam
    List<ExamPatternResponse> getCompulsoryPatterns(ExamType examType);

    // Update existing pattern
    ExamPatternResponse updateExamPattern(Long id, ExamPatternRequest request);

    // Soft delete — sets isActive to false
    void deactivateExamPattern(Long id);

    // Get pattern by id
    ExamPatternResponse getExamPatternById(Long id);
}