package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Doctor;
import com.example.demo.repository.DoctorRepository;

public class DoctorService {

    @Autowired
    public DoctorRepository doctorRepository;

    public List<Doctor> getDoctorsBySpecialty(String specialty){
        return doctorRepository.findBySpecialtyContainingIgnoreCase(specialty);
    }


    

    
}
