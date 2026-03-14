package com.example.intern_backend.controller;

import com.example.intern_backend.entity.Application;
import com.example.intern_backend.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/export")
@CrossOrigin(origins = "http://localhost:3000")
public class ExportController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("/applications")
    public ResponseEntity<byte[]> exportApplicationsCsv() {
        List<Application> apps = applicationRepository.findAll();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;

        String header = "Id,StudentId,StudentName,RollNumber,Section,Company,Role,Stipend,ApplicationDate,Status,Notes\n";
        String body = apps.stream()
                .map(a -> String.join(",",
                        safe(a.getId()),
                        safe(a.getStudent().getId()),
                        quote(a.getStudent().getName()),
                        quote(a.getStudent().getRollNumber()),
                        quote(a.getStudent().getSection()),
                        quote(a.getCompanyName()),
                        quote(a.getRoleApplied()),
                        quote(a.getStipend()),
                        a.getApplicationDate() != null ? a.getApplicationDate().format(dateFormatter) : "",
                        quote(a.getStatus() != null ? a.getStatus().name() : ""),
                        quote(a.getNotes())))
                .collect(Collectors.joining("\n"));

        String csv = header + body;
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        headers.setContentDispositionFormData("attachment", "applications.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

    private String safe(Object o) {
        return o == null ? "" : o.toString();
    }

    private String quote(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}

