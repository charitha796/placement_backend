package com.example.intern_backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AIServiceTest {

    @Autowired
    private AIService aiService;

    @Test
    void testServiceLoad() {
        assertNotNull(aiService);
    }
}
