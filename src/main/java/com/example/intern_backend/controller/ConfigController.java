package com.example.intern_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "http://localhost:3000")
public class ConfigController {

    @GetMapping("/sections")
    public ResponseEntity<List<String>> getSections() {
        // For now reuse the same static list as frontend, but served by backend
        return ResponseEntity.ok(List.of("A", "B", "B1", "B2", "C", "D"));
    }
}

