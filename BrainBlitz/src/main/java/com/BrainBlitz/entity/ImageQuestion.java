package com.BrainBlitz.entity;

import com.BrainBlitz.enums.ImageQuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "image_questions")
public class ImageQuestion {

    // ─────────────────────────────────────────────
    // PRIMARY KEY
    // ─────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─────────────────────────────────────────────
    // PARENT REFERENCE
    // ─────────────────────────────────────────────

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    @JsonIgnore
    private Question question;

    // ─────────────────────────────────────────────
    // IMAGE QUESTION TYPE
    // ─────────────────────────────────────────────

    // Tells frontend exactly how to render this question
    // MIRROR_IMAGE    → show main image, pick mirror from 4 options
    // WATER_IMAGE     → show main image, pick water reflection from 4 options
    // IMAGE_MCQ       → show question image, pick correct text option
    // IMAGE_OPTIONS   → show text question, pick correct image option
    // PATTERN_MATRIX  → show 3x3 grid with missing piece, pick from 4 image options
    // ODD_ONE_OUT     → show 4 images, pick the odd one
    // FIGURE_SERIES   → show sequence of images, pick next in series
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageQuestionType imageQuestionType;

    // ─────────────────────────────────────────────
    // MAIN QUESTION IMAGE
    // ─────────────────────────────────────────────

    // Primary image shown in the question
    // Used in: MIRROR_IMAGE, WATER_IMAGE, IMAGE_MCQ
    // null for: IMAGE_OPTIONS (text question, no main image)
    @Column
    private String mainImageUrl;

    // ─────────────────────────────────────────────
    // SUPPORTING IMAGES (JSON array)
    // ─────────────────────────────────────────────

    // Used differently per type:
    //
    // PATTERN_MATRIX  → 8 matrix cell images (3x3 grid minus the missing one)
    //   e.g. ["img1.png","img2.png","img3.png",
    //          "img4.png","img5.png","img6.png",
    //          "img7.png","img8.png"]
    //   (position 9 is the missing cell = the question)
    //
    // FIGURE_SERIES   → sequence images shown to user before the blank
    //   e.g. ["fig1.png","fig2.png","fig3.png","fig4.png"]
    //   user picks what comes next
    //
    // ODD_ONE_OUT     → all 4 images (one is the odd one)
    //   e.g. ["apple.png","mango.png","banana.png","car.png"]
    //
    // null for types that only need mainImageUrl
    @Column(columnDefinition = "TEXT")
    private String supportingImagesJson;  // JSON array of image URLs

    // ─────────────────────────────────────────────
    // MATRIX SPECIFIC (PATTERN_MATRIX only)
    // ─────────────────────────────────────────────

    // Position of the missing cell in the 3x3 grid
    // 1-9 where 9 = bottom right
    // Usually 9 (bottom-right) but can be any position
    @Column
    private Integer missingCellPosition;

    // ─────────────────────────────────────────────
    // IMAGE DIMENSIONS HINT (optional)
    // ─────────────────────────────────────────────

    // Hints to frontend how to size images
    // e.g. "200x200" or "400x300"
    // Helps maintain consistent UI across question types
    @Column
    private String imageDimensions;

    // ─────────────────────────────────────────────
    // ALT TEXT (accessibility)
    // ─────────────────────────────────────────────

    // Describes the image for accessibility
    // Also useful when image fails to load
    @Column(columnDefinition = "TEXT")
    private String mainImageAltText;

    @Column(columnDefinition = "TEXT")
    private String supportingImagesAltTextJson; // JSON array of alt texts

    // ─────────────────────────────────────────────
    // CORRECT ANSWER
    // ─────────────────────────────────────────────

    // For ODD_ONE_OUT → index of the odd image (1-4)
    // For PATTERN_MATRIX → index of correct option (1-4)
    // For FIGURE_SERIES → index of correct option (1-4)
    // For MIRROR_IMAGE, WATER_IMAGE → index of correct option (1-4)
    // For IMAGE_MCQ → stored in McqOption.isCorrect
    // For IMAGE_OPTIONS → stored in McqOption.isCorrect
    // null for IMAGE_MCQ and IMAGE_OPTIONS (handled by McqOption)
    @Column
    private Integer correctOptionIndex;

    // ─────────────────────────────────────────────
    // IMAGE OPTIONS (for image option questions)
    // ─────────────────────────────────────────────

    // JSON array of 4 image URLs shown as answer options
    // Used in: MIRROR_IMAGE, WATER_IMAGE, PATTERN_MATRIX,
    //          FIGURE_SERIES, ODD_ONE_OUT, IMAGE_OPTIONS
    // null for IMAGE_MCQ (uses McqOption.optionText instead)
    @Column(columnDefinition = "TEXT")
    private String optionImagesJson;   // JSON array of 4 image URLs

    @Column(columnDefinition = "TEXT")
    private String optionImagesAltTextJson; // JSON array of alt texts for options

	public ImageQuestion(Long id, Question question, ImageQuestionType imageQuestionType, String mainImageUrl,
			String supportingImagesJson, Integer missingCellPosition, String imageDimensions, String mainImageAltText,
			String supportingImagesAltTextJson, Integer correctOptionIndex, String optionImagesJson,
			String optionImagesAltTextJson) {
		super();
		this.id = id;
		this.question = question;
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

	public ImageQuestion() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
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

	public String getSupportingImagesJson() {
		return supportingImagesJson;
	}

	public void setSupportingImagesJson(String supportingImagesJson) {
		this.supportingImagesJson = supportingImagesJson;
	}

	public Integer getMissingCellPosition() {
		return missingCellPosition;
	}

	public void setMissingCellPosition(Integer missingCellPosition) {
		this.missingCellPosition = missingCellPosition;
	}

	public String getImageDimensions() {
		return imageDimensions;
	}

	public void setImageDimensions(String imageDimensions) {
		this.imageDimensions = imageDimensions;
	}

	public String getMainImageAltText() {
		return mainImageAltText;
	}

	public void setMainImageAltText(String mainImageAltText) {
		this.mainImageAltText = mainImageAltText;
	}

	public String getSupportingImagesAltTextJson() {
		return supportingImagesAltTextJson;
	}

	public void setSupportingImagesAltTextJson(String supportingImagesAltTextJson) {
		this.supportingImagesAltTextJson = supportingImagesAltTextJson;
	}

	public Integer getCorrectOptionIndex() {
		return correctOptionIndex;
	}

	public void setCorrectOptionIndex(Integer correctOptionIndex) {
		this.correctOptionIndex = correctOptionIndex;
	}

	public String getOptionImagesJson() {
		return optionImagesJson;
	}

	public void setOptionImagesJson(String optionImagesJson) {
		this.optionImagesJson = optionImagesJson;
	}

	public String getOptionImagesAltTextJson() {
		return optionImagesAltTextJson;
	}

	public void setOptionImagesAltTextJson(String optionImagesAltTextJson) {
		this.optionImagesAltTextJson = optionImagesAltTextJson;
	}
    
    
}