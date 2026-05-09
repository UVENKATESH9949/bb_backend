// src/main/java/com/BrainBlitz/dto/request/QuestionGroupRequest.java

package com.BrainBlitz.dto.request;

import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.NotNull;

public class QuestionGroupRequest {

    @NotNull(message = "Group type is required")
    private GroupType groupType;   // RC, CLOZE, TABLE_DI, BAR_DI etc.

    private ExamCategory examCategory;

    @NotNull(message = "Exam type is required")
    private ExamType examType;

    private String title;

    // Passage (RC / Cloze)
    private String passageText;
    private String passageTextHindi;

    // DI Data
    private String tableDataJson;
    private String chartDataJson;
    private String chartImageUrl;

    // Cloze Test
    private Integer blankCount;

    // Instructions
    private String instructions;
    private String instructionsHindi;

    // Metadata
    private Language language;
    private String subject;
    private String topic;

    // Default Constructor
    public QuestionGroupRequest() {}

    // Full Constructor
    public QuestionGroupRequest(
            GroupType groupType,
            ExamCategory examCategory,
            ExamType examType,
            String title,
            String passageText,
            String passageTextHindi,
            String tableDataJson,
            String chartDataJson,
            String chartImageUrl,
            Integer blankCount,
            String instructions,
            String instructionsHindi,
            Language language,
            String subject,
            String topic
    ) {
        this.groupType = groupType;
        this.examCategory = examCategory;
        this.examType = examType;
        this.title = title;
        this.passageText = passageText;
        this.passageTextHindi = passageTextHindi;
        this.tableDataJson = tableDataJson;
        this.chartDataJson = chartDataJson;
        this.chartImageUrl = chartImageUrl;
        this.blankCount = blankCount;
        this.instructions = instructions;
        this.instructionsHindi = instructionsHindi;
        this.language = language;
        this.subject = subject;
        this.topic = topic;
    }

    // Getters & Setters

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public ExamCategory getExamCategory() {
        return examCategory;
    }

    public void setExamCategory(ExamCategory examCategory) {
        this.examCategory = examCategory;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassageText() {
        return passageText;
    }

    public void setPassageText(String passageText) {
        this.passageText = passageText;
    }

    public String getPassageTextHindi() {
        return passageTextHindi;
    }

    public void setPassageTextHindi(String passageTextHindi) {
        this.passageTextHindi = passageTextHindi;
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

    public String getInstructionsHindi() {
        return instructionsHindi;
    }

    public void setInstructionsHindi(String instructionsHindi) {
        this.instructionsHindi = instructionsHindi;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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
}