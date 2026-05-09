package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.request.ExamPatternRequest;
import com.BrainBlitz.dto.response.ExamPatternResponse;
import com.BrainBlitz.entity.ExamPattern;
import com.BrainBlitz.enums.ExamType;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.ExamPatternRepository;
import com.BrainBlitz.service.ExamPatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamPatternServiceImpl implements ExamPatternService {

    @Autowired
    private ExamPatternRepository examPatternRepository;

    // ─────────────────────────────────────────────
    // CREATE
    // ─────────────────────────────────────────────

    @Override
    public ExamPatternResponse createExamPattern(ExamPatternRequest request) {

        // Check if pattern already exists for this exam + subject + topic
        if (examPatternRepository.existsByExamTypeAndSubjectAndTopic(
                request.getExamType(),
                request.getSubject(),
                request.getTopic())) {
            throw new RuntimeException(
                "Pattern already exists for " +
                request.getExamType() + " - " +
                request.getSubject() + " - " +
                request.getTopic()
            );
        }

        ExamPattern pattern = buildExamPattern(request);
        examPatternRepository.save(pattern);
        return mapToResponse(pattern);
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @Override
    public ExamPatternResponse getExamPatternById(Long id) {
        ExamPattern pattern = examPatternRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Exam pattern not found with id: " + id));
        return mapToResponse(pattern);
    }

    @Override
    public List<ExamPatternResponse> getPatternsByExamType(ExamType examType) {
        return examPatternRepository
            .findByExamTypeAndIsActiveTrue(examType)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ExamPatternResponse> getPatternsByExamTypeAndRound(
            ExamType examType, String roundName) {
        return examPatternRepository
            .findByExamTypeAndRoundNameAndIsActiveTrue(examType, roundName)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ExamPatternResponse> getCompulsoryPatterns(ExamType examType) {
        return examPatternRepository
            .findByExamTypeAndIsCompulsoryTrueAndIsActiveTrue(examType)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────

    @Override
    public ExamPatternResponse updateExamPattern(Long id, ExamPatternRequest request) {
        ExamPattern pattern = examPatternRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Exam pattern not found with id: " + id));

        pattern.setExamType(request.getExamType());
        pattern.setRoundName(request.getRoundName());
        pattern.setSubject(request.getSubject());
        pattern.setTopic(request.getTopic());
        pattern.setQuestionsCount(request.getQuestionsCount());
        pattern.setWeightagePercent(request.getWeightagePercent());
        pattern.setEasyCount(request.getEasyCount());
        pattern.setMediumCount(request.getMediumCount());
        pattern.setHardCount(request.getHardCount());
        pattern.setMarksPerQuestion(request.getMarksPerQuestion());
        pattern.setNegativeMarks(request.getNegativeMarks());
        pattern.setTimeLimitSeconds(request.getTimeLimitSeconds());
        pattern.setCompulsory(request.getIsCompulsory());

        examPatternRepository.save(pattern);
        return mapToResponse(pattern);
    }

    // ─────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────

    @Override
    public void deactivateExamPattern(Long id) {
        ExamPattern pattern = examPatternRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Exam pattern not found with id: " + id));
        pattern.setActive(false);
        examPatternRepository.save(pattern);
    }

    // ─────────────────────────────────────────────
    // HELPER — Build Entity From Request
    // ─────────────────────────────────────────────

    private ExamPattern buildExamPattern(ExamPatternRequest request) {
        ExamPattern pattern = new ExamPattern();
        pattern.setExamType(request.getExamType());
        pattern.setRoundName(request.getRoundName());
        pattern.setSubject(request.getSubject());
        pattern.setTopic(request.getTopic());
        pattern.setQuestionsCount(request.getQuestionsCount());
        pattern.setWeightagePercent(request.getWeightagePercent());
        pattern.setEasyCount(request.getEasyCount());
        pattern.setMediumCount(request.getMediumCount());
        pattern.setHardCount(request.getHardCount());
        pattern.setMarksPerQuestion(request.getMarksPerQuestion());
        pattern.setNegativeMarks(request.getNegativeMarks());
        pattern.setTimeLimitSeconds(request.getTimeLimitSeconds());
        pattern.setCompulsory(request.getIsCompulsory());
        pattern.setActive(true);
        return pattern;
    }

    // ─────────────────────────────────────────────
    // HELPER — Map Entity To Response
    // ─────────────────────────────────────────────

    private ExamPatternResponse mapToResponse(ExamPattern pattern) {
        ExamPatternResponse response = new ExamPatternResponse();
        response.setId(pattern.getId());
        response.setExamType(pattern.getExamType());
        response.setRoundName(pattern.getRoundName());
        response.setSubject(pattern.getSubject());
        response.setTopic(pattern.getTopic());
        response.setQuestionsCount(pattern.getQuestionsCount());
        response.setWeightagePercent(pattern.getWeightagePercent());
        response.setEasyCount(pattern.getEasyCount());
        response.setMediumCount(pattern.getMediumCount());
        response.setHardCount(pattern.getHardCount());
        response.setMarksPerQuestion(pattern.getMarksPerQuestion());
        response.setNegativeMarks(pattern.getNegativeMarks());
        response.setTimeLimitSeconds(pattern.getTimeLimitSeconds());
        response.setIsCompulsory(pattern.isCompulsory());
        response.setIsActive(pattern.isActive());
        return response;
    }
}