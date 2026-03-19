# Implementation Plan: CRUD de Departamentos y Relación Obligatoria

**Branch**: `004-crud-departamentos-relacion` | **Date**: 2026-03-16 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/004-crud-departamentos-relacion/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar la Funcionalidad 004 agregando CRUD completo de departamentos y una
relación obligatoria empleado→departamento en base de datos. El cambio se aplicará
vía Flyway (`V4` estructura + relación NOT NULL y `V5` datos iniciales), con
validación de existencia de departamento en alta/actualización de empleado,
preservando actualización parcial de `PUT` (solo cambia `departamentoId` si llega
en la petición). También se actualizarán DTOs y servicios para mantener activación
diferida de cuentas y se refactorizarán pruebas afectadas por la nueva restricción,
incluyendo pruebas de repositorio. El listado de departamentos se ajustará a
paginación/ordenamiento obligatorio (`page`, `size`, `sort`) y la regla
`password`→`email` en update parcial quedará validada por pruebas explícitas.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.3.x, Spring Data JPA, Spring Security, Flyway, springdoc-openapi  
**Storage**: PostgreSQL (Docker Compose en local)  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, Mockito, Spring Security Test  
**Target Platform**: Linux server / entorno local Docker  
**Project Type**: backend web-service monolítico  
**Performance Goals**: mantener comportamiento actual para operaciones CRUD internas (sin nuevos SLA formales)  
**Constraints**: seguridad Basic Auth por defecto, migraciones versionadas obligatorias, endpoints `/api/v1`, GET de colecciones con `page/size/sort`  
**Scale/Scope**: CRUD departamentos + ajustes en CRUD empleados + refactor de pruebas existentes

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Principio I (Stack): se mantiene Java 17 + Spring Boot 3.x.
- [x] Principio II (Seguridad): se conserva Basic Auth por defecto y gestión de credenciales por variables/secretos.
- [x] Principio III (Persistencia): cambios de esquema solo por Flyway con PostgreSQL y entorno Docker reproducible.
- [x] Principio IV (Contrato): endpoints nuevos/modificados bajo `/api/v1` con contrato OpenAPI actualizado y GET de colecciones con `page/size/sort`.
- [x] Principio V (Calidad): se planifica refactor de tests unitarios/integración/repositorio y verificación de build/tests.

## Project Structure

### Documentation (this feature)

```text
specs/004-crud-departamentos-relacion/
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
│   │   ├── controller/
│   │   ├── controller/dto/
│   │   ├── domain/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       └── db/migration/
└── test/
    └── java/com/example/empleados/
        ├── controller/
        ├── repository/
        └── service/
```

**Structure Decision**: se mantiene arquitectura monolítica existente, extendiendo
capas `domain/repository/service/controller` para `Departamento` y ajustando flujos
de `Empleado` para la relación obligatoria y actualización parcial.

## Phase 0: Research Focus

- Estrategia de migración segura para agregar `departamento_id NOT NULL` manteniendo enfoque de reset de BD local (`docker compose down -v` + recreación controlada) antes del gate final.
- Reglas de integridad para creación/actualización de empleado con departamento obligatorio en alta y opcional en update parcial.
- Patrón de validación de duplicados de correo al combinar actualización de empleado con activación diferida de cuenta.
- Regla explícita de validación: en update parcial, `password` sin `email` debe rechazarse.

## Phase 1: Design Outputs

- `data-model.md` con entidades `Departamento`, `Empleado` (con `departamentoId`) y relaciones.
- `contracts/departamentos-empleados.openapi.yaml` con CRUD departamentos, listado paginado/ordenado y payloads actualizados de empleado.
- `quickstart.md` con pasos de reset/migraciones `V4`+`V5`, validaciones funcionales (incluyendo paginación de departamentos) y actualización de pruebas.

## Post-Design Constitution Check

- [x] Principio I (Stack): sin cambios de stack ni runtime.
- [x] Principio II (Seguridad): sin endpoints no autorizados fuera de excepciones explícitas; secretos fuera de código.
- [x] Principio III (Persistencia): migraciones versionadas `V4`/`V5`, relación obligatoria en esquema y datos iniciales.
- [x] Principio IV (Contrato): rutas versionadas `/api/v1`, documentación OpenAPI y GET de colecciones con `page/size/sort` para departamentos.
- [x] Principio V (Calidad): plan incluye refactor de TODOS los tests afectados por `departamentoId` obligatorio en alta, incluyendo pruebas de repositorio.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | N/A | N/A |
