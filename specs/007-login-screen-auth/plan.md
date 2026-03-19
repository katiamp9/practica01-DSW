# Implementation Plan: Diseño del Login

**Branch**: `007-login-screen-auth` | **Date**: 2026-03-19 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/007-login-screen-auth/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar un flujo de login frontend profesional con formularios reactivos,
gestión de sesión con Signals, persistencia en localStorage, redirección inicial
por rol (`ROLE_ADMIN`/`ROLE_USER`), guard de autenticación+autorización por ruta
y propagación de credenciales Basic Auth en cada llamada HTTP al backend.

## Technical Context

**Language/Version**: TypeScript 5.9 + Angular 21 (frontend), Java 17 + Spring Boot 3.3.x (backend ya existente)  
**Primary Dependencies**: Angular standalone APIs, Signals, Reactive Forms, Angular Router, HttpClient, Spring Security Basic Auth  
**Storage**: localStorage en frontend para sesión/credenciales/rol; PostgreSQL en backend sin cambios de esquema para esta feature  
**Testing**: Angular test runner (unit/component), validación backend existente con Maven tests  
**Target Platform**: Linux local en desarrollo (`frontend :4200`, `backend :8080`)  
**Project Type**: aplicación web full-stack (SPA Angular + API Spring Boot)  
**Performance Goals**: login y redirección inicial en < 20s para usuarios válidos; guard sin latencia perceptible de navegación  
**Constraints**: mantener Basic Auth vigente, usar `inject()` + standalone + signals por constitución, mantener prefijo API `/api/v1`  
**Scale/Scope**: pantalla login + AuthService + guard/interceptor + vistas base `admin-dashboard` y `home`
**API Contract (login)**: `POST /api/v1/auth/login` con payload `{ "username": "...", "password": "..." }` y respuesta mínima `{ "rol": "...", "nombre": "..." }`

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Principio I (Stack): backend se mantiene en Java 17/Spring Boot 3.x; frontend permanece Angular 21.
- [x] Principio II (Seguridad): se conserva Basic Auth y se refuerza control por sesión/rol en frontend.
- [x] Principio III (Persistencia): sin cambios de DB; persistencia frontend limitada a sesión local.
- [x] Principio IV (Contrato): consumo de APIs versionadas bajo `/api/v1`.
- [x] Principio V (Calidad): plan incluye validación funcional de login, guard por rol e interceptor en flujo reproducible.

## Project Structure

### Documentation (this feature)

```text
specs/007-login-screen-auth/
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
│   ├── config/
│   ├── controller/
│   ├── repository/
│   └── service/
└── test/java/com/example/empleados/
    ├── config/
    ├── controller/
    ├── repository/
    └── service/

frontend-empleados/
├── src/app/
│   ├── componentes/
│   ├── guards/
│   ├── interceptores/
│   ├── modelos/
│   └── servicios/
└── src/environments/
```

**Structure Decision**: se usa estructura web application existente con backend y frontend separados; la implementación de la feature 007 se concentra en `frontend-empleados/` y solo consume contrato backend ya disponible.

## Phase 0: Research Focus

- Definir estrategia de almacenamiento local mínimo para Basic Auth + rol con Signals.
- Establecer patrón de guard por rol en Angular 21 sin duplicación de lógica en componentes.
- Definir contrato frontend de login (éxito/error) alineado a backend actual.
- Determinar lineamientos de UI profesional para login/home/dashboard usando skill de diseño.

## Phase 1: Design Outputs

- `data-model.md` con `LoginSession`, `UserIdentity` y `RouteAccessPolicy`.
- `contracts/login-flow-contract.md` con contrato exacto de `POST /api/v1/auth/login` (payload/response) y reglas de guard/interceptor.
- `quickstart.md` con ejecución local y checklist de validación de login por rol.

## Post-Design Constitution Check

- [x] Principio I (Stack): diseño mantiene frontend Angular 21 y backend Java/Spring vigente.
- [x] Principio II (Seguridad): diseño conserva Basic Auth y define limpieza de sesión ante inconsistencias de rol.
- [x] Principio III (Persistencia): no introduce cambios de esquema ni migraciones.
- [x] Principio IV (Contrato): diseño consume rutas `/api/v1` y exige rol en respuesta de login.
- [x] Principio V (Calidad): diseño contempla validaciones reproducibles de login, guard e interceptor.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | N/A | N/A |

## Implementation Result (2026-03-19)

- Se implementó routing con `provideRouter` y rutas públicas/privadas (`/login`, `/admin-dashboard`, `/home`).
- Se implementó `AuthService` con Signals y persistencia de `basicAuthToken`, `role`, `email`, `nombre`.
- Se implementaron `AuthApiService`, `AuthGuard`, `RoleGuard` y `AuthInterceptor` alineados al contrato `POST /api/v1/auth/login`.
- Se implementaron componentes standalone para login, home y dashboard admin con estilos consistentes.
- Se añadieron pruebas unitarias de auth (servicio/guard/interceptor) pendientes de ejecución local por versión de Node no compatible en el entorno actual.
