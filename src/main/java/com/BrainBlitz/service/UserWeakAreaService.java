package com.BrainBlitz.service;

import com.BrainBlitz.dto.response.UserWeakAreaResponse;
import java.util.List;

public interface UserWeakAreaService {

    // Fetch all weak areas for a user
    List<UserWeakAreaResponse> getWeakAreasByUserId(Long userId);

    // Fetch only weak topics for a user
    List<UserWeakAreaResponse> getWeakTopics(Long userId);

    // Fetch only slow topics for a user
    List<UserWeakAreaResponse> getSlowTopics(Long userId);

    // Fetch weak topics for a specific subject
    List<UserWeakAreaResponse> getWeakTopicsBySubject(Long userId, String subject);

    // Update weak areas after mock is submitted
    // Called by MockSessionService after every mock
    void updateWeakAreas(Long userId, Long sessionId);

    // Recalculate all weak areas for a user from scratch
    // Used if data inconsistency detected
    void recalculateWeakAreas(Long userId);
}