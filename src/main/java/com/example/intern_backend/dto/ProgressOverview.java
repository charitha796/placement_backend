package com.example.intern_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProgressOverview {
    private int totalStudents;
    private int notStarted;
    private int started;
    private int inProgress;
    private int completed;

    public ProgressOverview() {}

    public ProgressOverview(int totalStudents, int notStarted, int started, int inProgress, int completed) {
        this.totalStudents = totalStudents;
        this.notStarted = notStarted;
        this.started = started;
        this.inProgress = inProgress;
        this.completed = completed;
    }

    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
    public int getNotStarted() { return notStarted; }
    public void setNotStarted(int notStarted) { this.notStarted = notStarted; }
    public int getStarted() { return started; }
    public void setStarted(int started) { this.started = started; }
    public int getInProgress() { return inProgress; }
    public void setInProgress(int inProgress) { this.inProgress = inProgress; }
    public int getCompleted() { return completed; }
    public void setCompleted(int completed) { this.completed = completed; }
}
