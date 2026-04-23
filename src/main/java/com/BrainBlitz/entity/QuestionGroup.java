package com.BrainBlitz.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.BrainBlitz.enums.GroupType;
import com.BrainBlitz.enums.Language;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "question_groups")
public class QuestionGroup {

    // ─────────────────────────────────────────────
    // PRIMARY KEY
    // ─────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─────────────────────────────────────────────
    // GROUP TYPE
    // ─────────────────────────────────────────────

    // Tells frontend how to render this group
    // READING_COMPREHENSION → passage on left, questions on right
    // CLOZE_TEST            → passage with ___ blanks + MCQ options
    // TABLE_DI              → render table from JSON, questions below
    // BAR_CHART_DI          → render Chart.js bar chart, questions below
    // PIE_CHART_DI          → render Chart.js pie chart, questions below
    // LINE_GRAPH_DI         → render Chart.js line graph, questions below
    // CASELET_DI            → text passage with numbers, questions below
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupType groupType;

    // ─────────────────────────────────────────────
    // GROUP METADATA
    // ─────────────────────────────────────────────

    @Column(nullable = false)
    private String title;           // e.g. "Passage: The Amazon Rainforest"
                                    //       "Table: Production Data 2020-2024"

    @Column(nullable = false)
    private String examType;        // SSC, RRB, TCS etc.

    @Column(nullable = false)
    private String subject;         // English, Data Interpretation etc.

    @Column(nullable = false)
    private String topic;           // Reading Comprehension, Table DI etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @Column(nullable = false)
    private boolean isActive = true;

    // ─────────────────────────────────────────────
    // PASSAGE CONTENT
    // ─────────────────────────────────────────────

    // Used for: READING_COMPREHENSION, CLOZE_TEST, CASELET_DI
    // null for chart/table DI types
    @Column(columnDefinition = "TEXT")
    private String passageText;

    @Column(columnDefinition = "TEXT")
    private String passageTextHindi;  // Hindi version for bilingual

    // ─────────────────────────────────────────────
    // TABLE DI DATA
    // ─────────────────────────────────────────────

    // Used for: TABLE_DI only
    // JSON structure for rendering a table
    // Example:
    // {
    //   "headers": ["Year", "Production", "Export", "Import"],
    //   "rows": [
    //     ["2020", "1200", "400", "200"],
    //     ["2021", "1350", "450", "180"],
    //     ["2022", "1500", "500", "220"]
    //   ],
    //   "unit": "in thousand tonnes",
    //   "caption": "Production data of wheat in India"
    // }
    @Column(columnDefinition = "TEXT")
    private String tableDataJson;

    // ─────────────────────────────────────────────
    // CHART DI DATA
    // ─────────────────────────────────────────────

    // Used for: BAR_CHART_DI, PIE_CHART_DI, LINE_GRAPH_DI
    // Chart.js compatible JSON data
    //
    // BAR / LINE example:
    // {
    //   "labels": ["2020", "2021", "2022", "2023"],
    //   "datasets": [
    //     {
    //       "label": "Sales",
    //       "data": [400, 500, 600, 700]
    //     },
    //     {
    //       "label": "Profit",
    //       "data": [100, 150, 200, 250]
    //     }
    //   ],
    //   "xAxisLabel": "Year",
    //   "yAxisLabel": "Amount (in Crores)",
    //   "title": "Company Sales and Profit 2020-2023"
    // }
    //
    // PIE example:
    // {
    //   "labels": ["Coal", "Oil", "Gas", "Solar", "Wind"],
    //   "data": [35, 28, 20, 10, 7],
    //   "title": "Energy Production by Source 2023"
    // }
    @Column(columnDefinition = "TEXT")
    private String chartDataJson;

    // Pre-rendered chart image URL (optional)
    // If admin wants to upload a static chart image
    // instead of rendering dynamically via Chart.js
    @Column
    private String chartImageUrl;

    // ─────────────────────────────────────────────
    // CLOZE TEST SPECIFIC
    // ─────────────────────────────────────────────

    // For CLOZE_TEST — passage uses ___ as blank markers
    // Each blank corresponds to one MCQ question in the group
    // blankCount tells how many blanks are in the passage
    @Column
    private Integer blankCount;

    // ─────────────────────────────────────────────
    // INSTRUCTIONS
    // ─────────────────────────────────────────────

    // Directions shown above the group
    // e.g. "Read the following passage and answer questions 1-5"
    //      "Study the table below and answer the questions"
    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(columnDefinition = "TEXT")
    private String instructionsHindi;

    // ─────────────────────────────────────────────
    // CHILD QUESTIONS
    // ─────────────────────────────────────────────

    // All questions belonging to this group
    // Linked via groupId field on Question entity
    // Ordered by questionOrderInGroup
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id",
                insertable = false, updatable = false)
    @OrderBy("questionOrderInGroup ASC")
    private List<Question> questions;

    // ─────────────────────────────────────────────
    // AUDIT
    // ─────────────────────────────────────────────

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

	public QuestionGroup(Long id, GroupType groupType, String title, String examType, String subject, String topic,
			Language language, boolean isActive, String passageText, String passageTextHindi, String tableDataJson,
			String chartDataJson, String chartImageUrl, Integer blankCount, String instructions,
			String instructionsHindi, List<Question> questions, LocalDateTime createdAt, LocalDateTime updatedAt,
			String createdBy) {
		super();
		this.id = id;
		this.groupType = groupType;
		this.title = title;
		this.examType = examType;
		this.subject = subject;
		this.topic = topic;
		this.language = language;
		this.isActive = isActive;
		this.passageText = passageText;
		this.passageTextHindi = passageTextHindi;
		this.tableDataJson = tableDataJson;
		this.chartDataJson = chartDataJson;
		this.chartImageUrl = chartImageUrl;
		this.blankCount = blankCount;
		this.instructions = instructions;
		this.instructionsHindi = instructionsHindi;
		this.questions = questions;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdBy = createdBy;
	}

	public QuestionGroup() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupType groupType) {
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

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
    
    
    
}