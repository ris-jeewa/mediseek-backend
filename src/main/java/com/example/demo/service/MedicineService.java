package com.example.demo.service;

import com.example.demo.entity.Medicine;
import com.example.demo.exception.IdNotFoundException;
import com.example.demo.exception.IdNotFoundException;
import com.example.demo.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {
    @Autowired
    public MedicineRepository mediRepository;

    public Medicine createMedicine(Medicine medicine){
        return mediRepository.save(medicine);
    }

    public List<Medicine> createMedicines(List<Medicine> medicines) {return mediRepository.saveAll(medicines);}

    public List<Medicine> getMedicineList(){
        return mediRepository.findAll();
    }

    public Medicine getMedicineById(int id){
        return mediRepository.findById(id).orElse(null);
    }

    public Medicine updateMedicine(Medicine medicine){
        Optional<Medicine> foundMedi = mediRepository.findById(medicine.getId());
        if (foundMedi.isPresent()){
            Medicine updatedMedi = foundMedi.get();
            updatedMedi.setName(medicine.getName());
            updatedMedi.setPrice(medicine.getPrice());

            return mediRepository.save(medicine);
        }else {
            return null;
        }
    }

    public Medicine updateMedicineById(Medicine medicine){
        Optional <Medicine> foundMedi = mediRepository.findById(medicine.getId());

        if (foundMedi.isPresent()){
            Medicine updateMedi = foundMedi.get();

            if (medicine.getName().length() > 0 ){
                updateMedi.setName(medicine.getName());
            }
            updateMedi.setPrice(medicine.getPrice());

            if(medicine.getManufacturer().length() >0){
                updateMedi.setManufacturer(medicine.getManufacturer());
            }

            if (medicine.getDosage().length() > 0){
                updateMedi.setDosage(medicine.getDosage());

            }

            if (medicine.getIngredients().length() >0){
                updateMedi.setIngredients(medicine.getIngredients());


            }
            return mediRepository.save(medicine);
        }else {
            throw new IdNotFoundException("Medicine Id is not found");
        }
    }

    public String deleteById(int id) {
        mediRepository.deleteById(id);
        return "Medicine " + id + " is deleted.";
    }

    public List<Medicine> findMedicineByName(String brandName){
        return mediRepository.findByBrandName(brandName);
    }

}
