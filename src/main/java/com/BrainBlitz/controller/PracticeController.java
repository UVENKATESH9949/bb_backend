package com.BrainBlitz.controller;

import com.BrainBlitz.dto.ApiResponse;
import com.BrainBlitz.dto.request.PracticeSubmitRequest;
import com.BrainBlitz.entity.PracticeSession;
import com.BrainBlitz.entity.Question;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.repository.QuestionRepository;
import com.BrainBlitz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/practice")
public class PracticeController {

    @Autowired
    private PracticeService practiceService;

    @Autowired
    private QuestionRepository questionRepository;
    // ✅ Fetch questions for practice session
    // GET /api/practice/questions?examType=SSC_CGL&subject=Reasoning&count=10
//    @GetMapping("/questions")
//    public ResponseEntity<ApiResponse<?>> getPracticeQuestions(
//            @RequestParam String examType,
//            @RequestParam String subject,
//            @RequestParam(defaultValue = "10") int count,
//            @RequestParam(defaultValue = "english") String language,
//            Principal principal) {
//        try {
//            String email = principal.getName();
//            var response = practiceService.getPracticeQuestions(
//                email, examType, subject, count, language);
//            return ResponseEntity.ok(
//                new ApiResponse<>(true, "Questions fetched", response, 200)
//            );
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(
//                new ApiResponse<>(false, e.getMessage(), null, 400)
//            );
//        }
//    }
    
    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<?>> getQuestions(
        @RequestParam String category,
        @RequestParam String exam,
        @RequestParam String subject,
        @RequestParam String subTopics,
        @RequestParam(defaultValue = "10") int count,
        @RequestParam String mode,
        @RequestParam(required = false, defaultValue = "en") String language
    ) {
        try {
            // Use these params to filter questions, or ignore them for now
        	List<Question> questions = questionRepository.findAll(
        		    org.springframework.data.domain.PageRequest.of(0, 7)
        		).getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("questions", questions);
            response.put("levelId", null);
            response.put("timeLimit", 30);
            response.put("title", "Demo Quiz");
            response.put("difficulty", "easy");

            return ResponseEntity.ok(
                new ApiResponse<>(true, "Questions fetched", response, 200)
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Submit practice session performance
    // POST /api/practice/submit
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<?>> submitPractice(
            @RequestBody PracticeSubmitRequest request,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(401).body(
                new ApiResponse<>(false, "Not authenticated - please login again", null, 401)
            );
        }

        try {
            practiceService.submitPractice(user.getEmail(), request);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Performance saved", null, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }
    
    @GetMapping("/history")
    public ResponseEntity<?> getPracticeHistory(
            @RequestParam String subject,
            @RequestParam(defaultValue = "5") int limit,
            Principal principal) {
        try {
            String email = principal.getName();
            List<PracticeSession> history = practiceService.getRecentHistory(email, subject, limit);
            return ResponseEntity.ok(new ApiResponse<>(true, "History fetched", history, 200));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400));
        }
    }
}