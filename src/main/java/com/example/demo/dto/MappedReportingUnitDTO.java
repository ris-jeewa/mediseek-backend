package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MappedReportingUnitDTO {
    @JsonProperty("mapped_reporting_unit")
    private ReportingUnitDTO mappedReportingUnit;
}

