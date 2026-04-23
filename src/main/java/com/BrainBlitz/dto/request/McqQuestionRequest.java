// src/main/java/com/BrainBlitz/dto/request/McqQuestionRequest.java

package com.BrainBlitz.dto.request;

import com.BrainBlitz.entity.QuestionExplanation;
import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

public class McqQuestionRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;      // MCQ, MULTI_CORRECT, TRUE_FALSE etc.

    @NotNull(message = "Exam category is required")
    private ExamCategory examCategory;

    @NotNull(message = "Exam type is required")
    private ExamType examType;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String topic;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficultyLevel;

    @NotNull(message = "Language is required")
    private Language language;

    @Min(value = 0, message = "Marks cannot be negative")
    private Double marks = 1.0;

    @Min(value = 0, message = "Negative marks cannot be negative")
    private Double negativeMarks = 0.0;

    private String hint;
    
    private QuestionExplanation questionExplanation;
    private Boolean isAiGenerated = false;

    // Group ID — only for RC, cloze, DI type questions
    private Long groupId;

    @NotEmpty(message = "At least 2 options are required")
    @Size(min = 2, max = 6, message = "Options must be between 2 and 6")
    private List<McqOptionRequest> options;

	public McqQuestionRequest(@NotBlank(message = "Question text is required") String questionText,
			@NotNull(message = "Question type is required") QuestionType questionType,
			@NotNull(message = "Exam category is required") ExamCategory examCategory,
			@NotNull(message = "Exam type is required") ExamType examType,
			@NotBlank(message = "Subject is required") String subject, String topic,
			@NotNull(message = "Difficulty level is required") DifficultyLevel difficultyLevel,
			@NotNull(message = "Language is required") Language language,
			@Min(value = 0, message = "Marks cannot be negative") Double marks,
			@Min(value = 0, message = "Negative marks cannot be negative") Double negativeMarks, String hint,
			QuestionExplanation questionExplanation, Boolean isAiGenerated, Long groupId,
			@NotEmpty(message = "At least 2 options are required") @Size(min = 2, max = 6, message = "Options must be between 2 and 6") List<McqOptionRequest> options) {
		super();
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
		this.isAiGenerated = isAiGenerated;
		this.groupId = groupId;
		this.options = options;
	}

	public McqQuestionRequest() {
		super();
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

	public List<McqOptionRequest> getOptions() {
		return options;
	}

	public void setOptions(List<McqOptionRequest> options) {
		this.options = options;
	}

    public QuestionExplanation getQuestionExplanation() {
		return questionExplanation;
	}

	public void setQuestionExplanation(QuestionExplanation questionExplanation) {
		this.questionExplanation = questionExplanation;
	}



	public static class McqOptionRequest {

        @NotBlank(message = "Option text is required")
        private String optionText;

        private String optionImageUrl;      // optional — for image-based options

        @NotNull(message = "isCorrect flag is required")
        private Boolean isCorrect;

        @NotNull(message = "Option order is required")
        private Integer optionOrder;    
        // 1, 2, 3, 4

		public McqOptionRequest(@NotBlank(message = "Option text is required") String optionText, String optionImageUrl,
				@NotNull(message = "isCorrect flag is required") Boolean isCorrect,
				@NotNull(message = "Option order is required") Integer optionOrder) {
			super();
			this.optionText = optionText;
			this.optionImageUrl = optionImageUrl;
			this.isCorrect = isCorrect;
			this.optionOrder = optionOrder;
		}
		public McqOptionRequest() {
			super();
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
}