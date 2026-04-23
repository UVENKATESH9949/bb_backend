package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.request.MockAnswerRequest;
import com.BrainBlitz.dto.response.MockAnswerResponse;
import com.BrainBlitz.entity.*;
import com.BrainBlitz.enums.AnswerResult;
import com.BrainBlitz.enums.QuestionType;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.*;
import com.BrainBlitz.service.MockAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MockAnswerServiceImpl implements MockAnswerService {

    @Autowired
    private MockAnswerRepository mockAnswerRepository;

    @Autowired
    private MockSessionRepository mockSessionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // ─────────────────────────────────────────────
    // SAVE ANSWER
    // ─────────────────────────────────────────────

    @Override
    public MockAnswerResponse saveAnswer(Long sessionId, MockAnswerRequest request) {

        // Validate session exists
        MockSession session = mockSessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mock session not found with id: " + sessionId));

        // Validate question exists
        Question question = questionRepository.findById(request.getQuestionId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Question not found with id: " + request.getQuestionId()));

        // Prevent duplicate answer for same session + question
        if (mockAnswerRepository.existsByMockSessionIdAndQuestionId(
                sessionId, request.getQuestionId())) {
            throw new RuntimeException(
                "Answer already saved for question: " + request.getQuestionId());
        }

        MockAnswer answer = new MockAnswer();
        answer.setMockSession(session);
        answer.setQuestion(question);

        // Copy subject and topic from question — snapshot
        answer.setSubject(question.getSubject());
        answer.setTopic(question.getTopic());
        answer.setDifficultyLevel(question.getDifficultyLevel());

        answer.setUserAnswer(request.getUserAnswer());
        answer.setTimeTakenSeconds(request.getTimeTakenSeconds());
        answer.setAttempted(request.getUserAnswer() != null);
        answer.setMarkedReview(request.getIsMarkedReview());

        // Set initial result
        if (request.getUserAnswer() == null) {
            answer.setResult(AnswerResult.SKIPPED);
            answer.setMarksAwarded(0);
        } else {
            // Writing and speech → pending AI evaluation
            if (isAiEvaluatedType(question.getQuestionType())) {
                answer.setResult(AnswerResult.PENDING);
                answer.setMarksAwarded(0);
                answer.setAiEvaluated(false);
            } else {
                // All other types → evaluate immediately
                evaluateSingleAnswer(answer, question);
            }
        }

        mockAnswerRepository.save(answer);
        return mapToResponse(answer);
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @Override
    public List<MockAnswerResponse> getAnswersBySessionId(Long sessionId) {
        return mockAnswerRepository.findByMockSessionId(sessionId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<MockAnswerResponse> getWrongAnswers(Long sessionId) {
        return mockAnswerRepository
            .findByMockSessionIdAndResult(sessionId, AnswerResult.WRONG)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────
    // EVALUATE ALL ANSWERS ON SUBMIT
    // ─────────────────────────────────────────────

    @Override
    public void evaluateAnswers(Long sessionId) {

        List<MockAnswer> answers = mockAnswerRepository
            .findByMockSessionId(sessionId);

        for (MockAnswer answer : answers) {
            // Skip already evaluated or skipped
            if (answer.getResult() == AnswerResult.SKIPPED
                    || answer.getResult() == AnswerResult.PENDING) continue;

            // Skip already correctly evaluated
            if (answer.getResult() == AnswerResult.CORRECT
                    || answer.getResult() == AnswerResult.WRONG) continue;

            evaluateSingleAnswer(answer, answer.getQuestion());
            mockAnswerRepository.save(answer);
        }
    }

    // ─────────────────────────────────────────────
    // AI EVALUATION — For writing and speech
    // Called by background scheduler
    // ─────────────────────────────────────────────

    @Override
    public void processAiEvaluation(Long answerId) {
        MockAnswer answer = mockAnswerRepository.findById(answerId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Answer not found with id: " + answerId));

        // Skip if already evaluated
        if (answer.isAiEvaluated()) return;

        // TODO — Phase 3
        // Call Claude API here with:
        // - answer.getUserAnswer()
        // - question writing prompt
        // - evaluation criteria JSON
        // Get back score + feedback
        // Update answer.setAiScore()
        // Update answer.setAiFeedback()
        // Update answer.setAiEvaluated(true)
        // Update answer.setResult(CORRECT/WRONG based on score)
        // Update answer.setMarksAwarded()
    }

    // ─────────────────────────────────────────────
    // HELPER — Evaluate Single Answer
    // ─────────────────────────────────────────────

    private void evaluateSingleAnswer(MockAnswer answer, Question question) {
        String userAnswer = answer.getUserAnswer();
        String correctAnswer = getCorrectAnswer(question);

        if (userAnswer == null || userAnswer.isBlank()) {
            answer.setResult(AnswerResult.SKIPPED);
            answer.setMarksAwarded(0);
            return;
        }

        boolean isCorrect = userAnswer.trim()
            .equalsIgnoreCase(correctAnswer.trim());

        if (isCorrect) {
            answer.setResult(AnswerResult.CORRECT);
            answer.setMarksAwarded(question.getMarks());
        } else {
            answer.setResult(AnswerResult.WRONG);
            answer.setMarksAwarded(-question.getNegativeMarks());
        }
    }

    // ─────────────────────────────────────────────
    // HELPER — Get Correct Answer From Question
    // ─────────────────────────────────────────────

    private String getCorrectAnswer(Question question) {
        switch (question.getQuestionType()) {

            // MCQ — find option where isCorrect = true
            case MCQ:
            case TRUE_FALSE:
            case SYNONYM_ANTONYM:
            case ERROR_SPOTTING:
                return question.getOptions().stream()
                    .filter(McqOption::isCorrect)
                    .map(o -> String.valueOf(o.getOptionOrder()))
                    .findFirst()
                    .orElse("");

            // Fill blank — get first blank answer
            case FILL_BLANK:
            case TEXT_ANSWER:
                return question.getFillBlankAnswers().stream()
                    .filter(f -> f.getBlankPosition() == 1)
                    .map(FillBlankAnswer::getCorrectAnswer)
                    .findFirst()
                    .orElse("");

            // Arrangement — correct order string
            case SENTENCE_ARRANGEMENT:
            case PARA_JUMBLE:
                return question.getArrangementQuestion() != null
                    ? question.getArrangementQuestion().getCorrectOrder()
                    : "";

            // Image — correct option index
            case MIRROR_IMAGE:
            case WATER_IMAGE:
            case PATTERN_MATRIX:
            case FIGURE_SERIES:
            case ODD_ONE_OUT:
                return question.getImageQuestion() != null
                    ? String.valueOf(question.getImageQuestion()
                        .getCorrectOptionIndex())
                    : "";

            // Code snippet — accepted answers JSON
            case CODE_OUTPUT:
            case SQL_OUTPUT:
            case CODE_FILL:
                return question.getCodeSnippetQuestion() != null
                    ? question.getCodeSnippetQuestion()
                        .getAcceptedAnswersJson()
                    : "";

            default:
                return "";
        }
    }

    // ─────────────────────────────────────────────
    // HELPER — Is AI Evaluated Type
    // ─────────────────────────────────────────────

    private boolean isAiEvaluatedType(QuestionType type) {
        return type == QuestionType.EMAIL_WRITE
            || type == QuestionType.ESSAY_WRITE
            || type == QuestionType.SPEECH_ROUND
            || type == QuestionType.LISTENING_COMP;
    }

    // ─────────────────────────────────────────────
    // HELPER — Map Entity To Response
    // ─────────────────────────────────────────────

    private MockAnswerResponse mapToResponse(MockAnswer answer) {
        MockAnswerResponse response = new MockAnswerResponse();
        response.setId(answer.getId());
        response.setSessionId(answer.getMockSession().getId());
        response.setQuestionId(answer.getQuestion().getId());
        response.setSubject(answer.getSubject());
        response.setTopic(answer.getTopic());
        response.setDifficultyLevel(answer.getDifficultyLevel());
        response.setUserAnswer(answer.getUserAnswer());
        response.setResult(answer.getResult());
        response.setMarksAwarded(answer.getMarksAwarded());
        response.setTimeTakenSeconds(answer.getTimeTakenSeconds());
        response.setIsAttempted(answer.isAttempted());
        response.setIsMarkedReview(answer.isMarkedReview());
        response.setAiScore(answer.getAiScore());
        response.setAiFeedback(answer.getAiFeedback());
        response.setIsAiEvaluated(answer.isAiEvaluated());
        return response;
    }
}