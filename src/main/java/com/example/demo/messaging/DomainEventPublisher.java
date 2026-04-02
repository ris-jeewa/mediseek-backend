package com.example.demo.messaging;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Single entry point for domain events. When {@code spring.kafka.bootstrap-servers} is set,
 * events are published to Kafka; otherwise calls are no-ops so all services behave the same
 * in dev (no broker) and prod (with broker).
 */
@Service
public class DomainEventPublisher {

    private final Optional<KafkaProducerService> kafkaProducer;

    public DomainEventPublisher(Optional<KafkaProducerService> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    /** True when Kafka producer is active; use for health checks and tests. */
    public boolean isKafkaEnabled() {
        return kafkaProducer.isPresent();
    }

    public void publishAppEvent(AppEvent event) {
        kafkaProducer.ifPresent(k -> k.publishAppEvent(event));
    }

    public void publishHospitalEvent(AppEvent event) {
        kafkaProducer.ifPresent(k -> k.publishHospitalEvent(event));
    }

    public void publishPharmacyEvent(AppEvent event) {
        kafkaProducer.ifPresent(k -> k.publishPharmacyEvent(event));
    }

    public void publishMedicineEvent(AppEvent event) {
        kafkaProducer.ifPresent(k -> k.publishMedicineEvent(event));
    }

    public void publishDoctorEvent(AppEvent event) {
        kafkaProducer.ifPresent(k -> k.publishDoctorEvent(event));
    }
}
