package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    private Long id;
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
