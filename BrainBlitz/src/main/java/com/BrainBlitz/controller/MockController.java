package com.BrainBlitz.controller;

import com.BrainBlitz.dto.ApiResponse;
import com.BrainBlitz.dto.request.MockAnswerRequest;
import com.BrainBlitz.dto.request.MockSessionRequest;
import com.BrainBlitz.dto.response.MockAnswerResponse;
import com.BrainBlitz.dto.response.MockResultResponse;
import com.BrainBlitz.dto.response.MockSessionResponse;
import com.BrainBlitz.service.MockAnswerService;
import com.BrainBlitz.service.MockSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mock")
public class MockController {

    @Autowired
    private MockSessionService mockSessionService;

    @Autowired
    private MockAnswerService mockAnswerService;

    // ─────────────────────────────────────────────
    // START MOCK
    // POST /api/mock/start?userId=1
    // ─────────────────────────────────────────────
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<?>> startMock(
            @RequestParam Long userId,
            @RequestBody MockSessionRequest request) {
        try {
            MockSessionResponse response =
                mockSessionService.startMock(userId, request);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Mock started successfully",
                    response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ─────────────────────────────────────────────
    // SAVE ANSWER
    // POST /api/mock/{sessionId}/answer?userId=1
    // ─────────────────────────────────────────────
    @PostMapping("/{sessionId}/answer")
    public ResponseEntity<ApiResponse<?>> saveAnswer(
            @PathVariable Long sessionId,
            @RequestParam Long userId,
            @RequestBody MockAnswerRequest request) {
        try {
            MockAnswerResponse response =
                mockAnswerService.saveAnswer(sessionId, request);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Answer saved successfully",
                    response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ─────────────────────────────────────────────
    // SUBMIT MOCK
    // POST /api/mock/{sessionId}/submit?userId=1
    // ─────────────────────────────────────────────
    @PostMapping("/{sessionId}/submit")
    public ResponseEntity<ApiResponse<?>> submitMock(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        try {
            MockResultResponse response =
                mockSessionService.submitMock(sessionId, userId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Mock submitted successfully",
                    response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ─────────────────────────────────────────────
    // ABANDON MOCK
    // POST /api/mock/{sessionId}/abandon?userId=1
    // ─────────────────────────────────────────────
    @PostMapping("/{sessionId}/abandon")
    public ResponseEntity<ApiResponse<?>> abandonMock(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        try {
            mockSessionService.abandonMock(sessionId, userId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Mock abandoned", null, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ─────────────────────────────────────────────
    // TIMEOUT MOCK
    // POST /api/mock/{sessionId}/timeout?userId=1
    // ─────────────────────────────────────────────
    @PostMapping("/{sessionId}/timeout")
    public ResponseEntity<ApiResponse<?>> timeoutMock(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        try {
            mockSessionService.timeoutMock(sessionId, userId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Mock timed out", null, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ─────────────────────────────────────────────
    // GET ACTIVE MOCK
    // GET /api/mock/active?userId=1
    // ─────────────────────────────────────────────
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<?>> getActiveMock(
            @RequestParam Long userId) {
        try {
            MockSessionResponse response =
                mockSessionService.getActiveMock(userId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Active mock found", response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ─────────────────────────────────────────────
    // GET MOCK HISTORY
    // GET /api/mock/history?userId=1
    // ─────────────────────────────────────────────
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<?>> getMockHistory(
            @RequestParam Long userId) {
        try {
            List<MockSessionResponse> response =
                mockSessionService.getMockHistory(userId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Mock history fetched", response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ─────────────────────────────────────────────
    // GET MOCK RESULT
    // GET /api/mock/{sessionId}/result?userId=1
    // ─────────────────────────────────────────────
    @GetMapping("/{sessionId}/result")
    public ResponseEntity<ApiResponse<?>> getMockResult(
            @PathVariable Long sessionId,
            @RequestParam Long userId) {
        try {
            MockResultResponse response =
                mockSessionService.getMockResult(sessionId, userId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Mock result fetched", response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ─────────────────────────────────────────────
    // GET ALL ANSWERS FOR A SESSION
    // GET /api/mock/{sessionId}/answers
    // ─────────────────────────────────────────────
    @GetMapping("/{sessionId}/answers")
    public ResponseEntity<ApiResponse<?>> getAnswers(
            @PathVariable Long sessionId) {
        try {
            List<MockAnswerResponse> response =
                mockAnswerService.getAnswersBySessionId(sessionId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Answers fetched", response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ─────────────────────────────────────────────
    // GET WRONG ANSWERS FOR A SESSION
    // GET /api/mock/{sessionId}/wrong-answers
    // ─────────────────────────────────────────────
    @GetMapping("/{sessionId}/wrong-answers")
    public ResponseEntity<ApiResponse<?>> getWrongAnswers(
            @PathVariable Long sessionId) {
        try {
            List<MockAnswerResponse> response =
                mockAnswerService.getWrongAnswers(sessionId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Wrong answers fetched", response, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }
}