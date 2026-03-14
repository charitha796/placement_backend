package com.example.intern_backend.dto;

import lombok.Data;

import java.time.LocalDate;

public class InterviewRoundDto {

    private Long id;
    private String roundType;
    private LocalDate date;
    private String feedback;
    private String result;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRoundType() { return roundType; }
    public void setRoundType(String roundType) { this.roundType = roundType; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}

