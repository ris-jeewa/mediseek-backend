package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String brandName;

    @Column
    private String genericName;

    @Column
    private String manufacturer;

    @Column
    private String dosage;

    @Column
    private String ingredients;

    @Column
        private BigDecimal price;

    public Medicine() {
    }

    public Medicine(Integer id, String brandName,String genericName, String manufacturer, String dosage, String ingredients,BigDecimal price) {
        this.id = id;
        this.brandName = brandName;
        this.genericName = genericName;
        this.manufacturer = manufacturer;
        this.dosage = dosage;
        this.ingredients = ingredients;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return brandName;
    }

    public void setName(String brandName) {
        this.brandName = brandName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String toString(){
        return "Medicine{ id="+id+ "name"+brandName+"manufacturer="+manufacturer;
    }
}
