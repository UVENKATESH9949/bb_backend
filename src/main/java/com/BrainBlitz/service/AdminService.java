package com.BrainBlitz.service;

import com.BrainBlitz.dto.response.AdminDashboardStats;
import com.BrainBlitz.dto.response.UserSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    // Dashboard — aggregate stats for admin home page
    AdminDashboardStats getDashboardStats();

    // User list — paginated, optional search by name or email
    Page<UserSummaryResponse> getAllUsers(String search, Pageable pageable);
}