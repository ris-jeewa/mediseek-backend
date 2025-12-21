package com.example.demo.controller;

import com.example.demo.entity.BranchMedicine;
import com.example.demo.repository.BranchMedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/branch-medicine")
public class BranchMedicineController {

    @Autowired
    private BranchMedicineRepository repository;

    @GetMapping("/{medicineId}")
    public List<Long> getBranchIds(@PathVariable Long medicineId){
        List<BranchMedicine> list = repository.findAllById_MedicineId(medicineId);
        List<Long> ids = new ArrayList<>();
        if (list != null){
            for (BranchMedicine bm : list){
                ids.add(bm.getId().getBranchId());
            }
        }
        return ids;
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<BranchMedicine>> getByBranchId(@PathVariable Long branchId){
        return ResponseEntity.ok(repository.findAllById_BranchId(branchId));
    }

    @GetMapping("/medicine/{medicineId}")
    public ResponseEntity<List<BranchMedicine>> getByMedicineId(@PathVariable Long medicineId){
        return ResponseEntity.ok(repository.findAllById_MedicineId(medicineId));
    }
}

