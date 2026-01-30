# SkillTrack – Developer Growth Platform

**Tagline:**  
A modular assessment system demonstrating backend (Java), testing, and event-driven expertise — designed for 1B→2B developer growth.

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Objectives](#objectives)
    - Short Term: Back on Track
    - Medium Term: Spring Certified Professional
    - Long Term: 2B Growth Goals
3. [Architecture](#architecture)
4. [Modules / Features](#modules--features)
    - Assessment POC
    - Spring Batch Jobs
    - Kafka Event Flow
    - Security & WebFlux
5. [Testing & Evidence](#testing--evidence)
6. [Usage](#usage)
7. [Learning Notes](#learning-notes)
8. [Future Work](#future-work)
9. [References](#references)

---

## Project Overview
SkillTrack is a **modular backend platform** designed to measure, evaluate, and track developer skills across multiple domains. It integrates:

- **TDD & BDD practices** for high-quality, testable code
- **Spring Boot / Core / Data / Security / WebFlux** for production-grade backend
- **Kafka** for event-driven communication
- **Spring Batch** for periodic processing and reporting
- **DDD & Clean Architecture** principles for maintainable, modular code

This project serves as a **hands-on learning platform** for skill growth, and demonstrates readiness for **2A → 2B promotion**.

---

## Objectives

### Short Term – Back on Track
Goal: Rebuild technical fluency and confidence after a break.

**Key actions:**
- Build POC Assessment API
- Apply **TDD & BDD** in unit and integration tests
- Configure Spring Core / Boot manually and via `@Component`
- Add repositories, services, controllers, WebFlux endpoints
- Secure endpoints using Spring Security and AOP
- Integrate Kafka for event-driven flow
- Implement **Spring Batch job** for assessment report processing

### Medium Term – Spring Certification 2025
Goal: Prepare for certification while reinforcing Short Term knowledge.

**Key actions:**
- Follow Pluralsight Spring Professional learning paths
- Take mock exams and track progress
- Add mini-exercises for exam topics
- Enhance POC with advanced features (reactive endpoints, additional tests, security profiles)

### Long Term – 2B Developer Growth
Goal: Demonstrate **senior-level impact and ownership** through SkillTrack.

**Key actions:**
- Expand POC into fully modular system
- Add more batch jobs, reports, and event flows
- Share knowledge internally (documentation, demos, workshops)
- Maintain evidence of high-quality development practices

---

## Architecture

## Modules / Features

### Assessment POC
- CRUD API for Assessment and AssessmentResults
- Validation and persistence
- Unit and integration tests with TDD/BDD

### Spring Batch Jobs
- Aggregates assessment results
- Generates CSV reports
- Publishes `BatchReportGeneratedEvent` to Kafka

### Kafka Event Flow
- Events for Assessment completion and batch reports
- Demonstrates async, decoupled architecture

### Security & WebFlux
- Method-level security using Spring Security & AOP
- Reactive endpoint implemented via WebFlux
- Integration tests to ensure secure flows

---

## Testing & Evidence
- Unit tests for domain logic (TDD)
- Integration tests with `@SpringBootTest`
- BDD scenarios in Gherkin
- Evidence of course completions, mini-project exercises, and mock exams
- GitHub commit history reflects iterative development

---

## Usage

1. Clone repository:
```bash
git clone https://github.com/yourusername/skilltrack.git
```
2. Configure application properties (application.yml):

3. Run Spring Boot application:
```bash
./mvnw spring-boot:run
```
4. Access endpoints:
- REST API: /assessments
- WebFlux endpoint: /assessments/reactive

5. Run Batch Job:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.batch.job.names=processAssessmentsJob
```

---

## Learning Notes
- TDD / BDD principles applied across modules
- Kafka event-driven patterns explored
- Spring Batch job design & testing
- Clean Architecture / DDD patterns reinforced

---

## Future Work
- Expand batch jobs for multiple domains
- Implement more complex reporting & analytics
- Optional: split modules into microservices
- Internal knowledge sharing and workshops

---

## References
- Pluralsight 
- Official Spring Documentation
- Codecademy TDD / BDD Exercises
- Domain Driven Design Reference Book