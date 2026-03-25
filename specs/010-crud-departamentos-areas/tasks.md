# Tasks: CRUD Completo de Departamentos (Gestión de Áreas)

**Input**: Design documents from `/specs/010-crud-departamentos-areas/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required for user stories), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: Se incluyen tareas de prueba por el impacto en integridad de datos y contrato API protegido.

**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura base de módulo departamentos y rutas de integración.

- [X] T001 Revisar y ajustar ruta `/admin/departamentos` en frontend-empleados/src/app/app.routes.ts
- [X] T002 Crear modelo de dominio frontend para listado con personal en frontend-empleados/src/app/modelos/departamento-gestion.model.ts
- [X] T003 [P] Crear modelo de estado reactivo de lista en frontend-empleados/src/app/modelos/departamento-list-state.model.ts
- [X] T004 [P] Crear modelo de estado del modal de formulario en frontend-empleados/src/app/modelos/departamento-form-state.model.ts
- [X] T005 Crear servicio HTTP de departamentos en frontend-empleados/src/app/servicios/departamento.service.ts
- [X] T006 [P] Crear base de pruebas de integración/E2E para departamentos en frontend-empleados/cypress/e2e/departamentos-crud.cy.ts
- [X] T048 [P] Agregar prueba Cypress de solicitudes protegidas (Basic Auth vía interceptor/credenciales) para endpoints de departamentos en frontend-empleados/cypress/e2e/departamentos-auth.cy.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Dejar listas piezas compartidas backend/frontend que bloquean las historias.

**⚠️ CRITICAL**: Ninguna historia de usuario inicia antes de completar esta fase.

- [X] T007 Crear proyección de listado con conteo de personal incluyendo getter `getTotalEmpleados()` en src/main/java/com/example/empleados/repository/DepartamentoListaProjection.java
- [X] T008 Implementar consulta en repository con `LEFT JOIN + COUNT(e.id) AS totalEmpleados` para mapeo consistente de proyección en src/main/java/com/example/empleados/repository/DepartamentoRepository.java
- [X] T009 [P] Extender DTOs para respuesta de listado con `totalEmpleados` en src/main/java/com/example/empleados/controller/dto/DepartamentoDtos.java
- [X] T010 Adaptar servicio para devolver listado proyectado de departamentos en src/main/java/com/example/empleados/service/DepartamentoService.java
- [X] T011 Adaptar controlador para mapear `GET /api/v1/departamentos` con `totalEmpleados` en src/main/java/com/example/empleados/controller/DepartamentoController.java
- [X] T012 [P] Documentar contrato OpenAPI para listado y delete protegido en src/main/java/com/example/empleados/controller/DepartamentoController.java
- [X] T013 Alinear manejo de `409 Conflict` para departamento en uso en src/main/java/com/example/empleados/controller/ApiExceptionHandler.java
- [X] T014 [P] Agregar pruebas de integración del listado con `totalEmpleados` en src/test/java/com/example/empleados/controller/DepartamentoControllerIntegrationTest.java
- [X] T049 Implementar validación de negocio para `nombre` vacío/solo espacios y duplicado (case-insensitive) en create/update en src/main/java/com/example/empleados/service/DepartamentoService.java
- [X] T050 [P] Agregar pruebas de servicio/controlador para rechazos por nombre inválido/duplicado en src/test/java/com/example/empleados/service/DepartamentoServiceTest.java

**Checkpoint**: Base técnica lista para construir historias de usuario en paralelo.

---

## Phase 3: User Story 1 - Gestión CRUD de departamentos (Priority: P1) 🎯 MVP

**Goal**: Entregar CRUD funcional de departamentos con columna `Personal` y sincronía backend/frontend.

**Independent Test**: Crear, editar y borrar un departamento con `totalEmpleados = 0` desde la UI y verificar actualización reactiva de tabla.

### Tests for User Story 1

- [X] T015 [P] [US1] Cubrir create/update/delete exitosos en src/test/java/com/example/empleados/service/DepartamentoServiceTest.java
- [X] T016 [P] [US1] Cubrir contrato de respuesta de listado con `totalEmpleados` en src/test/java/com/example/empleados/controller/DepartamentoControllerIntegrationTest.java
- [X] T017 [P] [US1] Cubrir flujo CRUD de departamentos con Cypress en frontend-empleados/cypress/e2e/departamentos-crud.cy.ts

### Implementation for User Story 1

- [X] T018 [US1] Crear componente principal de gestión de departamentos en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.ts
- [X] T019 [P] [US1] Implementar template de tabla con columna `Personal` en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.html
- [X] T020 [P] [US1] Implementar estilos base de tabla y acciones CRUD en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css
- [X] T021 [US1] Reemplazar placeholder por componente de gestión en frontend-empleados/src/app/app.routes.ts
- [X] T022 [US1] Integrar carga paginada reactiva (`page,size,sort`) con Signals en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.ts
- [X] T023 [US1] Implementar acciones de crear y editar conectadas al servicio en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.ts
- [X] T024 [US1] Implementar acción de delete para filas válidas (`totalEmpleados = 0`) en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.ts

**Checkpoint**: US1 funcional y desplegable como MVP.

---

## Phase 4: User Story 2 - Flujo UX fluido con modal y búsqueda local (Priority: P1)

**Goal**: Optimizar productividad con modal reutilizable moderno y search bar reactivo local.

**Independent Test**: Usar búsqueda local por nombre sin requests extra y completar create/edit desde modal reutilizable sin recarga de página.

### Tests for User Story 2

- [X] T025 [P] [US2] Cubrir apertura/cierre y modos create/edit del modal con Cypress en frontend-empleados/cypress/e2e/departamento-form-modal.cy.ts
- [X] T026 [P] [US2] Cubrir filtro local reactivo con Signals usando Cypress en frontend-empleados/cypress/e2e/departamentos-busqueda-local.cy.ts

### Implementation for User Story 2

- [X] T027 [US2] Crear componente modal reutilizable de formulario en frontend-empleados/src/app/componentes/departamento-form-modal/departamento-form-modal.component.ts
- [X] T028 [P] [US2] Crear template del modal de formulario con estados create/edit en frontend-empleados/src/app/componentes/departamento-form-modal/departamento-form-modal.component.html
- [X] T029 [P] [US2] Diseñar estilos modernos del modal usando guía de frontend-design skill en frontend-empleados/src/app/componentes/departamento-form-modal/departamento-form-modal.component.css
- [X] T030 [US2] Integrar modal reutilizable en pantalla principal en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.html
- [X] T031 [US2] Implementar lógica de submit/cancel del modal en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.ts
- [X] T032 [US2] Implementar Search Bar reactivo con Signals usando `computed(() => ...)` para derivar `filteredItems` en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.ts
- [X] T033 [US2] Renderizar barra de búsqueda, estado vacío por filtro y nota visual de filtro aplicado sobre la página cargada en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.html
- [X] T046 [US2] Implementar confirmación de descarte al cerrar/cancelar modal con formulario dirty en frontend-empleados/src/app/componentes/departamento-form-modal/departamento-form-modal.component.ts
- [X] T047 [P] [US2] Cubrir confirmación de cambios sin guardar (descartar/continuar edición) con Cypress en frontend-empleados/cypress/e2e/departamento-form-discard-confirm.cy.ts

**Checkpoint**: US2 completa y validable sin dependencia de US3.

---

## Phase 5: User Story 3 - Integridad y manejo de errores de borrado (Priority: P2)

**Goal**: Garantizar borrado protegido por integridad y UX preventiva con tooltip en filas bloqueadas.

**Independent Test**: Intentar borrar departamento con `totalEmpleados > 0` y verificar deshabilitado + tooltip + toast específico de integridad al recibir `409`.

### Tests for User Story 3

- [X] T034 [P] [US3] Cubrir rechazo `409 Conflict` por departamento en uso en src/test/java/com/example/empleados/service/DepartamentoServiceTest.java
- [X] T035 [P] [US3] Cubrir mapeo de error delete→toast con Cypress en frontend-empleados/cypress/e2e/departamentos-delete-conflict-409.cy.ts

### Implementation for User Story 3

- [X] T036 [US3] Verificar y reforzar regla de no-delete con empleados asociados en src/main/java/com/example/empleados/service/DepartamentoService.java
- [X] T037 [US3] Implementar captura específica de `409` y toast requerido en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.ts
- [X] T051 [US3] Unificar literal de mensaje `409` en UI/contrato/pruebas y usar una única cadena canónica en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.ts
- [X] T038 [US3] Deshabilitar visualmente botón eliminar cuando `totalEmpleados > 0` en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.html
- [X] T039 [US3] Implementar tooltip explicativo en acción eliminar bloqueada en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.html
- [X] T040 [US3] Estilizar estado deshabilitado + tooltip de acciones en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css

**Checkpoint**: US3 completa con protección end-to-end (backend + frontend).

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de consistencia, documentación y validación integral.

- [X] T041 [P] Actualizar documentación de contrato final en specs/010-crud-departamentos-areas/contracts/departamentos-crud-management-contract.md
- [X] T042 [P] Ejecutar y registrar validación funcional completa en specs/010-crud-departamentos-areas/quickstart.md
- [X] T043 [P] Verificar sincronía de `data-model.md` contra implementación final en specs/010-crud-departamentos-areas/data-model.md
- [X] T044 [P] Validar OpenAPI de endpoints de departamentos y códigos de error en src/main/java/com/example/empleados/controller/DepartamentoController.java
- [X] T045 Confirmar trazabilidad FR→tasks en specs/010-crud-departamentos-areas/tasks.md
- [X] T052 [P] Consolidar y verificar en spec/contract/quickstart una sola redacción de mensaje para conflicto `409` en specs/010-crud-departamentos-areas/
- [X] T053 Ejecutar suite Cypress en verde y registrar Definition of Done (modo oscuro + eliminación con `409 Conflict`) en frontend-empleados/cypress/e2e/departamentos-darkmode.cy.ts y frontend-empleados/cypress/e2e/departamentos-delete-conflict-409.cy.ts

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: sin dependencias.
- **Foundational (Phase 2)**: depende de Setup y bloquea historias.
- **User Stories (Phase 3+)**: dependen de Foundational.
- **Polish (Phase 6)**: depende de historias completadas.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational; entrega MVP CRUD + columna Personal.
- **US2 (P1)**: inicia tras Foundational; puede correr en paralelo con US1.
- **US3 (P2)**: inicia tras Foundational; usa salidas de US1 para acción delete y tabla.

### Dependency Graph

- `Phase 1 -> Phase 2 -> {US1, US2} -> US3 -> Phase 6`

---

## Parallel Execution Examples

### User Story 1

```bash
Task: "T019 [US1] Implementar template de tabla con columna Personal en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.html"
Task: "T020 [US1] Implementar estilos base de tabla y acciones CRUD en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css"
Task: "T016 [US1] Cubrir contrato de respuesta de listado con totalEmpleados en src/test/java/com/example/empleados/controller/DepartamentoControllerIntegrationTest.java"
```

### User Story 2

```bash
Task: "T028 [US2] Crear template del modal de formulario en frontend-empleados/src/app/componentes/departamento-form-modal/departamento-form-modal.component.html"
Task: "T029 [US2] Diseñar estilos modernos del modal en frontend-empleados/src/app/componentes/departamento-form-modal/departamento-form-modal.component.css"
Task: "T026 [US2] Cubrir filtro local reactivo con Signals usando Cypress en frontend-empleados/cypress/e2e/departamentos-busqueda-local.cy.ts"
```

### User Story 3

```bash
Task: "T038 [US3] Deshabilitar visualmente botón eliminar cuando totalEmpleados > 0 en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.html"
Task: "T040 [US3] Estilizar estado deshabilitado + tooltip en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css"
Task: "T035 [US3] Cubrir mapeo de error delete→toast con Cypress en frontend-empleados/cypress/e2e/departamentos-delete-conflict-409.cy.ts"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar US1 de forma independiente.

### Incremental Delivery

1. Setup + Foundational.
2. US1 (MVP CRUD departamentos + Personal).
3. US2 (modal moderno + búsqueda reactiva).
4. US3 (delete protegido con tooltip + toast específico).
5. Polish y validación final.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego en paralelo:
   - Dev A: US1 (backend listado + CRUD base).
   - Dev B: US2 (modal + search Signals).
   - Dev C: US3 (delete protegido + tooltip + toast).

---

## Independent Test Criteria by Story

- **US1**: CRUD de departamentos operativo con tabla paginada y columna `Personal` basada en `totalEmpleados`.
- **US2**: Modal reutilizable moderno para create/edit con confirmación de cambios sin guardar y búsqueda local por nombre con Signals sin requests extra.
- **US3**: Borrado protegido (`409`) con botón deshabilitado + tooltip cuando hay personal, y toast específico al capturar error de integridad.
- **Definition of Done**: Suite Cypress en verde, incluyendo validación de modo oscuro y camino de error `409 Conflict` en eliminación.

---

## FR Traceability

- FR-001, FR-002 → T011, T018, T019, T022
- FR-002A → T007, T008, T009, T010, T014, T016
- FR-003, FR-004, FR-005 → T018, T023, T024, T027, T031
- FR-006, FR-007 → T049, T050
- FR-008, FR-017 → T027, T028, T030, T031, T046, T047
- FR-009, FR-018 → T026, T032, T033
- FR-010, FR-011 → T005, T048
- FR-012, FR-013, FR-016 → T013, T034, T036, T037, T038, T039, T040, T051, T052
- FR-014, FR-015 → T017, T018, T019, T020
