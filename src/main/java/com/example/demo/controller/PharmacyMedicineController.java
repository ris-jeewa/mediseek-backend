package com.example.demo.controller;

import com.example.demo.entity.PharmacyMedicine;
import com.example.demo.repository.PharmacyMedicineRepository;
import com.example.demo.service.PharmacyMedicineService;
import com.example.demo.supportingEntities.PharmacyMedicineId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pharmamed")
public class PharmacyMedicineController {

    @Autowired
//    private PharmacyMedicineService pharmacyMedicineService;
    private PharmacyMedicineRepository repository;

//    @GetMapping
//    public List<PharmacyMedicine> getAll(){
//        return repository.findAll();
//    }
    @GetMapping("/{medicineId}")
    public List<PharmacyMedicine> getIds(@PathVariable int medicineId){
        return repository.findAllById_MedicineId(medicineId);
    }
//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<PharmacyMedicine>> getPharmacyMedicineById(@PathVariable PharmacyMedicineId id){
//        return ResponseEntity.ok(pharmacyMedicineService.getPharmacyIds(id));
//    }

}
