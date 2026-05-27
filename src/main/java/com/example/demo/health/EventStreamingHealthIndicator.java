package com.example.demo.health;

import com.example.demo.messaging.DomainEventPublisher;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Reports whether domain events are wired to Kafka ({@link DomainEventPublisher#isKafkaEnabled()}).
 * Application stays UP when Kafka is off; detail explains integration mode.
 */
@Component
public class EventStreamingHealthIndicator implements HealthIndicator {

    private final DomainEventPublisher domainEventPublisher;

    public EventStreamingHealthIndicator(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Health health() {
        if (domainEventPublisher.isKafkaEnabled()) {
            return Health.up()
                    .withDetail("kafka", "event publishing enabled")
                    .build();
        }
        return Health.up()
                .withDetail("kafka", "not configured (spring.kafka.bootstrap-servers unset)")
                .build();
    }
}
