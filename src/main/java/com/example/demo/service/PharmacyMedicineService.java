package com.example.demo.service;

import com.example.demo.entity.PharmacyMedicine;
import com.example.demo.repository.PharmacyMedicineRepository;
import com.example.demo.supportingEntities.PharmacyMedicineId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PharmacyMedicineService {
    @Autowired
    private PharmacyMedicineRepository repository;

    public Optional<PharmacyMedicine> getPharmacyIds(PharmacyMedicineId medicine_id){
        return repository.findById(medicine_id);
    }
}
