package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;

    @Column
    private String location;

    @Column
    private String contactNum;

    @Column
    private String openHours;

    @Column
    private  String stock;


    public Pharmacy() {
    }

    public Pharmacy(int id, String name, String location, String contactNum, String openHours, String stock) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.contactNum = contactNum;
        this.openHours = openHours;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
