# Implementation Plan: Admin Dashboard Home & Navigation

**Branch**: `008-admin-dashboard-navigation` | **Date**: 2026-03-21 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/008-admin-dashboard-navigation/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar un dashboard administrativo con navegación superior (Inicio, Empleados,
Departamentos), tabla paginada de empleados (10 por página inicial), estados de
UI claros (cargando, vacío, error con reintento), y placeholders “Próximamente”
en rutas administrativas secundarias protegidas por sesión y rol.

## Technical Context

**Language/Version**: TypeScript 5.9 + Angular 21 (frontend), Java 17 + Spring Boot 3.3.x (backend existente)  
**Primary Dependencies**: Angular standalone APIs, Signals, Angular Router, HttpClient, Spring Security Basic Auth, Spring Data JPA Pageable  
**Storage**: localStorage para sesión en frontend; PostgreSQL en backend (sin cambios de esquema)  
**Testing**: Angular unit/component tests (Karma/Jasmine), Maven tests para backend existentes  
**Target Platform**: Linux local de desarrollo (frontend `:4200`, backend `:8080`)
**Project Type**: aplicación web full-stack (SPA Angular + API REST Spring Boot)  
**Performance Goals**: carga inicial de tabla de empleados < 2 segundos en entorno local estable para respuestas exitosas  
**Constraints**: mantener Basic Auth vigente; consumir `/api/v1/empleados` enviando `page`, `size`, `sort`; no romper rutas actuales de login/roles  
**Scale/Scope**: dashboard admin + navegación + placeholders + consumo paginado de empleados con tamaño inicial 10

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Principio I (Stack y versiones): se mantiene Java 17 + Spring Boot 3.x en backend y Angular 21 en frontend.
- [x] Principio II (Seguridad Basic Auth): el dashboard y rutas admin siguen protegidos por guard de sesión/rol y consumo de API autenticada.
- [x] Principio III (PostgreSQL + Docker): no hay cambios de persistencia ni migraciones; se reutiliza backend PostgreSQL existente.
- [x] Principio IV (API versionada/documentada): se consume contrato existente bajo `/api/v1/empleados` y se preserva prefijo versionado.
- [x] Principio V (Calidad + compatibilidad evolutiva): el diseño exige paginación `page/size/sort` en GET de colección y define validaciones reproducibles de UI/flujo.

## Project Structure

### Documentation (this feature)

```text
specs/008-admin-dashboard-navigation/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
└── tasks.md
```

### Source Code (repository root)

```text
src/
├── main/java/com/example/empleados/
│   ├── controller/
│   ├── controller/dto/
│   ├── service/
│   └── repository/
└── test/java/com/example/empleados/

frontend-empleados/
├── src/app/
│   ├── componentes/
│   ├── guards/
│   ├── interceptores/
│   ├── modelos/
│   └── servicios/
└── src/environments/
```

**Structure Decision**: Se mantiene la estructura actual de aplicación web con backend y frontend
separados; la implementación de la feature 008 se concentra en
`frontend-empleados/src/app` reutilizando el contrato backend existente.

## Phase 0: Research Focus

- Definir contrato frontend para consumir paginación obligatoria de empleados (`page`, `size`, `sort`).
- Definir estrategia de estado reactivo del dashboard para carga, vacío, error y datos.
- Definir patrón de navegación admin con rutas placeholders protegidas.
- Determinar lineamientos visuales de tabla, etiquetas de rol y feedback de error/reintento.

## Phase 1: Design Outputs

- `data-model.md` con `EmpleadoListado`, `EstadoDashboardAdmin`, `PaginacionDashboard` y `ItemNavegacionAdmin`.
- `contracts/admin-dashboard-navigation-contract.md` con contrato UI/API de navegación, paginación y manejo de errores.
- `quickstart.md` con validación local de rutas admin, tabla, paginación y acción manual “Reintentar”.

## Post-Design Constitution Check

- [x] Principio I (Stack y versiones): diseño permanece en Angular 21 + backend Java 17/Spring Boot 3.x.
- [x] Principio II (Seguridad Basic Auth): diseño mantiene guard de sesión/rol e interceptor para requests protegidas.
- [x] Principio III (PostgreSQL + Docker): no introduce cambios de modelo de datos ni migraciones.
- [x] Principio IV (API versionada/documentada): diseño mantiene consumo de `/api/v1/empleados` y rutas versionadas vigentes.
- [x] Principio V (Calidad + paginación): diseño obliga uso de `page`, `size`, `sort` y checklist reproducible para flujo completo.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | N/A | N/A |

## Implementation Result (2026-03-21)

- Se implementó `EmpleadoQueryService` con `HttpClient` para consultas paginadas de `/api/v1/empleados` usando `page`, `size` y `sort`.
- Se implementó dashboard administrativo reactivo con Signals, tabla de empleados (`nombre`, `clave`, `rol`), badges de rol y paginación `Anterior/Siguiente`.
- Se implementaron estados de UI: `loading`, `empty` (mensaje amigable) y `error` (botón “Reintentar”).
- Se añadieron rutas protegidas para placeholders:
  - `/admin/empleados`
  - `/admin/departamentos`
- Se añadieron pruebas unitarias para servicio y componentes principales del módulo admin.
- Evidencia de validación:
  - `npm test -- --watch=false --browsers=ChromeHeadless` → `TOTAL: 13 SUCCESS`
  - `npm run build` → build exitoso
