package com.example.demo.messaging;

import java.time.Instant;

/**
 * Domain event for publishing to Kafka. Use from services when you want to emit
 * domain events (e.g. after creating/updating entities).
 */
public record AppEvent(String entityId, String eventType, Object payload, Instant timestamp) {

    public static AppEvent of(String entityId, String eventType, Object payload) {
        return new AppEvent(entityId, eventType, payload, Instant.now());
    }
}
