package com.example.demo.service;

import com.example.demo.entity.BranchMedicine;
import com.example.demo.repository.BranchMedicineRepository;
import com.example.demo.supportingEntities.BranchMedicineId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchMedicineService {
    @Autowired
    private BranchMedicineRepository repository;

    public Optional<BranchMedicine> getBranchMedicineById(BranchMedicineId id){
        return repository.findById(id);
    }

    public List<BranchMedicine> getByMedicineId(Long medicineId){
        return repository.findAllById_MedicineId(medicineId);
    }

    public List<BranchMedicine> getByBranchId(Long branchId){
        return repository.findAllById_BranchId(branchId);
    }

    public BranchMedicine createBranchMedicine(BranchMedicine branchMedicine){
        return repository.save(branchMedicine);
    }

    public List<BranchMedicine> getAllBranchMedicines(){
        return repository.findAll();
    }
}

