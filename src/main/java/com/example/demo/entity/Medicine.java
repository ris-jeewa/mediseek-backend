package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "generic_name", nullable = false)
    private String genericName;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "dosage_form", nullable = false)
    private String dosageForm;

    @Column(name = "strength")
    private String strength;

    @Column(name = "category")
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public Medicine() {
    }

    public Medicine(Long id, String genericName, String brand, String dosageForm,
                   String strength, String category, String description) {
        this.id = id;
        this.genericName = genericName;
        this.brand = brand;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.category = category;
        this.description = description;
    }

  
}
