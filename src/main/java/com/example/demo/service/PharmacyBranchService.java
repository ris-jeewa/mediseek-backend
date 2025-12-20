package com.example.demo.service;

import com.example.demo.entity.PharmacyBranch;
import com.example.demo.exception.IdNotFoundException;
import com.example.demo.repository.PharmacyBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PharmacyBranchService {
    @Autowired
    private PharmacyBranchRepository repository;

    public PharmacyBranch createBranch(PharmacyBranch branch){
        return repository.save(branch);
    }

    public List<PharmacyBranch> createBranches(List<PharmacyBranch> branches){
        return repository.saveAll(branches);
    }

    public List<PharmacyBranch> getAllBranches(){
        return repository.findAll();
    }

    public PharmacyBranch getBranchById(Long id){
        return repository.findById(id).orElse(null);
    }

    public List<PharmacyBranch> getBranchesByPharmacyId(Long pharmacyId){
        return repository.findByPharmacyId(pharmacyId);
    }

    public List<PharmacyBranch> getActiveBranches(){
        return repository.findByIsActiveTrue();
    }

    public PharmacyBranch updateBranch(PharmacyBranch branch){
        Optional<PharmacyBranch> foundBranch = repository.findById(branch.getId());
        if(foundBranch.isPresent()){
            PharmacyBranch updatedBranch = foundBranch.get();
            updatedBranch.setBranchName(branch.getBranchName());
            updatedBranch.setAddress(branch.getAddress());
            updatedBranch.setCity(branch.getCity());
            updatedBranch.setLatitude(branch.getLatitude());
            updatedBranch.setLongitude(branch.getLongitude());
            updatedBranch.setMapsUrl(branch.getMapsUrl());
            updatedBranch.setContactNumber(branch.getContactNumber());
            updatedBranch.setOpeningHours(branch.getOpeningHours());
            updatedBranch.setIsActive(branch.getIsActive());

            return repository.save(updatedBranch);
        }else {
            throw new IdNotFoundException("Invalid branch Id");
        }
    }
}

