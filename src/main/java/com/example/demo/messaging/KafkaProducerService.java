package com.example.demo.messaging;

import com.example.demo.config.KafkaConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service to publish events to Kafka. Only active when spring.kafka.bootstrap-servers is set. Use this from your services/controllers
 * when you want to emit domain events (e.g. after creating/updating entities).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Publish an app event to the default events topic.
     */
    public CompletableFuture<SendResult<String, String>> publishAppEvent(AppEvent event) {
        return publishJson(KafkaConfig.TOPIC_APP_EVENTS, event.entityId(), event);
    }

    /**
     * Publish a JSON message to a topic with optional key.
     */
    public CompletableFuture<SendResult<String, String>> publishJson(String topic, String key, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            return kafkaTemplate.send(topic, key != null ? key : "", json)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.warn("Kafka send failed for topic {}: {}", topic, ex.getMessage());
                        } else if (result != null) {
                            log.debug("Sent to {} partition {} offset {}", topic,
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize payload for topic {}: {}", topic, e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Publish a plain string message.
     */
    public CompletableFuture<SendResult<String, String>> publish(String topic, String key, String message) {
        return kafkaTemplate.send(topic, key != null ? key : "", message)
                .whenComplete((result, ex) -> {
                    if (ex != null) log.warn("Kafka send failed for topic {}: {}", topic, ex.getMessage());
                });
    }
}
