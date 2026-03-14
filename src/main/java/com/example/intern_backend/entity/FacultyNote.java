package com.example.intern_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "faculty_notes")
public class FacultyNote {

    public FacultyNote() {}

    public FacultyNote(Long id, User faculty, User student, String note, String priority, LocalDateTime createdAt) {
        this.id = id;
        this.faculty = faculty;
        this.student = student;
        this.note = note;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private User faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(length = 2000)
    private String note;

    private String priority; // e.g. LOW, MEDIUM, HIGH

    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getFaculty() { return faculty; }
    public void setFaculty(User faculty) { this.faculty = faculty; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

