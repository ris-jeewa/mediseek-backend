package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Hospital;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginatedHospitalResponse {
    private List<Hospital> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
