package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Pharmacy_Branch")
@Getter
@Setter
public class PharmacyBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "maps_url", columnDefinition = "TEXT")
    private String mapsUrl;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BranchMedicine> medicines;

    public PharmacyBranch() {
    }

    public PharmacyBranch(Long id, Pharmacy pharmacy, String branchName, String address, String city,
                         BigDecimal latitude, BigDecimal longitude, String mapsUrl, String contactNumber,
                         String openingHours, Boolean isActive) {
        this.id = id;
        this.pharmacy = pharmacy;
        this.branchName = branchName;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mapsUrl = mapsUrl;
        this.contactNumber = contactNumber;
        this.openingHours = openingHours;
        this.isActive = isActive;
    }
}

