package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AnalzeWithDoctorsDTO;
import com.example.demo.dto.SymptomAnalysisResponse;
import com.example.demo.dto.SymptomRequest;
import com.example.demo.service.GroqService;

@RestController
@RequestMapping("/api/symptoms")
public class SymptomAnalysisController {

    private final GroqService groqService;

    public SymptomAnalysisController(GroqService groqService) {
        this.groqService = groqService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalzeWithDoctorsDTO> analyzeSymptoms(@RequestBody SymptomRequest request) {
        try {
            AnalzeWithDoctorsDTO analysis = groqService.analyzeSymptoms(request.getSymptoms());
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
