package com.BrainBlitz.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BrainBlitz.dto.ApiResponse;
import com.BrainBlitz.dto.LoginRequest;
import com.BrainBlitz.dto.UserResponseDto;
import com.BrainBlitz.entity.User;
import com.BrainBlitz.repository.BlacklistedTokenRepository;
import com.BrainBlitz.service.UserService;
import com.BrainBlitz.util.JwtService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    // ✅ Register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            UserResponseDto dto = userService.convertToDto(savedUser);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "User registered successfully. OTP sent to email.", dto, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifyOtp(
            @RequestParam String email,
            @RequestParam int otp) {
        try {
        	try {
        	    Map<String, Object> result = userService.verifyOtp(email, otp);
        	    return ResponseEntity.ok(
        	        new ApiResponse<>(true, "Email verified successfully", result, 200)
        	    );
        	} catch (RuntimeException e) {
        	    return ResponseEntity.badRequest().body(
        	        new ApiResponse<>(false, e.getMessage(), null, 400)
        	    );
        	}
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(false, e.getMessage(), null, 404)
            );
        }
    }

    // ✅ Resend OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<?>> resendOtp(@RequestParam String email) {
        try {
            String result = userService.resendOtp(email);
            return ResponseEntity.ok(
                new ApiResponse<>(true, result, null, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestParam String email) {
        try {
            String result = userService.forgotPassword(email);
            return ResponseEntity.ok(
                new ApiResponse<>(true, result, null, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(
            @RequestParam String email,
            @RequestParam int otp,
            @RequestParam String newPassword) {
        try {
            String result = userService.resetPassword(email, otp, newPassword);
            return ResponseEntity.ok(
                new ApiResponse<>(true, result, null, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }

    // ✅ User Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestParam String email,
            @RequestParam String password) {
        try {
        	Map<String, Object> result = userService.login(email, password);
        	return ResponseEntity.ok(
        	    new ApiResponse<>(true, "Login successful", result, 200)
        	);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(
                new ApiResponse<>(false, e.getMessage(), null, 401)
            );
        }
    }

    // ✅ User Logout
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(400).body(
                    new ApiResponse<>(false, "Missing or invalid Authorization header", null, 400)
                );
            }
            String token = authHeader.substring(7);
            String result = userService.logout(token);
            return ResponseEntity.ok(
                new ApiResponse<>(true, result, null, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }
    
    
    // GET CURRENT USER  ← THIS WAS MISSING
    // GET /api/auth/me
    // Header: Authorization: Bearer <token>
    //
    // Purpose: session restore on page refresh
    // When user refreshes the browser, AuthContext calls this
    // to restore the user object from the token stored in localStorage.
    // Without this, every refresh logs the user out.
    // ═══════════════════════════════════════════════════════════
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(
                    new ApiResponse<>(false, "Missing or invalid token", null, 401)
                );
            }

            String token = authHeader.substring(7);
            if (!jwtService.validateToken(token) || blacklistedTokenRepository.existsByToken(token)) {
                return ResponseEntity.status(401).body(
                    new ApiResponse<>(false, "Invalid or expired token", null, 401)
                );
            }

            String email = jwtService.extractEmail(token);
            User user = userService.getUserByEmail(email);
            UserResponseDto dto = userService.convertToDto(user);

            return ResponseEntity.ok(
                new ApiResponse<>(true, "Session restored", Map.of("user", dto, "token", token), 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(
                new ApiResponse<>(false, e.getMessage(), null, 401)
            );
        }
    }
    
}
