package com.example.intern_backend.service;

import com.example.intern_backend.dto.ProgressOverview;
import com.example.intern_backend.dto.SectionStats;
import com.example.intern_backend.entity.Application;
import com.example.intern_backend.entity.User;
import com.example.intern_backend.enums.ApplicationStatus;
import com.example.intern_backend.enums.Role;
import com.example.intern_backend.repository.ApplicationRepository;
import com.example.intern_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    public ProgressOverview getProgressOverview() {
        List<User> students = userRepository.findByRole(Role.STUDENT);
        int totalStudents = students.size();
        int notStarted = 0;
        int started = 0;
        int inProgress = 0;
        int completed = 0;

        for (User student : students) {
            List<Application> apps = applicationRepository.findByStudentId(student.getId());
            if (apps.isEmpty()) {
                notStarted++;
            } else {
                boolean hasOffer = apps.stream().anyMatch(a -> a.getStatus() == ApplicationStatus.OFFERED);
                if (hasOffer) {
                    completed++;
                } else {
                    boolean hasShortlistedOrInterviewed = apps.stream()
                            .anyMatch(a -> a.getStatus() == ApplicationStatus.SHORTLISTED
                                    || a.getStatus() == ApplicationStatus.INTERVIEWED);
                    if (hasShortlistedOrInterviewed) {
                        inProgress++;
                    } else {
                        started++;
                    }
                }
            }
        }

        return new ProgressOverview(totalStudents, notStarted, started, inProgress, completed);
    }

    public List<SectionStats> getSectionStats() {
        List<User> students = userRepository.findByRole(Role.STUDENT);
        Map<String, List<User>> sectionMap = students.stream()
                .collect(Collectors.groupingBy(u -> u.getSection() != null ? u.getSection() : "Unknown"));

        List<SectionStats> stats = new ArrayList<>();
        for (Map.Entry<String, List<User>> entry : sectionMap.entrySet()) {
            String section = entry.getKey();
            List<User> sectionStudents = entry.getValue();

            int totalApps = 0;
            int totalOffers = 0;
            int completedStudents = 0;

            for (User student : sectionStudents) {
                List<Application> apps = applicationRepository.findByStudentId(student.getId());
                totalApps += apps.size();
                boolean hasOffer = apps.stream().anyMatch(a -> a.getStatus() == ApplicationStatus.OFFERED);
                if (hasOffer) {
                    totalOffers++;
                    completedStudents++;
                }
            }

            double completionRate = sectionStudents.isEmpty() ? 0
                    : (double) completedStudents / sectionStudents.size() * 100;
            stats.add(new SectionStats(section, totalApps, totalOffers, completionRate));
        }

        return stats;
    }

    public Map<String, Long> getOverallStats() {
        return Map.of(
                "totalStudents", userRepository.countByRole(Role.STUDENT),
                "totalApplications", applicationRepository.count(),
                "totalOffers", applicationRepository.countByStatus(ApplicationStatus.OFFERED),
                "inProgress", applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED)
                        + applicationRepository.countByStatus(ApplicationStatus.INTERVIEWED));
    }
}
