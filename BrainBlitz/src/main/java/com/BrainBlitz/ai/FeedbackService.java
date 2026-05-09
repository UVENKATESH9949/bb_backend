package com.BrainBlitz.ai;


import com.BrainBlitz.dto.request.FeedbackRequestDto;
import com.BrainBlitz.dto.response.FeedbackResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private OllamaService ollamaService;

    public FeedbackResponseDto generateFeedback(FeedbackRequestDto request) {

        // Build prompt
        String prompt = buildPrompt(request);

        // Call LLaMA
        String feedback = ollamaService.generateFeedback(prompt);

        // Build response
        FeedbackResponseDto response = new FeedbackResponseDto();
        response.setFeedback(feedback);
        response.setCorrect(request.getCorrectAnswer()
                .equalsIgnoreCase(request.getStudentAnswer()));
        response.setStatus("success");

        return response;
    }

    private String buildPrompt(FeedbackRequestDto req) {
        return """
        You are BrainBlitz AI Tutor - expert coach for Indian competitive exams like SSC CGL, RRB NTPC, Banking exams.
        
        === QUESTION DETAILS ===
        Topic: %s
        Question: %s
        Options: A) %s  B) %s  C) %s  D) %s
        Correct Answer: %s
        Student's Answer: %s
        Explanation: %s
        
        === STUDENT PERFORMANCE ===
        Time Taken: %s
        Total Score: %d / %d
        
        Give detailed feedback in this format:
        
        🎯 RESULT: [Correct/Incorrect]
        
        📊 PERFORMANCE ANALYSIS:
        - [Analyze performance]
        
        ❌ WHAT WENT WRONG:
        - [Explain mistake]
        
        ✅ CORRECT APPROACH:
        - [Step by step solution]
        
        📐 FORMULA USED:
        - [Formula and when to use]
        
        💡 CONCEPT TO REMEMBER:
        - [Key concept and shortcut]
        
        ⚠️ COMMON MISTAKES TO AVOID:
        - [List mistakes]
        
        🎯 PRACTICE QUESTION:
        - [One similar question]
        
        🌟 MOTIVATION:
        - [Encouraging message]
        """.formatted(
                req.getTopic(),
                req.getQuestion(),
                req.getOptionA(), req.getOptionB(),
                req.getOptionC(), req.getOptionD(),
                req.getCorrectAnswer(),
                req.getStudentAnswer(),
                req.getExplanation(),
                req.getTimeTaken(),
                req.getTotalScore(),
                req.getTotalQuestions()
        );
    }
}
