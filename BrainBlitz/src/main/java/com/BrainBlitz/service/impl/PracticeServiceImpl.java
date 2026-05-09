package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.request.PracticeSubmitRequest;
import com.BrainBlitz.dto.response.QuestionResponse;
import com.BrainBlitz.entity.PracticeSession;
import com.BrainBlitz.entity.Question;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.entity.UserWeakArea;
import com.BrainBlitz.enums.DifficultyLevel;
import com.BrainBlitz.enums.ExamCategory;
import com.BrainBlitz.enums.ExamType;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.*;
import com.BrainBlitz.service.PracticeService;
import com.BrainBlitz.service.QuestionService;
import com.BrainBlitz.service.UserLevelService;
import com.BrainBlitz.service.UserWeakAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import com.BrainBlitz.repository.PracticeSessionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class PracticeServiceImpl implements PracticeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserWeakAreaRepository userWeakAreaRepository;

    @Autowired
    private UserSeenQuestionRepository userSeenQuestionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserWeakAreaService userWeakAreaService;

    @Autowired
    private UserLevelService userLevelService;
    
    @Autowired
    private PracticeSessionRepository practiceSessionRepository;

    // ─────────────────────────────────────────────
    // FETCH QUESTIONS FOR PRACTICE
    // ─────────────────────────────────────────────

    @Override
    public List<QuestionResponse> getPracticeQuestions(
            String email, String examType,
            String subject, int count, String language) {

        // Fetch user
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found"));

        // Get user level
        int userLevel = user.getLevel();

        // Get allowed difficulty levels based on user level
        List<DifficultyLevel> allowedLevels = getAllowedLevels(userLevel);

        // Fetch seen question IDs to exclude
        Set<Long> seenIds = userSeenQuestionRepository
            .findByUserId(user.getId())
            .stream()
            .map(s -> s.getQuestion().getId())
            .collect(Collectors.toSet());

        // Fetch questions
        ExamType examTypeEnum = ExamType.valueOf(examType);

        List<Question> questions = questionRepository
            .findQuestionsForMock(
                examTypeEnum, subject, subject, allowedLevels)
            .stream()
            .filter(q -> !seenIds.contains(q.getId()))
            .limit(count)
            .collect(Collectors.toList());

        // If not enough questions found → fetch without exclusion
        if (questions.size() < count) {
            questions = questionRepository
                .findQuestionsForMock(
                    examTypeEnum, subject, subject, allowedLevels)
                .stream()
                .limit(count)
                .collect(Collectors.toList());
        }

        if (questions.isEmpty()) {
            throw new RuntimeException(
                "No questions found for " + subject +
                " in " + examType);
        }

        // Map to response
        return questions.stream()
            .map(q -> questionService.getQuestionById(q.getId()))
            .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────
    // SUBMIT PRACTICE PERFORMANCE
    // ─────────────────────────────────────────────

    @Override
    public void submitPractice(String email, PracticeSubmitRequest request) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found"));

        double accuracy = request.getTotal() > 0
            ? (double) request.getCorrect() / request.getTotal() * 100
            : 0;

        // Update weak area for this subject
        updateWeakAreaFromPractice(
            user.getId(),
            request.getSubject(),
            request.getCorrect(),
            request.getTotal()
        );

        // Update user level based on practice performance
        // Practice affects level but less aggressively than mock
        // Only level up if accuracy > 80%
        // Only level down if accuracy < 30%
        if (accuracy >= 80) {
            userLevelService.updateLevelAfterMock(
                user.getId(), null, accuracy);
        } else if (accuracy < 30) {
            userLevelService.updateLevelAfterMock(
                user.getId(), null, accuracy);
        }
        
     // Save practice session
        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setExamCategory(ExamCategory.valueOf(request.getCategory()));
        session.setExamType(ExamType.valueOf(request.getExamType()));
        session.setSubject(request.getSubject());
        session.setMode(request.getMode());
        session.setTotalQuestions(request.getTotal());
        session.setCorrect(request.getCorrect());
        session.setWrong(request.getWrong());
        session.setSkipped(request.getTotal() - request.getCorrect() - request.getWrong());
        session.setScore(request.getScore());
        session.setAccuracyPercentage(accuracy);
        session.setDurationSeconds(request.getDurationSeconds());
        practiceSessionRepository.save(session);

        // Update streak
        userLevelService.updateStreak(user.getId());

        // Update total mocks attempted
        user.setTotalMocksAttempted(
            user.getTotalMocksAttempted() + 1);
        userRepository.save(user);
    }

    // ─────────────────────────────────────────────
    // HELPER — Update Weak Area From Practice
    // ─────────────────────────────────────────────

    private void updateWeakAreaFromPractice(
            Long userId, String subject,
            int correct, int total) {

        if (total == 0) return;

        double accuracy = (double) correct / total * 100;

        // Find existing weak area or create new
        Optional<UserWeakArea> existing =
            userWeakAreaRepository
                .findByUserIdAndSubjectAndTopic(
                    userId, subject, subject);

        UserWeakArea weakArea;

        if (existing.isPresent()) {
            weakArea = existing.get();

            // Weighted average with previous data
            int previousTotal = weakArea.getTotalAttempted();
            int newTotal = previousTotal + total;

            double newAccuracy = newTotal > 0
                ? ((weakArea.getAccuracyPercentage() * previousTotal)
                    + (accuracy * total)) / newTotal
                : accuracy;

            weakArea.setTotalAttempted(newTotal);
            weakArea.setAccuracyPercentage(newAccuracy);
            weakArea.setWeak(newTotal >= 3 && newAccuracy < 50.0);

        } else {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "User not found"));

            weakArea = new UserWeakArea();
            weakArea.setUser(user);
            weakArea.setSubject(subject);
            weakArea.setTopic(subject);
            weakArea.setTotalAttempted(total);
            weakArea.setAccuracyPercentage(accuracy);
            weakArea.setAvgTimeTaken(0);
            weakArea.setWeak(total >= 3 && accuracy < 50.0);
            weakArea.setSlow(false);
        }

        userWeakAreaRepository.save(weakArea);
    }

    // ─────────────────────────────────────────────
    // HELPER — Get Allowed Difficulty Levels
    // ─────────────────────────────────────────────

    private List<DifficultyLevel> getAllowedLevels(int userLevel) {
        if (userLevel <= 3) {
            return List.of(
                DifficultyLevel.LEVEL_1,
                DifficultyLevel.LEVEL_2,
                DifficultyLevel.LEVEL_3
            );
        }
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
    
    
    @Override
    public List<PracticeSession> getRecentHistory(String email, String subject, int limit) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return practiceSessionRepository
            .findTopByUserAndSubjectOrderByCreatedAtDesc(user, subject, 
                PageRequest.of(0, limit));
    }
}