package com.example.intern_backend.service;

import com.example.intern_backend.dto.FacultyNoteDto;
import com.example.intern_backend.entity.FacultyNote;
import com.example.intern_backend.entity.User;
import com.example.intern_backend.enums.Role;
import com.example.intern_backend.repository.FacultyNoteRepository;
import com.example.intern_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyNoteService {

    @Autowired
    private FacultyNoteRepository facultyNoteRepository;

    @Autowired
    private UserRepository userRepository;

    public List<FacultyNoteDto> listNotesForStudent(Long studentId) {
        return facultyNoteRepository.findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public FacultyNoteDto addNote(Long facultyId, Long studentId, FacultyNoteDto request) {
        User faculty = userRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        if (faculty.getRole() != Role.FACULTY && faculty.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access denied");
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        FacultyNote note = new FacultyNote();
        note.setFaculty(faculty);
        note.setStudent(student);
        note.setNote(request.getNote());
        note.setPriority(request.getPriority());
        note.setCreatedAt(LocalDateTime.now());

        FacultyNote saved = facultyNoteRepository.save(note);
        return mapToDto(saved);
    }

    private FacultyNoteDto mapToDto(FacultyNote note) {
        FacultyNoteDto dto = new FacultyNoteDto();
        dto.setId(note.getId());
        dto.setStudentId(note.getStudent().getId());
        dto.setStudentName(note.getStudent().getName());
        dto.setNote(note.getNote());
        dto.setPriority(note.getPriority());
        dto.setCreatedAt(note.getCreatedAt());
        return dto;
    }
}

