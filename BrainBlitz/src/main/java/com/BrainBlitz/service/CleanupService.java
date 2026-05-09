package com.BrainBlitz.service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.repository.UserRepository;

@Service
public class CleanupService {

    private static final Logger log = LoggerFactory.getLogger(CleanupService.class);

    @Autowired
    private UserRepository userRepository;

    // Runs every 10 minutes
    @Scheduled(fixedRate = 600000)
    public void removeExpiredUnverifiedUsers() {
        List<User> expiredUsers = userRepository
            .findByIsEmailVerifiedFalseAndOtpExpiryBefore(LocalDateTime.now());

        if (!expiredUsers.isEmpty()) {
            userRepository.deleteAll(expiredUsers);
            log.info("Removed {} unverified expired user(s)", expiredUsers.size());
        }
        // No log when nothing to delete — avoids noise
    }
}