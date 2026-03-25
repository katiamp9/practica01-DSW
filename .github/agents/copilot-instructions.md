# practica01 Development Guidelines

Auto-generated from all feature plans. Last updated: 2026-03-25

## Active Technologies
- PostgreSQL 16 (archive-001-crud-empleados)
- Java 17 + Spring Boot 3.x, Spring Security, Spring Data JPA, springdoc-openapi, Flyway (001-empleados-clave-compuesta)
- Java 17 + Spring Boot 3.3.x, Spring Security, Spring Data JPA, Flyway, springdoc-openapi (003-003-auth-empleado-login)
- PostgreSQL (tabla existente `empleados` + nuevas tablas `cuentas_empleado` y `credenciales_empleado`) (003-003-auth-empleado-login)
- Java 17 + Spring Boot 3.3.x, Spring Data JPA, Spring Security, Flyway, springdoc-openapi (004-crud-departamentos-relacion)
- PostgreSQL (Docker Compose en local) (004-crud-departamentos-relacion)
- Java 17 (backend), TypeScript + Angular 21 (frontend) + Spring Boot 3.3.x, Spring Security, Angular CLI 21, Angular Standalone Components/Signals/Control Flow (005-frontend-angular21-setup)
- PostgreSQL (sin cambios de modelo para esta feature) (005-frontend-angular21-setup)
- TypeScript 5.9 + Angular 21 (frontend), Java 17 + Spring Boot 3.3.x (backend ya existente) + Angular standalone APIs, Signals, Reactive Forms, Angular Router, HttpClient, Spring Security Basic Auth (007-login-screen-auth)
- localStorage en frontend para sesión/credenciales/rol; PostgreSQL en backend sin cambios de esquema para esta feature (007-login-screen-auth)
- TypeScript 5.9 + Angular 21 (frontend), Java 17 + Spring Boot 3.3.x (backend existente) + Angular standalone APIs, Signals, Angular Router, HttpClient, Spring Security Basic Auth, Spring Data JPA Pageable (008-admin-dashboard-navigation)
- localStorage para sesión en frontend; PostgreSQL en backend (sin cambios de esquema) (008-admin-dashboard-navigation)
- TypeScript 5.9 + Angular 21 (frontend), Java 17 + Spring Boot 3.3.x (backend existente) + Angular standalone APIs, Signals, Reactive Forms, Angular Router, HttpClient, Spring Security Basic Auth, Spring Data JPA Pageable (009-crud-empleados-completo)
- localStorage para sesión frontend; PostgreSQL backend sin cambios de esquema en esta iteración (009-crud-empleados-completo)
- Java 17 (backend), TypeScript 5.9 + Angular 21 (frontend) + Spring Boot 3.x, Spring Security (Basic Auth), Spring Data JPA, Flyway, PostgreSQL, Angular Standalone Components, Angular Signals, RxJS, HttpClient (009-crud-empleados-completo)
- PostgreSQL (Docker Compose en local/integración) (009-crud-empleados-completo)
- Java 17 (backend), TypeScript 5.9 (frontend Angular 21) + Spring Boot 3.3.2 (Web, Data JPA, Security, Validation), Flyway, PostgreSQL driver, springdoc OpenAPI 2.6.0, Angular 21, RxJS 7.8 (009-crud-empleados-completo)
- PostgreSQL (principal), H2 (pruebas) (009-crud-empleados-completo)
- Java 17 (backend), TypeScript 5.9 + Angular 21 (frontend) + Spring Boot 3.3.2 (Web, Data JPA, Security, Validation), PostgreSQL driver, springdoc OpenAPI, Angular Signals, Reactive Forms, HttpClient, RxJS 7.8 (010-crud-departamentos-areas)
- TypeScript 5.9 + Angular 21 (frontend), Java 17 + Spring Boot 3.3.x (backend existente) + Angular Standalone Components, Signals, Control Flow nativo, CSS global en `styles.css`, Spring Security Basic Auth, Spring Data JPA Pageable (011-redesign-github-experience)
- PostgreSQL (backend, sin cambios de esquema) (011-redesign-github-experience)

- Java 17 + Spring Boot 3.x, Spring Security (Basic Auth), Spring Data JPA, springdoc-openapi, Flyway (archive-001-crud-empleados)

## Project Structure

```text
backend/
frontend/
tests/
```

## Commands

# Add commands for Java 17

## Code Style

Java 17: Follow standard conventions

## Recent Changes
- 011-redesign-github-experience: Added TypeScript 5.9 + Angular 21 (frontend), Java 17 + Spring Boot 3.3.x (backend existente) + Angular Standalone Components, Signals, Control Flow nativo, CSS global en `styles.css`, Spring Security Basic Auth, Spring Data JPA Pageable
- 011-redesign-github-experience: Added TypeScript 5.9 + Angular 21 (frontend), Java 17 + Spring Boot 3.3.x (backend existente) + Angular Standalone Components, Signals, Control Flow nativo, CSS global en `styles.css`, Spring Security Basic Auth, Spring Data JPA Pageable
- 010-crud-departamentos-areas: Added Java 17 (backend), TypeScript 5.9 + Angular 21 (frontend) + Spring Boot 3.3.2 (Web, Data JPA, Security, Validation), PostgreSQL driver, springdoc OpenAPI, Angular Signals, Reactive Forms, HttpClient, RxJS 7.8


<!-- MANUAL ADDITIONS START -->
<!-- MANUAL ADDITIONS END -->
