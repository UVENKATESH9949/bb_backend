package com.BrainBlitz.service.impl;

import com.BrainBlitz.dto.LoginRequest;
import com.BrainBlitz.dto.request.RegisterRequest;
import com.BrainBlitz.dto.request.ResetPasswordRequest;
import com.BrainBlitz.dto.request.VerifyOtpRequest;
import com.BrainBlitz.entity.User;

public interface UserService {

    // ── existing methods (already in your impl) ──────────────
    String   register(RegisterRequest request);
    Object   verifyOtp(VerifyOtpRequest request);
    String   resendOtp(String email);
    Object   login(LoginRequest request);
    String   logout(String token);
    String   forgotPassword(String email);
    String   resetPassword(ResetPasswordRequest request);
    String   adminLogin(LoginRequest request);
    String   forceLogoutUser(Long userId);

    // ── NEW: needed by /auth/me in UserController ────────────

    // Load a user entity by email
    // Used by /auth/me to restore the session from the JWT token
    User getUserByEmail(String email);

    // Build the same response object that /login returns
    // e.g. { user: {...}, token: "..." }
    // This ensures /auth/me and /login return identical shapes
    // so AuthContext handles both identically
    Object buildUserResponse(User user);
}