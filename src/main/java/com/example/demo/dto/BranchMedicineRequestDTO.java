package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BranchMedicineRequestDTO {
    private Long branchId;
    private Long medicineId;
    private BigDecimal price;
    private Integer stockQuantity;
}


