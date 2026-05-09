package com.BrainBlitz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BrainBlitz.dto.ApiResponse;
import com.BrainBlitz.dto.LoginRequest;
import com.BrainBlitz.dto.response.AdminDashboardStats;
import com.BrainBlitz.dto.response.UserSummaryResponse;
import com.BrainBlitz.entity.User;
//import com.BrainBlitz.dto.response.UserDetailResponse;
import com.BrainBlitz.service.AdminService;
import com.BrainBlitz.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    // ✅ Admin Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> adminLogin(
            @RequestBody LoginRequest request) {
        try {
            String token = userService.adminLogin(request);
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Admin login successful", token, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(
                new ApiResponse<>(false, e.getMessage(), null, 401)
            );
        }
    }

    // ✅ Admin Logout
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

    // ✅ Admin Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<?>> adminDashboard() {
        AdminDashboardStats stats = adminService.getDashboardStats();
        return ResponseEntity.ok(new ApiResponse<>(true, "Stats", stats, 200));
    }

    // ✅ GET ALL USERS (Pagination + Search)
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {

        Page<UserSummaryResponse> users = userService.getAllUsers(page, size, search);

        return ResponseEntity.ok(
            new ApiResponse<>(true, "Users fetched successfully", users, 200)
        );
    }

    // ✅ GET USER DETAILS (Mock history + Level history)
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable String email) {

        User user = userService.getUserByEmail(email);

        return ResponseEntity.ok(
            new ApiResponse<>(true, "User details fetched", user, 200)
        );
    }

    // ✅ Force logout a specific user
    // DELETE /api/admin/users/{userId}/session
    @DeleteMapping("/users/{userId}/session")
    public ResponseEntity<ApiResponse<?>> forceLogoutUser(
            @PathVariable Long userId) {
        try {
            String result = userService.forceLogoutUser(userId);
            return ResponseEntity.ok(
                new ApiResponse<>(true, result, null, 200)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, e.getMessage(), null, 400)
            );
        }
    }
}