# Implementation Plan: Autenticación de empleados por correo y contraseña

**Branch**: `003-003-auth-empleado-login` | **Date**: 2026-03-14 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/003-003-auth-empleado-login/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Agregar autenticación de empleados por correo y contraseña bajo `/api/v1/auth/login`,
persistiendo cuentas/credenciales en PostgreSQL con migración Flyway V3 y UUID vía
`gen_random_uuid()`, validando contraseña con BCrypt y registrando intentos mediante
`AuthenticationAuditService`. Se mantiene Basic Auth como política global y se define
`/api/v1/auth/login` como endpoint público explícito para bootstrap de autenticación.
La migración V3 incluirá habilitación de `pgcrypto`, relación contra la **Clave
Compuesta Lógica** (`empleados.clave`) y creación del acceso inicial
`admin@empresa.com` con hash inicial `${INITIAL_ADMIN_PASSWORD_HASH}` provisto por
variables de entorno/secretos (sin credenciales en texto plano en repositorio).

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.3.x, Spring Security, Spring Data JPA, Flyway, springdoc-openapi  
**Storage**: PostgreSQL (tabla existente `empleados` + nuevas tablas `cuentas_empleado` y `credenciales_empleado`)  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, spring-security-test  
**Target Platform**: Linux server + Docker Compose para entorno local  
**Project Type**: backend web-service monolítico  
**Performance Goals**: foco de este alcance en seguridad/correctitud funcional; sin SLA de latencia nuevo en esta iteración  
**Constraints**: cumplir Constitución v1.1.0 (Basic Auth por defecto, `/api/v1`, PostgreSQL + Flyway, OpenAPI, pruebas automatizadas)  
**Scale/Scope**: autenticación para empleados del CRUD actual (catálogo interno, carga baja-media)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Principio I (Stack): se mantiene Java 17 + Spring Boot 3.x.
- [x] Principio II (Seguridad): se mantiene Basic Auth por defecto; `POST /api/v1/auth/login` se declara público explícito en Spec y configuración de seguridad; sin fallback de credenciales en texto plano.
- [x] Principio III (Persistencia): PostgreSQL + migración Flyway versionada (`V3`) + ejecución reproducible con Docker Compose.
- [x] Principio IV (Contrato): endpoint nuevo bajo `/api/v1/...` documentado en OpenAPI con requisitos de autenticación y payload de éxito `200` definido de forma exacta.
- [x] Principio V (Calidad): pruebas unitarias/integración para login y auditoría; gate de cierre obligatorio con build + tests + arranque de docker-compose; sin impacto en reglas de paginación de GET de colecciones existentes.

## Project Structure

### Documentation (this feature)

```text
specs/003-003-auth-empleado-login/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── auth.openapi.yaml
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

**Structure Decision**: Se mantiene la estructura monolítica existente y se extiende en los paquetes `domain/repository/service/controller/config`, evitando modularización adicional por no aportar valor en este alcance.

## Phase 0: Research Output

`research.md` documenta y resuelve: estrategia de referencia de `cuentas_empleado`
hacia `empleados.clave` como **Clave Compuesta Lógica**, patrón de hash con BCrypt,
convivencia con Basic Auth constitucional, política de error 401 uniforme, y diseño
de auditoría de login.

## Phase 1: Design Output

- `data-model.md`: define entidades `CuentaEmpleado`, `CredencialEmpleado` y `AuthenticationAuditEvent`, con relaciones y validaciones.
- `contracts/auth.openapi.yaml`: define contrato de `POST /api/v1/auth/login` (público) con respuesta de éxito exacta `200` (`{"authenticated": true}`) y respuesta `401` uniforme.
- `quickstart.md`: define pasos reproducibles para levantar DB, ejecutar migraciones, probar login y validar auditoría.

## Post-Design Constitution Check

- [x] Principio I (Stack): sin cambios de stack ni versión.
- [x] Principio II (Seguridad): se reemplaza `NoOpPasswordEncoder` por BCrypt para credenciales de empleados, se elimina fallback de credenciales en código y se mantiene Basic Auth por defecto.
- [x] Principio III (Persistencia): cambios en esquema solo vía Flyway V3 usando PostgreSQL (`gen_random_uuid()`) y semilla con secreto de entorno.
- [x] Principio IV (Contrato): endpoint público de login documentado en OpenAPI y Spec, versionado `/api/v1`, con payload `200` explícito.
- [x] Principio V (Calidad evolutiva): cobertura de pruebas para éxito/fallo de login y auditoría, gate obligatorio de build + tests + arranque docker-compose, sin degradar reglas existentes de GET paginado.

## Mandatory Gate de Arranque

Antes de cierre de la especificación implementada, MUST ejecutarse y pasar la secuencia:

1. Build de proyecto.
2. Suite de pruebas automatizadas.
3. Levantamiento de entorno mínimo con `docker compose`.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | N/A | N/A |
