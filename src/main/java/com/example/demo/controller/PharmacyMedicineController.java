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

import java.util.ArrayList;
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
    public List<Integer> getIds(@PathVariable int medicineId){
        List<PharmacyMedicine> list = repository.findAllById_MedicineId(medicineId);
        List<Integer> ids = new ArrayList<Integer>();
        if (list != null){
            for (PharmacyMedicine pm : list){
                ids.add(pm.getId().getPharmacyId());
            }
        }
        return ids;
        
    }
//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<PharmacyMedicine>> getPharmacyMedicineById(@PathVariable PharmacyMedicineId id){
//        return ResponseEntity.ok(pharmacyMedicineService.getPharmacyIds(id));
//    }

}
