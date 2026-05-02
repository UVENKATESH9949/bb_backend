package com.BrainBlitz.dto.response;

import java.util.List;
import java.time.LocalDateTime;

import com.BrainBlitz.enums.*;
import com.BrainBlitz.entity.*;


public class QuestionSummaryResponse {

    // ── Common fields ──────────────────────────────────────────
    private Long id;
    private String questionText;
    private String questionTextHindi;
    private String questionImageUrl;
    private QuestionType questionType;
    private ExamCategory examCategory;
    private ExamType examType;
    private String subject;
    private String topic;
    private DifficultyLevel difficultyLevel;
    private Language language;
    private Double marks;
    private Double negativeMarks;
    private Boolean isActive;
    private Boolean isAiGenerated;
    private Long groupId;
    private Integer questionOrderInGroup;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer timeLimitSeconds;
    private String hint;
    private String hintHindi;
    private QuestionExplanation explanation;

    // ── Type-specific payloads (only one will be non-null per question) ──
    private McqPayload mcqPayload;               // MCQ_SINGLE, TRUE_FALSE, SYNONYM, ERROR_SPOTTING
    private MultiCorrectPayload multiCorrectPayload; // MULTI_CORRECT
    private FillBlankPayload fillBlankPayload;   // FILL_BLANK, TEXT_ANSWER
    private ArrangementPayload arrangementPayload; // SENTENCE_ARRANGEMENT, PARA_JUMBLE
    private ImagePayload imagePayload;           // all IMAGE_* types
    private CodingPayload codingPayload;         // CODE_WRITE, CODE_DEBUG, CODE_OUTPUT, SQL_OUTPUT
    private CodeSnippetPayload codeSnippetPayload; // CODE_FILL, FLOWCHART_MCQ
    private WritingPayload writingPayload;       // EMAIL_WRITE, ESSAY_WRITE, SPEECH_ROUND, LISTENING_COMP

    // ── No-arg constructor (required for Jackson) ──────────────
    public QuestionSummaryResponse() {}

    // ──────────────────────────────────────────────────────────
    // NESTED PAYLOAD CLASSES
    // ──────────────────────────────────────────────────────────

    /**
     * MCQ single-correct, True/False, Synonym, Error Spotting
     * options and correctOptions are parallel lists — same index = same option
     */
    public static class McqPayload {
        private List<McqOptionDto> options;

        public McqPayload(List<McqOptionDto> options) {
            this.options = options;
        }

        public List<McqOptionDto> getOptions() { return options; }
        public void setOptions(List<McqOptionDto> options) { this.options = options; }
    }

    /**
     * Multi-correct — same structure as MCQ but multiple isCorrect=true
     */
    public static class MultiCorrectPayload {
        private List<McqOptionDto> options;

        public MultiCorrectPayload(List<McqOptionDto> options) {
            this.options = options;
        }

        public List<McqOptionDto> getOptions() { return options; }
        public void setOptions(List<McqOptionDto> options) { this.options = options; }
    }

    /**
     * Shared DTO for a single MCQ option
     */
    public static class McqOptionDto {
        private Long optionId;
        private int optionOrder;       // 1=A, 2=B, 3=C, 4=D
        private String optionText;
        private String optionTextHindi;
        private String optionImageUrl;
        private boolean isCorrect;
        private String optionExplanation;

        public McqOptionDto(Long optionId, int optionOrder, String optionText,
                            String optionTextHindi, String optionImageUrl,
                            boolean isCorrect, String optionExplanation) {
            this.optionId = optionId;
            this.optionOrder = optionOrder;
            this.optionText = optionText;
            this.optionTextHindi = optionTextHindi;
            this.optionImageUrl = optionImageUrl;
            this.isCorrect = isCorrect;
            this.optionExplanation = optionExplanation;
        }

        public Long getOptionId() { return optionId; }
        public int getOptionOrder() { return optionOrder; }
        public String getOptionText() { return optionText; }
        public String getOptionTextHindi() { return optionTextHindi; }
        public String getOptionImageUrl() { return optionImageUrl; }
        public boolean isCorrect() { return isCorrect; }
        public String getOptionExplanation() { return optionExplanation; }
    }

    /**
     * Fill in the blank — one entry per blank (blankPosition 1, 2, 3...)
     */
    public static class FillBlankPayload {
        private List<BlankDto> blanks;

        public FillBlankPayload(List<BlankDto> blanks) {
            this.blanks = blanks;
        }

        public List<BlankDto> getBlanks() { return blanks; }
        public void setBlanks(List<BlankDto> blanks) { this.blanks = blanks; }
    }

    public static class BlankDto {
        private int blankPosition;
        private String correctAnswer;
        private String alternateAnswers;  // JSON string as-is from DB
        private boolean caseSensitive;
        private boolean exactMatch;
        private String blankHint;
        private Integer expectedAnswerLength;

        public BlankDto(int blankPosition, String correctAnswer, String alternateAnswers,
                        boolean caseSensitive, boolean exactMatch,
                        String blankHint, Integer expectedAnswerLength) {
            this.blankPosition = blankPosition;
            this.correctAnswer = correctAnswer;
            this.alternateAnswers = alternateAnswers;
            this.caseSensitive = caseSensitive;
            this.exactMatch = exactMatch;
            this.blankHint = blankHint;
            this.expectedAnswerLength = expectedAnswerLength;
        }

        public int getBlankPosition() { return blankPosition; }
        public String getCorrectAnswer() { return correctAnswer; }
        public String getAlternateAnswers() { return alternateAnswers; }
        public boolean isCaseSensitive() { return caseSensitive; }
        public boolean isExactMatch() { return exactMatch; }
        public String getBlankHint() { return blankHint; }
        public Integer getExpectedAnswerLength() { return expectedAnswerLength; }
    }

    /**
     * Sentence arrangement / Para jumble
     */
    public static class ArrangementPayload {
        private ArrangementType arrangementType;
        private String segmentsJson;
        private String segmentsJsonHindi;
        private String correctOrder;
        private String alternateCorrectOrders;
        private String fixedOpeningSentence;
        private String fixedClosingSentence;
        private String fixedOpeningSentenceHindi;
        private String fixedClosingSentenceHindi;
        private boolean isDragDrop;

        public ArrangementPayload(ArrangementType arrangementType, String segmentsJson,
                                  String segmentsJsonHindi, String correctOrder,
                                  String alternateCorrectOrders, String fixedOpeningSentence,
                                  String fixedClosingSentence, String fixedOpeningSentenceHindi,
                                  String fixedClosingSentenceHindi, boolean isDragDrop) {
            this.arrangementType = arrangementType;
            this.segmentsJson = segmentsJson;
            this.segmentsJsonHindi = segmentsJsonHindi;
            this.correctOrder = correctOrder;
            this.alternateCorrectOrders = alternateCorrectOrders;
            this.fixedOpeningSentence = fixedOpeningSentence;
            this.fixedClosingSentence = fixedClosingSentence;
            this.fixedOpeningSentenceHindi = fixedOpeningSentenceHindi;
            this.fixedClosingSentenceHindi = fixedClosingSentenceHindi;
            this.isDragDrop = isDragDrop;
        }

        public ArrangementType getArrangementType() { return arrangementType; }
        public String getSegmentsJson() { return segmentsJson; }
        public String getSegmentsJsonHindi() { return segmentsJsonHindi; }
        public String getCorrectOrder() { return correctOrder; }
        public String getAlternateCorrectOrders() { return alternateCorrectOrders; }
        public String getFixedOpeningSentence() { return fixedOpeningSentence; }
        public String getFixedClosingSentence() { return fixedClosingSentence; }
        public String getFixedOpeningSentenceHindi() { return fixedOpeningSentenceHindi; }
        public String getFixedClosingSentenceHindi() { return fixedClosingSentenceHindi; }
        public boolean isDragDrop() { return isDragDrop; }
    }

    /**
     * All image question variants
     */
    public static class ImagePayload {
        private ImageQuestionType imageQuestionType;
        private String mainImageUrl;
        private String supportingImagesJson;
        private Integer missingCellPosition;
        private String imageDimensions;
        private String mainImageAltText;
        private String supportingImagesAltTextJson;
        private Integer correctOptionIndex;
        private String optionImagesJson;
        private String optionImagesAltTextJson;

        public ImagePayload(ImageQuestionType imageQuestionType, String mainImageUrl,
                            String supportingImagesJson, Integer missingCellPosition,
                            String imageDimensions, String mainImageAltText,
                            String supportingImagesAltTextJson, Integer correctOptionIndex,
                            String optionImagesJson, String optionImagesAltTextJson) {
            this.imageQuestionType = imageQuestionType;
            this.mainImageUrl = mainImageUrl;
            this.supportingImagesJson = supportingImagesJson;
            this.missingCellPosition = missingCellPosition;
            this.imageDimensions = imageDimensions;
            this.mainImageAltText = mainImageAltText;
            this.supportingImagesAltTextJson = supportingImagesAltTextJson;
            this.correctOptionIndex = correctOptionIndex;
            this.optionImagesJson = optionImagesJson;
            this.optionImagesAltTextJson = optionImagesAltTextJson;
        }

        public ImageQuestionType getImageQuestionType() { return imageQuestionType; }
        public String getMainImageUrl() { return mainImageUrl; }
        public String getSupportingImagesJson() { return supportingImagesJson; }
        public Integer getMissingCellPosition() { return missingCellPosition; }
        public String getImageDimensions() { return imageDimensions; }
        public String getMainImageAltText() { return mainImageAltText; }
        public String getSupportingImagesAltTextJson() { return supportingImagesAltTextJson; }
        public Integer getCorrectOptionIndex() { return correctOptionIndex; }
        public String getOptionImagesJson() { return optionImagesJson; }
        public String getOptionImagesAltTextJson() { return optionImagesAltTextJson; }
    }

    /**
     * Full coding question (code_write, code_debug, code_output, sql_output)
     */
    public static class CodingPayload {
        private String problemStatement;
        private String problemStatementHindi;
        private String inputFormat;
        private String outputFormat;
        private String constraints;
        private String starterCodeJson;
        private String supportedLanguagesJson;
        private String expectedTimeComplexity;
        private String expectedSpaceComplexity;
        private String whyThisDataStructure;
        private String whyThisApproach;
        private String alternateApproachesJson;
        private String buggyCodeJson;       // CODE_DEBUG only
        private String bugDescription;      // CODE_DEBUG only
        // Note: testCases and solutionSteps intentionally excluded from
        // session response — fetched separately when user submits

        public CodingPayload(String problemStatement, String problemStatementHindi,
                             String inputFormat, String outputFormat, String constraints,
                             String starterCodeJson, String supportedLanguagesJson,
                             String expectedTimeComplexity, String expectedSpaceComplexity,
                             String whyThisDataStructure, String whyThisApproach,
                             String alternateApproachesJson, String buggyCodeJson,
                             String bugDescription) {
            this.problemStatement = problemStatement;
            this.problemStatementHindi = problemStatementHindi;
            this.inputFormat = inputFormat;
            this.outputFormat = outputFormat;
            this.constraints = constraints;
            this.starterCodeJson = starterCodeJson;
            this.supportedLanguagesJson = supportedLanguagesJson;
            this.expectedTimeComplexity = expectedTimeComplexity;
            this.expectedSpaceComplexity = expectedSpaceComplexity;
            this.whyThisDataStructure = whyThisDataStructure;
            this.whyThisApproach = whyThisApproach;
            this.alternateApproachesJson = alternateApproachesJson;
            this.buggyCodeJson = buggyCodeJson;
            this.bugDescription = bugDescription;
        }

        public String getProblemStatement() { return problemStatement; }
        public String getProblemStatementHindi() { return problemStatementHindi; }
        public String getInputFormat() { return inputFormat; }
        public String getOutputFormat() { return outputFormat; }
        public String getConstraints() { return constraints; }
        public String getStarterCodeJson() { return starterCodeJson; }
        public String getSupportedLanguagesJson() { return supportedLanguagesJson; }
        public String getExpectedTimeComplexity() { return expectedTimeComplexity; }
        public String getExpectedSpaceComplexity() { return expectedSpaceComplexity; }
        public String getWhyThisDataStructure() { return whyThisDataStructure; }
        public String getWhyThisApproach() { return whyThisApproach; }
        public String getAlternateApproachesJson() { return alternateApproachesJson; }
        public String getBuggyCodeJson() { return buggyCodeJson; }
        public String getBugDescription() { return bugDescription; }
    }

    /**
     * Code snippet question (code_fill, flowchart_mcq, sql_output MCQ variant)
     */
    public static class CodeSnippetPayload {
        private String codeSnippet;
        private ProgrammingLanguage programmingLanguage;
        private String sqlTableDataJson;
        private String sqlQuery;
        private Integer blankLineNumber;
        private String acceptedAnswersJson;
        private String flowchartImageUrl;

        public CodeSnippetPayload(String codeSnippet, ProgrammingLanguage programmingLanguage,
                                  String sqlTableDataJson, String sqlQuery,
                                  Integer blankLineNumber, String acceptedAnswersJson,
                                  String flowchartImageUrl) {
            this.codeSnippet = codeSnippet;
            this.programmingLanguage = programmingLanguage;
            this.sqlTableDataJson = sqlTableDataJson;
            this.sqlQuery = sqlQuery;
            this.blankLineNumber = blankLineNumber;
            this.acceptedAnswersJson = acceptedAnswersJson;
            this.flowchartImageUrl = flowchartImageUrl;
        }

        public String getCodeSnippet() { return codeSnippet; }
        public ProgrammingLanguage getProgrammingLanguage() { return programmingLanguage; }
        public String getSqlTableDataJson() { return sqlTableDataJson; }
        public String getSqlQuery() { return sqlQuery; }
        public Integer getBlankLineNumber() { return blankLineNumber; }
        public String getAcceptedAnswersJson() { return acceptedAnswersJson; }
        public String getFlowchartImageUrl() { return flowchartImageUrl; }
    }

    /**
     * Writing / listening question
     */
    public static class WritingPayload {
        private WritingType writingType;
        private String prompt;
        private Integer minWords;
        private Integer maxWords;
        private String evaluationCriteriaJson;
        private String sampleAnswer;
        private String audioUrl;
        private Integer speechDurationSeconds;

        public WritingPayload(WritingType writingType, String prompt,
                              Integer minWords, Integer maxWords,
                              String evaluationCriteriaJson, String sampleAnswer,
                              String audioUrl, Integer speechDurationSeconds) {
            this.writingType = writingType;
            this.prompt = prompt;
            this.minWords = minWords;
            this.maxWords = maxWords;
            this.evaluationCriteriaJson = evaluationCriteriaJson;
            this.sampleAnswer = sampleAnswer;
            this.audioUrl = audioUrl;
            this.speechDurationSeconds = speechDurationSeconds;
        }

        public WritingType getWritingType() { return writingType; }
        public String getPrompt() { return prompt; }
        public Integer getMinWords() { return minWords; }
        public Integer getMaxWords() { return maxWords; }
        public String getEvaluationCriteriaJson() { return evaluationCriteriaJson; }
        public String getSampleAnswer() { return sampleAnswer; }
        public String getAudioUrl() { return audioUrl; }
        public Integer getSpeechDurationSeconds() { return speechDurationSeconds; }
    }

    // ──────────────────────────────────────────────────────────
    // GETTERS AND SETTERS — common fields
    // ──────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getQuestionTextHindi() { return questionTextHindi; }
    public void setQuestionTextHindi(String questionTextHindi) { this.questionTextHindi = questionTextHindi; }

    public String getQuestionImageUrl() { return questionImageUrl; }
    public void setQuestionImageUrl(String questionImageUrl) { this.questionImageUrl = questionImageUrl; }

    public QuestionType getQuestionType() { return questionType; }
    public void setQuestionType(QuestionType questionType) { this.questionType = questionType; }

    public ExamCategory getExamCategory() { return examCategory; }
    public void setExamCategory(ExamCategory examCategory) { this.examCategory = examCategory; }

    public ExamType getExamType() { return examType; }
    public void setExamType(ExamType examType) { this.examType = examType; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public Language getLanguage() { return language; }
    public void setLanguage(Language language) { this.language = language; }

    public Double getMarks() { return marks; }
    public void setMarks(Double marks) { this.marks = marks; }

    public Double getNegativeMarks() { return negativeMarks; }
    public void setNegativeMarks(Double negativeMarks) { this.negativeMarks = negativeMarks; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsAiGenerated() { return isAiGenerated; }
    public void setIsAiGenerated(Boolean isAiGenerated) { this.isAiGenerated = isAiGenerated; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public Integer getQuestionOrderInGroup() { return questionOrderInGroup; }
    public void setQuestionOrderInGroup(Integer questionOrderInGroup) { this.questionOrderInGroup = questionOrderInGroup; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Integer getTimeLimitSeconds() { return timeLimitSeconds; }
    public void setTimeLimitSeconds(Integer timeLimitSeconds) { this.timeLimitSeconds = timeLimitSeconds; }

    public String getHint() { return hint; }
    public void setHint(String hint) { this.hint = hint; }

    public String getHintHindi() { return hintHindi; }
    public void setHintHindi(String hintHindi) { this.hintHindi = hintHindi; }

    public QuestionExplanation getExplanation() { return explanation; }
    public void setExplanation(QuestionExplanation explanation) { this.explanation = explanation; }

    // ── Payload getters/setters ────────────────────────────────

    public McqPayload getMcqPayload() { return mcqPayload; }
    public void setMcqPayload(McqPayload mcqPayload) { this.mcqPayload = mcqPayload; }

    public MultiCorrectPayload getMultiCorrectPayload() { return multiCorrectPayload; }
    public void setMultiCorrectPayload(MultiCorrectPayload multiCorrectPayload) { this.multiCorrectPayload = multiCorrectPayload; }

    public FillBlankPayload getFillBlankPayload() { return fillBlankPayload; }
    public void setFillBlankPayload(FillBlankPayload fillBlankPayload) { this.fillBlankPayload = fillBlankPayload; }

    public ArrangementPayload getArrangementPayload() { return arrangementPayload; }
    public void setArrangementPayload(ArrangementPayload arrangementPayload) { this.arrangementPayload = arrangementPayload; }

    public ImagePayload getImagePayload() { return imagePayload; }
    public void setImagePayload(ImagePayload imagePayload) { this.imagePayload = imagePayload; }

    public CodingPayload getCodingPayload() { return codingPayload; }
    public void setCodingPayload(CodingPayload codingPayload) { this.codingPayload = codingPayload; }

    public CodeSnippetPayload getCodeSnippetPayload() { return codeSnippetPayload; }
    public void setCodeSnippetPayload(CodeSnippetPayload codeSnippetPayload) { this.codeSnippetPayload = codeSnippetPayload; }

    public WritingPayload getWritingPayload() { return writingPayload; }
    public void setWritingPayload(WritingPayload writingPayload) { this.writingPayload = writingPayload; }
}