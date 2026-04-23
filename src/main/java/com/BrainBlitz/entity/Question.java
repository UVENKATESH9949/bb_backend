package com.BrainBlitz.entity;

import com.BrainBlitz.enums.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    // ─────────────────────────────────────────────
    // PRIMARY KEY
    // ─────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─────────────────────────────────────────────
    // CLASSIFICATION FIELDS
    // ─────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamCategory examCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamType examType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    // ─────────────────────────────────────────────
    // SUBJECT / TOPIC
    // ─────────────────────────────────────────────

    @Column(nullable = false)
    private String subject;         // e.g. Quantitative Aptitude, DSA, DBMS

    @Column(nullable = false)
    private String topic;           // e.g. Time & Work, Binary Trees, Normalization

    // ─────────────────────────────────────────────
    // QUESTION CONTENT
    // ─────────────────────────────────────────────

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;    // Supports markdown

    @Column(columnDefinition = "TEXT")
    private String questionTextHindi;  // Hindi version if language = BILINGUAL

    @Column
    private String questionImageUrl;   // For image-based questions

    // ─────────────────────────────────────────────
    // DIFFICULTY
    // ─────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficultyLevel;  // LEVEL_1 to LEVEL_10

    // ─────────────────────────────────────────────
    // MARKS
    // ─────────────────────────────────────────────

    @Column(nullable = false)
    private double marks;           // Marks for correct answer

    @Column(nullable = false)
    private double negativeMarks;   // Deducted for wrong answer (0 if no negative marking)

    @Column
    private Integer timeLimitSeconds;  // Per-question time limit (null = no limit)

    // ─────────────────────────────────────────────
    // HINT
    // ─────────────────────────────────────────────

    @Column(columnDefinition = "TEXT")
    private String hint;            // Shown when user requests hint

    @Column(columnDefinition = "TEXT")
    private String hintHindi;       // Hindi hint for bilingual questions

    // ─────────────────────────────────────────────
    // EXPLANATION (Core BrainBlitz feature)
    // ─────────────────────────────────────────────

 // Structured explanation (Layer 1 breakdown + Layer 2 dynamic methods)
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private QuestionExplanation questionExplanation;

    // ─────────────────────────────────────────────
    // GROUP REFERENCE (for RC, DI, cloze questions)
    // ─────────────────────────────────────────────

    // If question belongs to a group (RC passage, DI set)
    // groupId is set — null for standalone questions
    @Column(name = "group_id")
    private Long groupId;

    @Column
    private Integer questionOrderInGroup;  // 1st, 2nd, 3rd question in the group

    // ─────────────────────────────────────────────
    // FLAGS
    // ─────────────────────────────────────────────

    @Column(nullable = false)
    private boolean isActive = true;        // false = hidden from mocks

    @Column(nullable = false)
    private boolean isAiGenerated = false;  // true = AI generated, false = manually added

    @Column(nullable = false)
    private int timesUsedInMock = 0;    // how many times this question appeared in any mock

    @Column
    private LocalDateTime lastUsedAt;   // when this question was last used in a mock
                                        // null = never used in any mock
    
    // ─────────────────────────────────────────────
    // CHILD RELATIONSHIPS
    // ─────────────────────────────────────────────

    // MCQ options (for MCQ, multi_correct, T/F, synonym, error spotting)
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<McqOption> options;

    // Fill blank answers
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FillBlankAnswer> fillBlankAnswers;

    // Arrangement question (sentence_arrangement, para_jumble)
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArrangementQuestion arrangementQuestion;

    // Image question (all 7 image types)
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private ImageQuestion imageQuestion;

    // Coding question (code_write, code_debug, code_fill, code_output, sql_output)
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private CodingQuestion codingQuestion;

    // Code snippet question (code_output, code_debug MCQ, code_fill, sql_output, flowchart)
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private CodeSnippetQuestion codeSnippetQuestion;

    // Writing question (email_write, essay_write, speech_round, listening_comp)
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private WritingQuestion writingQuestion;

    // ─────────────────────────────────────────────
    // AUDIT FIELDS
    // ─────────────────────────────────────────────

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String createdBy;   // Admin email who created this question

    @Column
    private String updatedBy;   // Admin email who last updated

    
    public Question(Long id, QuestionType questionType, ExamCategory examCategory, ExamType examType, Language language,
			String subject, String topic, String questionText, String questionTextHindi, String questionImageUrl,
			DifficultyLevel difficultyLevel, double marks, double negativeMarks, Integer timeLimitSeconds, String hint,
			String hintHindi,QuestionExplanation questionExplanation,
			String approachExplanationHindi, Long groupId, Integer questionOrderInGroup, boolean isActive,
			boolean isAiGenerated, List<McqOption> options, List<FillBlankAnswer> fillBlankAnswers,
			ArrangementQuestion arrangementQuestion, ImageQuestion imageQuestion, CodingQuestion codingQuestion,
			CodeSnippetQuestion codeSnippetQuestion, WritingQuestion writingQuestion, LocalDateTime createdAt,
			LocalDateTime updatedAt, String createdBy, String updatedBy) {
		super();
		this.id = id;
		this.questionType = questionType;
		this.examCategory = examCategory;
		this.examType = examType;
		this.language = language;
		this.subject = subject;
		this.topic = topic;
		this.questionText = questionText;
		this.questionTextHindi = questionTextHindi;
		this.questionImageUrl = questionImageUrl;
		this.difficultyLevel = difficultyLevel;
		this.marks = marks;
		this.negativeMarks = negativeMarks;
		this.timeLimitSeconds = timeLimitSeconds;
		this.hint = hint;
		this.hintHindi = hintHindi;
		this.groupId = groupId;
		this.questionOrderInGroup = questionOrderInGroup;
		this.isActive = isActive;
		this.isAiGenerated = isAiGenerated;
		this.options = options;
		this.fillBlankAnswers = fillBlankAnswers;
		this.arrangementQuestion = arrangementQuestion;
		this.imageQuestion = imageQuestion;
		this.codingQuestion = codingQuestion;
		this.codeSnippetQuestion = codeSnippetQuestion;
		this.writingQuestion = writingQuestion;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.questionExplanation = questionExplanation;
	}

    
	public Question() {
		super();
	}


	// ─────────────────────────────────────────────
    // LIFECYCLE HOOKS
    // ─────────────────────────────────────────────

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.isAiGenerated = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
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

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getQuestionTextHindi() {
		return questionTextHindi;
	}

	public void setQuestionTextHindi(String questionTextHindi) {
		this.questionTextHindi = questionTextHindi;
	}

	public String getQuestionImageUrl() {
		return questionImageUrl;
	}

	public void setQuestionImageUrl(String questionImageUrl) {
		this.questionImageUrl = questionImageUrl;
	}

	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public double getMarks() {
		return marks;
	}

	public void setMarks(double marks) {
		this.marks = marks;
	}

	public double getNegativeMarks() {
		return negativeMarks;
	}

	public void setNegativeMarks(double negativeMarks) {
		this.negativeMarks = negativeMarks;
	}

	public Integer getTimeLimitSeconds() {
		return timeLimitSeconds;
	}

	public void setTimeLimitSeconds(Integer timeLimitSeconds) {
		this.timeLimitSeconds = timeLimitSeconds;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getHintHindi() {
		return hintHindi;
	}

	public void setHintHindi(String hintHindi) {
		this.hintHindi = hintHindi;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getQuestionOrderInGroup() {
		return questionOrderInGroup;
	}

	public void setQuestionOrderInGroup(Integer questionOrderInGroup) {
		this.questionOrderInGroup = questionOrderInGroup;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isAiGenerated() {
		return isAiGenerated;
	}

	public void setAiGenerated(boolean isAiGenerated) {
		this.isAiGenerated = isAiGenerated;
	}

	public List<McqOption> getOptions() {
		return options;
	}

	public void setOptions(List<McqOption> options) {
		this.options = options;
	}

	public List<FillBlankAnswer> getFillBlankAnswers() {
		return fillBlankAnswers;
	}

	public void setFillBlankAnswers(List<FillBlankAnswer> fillBlankAnswers) {
		this.fillBlankAnswers = fillBlankAnswers;
	}

	public ArrangementQuestion getArrangementQuestion() {
		return arrangementQuestion;
	}

	public void setArrangementQuestion(ArrangementQuestion arrangementQuestion) {
		this.arrangementQuestion = arrangementQuestion;
	}

	public ImageQuestion getImageQuestion() {
		return imageQuestion;
	}

	public void setImageQuestion(ImageQuestion imageQuestion) {
		this.imageQuestion = imageQuestion;
	}

	public CodingQuestion getCodingQuestion() {
		return codingQuestion;
	}

	public void setCodingQuestion(CodingQuestion codingQuestion) {
		this.codingQuestion = codingQuestion;
	}

	public CodeSnippetQuestion getCodeSnippetQuestion() {
		return codeSnippetQuestion;
	}

	public void setCodeSnippetQuestion(CodeSnippetQuestion codeSnippetQuestion) {
		this.codeSnippetQuestion = codeSnippetQuestion;
	}

	public WritingQuestion getWritingQuestion() {
		return writingQuestion;
	}

	public void setWritingQuestion(WritingQuestion writingQuestion) {
		this.writingQuestion = writingQuestion;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public int getTimesUsedInMock() {
		return timesUsedInMock;
	}


	public void setTimesUsedInMock(int timesUsedInMock) {
		this.timesUsedInMock = timesUsedInMock;
	}


	public LocalDateTime getLastUsedAt() {
		return lastUsedAt;
	}


	public void setLastUsedAt(LocalDateTime lastUsedAt) {
		this.lastUsedAt = lastUsedAt;
	}


	public QuestionExplanation getQuestionExplanation() {
		return questionExplanation;
	}


	public void setQuestionExplanation(QuestionExplanation questionExplanation) {
		this.questionExplanation = questionExplanation;
	}
    
	
    
}