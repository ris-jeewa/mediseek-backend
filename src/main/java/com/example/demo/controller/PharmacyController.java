package com.example.demo.controller;

import com.example.demo.entity.Pharmacy;
import com.example.demo.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
public class PharmacyController {
    @Autowired
    private PharmacyService service;

    @PostMapping("/create")
    public ResponseEntity<Pharmacy> create(@RequestBody Pharmacy pharmacy){
        return ResponseEntity.ok(service.createPharmacy(pharmacy));
    }
    
    @PostMapping("/createpharmacies")
    public ResponseEntity<List<Pharmacy>> createPharmacies(@RequestBody List<Pharmacy> pharmacies){
        return ResponseEntity.ok(service.createPharmacies(pharmacies));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Pharmacy>> getPharmacies() {
        return ResponseEntity.ok(service.getPharmacies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pharmacy> getPharmacyById(@PathVariable Long id){
        return ResponseEntity.ok(service.getPharmacyById(id));
    }
}
