package com.example.demo.messaging;

import com.example.demo.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * Consumes domain events from Kafka.
 * <p>
 * Per-topic listeners use one consumer group each for domain-specific reactions.
 * {@link #consumeCrossServiceEvent} uses a single group and subscribes to every topic so
 * cross-cutting systems (audit, analytics, notifications, correlation) see all events.
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaConsumerService {

    private static final String CROSS_SERVICE_GROUP = "mediseek-cross-service-group";

    /**
     * Receives every event from all service domains. Add shared reactions here
     * (must stay aligned with {@link KafkaConfig#ALL_EVENT_TOPICS}).
     */
    @KafkaListener(
            topics = {
                    KafkaConfig.TOPIC_APP_EVENTS,
                    KafkaConfig.TOPIC_HOSPITAL_EVENTS,
                    KafkaConfig.TOPIC_PHARMACY_EVENTS,
                    KafkaConfig.TOPIC_MEDICINE_EVENTS,
                    KafkaConfig.TOPIC_DOCTOR_EVENTS
            },
            groupId = CROSS_SERVICE_GROUP)
    public void consumeCrossServiceEvent(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Kafka [cross-service] topic={} payload={}", topic, message);
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_APP_EVENTS, groupId = "mediseek-app-group")
    public void consumeAppEvent(String message) {
        log.info("Kafka Received app event: {}", message);
        // Add your processing logic here
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_HOSPITAL_EVENTS, groupId = "mediseek-hospital-group")
    public void consumeHospitalEvent(String message) {
        log.info("Kafka Received hospital event: {}", message);
        // Add your processing logic here
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_PHARMACY_EVENTS, groupId = "mediseek-pharmacy-group")
    public void consumePharmacyEvent(String message) {
        log.info("Kafka Received pharmacy event: {}", message);
        // Add your processing logic here
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_MEDICINE_EVENTS, groupId = "mediseek-medicine-group")
    public void consumeMedicineEvent(String message) {
        log.info("Kafka Received medicine event: {}", message);
        // Add your processing logic here
    }

    @KafkaListener(topics = KafkaConfig.TOPIC_DOCTOR_EVENTS, groupId = "mediseek-doctor-group")
    public void consumeDoctorEvent(String message) {
        log.info("Kafka Received doctor event: {}", message);
        // Add your processing logic here
    }
}
