// src/main/java/com/BrainBlitz/dto/response/CheckAnswerResponse.java
package com.BrainBlitz.dto.response;

import java.util.List;

public class CheckAnswerResponse {

    private boolean isCorrect;

    // Text to show as the correct answer e.g. "B) Paris"
    private String correctDisplay;

    // Explanation text
    private String explanation;

    // Points awarded (positive) or deducted (negative)
    private double points;

    // For MCQ — correct option ids so frontend can highlight
    private List<Long> correctOptionIds;

    // For MCQ — correct option texts e.g. ["B) Paris"]
    private List<String> correctOptions;

    // For image options — correct option image URLs
    private List<String> correctOptionImages;

    // Getters & Setters
    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }

    public String getCorrectDisplay() { return correctDisplay; }
    public void setCorrectDisplay(String correctDisplay) { this.correctDisplay = correctDisplay; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public double getPoints() { return points; }
    public void setPoints(double points) { this.points = points; }

    public List<Long> getCorrectOptionIds() { return correctOptionIds; }
    public void setCorrectOptionIds(List<Long> correctOptionIds) { this.correctOptionIds = correctOptionIds; }

    public List<String> getCorrectOptions() { return correctOptions; }
    public void setCorrectOptions(List<String> correctOptions) { this.correctOptions = correctOptions; }

    public List<String> getCorrectOptionImages() { return correctOptionImages; }
    public void setCorrectOptionImages(List<String> correctOptionImages) { this.correctOptionImages = correctOptionImages; }
}