package com.BrainBlitz.service;

import com.BrainBlitz.dto.request.PracticeSubmitRequest;
import com.BrainBlitz.dto.response.QuestionResponse;
import com.BrainBlitz.entity.PracticeSession;

import java.util.List;

public interface PracticeService {

    // Fetch questions for practice
    List<QuestionResponse> getPracticeQuestions(
        String email, String examType,
        String subject, int count, String language);

    // Save performance after practice
    void submitPractice(String email, PracticeSubmitRequest request);
    
    List<PracticeSession> getRecentHistory(String email, String subject, int limit);
}