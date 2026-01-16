package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PaginatedHospitalResponse;
import com.example.demo.service.HospitalService;

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
}
