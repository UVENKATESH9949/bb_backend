// src/main/java/com/BrainBlitz/dto/request/FillBlankRequest.java

package com.BrainBlitz.dto.request;

import com.BrainBlitz.entity.QuestionExplanation;
import com.BrainBlitz.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;


public class FillBlankRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;            // use ___ for blank positions

    @NotNull(message = "Question type is required")
    private QuestionType questionType;      // FILL_BLANK or TEXT

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

    @NotEmpty(message = "At least one blank answer is required")
    private List<BlankAnswerRequest> blanks;

    
    public FillBlankRequest(@NotBlank(message = "Question text is required") String questionText,
			@NotNull(message = "Question type is required") QuestionType questionType,
			@NotNull(message = "Exam category is required") ExamCategory examCategory,
			@NotNull(message = "Exam type is required") ExamType examType,
			@NotBlank(message = "Subject is required") String subject, String topic,
			@NotNull(message = "Difficulty level is required") DifficultyLevel difficultyLevel,
			@NotNull(message = "Language is required") Language language, Double marks, Double negativeMarks,
			String hint, String explanation, String approachExplanation, Boolean isAiGenerated,
			@NotEmpty(message = "At least one blank answer is required") List<BlankAnswerRequest> blanks, QuestionExplanation questionExplanation) {
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
		this.blanks = blanks;
	}

    

	public FillBlankRequest() {
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

	public List<BlankAnswerRequest> getBlanks() {
		return blanks;
	}

	public void setBlanks(List<BlankAnswerRequest> blanks) {
		this.blanks = blanks;
	}

	public static class BlankAnswerRequest {

        @NotNull(message = "Blank position is required")
        private Integer blankPosition;      // 1, 2, 3 — order of ___ in text

        @NotBlank(message = "Correct answer is required")
        private String correctAnswer;

        private List<String> alternateAnswers;  // other accepted answers

        private Boolean caseSensitive = false;

		public BlankAnswerRequest(@NotNull(message = "Blank position is required") Integer blankPosition,
				@NotBlank(message = "Correct answer is required") String correctAnswer, List<String> alternateAnswers,
				Boolean caseSensitive) {
			super();
			this.blankPosition = blankPosition;
			this.correctAnswer = correctAnswer;
			this.alternateAnswers = alternateAnswers;
			this.caseSensitive = caseSensitive;
		}

		public BlankAnswerRequest() {
			super();
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
}