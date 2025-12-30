package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.HospitalApiDTO;
import com.example.demo.dto.HospitalDTO;
import com.example.demo.dto.PaginatedHospitalResponse;
import com.example.demo.repository.HospitalApiRepository;

@Service
public class HospitalService {
    private final HospitalApiRepository repository;

    public HospitalService(HospitalApiRepository repository) {
        this.repository = repository;
    }

    public PaginatedHospitalResponse getHospitals(int page, int size) {
        // Validate pagination parameters
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10; // Default page size
        }

        List<HospitalApiDTO> all = repository.fetchHospitals();

        if (all == null || all.isEmpty()) {
            return new PaginatedHospitalResponse(List.of(), page, size, 0, 0);
        }

        int totalElements = all.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        if (fromIndex >= totalElements) {
            return new PaginatedHospitalResponse(List.of(), page, size, totalElements, totalPages);
        }

        List<HospitalDTO> pagedData = all.subList(fromIndex, toIndex)
                .stream()
                .map(h -> new HospitalDTO(
                        h.getReportingUnitCode(),
                        h.getReportingUnitName(),
                        h.getLatitude(),
                        h.getLongitude(),
                        h.getClosed(),
                        h.getIsPrivate()
                ))
                .toList();

        return new PaginatedHospitalResponse(
                pagedData, page, size, totalElements, totalPages
        );
    }
}
