package com.example.demo.controller;

import com.example.demo.dto.BranchMedicineDTO;
import com.example.demo.entity.BranchMedicine;
import com.example.demo.service.BranchMedicineService;
import com.example.demo.supportingEntities.BranchMedicineId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branch-medicine")
public class BranchMedicineController {

    @Autowired
    private BranchMedicineService service;

    @PostMapping("/create")
    public ResponseEntity<BranchMedicine> create(@RequestBody BranchMedicine branchMedicine){
        return ResponseEntity.ok(service.createBranchMedicine(branchMedicine));
    }

    @PutMapping("/update/{branchId}/{medicineId}")
    public ResponseEntity<BranchMedicine> update(
            @PathVariable Long branchId,
            @PathVariable Long medicineId,
            @RequestBody BranchMedicine branchMedicine){
        BranchMedicineId id = new BranchMedicineId(branchId, medicineId);
        return ResponseEntity.ok(service.updateBranchMedicine(id, branchMedicine));
    }

    @DeleteMapping("/{branchId}/{medicineId}")
    public ResponseEntity<String> delete(
            @PathVariable Long branchId,
            @PathVariable Long medicineId){
        return ResponseEntity.ok(service.deleteBranchMedicine(branchId, medicineId));
    }

    @GetMapping("/{medicineId}")
    public List<Long> getBranchIds(@PathVariable Long medicineId){
        return service.getBranchIdsByMedicineId(medicineId);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<BranchMedicineDTO>> getByBranchId(@PathVariable Long branchId){
        return ResponseEntity.ok(service.getBranchMedicinesByBranchId(branchId));
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<BranchMedicineDTO>> getByMedicineId(@PathVariable Long medicineId){
        return ResponseEntity.ok(service.getBranchMedicinesByMedicineId(medicineId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BranchMedicine>> getAllBranchMedicines(){
        return ResponseEntity.ok(service.getAllBranchMedicines());
    }
}

