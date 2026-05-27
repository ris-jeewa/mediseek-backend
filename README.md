# Mediseek Backend

Backend API for **Mediseek** — a healthcare search and discovery service. It exposes REST APIs for hospitals, pharmacies, branches, medicines, doctors, and contacts; uses **Groq** for AI symptom analysis with doctor matching; streams domain events through **Apache Kafka**; caches pharmacy reads with **Redis**; and exposes health and Prometheus metrics for operations.

---

## Table of contents

- [Tech stack](#tech-stack)
- [Architecture overview](#architecture-overview)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Quick start](#quick-start)
- [Docker Compose (Kafka, Redis, Grafana stack)](#docker-compose-kafka-redis-grafana-stack)
- [REST API reference](#rest-api-reference)
- [Domain model](#domain-model)
- [Kafka integration](#kafka-integration)
- [Caching (Redis)](#caching-redis)
- [Symptom analysis (Groq)](#symptom-analysis-groq)
- [Exception handling](#exception-handling)
- [CORS](#cors)
- [Observability](#observability)
- [Database scripts](#database-scripts)
- [Build and test](#build-and-test)
- [Deployment (Azure Web App)](#deployment-azure-web-app)
- [Project structure](#project-structure)
- [Security notes](#security-notes)

---

## Tech stack

| Layer | Technology |
|--------|------------|
| Runtime | **Java 17** |
| Framework | **Spring Boot 3.2** |
| Web | Spring Web, **Bean Validation** |
| Persistence | **Spring Data JPA**, **MySQL** (e.g. Azure Database for MySQL) |
| HTTP client | **RestTemplate** (Groq), **WebFlux** on classpath |
| Messaging | **Spring Kafka** |
| Cache | **Spring Cache** + **Spring Data Redis** |
| Utilities | **Lombok**, **Jackson** |
| Ops | **Spring Boot Actuator**, **Micrometer Prometheus** |
| Deploy | **Azure Web App Maven Plugin** (optional) |

---

## Architecture overview

- **Controllers** validate input and delegate to **services**.
- **Services** hold business logic, call **repositories**, optionally **KafkaProducerService**, and use **@Cacheable** / **@CacheEvict** where configured.
- **JPA entities** map to MySQL; `spring.jpa.hibernate.ddl-auto=update` is typical for iterative development (tune for production).
- **Kafka** is optional: if `spring.kafka.bootstrap-servers` is unset, Kafka beans are not created and the app still starts; services inject the producer with `required = false` and guard calls.
- **Redis** is required for the configured `RedisCacheManager`; if Redis is down, cache-backed endpoints can fail unless you add resilience or disable Redis for local runs.

```text
Client → REST → Service → JPA / Redis cache / Kafka / Groq API
```

---

## Prerequisites

- **JDK 17+**
- **Maven 3.6+**
- **MySQL** (local or cloud)
- **Redis** (local or e.g. Azure Cache for Redis) when using caching as configured
- **Optional:** Docker & Docker Compose — Kafka, Zookeeper, Kafka Exporter, Loki, Promtail, Grafana, Redis

---

## Configuration

Use **environment variables**, **`application-local.properties`**, or your platform’s app settings. **Do not commit secrets** (database passwords, Groq keys, production Redis URLs).

| Property / area | Purpose |
|-----------------|--------|
| `spring.datasource.*` | MySQL JDBC URL, username, password |
| `spring.jpa.hibernate.ddl-auto` | Schema strategy (`update`, `validate`, etc.) |
| `groq.api.key`, `groq.api.url`, `groq.model` | Groq OpenAI-compatible chat API |
| `spring.kafka.bootstrap-servers` | Kafka brokers; omit to disable Kafka integration |
| `spring.kafka.consumer.*`, `spring.kafka.admin.*` | Consumer group, offset reset, topic auto-create |
| `spring.data.redis.host`, `spring.data.redis.port`, `spring.data.redis.url` | Redis connection (URL form useful for TLS/Azure) |
| `spring.data.redis.time-to-live` | Default cache entry TTL (ms); default in code fallback `600000` if unset in some paths — align in your profile |
| `server.port` | Defaults to `80` via `${PORT:80}`; use `PORT=8080` locally if needed |
| `management.endpoints.web.exposure.include` | Actuator: `health`, `info`, `prometheus`, `metrics` |
| `logging.file.name` | File log path (e.g. `logs/mediseek-app.log` for Promtail) |

**Local profile example:** create `src/main/resources/application-local.properties` and run:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

---

## Quick start

1. Configure MySQL, Redis, and (optionally) Kafka and Groq as above.
2. Start Redis (and Kafka if you use events).
3. Run the application:

```bash
mvn spring-boot:run
```

Default listen port is **`${PORT:80}`**. On Windows without elevated port binding, set `PORT=8080` or override `server.port`.

---

## Docker Compose (Kafka, Redis, Grafana stack)

`docker-compose.yml` provides:

| Service | Ports | Role |
|---------|-------|------|
| **Zookeeper** | 2181 | Kafka coordination |
| **Kafka** | 9092 (host), 29092 (internal) | Message broker |
| **kafka-exporter** | 9308 | Kafka metrics for scraping |
| **Loki** | 3100 | Log aggregation |
| **Promtail** | (sidecar) | Ships `./logs` into Loki |
| **Grafana** | **3001** → 3000 | Dashboards (default admin password in compose: `admin`) |
| **Redis** | 6379 | Application cache backend |

Start everything:

```bash
docker-compose up -d
```

- Point the app at **`localhost:9092`** for Kafka and **`localhost:6379`** for Redis.
- Mount `./logs` so Promtail can read `logs/mediseek-app.log` (create the `logs` directory if needed).
- Add **Prometheus** separately if you want scrape configs; the compose file does not start Prometheus by default.
- Grafana: add Loki as a data source (`http://loki:3100` from inside Docker network, or `http://localhost:3100` from the host). Optionally add Prometheus and scrape `kafka-exporter:9308` or the app’s `/actuator/prometheus`.

---

## REST API reference

Base URL: `http://localhost:<port>` (e.g. `http://localhost:8080`).

### Hospitals — `/api/hospital`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/getAll?page=0&size=10` | Paginated hospitals from DB |
| POST | `/create` | Create one hospital (`HospitalCreateRequest` body) |
| POST | `/createMultiple` | Bulk create (`List<HospitalCreateRequest>`) |

### Pharmacies — `/api/pharmacy`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/create` | Create pharmacy |
| POST | `/createpharmacies` | Create multiple |
| GET | `/getAll` | List all |
| GET | `/{id}` | Get by id |
| GET | `/by-medicine/{medicineId}` | Pharmacies linked to a medicine (cached) |

### Pharmacy branches — `/api/pharmacy-branch`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/create` | Create branch |
| POST | `/createbranches` | Create multiple |
| GET | `/getAll` | List all |
| GET | `/{id}` | Get by id |
| GET | `/pharmacy/{pharmacyId}` | Branches for pharmacy |
| GET | `/active` | Active branches |

### Medicines — `/api/medicine`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/create` | Create medicine |
| POST | `/createmany` | Create many |
| GET | `/getmedicines` | List all |
| GET | `/getmedicine/{id}` | Get by id |
| PUT | `/updatemedicine` | Full update |
| PATCH | `/update` | Partial update |
| DELETE | `/{id}` | Delete |
| GET | `/search?q=...` | Text search over medicines |

### Branch–medicine links — `/api/branch-medicine`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/create` | Link medicine to branch |
| PUT | `/update/{branchId}/{medicineId}` | Update link |
| DELETE | `/{branchId}/{medicineId}` | Remove link |
| GET | `/{medicineId}` | Branch IDs that stock this medicine (`List<Long>`) |
| GET | `/branch/{branchId}` | By branch |
| GET | `/medicine/{medicineId}` | By medicine |
| GET | `/getAll` | All links |

### Doctors — `/api/doctor`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/{doctorId}` | Hospitals associated with the doctor (via `DoctorHospitalService`) |

> Doctor CRUD and hospital–doctor assignment may exist in services/repositories; the public REST surface above is what `DoctorController` exposes today.

### Contacts — `api/contact`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/create` | Create contact |

Spring resolves this as **`/api/contact`** from the application context root.

### Symptoms (AI) — `/api/symptoms`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/analyze` | Body: `SymptomRequest` (`symptoms` text). Returns `AnalzeWithDoctorsDTO`: Groq JSON analysis + doctors from DB matched by specialty |

### App health (custom) — `/api/health`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | Simple health check endpoint |

### Spring Actuator

| Path | Description |
|------|-------------|
| `/actuator/health` | Health status |
| `/actuator/info` | Application info (if configured) |
| `/actuator/prometheus` | Prometheus scrape format |
| `/actuator/metrics` | Metrics listing |

---

## Domain model

Main JPA entities under `com.example.demo.entity`:

- **Hospital** — hospital records (created via `HospitalCreateRequest` mapping in service)
- **Pharmacy**, **PharmacyBranch** — pharmacy and branch locations
- **Medicine** — catalog items
- **BranchMedicine** — availability of a medicine at a branch (composite key in `supportingEntities.BranchMedicineId`)
- **Doctor** — doctor profile including specialty (used for symptom → doctor matching)
- **DoctorHospital** — many-to-many link between doctors and hospitals
- **Contact** — contact submissions

DTOs for APIs live in `com.example.demo.dto` (e.g. `PaginatedHospitalResponse`, `SymptomRequest`, `ErrorResponse`).

---

## Kafka integration

- **Config:** `KafkaConfig` declares topics with `TopicBuilder` (3 partitions, 1 replica — adjust for production).
- **Topics:** `app.events`, `hospital.events`, `pharmacy.events`, `medicine.events`, `doctor.events`.
- **Producer:** `KafkaProducerService` serializes `AppEvent` (record: `entityId`, `eventType`, `payload`, `timestamp`) to JSON via `KafkaTemplate<String, String>`. Message key is typically `entityId`.
- **Consumer:** `KafkaConsumerService` uses `@KafkaListener` with dedicated `groupId` per topic (e.g. `mediseek-medicine-group`). Currently logs messages; extend with your processing.
- **Activation:** `@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")` on config, producer, and consumer. Services use `@Autowired(required = false)` and null checks before publishing.
- **Emitters:** e.g. `HospitalService`, `PharmacyService`, `MedicineService` publish after successful saves (`*_CREATED` style events).

Implementation details: `messaging/` package and `config/KafkaConfig.java`.

---

## Caching (Redis)

- **`@EnableCaching`** on `DemoApplication`.
- **`RedisConfig`** defines a `RedisCacheManager` with JSON values, string keys, configurable TTL (`spring.data.redis.time-to-live`), and **no cache for null values**.
- **`PharmacyService`** uses `@Cacheable`, `@CachePut`, and `@CacheEvict` on caches such as `pharmacies`, `pharmacy`, `pharmaciesByMedicine` so list/detail and by-medicine queries stay consistent after writes.

Ensure Redis is reachable before hitting cached pharmacy endpoints.

---

## Symptom analysis (Groq)

1. Client sends **POST `/api/symptoms/analyze`** with symptom text.
2. **`GroqService`** builds a structured prompt (specialty, urgency, explanation, recommendations) and calls Groq’s chat completions API.
3. The model response is parsed as **`SymptomAnalysisResponse`** (JSON). Specialties are split and used to query **`DoctorRepository.findBySpecialtyContainingIgnoreCase`**.
4. Response bundles analysis + matching doctors in **`AnalzeWithDoctorsDTO`**.

This is **not** a substitute for professional medical advice; suitable for triage-style UX only.

---

## Exception handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) maps exceptions to HTTP status and **`ErrorResponse`**, including:

- `IdNotFoundException`, `ResourceNotFoundException` → 404  
- `DuplicateResourceException`, `ValidationException` → 4xx as configured  
- `MethodArgumentNotValidException` → validation errors  
- `MethodArgumentTypeMismatchException` → bad parameter types  
- Generic `Exception` → 500  

---

## CORS

`CorsConfig` registers a `CorsFilter` allowing:

- `http://localhost:3000`
- `https://mediseek-pi.vercel.app`

All methods and headers for `/**`. Add origins as needed for new frontends.

---

## Observability

- **Logs:** `logging.file.name` → e.g. `logs/mediseek-app.log` (Promtail in Compose reads `./logs`).
- **Metrics:** Micrometer + Prometheus registry; scrape `/actuator/prometheus`.
- **Kafka:** Kafka Exporter on port **9308** when using Compose.

---

## Database scripts

Under `docs/`:

- **`schema.sql`** — reference schema
- **`data.sql`** — sample or seed data (if applicable)

Use these for documentation, manual setup, or DBA review; JPA `ddl-auto` may still apply in dev.

---

## Build and test

```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

```bash
mvn test
```

Artifact name and version match `pom.xml` (`artifactId` **demo**, version **0.0.1-SNAPSHOT**).

---

## Deployment (Azure Web App)

The POM includes **`azure-webapp-maven-plugin`** with example settings (resource group, app name, region **Southeast Asia**, **B1** tier, Java 17). Adjust for your subscription.

```bash
mvn clean package azure-webapp:deploy
```


Set application settings in Azure for: database URL/credentials, **Groq API key**, Redis, Kafka bootstrap servers (if used), and `PORT` / JVM options as required.

---

## Project structure

```text
src/main/java/com/example/demo/
├── DemoApplication.java      # @SpringBootApplication, @EnableCaching
├── config/                   # CorsConfig, KafkaConfig, RedisConfig
├── controller/               # REST endpoints
├── dto/                      # Request/response DTOs
├── entity/                   # JPA entities
├── exception/                # Custom exceptions + GlobalExceptionHandler
├── messaging/                # AppEvent, KafkaProducerService, KafkaConsumerService
├── repository/               # Spring Data JPA repositories
├── service/                  # Business logic
└── supportingEntities/       # e.g. composite keys (BranchMedicineId)

src/main/resources/
└── application.properties    # Prefer overrides via env / profile for secrets

docs/                         # SQL scripts, optional articles
logs/                         # Created at runtime when file logging enabled
```

---

## Security notes

- Rotate any credentials that were ever committed; use **secrets managers** or **Azure App Settings** in production.
- Restrict **CORS** origins in production.
- Run Kafka with **TLS** and authentication in real environments; current sample is plaintext for local dev.
- Review **`ddl-auto`** for production (prefer migrations e.g. Flyway/Liquibase for controlled schema changes).

---

## License / ownership

Project metadata in `pom.xml` (`groupId` **com.example**, `artifactId` **demo**). Update coordinates and description when publishing formally.

Ensure the Azure Web App has the correct environment variables (database URL, Groq API key, Kafka bootstrap servers if used).

