package com.BrainBlitz.service;

import com.BrainBlitz.dto.response.UserLevelHistoryResponse;
import java.util.List;

public interface UserLevelService {

    // Increase or decrease level based on mock performance
    void updateLevelAfterMock(Long userId, Long sessionId, double accuracyPercentage);

    // Decay level due to inactivity
    // Called by scheduler daily
    void decayLevelIfInactive(Long userId);

    // Decay levels for ALL inactive users
    // Called by scheduler — processes entire user base
    void decayAllInactiveUsers();

    // Update streak after mock
    void updateStreak(Long userId);

    // Fetch level history for a user
    List<UserLevelHistoryResponse> getLevelHistory(Long userId);

    // Fetch last level change for a user
    UserLevelHistoryResponse getLastLevelChange(Long userId);
}