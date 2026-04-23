package com.BrainBlitz.dto.request;

public class PracticeSubmitRequest {

    private String category;
    private String examType;
    private String subject;
    private int correct;
    private int total;
    private double score;
    private int wrong;
    private int skipped;
    private Integer durationSeconds;
    private String mode;
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    
    // Getters and Setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public int getCorrect() { return correct; }
    public void setCorrect(int correct) { this.correct = correct; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    
    public int getWrong() { return wrong; }
    public void setWrong(int wrong) { this.wrong = wrong; }

    public int getSkipped() { return skipped; }
    public void setSkipped(int skipped) { this.skipped = skipped; }

    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { 
        this.durationSeconds = durationSeconds; 
    }
}