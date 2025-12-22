package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyBranchDTO {
    private Long id;
    private Long pharmacy_id;
    private String branchName;
    private String address;
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String mapsUrl;
    private String contactNumber;
    private String openingHours;
    private Boolean isActive;
}

