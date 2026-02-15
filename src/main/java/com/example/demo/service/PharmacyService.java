package com.example.demo.service;

import com.example.demo.entity.Pharmacy;
import com.example.demo.entity.PharmacyBranch;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.IdNotFoundException;
import com.example.demo.messaging.AppEvent;
import com.example.demo.messaging.KafkaProducerService;
import com.example.demo.repository.BranchMedicineRepository;
import com.example.demo.repository.PharmacyBranchRepository;
import com.example.demo.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PharmacyService {
    @Autowired
    private PharmacyRepository repository;

    @Autowired
    private BranchMedicineRepository branchMedicineRepository;

    @Autowired
    private PharmacyBranchRepository pharmacyBranchRepository;

    @Autowired
    private BranchMedicineService branchMedicineService;

    @Autowired(required = false)
    private KafkaProducerService kafkaProducer;

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
        Pharmacy saved = repository.save(pharmacy);
        if (kafkaProducer != null) {
            kafkaProducer.publishPharmacyEvent(AppEvent.of(saved.getId().toString(), "PHARMACY_CREATED", saved));
        }
        return saved;
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
        List<Pharmacy> saved = repository.saveAll(pharmacies);
        if (kafkaProducer != null) {
            for (Pharmacy p : saved) {
                kafkaProducer.publishPharmacyEvent(AppEvent.of(p.getId().toString(), "PHARMACY_CREATED", p));
            }
        }
        return saved;
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

    @Transactional(readOnly = true)
    public List<Pharmacy> getPharmaciesByMedicineId(Long medicineId) {
        // Get all BranchMedicine records for the given medicineId
        // List<BranchMedicine> branchMedicines = branchMedicineRepository.findAllById_MedicineId(medicineId);
        
        // if (branchMedicines.isEmpty()) {
        //     throw new ResourceNotFoundException("No branches found for medicine with id " + medicineId);
        // }

        // // Extract unique branch IDs
        // Set<Long> branchIds = branchMedicines.stream()
        //         .map(bm -> bm.getId().getBranchId())
        //         .collect(Collectors.toSet());

        List<Long> branchIds = branchMedicineService.getBranchIdsByMedicineId(medicineId);

        // Get all PharmacyBranch records for those branch IDs
        List<PharmacyBranch> branches = pharmacyBranchRepository.findAllById(branchIds);

        // Extract unique pharmacy IDs
        Set<Long> pharmacyIds = branches.stream()
                .map(branch -> branch.getPharmacy().getId())
                .collect(Collectors.toSet());

        // Get all Pharmacy entities for those pharmacy IDs
        List<Pharmacy> pharmacies = repository.findAllById(pharmacyIds);

        return pharmacies;
    }
}
