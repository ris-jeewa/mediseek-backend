package com.example.demo.repository;

import com.example.demo.entity.PharmacyMedicine;
import com.example.demo.supportingEntities.PharmacyMedicineId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacyMedicineRepository extends JpaRepository<PharmacyMedicine, PharmacyMedicineId> {

    List<PharmacyMedicine> findAllById_MedicineId(int medicineId);
}
