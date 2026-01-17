package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DoctorHospital;

@Repository
public interface DoctorHospitalRepository extends JpaRepository<DoctorHospital, Long> {

    // Find all hospital associations for a specific doctor
    List<DoctorHospital> findByDoctorId(Long doctorId);
}
