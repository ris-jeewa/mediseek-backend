package com.example.demo.entity;

import com.example.demo.supportingEntities.BranchMedicineId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Branch_Medicine", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"branch_id", "medicine_id"})
})
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BranchMedicine {
    
    @EmbeddedId
    private BranchMedicineId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("branchId")
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private PharmacyBranch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicineId")
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public BranchMedicine() {
    }

    public BranchMedicine(BranchMedicineId id, PharmacyBranch branch, Medicine medicine,
                         BigDecimal price, Integer stockQuantity) {
        this.id = id;
        this.branch = branch;
        this.medicine = medicine;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = LocalDateTime.now();
    }

 
}

