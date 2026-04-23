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

        // ── DEMO MODE START ── comment this block and uncomment real code when done ──
        MockSession demoSession = new MockSession();
//        demoSession.setId(999L);
        demoSession.setExamType(request.getExamType());
        demoSession.setMockSource(request.getMockSource());
        demoSession.setStatus(MockStatus.IN_PROGRESS);
        demoSession.setStartTime(LocalDateTime.now());
        demoSession.setTotalQuestions(5);
        demoSession.setMaxPossibleScore(500.0);

        // Fetch any 5 real questions from DB regardless of exam type
        List<Question> demoQuestions = questionRepository.findAll(
            PageRequest.of(0, 5)
        ).getContent();

        if (demoQuestions.isEmpty()) {
            throw new RuntimeException("No questions in DB yet. Add some first.");
        }

        User anyUser = userRepository.findById(userId)
        	    .orElseThrow(() -> new RuntimeException("User not found"));
        	demoSession.setUser(anyUser);
        	
        mockSessionRepository.save(demoSession);
        return mapToSessionResponse(demoSession, demoQuestions);
        // ── DEMO MODE END ──


        // ── REAL CODE (commented out for now) ──
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

        // Only allow submitting IN_PROGRESS mocks
        if (session.getStatus() != MockStatus.IN_PROGRESS) {
            throw new RuntimeException(
                "This mock is already " + session.getStatus());
        }

        // Fetch all answers for this session
        List<MockAnswer> answers = mockAnswerRepository
            .findByMockSessionId(sessionId);

        // Calculate scores
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

        // Update session
        session.setStatus(MockStatus.COMPLETED);
        session.setEndTime(LocalDateTime.now());
        session.setDurationSeconds((int) ChronoUnit.SECONDS.between(
            session.getStartTime(), session.getEndTime()));
        session.setCorrect(correct);
        session.setWrong(wrong);
        session.setSkipped(skipped);
        session.setAttempted(attempted);
        session.setTotalScore(Math.max(totalScore, 0)); // never negative total
        session.setAccuracyPercentage(accuracy);
        mockSessionRepository.save(session);

        // Update user total mocks count
        User user = session.getUser();
        user.setTotalMocksAttempted(user.getTotalMocksAttempted() + 1);
        userRepository.save(user);

        // Process performance — update weak areas + level
        // Only process if not already processed
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

        // Still process performance even on timeout
        // User attempted questions — they should count
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

        // Fetch exam pattern
        List<ExamPattern> patterns = roundName != null
            ? examPatternRepository
                .findByExamTypeAndRoundNameAndIsActiveTrue(examType, roundName)
            : examPatternRepository
                .findByExamTypeAndIsActiveTrue(examType);

        if (patterns.isEmpty()) {
            throw new RuntimeException(
                "No exam pattern found for: " + examType);
        }

        // Fetch user weak areas
        List<String> weakTopics = userWeakAreaRepository
            .findByUserIdAndIsWeakTrue(userId)
            .stream()
            .map(UserWeakArea::getTopic)
            .collect(Collectors.toList());

        // Fetch already seen question IDs within cooldown
        // Correct → 30 days, Wrong → 7 days, Skipped → 3 days
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

        // Combine all seen questions to exclude
        Set<Long> excludeIds = new HashSet<>();
        excludeIds.addAll(seenCorrect);
        excludeIds.addAll(seenWrong);
        excludeIds.addAll(seenSkipped);

        List<Question> selectedQuestions = new ArrayList<>();

        // Select questions per pattern topic
        for (ExamPattern pattern : patterns) {

            // Is this a weak topic for user?
            boolean isWeakTopic = weakTopics.contains(pattern.getTopic());

            // Adjust difficulty based on user level and weak area
            // Weak topic → give easier questions first
            // Strong topic → give harder questions
            int adjustedLevel = isWeakTopic
                ? Math.max(userLevel - 1, 1)
                : userLevel;

            // Fetch questions for this topic at adjusted level
         // NEW — uses existing repository method
            List<DifficultyLevel> allowedLevels = getAllowedDifficultyLevels(
                adjustedLevel);

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

        // Shuffle to avoid predictable order
        Collections.shuffle(selectedQuestions);
        return selectedQuestions;
    }

    // ─────────────────────────────────────────────
    // HELPER — Match Difficulty To Level
    // ─────────────────────────────────────────────

    private List<DifficultyLevel> getAllowedDifficultyLevels(int userLevel) {

        // Beginner (level 1-3) → only easy questions
        if (userLevel <= 3) {
            return List.of(
                DifficultyLevel.LEVEL_1,
                DifficultyLevel.LEVEL_2,
                DifficultyLevel.LEVEL_3
            );
        }

        // Intermediate (level 4-6) → easy + medium questions
        if (userLevel <= 6) {
            return List.of(
                DifficultyLevel.LEVEL_1,
                DifficultyLevel.LEVEL_2,
                DifficultyLevel.LEVEL_3,
                DifficultyLevel.LEVEL_4,
                DifficultyLevel.LEVEL_5,
                DifficultyLevel.LEVEL_6
            );
        }

        // Advanced (level 7-10) → all levels
        return List.of(
            DifficultyLevel.LEVEL_1,
            DifficultyLevel.LEVEL_2,
            DifficultyLevel.LEVEL_3,
            DifficultyLevel.LEVEL_4,
            DifficultyLevel.LEVEL_5,
            DifficultyLevel.LEVEL_6,
            DifficultyLevel.LEVEL_7,
            DifficultyLevel.LEVEL_8,
            DifficultyLevel.LEVEL_9,
            DifficultyLevel.LEVEL_10
        );
    }

    // ─────────────────────────────────────────────
    // HELPER — Mark Questions As Seen
    // ─────────────────────────────────────────────

    private void markQuestionsAsSeen(
            Long userId, MockSession session,
            List<Question> questions) {

        User user = session.getUser();

        for (Question question : questions) {

            // Check if already exists — update seenAt
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
                seen.setResult(AnswerResult.SKIPPED); // default until answered
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

        if (patterns.isEmpty()) return totalQuestions; // default 1 mark each

        return patterns.stream()
            .mapToDouble(p -> p.getQuestionsCount() * p.getMarksPerQuestion())
            .sum();
    }

    // ─────────────────────────────────────────────
    // HELPER — Validate Session Ownership
    // ─────────────────────────────────────────────

    private MockSession validateSessionOwnership(
            Long sessionId, Long userId) {

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
     // Add after result.setAiStudyPlan(session.getAiStudyPlan());

        result.setLevelBefore(session.getUserLevelAtTime());
        result.setLevelAfter(session.getUser().getLevel());
        result.setLevelChanged(session.getUserLevelAtTime() != session.getUser().getLevel());
        result.setAnswers(answers.stream()
            .map(this::mapAnswerToResponse)
            .collect(Collectors.toList()));

        // Topic wise breakdown
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
            topicResult.setTotalQuestions(
                topicResult.getTotalQuestions() + 1);

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

        // ✅ id not sessionId
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
        response.setInstructions("Answer all questions. Timer runs for the full test.");
        response.setTimeLimitMinutes(30); // or get from exam pattern
        
        // ✅ Map questions to summary response
        response.setQuestions(questions.stream()
            .map(q -> {
                QuestionSummaryResponse summary = new QuestionSummaryResponse();
                summary.setId(q.getId());
                summary.setQuestionType(q.getQuestionType());
                summary.setQuestionText(q.getQuestionText());
                summary.setSubject(q.getSubject());
                summary.setTopic(q.getTopic());
                summary.setDifficultyLevel(q.getDifficultyLevel());
                summary.setMarks(q.getMarks());
                summary.setNegativeMarks(q.getNegativeMarks());
                summary.setTimeLimitSeconds(q.getTimeLimitSeconds());
                summary.setCorrectOptions(
                	    q.getOptions().stream()
                	        .sorted(Comparator.comparingInt(McqOption::getOptionOrder))
                	        .map(McqOption::isCorrect)
                	        .collect(Collectors.toList())
                	);
                summary.setOptions(
                	    q.getOptions().stream()
                	        .map(opt -> opt.getOptionText())
                	        .collect(Collectors.toList())
                	);
                
//                	summary.setCorrectOptions(q.getCorrectOptions()); // whatever your correct answer field is
                	summary.setExplanation(q.getQuestionExplanation());
                	summary.setHint(q.getHint());
                return summary;
            })
            .collect(Collectors.toList()));

        return response;
    }
    
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