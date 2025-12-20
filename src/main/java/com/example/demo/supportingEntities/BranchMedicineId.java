package com.example.demo.supportingEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BranchMedicineId implements Serializable {

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "medicine_id")
    private Long medicineId;

    public BranchMedicineId() {
    }

    public BranchMedicineId(Long branchId, Long medicineId) {
        this.branchId = branchId;
        this.medicineId = medicineId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchMedicineId that = (BranchMedicineId) o;
        return Objects.equals(branchId, that.branchId) &&
               Objects.equals(medicineId, that.medicineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchId, medicineId);
    }
}

