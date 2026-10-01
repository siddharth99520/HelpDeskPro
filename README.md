# HelpDeskPro

A phase-based Java + Spring Boot + PostgreSQL project.

## Project Phases Tracker
- [x] **Phase 1: Domain Modeling in Plain Java**
  - Modeled core entities (`User`, `Role`, `Ticket`, `Asset`, etc.).
  - Built pure-Java `TicketService` and `AssetService` enforcing workflow rules.
  - Implemented state-machine and asset-assignment rules.
  - Added JUnit tests for framework-free business logic.
- [x] **Phase 2: Database Design & SQL Foundations**
  - Created `docker-compose.yml` for PostgreSQL and Flyway.
  - Implemented base schema with all core tables and SLA timestamp columns.
  - Enforced asset double-assignment protection with a partial unique index.
  - Successfully ran Flyway migrations and validated raw SQL queries.
- [x] **Phase 3: Spring Boot REST Foundations**
  - Converted the project to a Spring Boot 2.7 REST API.
  - Re-packaged domain classes by business module (`ticket`, `asset`, `user`, `shared`).
  - Added a `TicketController` with hard-coded POST and GET endpoints.
  - Implemented a `@ControllerAdvice` global exception handler.
  - Configured `application.yml` to point to the PostgreSQL database.
- [x] **Phase 4: Persistence Layer (JPA + DTOs)**
  - Mapped core entities (Ticket, Asset, User, Category) to the DB schema.
  - Implemented `@Version` on Ticket and Asset for optimistic locking.
  - Replaced Maps with Spring Data JPA Repositories.
  - Enforced Bean Validation (`@Valid`) on the DTOs.
  - Added Testcontainers Postgres integration tests.
- [x] **Phase 5: Security: AuthN, AuthZ & Ownership**
  - Secured endpoints using `spring-boot-starter-security`.
  - Created a local `MockAuthFilter` using `X-User-Id` headers.
  - Injected `UserPrincipal` dynamically into Controllers.
  - Enforced ownership rules using `@PreAuthorize` at the Service layer.
- [x] **Phase 6: Workflow & Asset Lifecycle**
  - Built `AssetController` to handle adding, assigning, repairing, and retiring assets.
  - Added new Ticket endpoints: `PUT /api/tickets/{id}/assign` and `PUT /api/tickets/{id}/status`.
  - Wired REST endpoints to enforce pure-Java domain state machine rules.
- [ ] **Phase 7: SLA Engine & Audit Trail**
- [ ] **Phase 8: Testing Consolidation, Docker & Observability**
- [ ] **Phase 9: Modern Web Application (Next.js + React)**
- [ ] **Phase 10: Optional AI Layer — Gemini + MCP**
- [ ] **Phase 11: Documentation, Presentation & Interview Readiness**
