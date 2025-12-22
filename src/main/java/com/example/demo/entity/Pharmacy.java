package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Pharmacy")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PharmacyBranch> branches;

    public Pharmacy() {
    }

    public Pharmacy(Long id, String name, String registrationNumber, String contactNumber, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.contactNumber = contactNumber;
        this.isActive = isActive;
    }

   
}
