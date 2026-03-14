package com.example.intern_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

public class FacultyNoteDto {

    private Long id;
    private Long studentId;
    private String studentName;
    private String note;
    private String priority;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

