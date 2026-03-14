package com.example.intern_backend.controller;

import com.example.intern_backend.dto.ApplicationResponse;
import com.example.intern_backend.dto.BulkStatusUpdateRequest;
import com.example.intern_backend.dto.FacultyNoteDto;
import com.example.intern_backend.entity.User;
import com.example.intern_backend.enums.ApplicationStatus;
import com.example.intern_backend.service.ApplicationService;
import com.example.intern_backend.service.FacultyNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "http://localhost:3000")
public class FacultyController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private FacultyNoteService facultyNoteService;

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponse>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ApplicationResponse>> search(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "rollNumber", required = false) String rollNumber) {
        if (name != null) {
            return ResponseEntity.ok(applicationService.searchByName(name));
        } else if (rollNumber != null) {
            return ResponseEntity.ok(applicationService.searchByRollNumber(rollNumber));
        }
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ApplicationResponse>> filterBySection(@RequestParam("section") String section) {
        return ResponseEntity.ok(applicationService.filterBySection(section));
    }

    @GetMapping("/filter/status")
    public ResponseEntity<List<ApplicationResponse>> filterByStatus(@RequestParam("status") ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.filterByStatus(status));
    }

    @PutMapping("/applications/bulk-status")
    public ResponseEntity<Void> updateStatusBulk(@RequestBody BulkStatusUpdateRequest request) {
        applicationService.updateStatusBulk(request.getApplicationIds(), request.getStatus());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notes")
    public ResponseEntity<List<FacultyNoteDto>> getNotesForStudent(@RequestParam("studentId") Long studentId) {
        return ResponseEntity.ok(facultyNoteService.listNotesForStudent(studentId));
    }

    @PostMapping("/notes")
    public ResponseEntity<FacultyNoteDto> addNote(
            @AuthenticationPrincipal User faculty,
            @RequestBody FacultyNoteDto request) {
        return ResponseEntity.ok(facultyNoteService.addNote(faculty.getId(), request.getStudentId(), request));
    }
}
