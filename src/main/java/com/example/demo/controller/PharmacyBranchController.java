package com.example.demo.controller;

import com.example.demo.dto.BranchDTO;
import com.example.demo.dto.PharmacyBranchDTO;
import com.example.demo.entity.PharmacyBranch;
import com.example.demo.service.PharmacyBranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy-branch")
public class PharmacyBranchController {
    @Autowired
    private PharmacyBranchService service;

    @PostMapping("/create")
    public ResponseEntity<PharmacyBranch> create(@RequestBody PharmacyBranch branch){
        return ResponseEntity.ok(service.createBranch(branch));
    }
    
    @PostMapping("/createbranches")
    public ResponseEntity<List<PharmacyBranch>> createBranches(@RequestBody List<PharmacyBranch> branches){
        return ResponseEntity.ok(service.createBranches(branches));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PharmacyBranchDTO>> getAllBranches() {
        return ResponseEntity.ok(service.getAllBranches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyBranchDTO> getBranchById(@PathVariable Long id){
        return ResponseEntity.ok(service.getBranchById(id));
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    public ResponseEntity<List<BranchDTO>> getBranchesByPharmacyId(@PathVariable Long pharmacyId){
        return ResponseEntity.ok(service.getBranchesByPharmacyId(pharmacyId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PharmacyBranchDTO>> getActiveBranches(){
        return ResponseEntity.ok(service.getActiveBranches());
    }
}

