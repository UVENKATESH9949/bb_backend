// src/main/java/com/BrainBlitz/service/impl/QuizServiceImpl.java
package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.request.CheckAnswerRequest;
import com.BrainBlitz.dto.response.CheckAnswerResponse;
import com.BrainBlitz.entity.*;
import com.BrainBlitz.enums.QuestionType;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.*;
import com.BrainBlitz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ArrangementQuestionRepository arrangementQuestionRepository;

    @Autowired
    private FillBlankAnswerRepository fillBlankAnswerRepository;

    // ─────────────────────────────────────────────
    // MAIN ENTRY POINT
    // ─────────────────────────────────────────────

    @Override
    public CheckAnswerResponse checkAnswer(CheckAnswerRequest request, User user) {

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Question not found: " + request.getQuestionId()));

        QuestionType type = question.getQuestionType();

        // Route to correct checker based on question type
        if (isMcqType(type)) {
            return checkMcqAnswer(question, request);
        } else if (type == QuestionType.MULTI_CORRECT) {
            return checkMultiCorrectAnswer(question, request);
        } else if (type == QuestionType.FILL_BLANK) {
            return checkFillBlankAnswer(question, request);
        } else if (type == QuestionType.SENTENCE_ARRANGEMENT
                || type == QuestionType.PARA_JUMBLE) {
            return checkArrangementAnswer(question, request);
        } else {
            // TEXT_ANSWER, EMAIL_WRITE, ESSAY_WRITE etc. — AI evaluated
            // Return pending response; AI scheduler handles actual scoring
            return buildPendingResponse(question);
        }
    }

    // ─────────────────────────────────────────────
    // MCQ CHECKER
    // Covers: MCQ, TRUE_FALSE, SYNONYM_ANTONYM,
    //         IMAGE_MCQ, CODE_OUTPUT, SQL_OUTPUT,
    //         FLOWCHART_MCQ, ERROR_SPOTTING,
    //         and all DI / RC types
    // ─────────────────────────────────────────────

    private CheckAnswerResponse checkMcqAnswer(Question question,
                                                CheckAnswerRequest request) {

        List<McqOption> options = question.getOptions();

        // Find the option user selected
        McqOption selected = options.stream()
                .filter(o -> o.getId().equals(request.getSelectedOptionId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Option not found: " + request.getSelectedOptionId()));

        // Find the correct option(s)
        List<McqOption> correctOptions = options.stream()
                .filter(McqOption::isCorrect)
                .collect(Collectors.toList());

        boolean isCorrect = selected.isCorrect();
        double points = isCorrect
                ? question.getMarks()
                : -question.getNegativeMarks();

        // Build display string e.g. "B) Paris"
        String correctDisplay = correctOptions.stream()
                .map(o -> optionLabel(o.getOptionOrder()) + ") " + o.getOptionText())
                .collect(Collectors.joining(", "));

        // Explanation — prefer option-level, fallback to question-level
        String explanation = selected.getOptionExplanation() != null
                ? selected.getOptionExplanation()
                : getQuestionExplanation(question);

        CheckAnswerResponse res = new CheckAnswerResponse();
        res.setCorrect(isCorrect);
        res.setCorrectDisplay(correctDisplay);
        res.setExplanation(explanation);
        res.setPoints(points);
        res.setCorrectOptionIds(
                correctOptions.stream().map(McqOption::getId).collect(Collectors.toList()));
        res.setCorrectOptions(
                correctOptions.stream()
                        .map(o -> optionLabel(o.getOptionOrder()) + ") " + o.getOptionText())
                        .collect(Collectors.toList()));
        res.setCorrectOptionImages(
                correctOptions.stream()
                        .map(McqOption::getOptionImageUrl)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
        return res;
    }

    // ─────────────────────────────────────────────
    // MULTI CORRECT CHECKER
    // ─────────────────────────────────────────────

    private CheckAnswerResponse checkMultiCorrectAnswer(Question question,
                                                         CheckAnswerRequest request) {

        List<McqOption> options = question.getOptions();

        Set<Long> selectedIds = new HashSet<>(
                request.getSelectedOptionIds() != null
                        ? request.getSelectedOptionIds()
                        : Collections.emptyList());

        Set<Long> correctIds = options.stream()
                .filter(McqOption::isCorrect)
                .map(McqOption::getId)
                .collect(Collectors.toSet());

        boolean isCorrect = selectedIds.equals(correctIds);
        double points = isCorrect
                ? question.getMarks()
                : -question.getNegativeMarks();

        List<McqOption> correctOptions = options.stream()
                .filter(McqOption::isCorrect)
                .collect(Collectors.toList());

        String correctDisplay = correctOptions.stream()
                .map(o -> optionLabel(o.getOptionOrder()) + ") " + o.getOptionText())
                .collect(Collectors.joining(", "));

        CheckAnswerResponse res = new CheckAnswerResponse();
        res.setCorrect(isCorrect);
        res.setCorrectDisplay(correctDisplay);
        res.setExplanation(getQuestionExplanation(question));
        res.setPoints(points);
        res.setCorrectOptionIds(new ArrayList<>(correctIds));
        res.setCorrectOptions(
                correctOptions.stream()
                        .map(o -> optionLabel(o.getOptionOrder()) + ") " + o.getOptionText())
                        .collect(Collectors.toList()));
        res.setCorrectOptionImages(
                correctOptions.stream()
                        .map(McqOption::getOptionImageUrl)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
        return res;
    }

    // ─────────────────────────────────────────────
    // FILL BLANK CHECKER
    // ─────────────────────────────────────────────

    private CheckAnswerResponse checkFillBlankAnswer(Question question,
                                                      CheckAnswerRequest request) {

    	List<FillBlankAnswer> correctAnswers = question.getFillBlankAnswers();
    	if (correctAnswers == null || correctAnswers.isEmpty()) {
    	    CheckAnswerResponse res = new CheckAnswerResponse();
    	    res.setCorrect(false);
    	    res.setCorrectDisplay("No answer data available");
    	    res.setExplanation(null);
    	    res.setPoints(-question.getNegativeMarks());
    	    res.setCorrectOptions(Collections.emptyList());
    	    res.setCorrectOptionImages(Collections.emptyList());
    	    return res;
    	}

        String userAnswer = request.getTypedAnswer() != null
                ? request.getTypedAnswer().trim().toLowerCase()
                : "";

        boolean isCorrect = correctAnswers.stream()
                .anyMatch(a -> a.getCorrectAnswer().trim().toLowerCase().equals(userAnswer));

        double points = isCorrect
                ? question.getMarks()
                : -question.getNegativeMarks();

        String correctDisplay = correctAnswers.stream()
                .map(FillBlankAnswer::getCorrectAnswer)
                .collect(Collectors.joining(" / "));

        CheckAnswerResponse res = new CheckAnswerResponse();
        res.setCorrect(isCorrect);
        res.setCorrectDisplay(correctDisplay);
        res.setExplanation(getQuestionExplanation(question));
        res.setPoints(points);
        res.setCorrectOptions(List.of(correctDisplay));
        res.setCorrectOptionImages(Collections.emptyList());
        return res;
    }

    // ─────────────────────────────────────────────
    // ARRANGEMENT CHECKER
    // ─────────────────────────────────────────────

    private CheckAnswerResponse checkArrangementAnswer(Question question,
                                                        CheckAnswerRequest request) {

    	ArrangementQuestion arrangement = question.getArrangementQuestion();
    	if (arrangement == null) {
    	    CheckAnswerResponse res = new CheckAnswerResponse();
    	    res.setCorrect(false);
    	    res.setCorrectDisplay("No arrangement data available");
    	    res.setExplanation(null);
    	    res.setPoints(-question.getNegativeMarks());
    	    res.setCorrectOptions(Collections.emptyList());
    	    res.setCorrectOptionImages(Collections.emptyList());
    	    return res;
    	}

        String correctOrder = arrangement.getCorrectOrder(); // e.g. "P,Q,R,S"
        String userOrder = request.getArrangedOrder() != null
                ? request.getArrangedOrder().trim()
                : "";

        boolean isCorrect = correctOrder.trim().equalsIgnoreCase(userOrder.trim());
        double points = isCorrect
                ? question.getMarks()
                : -question.getNegativeMarks();

        CheckAnswerResponse res = new CheckAnswerResponse();
        res.setCorrect(isCorrect);
        res.setCorrectDisplay(correctOrder);
        res.setExplanation(getQuestionExplanation(question));
        res.setPoints(points);
        res.setCorrectOptions(List.of(correctOrder));
        res.setCorrectOptionImages(Collections.emptyList());
        return res;
    }

    // ─────────────────────────────────────────────
    // AI EVALUATED — pending response
    // ─────────────────────────────────────────────

    private CheckAnswerResponse buildPendingResponse(Question question) {
        CheckAnswerResponse res = new CheckAnswerResponse();
        res.setCorrect(false);
        res.setCorrectDisplay("AI Evaluation Pending");
        res.setExplanation("Your answer will be evaluated by AI shortly.");
        res.setPoints(0);
        res.setCorrectOptions(Collections.emptyList());
        res.setCorrectOptionImages(Collections.emptyList());
        return res;
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────

    // All types that use McqOption table for answer checking
    private boolean isMcqType(QuestionType type) {
        return type == QuestionType.MCQ
                || type == QuestionType.TRUE_FALSE
                || type == QuestionType.SYNONYM_ANTONYM
                || type == QuestionType.ERROR_SPOTTING
                || type == QuestionType.IMAGE_MCQ
                || type == QuestionType.IMAGE_OPTIONS
                || type == QuestionType.MIRROR_IMAGE
                || type == QuestionType.WATER_IMAGE
                || type == QuestionType.PATTERN_MATRIX
                || type == QuestionType.ODD_ONE_OUT
                || type == QuestionType.FIGURE_SERIES
                || type == QuestionType.CODE_OUTPUT
                || type == QuestionType.CODE_DEBUG
                || type == QuestionType.CODE_FILL
                || type == QuestionType.SQL_OUTPUT
                || type == QuestionType.FLOWCHART_MCQ
                || type == QuestionType.READING_COMPREHENSION
                || type == QuestionType.CLOZE_TEST
                || type == QuestionType.TABLE_DI
                || type == QuestionType.BAR_CHART_DI
                || type == QuestionType.PIE_CHART_DI
                || type == QuestionType.LINE_GRAPH_DI
                || type == QuestionType.CASELET_DI
                || type == QuestionType.LISTENING_COMP;
    }

    // Convert optionOrder int to letter label
    private String optionLabel(int order) {
        return switch (order) {
            case 1 -> "A";
            case 2 -> "B";
            case 3 -> "C";
            case 4 -> "D";
            case 5 -> "E";
            default -> String.valueOf(order);
        };
    }

    // Get explanation text from QuestionExplanation if exists
    private String getQuestionExplanation(Question question) {
        QuestionExplanation exp = question.getQuestionExplanation();
        if (exp == null) return null;

        // Combine all explanation layers into one string
        StringBuilder sb = new StringBuilder();
        if (exp.getWhatIsAsked() != null) sb.append(exp.getWhatIsAsked()).append("\n\n");
        if (exp.getWhatIsGiven() != null) sb.append(exp.getWhatIsGiven()).append("\n\n");
        if (exp.getWhatApproach() != null) sb.append(exp.getWhatApproach()).append("\n\n");
        if (exp.getHowToSolve() != null) sb.append(exp.getHowToSolve());
        return sb.toString().trim();
    }
}