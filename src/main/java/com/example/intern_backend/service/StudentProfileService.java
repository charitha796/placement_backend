package com.example.intern_backend.service;

import com.example.intern_backend.dto.StudentDocumentDto;
import com.example.intern_backend.dto.StudentProfileDto;
import com.example.intern_backend.entity.StudentDocument;
import com.example.intern_backend.entity.User;
import com.example.intern_backend.repository.StudentDocumentRepository;
import com.example.intern_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentDocumentRepository documentRepository;

    public StudentProfileDto getProfile(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentProfileDto dto = new StudentProfileDto();
        dto.setName(student.getName());
        dto.setRollNumber(student.getRollNumber());
        dto.setDepartment(student.getDepartment());
        dto.setSection(student.getSection());
        dto.setEmail(student.getEmail());
        dto.setHeadline(student.getHeadline());
        dto.setSkills(student.getSkills());
        dto.setGithubUrl(student.getGithubUrl());
        dto.setLinkedinUrl(student.getLinkedinUrl());
        dto.setPortfolioUrl(student.getPortfolioUrl());
        dto.setAbout(student.getAbout());
        return dto;
    }

    public StudentProfileDto updateProfile(Long studentId, StudentProfileDto request) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setDepartment(request.getDepartment());
        student.setSection(request.getSection());
        student.setHeadline(request.getHeadline());
        student.setSkills(request.getSkills());
        student.setGithubUrl(request.getGithubUrl());
        student.setLinkedinUrl(request.getLinkedinUrl());
        student.setPortfolioUrl(request.getPortfolioUrl());
        student.setAbout(request.getAbout());

        userRepository.save(student);
        return getProfile(studentId);
    }

    public List<StudentDocumentDto> listDocuments(Long studentId) {
        return documentRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapDocToDto)
                .collect(Collectors.toList());
    }

    public StudentDocumentDto addDocument(Long studentId, StudentDocumentDto dto) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentDocument doc = new StudentDocument();
        doc.setStudent(student);
        doc.setTitle(dto.getTitle());
        doc.setType(dto.getType());
        doc.setFileUrl(dto.getFileUrl());
        doc.setCreatedAt(LocalDateTime.now());

        StudentDocument saved = documentRepository.save(doc);
        return mapDocToDto(saved);
    }

    public StudentDocumentDto addDocumentFromFile(Long studentId, MultipartFile file, String type) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            Path uploadDir = Paths.get("uploads/documents").toAbsolutePath().normalize();
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document";
            String filename = System.currentTimeMillis() + "_" + originalName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            Path target = uploadDir.resolve(filename);
            
            System.out.println("Uploading file: " + originalName + " to " + target.toString());
            
            file.transferTo(target.toFile());

            String fileUrl = "http://localhost:8081/files/" + filename;

            StudentDocument doc = new StudentDocument();
            doc.setStudent(student);
            doc.setTitle(originalName);
            doc.setType(type != null ? type : "OTHER");
            doc.setFileUrl(fileUrl);
            doc.setCreatedAt(LocalDateTime.now());

            StudentDocument saved = documentRepository.save(doc);
            System.out.println("Document saved successfully with ID: " + saved.getId());
            return mapDocToDto(saved);
        } catch (IOException e) {
            System.err.println("Failed to store file: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    public void deleteDocument(Long studentId, Long documentId) {
        StudentDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!doc.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Access denied");
        }

        documentRepository.delete(doc);
    }

    private StudentDocumentDto mapDocToDto(StudentDocument doc) {
        StudentDocumentDto dto = new StudentDocumentDto();
        dto.setId(doc.getId());
        dto.setTitle(doc.getTitle());
        dto.setType(doc.getType());
        dto.setFileUrl(doc.getFileUrl());
        dto.setCreatedAt(doc.getCreatedAt());
        return dto;
    }
}

