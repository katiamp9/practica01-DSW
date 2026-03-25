# Implementation Plan: CRUD Completo de Departamentos (Gestión de Áreas)

**Branch**: `010-crud-departamentos-areas` | **Date**: 2026-03-24 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/010-crud-departamentos-areas/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar CRUD completo de departamentos con contrato backend/frontend sincronizado, incluyendo
`totalEmpleados` en el listado para habilitar columna `Personal`, bloqueo de borrado por integridad
con `409 Conflict`, tooltip y deshabilitado visual cuando hay personal asociado, y gestión reactiva
de estado + búsqueda local con Signals para una UX fluida basada en modal reutilizable.

## Technical Context

**Language/Version**: Java 17 (backend), TypeScript 5.9 + Angular 21 (frontend)  
**Primary Dependencies**: Spring Boot 3.3.2 (Web, Data JPA, Security, Validation), PostgreSQL driver, springdoc OpenAPI, Angular Signals, Reactive Forms, HttpClient, RxJS 7.8  
**Storage**: PostgreSQL (principal), H2 (pruebas)  
**Testing**: JUnit 5 + Spring Boot Test + Mockito + Spring Security Test; Jasmine/Karma en frontend  
**Target Platform**: Linux local con backend `:8080` y frontend Angular en desarrollo  
**Project Type**: Aplicación web full-stack (API REST + SPA)  
**Performance Goals**: p95 de carga de listado de departamentos < 2s; filtrado local percibido como instantáneo para tablas administrativas típicas  
**Constraints**: rutas API versionadas `/api/v1`; auth Basic obligatoria en operaciones protegidas; colecciones GET con `page`,`size`,`sort`; delete protegido con `409` cuando `totalEmpleados > 0`  
**Scale/Scope**: módulo administrativo de departamentos (listado, create, update, delete, búsqueda local, modal, feedback de error/éxito)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Phase 0 Gate Review

- **I. Stack y Versiones Obligatorias**: PASS — se mantiene Java 17 + Spring Boot 3.3.x y Angular 21.
- **II. Seguridad por Defecto con Basic Auth**: PASS — operaciones de departamentos consumidas por HttpClient con interceptor de credenciales.
- **III. Persistencia PostgreSQL y Entorno Docker**: PASS — se preserva PostgreSQL; no se requiere cambio de motor ni esquema manual fuera de migraciones.
- **IV. Contrato API Versionado y Documentado**: PASS — endpoints bajo `/api/v1/departamentos`, plan incluye actualización OpenAPI para `totalEmpleados` y errores.
- **V. Calidad Verificable y Compatibilidad Evolutiva**: PASS — incluirá pruebas de servicio/controlador y de UI para flujo protegido de delete y render reactivo.

## Project Structure

### Documentation (this feature)

```text
specs/010-crud-departamentos-areas/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── departamentos-crud-management-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
src/
├── main/java/com/example/empleados/
│   ├── controller/
│   ├── controller/dto/
│   ├── domain/
│   ├── repository/
│   └── service/
└── test/java/com/example/empleados/

frontend-empleados/
├── src/app/
│   ├── componentes/
│   ├── modelos/
│   ├── servicios/
│   └── guards/
└── src/environments/
```

**Structure Decision**: se mantiene arquitectura actual backend + frontend en el mismo repositorio, concentrando
el alcance en módulo de departamentos con cambios coordinados de controller/service/repository/DTO y componentes
Angular administrativos.

## Phase 0: Research Focus

- Definir estrategia eficiente de `totalEmpleados` (cálculo en BD con `LEFT JOIN + COUNT`) para el listado.
- Validar contrato de error de integridad para delete protegido (`409 Conflict`) y mensaje UX asociado.
- Alinear diseño de búsqueda local con Signals para evitar roundtrips innecesarios al backend.

## Phase 1: Design Focus

- Diseñar DTO de salida de listado (`DepartamentoListaDto`) con `id`, `nombre`, `totalEmpleados`.
- Definir reglas de estado UI para tabla/reactividad/modal/toast/tooltip.
- Documentar contrato API y flujo end-to-end de delete protegido y deshabilitado visual.

## Post-Phase 1 Constitution Check

- **I. Stack y Versiones Obligatorias**: PASS — diseño no introduce tecnologías fuera del stack.
- **II. Seguridad por Defecto con Basic Auth**: PASS — contrato mantiene peticiones protegidas con interceptor.
- **III. Persistencia PostgreSQL y Entorno Docker**: PASS — diseño usa consultas JPQL/proyecciones sobre PostgreSQL existente.
- **IV. Contrato API Versionado y Documentado**: PASS — contrato fija `/api/v1/departamentos` y actualización documental OpenAPI.
- **V. Calidad Verificable y Compatibilidad Evolutiva**: PASS — quickstart y criterios de prueba cubren paginación, errores y UX protegida.

## Complexity Tracking

No constitutional violations identified.

