package com.BrainBlitz.service;

import com.BrainBlitz.dto.request.MockSessionRequest;
import com.BrainBlitz.dto.response.MockSessionResponse;
import com.BrainBlitz.dto.response.MockResultResponse;
import java.util.List;

public interface MockSessionService {

    // Start a new mock session
    // Returns session id + questions list
    MockSessionResponse startMock(Long userId, MockSessionRequest request);

    // Submit mock — triggers evaluation + performance processing
    MockResultResponse submitMock(Long sessionId, Long userId);

    // Abandon mock — user closes without submitting
    void abandonMock(Long sessionId, Long userId);

    // Timeout mock — timer ran out
    void timeoutMock(Long sessionId, Long userId);

    // Get active/in-progress mock for a user
    MockSessionResponse getActiveMock(Long userId);

    // Get all mocks for a user
    List<MockSessionResponse> getMockHistory(Long userId);

    // Get mock result by session id
    MockResultResponse getMockResult(Long sessionId, Long userId);
}