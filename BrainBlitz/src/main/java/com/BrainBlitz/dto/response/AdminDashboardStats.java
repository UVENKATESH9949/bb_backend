package com.BrainBlitz.dto.response;

public class AdminDashboardStats {

    // ── User stats ──────────────────────────────────────────
    private long totalUsers;
    private long newUsersToday;
    private long activeUsersWeek;

    // ── Question stats ───────────────────────────────────────
    private long totalQuestions;
    private long activeQuestions;

    // ── Mock stats ───────────────────────────────────────────
    private long totalMocksToday;
    private long totalMocksAllTime;
    private double avgAccuracy;

    // ── AI stats ─────────────────────────────────────────────
    private long aiGeneratedCount;

    // 🔹 Private constructor
    private AdminDashboardStats(Builder builder) {
        this.totalUsers = builder.totalUsers;
        this.newUsersToday = builder.newUsersToday;
        this.activeUsersWeek = builder.activeUsersWeek;
        this.totalQuestions = builder.totalQuestions;
        this.activeQuestions = builder.activeQuestions;
        this.totalMocksToday = builder.totalMocksToday;
        this.totalMocksAllTime = builder.totalMocksAllTime;
        this.avgAccuracy = builder.avgAccuracy;
        this.aiGeneratedCount = builder.aiGeneratedCount;
    }

    // 🔹 Static builder() method
    public static Builder builder() {
        return new Builder();
    }

    // 🔹 Builder class
    public static class Builder {

        private long totalUsers;
        private long newUsersToday;
        private long activeUsersWeek;

        private long totalQuestions;
        private long activeQuestions;

        private long totalMocksToday;
        private long totalMocksAllTime;
        private double avgAccuracy;

        private long aiGeneratedCount;

        public Builder totalUsers(long totalUsers) {
            this.totalUsers = totalUsers;
            return this;
        }

        public Builder newUsersToday(long newUsersToday) {
            this.newUsersToday = newUsersToday;
            return this;
        }

        public Builder activeUsersWeek(long activeUsersWeek) {
            this.activeUsersWeek = activeUsersWeek;
            return this;
        }

        public Builder totalQuestions(long totalQuestions) {
            this.totalQuestions = totalQuestions;
            return this;
        }

        public Builder activeQuestions(long activeQuestions) {
            this.activeQuestions = activeQuestions;
            return this;
        }

        public Builder totalMocksToday(long totalMocksToday) {
            this.totalMocksToday = totalMocksToday;
            return this;
        }

        public Builder totalMocksAllTime(long totalMocksAllTime) {
            this.totalMocksAllTime = totalMocksAllTime;
            return this;
        }

        public Builder avgAccuracy(double avgAccuracy) {
            this.avgAccuracy = avgAccuracy;
            return this;
        }

        public Builder aiGeneratedCount(long aiGeneratedCount) {
            this.aiGeneratedCount = aiGeneratedCount;
            return this;
        }

        // 🔹 Build method
        public AdminDashboardStats build() {
            return new AdminDashboardStats(this);
        }
    }

    // 🔹 Getters

    public long getTotalUsers() { return totalUsers; }
    public long getNewUsersToday() { return newUsersToday; }
    public long getActiveUsersWeek() { return activeUsersWeek; }
    public long getTotalQuestions() { return totalQuestions; }
    public long getActiveQuestions() { return activeQuestions; }
    public long getTotalMocksToday() { return totalMocksToday; }
    public long getTotalMocksAllTime() { return totalMocksAllTime; }
    public double getAvgAccuracy() { return avgAccuracy; }
    public long getAiGeneratedCount() { return aiGeneratedCount; }
}