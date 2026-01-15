package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Doctor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalzeWithDoctorsDTO {
    SymptomAnalysisResponse symptomAnalysisResponse;
    List<Doctor> doctors;
    
}
