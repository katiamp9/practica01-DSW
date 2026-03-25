# Implementation Plan: CRUD Completo de Empleados

**Branch**: `009-crud-empleados-completo` | **Date**: 2026-03-21 | **Spec**: [specs/009-crud-empleados-completo/spec.md](specs/009-crud-empleados-completo/spec.md)
**Input**: Feature specification from `/specs/009-crud-empleados-completo/spec.md`

## Summary

Consolidar el CRUD administrativo de empleados con formulario unificado create/edit, carga reactiva de departamentos, protección de eliminación de cuenta crítica, y cierre de brechas de visibilidad de `email`: backend con `LEFT JOIN` a cuentas en listados, contrato canónico `email`, fallback visual `Sin correo`, precarga en edición con campo `readonly`, e inmutabilidad de `email` validada en backend (`400` si se intenta modificar).

## Technical Context

**Language/Version**: Java 17 (backend), TypeScript 5.9 (frontend Angular 21)  
**Primary Dependencies**: Spring Boot 3.3.2 (Web, Data JPA, Security, Validation), Flyway, PostgreSQL driver, springdoc OpenAPI 2.6.0, Angular 21, RxJS 7.8  
**Storage**: PostgreSQL (principal), H2 (pruebas)  
**Testing**: JUnit 5 + Spring Boot Test + Mockito + Spring Security Test; Jasmine/Karma para Angular  
**Target Platform**: Linux con backend Spring Boot y frontend SPA Angular  
**Project Type**: Aplicación web con backend API + frontend separado  
**Performance Goals**: p95 de carga de listado de empleados < 2s en entorno normal (SC-002)  
**Constraints**: autenticación Basic Auth en endpoints protegidos; APIs versionadas `/api/v1`; GET de colecciones con `page,size,sort`; `email` inmutable en edición  
**Scale/Scope**: Módulo administrativo de empleados y departamentos en una sola vista, con CRUD completo y validaciones de negocio

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Phase 0 Gate Review

- **I. Stack y Versiones Obligatorias**: PASS — Spring Boot 3.3.2 + Java 17 y Angular 21 en `frontend-empleados`.
- **II. Seguridad por Defecto con Basic Auth**: PASS — el flujo mantiene endpoints protegidos y uso de interceptor/credenciales.
- **III. Persistencia PostgreSQL y Entorno Docker**: PASS — PostgreSQL + Flyway + Docker Compose ya vigente.
- **IV. Contrato API Versionado y Documentado**: PASS — rutas bajo `/api/v1` y contrato de respuesta actualizado para `email`.
- **V. Calidad Verificable y Compatibilidad Evolutiva**: PASS — pruebas unitarias/integración y parámetros `page,size,sort` en GET de colecciones.

### Post-Phase 1 Design Re-check

- PASS — Diseño de entidades, contrato y quickstart preservan los cinco principios sin excepciones.

## Project Structure

### Documentation (this feature)

```text
specs/009-crud-empleados-completo/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── empleados-crud-management-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
src/
├── main/
│   ├── java/com/example/empleados/
│   │   ├── controller/
│   │   ├── controller/dto/
│   │   ├── domain/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       └── db/migration/
└── test/java/com/example/empleados/

frontend-empleados/
├── src/app/
│   ├── componentes/
│   ├── pages/
│   ├── servicios/
│   └── auth/
└── src/environments/
```

**Structure Decision**: Arquitectura web app de dos capas existentes en monorepo (`src` backend + `frontend-empleados`), manteniendo separación de responsabilidades API/UI y pruebas por capa.

## Phase 0: Research Focus

- Definir estrategia de obtención de `email` en listado (`LEFT JOIN` con cuentas) sin excluir empleados sin cuenta.
- Definir semántica contractual para `email` nulo (`null` en backend, `Sin correo` en UI).
- Acordar política de edición: `email` visible y `readonly` en formulario, validación backend de inmutabilidad con `400`.

## Phase 1: Design Focus

- Ajustar modelo funcional para separar identidad de cuenta (`email`) y datos editables de empleado.
- Actualizar contrato API CRUD y reglas de UI para columna Email y formulario de edición.
- Documentar pasos de validación end-to-end en quickstart para listar/editar/bloquear cambios de `email`.

## Complexity Tracking

No constitutional violations identified.
