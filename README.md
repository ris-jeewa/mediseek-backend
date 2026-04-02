# Mediseek Backend

Backend API for **Mediseek** — a healthcare search and discovery service. It provides hospitals, pharmacies, doctors, medicines, and AI-powered symptom analysis, with event streaming and observability built in.

## Tech Stack

- **Java 17** · **Spring Boot 3.2**
- **Spring Data JPA** · **MySQL** (e.g. Azure MySQL)
- **Apache Kafka** — event publishing (hospital/pharmacy updates, etc.)
- **Groq API** — LLM-based symptom analysis (e.g. Llama 3.3 70B)
- **Spring Boot Actuator** · **Grafana** · **Loki** · **Promtail** — health, metrics, and logs
- **Azure Web App** — deployment target (optional)

## Prerequisites

- JDK 17+
- Maven 3.6+
- MySQL (local or Cloud)
- (Optional) Docker & Docker Compose — for Kafka, Grafana, Prometheus, Loki, Promtail

## Quick Start

### 1. Configure the application

Copy or override settings as needed. Use environment variables or a local profile so **credentials are not committed**:

- `spring.datasource.url` — MySQL JDBC URL  
- `spring.datasource.username` / `spring.datasource.password`  
- `groq.api.key` — [Groq](https://console.groq.com/) API key  
- `spring.kafka.bootstrap-servers` — e.g. `localhost:9092` when running Kafka locally  

Example for local override: create `src/main/resources/application-local.properties` with your values, then run with `-Dspring.profiles.active=local`.

### 2. Start infrastructure (optional)

For Kafka, Prometheus, Grafana, Loki, and Promtail:

```bash
docker-compose up -d
```

See [docs/KAFKA_GRAFANA_SETUP.md](docs/KAFKA_GRAFANA_SETUP.md) for Grafana data sources and dashboard IDs.

### 3. Run the application

```bash
mvn spring-boot:run
```

By default the server uses `server.port=${PORT:80}`. If port 80 needs admin rights, set `PORT=8080` or add `server.port=8080` in your config.

## API Overview

| Area | Base path | Description |
|------|-----------|-------------|
| Hospitals | `/api/hospital` | Paginated list, create single/multiple hospitals |
| Pharmacies | `/api/pharmacy` | Pharmacy CRUD |
| Pharmacy branches | `/api/pharmacy-branch` | Branch management |
| Doctors | `/api/doctor` | Doctor CRUD and hospital associations |
| Medicines | `/api/medicine` | Medicine catalog |
| Branch medicines | `/api/branch-medicine` | Medicine availability at pharmacy branches |
| Contacts | `/api/contact` | Contact management |
| Symptom analysis | `/api/symptoms/analyze` | POST: AI symptom analysis via Groq (request body: `SymptomRequest`) |

Health and metrics:

- **Health**: `/actuator/health`
- **Prometheus metrics**: `/actuator/prometheus`

## Project Structure

```
src/main/java/com/example/demo/
├── config/          # Kafka, CORS, etc.
├── controller/      # REST controllers
├── dto/             # Request/response DTOs
├── entity/          # JPA entities (Hospital, Pharmacy, Doctor, Medicine, etc.)
├── exception/       # Global exception handling
├── messaging/       # Kafka producer/consumer, AppEvent
├── repository/      # Spring Data JPA repositories
├── service/         # Business logic (Hospital, Pharmacy, Groq, etc.)
└── supportingEntities/
```

## Observability

- **Logs**: Application logs go to `logs/mediseek-app.log`. Promtail can ship them to Loki for querying in Grafana.
- **Metrics**: Actuator exposes Prometheus metrics; Grafana can use them and the Kafka Exporter dashboard (see docs).
- **Ports** (when using Docker Compose): Kafka 9092, Prometheus 9090, Grafana 3001, Loki 3100, Kafka Exporter 9308.

Full steps: [docs/KAFKA_GRAFANA_SETUP.md](docs/KAFKA_GRAFANA_SETUP.md).

## Deployment (Azure)

The project includes the **Azure Web App Maven plugin**. Configure `resourceGroup`, `appName`, and any app settings (e.g. env vars for DB and Groq) as needed, then:

```bash
mvn clean package azure-webapp:deploy
```

Ensure the Azure Web App has the correct environment variables (database URL, Groq API key, Kafka bootstrap servers if used).
