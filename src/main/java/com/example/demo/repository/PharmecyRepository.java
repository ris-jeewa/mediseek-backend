package com.example.demo.repository;

import com.example.demo.entity.Pharmecy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmecyRepository extends JpaRepository<Pharmecy,Integer> {
}
