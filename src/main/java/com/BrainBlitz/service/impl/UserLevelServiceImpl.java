package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.response.UserLevelHistoryResponse;
import com.BrainBlitz.entity.MockSession;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.entity.UserLevelHistory;
import com.BrainBlitz.enums.LevelChangeReason;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.MockSessionRepository;
import com.BrainBlitz.repository.UserLevelHistoryRepository;
import com.BrainBlitz.repository.UserRepository;
import com.BrainBlitz.service.UserLevelService;
import com.BrainBlitz.enums.MockStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserLevelServiceImpl implements UserLevelService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLevelHistoryRepository userLevelHistoryRepository;

    @Autowired
    private MockSessionRepository mockSessionRepository;

    // ─────────────────────────────────────────────
    // LEVEL THRESHOLDS
    // ─────────────────────────────────────────────

    // Accuracy above this → level increases
    private static final double LEVEL_UP_THRESHOLD = 75.0;

    // Accuracy below this → level decreases
    private static final double LEVEL_DOWN_THRESHOLD = 40.0;

    // Inactive days before level decays
    private static final int INACTIVITY_DAYS = 5;

    // Minimum level — never goes below this
    private static final int MIN_LEVEL = 1;

    // Maximum level
    private static final int MAX_LEVEL = 10;

    // ─────────────────────────────────────────────
    // UPDATE LEVEL AFTER MOCK
    // ─────────────────────────────────────────────

    @Override
    public void updateLevelAfterMock(
            Long userId, Long sessionId, double accuracyPercentage) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + userId));

        MockSession session = mockSessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mock session not found with id: " + sessionId));

        int currentLevel = user.getLevel();
        int newLevel = currentLevel;

        // Level UP — accuracy above 75%
        if (accuracyPercentage >= LEVEL_UP_THRESHOLD) {
            newLevel = Math.min(currentLevel + 1, MAX_LEVEL);
        }
        // Level DOWN — accuracy below 40%
        else if (accuracyPercentage < LEVEL_DOWN_THRESHOLD) {
            newLevel = Math.max(currentLevel - 1, MIN_LEVEL);
        }
        // Between 40% and 75% — level stays same

        // Only save history if level actually changed
        if (newLevel != currentLevel) {
            user.setLevel(newLevel);
            userRepository.save(user);

            String description = newLevel > currentLevel
                ? "Level increased from " + currentLevel + " to " + newLevel
                    + " after scoring " + String.format("%.1f", accuracyPercentage) + "%"
                : "Level decreased from " + currentLevel + " to " + newLevel
                    + " after scoring " + String.format("%.1f", accuracyPercentage) + "%";

            saveLevelHistory(
                user, currentLevel, newLevel,
                LevelChangeReason.MOCK_PERFORMANCE,
                session, accuracyPercentage, description
            );
        }

        // Always update lastMockDate
        user.setLastMockDate(LocalDateTime.now());
        user.setLastActiveDate(LocalDateTime.now());
        userRepository.save(user);
    }

    // ─────────────────────────────────────────────
    // STREAK UPDATE
    // ─────────────────────────────────────────────

    @Override
    public void updateStreak(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + userId));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastActive = user.getLastActiveDate();

        if (lastActive == null) {
            // First time — start streak at 1
            user.setStreak(1);
        } else {
            long daysDiff = java.time.temporal.ChronoUnit.DAYS
                .between(lastActive.toLocalDate(), now.toLocalDate());

            if (daysDiff == 1) {
                // Consecutive day — increment streak
                user.setStreak(user.getStreak() + 1);
            } else if (daysDiff > 1) {
                // Streak broken — reset to 1
                int previousStreak = user.getStreak();
                user.setStreak(1);

                // Save history for broken streak
                if (previousStreak > 1) {
                    saveLevelHistory(
                        user, user.getLevel(), user.getLevel(),
                        LevelChangeReason.STREAK_BROKEN,
                        null, null,
                        "Streak broken after " + previousStreak + " days"
                    );
                }
            }
            // daysDiff == 0 → same day, no streak change
        }

        user.setLastActiveDate(now);
        userRepository.save(user);
    }

    // ─────────────────────────────────────────────
    // LEVEL DECAY — SINGLE USER
    // ─────────────────────────────────────────────

    @Override
    public void decayLevelIfInactive(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + userId));

        // Skip if already at minimum level
        if (user.getLevel() <= MIN_LEVEL) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastActive = user.getLastActiveDate();

        // Skip if user has never been active
        if (lastActive == null) return;

        long inactiveDays = java.time.temporal.ChronoUnit.DAYS
            .between(lastActive.toLocalDate(), now.toLocalDate());

        // Decay only if inactive for more than threshold days
        if (inactiveDays >= INACTIVITY_DAYS) {
            int currentLevel = user.getLevel();
            int newLevel = Math.max(currentLevel - 1, MIN_LEVEL);

            user.setLevel(newLevel);
            userRepository.save(user);

            saveLevelHistory(
                user, currentLevel, newLevel,
                LevelChangeReason.INACTIVITY,
                null, null,
                "Level decreased from " + currentLevel + " to " + newLevel
                    + " due to " + inactiveDays + " days of inactivity"
            );
        }
    }

    // ─────────────────────────────────────────────
    // LEVEL DECAY — ALL INACTIVE USERS
    // Called by scheduler every day
    // ─────────────────────────────────────────────

    @Override
    public void decayAllInactiveUsers() {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            try {
                decayLevelIfInactive(user.getId());
            } catch (Exception e) {
                // Skip failed users — don't stop entire batch
                System.err.println("Failed to decay level for user: "
                    + user.getId() + " — " + e.getMessage());
            }
        }
    }

    // ─────────────────────────────────────────────
    // READ
    // ─────────────────────────────────────────────

    @Override
    public List<UserLevelHistoryResponse> getLevelHistory(Long userId) {
        return userLevelHistoryRepository
            .findByUserIdOrderByChangedAtDesc(userId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public UserLevelHistoryResponse getLastLevelChange(Long userId) {
        return userLevelHistoryRepository
            .findTopByUserIdOrderByChangedAtDesc(userId)
            .map(this::mapToResponse)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No level history found for user: " + userId));
    }

    // ─────────────────────────────────────────────
    // HELPER — Save Level History
    // ─────────────────────────────────────────────

    private void saveLevelHistory(
            User user, int previousLevel, int newLevel,
            LevelChangeReason reason, MockSession session,
            Double triggerScore, String description) {

        UserLevelHistory history = new UserLevelHistory();
        history.setUser(user);
        history.setPreviousLevel(previousLevel);
        history.setNewLevel(newLevel);
        history.setReason(reason);
        history.setMockSession(session);
        history.setTriggerScore(triggerScore);
        history.setDescription(description);

        userLevelHistoryRepository.save(history);
    }

    // ─────────────────────────────────────────────
    // HELPER — Map Entity To Response
    // ─────────────────────────────────────────────

    private UserLevelHistoryResponse mapToResponse(UserLevelHistory history) {
        UserLevelHistoryResponse response = new UserLevelHistoryResponse();
        response.setId(history.getId());
        response.setUserId(history.getUser().getId());
        response.setPreviousLevel(history.getPreviousLevel());
        response.setNewLevel(history.getNewLevel());
        response.setReason(history.getReason());
        response.setTriggerScore(history.getTriggerScore());
        response.setDescription(history.getDescription());
        response.setChangedAt(history.getChangedAt());
        if (history.getMockSession() != null) {
            response.setSessionId(history.getMockSession().getId());
        }
        return response;
    }
}