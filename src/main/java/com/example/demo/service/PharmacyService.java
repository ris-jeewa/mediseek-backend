package com.example.demo.service;

import com.example.demo.entity.Pharmacy;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.IdNotFoundException;
import com.example.demo.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PharmacyService {
    @Autowired
    private PharmacyRepository repository;

    private void validateRegistrationNumber(String regNumber) {
        if (regNumber != null && !regNumber.trim().isEmpty() &&
                repository.existsByRegistrationNumber(regNumber.trim())) {
            throw new DuplicateResourceException(
                    "Pharmacy with registration number " + regNumber + " already exists");
        }
    }

    public Pharmacy createPharmacy(Pharmacy pharmacy) {
        // Check for duplicate registration number
        if (pharmacy.getRegistrationNumber() != null &&
                !pharmacy.getRegistrationNumber().trim().isEmpty()) {
            validateRegistrationNumber(pharmacy.getRegistrationNumber());
        }
        return repository.save(pharmacy);
    }

    public List<Pharmacy> createPharmacies(List<Pharmacy> pharmacies) {
        // Check for duplicates within the list itself
        Set<String> registrationNumbersInBatch = new HashSet<>();

        for (Pharmacy pharmacy : pharmacies) {
            String regNumber = pharmacy.getRegistrationNumber();

            // Skip validation if registration number is null or empty
            if (regNumber == null || regNumber.trim().isEmpty()) {
                continue;
            }

            String trimmedRegNumber = regNumber.trim();

            // Check for duplicates within the batch
            if (registrationNumbersInBatch.contains(trimmedRegNumber)) {
                throw new DuplicateResourceException(
                        "Duplicate registration number " + trimmedRegNumber + " found in the request");
            }
            registrationNumbersInBatch.add(trimmedRegNumber);

            // Check for duplicates in the database
            validateRegistrationNumber(trimmedRegNumber);
        }
        return repository.saveAll(pharmacies);
    }

    public List<Pharmacy> getPharmacies() {
        return repository.findAll();
    }

    public Pharmacy getPharmacyById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Pharmacy with id " + id + " not found"));
    }

    public Pharmacy updatePharmacy(Pharmacy pharmacy) {
        Optional<Pharmacy> foundPharmacy = repository.findById(pharmacy.getId());

        if (foundPharmacy.isPresent()) {
            Pharmacy updatedPharmacy = foundPharmacy.get();
            // Check if registration number is being changed and if new one already exists
            String newRegNumber = pharmacy.getRegistrationNumber();
            String currentRegNumber = updatedPharmacy.getRegistrationNumber();
            
            if (newRegNumber != null && !newRegNumber.trim().isEmpty() &&
                !newRegNumber.trim().equals(currentRegNumber != null ? currentRegNumber.trim() : "")) {
                // Registration number is being changed, check if new one exists
                validateRegistrationNumber(newRegNumber);
            }
            
            updatedPharmacy.setName(pharmacy.getName());
            updatedPharmacy.setRegistrationNumber(pharmacy.getRegistrationNumber());
            updatedPharmacy.setContactNumber(pharmacy.getContactNumber());
            updatedPharmacy.setIsActive(pharmacy.getIsActive());

            return repository.save(updatedPharmacy);
        } else {
            throw new IdNotFoundException("Invalid pharmacy Id");
        }
    }
}
