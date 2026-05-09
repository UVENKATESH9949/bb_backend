package com.BrainBlitz.service;

import com.BrainBlitz.dto.request.MockAnswerRequest;
import com.BrainBlitz.dto.response.MockAnswerResponse;
import java.util.List;

public interface MockAnswerService {

    // Save a single answer during mock
    MockAnswerResponse saveAnswer(Long sessionId, MockAnswerRequest request);

    // Fetch all answers for a session
    List<MockAnswerResponse> getAnswersBySessionId(Long sessionId);

    // Fetch all wrong answers for a session
    // Used for weak area calculation
    List<MockAnswerResponse> getWrongAnswers(Long sessionId);

    // Evaluate all answers for a session
    // Called when mock is submitted
    void evaluateAnswers(Long sessionId);

    // Process AI evaluation for writing and speech answers
    // Called by background scheduler
    void processAiEvaluation(Long answerId);
}