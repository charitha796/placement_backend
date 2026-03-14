package com.example.intern_backend.repository;

import com.example.intern_backend.entity.Application;
import com.example.intern_backend.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentId);

    List<Application> findByStudentNameContainingIgnoreCase(String name);

    List<Application> findByStudentRollNumber(String rollNumber);

    List<Application> findByStudentSectionIgnoreCase(String section);

    List<Application> findByStatus(ApplicationStatus status);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.student.id = :studentId AND a.status = :status")
    long countByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") ApplicationStatus status);

    long countByStatus(ApplicationStatus status);
}
