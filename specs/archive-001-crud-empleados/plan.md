# Implementation Plan: CRUD de Empleados

**Branch**: `001-crud-empleados` | **Date**: 2026-03-05 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/archive-001-crud-empleados/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Consolidar el backend Spring Boot del CRUD de empleados con contrato versionado
obligatorio bajo `/api/v1`, seguridad Basic Auth, persistencia PostgreSQL con
migraciones y reglas explícitas de listado paginado/ordenado. Para `GET` de
colecciones se adopta `Pageable` con `page` y `sort` obligatorios, `size`
opcional (default `20`, máximo `100`), lista blanca de campos de ordenamiento
(`clave`, `nombre`, `direccion`, `telefono`) y respuesta `422` cuando los
parámetros de paginación/ordenamiento sean inválidos.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.x, Spring Security (Basic Auth), Spring Data JPA, springdoc-openapi, Flyway  
**Storage**: PostgreSQL 16  
**Testing**: JUnit 5, Spring Boot Test, MockMvc e integración API  
**Target Platform**: Linux con Docker/Compose  
**Project Type**: backend web-service  
**Performance Goals**: p95 < 200ms en operaciones CRUD simples con hasta 10k empleados  
**Constraints**: Solo `/api/v1`; `page` y `sort` obligatorios; `size` opcional default 20 máx. 100; `422` en paginación/orden inválidos  
**Scale/Scope**: sistema administrativo interno con ~10 usuarios concurrentes y hasta 10k registros

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Confirma compatibilidad Spring Boot 3.x + Java 17.
- [x] Define autenticación básica para endpoints protegidos y manejo seguro de credenciales.
- [x] Define PostgreSQL como persistencia principal con estrategia de migraciones.
- [x] Define ejecución local/integración con Docker (compose) reproducible.
- [x] Define versionamiento por prefijo de URL `/api/v{n}` (versión vigente: `/api/v1`).
- [x] Define actualización de contrato OpenAPI/Swagger para endpoints nuevos o modificados.
- [x] Define uso de `Pageable` en GET de colecciones con `page`, `size`, `sort` y reglas de validación.
- [x] Define evidencia mínima de calidad: pruebas y validación de build, incluyendo paginación/ordenamiento.

## Project Structure

### Documentation (this feature)

```text
specs/archive-001-crud-empleados/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── empleados.openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
src/
├── main/
│   ├── java/
│   │   └── com/example/empleados/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── domain/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
│       ├── application.yml
│       └── db/migration/
└── test/
  └── java/com/example/empleados/

docker/
└── compose/
```

**Structure Decision**: Se adopta estructura monolítica Spring Boot (`src/main`
y `src/test`) con documentación funcional en `specs/archive-001-crud-empleados` y
entorno reproducible en `docker/compose`. Se descartan variantes web/móvil por
no aplicar al alcance del backend actual.

## Phase 0: Research Output

`research.md` resuelve decisiones de stack, persistencia/migraciones,
versionamiento `/api/v1`, política de errores HTTP y política de paginación
(`page` y `sort` obligatorios; `size` opcional con default `20` y máximo `100`).
No quedan `NEEDS CLARIFICATION`.

## Phase 1: Design Output

- `data-model.md`: define entidad `Empleado`, reglas de validación y modelo de
  consulta paginado.
- `contracts/empleados.openapi.yaml`: define endpoints versionados `/api/v1` y
  parámetros de paginación/ordenamiento.
- `quickstart.md`: detalla ejecución local, pruebas y ejemplos de uso.

## Post-Design Constitution Check

- [x] Principio I (Stack): Java 17 + Spring Boot 3.x confirmados.
- [x] Principio II (Seguridad): Basic Auth aplicado a endpoints de negocio.
- [x] Principio III (Persistencia): PostgreSQL + migraciones versionadas + Docker definidos.
- [x] Principio IV (Contrato): OpenAPI con rutas `/api/v1` documentadas.
- [x] Principio V (Calidad evolutiva): GET de colecciones con `Pageable` y validación de parámetros.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | N/A | N/A |
