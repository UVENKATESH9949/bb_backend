package com.BrainBlitz.dto.response;

import com.BrainBlitz.enums.*;
import java.time.LocalDateTime;
import java.util.List;

public class MockSessionResponse {

    private Long id;
    private Long userId;
    private ExamType examType;
    private MockSource mockSource;
    private String roundName;
    private Integer userLevelAtTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationSeconds;
    private Integer totalQuestions;
    private Integer attempted;
    private Integer correct;
    private Integer wrong;
    private Integer skipped;
    private Double totalScore;
    private Double maxPossibleScore;
    private Double accuracyPercentage;
    private MockStatus status;
    private String aiStudyPlan;
    private Boolean isPerformanceProcessed;
    private LocalDateTime createdAt;
    private String title;
    private String instructions;
    private Integer timeLimitMinutes;

    // ── NEW — replaces flat List<QuestionSummaryResponse> ──
    private List<QuestionItem> questions;

    // ══════════════════════════════════════════════════════
    // QUESTION ITEM WRAPPER
    // ══════════════════════════════════════════════════════

    public static class QuestionItem {

        public enum ItemType { STANDALONE, GROUPED }

        private ItemType itemType;
        private QuestionSummaryResponse question;       // set when STANDALONE
        private GroupedQuestionItem groupedQuestion;    // set when GROUPED

        public static QuestionItem standalone(QuestionSummaryResponse q) {
            QuestionItem item = new QuestionItem();
            item.itemType = ItemType.STANDALONE;
            item.question = q;
            return item;
        }

        public static QuestionItem grouped(GroupedQuestionItem g) {
            QuestionItem item = new QuestionItem();
            item.itemType = ItemType.GROUPED;
            item.groupedQuestion = g;
            return item;
        }

        public ItemType getItemType() { return itemType; }
        public QuestionSummaryResponse getQuestion() { return question; }
        public GroupedQuestionItem getGroupedQuestion() { return groupedQuestion; }
    }

    // ══════════════════════════════════════════════════════
    // GROUPED QUESTION ITEM — parent data + children
    // ══════════════════════════════════════════════════════

    public static class GroupedQuestionItem {

        private Long groupId;
        private GroupType groupType;
        private String title;
        private String instructions;
        private String instructionsHindi;

        // RC / CLOZE / CASELET
        private String passageText;
        private String passageTextHindi;
        private Integer blankCount;

        // TABLE_DI
        private String tableDataJson;

        // BAR / PIE / LINE chart DI
        private String chartDataJson;
        private String chartImageUrl;

        // Child questions ordered by questionOrderInGroup
        private List<QuestionSummaryResponse> subQuestions;

        public GroupedQuestionItem(Long groupId, GroupType groupType,
                                   String title, String instructions,
                                   String instructionsHindi, String passageText,
                                   String passageTextHindi, Integer blankCount,
                                   String tableDataJson, String chartDataJson,
                                   String chartImageUrl,
                                   List<QuestionSummaryResponse> subQuestions) {
            this.groupId = groupId;
            this.groupType = groupType;
            this.title = title;
            this.instructions = instructions;
            this.instructionsHindi = instructionsHindi;
            this.passageText = passageText;
            this.passageTextHindi = passageTextHindi;
            this.blankCount = blankCount;
            this.tableDataJson = tableDataJson;
            this.chartDataJson = chartDataJson;
            this.chartImageUrl = chartImageUrl;
            this.subQuestions = subQuestions;
        }

        public Long getGroupId() { return groupId; }
        public GroupType getGroupType() { return groupType; }
        public String getTitle() { return title; }
        public String getInstructions() { return instructions; }
        public String getInstructionsHindi() { return instructionsHindi; }
        public String getPassageText() { return passageText; }
        public String getPassageTextHindi() { return passageTextHindi; }
        public Integer getBlankCount() { return blankCount; }
        public String getTableDataJson() { return tableDataJson; }
        public String getChartDataJson() { return chartDataJson; }
        public String getChartImageUrl() { return chartImageUrl; }
        public List<QuestionSummaryResponse> getSubQuestions() { return subQuestions; }
    }

    // ══════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ══════════════════════════════════════════════════════

    public MockSessionResponse() {}

    public MockSessionResponse(Long id, Long userId, ExamType examType,
            MockSource mockSource, Integer userLevelAtTime,
            LocalDateTime startTime, Integer totalQuestions,
            Double maxPossibleScore, MockStatus status,
            LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.examType = examType;
        this.mockSource = mockSource;
        this.userLevelAtTime = userLevelAtTime;
        this.startTime = startTime;
        this.totalQuestions = totalQuestions;
        this.maxPossibleScore = maxPossibleScore;
        this.status = status;
        this.createdAt = createdAt;
    }

    // ══════════════════════════════════════════════════════
    // GETTERS AND SETTERS
    // ══════════════════════════════════════════════════════

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public ExamType getExamType() { return examType; }
    public void setExamType(ExamType examType) { this.examType = examType; }

    public MockSource getMockSource() { return mockSource; }
    public void setMockSource(MockSource mockSource) { this.mockSource = mockSource; }

    public String getRoundName() { return roundName; }
    public void setRoundName(String roundName) { this.roundName = roundName; }

    public Integer getUserLevelAtTime() { return userLevelAtTime; }
    public void setUserLevelAtTime(Integer userLevelAtTime) { this.userLevelAtTime = userLevelAtTime; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Integer getAttempted() { return attempted; }
    public void setAttempted(Integer attempted) { this.attempted = attempted; }

    public Integer getCorrect() { return correct; }
    public void setCorrect(Integer correct) { this.correct = correct; }

    public Integer getWrong() { return wrong; }
    public void setWrong(Integer wrong) { this.wrong = wrong; }

    public Integer getSkipped() { return skipped; }
    public void setSkipped(Integer skipped) { this.skipped = skipped; }

    public Double getTotalScore() { return totalScore; }
    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }

    public Double getMaxPossibleScore() { return maxPossibleScore; }
    public void setMaxPossibleScore(Double maxPossibleScore) { this.maxPossibleScore = maxPossibleScore; }

    public Double getAccuracyPercentage() { return accuracyPercentage; }
    public void setAccuracyPercentage(Double accuracyPercentage) { this.accuracyPercentage = accuracyPercentage; }

    public MockStatus getStatus() { return status; }
    public void setStatus(MockStatus status) { this.status = status; }

    public String getAiStudyPlan() { return aiStudyPlan; }
    public void setAiStudyPlan(String aiStudyPlan) { this.aiStudyPlan = aiStudyPlan; }

    public Boolean getIsPerformanceProcessed() { return isPerformanceProcessed; }
    public void setIsPerformanceProcessed(Boolean isPerformanceProcessed) { this.isPerformanceProcessed = isPerformanceProcessed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public Integer getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(Integer timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }

    public List<QuestionItem> getQuestions() { return questions; }
    public void setQuestions(List<QuestionItem> questions) { this.questions = questions; }
}