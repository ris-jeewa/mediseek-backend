package com.example.demo.service;

import com.example.demo.entity.Pharmacy;
import com.example.demo.entity.Pharmacy;
import com.example.demo.exception.IdNotFoundException;
import com.example.demo.repository.PharmacyRepository;
import com.example.demo.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PharmacyService {
    @Autowired
    private PharmacyRepository repository;

    public Pharmacy createPharmacy(Pharmacy pharmacy){
        return repository.save(pharmacy);
    }
    public List<Pharmacy> createPharmacies(List<Pharmacy> pharmacies){
        return repository.saveAll(pharmacies);
    }
    public List<Pharmacy> getPharmacies(){
        return repository.findAll();
    }

    public Pharmacy getPharmacyById(int id){
        return repository.findById(id).orElse(null);
    }

    public Pharmacy updatePharmacy(Pharmacy pharmacy){
        Optional<Pharmacy> foundPharmacy = repository.findById(pharmacy.getId());
        if(foundPharmacy.isPresent()){
            Pharmacy updatedPharmacy = foundPharmacy.get();
            updatedPharmacy.setName(pharmacy.getName());
            updatedPharmacy.setLocation(pharmacy.getLocation());
            updatedPharmacy.setContactNum(pharmacy.getContactNum());
            updatedPharmacy.setOpenHours(pharmacy.getOpenHours());
            updatedPharmacy.setStock(pharmacy.getStock());

            return repository.save(pharmacy);
        }else  {
            throw new IdNotFoundException("Invalid pharmacy Id");
        }
    }
}
