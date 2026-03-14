package com.example.intern_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "interview_rounds")
public class InterviewRound {

    public InterviewRound() {}

    public InterviewRound(Long id, Application application, String roundType, LocalDate date, String feedback, String result) {
        this.id = id;
        this.application = application;
        this.roundType = roundType;
        this.date = date;
        this.feedback = feedback;
        this.result = result;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    private String roundType; // e.g. HR, Technical, Online Test

    private LocalDate date;

    @Column(length = 2000)
    private String feedback;

    private String result; // e.g. PASSED, FAILED, PENDING

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }
    public String getRoundType() { return roundType; }
    public void setRoundType(String roundType) { this.roundType = roundType; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}

