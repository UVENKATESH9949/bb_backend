package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.request.MockSessionRequest;
import com.BrainBlitz.dto.response.*;
import com.BrainBlitz.entity.*;
import com.BrainBlitz.enums.*;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.*;
import com.BrainBlitz.service.*;
import com.BrainBlitz.dto.response.MockResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

// ✅ REMOVED: javax.swing.text.html.Option — was causing wrong Option type

@Service
public class MockSessionServiceImpl implements MockSessionService {

    @Autowired
    private MockSessionRepository mockSessionRepository;

    @Autowired
    private MockAnswerRepository mockAnswerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamPatternRepository examPatternRepository;

    @Autowired
    private UserWeakAreaRepository userWeakAreaRepository;

    @Autowired
    private UserSeenQuestionRepository userSeenQuestionRepository;

    @Autowired
    private UserWeakAreaService userWeakAreaService;

    @Autowired
    private UserLevelService userLevelService;

    // ─────────────────────────────────────────────
    // START MOCK
    // ─────────────────────────────────────────────

    @Override
    public MockSessionResponse startMock(Long userId, MockSessionRequest request) {

        // ── DEMO MODE ─────────────────────────────────────────────
        // Fetches real questions from DB regardless of exam type.
        // Remove this block and uncomment REAL CODE below when ready.
        // ─────────────────────────────────────────────────────────

        User anyUser = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Fetch up to 5 real questions
        List<Question> demoQuestions = questionRepository
            .findAll(PageRequest.of(0, 5))
            .getContent();

        if (demoQuestions.isEmpty()) {
            throw new RuntimeException(
                "No questions in DB yet. Add some first.");
        }

        // ✅ Print options using McqOption — NOT javax.swing.text.html.Option
        for (Question q : demoQuestions) {
            if (q.getOptions() != null) {
                for (McqOption o : q.getOptions()) {
                    System.out.println(o.toString());
                }
            }
        }

        MockSession demoSession = new MockSession();
        demoSession.setUser(anyUser);
        demoSession.setExamType(request.getExamType());
        demoSession.setMockSource(request.getMockSource());
        demoSession.setStatus(MockStatus.IN_PROGRESS);
        demoSession.setStartTime(LocalDateTime.now());
        demoSession.setTotalQuestions(demoQuestions.size());   // ✅ matches actual list size
        demoSession.setMaxPossibleScore(demoQuestions.size() * 100.0);
        demoSession.setUserLevelAtTime(anyUser.getLevel());    // ✅ was missing — NOT NULL in DB

        mockSessionRepository.save(demoSession);
        return mapToSessionResponse(demoSession, demoQuestions);

        // ── DEMO MODE END ─────────────────────────────────────────


        // ── REAL CODE (uncomment when demo mode is removed) ───────
        /*
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + userId));

        Optional<MockSession> activeMock = mockSessionRepository
            .findByUserIdAndStatus(userId, MockStatus.IN_PROGRESS);

        if (activeMock.isPresent()) {
            throw new RuntimeException(
                "You already have an active mock in progress. " +
                "Please submit or abandon it first.");
        }

        List<Question> selectedQuestions = selectQuestions(
            userId, user.getLevel(), request.getExamType(),
            request.getMockSource(), request.getRoundName()
        );

        if (selectedQuestions.isEmpty()) {
            throw new RuntimeException(
                "Not enough questions available for this mock. " +
                "Please try again later.");
        }

        MockSession session = new MockSession();
        session.setUser(user);
        session.setExamType(request.getExamType());
        session.setMockSource(request.getMockSource());
        session.setUserLevelAtTime(user.getLevel());
        session.setStartTime(LocalDateTime.now());
        session.setTotalQuestions(selectedQuestions.size());
        session.setStatus(MockStatus.IN_PROGRESS);

        double maxScore = calculateMaxScore(
            request.getExamType(), selectedQuestions.size());
        session.setMaxPossibleScore(maxScore);

        mockSessionRepository.save(session);
        markQuestionsAsSeen(userId, session, selectedQuestions);
        userLevelService.updateStreak(userId);

        return mapToSessionResponse(session, selectedQuestions);
        */
    }

    // ─────────────────────────────────────────────
    // SUBMIT MOCK
    // ─────────────────────────────────────────────

    @Override
    public MockResultResponse submitMock(Long sessionId, Long userId) {

        MockSession session = validateSessionOwnership(sessionId, userId);

        if (session.getStatus() != MockStatus.IN_PROGRESS) {
            throw new RuntimeException(
                "This mock is already " + session.getStatus());
        }

        List<MockAnswer> answers = mockAnswerRepository
            .findByMockSessionId(sessionId);

        int correct = 0, wrong = 0, skipped = 0;
        double totalScore = 0;

        for (MockAnswer answer : answers) {
            switch (answer.getResult()) {
                case CORRECT:
                    correct++;
                    totalScore += answer.getMarksAwarded();
                    break;
                case WRONG:
                    wrong++;
                    totalScore += answer.getMarksAwarded(); // negative marks
                    break;
                case SKIPPED:
                case PENDING:
                    skipped++;
                    break;
                default:
                    break;
            }
        }

        int attempted = correct + wrong;
        double accuracy = attempted > 0
            ? (double) correct / attempted * 100
            : 0;

        session.setStatus(MockStatus.COMPLETED);
        session.setEndTime(LocalDateTime.now());
        session.setDurationSeconds((int) ChronoUnit.SECONDS.between(
            session.getStartTime(), session.getEndTime()));
        session.setCorrect(correct);
        session.setWrong(wrong);
        session.setSkipped(skipped);
        session.setAttempted(attempted);
        session.setTotalScore(Math.max(totalScore, 0));
        session.setAccuracyPercentage(accuracy);
        mockSessionRepository.save(session);

        User user = session.getUser();
        user.setTotalMocksAttempted(user.getTotalMocksAttempted() + 1);
        userRepository.save(user);

        if (!session.isPerformanceProcessed()) {
            userWeakAreaService.updateWeakAreas(userId, sessionId);
            userLevelService.updateLevelAfterMock(userId, sessionId, accuracy);
            session.setPerformanceProcessed(true);
            mockSessionRepository.save(session);
        }

        return buildMockResult(session, answers);
    }

    // ─────────────────────────────────────────────
    // ABANDON MOCK
    // ─────────────────────────────────────────────

    @Override
    public void abandonMock(Long sessionId, Long userId) {
        MockSession session = validateSessionOwnership(sessionId, userId);
        if (session.getStatus() != MockStatus.IN_PROGRESS) return;
        session.setStatus(MockStatus.ABANDONED);
        session.setEndTime(LocalDateTime.now());
        mockSessionRepository.save(session);
    }

    // ─────────────────────────────────────────────
    // TIMEOUT MOCK
    // ─────────────────────────────────────────────

    @Override
    public void timeoutMock(Long sessionId, Long userId) {
        MockSession session = validateSessionOwnership(sessionId, userId);
        if (session.getStatus() != MockStatus.IN_PROGRESS) return;

        session.setStatus(MockStatus.TIMED_OUT);
        session.setEndTime(LocalDateTime.now());
        mockSessionRepository.save(session);

        List<MockAnswer> answers = mockAnswerRepository
            .findByMockSessionId(sessionId);

        if (!answers.isEmpty() && !session.isPerformanceProcessed()) {
            int correct = (int) answers.stream()
                .filter(a -> a.getResult() == AnswerResult.CORRECT)
                .count();
            int attempted = (int) answers.stream()
                .filter(a -> a.getResult() != AnswerResult.SKIPPED)
                .count();
            double accuracy = attempted > 0
                ? (double) correct / attempted * 100 : 0;

            userWeakAreaService.updateWeakAreas(userId, sessionId);
            userLevelService.updateLevelAfterMock(userId, sessionId, accuracy);
            session.setPerformanceProcessed(true);
            mockSessionRepository.save(session);
        }
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @Override
    public MockSessionResponse getActiveMock(Long userId) {
        MockSession session = mockSessionRepository
            .findByUserIdAndStatus(userId, MockStatus.IN_PROGRESS)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No active mock found for user: " + userId));

        List<Question> questions = mockAnswerRepository
            .findByMockSessionId(session.getId())
            .stream()
            .map(MockAnswer::getQuestion)
            .collect(Collectors.toList());

        return mapToSessionResponse(session, questions);
    }

    @Override
    public List<MockSessionResponse> getMockHistory(Long userId) {
        return mockSessionRepository
            .findByUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(s -> mapToSessionResponse(s, Collections.emptyList()))
            .collect(Collectors.toList());
    }

    @Override
    public MockResultResponse getMockResult(Long sessionId, Long userId) {
        MockSession session = validateSessionOwnership(sessionId, userId);
        List<MockAnswer> answers = mockAnswerRepository
            .findByMockSessionId(sessionId);
        return buildMockResult(session, answers);
    }

    // ─────────────────────────────────────────────
    // CORE ENGINE — Question Selection
    // ─────────────────────────────────────────────

    private List<Question> selectQuestions(
            Long userId, int userLevel, ExamType examType,
            MockSource mockSource, String roundName) {

        List<ExamPattern> patterns = roundName != null
            ? examPatternRepository
                .findByExamTypeAndRoundNameAndIsActiveTrue(examType, roundName)
            : examPatternRepository
                .findByExamTypeAndIsActiveTrue(examType);

        if (patterns.isEmpty()) {
            throw new RuntimeException(
                "No exam pattern found for: " + examType);
        }

        List<String> weakTopics = userWeakAreaRepository
            .findByUserIdAndIsWeakTrue(userId)
            .stream()
            .map(UserWeakArea::getTopic)
            .collect(Collectors.toList());

        Set<Long> seenCorrect = userSeenQuestionRepository
            .findByUserIdAndResultAndSeenAtAfter(
                userId, AnswerResult.CORRECT,
                LocalDateTime.now().minusDays(30))
            .stream()
            .map(s -> s.getQuestion().getId())
            .collect(Collectors.toSet());

        Set<Long> seenWrong = userSeenQuestionRepository
            .findByUserIdAndResultAndSeenAtAfter(
                userId, AnswerResult.WRONG,
                LocalDateTime.now().minusDays(7))
            .stream()
            .map(s -> s.getQuestion().getId())
            .collect(Collectors.toSet());

        Set<Long> seenSkipped = userSeenQuestionRepository
            .findByUserIdAndResultAndSeenAtAfter(
                userId, AnswerResult.SKIPPED,
                LocalDateTime.now().minusDays(3))
            .stream()
            .map(s -> s.getQuestion().getId())
            .collect(Collectors.toSet());

        Set<Long> excludeIds = new HashSet<>();
        excludeIds.addAll(seenCorrect);
        excludeIds.addAll(seenWrong);
        excludeIds.addAll(seenSkipped);

        List<Question> selectedQuestions = new ArrayList<>();

        for (ExamPattern pattern : patterns) {
            boolean isWeakTopic = weakTopics.contains(pattern.getTopic());
            int adjustedLevel = isWeakTopic
                ? Math.max(userLevel - 1, 1)
                : userLevel;

            List<DifficultyLevel> allowedLevels =
                getAllowedDifficultyLevels(adjustedLevel);

            List<Question> topicQuestions = questionRepository
                .findQuestionsForMock(
                    examType,
                    pattern.getSubject(),
                    pattern.getTopic(),
                    allowedLevels)
                .stream()
                .filter(q -> !excludeIds.contains(q.getId()))
                .limit(pattern.getQuestionsCount())
                .collect(Collectors.toList());

            selectedQuestions.addAll(topicQuestions);
        }

        Collections.shuffle(selectedQuestions);
        return selectedQuestions;
    }

    // ─────────────────────────────────────────────
    // HELPER — Match Difficulty To Level
    // ─────────────────────────────────────────────

    private List<DifficultyLevel> getAllowedDifficultyLevels(int userLevel) {
        if (userLevel <= 3) {
            return List.of(
                DifficultyLevel.LEVEL_1,
                DifficultyLevel.LEVEL_2,
                DifficultyLevel.LEVEL_3);
        }
        if (userLevel <= 6) {
            return List.of(
                DifficultyLevel.LEVEL_1, DifficultyLevel.LEVEL_2,
                DifficultyLevel.LEVEL_3, DifficultyLevel.LEVEL_4,
                DifficultyLevel.LEVEL_5, DifficultyLevel.LEVEL_6);
        }
        return List.of(
            DifficultyLevel.LEVEL_1,  DifficultyLevel.LEVEL_2,
            DifficultyLevel.LEVEL_3,  DifficultyLevel.LEVEL_4,
            DifficultyLevel.LEVEL_5,  DifficultyLevel.LEVEL_6,
            DifficultyLevel.LEVEL_7,  DifficultyLevel.LEVEL_8,
            DifficultyLevel.LEVEL_9,  DifficultyLevel.LEVEL_10);
    }

    // ─────────────────────────────────────────────
    // HELPER — Mark Questions As Seen
    // ─────────────────────────────────────────────

    private void markQuestionsAsSeen(
            Long userId, MockSession session,
            List<Question> questions) {

        User user = session.getUser();

        for (Question question : questions) {
            Optional<UserSeenQuestion> existing =
                userSeenQuestionRepository
                    .findByUserIdAndQuestionId(userId, question.getId());

            if (existing.isPresent()) {
                UserSeenQuestion seen = existing.get();
                seen.setSeenAt(LocalDateTime.now());
                seen.setMockSession(session);
                userSeenQuestionRepository.save(seen);
            } else {
                UserSeenQuestion seen = new UserSeenQuestion();
                seen.setUser(user);
                seen.setQuestion(question);
                seen.setMockSession(session);
                seen.setSeenAt(LocalDateTime.now());
                seen.setResult(AnswerResult.SKIPPED);
                userSeenQuestionRepository.save(seen);
            }
        }
    }

    // ─────────────────────────────────────────────
    // HELPER — Calculate Max Possible Score
    // ─────────────────────────────────────────────

    private double calculateMaxScore(ExamType examType, int totalQuestions) {
        List<ExamPattern> patterns = examPatternRepository
            .findByExamTypeAndIsActiveTrue(examType);
        if (patterns.isEmpty()) return totalQuestions;
        return patterns.stream()
            .mapToDouble(p -> p.getQuestionsCount() * p.getMarksPerQuestion())
            .sum();
    }

    // ─────────────────────────────────────────────
    // HELPER — Validate Session Ownership
    // ─────────────────────────────────────────────

    private MockSession validateSessionOwnership(Long sessionId, Long userId) {
        MockSession session = mockSessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mock session not found with id: " + sessionId));

        if (!session.getUser().getId().equals(userId)) {
            throw new RuntimeException(
                "Access denied: This mock does not belong to you");
        }
        return session;
    }

    // ─────────────────────────────────────────────
    // HELPER — Build Mock Result Response
    // ─────────────────────────────────────────────

    private MockResultResponse buildMockResult(
            MockSession session, List<MockAnswer> answers) {

        MockResultResponse result = new MockResultResponse();
        result.setSessionId(session.getId());
        result.setExamType(session.getExamType());
        result.setTotalQuestions(session.getTotalQuestions());
        result.setAttempted(session.getAttempted());
        result.setCorrect(session.getCorrect());
        result.setWrong(session.getWrong());
        result.setSkipped(session.getSkipped());
        result.setTotalScore(session.getTotalScore());
        result.setMaxPossibleScore(session.getMaxPossibleScore());
        result.setAccuracyPercentage(session.getAccuracyPercentage());
        result.setDurationSeconds(session.getDurationSeconds());
        result.setStatus(session.getStatus());
        result.setAiStudyPlan(session.getAiStudyPlan());
        result.setLevelBefore(session.getUserLevelAtTime());
        result.setLevelAfter(session.getUser().getLevel());
        result.setLevelChanged(
            session.getUserLevelAtTime() != session.getUser().getLevel());

        result.setAnswers(answers.stream()
            .map(this::mapAnswerToResponse)
            .collect(Collectors.toList()));

        Map<String, TopicResultResponse> topicBreakdown = new LinkedHashMap<>();
        for (MockAnswer answer : answers) {
            String key = answer.getSubject() + " → " + answer.getTopic();
            topicBreakdown.computeIfAbsent(key, k -> {
                TopicResultResponse t = new TopicResultResponse();
                t.setSubject(answer.getSubject());
                t.setTopic(answer.getTopic());
                return t;
            });

            TopicResultResponse topicResult = topicBreakdown.get(key);
            topicResult.setTotalQuestions(topicResult.getTotalQuestions() + 1);

            if (answer.getResult() == AnswerResult.CORRECT) {
                topicResult.setCorrect(topicResult.getCorrect() + 1);
            } else if (answer.getResult() == AnswerResult.WRONG) {
                topicResult.setWrong(topicResult.getWrong() + 1);
            } else {
                topicResult.setSkipped(topicResult.getSkipped() + 1);
            }
        }

        result.setTopicBreakdown(new ArrayList<>(topicBreakdown.values()));
        return result;
    }

    // ─────────────────────────────────────────────
    // HELPER — Map Session To Response
    // ─────────────────────────────────────────────

    private MockSessionResponse mapToSessionResponse(
            MockSession session, List<Question> questions) {

        MockSessionResponse response = new MockSessionResponse();
        response.setId(session.getId());
        response.setUserId(session.getUser().getId());
        response.setExamType(session.getExamType());
        response.setMockSource(session.getMockSource());
        response.setUserLevelAtTime(session.getUserLevelAtTime());
        response.setTotalQuestions(session.getTotalQuestions());
        response.setStatus(session.getStatus());
        response.setStartTime(session.getStartTime());
        response.setCreatedAt(session.getCreatedAt());
        response.setTitle(session.getExamType().name() + " Mock Test");
        response.setInstructions(
            "Answer all questions. Timer runs for the full test.");
        response.setTimeLimitMinutes(30);

        response.setQuestions(questions.stream()
            .map(this::mapToQuestionSummary)
            .collect(Collectors.toList()));

        return response;
    }

    // ─────────────────────────────────────────────────────────────────
    // Maps a Question entity → QuestionSummaryResponse
    // Branches on QuestionType to populate the correct payload
    // ─────────────────────────────────────────────────────────────────

    private QuestionSummaryResponse mapToQuestionSummary(Question q) {

        QuestionSummaryResponse summary = new QuestionSummaryResponse();

        // ── Common fields ──────────────────────────────────────
        summary.setId(q.getId());
        summary.setQuestionType(q.getQuestionType());
        summary.setQuestionText(q.getQuestionText());
        summary.setQuestionTextHindi(q.getQuestionTextHindi());
        summary.setQuestionImageUrl(q.getQuestionImageUrl());
        summary.setExamCategory(q.getExamCategory());
        summary.setExamType(q.getExamType());
        summary.setSubject(q.getSubject());
        summary.setTopic(q.getTopic());
        summary.setDifficultyLevel(q.getDifficultyLevel());
        summary.setLanguage(q.getLanguage());
        summary.setMarks(q.getMarks());
        summary.setNegativeMarks(q.getNegativeMarks());
        summary.setIsActive(q.isActive());
        summary.setIsAiGenerated(q.isAiGenerated());
        summary.setGroupId(q.getGroupId());
        summary.setQuestionOrderInGroup(q.getQuestionOrderInGroup());
        summary.setTimeLimitSeconds(q.getTimeLimitSeconds());
        summary.setHint(q.getHint());
        summary.setHintHindi(q.getHintHindi());
        summary.setExplanation(q.getQuestionExplanation());
        summary.setCreatedAt(q.getCreatedAt());
        summary.setUpdatedAt(q.getUpdatedAt());

        // ── Type-specific payload ──────────────────────────────
        switch (q.getQuestionType()) {

        case MCQ:
        case TRUE_FALSE:
        case SYNONYM_ANTONYM:
        case ERROR_SPOTTING:
                summary.setMcqPayload(buildMcqPayload(q));
                break;

            case MULTI_CORRECT:
                summary.setMultiCorrectPayload(buildMultiCorrectPayload(q));
                break;

            case FILL_BLANK:
            case TEXT_ANSWER:
                summary.setFillBlankPayload(buildFillBlankPayload(q));
                break;

            case SENTENCE_ARRANGEMENT:
            case PARA_JUMBLE:
                summary.setArrangementPayload(buildArrangementPayload(q));
                break;

            case MIRROR_IMAGE:
            case WATER_IMAGE:
            case PATTERN_MATRIX:
            case ODD_ONE_OUT:
            case FIGURE_SERIES:
                summary.setImagePayload(buildImagePayload(q));
                break;

            // IMAGE_MCQ / IMAGE_OPTIONS need both image data AND text options
            case IMAGE_MCQ:
            case IMAGE_OPTIONS:
                summary.setImagePayload(buildImagePayload(q));
                summary.setMcqPayload(buildMcqPayload(q));
                break;

            case CODE_WRITE:
            case CODE_DEBUG:
                summary.setCodingPayload(buildCodingPayload(q));
                break;

            // Code snippet types — also include MCQ options if present
            case CODE_OUTPUT:
            case CODE_FILL:
            case SQL_OUTPUT:
            case FLOWCHART_MCQ:
                summary.setCodeSnippetPayload(buildCodeSnippetPayload(q));
                if (q.getOptions() != null && !q.getOptions().isEmpty()) {
                    summary.setMcqPayload(buildMcqPayload(q));
                }
                break;

            case EMAIL_WRITE:
            case ESSAY_WRITE:
            case SPEECH_ROUND:
            case LISTENING_COMP:
                summary.setWritingPayload(buildWritingPayload(q));
                break;

            default:
                // Unknown type — common fields still returned, no payload
                break;
        }

        return summary;
    }

    // ─────────────────────────────────────────────────────────────────
    // PAYLOAD BUILDER HELPERS
    // ─────────────────────────────────────────────────────────────────

    /** Sorted by optionOrder so A/B/C/D are always in correct order */
    private QuestionSummaryResponse.McqPayload buildMcqPayload(Question q) {
        List<QuestionSummaryResponse.McqOptionDto> optionDtos =
            q.getOptions() == null ? Collections.emptyList() :
            q.getOptions().stream()
                .sorted(Comparator.comparingInt(McqOption::getOptionOrder))
                .map(opt -> new QuestionSummaryResponse.McqOptionDto(
                    opt.getId(),
                    opt.getOptionOrder(),
                    opt.getOptionText(),
                    opt.getOptionTextHindi(),
                    opt.getOptionImageUrl(),
                    opt.isCorrect(),
                    opt.getOptionExplanation()
                ))
                .collect(Collectors.toList());

        return new QuestionSummaryResponse.McqPayload(optionDtos);
    }

    /** Same data as MCQ — separate payload so frontend renders checkboxes */
    private QuestionSummaryResponse.MultiCorrectPayload buildMultiCorrectPayload(
            Question q) {
        List<QuestionSummaryResponse.McqOptionDto> optionDtos =
            q.getOptions() == null ? Collections.emptyList() :
            q.getOptions().stream()
                .sorted(Comparator.comparingInt(McqOption::getOptionOrder))
                .map(opt -> new QuestionSummaryResponse.McqOptionDto(
                    opt.getId(),
                    opt.getOptionOrder(),
                    opt.getOptionText(),
                    opt.getOptionTextHindi(),
                    opt.getOptionImageUrl(),
                    opt.isCorrect(),
                    opt.getOptionExplanation()
                ))
                .collect(Collectors.toList());

        return new QuestionSummaryResponse.MultiCorrectPayload(optionDtos);
    }

    /** Sorted by blankPosition — blank 1, blank 2, blank 3... */
    private QuestionSummaryResponse.FillBlankPayload buildFillBlankPayload(
            Question q) {
        List<QuestionSummaryResponse.BlankDto> blanks =
            q.getFillBlankAnswers() == null ? Collections.emptyList() :
            q.getFillBlankAnswers().stream()
                .sorted(Comparator.comparingInt(FillBlankAnswer::getBlankPosition))
                .map(fba -> new QuestionSummaryResponse.BlankDto(
                    fba.getBlankPosition(),
                    fba.getCorrectAnswer(),
                    fba.getAlternateAnswers(),
                    fba.isCaseSensitive(),
                    fba.isExactMatch(),
                    fba.getBlankHint(),
                    fba.getExpectedAnswerLength()
                ))
                .collect(Collectors.toList());

        return new QuestionSummaryResponse.FillBlankPayload(blanks);
    }

    private QuestionSummaryResponse.ArrangementPayload buildArrangementPayload(
            Question q) {
        ArrangementQuestion aq = q.getArrangementQuestion();
        if (aq == null) return null;

        return new QuestionSummaryResponse.ArrangementPayload(
            aq.getArrangementType(),
            aq.getSegmentsJson(),
            aq.getSegmentsJsonHindi(),
            aq.getCorrectOrder(),
            aq.getAlternateCorrectOrders(),
            aq.getFixedOpeningSentence(),
            aq.getFixedClosingSentence(),
            aq.getFixedOpeningSentenceHindi(),
            aq.getFixedClosingSentenceHindi(),
            aq.isDragDrop()
        );
    }

    private QuestionSummaryResponse.ImagePayload buildImagePayload(Question q) {
        ImageQuestion iq = q.getImageQuestion();
        if (iq == null) return null;

        return new QuestionSummaryResponse.ImagePayload(
            iq.getImageQuestionType(),
            iq.getMainImageUrl(),
            iq.getSupportingImagesJson(),
            iq.getMissingCellPosition(),
            iq.getImageDimensions(),
            iq.getMainImageAltText(),
            iq.getSupportingImagesAltTextJson(),
            iq.getCorrectOptionIndex(),
            iq.getOptionImagesJson(),
            iq.getOptionImagesAltTextJson()
        );
    }

    /**
     * Excludes testCases and solutionSteps —
     * those are fetched separately after submission, not needed mid-test
     */
    private QuestionSummaryResponse.CodingPayload buildCodingPayload(Question q) {
        CodingQuestion cq = q.getCodingQuestion();
        if (cq == null) return null;

        return new QuestionSummaryResponse.CodingPayload(
            cq.getProblemStatement(),
            cq.getProblemStatementHindi(),
            cq.getInputFormat(),
            cq.getOutputFormat(),
            cq.getConstraints(),
            cq.getStarterCodeJson(),
            cq.getSupportedLanguagesJson(),
            cq.getExpectedTimeComplexity(),
            cq.getExpectedSpaceComplexity(),
            cq.getWhyThisDataStructure(),
            cq.getWhyThisApproach(),
            cq.getAlternateApproachesJson(),
            cq.getBuggyCodeJson(),
            cq.getBugDescription()
        );
    }

    private QuestionSummaryResponse.CodeSnippetPayload buildCodeSnippetPayload(
            Question q) {
        CodeSnippetQuestion csq = q.getCodeSnippetQuestion();
        if (csq == null) return null;

        return new QuestionSummaryResponse.CodeSnippetPayload(
            csq.getCodeSnippet(),
            csq.getProgrammingLanguage(),
            csq.getSqlTableDataJson(),
            csq.getSqlQuery(),
            csq.getBlankLineNumber(),
            csq.getAcceptedAnswersJson(),
            csq.getFlowchartImageUrl()
        );
    }

    private QuestionSummaryResponse.WritingPayload buildWritingPayload(Question q) {
        WritingQuestion wq = q.getWritingQuestion();
        if (wq == null) return null;

        return new QuestionSummaryResponse.WritingPayload(
            wq.getWritingType(),
            wq.getPrompt(),
            wq.getMinWords(),
            wq.getMaxWords(),
            wq.getEvaluationCriteriaJson(),
            wq.getSampleAnswer(),
            wq.getAudioUrl(),
            wq.getSpeechDurationSeconds()
        );
    }

    // ─────────────────────────────────────────────
    // HELPER — Map Answer To Response
    // ─────────────────────────────────────────────

    private MockAnswerResponse mapAnswerToResponse(MockAnswer answer) {
        MockAnswerResponse response = new MockAnswerResponse();
        response.setId(answer.getId());
        response.setQuestionId(answer.getQuestion().getId());
        response.setSubject(answer.getSubject());
        response.setTopic(answer.getTopic());
        response.setUserAnswer(answer.getUserAnswer());
        response.setResult(answer.getResult());
        response.setMarksAwarded(answer.getMarksAwarded());
        response.setTimeTakenSeconds(answer.getTimeTakenSeconds());
        response.setIsAttempted(answer.isAttempted());
        return response;
    }
}