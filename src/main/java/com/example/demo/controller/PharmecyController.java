package com.example.demo.controller;

import com.example.demo.entity.Pharmecy;
import com.example.demo.service.PharmecyService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PharmecyController {
    @Autowired
    private PharmecyService service;

    @PostMapping("/pharmecy")
    public ResponseEntity<Pharmecy> create(@RequestBody Pharmecy pharmecy){
        return ResponseEntity.ok(service.createPharmecy(pharmecy));
    }
    @PostMapping("/pharmecies")
    public ResponseEntity<List<Pharmecy>> createPharmecies(@RequestBody List<Pharmecy> pharmecies){
        return ResponseEntity.ok(service.createPharmecies(pharmecies));
    }

    @GetMapping("/pharmecy")
    public ResponseEntity<List<Pharmecy>> getPharmecies(){
        return ResponseEntity.ok(service.getPharmecies());
    }


}
