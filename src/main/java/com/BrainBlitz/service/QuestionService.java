// src/main/java/com/BrainBlitz/service/QuestionService.java

package com.BrainBlitz.service;

import com.BrainBlitz.dto.request.*;
import com.BrainBlitz.dto.response.*;
import com.BrainBlitz.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    // ── Create ────────────────────────────────────────────────────────
    QuestionResponse createMcqQuestion(McqQuestionRequest request);
    QuestionResponse createFillBlank(FillBlankRequest request);
    QuestionResponse createArrangement(ArrangementRequest request);
    QuestionResponse createImageQuestion(ImageQuestionRequest request);
    QuestionResponse createQuestionGroup(QuestionGroupRequest request);
    QuestionResponse addQuestionsToGroup(Long groupId, McqQuestionRequest request);
    QuestionResponse createCodingQuestion(CodingQuestionRequest request);
    QuestionResponse createCodeSnippet(CodeSnippetRequest request);
    QuestionResponse createWritingQuestion(WritingQuestionRequest request);

    // ── Read ──────────────────────────────────────────────────────────
    QuestionResponse getQuestionById(Long id);
    Page<QuestionSummaryResponse> getAllQuestions(
        QuestionType questionType,
        ExamCategory examCategory,
        ExamType examType,
        String subject,
        String topic,
        DifficultyLevel difficultyLevel,
        Language language,
        Boolean isActive,
        Boolean isAiGenerated,
        Pageable pageable
    );

    // ── Update ────────────────────────────────────────────────────────
    QuestionResponse updateBaseQuestion(Long id, McqQuestionRequest request);
    void updateStatus(Long id, Boolean isActive);
    void bulkUpdateStatus(List<Long> ids, Boolean isActive);

    // ── Delete ────────────────────────────────────────────────────────
    void deleteQuestion(Long id);
    void bulkDeleteQuestions(List<Long> ids);
}