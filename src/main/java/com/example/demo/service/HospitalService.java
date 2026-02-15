package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.HospitalCreateRequest;
import com.example.demo.dto.PaginatedHospitalResponse;
import com.example.demo.entity.Hospital;
import com.example.demo.messaging.AppEvent;
import com.example.demo.messaging.KafkaProducerService;
import com.example.demo.repository.HospitalRepository;

@Service
public class HospitalService {
    // private final HospitalApiRepository repository;

    // public HospitalService(HospitalApiRepository repository) {
    //     this.repository = repository;
    // }

    // public PaginatedHospitalResponse getHospitals(int page, int size) {
    //     // Validate pagination parameters
    //     if (page < 0) {
    //         page = 0;
    //     }
    //     if (size <= 0) {
    //         size = 10; // Default page size
    //     }

    //     List<HospitalApiDTO> all = repository.fetchHospitals();

    //     if (all == null || all.isEmpty()) {
    //         return new PaginatedHospitalResponse(List.of(), page, size, 0, 0);
    //     }

    //     int totalElements = all.size();
    //     int totalPages = (int) Math.ceil((double) totalElements / size);

    //     int fromIndex = page * size;
    //     int toIndex = Math.min(fromIndex + size, totalElements);

    //     if (fromIndex >= totalElements) {
    //         return new PaginatedHospitalResponse(List.of(), page, size, totalElements, totalPages);
    //     }

    //     List<HospitalDTO> pagedData = all.subList(fromIndex, toIndex)
    //             .stream()
    //             .map(h -> new HospitalDTO(
    //                     h.getReportingUnitCode(),
    //                     h.getReportingUnitName(),
    //                     h.getLatitude(),
    //                     h.getLongitude(),
    //                     h.getClosed(),
    //                     h.getIsPrivate()
    //             ))
    //             .toList();

    //     return new PaginatedHospitalResponse(
    //             pagedData, page, size, totalElements, totalPages
    //     );
    // }

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired(required = false)
    private KafkaProducerService kafkaProducer;

    public PaginatedHospitalResponse getAllHospoHospitals(int page, int size){
                // Validate pagination parameters
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10; // Default page size
        }

        List<Hospital> hospitals = hospitalRepository.findAll();

        if (hospitals == null || hospitals.isEmpty()) {
            return new PaginatedHospitalResponse(List.of(), page, size, 0, 0);
        }

        int totalElements = hospitals.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        if (fromIndex >= totalElements) {
            return new PaginatedHospitalResponse(List.of(), page, size, totalElements, totalPages);
        }

        List<Hospital> pagedData = hospitals.subList(fromIndex, toIndex);

        return new PaginatedHospitalResponse(
                pagedData, page, size, totalElements, totalPages
        );
    }

    @Transactional
    public Hospital createHospital(HospitalCreateRequest request) {
        Hospital saved = hospitalRepository.save(toEntity(request));
        if (kafkaProducer != null) {
            kafkaProducer.publishHospitalEvent(AppEvent.of(saved.getId().toString(), "HOSPITAL_CREATED", saved));
        }
        return saved;
    }

    @Transactional
    public List<Hospital> createHospitals(List<HospitalCreateRequest> requests) {
        List<Hospital> hospitals = requests.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        List<Hospital> saved = hospitalRepository.saveAll(hospitals);
        if (kafkaProducer != null) {
            for (Hospital h : saved) {
                kafkaProducer.publishAppEvent(AppEvent.of(h.getId().toString(), "HOSPITAL_CREATED", h));
            }
        }
        return saved;
    }

    private Hospital toEntity(HospitalCreateRequest req) {
        Hospital h = new Hospital();
        h.setName(req.getName());
        h.setType(req.getType());
        h.setRating(req.getRating());
        h.setOpenHours(req.getOpenHours());
        h.setMap(req.getMap());
        h.setTelephone(req.getTelephone());
        return h;
    }
}
