package com.BrainBlitz.scheduler;

import com.BrainBlitz.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class TokenCleanupScheduler {

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanExpiredTokens() {
        blacklistedTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        System.out.println("Expired blacklisted tokens cleaned up at: " + LocalDateTime.now());
    }
}