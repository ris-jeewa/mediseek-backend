package com.example.demo.service;

import com.example.demo.entity.Medicine;
import com.example.demo.exception.IdNotFoundException;
import com.example.demo.messaging.AppEvent;
import com.example.demo.messaging.DomainEventPublisher;
import com.example.demo.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {
    @Autowired
    public MedicineRepository mediRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public Medicine createMedicine(Medicine medicine){
        Medicine saved = mediRepository.save(medicine);
        domainEventPublisher.publishMedicineEvent(AppEvent.of(saved.getId().toString(), "MEDICINE_CREATED", saved));
        return saved;
    }

    public List<Medicine> createMedicines(List<Medicine> medicines) {
        List<Medicine> saved = mediRepository.saveAll(medicines);
        for (Medicine m : saved) {
            domainEventPublisher.publishMedicineEvent(AppEvent.of(m.getId().toString(), "MEDICINE_CREATED", m));
        }
        return saved;
    }

    public List<Medicine> getMedicineList(){
        return mediRepository.findAll();
    }

    public Medicine getMedicineById(Long id){
        return mediRepository.findById(id).orElse(null);
    }

    public Medicine updateMedicine(Medicine medicine){
        Optional<Medicine> foundMedi = mediRepository.findById(medicine.getId());
        if (foundMedi.isPresent()){
            Medicine updatedMedi = foundMedi.get();
            updatedMedi.setGenericName(medicine.getGenericName());
            updatedMedi.setBrand(medicine.getBrand());
            updatedMedi.setDosageForm(medicine.getDosageForm());
            updatedMedi.setStrength(medicine.getStrength());
            updatedMedi.setCategory(medicine.getCategory());
            updatedMedi.setDescription(medicine.getDescription());

            Medicine saved = mediRepository.save(updatedMedi);
            domainEventPublisher.publishMedicineEvent(AppEvent.of(saved.getId().toString(), "MEDICINE_UPDATED", saved));
            return saved;
        }else {
            return null;
        }
    }

    public Medicine updateMedicineById(Medicine medicine){
        Optional<Medicine> foundMedi = mediRepository.findById(medicine.getId());

        if (foundMedi.isPresent()){
            Medicine updateMedi = foundMedi.get();

            if (medicine.getGenericName() != null && medicine.getGenericName().length() > 0){
                updateMedi.setGenericName(medicine.getGenericName());
            }
            if (medicine.getBrand() != null && medicine.getBrand().length() > 0){
                updateMedi.setBrand(medicine.getBrand());
            }
            if (medicine.getDosageForm() != null && medicine.getDosageForm().length() > 0){
                updateMedi.setDosageForm(medicine.getDosageForm());
            }
            if (medicine.getStrength() != null && medicine.getStrength().length() > 0){
                updateMedi.setStrength(medicine.getStrength());
            }
            if (medicine.getCategory() != null && medicine.getCategory().length() > 0){
                updateMedi.setCategory(medicine.getCategory());
            }
            if (medicine.getDescription() != null && medicine.getDescription().length() > 0){
                updateMedi.setDescription(medicine.getDescription());
            }
            Medicine saved = mediRepository.save(updateMedi);
            domainEventPublisher.publishMedicineEvent(AppEvent.of(saved.getId().toString(), "MEDICINE_UPDATED", saved));
            return saved;
        }else {
            throw new IdNotFoundException("Medicine Id is not found");
        }
    }

    public String deleteById(Long id) {
        mediRepository.deleteById(id);
        domainEventPublisher.publishMedicineEvent(AppEvent.of(id.toString(), "MEDICINE_DELETED", null));
        return "Medicine " + id + " is deleted.";
    }

    public List<Medicine> findMedicineByBrand(String brand){
        return mediRepository.findByBrand(brand);
    }

    public List<Medicine> findMedicineByGenName(String genericName){
        return mediRepository.findByGenericName(genericName);
    }

    public List<Medicine> searchMedicine(String keyword) {
        return mediRepository
            .findByGenericNameContainingIgnoreCaseOrBrandContainingIgnoreCase(
                keyword, keyword
            );
    }

    public List<Medicine> findMedicineByCategory(String category) {
        return mediRepository.findByCategory(category);
    }
}
