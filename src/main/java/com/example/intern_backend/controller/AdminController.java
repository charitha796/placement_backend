package com.example.intern_backend.controller;

import com.example.intern_backend.dto.ProgressOverview;
import com.example.intern_backend.dto.SectionStats;
import com.example.intern_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/progress")
    public ResponseEntity<ProgressOverview> getProgressOverview() {
        return ResponseEntity.ok(adminService.getProgressOverview());
    }

    @GetMapping("/sections")
    public ResponseEntity<List<SectionStats>> getSectionStats() {
        return ResponseEntity.ok(adminService.getSectionStats());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getOverallStats() {
        return ResponseEntity.ok(adminService.getOverallStats());
    }
}
