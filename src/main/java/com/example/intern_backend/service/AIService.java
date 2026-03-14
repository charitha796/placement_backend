package com.example.intern_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.intern_backend.entity.User;
import com.example.intern_backend.entity.StudentDocument;
import com.example.intern_backend.repository.StudentDocumentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private StudentDocumentRepository documentRepository;

    public String getAIResponse(User student, String prompt, List<Map<String, String>> history) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Fetch resume
        String resumeText = "";
        try {
            List<StudentDocument> docs = documentRepository.findByStudentId(student.getId());
            StudentDocument resume = docs.stream()
                .filter(d -> "RESUME".equals(d.getType()))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .findFirst()
                .orElse(null);

            if (resume != null && resume.getFileUrl() != null) {
                // The fileUrl looks like: http://localhost:8081/files/filename.pdf
                // We need to resolve this back to the local uploads/documents folder
                String filename = resume.getFileUrl().substring(resume.getFileUrl().lastIndexOf("/") + 1);
                File file = new File("uploads/documents/" + filename);
                if (file.exists() && filename.toLowerCase().endsWith(".pdf")) {
                    try (PDDocument document = Loader.loadPDF(file)) {
                        PDFTextStripper textStripper = new PDFTextStripper();
                        resumeText = textStripper.getText(document);
                        // Limit resume text length to avoid context window issues
                        if (resumeText.length() > 3000) {
                            resumeText = resumeText.substring(0, 3000) + "...";
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error extracting resume: " + e.getMessage());
        }

        // System prompt to set the persona as an interviewer
        String systemPrompt = "You are an expert technical interviewer for internship placements. " +
                "You are friendly but professional. Ask one question at a time. " +
                "Keep your questions very short and concise (maximum 2 sentences). " +
                "Focus strictly on the candidate's technical skills, programming languages, and projects mentioned in their resume. " +
                "If they answer, give a very brief 1-sentence feedback and ask the next technical question. ";
                
        if (!resumeText.isEmpty()) {
            systemPrompt += "The candidate's resume text is provided below. Base ALL your questions on their specific technical skills and projects.\n\n" +
                            "--- RESUME ---\n" + resumeText + "\n--- END RESUME ---\n";
        }

        Map<String, Object> body = new HashMap<>();
       body.put("model", "meta-llama/Meta-Llama-3-8B-Instruct");
       
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        
        if (history != null && !history.isEmpty()) {
            messages.addAll(history);
        }
        
        messages.add(Map.of("role", "user", "content", prompt));
        
        body.put("messages", messages);
        body.put("max_tokens", 800);
        body.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            return "Error: Could not connect to AI service. " + e.getMessage();
        }

        return "I'm sorry, I couldn't generate a response right now.";
    }
}
