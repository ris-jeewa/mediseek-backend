package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.HospitalCreateRequest;
import com.example.demo.dto.PaginatedHospitalResponse;
import com.example.demo.entity.Hospital;
import com.example.demo.service.HospitalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hospital")
public class HospitalController {
       
    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/getAll")
    public ResponseEntity<PaginatedHospitalResponse> getHospitals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hospitalService.getAllHospoHospitals(page, size));
    }

    @PostMapping("/create")
    public ResponseEntity<Hospital> createHospital(@Valid @RequestBody HospitalCreateRequest request) {
        Hospital hospital = hospitalService.createHospital(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(hospital);
    }

    @PostMapping("/createMultiple")
    public ResponseEntity<List<Hospital>> createHospitals(@Valid @RequestBody List<HospitalCreateRequest> requests) {
        List<Hospital> created = hospitalService.createHospitals(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
