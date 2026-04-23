// src/main/java/com/BrainBlitz/controller/QuizController.java
package com.BrainBlitz.controller;

import com.BrainBlitz.dto.request.CheckAnswerRequest;
import com.BrainBlitz.dto.response.CheckAnswerResponse;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // POST /api/questions/check
    @PostMapping("/check")
    public ResponseEntity<CheckAnswerResponse> checkAnswer(
            @RequestBody CheckAnswerRequest request,
            @AuthenticationPrincipal User user) {

        CheckAnswerResponse response = quizService.checkAnswer(request, user);
        return ResponseEntity.ok(response);
    }
}