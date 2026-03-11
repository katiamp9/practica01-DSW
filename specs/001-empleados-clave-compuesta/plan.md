# Implementation Plan: Clave Compuesta Empleados

**Branch**: `001-empleados-clave-compuesta` | **Date**: 2026-03-10 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-empleados-clave-compuesta/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Adaptar el CRUD de empleados para que la clave de negocio se gestione como
identificador compuesto lógico con prefijo fijo `EMP-` y consecutivo numérico
autogenerado, manteniendo la inmutabilidad del identificador y compatibilidad
de consultas/listados versionados en `/api/v1`. El plan preserva seguridad con
Basic Auth, persistencia PostgreSQL con migraciones y validación estricta de
paginación/ordenamiento (`page`, `sort` obligatorios; `size` opcional default
20, máximo 100; errores `422`).

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.x, Spring Security, Spring Data JPA, springdoc-openapi, Flyway  
**Storage**: PostgreSQL 16  
**Testing**: JUnit 5, Spring Boot Test, MockMvc (unitarias + integración)  
**Target Platform**: Linux con Docker/Compose  
**Project Type**: backend web-service  
**Performance Goals**: p95 < 200ms para operaciones CRUD con hasta 10k registros  
**Constraints**: rutas solo `/api/v1`; clave autogenerada `EMP-<n>`; `page`+`sort` obligatorios; `size` opcional default 20 máx. 100; `422` en paginación/orden inválidos  
**Scale/Scope**: catálogo administrativo interno (baja concurrencia, alta consistencia de datos)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Confirma compatibilidad Spring Boot 3.x + Java 17.
- [x] Define autenticación básica para endpoints protegidos y manejo seguro de credenciales.
- [x] Define PostgreSQL como persistencia principal con estrategia de migraciones.
- [x] Define ejecución local/integración con Docker (compose) reproducible.
- [x] Define versionamiento por prefijo de URL `/api/v{n}` (versión vigente: `/api/v1`).
- [x] Define actualización de contrato OpenAPI/Swagger para endpoints nuevos o modificados.
- [x] Para cada GET de colecciones, define `Pageable` obligatorio (`page`, `size`, `sort`).
- [x] Define evidencia mínima de calidad: pruebas y validación de build, incluyendo paginación/ordenamiento.

## Project Structure

### Documentation (this feature)

```text
specs/001-empleados-clave-compuesta/
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
│   ├── java/com/example/empleados/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── domain/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       ├── application.yml
│       └── db/migration/
└── test/
  └── java/com/example/empleados/

docker/
└── compose/
```

**Structure Decision**: Se mantiene estructura monolítica Spring Boot existente
porque el cambio es evolutivo sobre dominio/controladores/repositorio y no
requiere separación de servicios ni frontend independiente.

## Phase 0: Research Output

`research.md` documenta decisiones para generación de clave compuesta,
concurrencia de altas, validación de errores HTTP, compatibilidad con contrato
OpenAPI y política de paginación/ordenamiento sin clarificaciones pendientes.

## Phase 1: Design Output

- `data-model.md`: define entidad `Empleado`, componentes lógicos de clave,
  reglas de validación y transiciones del ciclo de vida.
- `contracts/empleados.openapi.yaml`: actualiza contrato de endpoints
  versionados y respuestas de error (`400`, `404`, `409`, `422`).
- `quickstart.md`: define flujo reproducible para ejecutar, validar y probar el
  comportamiento esperado del identificador compuesto.

## Post-Design Constitution Check

- [x] Principio I (Stack): Java 17 + Spring Boot 3.x preservados.
- [x] Principio II (Seguridad): endpoints protegidos con Basic Auth.
- [x] Principio III (Persistencia): PostgreSQL + migraciones versionadas + Docker.
- [x] Principio IV (Contrato): OpenAPI actualizado con `/api/v1` y autenticación.
- [x] Principio V (Calidad evolutiva): GET de colecciones con `Pageable` y validación de parámetros.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | N/A | N/A |
