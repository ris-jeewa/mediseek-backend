package com.example.demo.messaging;

import com.example.demo.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumes app events from Kafka. Add your processing logic here
 * (e.g. analytics, notifications, cache updates).
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaConsumerService {

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
}
