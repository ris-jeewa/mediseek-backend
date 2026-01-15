package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DoctorHospitalsResponseDTO;
import com.example.demo.entity.Hospital;
import com.example.demo.service.DoctorHospitalService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorHospitalService doctorHospitalService;

    @GetMapping("/{doctorId}")
    public ResponseEntity<List<Hospital>> getDoctorWithHospitals(@PathVariable Long doctorId) {
        try {
            List<Hospital> response = doctorHospitalService.getHospitalsByDoctorId(doctorId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
