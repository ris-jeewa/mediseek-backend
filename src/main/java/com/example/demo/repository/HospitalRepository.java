package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital,Long> {
    
}
