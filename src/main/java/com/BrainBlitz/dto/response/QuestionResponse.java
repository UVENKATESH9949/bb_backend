// src/main/java/com/BrainBlitz/dto/response/QuestionResponse.java

package com.BrainBlitz.dto.response;

import com.BrainBlitz.entity.QuestionExplanation;
import com.BrainBlitz.enums.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

public class QuestionResponse {

    // ── Base question fields ──────────────────────────────────────────
    private Long id;
    private String questionText;
    private QuestionType questionType;
    private ExamCategory examCategory;
    private ExamType examType;
    private String subject;
    private String topic;
    private DifficultyLevel difficultyLevel;
    private Language language;
    private Double marks;
    private Double negativeMarks;
    private String hint;
    private QuestionExplanation questionExplanation;
    private Boolean isActive;
    private Boolean isAiGenerated;
    private int timesUsedInMock;
    private LocalDateTime lastUsedAt;
    private Long groupId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ── MCQ options ───────────────────────────────────────────────────
    // Populated for: mcq, multi_correct, true_false,
    //                synonym_antonym, error_spotting
    private List<McqOptionResponse> options;

    // ── Fill blank answers ────────────────────────────────────────────
    // Populated for: fill_blank, text
    private List<FillBlankAnswerResponse> blanks;

    // ── Arrangement ───────────────────────────────────────────────────
    // Populated for: sentence_arrangement, para_jumble
    private ArrangementResponse arrangement;

    // ── Image question ────────────────────────────────────────────────
    // Populated for: all 7 image types
    private ImageQuestionResponse imageQuestion;

    // ── Code snippet ──────────────────────────────────────────────────
    // Populated for: code_output, code_fill, sql_output,
    //                code_debug (MCQ), flowchart_mcq
    private CodeSnippetResponse codeSnippet;

    // ── Coding question ───────────────────────────────────────────────
    // Populated for: code_write, code_debug (fix mode)
    private CodingQuestionResponse codingQuestion;

    // ── Writing question ──────────────────────────────────────────────
    // Populated for: email_write, essay_write,
    //                speech_round, listening_comp
    private WritingQuestionResponse writingQuestion;

    // ── Group info ────────────────────────────────────────────────────
    // Populated when question belongs to RC/DI group
    private QuestionGroupResponse group;

    public QuestionResponse(Long id, String questionText, QuestionType questionType, ExamCategory examCategory,
			ExamType examType, String subject, String topic, DifficultyLevel difficultyLevel, Language language,
			Double marks, Double negativeMarks, String hint, QuestionExplanation questionExplanation,
			Boolean isActive, Boolean isAiGenerated, Long groupId, LocalDateTime createdAt, LocalDateTime updatedAt,
			List<McqOptionResponse> options, List<FillBlankAnswerResponse> blanks, ArrangementResponse arrangement,
			ImageQuestionResponse imageQuestion, CodeSnippetResponse codeSnippet, CodingQuestionResponse codingQuestion,
			WritingQuestionResponse writingQuestion, QuestionGroupResponse group) {
		super();
		this.id = id;
		this.questionText = questionText;
		this.questionType = questionType;
		this.examCategory = examCategory;
		this.examType = examType;
		this.subject = subject;
		this.topic = topic;
		this.difficultyLevel = difficultyLevel;
		this.language = language;
		this.marks = marks;
		this.negativeMarks = negativeMarks;
		this.hint = hint;
		this.questionExplanation = questionExplanation;
		this.isActive = isActive;
		this.isAiGenerated = isAiGenerated;
		this.groupId = groupId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.options = options;
		this.blanks = blanks;
		this.arrangement = arrangement;
		this.imageQuestion = imageQuestion;
		this.codeSnippet = codeSnippet;
		this.codingQuestion = codingQuestion;
		this.writingQuestion = writingQuestion;
		this.group = group;
	}

    
	public QuestionResponse() {
		super();
	}
	
	

	// ═════════════════════════════════════════════════════════════════
    // Nested response classes — all inside QuestionResponse
    // ═════════════════════════════════════════════════════════════════

    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getQuestionText() {
		return questionText;
	}


	public void setQuestionText(String questionText) {
		this.questionText = questionText;
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


	public DifficultyLevel getDifficultyLevel() {
		return difficultyLevel;
	}


	public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}


	public Language getLanguage() {
		return language;
	}


	public void setLanguage(Language language) {
		this.language = language;
	}


	public Double getMarks() {
		return marks;
	}


	public void setMarks(Double marks) {
		this.marks = marks;
	}


	public Double getNegativeMarks() {
		return negativeMarks;
	}


	public void setNegativeMarks(Double negativeMarks) {
		this.negativeMarks = negativeMarks;
	}


	public String getHint() {
		return hint;
	}


	public void setHint(String hint) {
		this.hint = hint;
	}
	

	public QuestionExplanation getQuestionExplanation() {
		return questionExplanation;
	}


	public void setQuestionExplanation(QuestionExplanation questionExplanation) {
		this.questionExplanation = questionExplanation;
	}


	public Boolean getIsActive() {
		return isActive;
	}


	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}


	public Boolean getIsAiGenerated() {
		return isAiGenerated;
	}


	public void setIsAiGenerated(Boolean isAiGenerated) {
		this.isAiGenerated = isAiGenerated;
	}


	public Long getGroupId() {
		return groupId;
	}


	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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


	public List<McqOptionResponse> getOptions() {
		return options;
	}


	public void setOptions(List<McqOptionResponse> options) {
		this.options = options;
	}


	public List<FillBlankAnswerResponse> getBlanks() {
		return blanks;
	}


	public void setBlanks(List<FillBlankAnswerResponse> blanks) {
		this.blanks = blanks;
	}


	public ArrangementResponse getArrangement() {
		return arrangement;
	}


	public void setArrangement(ArrangementResponse arrangement) {
		this.arrangement = arrangement;
	}


	public ImageQuestionResponse getImageQuestion() {
		return imageQuestion;
	}


	public void setImageQuestion(ImageQuestionResponse imageQuestion) {
		this.imageQuestion = imageQuestion;
	}


	public CodeSnippetResponse getCodeSnippet() {
		return codeSnippet;
	}


	public void setCodeSnippet(CodeSnippetResponse codeSnippet) {
		this.codeSnippet = codeSnippet;
	}


	public CodingQuestionResponse getCodingQuestion() {
		return codingQuestion;
	}


	public void setCodingQuestion(CodingQuestionResponse codingQuestion) {
		this.codingQuestion = codingQuestion;
	}


	public WritingQuestionResponse getWritingQuestion() {
		return writingQuestion;
	}


	public void setWritingQuestion(WritingQuestionResponse writingQuestion) {
		this.writingQuestion = writingQuestion;
	}


	public QuestionGroupResponse getGroup() {
		return group;
	}


	public void setGroup(QuestionGroupResponse group) {
		this.group = group;
	}
	
	public int getTimesUsedInMock() { return timesUsedInMock; }
	public void setTimesUsedInMock(int timesUsedInMock) { this.timesUsedInMock = timesUsedInMock; }

	public LocalDateTime getLastUsedAt() { return lastUsedAt; }
	public void setLastUsedAt(LocalDateTime lastUsedAt) { this.lastUsedAt = lastUsedAt; }
	
    public static class McqOptionResponse {
        private Long id;
        private String optionText;
        private String optionImageUrl;
        private Boolean isCorrect;
        private Integer optionOrder;
		public McqOptionResponse(Long id, String optionText, String optionImageUrl, Boolean isCorrect,
				Integer optionOrder) {
			super();
			this.id = id;
			this.optionText = optionText;
			this.optionImageUrl = optionImageUrl;
			this.isCorrect = isCorrect;
			this.optionOrder = optionOrder;
		}
		public McqOptionResponse() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getOptionText() {
			return optionText;
		}
		public void setOptionText(String optionText) {
			this.optionText = optionText;
		}
		public String getOptionImageUrl() {
			return optionImageUrl;
		}
		public void setOptionImageUrl(String optionImageUrl) {
			this.optionImageUrl = optionImageUrl;
		}
		public Boolean getIsCorrect() {
			return isCorrect;
		}
		public void setIsCorrect(Boolean isCorrect) {
			this.isCorrect = isCorrect;
		}
		public Integer getOptionOrder() {
			return optionOrder;
		}
		public void setOptionOrder(Integer optionOrder) {
			this.optionOrder = optionOrder;
		}
        
        
    }

    
    public static class FillBlankAnswerResponse {
        private Long id;
        private Integer blankPosition;
        private String correctAnswer;
        private List<String> alternateAnswers;
        private Boolean caseSensitive;
		public FillBlankAnswerResponse(Long id, Integer blankPosition, String correctAnswer,
				List<String> alternateAnswers, Boolean caseSensitive) {
			super();
			this.id = id;
			this.blankPosition = blankPosition;
			this.correctAnswer = correctAnswer;
			this.alternateAnswers = alternateAnswers;
			this.caseSensitive = caseSensitive;
		}
		public FillBlankAnswerResponse() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Integer getBlankPosition() {
			return blankPosition;
		}
		public void setBlankPosition(Integer blankPosition) {
			this.blankPosition = blankPosition;
		}
		public String getCorrectAnswer() {
			return correctAnswer;
		}
		public void setCorrectAnswer(String correctAnswer) {
			this.correctAnswer = correctAnswer;
		}
		public List<String> getAlternateAnswers() {
			return alternateAnswers;
		}
		public void setAlternateAnswers(List<String> alternateAnswers) {
			this.alternateAnswers = alternateAnswers;
		}
		public Boolean getCaseSensitive() {
			return caseSensitive;
		}
		public void setCaseSensitive(Boolean caseSensitive) {
			this.caseSensitive = caseSensitive;
		}
        
        
    }
    
    public static class ArrangementResponse {
        private Long id;
        private ArrangementType arrangementType;
        private List<String> segments;
        private String correctOrder;
		public ArrangementResponse(Long id, ArrangementType arrangementType, List<String> segments,
				String correctOrder) {
			super();
			this.id = id;
			this.arrangementType = arrangementType;
			this.segments = segments;
			this.correctOrder = correctOrder;
		}
		public ArrangementResponse() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public ArrangementType getArrangementType() {
			return arrangementType;
		}
		public void setArrangementType(ArrangementType arrangementType) {
			this.arrangementType = arrangementType;
		}
		public List<String> getSegments() {
			return segments;
		}
		public void setSegments(List<String> segments) {
			this.segments = segments;
		}
		public String getCorrectOrder() {
			return correctOrder;
		}
		public void setCorrectOrder(String correctOrder) {
			this.correctOrder = correctOrder;
		}
        
        
    }

    
    public static class ImageQuestionResponse {
        private Long id;
        private ImageQuestionType imageQuestionType;
        private String mainImageUrl;
        private List<String> supportingImageUrls;
        private List<McqOptionResponse> options;
        private String optionImagesJson;
        private Integer correctOptionIndex;
        
		public ImageQuestionResponse(Long id, ImageQuestionType imageQuestionType, String mainImageUrl,
				List<String> supportingImageUrls, List<McqOptionResponse> options) {
			super();
			this.id = id;
			this.imageQuestionType = imageQuestionType;
			this.mainImageUrl = mainImageUrl;
			this.supportingImageUrls = supportingImageUrls;
			this.options = options;
		}
		public ImageQuestionResponse() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public ImageQuestionType getImageQuestionType() {
			return imageQuestionType;
		}
		public void setImageQuestionType(ImageQuestionType imageQuestionType) {
			this.imageQuestionType = imageQuestionType;
		}
		public String getMainImageUrl() {
			return mainImageUrl;
		}
		public void setMainImageUrl(String mainImageUrl) {
			this.mainImageUrl = mainImageUrl;
		}
		public List<String> getSupportingImageUrls() {
			return supportingImageUrls;
		}
		public void setSupportingImageUrls(List<String> supportingImageUrls) {
			this.supportingImageUrls = supportingImageUrls;
		}
		public List<McqOptionResponse> getOptions() {
			return options;
		}
		public void setOptions(List<McqOptionResponse> options) {
			this.options = options;
		}
		public String getOptionImagesJson() {
	        return optionImagesJson;
	    }
	    public void setOptionImagesJson(String optionImagesJson) {
	        this.optionImagesJson = optionImagesJson;
	    }
	    public Integer getCorrectOptionIndex() {
	        return correctOptionIndex;
	    }
	    public void setCorrectOptionIndex(Integer correctOptionIndex) {
	        this.correctOptionIndex = correctOptionIndex;
	    }
        
    }

    
    public static class CodeSnippetResponse {
        private Long id;
        private String codeSnippet;
        private ProgrammingLanguage programmingLanguage;
        private String sqlTableDataJson;
        private String sqlQuery;
        private Integer blankLineNumber;
        private List<String> acceptedAnswers;
        private String flowchartImageUrl;
        private List<McqOptionResponse> options;
		public CodeSnippetResponse(Long id, String codeSnippet, ProgrammingLanguage programmingLanguage,
				String sqlTableDataJson, String sqlQuery, Integer blankLineNumber, List<String> acceptedAnswers,
				String flowchartImageUrl, List<McqOptionResponse> options) {
			super();
			this.id = id;
			this.codeSnippet = codeSnippet;
			this.programmingLanguage = programmingLanguage;
			this.sqlTableDataJson = sqlTableDataJson;
			this.sqlQuery = sqlQuery;
			this.blankLineNumber = blankLineNumber;
			this.acceptedAnswers = acceptedAnswers;
			this.flowchartImageUrl = flowchartImageUrl;
			this.options = options;
		}
		public CodeSnippetResponse() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getCodeSnippet() {
			return codeSnippet;
		}
		public void setCodeSnippet(String codeSnippet) {
			this.codeSnippet = codeSnippet;
		}
		public ProgrammingLanguage getProgrammingLanguage() {
			return programmingLanguage;
		}
		public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
			this.programmingLanguage = programmingLanguage;
		}
		public String getSqlTableDataJson() {
			return sqlTableDataJson;
		}
		public void setSqlTableDataJson(String sqlTableDataJson) {
			this.sqlTableDataJson = sqlTableDataJson;
		}
		public String getSqlQuery() {
			return sqlQuery;
		}
		public void setSqlQuery(String sqlQuery) {
			this.sqlQuery = sqlQuery;
		}
		public Integer getBlankLineNumber() {
			return blankLineNumber;
		}
		public void setBlankLineNumber(Integer blankLineNumber) {
			this.blankLineNumber = blankLineNumber;
		}
		public List<String> getAcceptedAnswers() {
			return acceptedAnswers;
		}
		public void setAcceptedAnswers(List<String> acceptedAnswers) {
			this.acceptedAnswers = acceptedAnswers;
		}
		public String getFlowchartImageUrl() {
			return flowchartImageUrl;
		}
		public void setFlowchartImageUrl(String flowchartImageUrl) {
			this.flowchartImageUrl = flowchartImageUrl;
		}
		public List<McqOptionResponse> getOptions() {
			return options;
		}
		public void setOptions(List<McqOptionResponse> options) {
			this.options = options;
		}
        
        
    }

    
    public static class CodingQuestionResponse {
        private Long id;
        private String problemStatement;
        private String inputFormat;
        private String outputFormat;
        private String constraints;
        private String realWorldContext;
        private String starterCodeJson;
        private String solutionCodeJson;
        private String driverCodeJson;
        private List<ProgrammingLanguage> supportedLanguages;
        private String expectedTimeComplexity;
        private String expectedSpaceComplexity;
        private String approachesJson;
        private List<String> hintsJson;
        private List<String> prerequisitesJson;
        private String commonMistakesJson;
        private List<String> companyTagsJson;
        private String buggyCodeJson;
        private String bugDescription;
        private List<TestCaseResponse> testCases;
        private List<SolutionStepResponse> solutionSteps;

        public CodingQuestionResponse() {}

        public CodingQuestionResponse(Long id, String problemStatement, String inputFormat,
                String outputFormat, String constraints, String realWorldContext,
                String starterCodeJson, String solutionCodeJson, String driverCodeJson,
                List<ProgrammingLanguage> supportedLanguages, String expectedTimeComplexity,
                String expectedSpaceComplexity, String approachesJson, List<String> hintsJson,
                List<String> prerequisitesJson, String commonMistakesJson,
                List<String> companyTagsJson, String buggyCodeJson, String bugDescription,
                List<TestCaseResponse> testCases, List<SolutionStepResponse> solutionSteps) {
            this.id = id;
            this.problemStatement = problemStatement;
            this.inputFormat = inputFormat;
            this.outputFormat = outputFormat;
            this.constraints = constraints;
            this.realWorldContext = realWorldContext;
            this.starterCodeJson = starterCodeJson;
            this.solutionCodeJson = solutionCodeJson;
            this.driverCodeJson = driverCodeJson;
            this.supportedLanguages = supportedLanguages;
            this.expectedTimeComplexity = expectedTimeComplexity;
            this.expectedSpaceComplexity = expectedSpaceComplexity;
            this.approachesJson = approachesJson;
            this.hintsJson = hintsJson;
            this.prerequisitesJson = prerequisitesJson;
            this.commonMistakesJson = commonMistakesJson;
            this.companyTagsJson = companyTagsJson;
            this.buggyCodeJson = buggyCodeJson;
            this.bugDescription = bugDescription;
            this.testCases = testCases;
            this.solutionSteps = solutionSteps;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getProblemStatement() { return problemStatement; }
        public void setProblemStatement(String problemStatement) { this.problemStatement = problemStatement; }
        public String getInputFormat() { return inputFormat; }
        public void setInputFormat(String inputFormat) { this.inputFormat = inputFormat; }
        public String getOutputFormat() { return outputFormat; }
        public void setOutputFormat(String outputFormat) { this.outputFormat = outputFormat; }
        public String getConstraints() { return constraints; }
        public void setConstraints(String constraints) { this.constraints = constraints; }
        public String getRealWorldContext() { return realWorldContext; }
        public void setRealWorldContext(String realWorldContext) { this.realWorldContext = realWorldContext; }
        public String getStarterCodeJson() { return starterCodeJson; }
        public void setStarterCodeJson(String starterCodeJson) { this.starterCodeJson = starterCodeJson; }
        public String getSolutionCodeJson() { return solutionCodeJson; }
        public void setSolutionCodeJson(String solutionCodeJson) { this.solutionCodeJson = solutionCodeJson; }
        public String getDriverCodeJson() { return driverCodeJson; }
        public void setDriverCodeJson(String driverCodeJson) { this.driverCodeJson = driverCodeJson; }
        public List<ProgrammingLanguage> getSupportedLanguages() { return supportedLanguages; }
        public void setSupportedLanguages(List<ProgrammingLanguage> supportedLanguages) { this.supportedLanguages = supportedLanguages; }
        public String getExpectedTimeComplexity() { return expectedTimeComplexity; }
        public void setExpectedTimeComplexity(String expectedTimeComplexity) { this.expectedTimeComplexity = expectedTimeComplexity; }
        public String getExpectedSpaceComplexity() { return expectedSpaceComplexity; }
        public void setExpectedSpaceComplexity(String expectedSpaceComplexity) { this.expectedSpaceComplexity = expectedSpaceComplexity; }
        public String getApproachesJson() { return approachesJson; }
        public void setApproachesJson(String approachesJson) { this.approachesJson = approachesJson; }
        public List<String> getHintsJson() { return hintsJson; }
        public void setHintsJson(List<String> hintsJson) { this.hintsJson = hintsJson; }
        public List<String> getPrerequisitesJson() { return prerequisitesJson; }
        public void setPrerequisitesJson(List<String> prerequisitesJson) { this.prerequisitesJson = prerequisitesJson; }
        public String getCommonMistakesJson() { return commonMistakesJson; }
        public void setCommonMistakesJson(String commonMistakesJson) { this.commonMistakesJson = commonMistakesJson; }
        public List<String> getCompanyTagsJson() { return companyTagsJson; }
        public void setCompanyTagsJson(List<String> companyTagsJson) { this.companyTagsJson = companyTagsJson; }
        public String getBuggyCodeJson() { return buggyCodeJson; }
        public void setBuggyCodeJson(String buggyCodeJson) { this.buggyCodeJson = buggyCodeJson; }
        public String getBugDescription() { return bugDescription; }
        public void setBugDescription(String bugDescription) { this.bugDescription = bugDescription; }
        public List<TestCaseResponse> getTestCases() { return testCases; }
        public void setTestCases(List<TestCaseResponse> testCases) { this.testCases = testCases; }
        public List<SolutionStepResponse> getSolutionSteps() { return solutionSteps; }
        public void setSolutionSteps(List<SolutionStepResponse> solutionSteps) { this.solutionSteps = solutionSteps; }
    }

    
    public static class TestCaseResponse {
        private Long id;
        private String input;
        private String expectedOutput;
        private Boolean isSample;
        private Boolean isHidden;
        private String explanation;
        private Integer weightage;
        private Boolean isEdgeCase;
        private Integer displayOrder;
        
		public TestCaseResponse(Long id, String input, String expectedOutput, Boolean isSample, Boolean isHidden,
				String explanation, Integer weightage,Boolean isEdgeCase,Integer displayOrder) {
			super();
			this.id = id;
			this.input = input;
			this.expectedOutput = expectedOutput;
			this.isSample = isSample;
			this.isHidden = isHidden;
			this.explanation = explanation;
			this.weightage = weightage;
			this.isEdgeCase = isEdgeCase;
			this.displayOrder = displayOrder;
		}
		public TestCaseResponse() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getInput() {
			return input;
		}
		public void setInput(String input) {
			this.input = input;
		}
		public String getExpectedOutput() {
			return expectedOutput;
		}
		public void setExpectedOutput(String expectedOutput) {
			this.expectedOutput = expectedOutput;
		}
		public Boolean getIsSample() {
			return isSample;
		}
		public void setIsSample(Boolean isSample) {
			this.isSample = isSample;
		}
		public Boolean getIsHidden() {
			return isHidden;
		}
		public void setIsHidden(Boolean isHidden) {
			this.isHidden = isHidden;
		}
		public String getExplanation() {
			return explanation;
		}
		public void setExplanation(String explanation) {
			this.explanation = explanation;
		}
		public Integer getWeightage() {
			return weightage;
		}
		public void setWeightage(Integer weightage) {
			this.weightage = weightage;
		}
		public Boolean getIsEdgeCase() {
			return isEdgeCase;
		}
		public void setIsEdgeCase(Boolean isEdgeCase) {
			this.isEdgeCase = isEdgeCase;
		}
		public Integer getDisplayOrder() {
			return displayOrder;
		}
		public void setDisplayOrder(Integer displayOrder) {
			this.displayOrder = displayOrder;
		}
        
        
    }

    
    public static class SolutionStepResponse {
        private Long id;
        private Integer stepNumber;
        private String stepTitle;
        private String stepDescription;
        private String codeAtThisStep;
        private String imageUrl;
		public SolutionStepResponse(Long id, Integer stepNumber, String stepTitle, String stepDescription,
				String codeAtThisStep, String imageUrl) {
			super();
			this.id = id;
			this.stepNumber = stepNumber;
			this.stepTitle = stepTitle;
			this.stepDescription = stepDescription;
			this.codeAtThisStep = codeAtThisStep;
			this.imageUrl = imageUrl;
		}
		public SolutionStepResponse() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Integer getStepNumber() {
			return stepNumber;
		}
		public void setStepNumber(Integer stepNumber) {
			this.stepNumber = stepNumber;
		}
		public String getStepTitle() {
			return stepTitle;
		}
		public void setStepTitle(String stepTitle) {
			this.stepTitle = stepTitle;
		}
		public String getStepDescription() {
			return stepDescription;
		}
		public void setStepDescription(String stepDescription) {
			this.stepDescription = stepDescription;
		}
		public String getCodeAtThisStep() {
			return codeAtThisStep;
		}
		public void setCodeAtThisStep(String codeAtThisStep) {
			this.codeAtThisStep = codeAtThisStep;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
        
        
        
    }

    
    public static class WritingQuestionResponse {
        
    	private Long id;
        private WritingType writingType;
        private String prompt;
        private Integer minWords;
        private Integer maxWords;
        private String evaluationCriteriaJson;
        private String sampleAnswer;
        private String audioUrl;
        private Integer speechDurationSeconds;
        private GradingStatus gradingStatus;    // ✅ enum not String
        
        // Add these
        private String transcriptUrl;
        private Integer maxFileSizeMb;
        private GradingType gradingType;
        private Double maxScore;
        private String rubricJson;
		public WritingQuestionResponse(Long id, WritingType writingType, String prompt, Integer minWords,
				Integer maxWords, String evaluationCriteriaJson, String sampleAnswer, String audioUrl,
				Integer speechDurationSeconds, GradingStatus gradingStatus, String transcriptUrl, Integer maxFileSizeMb,
				GradingType gradingType, Double maxScore, String rubricJson) {
			super();
			this.id = id;
			this.writingType = writingType;
			this.prompt = prompt;
			this.minWords = minWords;
			this.maxWords = maxWords;
			this.evaluationCriteriaJson = evaluationCriteriaJson;
			this.sampleAnswer = sampleAnswer;
			this.audioUrl = audioUrl;
			this.speechDurationSeconds = speechDurationSeconds;
			this.gradingStatus = gradingStatus;
			this.transcriptUrl = transcriptUrl;
			this.maxFileSizeMb = maxFileSizeMb;
			this.gradingType = gradingType;
			this.maxScore = maxScore;
			this.rubricJson = rubricJson;
		}
		public WritingQuestionResponse() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public WritingType getWritingType() {
			return writingType;
		}
		public void setWritingType(WritingType writingType) {
			this.writingType = writingType;
		}
		public String getPrompt() {
			return prompt;
		}
		public void setPrompt(String prompt) {
			this.prompt = prompt;
		}
		public Integer getMinWords() {
			return minWords;
		}
		public void setMinWords(Integer minWords) {
			this.minWords = minWords;
		}
		public Integer getMaxWords() {
			return maxWords;
		}
		public void setMaxWords(Integer maxWords) {
			this.maxWords = maxWords;
		}
		public String getEvaluationCriteriaJson() {
			return evaluationCriteriaJson;
		}
		public void setEvaluationCriteriaJson(String evaluationCriteriaJson) {
			this.evaluationCriteriaJson = evaluationCriteriaJson;
		}
		public String getSampleAnswer() {
			return sampleAnswer;
		}
		public void setSampleAnswer(String sampleAnswer) {
			this.sampleAnswer = sampleAnswer;
		}
		public String getAudioUrl() {
			return audioUrl;
		}
		public void setAudioUrl(String audioUrl) {
			this.audioUrl = audioUrl;
		}
		public Integer getSpeechDurationSeconds() {
			return speechDurationSeconds;
		}
		public void setSpeechDurationSeconds(Integer speechDurationSeconds) {
			this.speechDurationSeconds = speechDurationSeconds;
		}
		public GradingStatus getGradingStatus() {
			return gradingStatus;
		}
		public void setGradingStatus(GradingStatus gradingStatus) {
			this.gradingStatus = gradingStatus;
		}
		public String getTranscriptUrl() {
			return transcriptUrl;
		}
		public void setTranscriptUrl(String transcriptUrl) {
			this.transcriptUrl = transcriptUrl;
		}
		public Integer getMaxFileSizeMb() {
			return maxFileSizeMb;
		}
		public void setMaxFileSizeMb(Integer maxFileSizeMb) {
			this.maxFileSizeMb = maxFileSizeMb;
		}
		public GradingType getGradingType() {
			return gradingType;
		}
		public void setGradingType(GradingType gradingType) {
			this.gradingType = gradingType;
		}
		public Double getMaxScore() {
			return maxScore;
		}
		public void setMaxScore(Double maxScore) {
			this.maxScore = maxScore;
		}
		public String getRubricJson() {
			return rubricJson;
		}
		public void setRubricJson(String rubricJson) {
			this.rubricJson = rubricJson;
		}
        
        
        
    }

    public static class QuestionGroupResponse {
        private Long id;
        private GroupType groupType;
        private String title;
        private String passageText;
        private String diDataJson;
        private String diImageUrl;
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
		// Passage (RC / Cloze)
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
		
		public QuestionGroupResponse(Long id, GroupType groupType, String title, String passageText, String diDataJson,
				String diImageUrl, String passageTextHindi, String tableDataJson, String chartDataJson,
				String chartImageUrl, Integer blankCount, String instructions, String instructionsHindi) {
			super();
			this.id = id;
			this.groupType = groupType;
			this.title = title;
			this.passageText = passageText;
			this.diDataJson = diDataJson;
			this.diImageUrl = diImageUrl;
			this.passageTextHindi = passageTextHindi;
			this.tableDataJson = tableDataJson;
			this.chartDataJson = chartDataJson;
			this.chartImageUrl = chartImageUrl;
			this.blankCount = blankCount;
			this.instructions = instructions;
			this.instructionsHindi = instructionsHindi;
		}
		public QuestionGroupResponse() {
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
}