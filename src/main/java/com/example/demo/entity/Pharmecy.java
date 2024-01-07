package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Pharmecy {
    @Column
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String location;

    @Column
    private String contact;

    public Pharmecy(String name,String location, String contact) {
        this.name = name;
        this.location = location;
        this.contact = contact;
    }

    public Pharmecy() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
