# Implementation Plan: Soporte de Roles y Autorización

**Branch**: `006-roles-autorizacion-back` | **Date**: 2026-03-19 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/006-roles-autorizacion-back/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Extender el modelo de seguridad backend con soporte de roles persistidos,
agregando `rol` a `Empleado` con valor por defecto `ROLE_USER`, garantizando
backfill para registros existentes y actualizando la carga del administrador
inicial para `ROLE_ADMIN`. La capa de autenticación/autorización debe mapear el
rol persistido a `GrantedAuthority` sin romper el flujo actual de Basic Auth.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.3.x, Spring Security, Spring Data JPA, Flyway  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, Spring Boot Test, pruebas unitarias e integración  
**Target Platform**: Linux local + Docker Compose para PostgreSQL  
**Project Type**: backend web-service monolítico  
**Performance Goals**: sin degradación perceptible en autenticación y consultas de usuario  
**Constraints**: Basic Auth vigente, esquema DB versionado por Flyway, compatibilidad total con datos actuales  
**Scale/Scope**: campo de rol en empleados + migración/backfill + mapeo a authorities en seguridad

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Principio I (Stack): se mantiene Java 17 + Spring Boot 3.x sin cambios de runtime.
- [x] Principio II (Seguridad): Basic Auth permanece activa; solo se añade autorización por rol persistido.
- [x] Principio III (Persistencia): cambios de esquema/datos se harán con migración Flyway versionada.
- [x] Principio IV (Contrato): no se alteran prefijos versionados `/api/v1` ni se rompen contratos existentes.
- [x] Principio V (Calidad): plan contempla pruebas unitarias/integración y validación reproducible de build/tests.

## Project Structure

### Documentation (this feature)

```text
specs/006-roles-autorizacion-back/
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
├── main/
│   ├── java/com/example/empleados/
│   │   ├── config/
│   │   ├── domain/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       └── db/migration/
└── test/
    └── java/com/example/empleados/
        ├── config/
        └── service/
```

**Structure Decision**: se mantiene la arquitectura backend existente y se extienden únicamente capas `domain`, `service`, `config` y migraciones para introducir soporte de rol sin cambios de topología de proyecto.

## Phase 0: Research Focus

- Definir formato de rol persistido (`String` vs `Enum`) compatible con DB actual y evolución futura.
- Diseñar estrategia de backfill para empleados existentes sin impactar disponibilidad.
- Establecer patrón de mapeo de rol persistido a `GrantedAuthority` en Spring Security.
- Confirmar actualización del seed de administrador inicial con `ROLE_ADMIN` en migraciones vigentes.

## Phase 1: Design Outputs

- `data-model.md` con entidades `Empleado`, `RolDeAcceso` y `AdministradorInicial` actualizadas.
- `contracts/roles-authorities-contract.md` con contrato de mapeo rol→authority y reglas de compatibilidad.
- `quickstart.md` con pasos de migración, validación de defaults/backfill y verificación de authorities.

## Post-Design Constitution Check

- [x] Principio I (Stack): diseño sigue en Java 17 + Spring Boot 3.x.
- [x] Principio II (Seguridad): diseño mantiene Basic Auth y agrega autorización sin exponer credenciales.
- [x] Principio III (Persistencia): diseño especifica migración versionada y respaldo para datos existentes.
- [x] Principio IV (Contrato): diseño no introduce endpoints fuera de `/api/v1`.
- [x] Principio V (Calidad): diseño incorpora validación automatizada de defaults, backfill y authorities.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | N/A | N/A |

## Implementation Results (2026-03-19)

- Migraciones alineadas para roles: creación con default en `V1`, seed admin con `ROLE_ADMIN` en `V3` y backfill/not-null en `V6`.
- Dominio y servicios actualizados para normalizar y preservar `rol` válido (`ROLE_USER`/`ROLE_ADMIN`).
- Seguridad integrada con `EmpleadoUserDetailsService` para mapear rol persistido a `GrantedAuthority`.
- Repositorio de cuentas optimizado con carga de `empleado` y `credencial` para autenticación.
- Validación de calidad completada con `mvn test` exitoso (62 pruebas, 0 fallos, 0 errores).
