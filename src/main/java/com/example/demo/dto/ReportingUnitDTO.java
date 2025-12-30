package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportingUnitDTO {
    @JsonProperty("reporting_unit_code")
    private String reportingUnitCode;

    @JsonProperty("reporting_unit_name")
    private String reportingUnitName;

    @JsonProperty("reporting_unit_type")
    private ReportingUnitTypeDTO reportingUnitType;
}

