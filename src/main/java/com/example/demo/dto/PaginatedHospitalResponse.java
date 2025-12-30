package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginatedHospitalResponse {
    private List<HospitalDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
