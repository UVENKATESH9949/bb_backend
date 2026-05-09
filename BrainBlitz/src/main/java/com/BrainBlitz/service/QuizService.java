// src/main/java/com/BrainBlitz/service/QuizService.java
package com.BrainBlitz.service;

import com.BrainBlitz.dto.request.CheckAnswerRequest;
import com.BrainBlitz.dto.response.CheckAnswerResponse;
import com.BrainBlitz.entity.User;

public interface QuizService {
    CheckAnswerResponse checkAnswer(CheckAnswerRequest request, User user);
}