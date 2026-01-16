package com.example.demo.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.HospitalApiDTO;
import com.example.demo.dto.HospitalApiResponseDTO;
import com.example.demo.dto.HospitalResultDTO;
import com.example.demo.dto.MappedReportingUnitDTO;

@Repository
public class HospitalApiRepository {
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_URL =
        "https://myhospitalsapi.aihw.gov.au/api/v1/reporting-units";

    public List<HospitalApiDTO> fetchHospitals() {
        try {
            HospitalApiResponseDTO response =
                    restTemplate.getForObject(API_URL, HospitalApiResponseDTO.class);

            if (response == null || response.getResult() == null || response.getResult().isEmpty()) {
                return Collections.emptyList();
            }

            List<HospitalApiDTO> hospitals = new ArrayList<>();

            for (HospitalResultDTO result : response.getResult()) {
                if (result.getMappedReportingUnits() != null) {
                    // Filter for LHN type (Local Hospital Network)
                    List<MappedReportingUnitDTO> lhnUnits = result.getMappedReportingUnits().stream()
                            .filter(mru -> mru.getMappedReportingUnit() != null
                                    && mru.getMappedReportingUnit().getReportingUnitType() != null
                                    && "LHN".equals(mru.getMappedReportingUnit().getReportingUnitType().getReportingUnitTypeCode()))
                            .collect(Collectors.toList());

                    // Create HospitalApiDTO for each LHN unit
                    for (MappedReportingUnitDTO lhnUnit : lhnUnits) {
                        HospitalApiDTO hospital = new HospitalApiDTO();
                        hospital.setReportingUnitCode(lhnUnit.getMappedReportingUnit().getReportingUnitCode());
                        hospital.setReportingUnitName(lhnUnit.getMappedReportingUnit().getReportingUnitName());
                        hospital.setLatitude(result.getLatitude());
                        hospital.setLongitude(result.getLongitude());
                        hospital.setClosed(result.getClosed());
                        hospital.setIsPrivate(result.getIsPrivate());
                        hospitals.add(hospital);
                    }
                }
            }

            return hospitals;
        } catch (RestClientException e) {
            // Log the error
            System.err.println("Error fetching hospitals from external API: " + e.getMessage());
            e.printStackTrace();
            // Return empty list or throw a custom exception
            return Collections.emptyList();
        }
    }
}
