package com.example.demo.service;

import com.example.demo.entity.Pharmecy;
import com.example.demo.repository.PharmecyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmecyService {
    @Autowired
    private PharmecyRepository repository;

    public Pharmecy createPharmecy(Pharmecy pharmecy){
        return repository.save(pharmecy);
    }
    public List<Pharmecy> createPharmecies(List<Pharmecy> pharmecies){
        return repository.saveAll(pharmecies);
    }
    public List<Pharmecy> getPharmecies(){
        return repository.findAll();
    }
}
