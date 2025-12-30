package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportingUnitTypeDTO {
    @JsonProperty("reporting_unit_type_code")
    private String reportingUnitTypeCode;

    @JsonProperty("reporting_unit_type_name")
    private String reportingUnitTypeName;
}

