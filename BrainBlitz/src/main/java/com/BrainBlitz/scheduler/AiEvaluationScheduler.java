package com.BrainBlitz.scheduler;

import com.BrainBlitz.entity.MockAnswer;
import com.BrainBlitz.enums.AnswerResult;
import com.BrainBlitz.repository.MockAnswerRepository;
import com.BrainBlitz.service.MockAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AiEvaluationScheduler {

    @Autowired
    private MockAnswerService mockAnswerService;

    @Autowired
    private MockAnswerRepository mockAnswerRepository;

    // ─────────────────────────────────────────────
    // Runs every 30 minutes
    // Processes all PENDING answers waiting for AI evaluation
    // Writing + speech answers are marked PENDING on submit
    // Claude API will evaluate them here (Phase 3)
    // ─────────────────────────────────────────────
    @Scheduled(fixedDelay = 30 * 60 * 1000) // every 30 minutes
    public void processAiEvaluations() {

        // Fetch all answers still pending AI evaluation
    	List<MockAnswer> pendingAnswers = mockAnswerRepository
    		    .findPendingAiEvaluation(AnswerResult.PENDING);

        if (pendingAnswers.isEmpty()) return;

        System.out.println("Processing " + pendingAnswers.size()
            + " pending AI evaluations...");

        for (MockAnswer answer : pendingAnswers) {
            try {
                mockAnswerService.processAiEvaluation(answer.getId());
            } catch (Exception e) {
                // Skip failed — retry next run
                System.err.println("AI evaluation failed for answer: "
                    + answer.getId() + " — " + e.getMessage());
            }
        }

        System.out.println("AI evaluation batch completed.");
    }
}