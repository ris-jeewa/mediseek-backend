package com.example.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaConfig {

    public static final String TOPIC_APP_EVENTS = "app.events";
    public static final String TOPIC_HOSPITAL_EVENTS = "hospital.events";
    public static final String TOPIC_PHARMACY_EVENTS = "pharmacy.events";
    public static final String TOPIC_MEDICINE_EVENTS = "medicine.events";

    @Bean
    public NewTopic appEventsTopic() {
        return TopicBuilder.name(TOPIC_APP_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic hospitalEventsTopic() {
        return TopicBuilder.name(TOPIC_HOSPITAL_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic pharmacyEventsTopic() {
        return TopicBuilder.name(TOPIC_PHARMACY_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic medicineEventsTopic() {
        return TopicBuilder.name(TOPIC_MEDICINE_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
