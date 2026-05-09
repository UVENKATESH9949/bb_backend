package com.BrainBlitz.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.BrainBlitz.dto.LoginRequest;
import com.BrainBlitz.dto.UserResponseDto;
import com.BrainBlitz.entity.Role;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.exception.ResourceNotFoundException;
import com.BrainBlitz.repository.UserRepository;
import com.BrainBlitz.util.JwtService;
import com.BrainBlitz.dto.response.UserSummaryResponse;
import com.BrainBlitz.entity.BlacklistedToken;
import com.BrainBlitz.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Random random = new Random();

    // 1️⃣ Register user + generate OTP + send email
    public User registerUser(User user) {
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            User existing = existingUser.get();
            // If email not verified → allow re-registration
            // Delete old record and create fresh
            if (!existing.isEmailVerified()) {
                userRepository.delete(existing);
            } else {
                throw new RuntimeException("Email already registered");
            }
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default values
        user.setRole(Role.USER); // default role
        user.setScore(0.0);
        user.setLevel(1);
        user.setStreak(0);
        user.setActive(true);
        user.setEmailVerified(false);

        // Generate OTP
        int otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        // Save user in DB
        User savedUser = userRepository.save(user);

        // Send OTP email
        emailService.sendOtpEmail(user.getEmail(), otp);

        return savedUser;
    }

 // ✅ Add this method in UserService.java
    public String resendOtp(String email) {
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if already verified
        if (user.isEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        // Check OTP attempt limit (max 3 attempts)
        if (user.getOtpNumberOfAttempts() >= 3) {
            throw new RuntimeException("Maximum OTP resend limit reached. Please contact support.");
        }

        // Generate new OTP
        int newOtp = generateOtp();
        user.setOtp(newOtp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        // Increment attempt count
        user.setOtpNumberOfAttempts(user.getOtpNumberOfAttempts() + 1);

        // Save updated user
        userRepository.save(user);

        // Send new OTP email
        emailService.sendOtpEmail(email, newOtp);

        return "OTP resent successfully. Attempt " + user.getOtpNumberOfAttempts() + " of 3";
    }
    
    // 2️⃣ Verify OTP
    public Map<String, Object> verifyOtp(String email, int otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() != otp) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setEmailVerified(true);
        user.setOtp(0);
        user.setOtpExpiry(null);
        userRepository.save(user);

        // Generate token immediately after verification
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        user.setCurrentToken(token);
        userRepository.save(user);
        UserResponseDto dto = convertToDto(user);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", dto);
        return result;
    }

    // 3️⃣ Helper method to generate 6-digit OTP
    private int generateOtp() {
        return 100000 + random.nextInt(900000);
    }
    
 // Add this method in UserService.java
    public UserResponseDto convertToDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setScore(user.getScore());
        dto.setRole(user.getRole()); // default role
        dto.setEmailVerified(user.isEmailVerified());
        dto.setTargetExams(user.getTargetExams());
        dto.setLevel(user.getLevel());
        dto.setStreak(user.getStreak());
        dto.setActive(user.isActive());
        return dto;
    }

 // 4️⃣ Forgot Password → Generate OTP & send email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String forgotPassword(String email) {
        // Check if user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Optional: check if email is verified
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified. Please verify your email first.");
        }

        // Generate OTP
        int otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        // Reset OTP attempts (optional but recommended)
        user.setOtpNumberOfAttempts(0);

        // Save updated user
        userRepository.save(user);

        // Send OTP email for password reset
        emailService.sendOtpEmail(email, otp);

        return "Password reset OTP sent to email";
    }
    
    public String resetPassword(String email, int otp, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check OTP
        if (user.getOtp() != otp) {
            throw new RuntimeException("Invalid OTP");
        }

        // Check expiry
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        // Encrypt new password
        user.setPassword(passwordEncoder.encode(newPassword));

        // Clear OTP
        user.setOtp(0);
        user.setOtpExpiry(null);

        userRepository.save(user);

        return "Password reset successfully";
    }
    
    
    public Map<String, Object> login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email first");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        user.setCurrentToken(token);
        userRepository.save(user);
        UserResponseDto dto = convertToDto(user);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", dto);
        return result;
    }
    
    public String logout(String token) {

        // ✅ Validate token first
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        // ✅ Check if already blacklisted
        if (blacklistedTokenRepository.existsByToken(token)) {
            throw new RuntimeException("Already logged out");
        }

        // ✅ Get token expiry to store in DB
        Date expiry = jwtService.extractExpiration(token);
        LocalDateTime expiresAt = expiry.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // ✅ Save to blacklist
        BlacklistedToken blacklistedToken = new BlacklistedToken(
                token,
                LocalDateTime.now(),
                expiresAt
        );
        blacklistedTokenRepository.save(blacklistedToken);

        return "Logged out successfully";
    }
    
    public String adminLogin(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // ✅ Check role FIRST (very important)
        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Access denied: Admin only");
        }

        // ✅ Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // ✅ Optional (recommended)
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
        }

        // ✅ Generate JWT
        return jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );
    }
    public String forceLogoutUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + userId));

        // If user has active token — blacklist it
        if (user.getCurrentToken() != null) {

            // Only blacklist if not already blacklisted
            if (!blacklistedTokenRepository.existsByToken(user.getCurrentToken())) {

                Date expiry = jwtService.extractExpiration(user.getCurrentToken());
                LocalDateTime expiresAt = expiry.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

                BlacklistedToken blacklistedToken = new BlacklistedToken(
                    user.getCurrentToken(),
                    LocalDateTime.now(),
                    expiresAt
                );
                blacklistedTokenRepository.save(blacklistedToken);
            }

            // Clear token from user
            user.setCurrentToken(null);
            userRepository.save(user);
        }

        return "User session terminated successfully";
    }
    
    public Page<UserSummaryResponse> getAllUsers(int page, int size, String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<User> users;

        if (search != null && !search.trim().isEmpty()) {
            users = userRepository.findByEmailContainingIgnoreCase(search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(user -> 
        UserSummaryResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .currentLevel(user.getLevel())
            .isActive(user.isActive())
            .isEmailVerified(user.isEmailVerified())
            .build()
    );
    }
    

//═══════════════════════════════════════════════════════════════════════════
//UserServiceImpl — ADD these two methods to your existing UserServiceImpl
//
//Your existing impl already has: register, verifyOtp, resendOtp,
//login, logout, forgotPassword, resetPassword, adminLogin, forceLogoutUser
//
//ADD the two methods below.
//═══════════════════════════════════════════════════════════════════════════

//─── ADD INSIDE your existing UserServiceImpl class ──────────────────────────
//
//// ── getUserByEmail ────────────────────────────────────────────────────
//// Loads user from DB by email — used by /auth/me to restore session
//@Override
//public User getUserByEmail(String email) {
//   return userRepository.findByEmail(email)
//       .orElseThrow(() -> new RuntimeException("User not found: " + email));
//}
//
//
//// ── buildUserResponse ─────────────────────────────────────────────────
//// Returns the same { user, token } shape that your /login endpoint returns.
//// /auth/me calls this so AuthContext gets an identical object on refresh.
////
//// IMPORTANT: Match this exactly to what your existing login() method returns.
//// If your login() returns a LoginResponse DTO, use that same DTO here.
//// If it returns a Map, return a Map here.
////
//// Example if your login returns Map<String, Object>:
//@Override
//public Object buildUserResponse(User user) {
//   // Generate a fresh token for the session restore
//   // (same as what login does)
//   String token = jwtService.generateToken(user.getEmail());
//
//   // Return the same structure as your login response
//   // Adjust field names to match what your existing /login returns
//   return Map.of(
//       "user", Map.of(
//           "id",              user.getId(),
//           "name",            user.getName(),
//           "email",           user.getEmail(),
//           "role",            user.getRole().name(),
//           "currentLevel",    user.getCurrentLevel(),
//           "currentStreak",   user.getCurrentStreak(),
//           "isEmailVerified", user.isEmailVerified()
//       ),
//       "token", token
//   );
//}
//
//─────────────────────────────────────────────────────────────────────────────
//
//ALTERNATIVE: If your login() method already builds a LoginResponse DTO,
//extract that logic into a private helper method and call it from both
//login() and buildUserResponse():
//
//private LoginResponse toLoginResponse(User user) {
//   String token = jwtService.generateToken(user.getEmail());
//   return new LoginResponse(user.getId(), user.getName(),
//                            user.getEmail(), user.getRole().name(), token);
//}
//
//@Override
//public Object login(LoginRequest request) {
//   // ... validate password ...
//   return toLoginResponse(user);   // ← use helper
//}
//
//@Override
//public Object buildUserResponse(User user) {
//   return toLoginResponse(user);   // ← same helper
//}
//
//─────────────────────────────────────────────────────────────────────────────
//
//This file is a reference guide. Add the methods to your existing
//UserServiceImpl.java — do NOT create a new class.
//─────────────────────────────────────────────────────────────────────────────

public class UserServiceImplAdditions {
 // Reference only — see comments above
}
}
