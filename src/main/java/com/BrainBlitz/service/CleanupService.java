package com.BrainBlitz.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.BrainBlitz.entity.User;
import com.BrainBlitz.repository.UserRepository;

@Service
public class CleanupService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Runs every 1 minute to check for expired OTPs
    @Scheduled(fixedRate = 60000)
    public void removeExpiredUnverifiedUsers() {
        List<User> expiredUsers = userRepository
            .findByIsEmailVerifiedFalseAndOtpExpiryBefore(LocalDateTime.now());

        if (!expiredUsers.isEmpty()) {
            userRepository.deleteAll(expiredUsers);
            System.out.println("🗑️ Removed " + expiredUsers.size() + " unverified expired user(s) at " + LocalDateTime.now());
        }
    }
}