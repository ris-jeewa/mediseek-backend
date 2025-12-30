package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalResultDTO {
    private Double latitude;
    private Double longitude;
    private Boolean closed;

    @JsonProperty("private")
    private Boolean isPrivate;

    @JsonProperty("mapped_reporting_units")
    private List<MappedReportingUnitDTO> mappedReportingUnits;
}

