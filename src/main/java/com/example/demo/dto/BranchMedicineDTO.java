package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchMedicineDTO {
    private Long id;
    private Long branchId;
    private Long medicineId;
    private String genericName;
    private String brand;
    private String dosageForm;
    private String strength;
    private String category;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private LocalDateTime lastUpdated;
}

