package com.example.demo.service;

import com.example.demo.entity.BranchMedicine;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BranchMedicineRepository;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.repository.PharmacyBranchRepository;
import com.example.demo.supportingEntities.BranchMedicineId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BranchMedicineService {
    @Autowired
    private BranchMedicineRepository repository;

    @Autowired
    private PharmacyBranchRepository branchRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    public List<Long> getBranchIdsByMedicineId(Long medicineId){
        List<BranchMedicine> list = repository.findAllById_MedicineId(medicineId);
        List<Long> ids = new ArrayList<>();
        if (list != null){
            for (BranchMedicine bm : list){
                ids.add(bm.getId().getBranchId());
            }
        }
        return ids;
    }

    public List<BranchMedicine> getBranchMedicinesByMedicineId(Long medicineId){
        return repository.findAllById_MedicineId(medicineId);
    }

    public List<BranchMedicine> getBranchMedicinesByBranchId(Long branchId){
        return repository.findAllById_BranchId(branchId);
    }
    
    public Optional<BranchMedicine> getBranchMedicineById(BranchMedicineId id){
        return repository.findById(id);
    }

    public BranchMedicine createBranchMedicine(BranchMedicine branchMedicine){
        // Validate branch exists
        Long branchId = branchMedicine.getBranch() != null ? branchMedicine.getBranch().getId() : null;
        if (branchId == null) {
            throw new ResourceNotFoundException("Branch ID is required");
        }
        
        // Fetch and validate branch
        var branch = branchRepository.findById(branchId)
            .orElseThrow(() -> new ResourceNotFoundException("Pharmacy branch with id " + branchId + " not found"));
        
        // Validate medicine exists
        Long medicineId = branchMedicine.getMedicine() != null ? branchMedicine.getMedicine().getId() : null;
        if (medicineId == null) {
            throw new ResourceNotFoundException("Medicine ID is required");
        }
        
        // Fetch and validate medicine
        var medicine = medicineRepository.findById(medicineId)
            .orElseThrow(() -> new ResourceNotFoundException("Medicine with id " + medicineId + " not found"));

        // Check if combination already exists
        BranchMedicineId id = new BranchMedicineId(branchId, medicineId);
        if (repository.existsById(id)) {
            throw new DuplicateResourceException("BranchMedicine with branchId " + branchId + " and medicineId " + medicineId + " already exists");
        }

        // Set the composite ID and ensure branch/medicine objects are set
        branchMedicine.setId(id);
        branchMedicine.setBranch(branch);
        branchMedicine.setMedicine(medicine);
        
        return repository.save(branchMedicine);
    }

    public BranchMedicine updateBranchMedicine(BranchMedicineId id, BranchMedicine branchMedicine){
        Optional<BranchMedicine> found = repository.findById(id);
        if (found.isPresent()) {
            BranchMedicine existing = found.get();
            
            // Update price if provided
            if (branchMedicine.getPrice() != null) {
                existing.setPrice(branchMedicine.getPrice());
            }
            
            // Update stock quantity if provided
            if (branchMedicine.getStockQuantity() != null) {
                existing.setStockQuantity(branchMedicine.getStockQuantity());
            }
            
            // lastUpdated is automatically set by @PreUpdate
            
            return repository.save(existing);
        } else {
            throw new ResourceNotFoundException("BranchMedicine with branchId " + id.getBranchId() + " and medicineId " + id.getMedicineId() + " not found");
        }
    }

    public String deleteBranchMedicine(Long branchId, Long medicineId){
        BranchMedicineId id = new BranchMedicineId(branchId, medicineId);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "BranchMedicine with branchId " + branchId + " and medicineId " + medicineId + " deleted successfully";
        } else {
            throw new ResourceNotFoundException("BranchMedicine with branchId " + branchId + " and medicineId " + medicineId + " not found");
        }
    }

    public List<BranchMedicine> getAllBranchMedicines(){
        return repository.findAll();
    }
}

