package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalApiDTO {
    @JsonProperty("reporting_unit_code")
    private String reportingUnitCode;

    @JsonProperty("reporting_unit_name")
    private String reportingUnitName;

    private Double latitude;
    private Double longitude;

    private Boolean closed;

    @JsonProperty("private")
    private Boolean isPrivate; 
}
