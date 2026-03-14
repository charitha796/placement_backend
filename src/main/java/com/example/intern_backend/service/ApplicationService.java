package com.example.intern_backend.service;

import com.example.intern_backend.dto.ApplicationRequest;
import com.example.intern_backend.dto.ApplicationResponse;
import com.example.intern_backend.dto.InterviewRoundDto;
import com.example.intern_backend.entity.Application;
import com.example.intern_backend.entity.InterviewRound;
import com.example.intern_backend.entity.User;
import com.example.intern_backend.enums.ApplicationStatus;
import com.example.intern_backend.repository.ApplicationRepository;
import com.example.intern_backend.repository.InterviewRoundRepository;
import com.example.intern_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterviewRoundRepository interviewRoundRepository;

    public ApplicationResponse addApplication(Long studentId, ApplicationRequest request) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Application application = new Application();
        application.setStudent(student);
        application.setCompanyName(request.getCompanyName());
        application.setRoleApplied(request.getRoleApplied());
        application.setStipend(request.getStipend());
        application.setApplicationDate(LocalDate.now());
        application.setStatus(request.getStatus());
        application.setNotes(request.getNotes());
        application.setLastUpdated(LocalDateTime.now());

        Application saved = applicationRepository.save(application);
        return mapToResponse(saved);
    }

    public List<ApplicationResponse> getMyApplications(Long studentId) {
        return applicationRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ApplicationResponse updateApplication(Long appId, Long studentId, ApplicationRequest request) {
        Application application = applicationRepository.findById(appId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Access denied");
        }

        try {
            application.setCompanyName(request.getCompanyName());
            application.setRoleApplied(request.getRoleApplied());
            application.setStipend(request.getStipend());
            application.setStatus(request.getStatus());
            application.setNotes(request.getNotes());
            application.setLastUpdated(LocalDateTime.now());

            Application updated = applicationRepository.save(application);
            return mapToResponse(updated);
        } catch (Exception e) {
            System.err.println("Failed to update application ID: " + appId + ". Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update application: " + e.getMessage(), e);
        }
    }

    public List<InterviewRoundDto> getInterviewRounds(Long applicationId, Long studentId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Access denied");
        }

        return interviewRoundRepository.findByApplicationId(applicationId)
                .stream()
                .map(this::mapRoundToDto)
                .collect(Collectors.toList());
    }

    public InterviewRoundDto addInterviewRound(Long applicationId, Long studentId, InterviewRoundDto dto) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Access denied");
        }

        InterviewRound round = new InterviewRound();
        round.setApplication(application);
        round.setRoundType(dto.getRoundType());
        round.setDate(dto.getDate());
        round.setFeedback(dto.getFeedback());
        round.setResult(dto.getResult());

        InterviewRound saved = interviewRoundRepository.save(round);
        return mapRoundToDto(saved);
    }

    public InterviewRoundDto updateInterviewRound(Long roundId, Long studentId, InterviewRoundDto dto) {
        InterviewRound round = interviewRoundRepository.findById(roundId)
                .orElseThrow(() -> new RuntimeException("Interview round not found"));

        if (!round.getApplication().getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Access denied");
        }

        round.setRoundType(dto.getRoundType());
        round.setDate(dto.getDate());
        round.setFeedback(dto.getFeedback());
        round.setResult(dto.getResult());

        InterviewRound updated = interviewRoundRepository.save(round);
        return mapRoundToDto(updated);
    }

    public void deleteInterviewRound(Long roundId, Long studentId) {
        InterviewRound round = interviewRoundRepository.findById(roundId)
                .orElseThrow(() -> new RuntimeException("Interview round not found"));

        if (!round.getApplication().getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Access denied");
        }

        interviewRoundRepository.delete(round);
    }

    public List<ApplicationResponse> getAllApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> searchByName(String name) {
        return applicationRepository.findByStudentNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> searchByRollNumber(String rollNumber) {
        return applicationRepository.findByStudentRollNumber(rollNumber)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> filterBySection(String section) {
        return applicationRepository.findByStudentSectionIgnoreCase(section)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponse> filterByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void updateStatusBulk(List<Long> applicationIds, ApplicationStatus status) {
        List<Application> apps = applicationRepository.findAllById(applicationIds);
        for (Application app : apps) {
            app.setStatus(status);
            app.setLastUpdated(LocalDateTime.now());
        }
        applicationRepository.saveAll(apps);
    }

    private ApplicationResponse mapToResponse(Application app) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(app.getId());
        response.setCompanyName(app.getCompanyName());
        response.setRoleApplied(app.getRoleApplied());
        response.setStipend(app.getStipend());
        response.setApplicationDate(app.getApplicationDate());
        response.setStatus(app.getStatus());
        response.setNotes(app.getNotes());
        response.setLastUpdated(app.getLastUpdated());
        response.setStudentName(app.getStudent().getName());
        response.setRollNumber(app.getStudent().getRollNumber());
        response.setStudentId(app.getStudent().getId());
        response.setSection(app.getStudent().getSection());
        return response;
    }

    private InterviewRoundDto mapRoundToDto(InterviewRound round) {
        InterviewRoundDto dto = new InterviewRoundDto();
        dto.setId(round.getId());
        dto.setRoundType(round.getRoundType());
        dto.setDate(round.getDate());
        dto.setFeedback(round.getFeedback());
        dto.setResult(round.getResult());
        return dto;
    }
}
