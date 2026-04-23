// src/main/java/com/BrainBlitz/dto/request/QuestionGroupRequest.java

package com.BrainBlitz.dto.request;

import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

public class QuestionGroupRequest {

    @NotNull(message = "Group type is required")
    private GroupType groupType;            // RC, CLOZE, TABLE_DI, BAR_DI etc.

    @NotNull(message = "Exam category is required")
    private ExamCategory examCategory;

    @NotNull(message = "Exam type is required")
    private ExamType examType;

    private String title;                   // optional heading for the group

    // For RC and cloze_test
    private String passageText;

    // For TABLE_DI — JSON table data
    // For BAR_DI, PIE_DI, LINE_DI — Chart.js compatible JSON
    // e.g. {"labels":["Jan","Feb"],"datasets":[{"data":[10,20]}]}
    private String diDataJson;

    // For chart-based DI — image of the chart (alternative to JSON)
    private String diImageUrl;

	public QuestionGroupRequest(@NotNull(message = "Group type is required") GroupType groupType,
			@NotNull(message = "Exam category is required") ExamCategory examCategory,
			@NotNull(message = "Exam type is required") ExamType examType, String title, String passageText,
			String diDataJson, String diImageUrl) {
		super();
		this.groupType = groupType;
		this.examCategory = examCategory;
		this.examType = examType;
		this.title = title;
		this.passageText = passageText;
		this.diDataJson = diDataJson;
		this.diImageUrl = diImageUrl;
	}

	public QuestionGroupRequest() {
		super();
	}

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

	public String getDiDataJson() {
		return diDataJson;
	}

	public void setDiDataJson(String diDataJson) {
		this.diDataJson = diDataJson;
	}

	public String getDiImageUrl() {
		return diImageUrl;
	}

	public void setDiImageUrl(String diImageUrl) {
		this.diImageUrl = diImageUrl;
	}
    
    
}