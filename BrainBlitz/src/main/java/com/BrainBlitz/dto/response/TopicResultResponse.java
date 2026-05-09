package com.BrainBlitz.dto.response;

public class TopicResultResponse {

    private String subject;
    private String topic;
    private int totalQuestions;
    private int correct;
    private int wrong;
    private int skipped;
    private double accuracyPercentage;
    private double avgTimeTaken;

    // ─────────────────────────────────────────────
    // GETTERS AND SETTERS
    // ─────────────────────────────────────────────

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { 
        this.totalQuestions = totalQuestions; 
    }

    public int getCorrect() { return correct; }
    public void setCorrect(int correct) { this.correct = correct; }

    public int getWrong() { return wrong; }
    public void setWrong(int wrong) { this.wrong = wrong; }

    public int getSkipped() { return skipped; }
    public void setSkipped(int skipped) { this.skipped = skipped; }

    public double getAccuracyPercentage() { return accuracyPercentage; }
    public void setAccuracyPercentage(double accuracyPercentage) { 
        this.accuracyPercentage = accuracyPercentage; 
    }

    public double getAvgTimeTaken() { return avgTimeTaken; }
    public void setAvgTimeTaken(double avgTimeTaken) { 
        this.avgTimeTaken = avgTimeTaken; 
    }
}