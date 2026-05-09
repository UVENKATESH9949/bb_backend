// src/main/java/com/BrainBlitz/dto/request/ImageQuestionRequest.java

package com.BrainBlitz.dto.request;

import com.BrainBlitz.entity.QuestionExplanation;
import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

public class ImageQuestionRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Image question type is required")
    private ImageQuestionType imageQuestionType;    // MIRROR, WATER, PATTERN etc.

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

    private Double marks = 1.0;
    private Double negativeMarks = 0.0;
    private String hint;
    private QuestionExplanation questionExplanation;
    private Boolean isAiGenerated = false;

    @NotBlank(message = "Main image URL is required")
    private String mainImageUrl;

    // For pattern_matrix — 8 grid images + 4 option images
    // For odd_one_out — multiple images to compare
    // For figure_series — sequence images
    private List<String> supportingImageUrls;

    // Options — can be text or image URLs depending on type
    @NotEmpty(message = "At least 2 options required")
    private List<ImageOptionRequest> options;

    
    public ImageQuestionRequest(@NotBlank(message = "Question text is required") String questionText,
			@NotNull(message = "Image question type is required") ImageQuestionType imageQuestionType,
			@NotNull(message = "Exam category is required") ExamCategory examCategory,
			@NotNull(message = "Exam type is required") ExamType examType,
			@NotBlank(message = "Subject is required") String subject, String topic,
			@NotNull(message = "Difficulty level is required") DifficultyLevel difficultyLevel,
			@NotNull(message = "Language is required") Language language, Double marks, Double negativeMarks,
			String hint, QuestionExplanation questionExplanation, Boolean isAiGenerated,
			@NotBlank(message = "Main image URL is required") String mainImageUrl, List<String> supportingImageUrls,
			@NotEmpty(message = "At least 2 options required") List<ImageOptionRequest> options) {
		super();
		this.questionText = questionText;
		this.imageQuestionType = imageQuestionType;
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
		this.mainImageUrl = mainImageUrl;
		this.supportingImageUrls = supportingImageUrls;
		this.options = options;
	}

    
	public ImageQuestionRequest() {
		super();
	}

	
	public String getQuestionText() {
		return questionText;
	}


	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}


	public ImageQuestionType getImageQuestionType() {
		return imageQuestionType;
	}


	public void setImageQuestionType(ImageQuestionType imageQuestionType) {
		this.imageQuestionType = imageQuestionType;
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


	public Boolean getIsAiGenerated() {
		return isAiGenerated;
	}


	public void setIsAiGenerated(Boolean isAiGenerated) {
		this.isAiGenerated = isAiGenerated;
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


	public List<ImageOptionRequest> getOptions() {
		return options;
	}


	public void setOptions(List<ImageOptionRequest> options) {
		this.options = options;
	}


	public static class ImageOptionRequest {

        private String optionText;          // text option
        private String optionImageUrl;      // image option
        private Boolean isCorrect;
        private Integer optionOrder;
		public ImageOptionRequest(String optionText, String optionImageUrl, Boolean isCorrect, Integer optionOrder) {
			super();
			this.optionText = optionText;
			this.optionImageUrl = optionImageUrl;
			this.isCorrect = isCorrect;
			this.optionOrder = optionOrder;
		}
		public ImageOptionRequest() {
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