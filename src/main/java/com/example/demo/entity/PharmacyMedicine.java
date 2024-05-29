package com.example.demo.entity;

import com.example.demo.supportingEntities.PharmacyMedicineId;
import jakarta.persistence.*;

@Entity
@Table
public class PharmacyMedicine {
    @EmbeddedId
    private PharmacyMedicineId id ;

    public PharmacyMedicine() {
    }

    public PharmacyMedicine(PharmacyMedicineId id) {
        this.id = id;
    }

    public PharmacyMedicineId getId() {
        return id;
    }

    public void setId(PharmacyMedicineId id) {
        this.id = id;
    }
}
