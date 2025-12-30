package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class HospitalDTO {
    private String code;
    private String name;
    private Double latitude;
    private Double longitude;
    private Boolean closed;
    private Boolean isPrivate;
}
