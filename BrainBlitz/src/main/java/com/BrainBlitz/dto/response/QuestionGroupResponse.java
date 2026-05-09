package com.BrainBlitz.dto.response;


public class QuestionGroupResponse {

    private Long id;
    private String groupType;
    private String title;
    private String examType;
    private String examCategory;
    private String subject;
    private String topic;
    private String language;
    private String passageText;
    private String tableDataJson;
    private String chartDataJson;
    private String chartImageUrl;
    private Integer blankCount;
    private String instructions;
    private boolean isActive;

    // No-argument constructor
    public QuestionGroupResponse() {
    }

    // All-argument constructor
    public QuestionGroupResponse(Long id, String groupType, String title, String examType,
                                 String examCategory, String subject, String topic, String language,
                                 String passageText, String tableDataJson, String chartDataJson,
                                 String chartImageUrl, Integer blankCount, String instructions,
                                 boolean isActive) {
        this.id = id;
        this.groupType = groupType;
        this.title = title;
        this.examType = examType;
        this.examCategory = examCategory;
        this.subject = subject;
        this.topic = topic;
        this.language = language;
        this.passageText = passageText;
        this.tableDataJson = tableDataJson;
        this.chartDataJson = chartDataJson;
        this.chartImageUrl = chartImageUrl;
        this.blankCount = blankCount;
        this.instructions = instructions;
        this.isActive = isActive;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getExamCategory() {
        return examCategory;
    }

    public void setExamCategory(String examCategory) {
        this.examCategory = examCategory;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPassageText() {
        return passageText;
    }

    public void setPassageText(String passageText) {
        this.passageText = passageText;
    }

    public String getTableDataJson() {
        return tableDataJson;
    }

    public void setTableDataJson(String tableDataJson) {
        this.tableDataJson = tableDataJson;
    }

    public String getChartDataJson() {
        return chartDataJson;
    }

    public void setChartDataJson(String chartDataJson) {
        this.chartDataJson = chartDataJson;
    }

    public String getChartImageUrl() {
        return chartImageUrl;
    }

    public void setChartImageUrl(String chartImageUrl) {
        this.chartImageUrl = chartImageUrl;
    }

    public Integer getBlankCount() {
        return blankCount;
    }

    public void setBlankCount(Integer blankCount) {
        this.blankCount = blankCount;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
