package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.response.AdminDashboardStats;
import com.BrainBlitz.dto.response.UserSummaryResponse;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.repository.MockSessionRepository;
import com.BrainBlitz.repository.QuestionRepository;
import com.BrainBlitz.repository.UserRepository;
import com.BrainBlitz.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AdminServiceImpl implements AdminService {

    private  UserRepository userRepository;
    private  QuestionRepository questionRepository;
    private  MockSessionRepository mockSessionRepository;

    // ═══════════════════════════════════════════════════════════
    // DASHBOARD STATS
    // ═══════════════════════════════════════════════════════════

    public AdminServiceImpl(UserRepository userRepository,
				            QuestionRepository questionRepository,
				            MockSessionRepository mockSessionRepository) {
				this.userRepository = userRepository;
				this.questionRepository = questionRepository;
				this.mockSessionRepository = mockSessionRepository;
	}
    
    @Override
    public AdminDashboardStats getDashboardStats() {

        // ── today boundaries ──────────────────────────────────
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd   = todayStart.plusDays(1);

        // ── last 7 days boundary ──────────────────────────────
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);

        // ── user counts ───────────────────────────────────────
        long totalUsers       = userRepository.count();
        long newUsersToday    = userRepository.countByCreatedAtBetween(todayStart, todayEnd);
        long activeUsersWeek  = mockSessionRepository.countDistinctUserIdSince(weekAgo);

        // ── question counts ───────────────────────────────────
        long totalQuestions   = questionRepository.count();
        long activeQuestions  = questionRepository.countByIsActiveTrue();

        // ── mock counts ───────────────────────────────────────
        long totalMocksToday    = mockSessionRepository.countByCreatedAtBetween(todayStart, todayEnd);
        long totalMocksAllTime  = mockSessionRepository.count();

        // ── average accuracy (platform-wide) ─────────────────
        // Uses completed mocks only — avoids skewing by abandoned sessions
        Double avgAccuracy = mockSessionRepository.findAverageAccuracy();
        double accuracy = (avgAccuracy != null) ? avgAccuracy : 0.0;

        // ── AI question count ─────────────────────────────────
        long aiGeneratedCount = questionRepository.countByIsAiGeneratedTrue();

        return AdminDashboardStats.builder()
                .totalUsers(totalUsers)
                .newUsersToday(newUsersToday)
                .activeUsersWeek(activeUsersWeek)
                .totalQuestions(totalQuestions)
                .activeQuestions(activeQuestions)
                .totalMocksToday(totalMocksToday)
                .totalMocksAllTime(totalMocksAllTime)
                .avgAccuracy(Math.round(accuracy * 10.0) / 10.0) // round to 1 decimal
                .aiGeneratedCount(aiGeneratedCount)
                .build();
    }

    // ═══════════════════════════════════════════════════════════
    // USER LIST
    // ═══════════════════════════════════════════════════════════

    @Override
    public Page<UserSummaryResponse> getAllUsers(String search, Pageable pageable) {

        Page<User> users;

        if (search != null && !search.isBlank()) {
            // search by name OR email (case-insensitive)
            users = userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    search.trim(), search.trim(), pageable
            );
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(this::toSummaryResponse);
    }

    // ─── mapper ──────────────────────────────────────────────────────────────
    private UserSummaryResponse toSummaryResponse(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : "USER")
                .currentLevel(user.getLevel())
                .currentStreak(user.getStreak())
                .isEmailVerified(user.isEmailVerified())
                .isActive(user.isActive())
//                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastActiveDate())
                .build();
    }
}
