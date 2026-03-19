# Tasks: CRUD de Departamentos y Relación Obligatoria

**Input**: Design documents from `/specs/004-crud-departamentos-relacion/`
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: Se incluyen tareas de pruebas porque la especificación exige refactor total de tests afectados (FR-015).

**Organization**: Las tareas se agrupan por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar esquema base y contratos de entrada/salida impactados.

- [X] T001 Crear migración `V4__create_departamentos_table.sql` con tabla `departamentos` y columna `departamento_id` NOT NULL (enfocada a entorno con reset de BD) en `src/main/resources/db/migration/V4__create_departamentos_table.sql`
- [X] T002 Crear migración de semillas `V5__insert_default_departments.sql` con Sistemas, RH y Ventas en `src/main/resources/db/migration/V5__insert_default_departments.sql`
- [X] T003 [P] Extender DTOs de empleado para `departamentoId` (obligatorio en create, opcional en update) en `src/main/java/com/example/empleados/controller/dto/EmpleadoDtos.java`
- [X] T034 [P] Documentar y validar procedimiento de reset de BD previo a aplicar `V4`/`V5` (`docker compose down -v` + recreación) en `specs/004-crud-departamentos-relacion/quickstart.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura de dominio y validación compartida para departamentos y empleado-departamento.

**⚠️ CRITICAL**: Ninguna historia inicia antes de completar esta fase.

- [X] T004 Crear entidad JPA `Departamento` en `src/main/java/com/example/empleados/domain/Departamento.java`
- [X] T005 [P] Crear repositorio `DepartamentoRepository` en `src/main/java/com/example/empleados/repository/DepartamentoRepository.java`
- [X] T006 Actualizar entidad `Empleado` para relación obligatoria con `Departamento` en `src/main/java/com/example/empleados/domain/Empleado.java`
- [X] T007 Implementar excepción de dominio para departamento inexistente en `src/main/java/com/example/empleados/service/DepartamentoNotFoundException.java`
- [X] T008 Implementar servicio de validación de departamento existente en `src/main/java/com/example/empleados/service/DepartamentoValidationService.java`
- [X] T009 Extender `ApiExceptionHandler` para mapear errores de departamento a códigos HTTP consistentes en `src/main/java/com/example/empleados/controller/ApiExceptionHandler.java`

**Checkpoint**: Base de dominio y validación lista; US1/US2/US3 pueden avanzar.

---

## Phase 3: User Story 1 - Gestión de departamentos (Priority: P1) 🎯 MVP

**Goal**: Habilitar CRUD completo de departamentos por API.

**Independent Test**: Crear, listar, consultar, actualizar y eliminar departamentos vía `/api/v1/departamentos`, incluyendo paginación/ordenamiento obligatorio en listados.

### Tests for User Story 1

- [X] T010 [P] [US1] Crear pruebas unitarias de servicio de departamentos en `src/test/java/com/example/empleados/service/DepartamentoServiceTest.java`
- [X] T011 [P] [US1] Crear pruebas de integración de controller para CRUD departamentos, incluyendo requerimiento de `page/size/sort` y validación de ordenamiento en `src/test/java/com/example/empleados/controller/DepartamentoControllerIntegrationTest.java`

### Implementation for User Story 1

- [X] T012 [US1] Crear DTOs de departamento (`DepartamentoRequest`, `DepartamentoResponse`) en `src/main/java/com/example/empleados/controller/dto/DepartamentoDtos.java`
- [X] T013 [US1] Implementar `DepartamentoService` con create/list/get/update/delete y listado paginado/ordenado en `src/main/java/com/example/empleados/service/DepartamentoService.java`
- [X] T014 [US1] Implementar `DepartamentoController` bajo `/api/v1/departamentos` exigiendo `page/size/sort` para GET de colección en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [X] T015 [US1] Alinear validaciones de eliminación para conflicto por empleados asociados en `src/main/java/com/example/empleados/service/DepartamentoService.java`

**Checkpoint**: US1 funcional y validable de forma independiente.

---

## Phase 4: User Story 2 - Alta de empleado con departamento obligatorio (Priority: P1)

**Goal**: Forzar departamento en creación de empleado y validar su existencia antes de persistir.

**Independent Test**: `POST /api/v1/empleados` rechaza ausencia/inexistencia de `departamentoId` y acepta altas válidas.

### Tests for User Story 2

- [X] T016 [P] [US2] Extender pruebas unitarias de creación de empleado para `departamentoId` obligatorio en `src/test/java/com/example/empleados/service/EmpleadoCreateServiceTest.java`
- [X] T017 [P] [US2] Refactorizar pruebas de validación de empleado para reglas de departamento en `src/test/java/com/example/empleados/service/EmpleadoValidationServiceTest.java`
- [X] T018 [P] [US2] Actualizar pruebas integrales de controller de empleados con `departamentoId` válido en `src/test/java/com/example/empleados/controller/EmpleadoControllerIntegrationTest.java`
- [X] T035 [P] [US2] Refactorizar pruebas de repositorio impactadas por `departamento` obligatorio en fixtures de `Empleado` en `src/test/java/com/example/empleados/repository/CredencialEmpleadoRepositoryIntegrationTest.java`

### Implementation for User Story 2

- [X] T019 [US2] Actualizar `EmpleadoValidationService` para exigir `departamentoId` en create y validar reglas de negocio en `src/main/java/com/example/empleados/service/EmpleadoValidationService.java`
- [X] T020 [US2] Integrar validación de existencia de departamento en flujo de creación en `src/main/java/com/example/empleados/service/EmpleadoCreateService.java`
- [X] T021 [US2] Persistir vínculo `empleado.departamento` en creación de empleado en `src/main/java/com/example/empleados/service/EmpleadoCreateService.java`
- [X] T022 [US2] Mantener creación opcional de cuenta en alta de empleado con transaccionalidad en `src/main/java/com/example/empleados/service/EmpleadoCreateService.java`

**Checkpoint**: US2 funcional y validable de forma independiente.

---

## Phase 5: User Story 3 - Actualización parcial y activación diferida (Priority: P2)

**Goal**: Permitir PUT parcial con `departamentoId` opcional y activación/actualización de cuenta por `email/password`.

**Independent Test**: PUT con solo `email/password` conserva datos del empleado; PUT con `departamentoId` actualiza solo departamento; errores por duplicidad o departamento inválido son transaccionales.

### Tests for User Story 3

- [X] T023 [P] [US3] Extender pruebas unitarias de update parcial de empleado en `src/test/java/com/example/empleados/service/EmpleadoUpdateServiceTest.java`
- [X] T024 [P] [US3] Extender pruebas de `CredencialEmpleadoService` para alta diferida/update con empleado, duplicidad de correo y regla `password` sin `email` en `src/test/java/com/example/empleados/service/CredencialEmpleadoServiceTest.java`
- [X] T025 [P] [US3] Ajustar pruebas de autenticación impactadas por relación de departamento en `src/test/java/com/example/empleados/service/AuthLoginServiceSuccessTest.java`
- [X] T026 [P] [US3] Ajustar pruebas de autenticación de fallo impactadas por modelo empleado/departamento en `src/test/java/com/example/empleados/service/AuthLoginServiceFailureTest.java`
- [X] T036 [P] [US3] Añadir prueba de integración para rechazo de update parcial con `password` sin `email` en `src/test/java/com/example/empleados/controller/EmpleadoControllerIntegrationTest.java`

### Implementation for User Story 3

- [X] T027 [US3] Actualizar `EmpleadoUpdateService` para actualización parcial de campos no nulos y `departamentoId` opcional en `src/main/java/com/example/empleados/service/EmpleadoUpdateService.java`
- [X] T028 [US3] Validar existencia de departamento solo cuando `departamentoId` llegue en PUT en `src/main/java/com/example/empleados/service/EmpleadoUpdateService.java`
- [X] T029 [US3] Mantener/fortalecer lógica de activación diferida y update de credenciales (BCrypt + correo único), rechazando `password` cuando `email` no esté presente en `src/main/java/com/example/empleados/service/CredencialEmpleadoService.java`
- [X] T030 [US3] Asegurar transaccionalidad de update empleado+cuenta ante errores de correo duplicado en `src/main/java/com/example/empleados/service/EmpleadoUpdateService.java`

**Checkpoint**: US3 funcional y validable de forma independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de documentación, contrato y gate final.

- [X] T031 [P] Sincronizar contrato OpenAPI final con respuestas/payloads definitivos y paginación/ordenamiento obligatorio en `GET /api/v1/departamentos` en `specs/004-crud-departamentos-relacion/contracts/departamentos-empleados.openapi.yaml`
- [X] T032 [P] Actualizar quickstart con evidencia final de V4/V5, CRUD departamentos y casos create/update empleado en `specs/004-crud-departamentos-relacion/quickstart.md`
- [ ] T033 Ejecutar gate final (`mvn -DskipTests package` + `mvn test` + arranque postgres con docker compose) y registrar resultados en `specs/004-crud-departamentos-relacion/quickstart.md` *(build+tests OK; pendiente arranque docker por permisos de socket)*

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: sin dependencias.
- **Phase 2 (Foundational)**: depende de Setup; bloquea todas las historias.
- **Phase 3 (US1)**: depende de Foundational; habilita CRUD de departamentos con paginación/ordenamiento en listados.
- **Phase 4 (US2)**: depende de Foundational (y usa catálogo de departamentos disponible por semillas/migraciones).
- **Phase 5 (US3)**: depende de base de US2 en `EmpleadoUpdateService`/validaciones y de servicios de cuenta existentes.
- **Phase 6 (Polish)**: depende de historias objetivo completadas y gate final.

### User Story Dependencies

- **US1**: independiente tras Foundational.
- **US2**: independiente tras Foundational, aunque se beneficia de endpoints US1 para validaciones manuales end-to-end.
- **US3**: depende de US2 por cambios en DTO/servicio de update de empleado.

### Within Each User Story

- Tests primero (fallando) → implementación → integración → checkpoint.

### Dependency Graph

- Setup → Foundational → {US1, US2} → US3 → Polish

---

## Parallel Execution Examples

### US1

- Ejecutar en paralelo `T010` y `T011` (pruebas en archivos diferentes).

### US2

- Ejecutar en paralelo `T016`, `T017`, `T018` y `T035` (refactor de pruebas por capas diferentes).

### US3

- Ejecutar en paralelo `T023`, `T024`, `T025`, `T026` y `T036` (pruebas de update/credenciales/auth en archivos distintos).

---

## Implementation Strategy

### MVP First (US1)

1. Completar Setup + Foundational.
2. Completar US1 (CRUD departamentos).
3. Validar US1 de forma independiente.

### Incremental Delivery

1. US1: catálogo de departamentos disponible.
2. US2: alta de empleado con departamento obligatorio.
3. US3: actualización parcial + activación diferida.
4. Polish: contrato, quickstart y gate final.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Después:
   - Dev A: US1 (`DepartamentoService`, `DepartamentoController`, tests).
   - Dev B: US2 (`EmpleadoCreateService`, validaciones, tests de create).
   - Dev C: US3 (`EmpleadoUpdateService`, `CredencialEmpleadoService`, tests de update/auth).
