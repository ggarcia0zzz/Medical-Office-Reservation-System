# Medical Office Reservation System

A RESTful backend API for managing medical office appointments, built with Spring Boot and PostgreSQL. The system handles the full appointment lifecycle — from scheduling and confirmation to completion and cancellation — alongside doctor scheduling, office management, and operational reporting.

---

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.5**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **MapStruct** — DTO/entity mapping
- **Lombok** — boilerplate reduction
- **Docker** — local database
- **Testcontainers + JUnit 5** — integration testing with a real PostgreSQL instance

---

## Project Structure

```
src/main/java/com/example/medicalofficereservationsystem/
├── entities/          # JPA entities
├── enums/             # AppointmentStatus, OfficeStatus, PatientStatus
├── repository/        # Spring Data JPA repositories
├── service/
│   ├── Mapper/        # MapStruct mappers
│   └── *.java         # Service interfaces and implementations
└── api/
    └── dto/           # Request/Response records per domain
```

---

## Domain Model

| Entity | Description |
|---|---|
| `Patient` | A person who books appointments |
| `Doctor` | A physician with a specialty and weekly schedule |
| `Specialty` | Medical specialty (e.g. Cardiology, Neurology) |
| `Office` | A physical consultation room with opening/closing hours |
| `DoctorSchedule` | A doctor's working hours for a given day of the week |
| `AppointmentType` | Type of appointment with a fixed duration in minutes |
| `Appointment` | A scheduled meeting between a patient, doctor, and office |

---

## Services

### Patient
- Create, retrieve (by ID or all), and update patients

### Doctor
- Create, retrieve (by ID or all), and update doctors

### Appointment
- Create an appointment
- Transition status: `SCHEDULED → CONFIRMED → COMPLETED`
- Mark as `NO_SHOW` or `CANCELLED` (with cancellation reason)

### Doctor Schedule
- Assign working hours to a doctor per day of the week
- Retrieve all schedules for a given doctor

### Availability Slots
- Given a doctor, a date, and an appointment type, returns all free time slots for that day based on the doctor's schedule and existing appointments

### Reports
- **Doctor Productivity** — completed vs. total appointments, productivity percentage
- **Patient No-Show Ranking** — top 10 patients by no-show count
- **Office Occupancy** — busy hours vs. available hours per office over a date range, with pagination

---

## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker

### 1. Start the database

```bash
docker run --name medical-office-db \
  -e POSTGRES_DB=medical_office_reservation_system \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=example \
  -p 5432:5432 \
  -d postgres:16-alpine
```

### 2. Run the application

```bash
./mvnw spring-boot:run
```

Hibernate will auto-create the schema on first run (`ddl-auto=update`).

---

## Configuration

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medical_office_reservation_system
spring.datasource.username=postgres
spring.datasource.password=example
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Running Tests

Tests use Testcontainers to spin up a real PostgreSQL instance automatically — no manual setup needed.

```bash
./mvnw test
```

Test coverage includes:

- **Repository tests** — custom JPQL queries (overlap detection, no-show ranking, occupancy, specialty filtering)
- **Service unit tests** — Mockito-based tests for appointment lifecycle, availability slot generation, and schedule management

---

## Appointment Status Flow

```
SCHEDULED → CONFIRMED → COMPLETED
         ↘            ↘
          CANCELLED    NO_SHOW
```

---

## Authors

- **Gabriela García**
- **Gabriel Gomez**
