package com.BrainBlitz.repository;

import com.BrainBlitz.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.BrainBlitz.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // ✅ Find all unverified users whose OTP has expired
    List<User> findByIsEmailVerifiedFalseAndOtpExpiryBefore(LocalDateTime now);
    
 // ── NEW: needed by AdminServiceImpl ─────────────────────
    
    // Count users registered between two timestamps
    // Used for "new users today" dashboard stat
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
 
    // Search users by name OR email (case-insensitive)
    // Used by admin user list search box
    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name,
            String email,
            Pageable pageable
    );
    
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    
    
}