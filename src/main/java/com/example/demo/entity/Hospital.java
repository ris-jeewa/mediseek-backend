package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Hospital {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;        // Government / Private

    @Column(name = "rating")
    private Double rating;

    @Column(name = "open_hours", nullable = false)
    private String openHours;   // e.g. "08:00 - 20:00"

    @Column(name = "map", nullable = false )
    private String map;         // Google Maps link or coordinates

    @Column(name = "telephone", nullable = false)
    private String telephone;

}
