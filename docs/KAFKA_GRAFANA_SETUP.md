# Kafka & Grafana Setup Guide

## Quick Start

### 1. Start infrastructure

```bash
docker-compose up -d
```

This starts: Kafka (9092), Zookeeper (2181), Prometheus (9090), Grafana (3001), Kafka Exporter (9308).

### 2. Run the application

```bash
mvn spring-boot:run
```

For local dev, if port 80 requires admin rights, set `PORT=8080` or add `server.port=8080` to `application.properties`.

### 3. Configure Grafana

1. Open http://localhost:3001
2. Login: **admin** / **admin**
3. Add Prometheus data source:
   - Configuration → Data Sources → Add data source
   - Choose **Prometheus**
   - URL: `http://prometheus:9090` (from inside Docker) or `http://host.docker.internal:9090` if Grafana runs outside Docker
   - Save & Test

4. Import dashboards:
   - Spring Boot 2.1 Statistics: ID **10280**
   - Kafka Exporter Overview: ID **7589**
   - Dashboards → Import → enter ID → Load

### 4. Verify Prometheus can reach the app

If the app runs on your host, ensure `prometheus.yml` targets the correct host and port. For Docker Desktop, `host.docker.internal` points to your machine. Update the target if needed:

```yaml
- targets: ['host.docker.internal:8080']  # or :80
```

---

## Publishing events from your services

```java
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final KafkaProducerService kafkaProducer;

    public Hospital create(Hospital hospital) {
        Hospital saved = hospitalRepository.save(hospital);
        kafkaProducer.publishAppEvent(AppEvent.of(
            saved.getId().toString(), "HOSPITAL_CREATED", saved));
        return saved;
    }
}
```

---

## Ports

| Service       | Port | URL                    |
|---------------|------|------------------------|
| Kafka         | 9092 | localhost:9092         |
| Prometheus    | 9090 | http://localhost:9090  |
| Grafana       | 3001 | http://localhost:3001  |
| Kafka Exporter| 9308 | localhost:9308         |
| App Actuator  | /actuator/prometheus | http://localhost:8080/actuator/prometheus |

---

## Production notes

- Use environment variables for Kafka bootstrap servers: `spring.kafka.bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS}`
- For Azure deployment, consider Azure Event Hubs (Kafka-compatible) or managed Kafka
- Grafana can be run separately; configure Prometheus to scrape your deployed app's `/actuator/prometheus` endpoint
