# Tasks: CRUD de Empleados

**Input**: Design documents from `/specs/archive-001-crud-empleados/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`

**Tests**: No se incluyen tareas de creación de pruebas nuevas porque la especificación no exige TDD explícito. Se incluyen tareas de ejecución de pruebas existentes.

**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Project Initialization)

**Purpose**: Alinear entorno base, configuración y documentación operativa.

- [ ] T001 Verificar dependencias y plugins de build en `pom.xml`
- [ ] T002 Configurar propiedades base de ejecución en `src/main/resources/application.yml`
- [ ] T003 [P] Definir variables de entorno de referencia en `.env.example`
- [ ] T004 [P] Configurar servicios locales de ejecución en `docker/compose/docker-compose.yml`
- [ ] T005 Configurar reglas de exclusión de build/entorno en `.gitignore`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Implementar bloques compartidos que habilitan todas las historias.

- [ ] T006 Crear migración inicial y secuencia autonumérica en `src/main/resources/db/migration/V1__create_empleados_table.sql`
- [ ] T007 [P] Configurar autenticación Basic Auth y reglas públicas mínimas en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [ ] T008 [P] Implementar entidad `Empleado` con constraints de dominio en `src/main/java/com/example/empleados/domain/Empleado.java`
- [ ] T009 [P] Implementar acceso a datos y generación de consecutivo en `src/main/java/com/example/empleados/repository/EmpleadoRepository.java`
- [ ] T010 [P] Definir DTOs de entrada/salida y errores de API en `src/main/java/com/example/empleados/controller/dto/EmpleadoDtos.java`
- [ ] T011 Implementar manejo global de excepciones HTTP en `src/main/java/com/example/empleados/controller/ApiExceptionHandler.java`
- [ ] T012 Establecer prefijo versionado `/api/v1/empleados` en `src/main/java/com/example/empleados/controller/`
- [ ] T013 Alinear contrato base (auth + versionado + errores) en `specs/archive-001-crud-empleados/contracts/empleados.openapi.yaml`

**Checkpoint**: Base técnica lista; historias de usuario habilitadas.

---

## Phase 3: User Story 1 - Registrar empleados (Priority: P1) 🎯 MVP

**Goal**: Dar de alta empleados con clave autogenerada `EMP-<consecutivo>` y validaciones de obligatoriedad/longitud.

**Independent Test Criteria**: `POST /api/v1/empleados` crea empleado válido con `201` y rechaza payload inválido o clave manual con `400`.

### Implementation for User Story 1

- [ ] T014 [P] [US1] Implementar validaciones de alta y rechazo de clave manual en `src/main/java/com/example/empleados/service/EmpleadoValidationService.java`
- [ ] T015 [P] [US1] Implementar generación de clave `EMP-<consecutivo>` en `src/main/java/com/example/empleados/service/ClaveEmpleadoGenerator.java`
- [ ] T016 [US1] Implementar excepción de colisión de clave en `src/main/java/com/example/empleados/service/ClaveCollisionException.java`
- [ ] T017 [US1] Implementar lógica de creación de empleado en `src/main/java/com/example/empleados/service/EmpleadoCreateService.java`
- [ ] T018 [US1] Implementar endpoint de creación versionado en `src/main/java/com/example/empleados/controller/EmpleadoCreateController.java`
- [ ] T019 [US1] Mapear entidad a respuesta API de creación en `src/main/java/com/example/empleados/controller/EmpleadoMapper.java`
- [ ] T020 [US1] Actualizar operación `POST /api/v1/empleados` en `specs/archive-001-crud-empleados/contracts/empleados.openapi.yaml`

**Checkpoint**: US1 implementada y demostrable de forma independiente.

---

## Phase 4: User Story 2 - Consultar empleados (Priority: P2)

**Goal**: Consultar por clave y listar empleados con paginación/ordenamiento obligatorios y validación de `422`.

**Independent Test Criteria**: `GET /api/v1/empleados/{clave}` responde `200/404`; `GET /api/v1/empleados` exige `page`+`sort`, aplica `size` default 20, límite 100 y responde `422` en parámetros inválidos.

### Implementation for User Story 2

- [ ] T021 [P] [US2] Implementar excepción de no encontrado por clave en `src/main/java/com/example/empleados/service/EmpleadoNotFoundException.java`
- [ ] T022 [P] [US2] Implementar consultas por clave y colección paginada en `src/main/java/com/example/empleados/service/EmpleadoQueryService.java`
- [ ] T023 [US2] Implementar endpoint `GET /api/v1/empleados/{clave}` en `src/main/java/com/example/empleados/controller/EmpleadoQueryController.java`
- [ ] T024 [US2] Implementar endpoint `GET /api/v1/empleados` con `Pageable` en `src/main/java/com/example/empleados/controller/EmpleadoQueryController.java`
- [ ] T025 [US2] Implementar validación de parámetros `page/size/sort` y whitelist de campos en `src/main/java/com/example/empleados/controller/EmpleadoQueryController.java`
- [ ] T026 [US2] Implementar respuesta `422` para paginación/ordenamiento inválidos en `src/main/java/com/example/empleados/controller/ApiExceptionHandler.java`
- [ ] T027 [US2] Actualizar operaciones `GET` y esquema paginado en `specs/archive-001-crud-empleados/contracts/empleados.openapi.yaml`

**Checkpoint**: US2 implementada y validable sin depender de US3.

---

## Phase 5: User Story 3 - Actualizar y eliminar empleados (Priority: P3)

**Goal**: Actualizar y eliminar empleados por clave manteniendo la inmutabilidad de `clave`.

**Independent Test Criteria**: `PUT /api/v1/empleados/{clave}` actualiza solo campos permitidos y `DELETE /api/v1/empleados/{clave}` elimina; clave inexistente devuelve `404`.

### Implementation for User Story 3

- [ ] T028 [P] [US3] Implementar validación de actualización de campos permitidos en `src/main/java/com/example/empleados/service/EmpleadoValidationService.java`
- [ ] T029 [P] [US3] Implementar lógica de actualización por clave en `src/main/java/com/example/empleados/service/EmpleadoUpdateService.java`
- [ ] T030 [P] [US3] Implementar lógica de eliminación por clave en `src/main/java/com/example/empleados/service/EmpleadoDeleteService.java`
- [ ] T031 [US3] Implementar endpoint `PUT /api/v1/empleados/{clave}` en `src/main/java/com/example/empleados/controller/EmpleadoUpdateController.java`
- [ ] T032 [US3] Implementar endpoint `DELETE /api/v1/empleados/{clave}` en `src/main/java/com/example/empleados/controller/EmpleadoDeleteController.java`
- [ ] T033 [US3] Actualizar operaciones `PUT/DELETE` en `specs/archive-001-crud-empleados/contracts/empleados.openapi.yaml`

**Checkpoint**: US3 implementada; CRUD completo con reglas de negocio preservadas.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de consistencia documental y validación reproducible.

- [ ] T034 [P] Actualizar guía de ejecución y ejemplos de validación en `specs/archive-001-crud-empleados/quickstart.md`
- [ ] T035 [P] Sincronizar decisiones finales entre diseño y planificación en `specs/archive-001-crud-empleados/plan.md`
- [ ] T036 Ejecutar suite de pruebas del proyecto usando `pom.xml`
- [ ] T037 Verificar consistencia final entre endpoints implementados y contrato en `specs/archive-001-crud-empleados/contracts/empleados.openapi.yaml`

---

## Dependencies & Execution Order

### Phase Dependencies

- Phase 1 → habilita Phase 2.
- Phase 2 → bloquea e inicializa todas las historias.
- Phase 3 (US1), Phase 4 (US2), Phase 5 (US3) → dependen de Phase 2.
- Phase 6 → depende de completar historias implementadas.

### User Story Dependencies

- US1 (P1) entrega el MVP funcional.
- US2 (P2) depende de base compartida pero no de completar US3.
- US3 (P3) depende de base compartida y puede ejecutarse tras US1/US2.

### Dependency Graph

Setup → Foundational → US1 → US2 → US3 → Polish

---

## Parallel Execution Examples

### US1

- T014 y T015 en paralelo (validación y generación de clave en archivos distintos).

### US2

- T021 y T022 en paralelo (excepción y servicio de consulta en archivos distintos).

### US3

- T029 y T030 en paralelo (servicios de actualización y eliminación en archivos distintos).

---

## Implementation Strategy

### MVP First (Recommended)

1. Completar Phase 1 y Phase 2.
2. Completar US1 (T014-T020).
3. Validar alta end-to-end por contrato.

### Incremental Delivery

1. Entregar US1 (alta).
2. Entregar US2 (consulta y listado paginado).
3. Entregar US3 (actualización y eliminación).
4. Cerrar con validación transversal (Phase 6).
