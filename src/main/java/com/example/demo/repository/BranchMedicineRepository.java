package com.example.demo.repository;

import com.example.demo.entity.BranchMedicine;
import com.example.demo.supportingEntities.BranchMedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchMedicineRepository extends JpaRepository<BranchMedicine, BranchMedicineId> {
    List<BranchMedicine> findAllById_MedicineId(Long medicineId);
    List<BranchMedicine> findAllById_BranchId(Long branchId);
}

