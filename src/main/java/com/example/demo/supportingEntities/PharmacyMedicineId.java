package com.example.demo.supportingEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jdk.jfr.DataAmount;

import java.io.Serializable;


@Embeddable
public class PharmacyMedicineId implements Serializable {

    private int pharmacyId;

    private int medicineId;

    public PharmacyMedicineId() {
    }

    public PharmacyMedicineId(int pharmacyId, int medicineId) {
        this.pharmacyId = pharmacyId;
        this.medicineId = medicineId;
    }

    public int getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(int pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }
}
