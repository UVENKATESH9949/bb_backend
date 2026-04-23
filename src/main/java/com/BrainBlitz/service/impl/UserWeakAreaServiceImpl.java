package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.response.UserWeakAreaResponse;
import com.BrainBlitz.entity.MockAnswer;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.entity.UserWeakArea;
import com.BrainBlitz.enums.AnswerResult;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.MockAnswerRepository;
import com.BrainBlitz.repository.UserRepository;
import com.BrainBlitz.repository.UserWeakAreaRepository;
import com.BrainBlitz.service.UserWeakAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserWeakAreaServiceImpl implements UserWeakAreaService {

    @Autowired
    private UserWeakAreaRepository userWeakAreaRepository;

    @Autowired
    private MockAnswerRepository mockAnswerRepository;

    @Autowired
    private UserRepository userRepository;

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @Override
    public List<UserWeakAreaResponse> getWeakAreasByUserId(Long userId) {
        validateUser(userId);
        return userWeakAreaRepository.findByUserId(userId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserWeakAreaResponse> getWeakTopics(Long userId) {
        validateUser(userId);
        return userWeakAreaRepository.findByUserIdAndIsWeakTrue(userId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserWeakAreaResponse> getSlowTopics(Long userId) {
        validateUser(userId);
        return userWeakAreaRepository.findByUserIdAndIsSlowTrue(userId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserWeakAreaResponse> getWeakTopicsBySubject(
            Long userId, String subject) {
        validateUser(userId);
        return userWeakAreaRepository
            .findByUserIdAndSubjectAndIsWeakTrue(userId, subject)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────
    // UPDATE WEAK AREAS AFTER MOCK
    // ─────────────────────────────────────────────

    @Override
    public void updateWeakAreas(Long userId, Long sessionId) {

        // Fetch all answers for this session
        List<MockAnswer> answers = mockAnswerRepository
            .findByMockSessionId(sessionId);

        // Skip if no answers
        if (answers.isEmpty()) return;

        // Group answers by subject + topic
        // Key = "subject::topic"
        Map<String, List<MockAnswer>> groupedAnswers = answers.stream()
            .collect(Collectors.groupingBy(
                a -> a.getSubject() + "::" + a.getTopic()
            ));

        // Process each subject + topic group
        for (Map.Entry<String, List<MockAnswer>> entry : groupedAnswers.entrySet()) {

            String[] parts = entry.getKey().split("::");
            String subject = parts[0];
            String topic = parts[1];
            List<MockAnswer> topicAnswers = entry.getValue();

            // Calculate metrics for this topic
            long attempted = topicAnswers.stream()
                .filter(a -> a.getResult() != AnswerResult.SKIPPED)
                .count();

            long correct = topicAnswers.stream()
                .filter(a -> a.getResult() == AnswerResult.CORRECT)
                .count();

            double accuracy = attempted > 0
                ? (double) correct / attempted * 100
                : 0;

            double avgTime = topicAnswers.stream()
                .mapToInt(MockAnswer::getTimeTakenSeconds)
                .average()
                .orElse(0);

            // Fetch or create weak area record
            UserWeakArea weakArea = userWeakAreaRepository
                .findByUserIdAndSubjectAndTopic(userId, subject, topic)
                .orElseGet(() -> createNewWeakArea(userId, subject, topic));

            // Update metrics
            // Weighted average with previous data
            int previousAttempted = weakArea.getTotalAttempted();
            int newTotal = previousAttempted + (int) attempted;

            double newAccuracy = newTotal > 0
                ? ((weakArea.getAccuracyPercentage() * previousAttempted)
                    + (accuracy * attempted)) / newTotal
                : accuracy;

            double newAvgTime = newTotal > 0
                ? ((weakArea.getAvgTimeTaken() * previousAttempted)
                    + (avgTime * attempted)) / newTotal
                : avgTime;

            weakArea.setTotalAttempted(newTotal);
            weakArea.setAccuracyPercentage(newAccuracy);
            weakArea.setAvgTimeTaken(newAvgTime);

            // Mark as weak if accuracy below 50%
            // Minimum 3 attempts before marking weak
            // Avoids false weak areas from 1-2 questions
            weakArea.setWeak(newTotal >= 3 && newAccuracy < 50.0);

            // Mark as slow if avg time above 90 seconds per question
            weakArea.setSlow(newAvgTime > 90);

            userWeakAreaRepository.save(weakArea);
        }
    }

    // ─────────────────────────────────────────────
    // RECALCULATE FROM SCRATCH
    // ─────────────────────────────────────────────

    @Override
    public void recalculateWeakAreas(Long userId) {

        // Delete all existing weak areas for user
        List<UserWeakArea> existing = userWeakAreaRepository
            .findByUserId(userId);
        userWeakAreaRepository.deleteAll(existing);

        // Fetch all answers ever given by this user
        List<MockAnswer> allAnswers = mockAnswerRepository
            .findByMockSessionId(userId);

        if (allAnswers.isEmpty()) return;

        // Group by subject + topic
        Map<String, List<MockAnswer>> grouped = allAnswers.stream()
            .collect(Collectors.groupingBy(
                a -> a.getSubject() + "::" + a.getTopic()
            ));

        // Rebuild each weak area from scratch
        for (Map.Entry<String, List<MockAnswer>> entry : grouped.entrySet()) {
            String[] parts = entry.getKey().split("::");
            String subject = parts[0];
            String topic = parts[1];
            List<MockAnswer> topicAnswers = entry.getValue();

            long attempted = topicAnswers.stream()
                .filter(a -> a.getResult() != AnswerResult.SKIPPED)
                .count();

            long correct = topicAnswers.stream()
                .filter(a -> a.getResult() == AnswerResult.CORRECT)
                .count();

            double accuracy = attempted > 0
                ? (double) correct / attempted * 100
                : 0;

            double avgTime = topicAnswers.stream()
                .mapToInt(MockAnswer::getTimeTakenSeconds)
                .average()
                .orElse(0);

            UserWeakArea weakArea = createNewWeakArea(userId, subject, topic);
            weakArea.setTotalAttempted((int) attempted);
            weakArea.setAccuracyPercentage(accuracy);
            weakArea.setAvgTimeTaken(avgTime);
            weakArea.setWeak(attempted >= 3 && accuracy < 50.0);
            weakArea.setSlow(avgTime > 90);

            userWeakAreaRepository.save(weakArea);
        }
    }

    // ─────────────────────────────────────────────
    // HELPER — Create New Weak Area Record
    // ─────────────────────────────────────────────

    private UserWeakArea createNewWeakArea(
            Long userId, String subject, String topic) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + userId));

        UserWeakArea weakArea = new UserWeakArea();
        weakArea.setUser(user);
        weakArea.setSubject(subject);
        weakArea.setTopic(topic);
        weakArea.setAccuracyPercentage(0);
        weakArea.setAvgTimeTaken(0);
        weakArea.setTotalAttempted(0);
        weakArea.setWeak(false);
        weakArea.setSlow(false);
        return weakArea;
    }

    // ─────────────────────────────────────────────
    // HELPER — Validate User Exists
    // ─────────────────────────────────────────────

    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                "User not found with id: " + userId);
        }
    }

    // ─────────────────────────────────────────────
    // HELPER — Map Entity To Response
    // ─────────────────────────────────────────────

    private UserWeakAreaResponse mapToResponse(UserWeakArea weakArea) {
        UserWeakAreaResponse response = new UserWeakAreaResponse();
        response.setId(weakArea.getId());
        response.setUserId(weakArea.getUser().getId());
        response.setSubject(weakArea.getSubject());
        response.setTopic(weakArea.getTopic());
        response.setAccuracyPercentage(weakArea.getAccuracyPercentage());
        response.setAvgTimeTaken(weakArea.getAvgTimeTaken());
        response.setTotalAttempted(weakArea.getTotalAttempted());
        response.setIsWeak(weakArea.isWeak());
        response.setIsSlow(weakArea.isSlow());
        response.setLastUpdated(weakArea.getLastUpdated());
        return response;
    }
}