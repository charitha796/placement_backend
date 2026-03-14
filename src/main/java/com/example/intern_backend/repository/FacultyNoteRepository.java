package com.example.intern_backend.repository;

import com.example.intern_backend.entity.FacultyNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyNoteRepository extends JpaRepository<FacultyNote, Long> {

    List<FacultyNote> findByStudentIdOrderByCreatedAtDesc(Long studentId);
}

