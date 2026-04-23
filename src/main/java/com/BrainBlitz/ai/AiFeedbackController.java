package com.BrainBlitz.ai;


import com.BrainBlitz.dto.request.FeedbackRequestDto;
import com.BrainBlitz.dto.response.FeedbackResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiFeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<FeedbackResponseDto> getFeedback(
            @RequestBody FeedbackRequestDto request) {
        FeedbackResponseDto response = feedbackService.generateFeedback(request);
        return ResponseEntity.ok(response);
    }

    // Test endpoint
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("AI Feedback Service is running! ✅");
    }
}
