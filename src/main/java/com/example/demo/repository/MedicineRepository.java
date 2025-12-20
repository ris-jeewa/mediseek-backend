package com.example.demo.repository;

import com.example.demo.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByBrand(String brand);
    List<Medicine> findByGenericName(String genericName);
    List<Medicine> findByGenericNameContainingIgnoreCaseOrNameContainingIgnoreCase(
        String genericName,
        String name
    );
    List<Medicine> findByCategory(String category);
}


