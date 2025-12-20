package com.example.demo.repository;

import com.example.demo.entity.PharmacyBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacyBranchRepository extends JpaRepository<PharmacyBranch, Long> {
    List<PharmacyBranch> findByPharmacyId(Long pharmacyId);
    List<PharmacyBranch> findByIsActiveTrue();
}

