# Tasks: Autenticación de empleados por correo y contraseña

**Input**: Design documents from `/specs/003-003-auth-empleado-login/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`

**Tests**: Se incluyen tareas de pruebas porque el plan y la constitución exigen validación automatizada para flujos críticos de seguridad.

**Organization**: Las tareas se agrupan por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura mínima y artefactos base para comenzar implementación.

- [ ] T001 Crear migración base `V3__auth_empleado.sql` con `CREATE EXTENSION IF NOT EXISTS pgcrypto;` en `src/main/resources/db/migration/V3__auth_empleado.sql`
- [ ] T002 Crear DTOs de autenticación (`LoginRequest`, `LoginSuccessResponse`, `ErrorResponse`) definiendo payload de éxito exacto `{"authenticated": true}` en `src/main/java/com/example/empleados/controller/dto/AuthDtos.java`
- [ ] T003 [P] Documentar en `.env.example` cómo configurar `${INITIAL_ADMIN_PASSWORD_HASH}` para `admin@empresa.com` sin incluir valores reales en el repositorio

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura y componentes bloqueantes que deben existir antes de cualquier historia.

**⚠️ CRITICAL**: Ninguna historia de usuario inicia antes de completar esta fase.

- [ ] T004 Implementar entidad `CuentaEmpleado` referenciando la **Clave Compuesta Lógica** (`empleados.clave`) en `src/main/java/com/example/empleados/domain/CuentaEmpleado.java`
- [ ] T005 [P] Implementar entidad `CredencialEmpleado` en `src/main/java/com/example/empleados/domain/CredencialEmpleado.java`
- [ ] T006 [P] Crear repositorio `CuentaEmpleadoRepository` en `src/main/java/com/example/empleados/repository/CuentaEmpleadoRepository.java`
- [ ] T007 [P] Crear repositorio `CredencialEmpleadoRepository` en `src/main/java/com/example/empleados/repository/CredencialEmpleadoRepository.java`
- [ ] T008 Implementar `AuthenticationAuditService` (contrato + implementación) en `src/main/java/com/example/empleados/service/AuthenticationAuditService.java`
- [ ] T009 Completar SQL de tablas `cuentas_empleado` y `credenciales_empleado` con UUID/FK/unique e INSERT de acceso inicial `admin@empresa.com` vinculado a **Clave Compuesta Lógica**, tomando el valor desde `${INITIAL_ADMIN_PASSWORD_HASH}` en `src/main/resources/db/migration/V3__auth_empleado.sql`
- [ ] T010 Extender seguridad para `POST /api/v1/auth/login` como endpoint público explícito, reemplazar `NoOpPasswordEncoder` por BCrypt y eliminar fallback `${INITIAL_ADMIN_PASSWORD}` en `src/main/java/com/example/empleados/config/SecurityConfig.java`

**Checkpoint**: Fundación lista; US1/US2/US3 pueden avanzar.

---

## Phase 3: User Story 1 - Inicio de sesión válido (Priority: P1) 🎯 MVP

**Goal**: Permitir login exitoso por correo y contraseña válidos.

**Independent Test**: `POST /api/v1/auth/login` con credenciales válidas responde `200` y registra auditoría de éxito.

### Tests for User Story 1

- [ ] T012 [P] [US1] Crear prueba unitaria de éxito de login en `src/test/java/com/example/empleados/service/AuthLoginServiceSuccessTest.java`
- [ ] T013 [P] [US1] Crear prueba de integración HTTP de login exitoso en `src/test/java/com/example/empleados/controller/AuthControllerSuccessIntegrationTest.java`

### Implementation for User Story 1

- [ ] T014 [US1] Implementar servicio de login (búsqueda por correo + validación hash + respuesta éxito) en `src/main/java/com/example/empleados/service/AuthLoginService.java`
- [ ] T015 [US1] Implementar endpoint `POST /api/v1/auth/login` en `src/main/java/com/example/empleados/controller/AuthController.java`
- [ ] T016 [US1] Registrar evento de auditoría de éxito en flujo de login en `src/main/java/com/example/empleados/service/AuthLoginService.java`
- [ ] T017 [US1] Integrar mapeo de respuesta 200 exacta `{"authenticated": true}` conforme contrato en `src/main/java/com/example/empleados/controller/AuthController.java`

**Checkpoint**: US1 funcional y comprobable de forma independiente.

---

## Phase 4: User Story 2 - Rechazo seguro de credenciales inválidas (Priority: P1)

**Goal**: Rechazar correos inexistentes o contraseñas inválidas con mensaje uniforme y estado 401.

**Independent Test**: Cualquier credencial inválida retorna `401 Unauthorized` con `Invalid email or password` sin filtrar causa.

### Tests for User Story 2

- [ ] T018 [P] [US2] Crear pruebas unitarias de fallo por correo inexistente/clave inválida en `src/test/java/com/example/empleados/service/AuthLoginServiceFailureTest.java`
- [ ] T019 [P] [US2] Crear pruebas de integración HTTP de respuesta 401 uniforme en `src/test/java/com/example/empleados/controller/AuthControllerFailureIntegrationTest.java`

### Implementation for User Story 2

- [ ] T020 [US2] Implementar excepción de credenciales inválidas con mensaje uniforme en `src/main/java/com/example/empleados/service/InvalidCredentialsException.java`
- [ ] T021 [US2] Implementar flujo de fallo y normalización de email en `src/main/java/com/example/empleados/service/AuthLoginService.java`
- [ ] T022 [US2] Mapear `InvalidCredentialsException` a `401` en `src/main/java/com/example/empleados/controller/ApiExceptionHandler.java`
- [ ] T023 [US2] Registrar auditoría de intentos fallidos en `src/main/java/com/example/empleados/service/AuthLoginService.java`

**Checkpoint**: US2 funcional y comprobable de forma independiente.

---

## Phase 5: User Story 3 - Gestión segura de credenciales (Priority: P2)

**Goal**: Garantizar almacenamiento no reversible de contraseñas y trazabilidad de cuenta/credencial.

**Independent Test**: Las credenciales persistidas contienen hash BCrypt y nunca password plano.

### Tests for User Story 3

- [ ] T024 [P] [US3] Crear prueba de integración de persistencia de hash (no texto plano) en `src/test/java/com/example/empleados/repository/CredencialEmpleadoRepositoryIntegrationTest.java`
- [ ] T025 [P] [US3] Crear prueba de configuración de encoder BCrypt en `src/test/java/com/example/empleados/config/SecurityConfigBCryptTest.java`

### Implementation for User Story 3

- [ ] T026 [US3] Implementar servicio de gestión de credenciales con hashing BCrypt en `src/main/java/com/example/empleados/service/CredencialEmpleadoService.java`
- [ ] T027 [US3] Aplicar normalización/unicidad de correo en repositorio de cuentas en `src/main/java/com/example/empleados/repository/CuentaEmpleadoRepository.java`
- [ ] T028 [US3] Enlazar creación/lectura de credenciales con flujo de login en `src/main/java/com/example/empleados/service/AuthLoginService.java`

**Checkpoint**: US3 funcional y comprobable de forma independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre transversal, documentación y verificación final.

- [ ] T029 [P] Sincronizar contrato final de login en `specs/003-003-auth-empleado-login/contracts/auth.openapi.yaml`
- [ ] T030 [P] Documentar evidencia final de pruebas, acceso inicial y escenarios en `specs/003-003-auth-empleado-login/quickstart.md`
- [ ] T031 Ejecutar Gate de Arranque obligatorio: build + tests + `docker compose -f docker/compose/docker-compose.yml up -d postgres` y registrar resultado en `specs/003-003-auth-empleado-login/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: sin dependencias.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3 (US1)**: depende de Phase 2; habilita MVP.
- **Phase 4 (US2)**: depende de base de US1 (`AuthLoginService`, `AuthController`, handler).
- **Phase 5 (US3)**: depende de entidades/repositorios/migración de Phase 2; puede ejecutarse en paralelo parcial con US2 si no hay conflicto de archivos.
- **Phase 6 (Polish)**: depende de historias objetivo completadas e incluye Gate de Arranque obligatorio.

### User Story Dependencies

- **US1**: independiente tras Foundational.
- **US2**: usa componentes de US1 para ruta de fallo y manejo uniforme de 401.
- **US3**: refuerza persistencia/hash; aporta endurecimiento de seguridad al flujo US1/US2.

### Within Each User Story

- Pruebas primero (fallando) → implementación → integración → checkpoint.

### Dependency Graph

- Setup → Foundational → US1 → {US2, US3} → Polish

---

## Parallel Execution Examples

### US1

- Ejecutar en paralelo `T012` y `T013` (archivos de prueba distintos).

### US2

- Ejecutar en paralelo `T018` y `T019` (archivos de prueba distintos).

### US3

- Ejecutar en paralelo `T024` y `T025` (pruebas en capas distintas).

---

## Implementation Strategy

### MVP First (US1)

1. Completar Phase 1 + Phase 2.
2. Completar US1 (Phase 3).
3. Validar login exitoso y auditoría.
4. Demo/despliegue de MVP.

### Incremental Delivery

1. MVP con US1.
2. Añadir US2 para endurecer respuesta de error.
3. Añadir US3 para endurecimiento de persistencia y hashing.
4. Cerrar con documentación y Gate de Arranque obligatorio en Phase 6.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego:
   - Dev A: US1/US2 (`AuthLoginService`, `AuthController`, `ApiExceptionHandler`).
   - Dev B: US3 (`CredencialEmpleadoService`, repositorios, tests de seguridad).
   - Dev C: contratos/documentación y quickstart.
