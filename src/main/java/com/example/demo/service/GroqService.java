package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.AnalzeWithDoctorsDTO;
import com.example.demo.dto.SymptomAnalysisResponse;
import com.example.demo.entity.Doctor;
import com.example.demo.messaging.AppEvent;
import com.example.demo.messaging.DomainEventPublisher;
import com.example.demo.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final DomainEventPublisher domainEventPublisher;

    public GroqService(ObjectMapper objectMapper, DomainEventPublisher domainEventPublisher) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
        this.domainEventPublisher = domainEventPublisher;
    }

    public AnalzeWithDoctorsDTO analyzeSymptoms(String symptoms) {
        String prompt = String.format(
                """
                                    You are a medical symptom analyzer assistant. Analyze the following symptoms and recommend the appropriate medical specialist(s).

                        Symptoms: "%s"

                        Respond ONLY with a valid JSON object in this exact format (no markdown, no code blocks):
                        {
                            "specialty": "comma-separated values from: cardiology, dermatology, neurology, oncology, urology, general surgery, pediatrics, gastroenterology",
                            "urgency": "one of: high, medium, low",
                            "explanation": "A brief 1-2 sentence explanation of why these specialist(s) are recommended",
                            "recommendations": ["recommendation 1", "recommendation 2", "recommendation 3"]
                        }

                        Rules:
                        - If more than one specialty is relevant, list them as a comma-separated string (e.g. "neurology, cardiology").
                        - If only one specialty applies, return a single value without extra commas.
                        - Return ONLY the JSON object, nothing else.

                                    """,
                symptoms);

        // Build request body for Groq API 
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)),
                "temperature", 0.7,
                "max_tokens", 500);

        try {
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Call Groq API
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class);

            AnalzeWithDoctorsDTO result = parseGroqResponse(response.getBody());
            int doctorCount = result.getDoctors() != null ? result.getDoctors().size() : 0;
            domainEventPublisher.publishAppEvent(AppEvent.of(
                    "symptom-analysis",
                    "SYMPTOM_ANALYSIS_COMPLETED",
                    Map.of("doctorCount", doctorCount)));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze symptoms: " + e.getMessage(), e);
        }
    }

    private AnalzeWithDoctorsDTO parseGroqResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);

            // Extract text from Groq response
            String text = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // Clean the response 
            text = text.replaceAll("```json\\s*", "")
                    .replaceAll("```\\s*", "")
                    .trim();

            // Parse JSON to DTO
            SymptomAnalysisResponse analyzeResponse = objectMapper.readValue(text, SymptomAnalysisResponse.class);

            List<String> specialties = Arrays.stream(analyzeResponse.getSpecialty().split(","))
                    .map(String::trim)
                    .toList();

            List<Doctor> doctors = new ArrayList<>();
            for (String specialty : specialties) {
                List<Doctor> foundDoctors = doctorRepository.findBySpecialtyContainingIgnoreCase(specialty);
                doctors.addAll(foundDoctors);
            }

            AnalzeWithDoctorsDTO analyzeWithDoctorsDTO = new AnalzeWithDoctorsDTO();
            analyzeWithDoctorsDTO.setDoctors(doctors);
            analyzeWithDoctorsDTO.setSymptomAnalysisResponse(analyzeResponse);
            return analyzeWithDoctorsDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response: " + e.getMessage(), e);
        }
    }
}
