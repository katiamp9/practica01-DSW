# Tasks: Clave Compuesta Empleados

**Input**: Design documents from `/specs/001-empleados-clave-compuesta/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`

**Tests**: No se agregan tareas de escritura de pruebas nuevas porque la especificación no solicita TDD explícito. Se incluyen tareas de validación de pruebas existentes.

**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Project Initialization)

**Purpose**: Preparar configuración base y entorno reproducible.

- [X] T001 Verificar dependencias del proyecto en `pom.xml`
- [X] T002 Ajustar variables de conexión y seguridad en `src/main/resources/application.yml`
- [X] T003 [P] Actualizar variables de entorno de ejemplo en `.env.example`
- [X] T004 [P] Revisar stack local de servicios en `docker/compose/docker-compose.yml`
- [X] T005 Consolidar exclusiones de build y entorno en `.gitignore`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Implementar capacidades compartidas que bloquean todas las historias.

- [X] T006 Crear/ajustar secuencia y constraints de clave en `src/main/resources/db/migration/V1__create_empleados_table.sql`
- [X] T007 [P] Configurar reglas de seguridad Basic Auth en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T008 [P] Definir modelo de respuesta y errores en `src/main/java/com/example/empleados/controller/dto/EmpleadoDtos.java`
- [X] T009 [P] Ajustar entidad `Empleado` con clave compuesta lógica en `src/main/java/com/example/empleados/domain/Empleado.java`
- [X] T010 [P] Ajustar acceso a datos y consecutivo en `src/main/java/com/example/empleados/repository/EmpleadoRepository.java`
- [X] T011 Configurar manejo global de excepciones base en `src/main/java/com/example/empleados/controller/ApiExceptionHandler.java`
- [X] T012 Estandarizar prefijo versionado `/api/v1/empleados` en `src/main/java/com/example/empleados/controller/`
- [X] T013 Alinear contrato base de seguridad y versionado en `specs/001-empleados-clave-compuesta/contracts/empleados.openapi.yaml`

**Checkpoint**: Fundación lista; historias habilitadas.

---

## Phase 3: User Story 1 - Alta con clave automática (Priority: P1) 🎯 MVP

**Goal**: Registrar empleados con clave autogenerada `EMP-<consecutivo>` e impedir captura manual de clave.

**Independent Test Criteria**: `POST /api/v1/empleados` devuelve `201` con clave `EMP-<n>` y rechaza payload con `clave` manual mediante `400`.

### Implementation for User Story 1

- [X] T014 [P] [US1] Implementar validaciones de alta y rechazo de clave manual en `src/main/java/com/example/empleados/service/EmpleadoValidationService.java`
- [X] T015 [P] [US1] Implementar generación de clave con prefijo `EMP-` en `src/main/java/com/example/empleados/service/ClaveEmpleadoGenerator.java`
- [X] T016 [US1] Implementar excepción de colisión de clave en `src/main/java/com/example/empleados/service/ClaveCollisionException.java`
- [X] T017 [US1] Implementar reintentos (hasta 3) y persistencia de alta en `src/main/java/com/example/empleados/service/EmpleadoCreateService.java`
- [X] T018 [US1] Exponer endpoint `POST /api/v1/empleados` en `src/main/java/com/example/empleados/controller/EmpleadoCreateController.java`
- [X] T019 [US1] Mapear entidad a DTO de salida para alta en `src/main/java/com/example/empleados/controller/EmpleadoMapper.java`
- [X] T020 [US1] Actualizar contrato de creación con `400/409` en `specs/001-empleados-clave-compuesta/contracts/empleados.openapi.yaml`

**Checkpoint**: US1 funcional y demostrable de forma independiente.

---

## Phase 4: User Story 2 - Consulta consistente por clave y listado (Priority: P2)

**Goal**: Consultar empleados por clave y listar colección con paginación/ordenamiento válidos.

**Independent Test Criteria**: `GET /api/v1/empleados/{clave}` responde `200/404`; `GET /api/v1/empleados` exige `page`+`sort`, aplica default `size=20` y devuelve `422` para parámetros inválidos.

### Implementation for User Story 2

- [X] T021 [P] [US2] Implementar excepción de recurso inexistente en `src/main/java/com/example/empleados/service/EmpleadoNotFoundException.java`
- [X] T022 [P] [US2] Implementar consulta por clave y listado paginado en `src/main/java/com/example/empleados/service/EmpleadoQueryService.java`
- [X] T023 [US2] Implementar endpoint `GET /api/v1/empleados/{clave}` en `src/main/java/com/example/empleados/controller/EmpleadoQueryController.java`
- [X] T024 [US2] Implementar endpoint `GET /api/v1/empleados` con `Pageable` en `src/main/java/com/example/empleados/controller/EmpleadoQueryController.java`
- [X] T025 [US2] Validar reglas `page/size/sort` y whitelist de campos en `src/main/java/com/example/empleados/controller/EmpleadoQueryController.java`
- [X] T026 [US2] Implementar respuesta `422` para parámetros inválidos en `src/main/java/com/example/empleados/controller/ApiExceptionHandler.java`
- [X] T027 [US2] Actualizar contrato de consultas y esquema paginado en `specs/001-empleados-clave-compuesta/contracts/empleados.openapi.yaml`

**Checkpoint**: US2 funcional y verificable sin US3.

---

## Phase 5: User Story 3 - Mantenimiento sin alterar identificador (Priority: P3)

**Goal**: Actualizar y eliminar empleados sin mutar la clave de negocio.

**Independent Test Criteria**: `PUT /api/v1/empleados/{clave}` actualiza campos permitidos manteniendo clave; `DELETE /api/v1/empleados/{clave}` elimina; claves inexistentes devuelven `404`.

### Implementation for User Story 3

- [X] T028 [P] [US3] Ajustar validación de update para impedir cambio de clave en `src/main/java/com/example/empleados/service/EmpleadoValidationService.java`
- [X] T029 [P] [US3] Implementar actualización por clave en `src/main/java/com/example/empleados/service/EmpleadoUpdateService.java`
- [X] T030 [P] [US3] Implementar eliminación por clave en `src/main/java/com/example/empleados/service/EmpleadoDeleteService.java`
- [X] T031 [US3] Exponer endpoint `PUT /api/v1/empleados/{clave}` en `src/main/java/com/example/empleados/controller/EmpleadoUpdateController.java`
- [X] T032 [US3] Exponer endpoint `DELETE /api/v1/empleados/{clave}` en `src/main/java/com/example/empleados/controller/EmpleadoDeleteController.java`
- [X] T033 [US3] Actualizar contrato para `PUT/DELETE` y errores `404` en `specs/001-empleados-clave-compuesta/contracts/empleados.openapi.yaml`

**Checkpoint**: US3 completa el CRUD sin comprometer inmutabilidad de clave.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cerrar consistencia documental y validación reproducible.

- [X] T034 [P] Actualizar guía de validación funcional en `specs/001-empleados-clave-compuesta/quickstart.md`
- [X] T035 [P] Sincronizar decisiones de implementación en `specs/001-empleados-clave-compuesta/plan.md`
- [X] T036 Ejecutar validación de build/tests sobre `pom.xml`
- [X] T037 Verificar consistencia final código-contrato en `specs/001-empleados-clave-compuesta/contracts/empleados.openapi.yaml`

---

## Dependencies & Execution Order

### Phase Dependencies

- Phase 1 (Setup): sin dependencias.
- Phase 2 (Foundational): depende de Phase 1 y bloquea historias.
- Phase 3/4/5 (US1/US2/US3): dependen de Phase 2.
- Phase 6 (Polish): depende de historias completadas.

### User Story Dependencies

- US1 (P1): primer incremento funcional (MVP).
- US2 (P2): depende de base compartida; no depende de finalizar US3.
- US3 (P3): depende de base compartida; puede ejecutarse tras US1/US2.

### Dependency Graph

Setup → Foundational → US1 → US2 → US3 → Polish

---

## Parallel Execution Examples

### US1

- T014 y T015 pueden ejecutarse en paralelo (servicios distintos).

### US2

- T021 y T022 pueden ejecutarse en paralelo (excepción y servicio).

### US3

- T029 y T030 pueden ejecutarse en paralelo (servicios de update/delete).

---

## Implementation Strategy

### MVP First (Recommended)

1. Completar Phase 1 y Phase 2.
2. Completar US1 (T014-T020).
3. Validar flujo de alta con clave compuesta.

### Incremental Delivery

1. Entregar US1 (alta con clave automática).
2. Entregar US2 (consulta y listado paginado).
3. Entregar US3 (update/delete con clave inmutable).
4. Cerrar con validación transversal en Phase 6.
