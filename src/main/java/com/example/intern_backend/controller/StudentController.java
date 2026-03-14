package com.example.intern_backend.controller;

import com.example.intern_backend.dto.ApplicationRequest;
import com.example.intern_backend.dto.ApplicationResponse;
import com.example.intern_backend.dto.InterviewRoundDto;
import com.example.intern_backend.dto.StudentDocumentDto;
import com.example.intern_backend.dto.StudentProfileDto;
import com.example.intern_backend.entity.User;
import com.example.intern_backend.service.ApplicationService;
import com.example.intern_backend.service.StudentProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private StudentProfileService studentProfileService;

    @PostMapping("/applications")
    public ResponseEntity<ApplicationResponse> addApplication(@AuthenticationPrincipal User student,
            @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.addApplication(student.getId(), request));
    }

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(@AuthenticationPrincipal User student) {
        return ResponseEntity.ok(applicationService.getMyApplications(student.getId()));
    }

    @PutMapping("/applications/{id}")
    public ResponseEntity<ApplicationResponse> updateApplication(
            @AuthenticationPrincipal User student,
            @PathVariable("id") Long id,
            @RequestBody ApplicationRequest request) {
        return ResponseEntity.ok(applicationService.updateApplication(id, student.getId(), request));
    }

    @GetMapping("/profile")
    public ResponseEntity<StudentProfileDto> getProfile(@AuthenticationPrincipal User student) {
        return ResponseEntity.ok(studentProfileService.getProfile(student.getId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<StudentProfileDto> updateProfile(
            @AuthenticationPrincipal User student,
            @RequestBody StudentProfileDto request) {
        return ResponseEntity.ok(studentProfileService.updateProfile(student.getId(), request));
    }

    @GetMapping("/documents")
    public ResponseEntity<List<StudentDocumentDto>> listDocuments(@AuthenticationPrincipal User student) {
        return ResponseEntity.ok(studentProfileService.listDocuments(student.getId()));
    }

    @PostMapping("/documents")
    public ResponseEntity<StudentDocumentDto> addDocument(
            @AuthenticationPrincipal User student,
            @RequestBody StudentDocumentDto request) {
        return ResponseEntity.ok(studentProfileService.addDocument(student.getId(), request));
    }

    @PostMapping("/documents/upload")
    public ResponseEntity<StudentDocumentDto> uploadDocument(
            @AuthenticationPrincipal User student,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", required = false) String type) {
        return ResponseEntity.ok(studentProfileService.addDocumentFromFile(student.getId(), file, type));
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(
            @AuthenticationPrincipal User student,
            @PathVariable("id") Long id) {
        studentProfileService.deleteDocument(student.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/applications/{id}/rounds")
    public ResponseEntity<List<InterviewRoundDto>> getInterviewRounds(
            @AuthenticationPrincipal User student,
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(applicationService.getInterviewRounds(id, student.getId()));
    }

    @PostMapping("/applications/{id}/rounds")
    public ResponseEntity<InterviewRoundDto> addInterviewRound(
            @AuthenticationPrincipal User student,
            @PathVariable("id") Long id,
            @RequestBody InterviewRoundDto dto) {
        return ResponseEntity.ok(applicationService.addInterviewRound(id, student.getId(), dto));
    }

    @PutMapping("/rounds/{roundId}")
    public ResponseEntity<InterviewRoundDto> updateInterviewRound(
            @AuthenticationPrincipal User student,
            @PathVariable("roundId") Long roundId,
            @RequestBody InterviewRoundDto dto) {
        return ResponseEntity.ok(applicationService.updateInterviewRound(roundId, student.getId(), dto));
    }

    @DeleteMapping("/rounds/{roundId}")
    public ResponseEntity<Void> deleteInterviewRound(
            @AuthenticationPrincipal User student,
            @PathVariable("roundId") Long roundId) {
        applicationService.deleteInterviewRound(roundId, student.getId());
        return ResponseEntity.noContent().build();
    }
}
