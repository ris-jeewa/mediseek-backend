package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Type is required")
    private String type;

    private Double rating;

    @NotBlank(message = "Open hours are required")
    private String openHours;

    @NotBlank(message = "Map is required")
    private String map;

    @NotBlank(message = "Telephone is required")
    private String telephone;
}
