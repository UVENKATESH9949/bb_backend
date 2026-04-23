package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.request.*;
import com.BrainBlitz.dto.response.QuestionResponse;
import com.BrainBlitz.dto.response.QuestionSummaryResponse;
import com.BrainBlitz.entity.*;
import com.BrainBlitz.enums.*;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.*;
import com.BrainBlitz.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final McqOptionRepository mcqOptionRepository;
    private final FillBlankAnswerRepository fillBlankAnswerRepository;
    private final ArrangementQuestionRepository arrangementQuestionRepository;
    private final ImageQuestionRepository imageQuestionRepository;
    private final QuestionGroupRepository questionGroupRepository;
    private final CodingQuestionRepository codingQuestionRepository;
    private final CodeSnippetQuestionRepository codeSnippetQuestionRepository;
    private final WritingQuestionRepository writingQuestionRepository;
    private final QuestionExplanationRepository questionExplanationRepository;
    private final ObjectMapper objectMapper;

    public QuestionServiceImpl(
            QuestionRepository questionRepository,
            McqOptionRepository mcqOptionRepository,
            FillBlankAnswerRepository fillBlankAnswerRepository,
            ArrangementQuestionRepository arrangementQuestionRepository,
            ImageQuestionRepository imageQuestionRepository,
            QuestionGroupRepository questionGroupRepository,
            CodingQuestionRepository codingQuestionRepository,
            CodeSnippetQuestionRepository codeSnippetQuestionRepository,
            WritingQuestionRepository writingQuestionRepository,
            QuestionExplanationRepository questionExplanationRepository,
            ObjectMapper objectMapper) {
        this.questionRepository = questionRepository;
        this.mcqOptionRepository = mcqOptionRepository;
        this.fillBlankAnswerRepository = fillBlankAnswerRepository;
        this.arrangementQuestionRepository = arrangementQuestionRepository;
        this.imageQuestionRepository = imageQuestionRepository;
        this.questionGroupRepository = questionGroupRepository;
        this.codingQuestionRepository = codingQuestionRepository;
        this.codeSnippetQuestionRepository = codeSnippetQuestionRepository;
        this.writingQuestionRepository = writingQuestionRepository;
        this.questionExplanationRepository = questionExplanationRepository;
        this.objectMapper = objectMapper;
    }


    // ═════════════════════════════════════════════════════════════════
    // CREATE METHODS
    // ═════════════════════════════════════════════════════════════════

    @Override
    public QuestionResponse createMcqQuestion(McqQuestionRequest request) {
        validateCorrectAnswers(request.getOptions());

        // 1. Build and save base question WITHOUT explanation
        Question question = buildBaseQuestion(
            request.getQuestionText(), request.getQuestionType(),
            request.getExamCategory(), request.getExamType(),
            request.getSubject(), request.getTopic(),
            request.getDifficultyLevel(), request.getLanguage(),
            request.getMarks(), request.getNegativeMarks(),
            request.getHint(), null,  // ← pass null here
            request.getIsAiGenerated(), request.getGroupId()
        );
        question = questionRepository.save(question); // question now has a valid ID

        // 2. Now wire and save the explanation
        QuestionExplanation qe = request.getQuestionExplanation();
        if (qe != null) {
            qe.setQuestion(question);
            
            // Clear the back-reference on each method BEFORE saving
            if (qe.getMethods() != null) {
                qe.getMethods().forEach(method -> method.setQuestionExplanation(qe));
            }
            
            questionExplanationRepository.save(qe); // now cascades methods with valid FK
        }

        // 3. Save options
        final Question savedQuestion = question;
        List<McqOption> options = request.getOptions().stream()
            .map(opt -> {
                McqOption o = new McqOption();
                o.setQuestion(savedQuestion);
                o.setOptionText(opt.getOptionText());
                o.setOptionImageUrl(opt.getOptionImageUrl());
                o.setCorrect(Boolean.TRUE.equals(opt.getIsCorrect()));
                o.setOptionOrder(opt.getOptionOrder());
                return o;
            })
            .collect(Collectors.toList());

        mcqOptionRepository.saveAll(options);
        return getQuestionById(question.getId());
    }

    @Override
    public QuestionResponse createFillBlank(FillBlankRequest request) {
        Question question = buildBaseQuestion(
            request.getQuestionText(), request.getQuestionType(),
            request.getExamCategory(), request.getExamType(),
            request.getSubject(), request.getTopic(),
            request.getDifficultyLevel(), request.getLanguage(),
            request.getMarks(), request.getNegativeMarks(),
            request.getHint(), null, request.getIsAiGenerated(), null
        );
        question = questionRepository.save(question);

        final Question savedQuestion = question;
        List<FillBlankAnswer> blanks = request.getBlanks().stream()
            .map(b -> {
                FillBlankAnswer fba = new FillBlankAnswer();
                fba.setQuestion(savedQuestion);
                fba.setBlankPosition(b.getBlankPosition());
                fba.setCorrectAnswer(b.getCorrectAnswer());
                // field is: String alternateAnswers (not alternateAnswersJson)
                fba.setAlternateAnswers(toJson(b.getAlternateAnswers()));
                fba.setCaseSensitive(
                    b.getCaseSensitive() != null ? b.getCaseSensitive() : false);
                return fba;
            })
            .collect(Collectors.toList());

        fillBlankAnswerRepository.saveAll(blanks);
        return getQuestionById(question.getId());
    }

    @Override
    public QuestionResponse createArrangement(ArrangementRequest request) {
        QuestionType qType = request.getArrangementType() == ArrangementType.SENTENCE
            ? QuestionType.SENTENCE_ARRANGEMENT
            : QuestionType.PARA_JUMBLE;

        Question question = buildBaseQuestion(
            request.getQuestionText(), qType,
            request.getExamCategory(), request.getExamType(),
            request.getSubject(), request.getTopic(),
            request.getDifficultyLevel(), request.getLanguage(),
            request.getMarks(), request.getNegativeMarks(),
            request.getHint(), null,
             request.getIsAiGenerated(), null
        );
        question = questionRepository.save(question);

        ArrangementQuestion arrangement = new ArrangementQuestion();
        arrangement.setQuestion(question);
        arrangement.setArrangementType(request.getArrangementType());
        arrangement.setSegmentsJson(toJson(request.getSegments()));
        arrangement.setCorrectOrder(request.getCorrectOrder());
        arrangementQuestionRepository.save(arrangement);

        return getQuestionById(question.getId());
    }

    @Override
    public QuestionResponse createImageQuestion(ImageQuestionRequest request) {
        QuestionType qType = mapImageTypeToQuestionType(
            request.getImageQuestionType());

        Question question = buildBaseQuestion(
            request.getQuestionText(), qType,
            request.getExamCategory(), request.getExamType(),
            request.getSubject(), request.getTopic(),
            request.getDifficultyLevel(), request.getLanguage(),
            request.getMarks(), request.getNegativeMarks(),
            request.getHint(), null,
            request.getIsAiGenerated(), null
        );
        question = questionRepository.save(question);

        ImageQuestion imageQuestion = new ImageQuestion();
        imageQuestion.setQuestion(question);
        imageQuestion.setImageQuestionType(request.getImageQuestionType());
        imageQuestion.setMainImageUrl(request.getMainImageUrl());
        // field is: supportingImagesJson (not supportingImageUrlsJson)
        imageQuestion.setSupportingImagesJson(
            toJson(request.getSupportingImageUrls()));
        imageQuestionRepository.save(imageQuestion);

        if (request.getOptions() != null) {
            final Question savedQuestion = question;
            List<McqOption> options = request.getOptions().stream()
                .map(opt -> {
                    McqOption o = new McqOption();
                    o.setQuestion(savedQuestion);
                    o.setOptionText(opt.getOptionText());
                    o.setOptionImageUrl(opt.getOptionImageUrl());
                    o.setCorrect(Boolean.TRUE.equals(opt.getIsCorrect()));
                    o.setOptionOrder(opt.getOptionOrder());
                    return o;
                })
                .collect(Collectors.toList());
            mcqOptionRepository.saveAll(options);
        }
        return getQuestionById(question.getId());
    }

    @Override
    public QuestionResponse createQuestionGroup(QuestionGroupRequest request) {
        QuestionGroup group = new QuestionGroup();
        group.setGroupType(request.getGroupType());
        // field is: String examType (not ExamType enum)
        group.setExamType(request.getExamType() != null
            ? request.getExamType().name() : null);
        group.setTitle(request.getTitle());
        group.setPassageText(request.getPassageText());
        // fields are: tableDataJson and chartDataJson (not diDataJson)
        group.setTableDataJson(request.getDiDataJson());
        // field is: chartImageUrl (not diImageUrl)
        group.setChartImageUrl(request.getDiImageUrl());
//        group.setExamType(request.getExamCategory());
        questionGroupRepository.save(group);

        QuestionResponse response = new QuestionResponse();
        QuestionResponse.QuestionGroupResponse gr =
            new QuestionResponse.QuestionGroupResponse();
        gr.setId(group.getId());
        gr.setGroupType(group.getGroupType());
        gr.setTitle(group.getTitle());
        gr.setPassageText(group.getPassageText());
        gr.setDiDataJson(group.getTableDataJson());
        gr.setDiImageUrl(group.getChartImageUrl());
        response.setGroup(gr);
        return response;
    }

    @Override
    public QuestionResponse addQuestionsToGroup(Long groupId,
                                                 McqQuestionRequest request) {
        questionGroupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "QuestionGroup", groupId));
        request.setGroupId(groupId);
        return createMcqQuestion(request);
    }

    @Override
    public QuestionResponse createCodingQuestion(CodingQuestionRequest request) {
        Question question = buildBaseQuestion(
            request.getProblemStatement(), request.getQuestionType(),
            request.getExamCategory(), request.getExamType(),
            request.getSubject(), request.getTopic(),
            request.getDifficultyLevel(), Language.ENGLISH,
            request.getMarks(), request.getNegativeMarks(),
            request.getHint(),null,
            request.getIsAiGenerated(), null
        );
        question = questionRepository.save(question);

        CodingQuestion codingQuestion = new CodingQuestion();
        codingQuestion.setQuestion(question);
        codingQuestion.setProblemStatement(request.getProblemStatement());
        codingQuestion.setInputFormat(request.getInputFormat());
        codingQuestion.setOutputFormat(request.getOutputFormat());
        codingQuestion.setConstraints(request.getConstraints());
        // field is: starterCodeJson (not codeSnippet)
        codingQuestion.setStarterCodeJson(request.getCodeSnippet());
        // field is: solutionCodeJson (not solutionCode)
        codingQuestion.setSolutionCodeJson(request.getSolutionCode());
        codingQuestion.setSupportedLanguagesJson(
            toJson(request.getSupportedLanguages()));
        // field is: expectedTimeComplexity (not timeComplexity)
        codingQuestion.setExpectedTimeComplexity(request.getTimeComplexity());
        // field is: expectedSpaceComplexity (not spaceComplexity)
        codingQuestion.setExpectedSpaceComplexity(request.getSpaceComplexity());
        codingQuestionRepository.save(codingQuestion);

        return getQuestionById(question.getId());
    }

    @Override
    public QuestionResponse createCodeSnippet(CodeSnippetRequest request) {
        Question question = buildBaseQuestion(
            request.getQuestionText(), request.getQuestionType(),
            request.getExamCategory(), request.getExamType(),
            request.getSubject(), request.getTopic(),
            request.getDifficultyLevel(), Language.ENGLISH,
            request.getMarks(), request.getNegativeMarks(),
            request.getHint(), null,
            request.getIsAiGenerated(), null
        );
        question = questionRepository.save(question);

        CodeSnippetQuestion snippet = new CodeSnippetQuestion();
        snippet.setQuestion(question);
        snippet.setCodeSnippet(request.getCodeSnippet());
        snippet.setProgrammingLanguage(request.getProgrammingLanguage());
        snippet.setSqlTableDataJson(request.getSqlTableDataJson());
        snippet.setSqlQuery(request.getSqlQuery());
        snippet.setBlankLineNumber(request.getBlankLineNumber());
        snippet.setAcceptedAnswersJson(toJson(request.getAcceptedAnswers()));
        snippet.setFlowchartImageUrl(request.getFlowchartImageUrl());
        codeSnippetQuestionRepository.save(snippet);

        if (request.getOptions() != null) {
            final Question savedQuestion = question;
            List<McqOption> options = request.getOptions().stream()
                .map(opt -> {
                    McqOption o = new McqOption();
                    o.setQuestion(savedQuestion);
                    o.setOptionText(opt.getOptionText());
                    o.setOptionImageUrl(opt.getOptionImageUrl());
                    o.setCorrect(Boolean.TRUE.equals(opt.getIsCorrect()));
                    o.setOptionOrder(opt.getOptionOrder());
                    return o;
                })
                .collect(Collectors.toList());
            mcqOptionRepository.saveAll(options);
        }
        return getQuestionById(question.getId());
    }

    @Override
    public QuestionResponse createWritingQuestion(WritingQuestionRequest request) {
        QuestionType qType = mapWritingTypeToQuestionType(request.getWritingType());

        Question question = buildBaseQuestion(
            request.getPrompt(), qType,
            request.getExamCategory(), request.getExamType(),
            request.getSubject(), request.getTopic(),
            request.getDifficultyLevel(), Language.ENGLISH,
            request.getMarks(), request.getNegativeMarks(),
            request.getHint(), null,
            request.getIsAiGenerated(), null
        );
        question = questionRepository.save(question);

        WritingQuestion writingQuestion = new WritingQuestion();
        writingQuestion.setQuestion(question);
        writingQuestion.setWritingType(request.getWritingType());
        writingQuestion.setPrompt(request.getPrompt());
        writingQuestion.setMinWords(request.getMinWords());
        writingQuestion.setMaxWords(request.getMaxWords());
        writingQuestion.setEvaluationCriteriaJson(
            request.getEvaluationCriteriaJson());
        writingQuestion.setSampleAnswer(request.getSampleAnswer());
        writingQuestion.setAudioUrl(request.getAudioUrl());
        writingQuestion.setSpeechDurationSeconds(
            request.getSpeechDurationSeconds());
        writingQuestion.setGradingStatus("PENDING");
        writingQuestionRepository.save(writingQuestion);

        return getQuestionById(question.getId());
    }


    // ═════════════════════════════════════════════════════════════════
    // READ METHODS
    // ═════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));
        return mapToFullResponse(question);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionSummaryResponse> getAllQuestions(
            QuestionType questionType, ExamCategory examCategory,
            ExamType examType, String subject, String topic,
            DifficultyLevel difficultyLevel, Language language,
            Boolean isActive, Boolean isAiGenerated, Pageable pageable) {

        return questionRepository.findAllWithFilters(
            questionType, examCategory, examType, subject, topic,
            difficultyLevel, language, isActive, isAiGenerated, pageable
        ).map(this::mapToSummaryResponse);
    }


    // ═════════════════════════════════════════════════════════════════
    // UPDATE METHODS
    // ═════════════════════════════════════════════════════════════════

    @Override
    public QuestionResponse updateBaseQuestion(Long id,
                                                McqQuestionRequest request) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        if (request.getQuestionText() != null)
            question.setQuestionText(request.getQuestionText());
        if (request.getSubject() != null)
            question.setSubject(request.getSubject());
        if (request.getTopic() != null)
            question.setTopic(request.getTopic());
        if (request.getDifficultyLevel() != null)
            question.setDifficultyLevel(request.getDifficultyLevel());
        if (request.getMarks() != null)
            question.setMarks(request.getMarks());
        if (request.getNegativeMarks() != null)
            question.setNegativeMarks(request.getNegativeMarks());
        if (request.getHint() != null)
            question.setHint(request.getHint());
        if (request.getQuestionExplanation() != null)
            question.setQuestionExplanation(request.getQuestionExplanation());

        questionRepository.save(question);
        return mapToFullResponse(question);
    }

    @Override
    public void updateStatus(Long id, Boolean isActive) {
        Question question = questionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));
        // field: boolean isActive → setter: setActive()
        question.setActive(isActive);
        questionRepository.save(question);
    }

    @Override
    public void bulkUpdateStatus(List<Long> ids, Boolean isActive) {
        List<Question> questions = questionRepository.findAllById(ids);
        questions.forEach(q -> q.setActive(isActive));
        questionRepository.saveAll(questions);
    }


    // ═════════════════════════════════════════════════════════════════
    // DELETE METHODS
    // ═════════════════════════════════════════════════════════════════

    @Override
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id))
            throw new ResourceNotFoundException("Question", id);
        questionRepository.deleteById(id);
    }

    @Override
    public void bulkDeleteQuestions(List<Long> ids) {
        questionRepository.deleteAllById(ids);
    }


    // ═════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═════════════════════════════════════════════════════════════════

    private Question buildBaseQuestion(
            String questionText, QuestionType questionType,
            ExamCategory examCategory, ExamType examType,
            String subject, String topic,
            DifficultyLevel difficultyLevel, Language language,
            Double marks, Double negativeMarks,
            String hint,
            QuestionExplanation approachExplanation, Boolean isAiGenerated,
            Long groupId) {

        Question q = new Question();
        q.setQuestionText(questionText);
        q.setQuestionType(questionType);
        q.setExamCategory(examCategory);
        q.setExamType(examType);
        q.setSubject(subject);
        q.setTopic(topic);
        q.setDifficultyLevel(difficultyLevel);
        q.setLanguage(language != null ? language : Language.ENGLISH);
        q.setMarks(marks != null ? marks : 1.0);
        q.setNegativeMarks(negativeMarks != null ? negativeMarks : 0.0);
        q.setHint(hint);
        q.setQuestionExplanation(approachExplanation);
        // field: boolean isActive → setter: setActive()
        q.setActive(true);
        // field: boolean isAiGenerated → setter: setAiGenerated()
        q.setAiGenerated(isAiGenerated != null ? isAiGenerated : false);
        q.setGroupId(groupId);
        return q;
    }

    private void validateCorrectAnswers(
            List<McqQuestionRequest.McqOptionRequest> options) {
        boolean hasCorrect = options.stream()
            .anyMatch(o -> Boolean.TRUE.equals(o.getIsCorrect()));
        if (!hasCorrect)
            throw new IllegalArgumentException(
                "At least one option must be marked as correct");
    }

    private QuestionSummaryResponse mapToSummaryResponse(Question q) {
        QuestionSummaryResponse r = new QuestionSummaryResponse();
        r.setId(q.getId());
        r.setQuestionText(q.getQuestionText());
        r.setQuestionType(q.getQuestionType());
        r.setExamCategory(q.getExamCategory());
        r.setExamType(q.getExamType());
        r.setSubject(q.getSubject());
        r.setTopic(q.getTopic());
        r.setDifficultyLevel(q.getDifficultyLevel());
        r.setLanguage(q.getLanguage());
        r.setMarks(q.getMarks());
        r.setNegativeMarks(q.getNegativeMarks());
        // field: boolean isActive → getter: isActive()
        r.setIsActive(q.isActive());
        // field: boolean isAiGenerated → getter: isAiGenerated()
        r.setIsAiGenerated(q.isAiGenerated());
        r.setGroupId(q.getGroupId());
        r.setCreatedAt(q.getCreatedAt());
        r.setUpdatedAt(q.getUpdatedAt());
        return r;
    }

    private QuestionResponse mapToFullResponse(Question q) {
        QuestionResponse r = new QuestionResponse();
        r.setId(q.getId());
        r.setQuestionText(q.getQuestionText());
        r.setQuestionType(q.getQuestionType());
        r.setExamCategory(q.getExamCategory());
        r.setExamType(q.getExamType());
        r.setSubject(q.getSubject());
        r.setTopic(q.getTopic());
        r.setDifficultyLevel(q.getDifficultyLevel());
        r.setLanguage(q.getLanguage());
        r.setMarks(q.getMarks());
        r.setNegativeMarks(q.getNegativeMarks());
        r.setHint(q.getHint());
        
        // primitive boolean → isActive() / isAiGenerated()
        r.setIsActive(q.isActive());
        r.setIsAiGenerated(q.isAiGenerated());
        r.setGroupId(q.getGroupId());
        r.setCreatedAt(q.getCreatedAt());
        r.setUpdatedAt(q.getUpdatedAt());

        // MCQ options
        List<McqOption> options =
            mcqOptionRepository.findByQuestionIdOrderByOptionOrder(q.getId());
        if (!options.isEmpty()) {
            r.setOptions(options.stream().map(o -> {
                QuestionResponse.McqOptionResponse opt =
                    new QuestionResponse.McqOptionResponse();
                opt.setId(o.getId());
                opt.setOptionText(o.getOptionText());
                opt.setOptionImageUrl(o.getOptionImageUrl());
                // field: boolean isCorrect → getter: isCorrect()
                opt.setIsCorrect(o.isCorrect());
                opt.setOptionOrder(o.getOptionOrder());
                return opt;
            }).collect(Collectors.toList()));
        }

        // Fill blank answers
        List<FillBlankAnswer> blanks =
            fillBlankAnswerRepository
                .findByQuestionIdOrderByBlankPosition(q.getId());
        if (!blanks.isEmpty()) {
            r.setBlanks(blanks.stream().map(b -> {
                QuestionResponse.FillBlankAnswerResponse fbr =
                    new QuestionResponse.FillBlankAnswerResponse();
                fbr.setId(b.getId());
                fbr.setBlankPosition(b.getBlankPosition());
                fbr.setCorrectAnswer(b.getCorrectAnswer());
                // field: String alternateAnswers (not alternateAnswersJson)
                fbr.setAlternateAnswers(fromJson(b.getAlternateAnswers()));
                // field: boolean caseSensitive → getter: isCaseSensitive()
                fbr.setCaseSensitive(b.isCaseSensitive());
                return fbr;
            }).collect(Collectors.toList()));
        }

        // Arrangement
        arrangementQuestionRepository.findByQuestionId(q.getId())
            .ifPresent(a -> {
                QuestionResponse.ArrangementResponse ar =
                    new QuestionResponse.ArrangementResponse();
                ar.setId(a.getId());
                ar.setArrangementType(a.getArrangementType());
                ar.setSegments(fromJson(a.getSegmentsJson()));
                ar.setCorrectOrder(a.getCorrectOrder());
                r.setArrangement(ar);
            });

        // Image question
        imageQuestionRepository.findByQuestionId(q.getId())
            .ifPresent(img -> {
                QuestionResponse.ImageQuestionResponse ir =
                    new QuestionResponse.ImageQuestionResponse();
                ir.setId(img.getId());
                ir.setImageQuestionType(img.getImageQuestionType());
                ir.setMainImageUrl(img.getMainImageUrl());
                // field: supportingImagesJson
                ir.setSupportingImageUrls(
                    fromJson(img.getSupportingImagesJson()));
                r.setImageQuestion(ir);
            });

        // Code snippet
        codeSnippetQuestionRepository.findByQuestionId(q.getId())
            .ifPresent(cs -> {
                QuestionResponse.CodeSnippetResponse csr =
                    new QuestionResponse.CodeSnippetResponse();
                csr.setId(cs.getId());
                csr.setCodeSnippet(cs.getCodeSnippet());
                csr.setProgrammingLanguage(cs.getProgrammingLanguage());
                csr.setSqlTableDataJson(cs.getSqlTableDataJson());
                csr.setSqlQuery(cs.getSqlQuery());
                csr.setBlankLineNumber(cs.getBlankLineNumber());
                csr.setAcceptedAnswers(fromJson(cs.getAcceptedAnswersJson()));
                csr.setFlowchartImageUrl(cs.getFlowchartImageUrl());
                r.setCodeSnippet(csr);
            });

        // Coding question
        codingQuestionRepository.findByQuestionId(q.getId())
            .ifPresent(cq -> {
                QuestionResponse.CodingQuestionResponse cqr =
                    new QuestionResponse.CodingQuestionResponse();
                cqr.setId(cq.getId());
                cqr.setProblemStatement(cq.getProblemStatement());
                cqr.setInputFormat(cq.getInputFormat());
                cqr.setOutputFormat(cq.getOutputFormat());
                cqr.setConstraints(cq.getConstraints());
                // field: starterCodeJson
                cqr.setCodeSnippet(cq.getStarterCodeJson());
                // field: solutionCodeJson
                cqr.setSolutionCode(cq.getSolutionCodeJson());
                // field: expectedTimeComplexity
                cqr.setTimeComplexity(cq.getExpectedTimeComplexity());
                // field: expectedSpaceComplexity
                cqr.setSpaceComplexity(cq.getExpectedSpaceComplexity());
                cqr.setWhyThisDataStructure(cq.getWhyThisDataStructure());
                cqr.setWhyThisApproach(cq.getWhyThisApproach());
                r.setCodingQuestion(cqr);
            });

        // Writing question
        writingQuestionRepository.findByQuestionId(q.getId())
            .ifPresent(wq -> {
                QuestionResponse.WritingQuestionResponse wqr =
                    new QuestionResponse.WritingQuestionResponse();
                wqr.setId(wq.getId());
                wqr.setWritingType(wq.getWritingType());
                wqr.setPrompt(wq.getPrompt());
                wqr.setMinWords(wq.getMinWords());
                wqr.setMaxWords(wq.getMaxWords());
                wqr.setEvaluationCriteriaJson(wq.getEvaluationCriteriaJson());
                wqr.setSampleAnswer(wq.getSampleAnswer());
                wqr.setAudioUrl(wq.getAudioUrl());
                wqr.setSpeechDurationSeconds(wq.getSpeechDurationSeconds());
                wqr.setGradingStatus(wq.getGradingStatus());
                r.setWritingQuestion(wqr);
            });

        return r;
    }

    // ── JSON helpers ──────────────────────────────────────────────────

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(
                "JSON serialize failed: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> fromJson(String json) {
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            return null;
        }
    }

    // ── Enum mapping helpers ──────────────────────────────────────────

    private QuestionType mapImageTypeToQuestionType(ImageQuestionType type) {
        switch (type) {
            case MIRROR_IMAGE: return QuestionType.MIRROR_IMAGE;
            case WATER_IMAGE: return QuestionType.WATER_IMAGE;
            case PATTERN_MATRIX: return QuestionType.PATTERN_MATRIX;
            case ODD_ONE_OUT: return QuestionType.ODD_ONE_OUT;
            case FIGURE_SERIES: return QuestionType.FIGURE_SERIES;
            case IMAGE_MCQ: return QuestionType.IMAGE_MCQ;
            case IMAGE_OPTIONS: return QuestionType.IMAGE_OPTIONS;
            default: throw new IllegalArgumentException(
                "Unknown image type: " + type);
        }
    }

    private QuestionType mapWritingTypeToQuestionType(WritingType type) {
        switch (type) {
            case FORMAL_MAIL: return QuestionType.EMAIL_WRITE;
            case ESSAY: return QuestionType.ESSAY_WRITE;
            case SPEECH: return QuestionType.SPEECH_ROUND;
            case LISTENING: return QuestionType.LISTENING_COMP;
            default: throw new IllegalArgumentException(
                "Unknown writing type: " + type);
        }
    }
}