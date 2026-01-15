package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.DoctorHospital;
import com.example.demo.entity.Hospital;
import com.example.demo.repository.DoctorHospitalRepository;
import com.example.demo.repository.DoctorRepository;

@Service
public class DoctorHospitalService {

    @Autowired
    private DoctorHospitalRepository doctorHospitalRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Hospital> getHospitalsByDoctorId(Long doctorId) {
        // Get doctor info
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));

        // Get all hospital associations for this doctor
        List<DoctorHospital> doctorHospitals = doctorHospitalRepository.findByDoctorId(doctorId);

        List<Hospital> hospitals = new ArrayList<>();
            
        // Map hospitals to DTOs
        for (DoctorHospital dh : doctorHospitals) {
            Hospital hospital = dh.getHospital();
            hospitals.add(hospital);
        }

        return hospitals;
    }
}
