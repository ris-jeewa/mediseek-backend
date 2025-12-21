package com.example.demo.controller;

import com.example.demo.entity.Medicine;
import com.example.demo.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicine")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    @PostMapping("/create")
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine){
        return ResponseEntity.ok(medicineService.createMedicine(medicine));
    }

    @PostMapping("/createmany")
    public ResponseEntity<List<Medicine>> createMany(@RequestBody List<Medicine> medicines){
        return ResponseEntity.ok(medicineService.createMedicines(medicines));
    }

    @GetMapping("/getmedicines")
    public ResponseEntity<List<Medicine>> findAll(){
        return ResponseEntity.ok(medicineService.getMedicineList());
    }

    @GetMapping("/getmedicine/{id}")
    public ResponseEntity<Medicine> findById(@PathVariable Long id){
        return ResponseEntity.ok((medicineService.getMedicineById(id)));
    }

    @PutMapping("/updatemedicine")
    public ResponseEntity<Medicine> updateMedicine(@RequestBody Medicine medicine){
        return ResponseEntity.ok().body(medicineService.updateMedicine(medicine));
    }

    @PatchMapping("/update")
    public ResponseEntity<Medicine> updateMedicineById(@RequestBody Medicine medicine){
        return ResponseEntity.ok().body(medicineService.updateMedicineById(medicine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicine(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.deleteById(id));
    }

    @GetMapping("/search")
    public List<Medicine> search(@RequestParam String q) {
        return medicineService.searchMedicine(q);
    }
}
