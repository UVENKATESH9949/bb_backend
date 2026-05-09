package com.BrainBlitz.scheduler;

import com.BrainBlitz.service.UserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LevelDecayScheduler {

    @Autowired
    private UserLevelService userLevelService;

    // ─────────────────────────────────────────────
    // Runs every day at midnight
    // Decays level for all inactive users
    // ─────────────────────────────────────────────
    @Scheduled(cron = "0 0 0 * * *")
    public void decayInactiveUserLevels() {
        System.out.println("Running level decay scheduler...");
        userLevelService.decayAllInactiveUsers();
        System.out.println("Level decay completed.");
    }
}