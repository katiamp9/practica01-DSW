# Tasks: CRUD Completo de Empleados

**Input**: Design documents from `/specs/009-crud-empleados-completo/`
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: Se incluyen tareas de pruebas porque la especificación define escenarios de testing obligatorios y la constitución exige calidad verificable.

**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar baseline técnico y trazabilidad para el alcance de la feature.

- [ ] T001 Verificar configuración de rutas admin en frontend-empleados/src/app/app.routes.ts
- [ ] T002 Verificar contrato de modelos de gestión en frontend-empleados/src/app/modelos/empleado-gestion.model.ts
- [ ] T003 [P] Verificar modelos de estado de formulario en frontend-empleados/src/app/modelos/estado-formulario-empleado.model.ts
- [ ] T004 [P] Verificar modelo de catálogo de departamentos en frontend-empleados/src/app/modelos/departamento-option.model.ts
- [ ] T005 [P] Verificar mapeador base de payloads en frontend-empleados/src/app/modelos/empleado-mapper.ts
- [ ] T006 Confirmar endpoints versionados usados por frontend en frontend-empleados/src/app/servicios/empleado.service.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Dejar listas las piezas compartidas que bloquean cualquier historia de usuario.

**⚠️ CRITICAL**: Ninguna historia se implementa antes de completar esta fase.

- [X] T007 Consolidar consulta paginada de empleados en src/main/java/com/example/empleados/service/EmpleadoQueryService.java
- [X] T008 [P] Implementar `LEFT JOIN` para email en listado en src/main/java/com/example/empleados/repository/EmpleadoRepository.java
- [X] T009 [P] Alinear DTO de respuesta con campo canónico `email` en src/main/java/com/example/empleados/controller/dto/EmpleadoDtos.java
- [X] T010 Ajustar mapeo entidad->DTO para `email` nullable en src/main/java/com/example/empleados/controller/EmpleadoMapper.java
- [X] T011 [P] Actualizar contrato OpenAPI/Swagger para listado de empleados con `email` nullable y semántica de `LEFT JOIN` en src/main/java/com/example/empleados/controller/EmpleadoQueryController.java y src/main/java/com/example/empleados/controller/dto/EmpleadoDtos.java
- [X] T012 Implementar política de inmutabilidad de email + respuesta `400` (regla y manejo de excepción) en src/main/java/com/example/empleados/service/EmpleadoUpdateService.java y src/main/java/com/example/empleados/controller/ApiExceptionHandler.java
- [ ] T013 [P] Alinear contrato cliente para campo `email` en frontend-empleados/src/app/servicios/empleado.service.ts
- [ ] T014 [P] Preparar estado reactivo de sesión y tabla para reglas de delete en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.ts
- [ ] T015 Ajustar utilidades de validación de formulario en frontend-empleados/src/app/modelos/empleado-validations.ts
- [X] T054 Implementar generación de `clave` en backend con formato `EMP-` + secuencia en src/main/java/com/example/empleados/service/ClaveEmpleadoGenerator.java y src/main/java/com/example/empleados/service/EmpleadoCreateService.java
- [X] T055 Implementar manejo de colisión de `clave` autogenerada con `409 Conflict` en src/main/java/com/example/empleados/service/EmpleadoCreateService.java

**Checkpoint**: Base técnica lista; US1-US3 pueden desarrollarse sin bloqueos transversales.

---

## Phase 3: User Story 1 - Gestión integral de empleados (Priority: P1) 🎯 MVP

**Goal**: Entregar la gestión CRUD completa con listado reactivo, columna Departamento por nombre y columna Email visible con fallback.

**Independent Test**: Crear/editar/eliminar desde la vista y verificar refresco inmediato; confirmar Email visible para todos los registros y `Sin correo` cuando aplique.

### Tests for User Story 1

- [ ] T016 [P] [US1] Cubrir listado paginado y refresco de CRUD en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados-crud-flow.spec.ts
- [ ] T017 [P] [US1] Cubrir render de columnas Departamento/Email en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.spec.ts
- [X] T018 [P] [US1] Cubrir fallback `Sin correo` para email null en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.spec.ts
- [X] T019 [P] [US1] Cubrir contrato de listado con email en src/test/java/com/example/empleados/controller/EmpleadoControllerIntegrationTest.java

### Implementation for User Story 1

- [X] T020 [US1] Implementar consulta backend de listado con email en src/main/java/com/example/empleados/service/EmpleadoQueryService.java
- [X] T021 [US1] Exponer email en endpoint de listado en src/main/java/com/example/empleados/controller/EmpleadoQueryController.java
- [ ] T022 [US1] Implementar resolución de nombre de departamento en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.ts
- [X] T023 [US1] Implementar render de columna Email con fallback `Sin correo` en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.html
- [ ] T024 [US1] Ajustar estilos de tabla para estado de datos faltantes en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css
- [ ] T025 [US1] Asegurar refresco de lista tras create/update/delete en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.ts
- [ ] T026 [US1] Verificar paginación y ordenamiento de empleados en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.ts

**Checkpoint**: US1 funcional y demostrable como MVP.

---

## Phase 4: User Story 2 - Formulario unificado con validaciones (Priority: P1)

**Goal**: Formulario único create/edit con validaciones completas, carga de departamentos y reglas de email en edición.

**Independent Test**: Abrir formulario create/edit, validar reglas de nombre/email/departamento, confirmar que email en edición está precargado y `readonly`.

### Tests for User Story 2

- [ ] T027 [P] [US2] Cubrir modo create/edit y carga de valores iniciales en frontend-empleados/src/app/componentes/empleado-form/empleado-form.component.spec.ts
- [ ] T028 [P] [US2] Cubrir validaciones de nombre, email corporativo y departamento en frontend-empleados/src/app/componentes/empleado-form/empleado-form.validations.spec.ts
- [ ] T029 [P] [US2] Cubrir `POST` sin clave y `PUT` con departamentoId en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados-crud-flow.spec.ts
- [X] T030 [P] [US2] Cubrir rechazo `400` por intento de cambio de email en src/test/java/com/example/empleados/service/EmpleadoUpdateServiceTest.java
- [X] T056 [P] [US2] Cubrir colisión de `clave` autogenerada y respuesta `409` en src/test/java/com/example/empleados/service/EmpleadoCreateServiceTest.java

### Implementation for User Story 2

- [X] T031 [US2] Implementar precarga de email en modo edición en frontend-empleados/src/app/componentes/empleado-form/empleado-form.component.ts
- [X] T032 [US2] Renderizar campo email como `readonly` en edición en frontend-empleados/src/app/componentes/empleado-form/empleado-form.component.html
- [ ] T033 [US2] Mantener regla create sin `clave` y mapping correcto de payload en frontend-empleados/src/app/componentes/empleado-form/empleado-form.component.ts
- [ ] T034 [US2] Cargar departamentos al abrir formulario con Signals en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.ts
- [ ] T035 [US2] Conectar select de departamento mostrando nombre y enviando ID en frontend-empleados/src/app/componentes/empleado-form/empleado-form.component.ts
- [ ] T036 [US2] Mostrar advertencia cuando no hay departamentos en frontend-empleados/src/app/componentes/empleado-form/empleado-form.component.html
- [X] T037 [US2] Integrar flujo de create sin `clave` usando generador backend en src/main/java/com/example/empleados/service/EmpleadoCreateService.java
- [X] T038 [US2] Propagar conflicto de colisión de clave con mensaje explícito al cliente en src/main/java/com/example/empleados/controller/ApiExceptionHandler.java

**Checkpoint**: US2 completa e independiente, con reglas de formulario y contrato de edición cerradas.

---

## Phase 5: User Story 3 - Confirmación y feedback de operaciones (Priority: P2)

**Goal**: Garantizar operaciones seguras (confirmación y protección), con feedback claro y consistencia entre UI y backend.

**Independent Test**: Cancelar y confirmar eliminación, verificar toasts, bloqueo de self-delete en UI y `403` para admin principal en backend.

### Tests for User Story 3

- [ ] T039 [P] [US3] Cubrir confirmación/cancelación de delete en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.confirm-delete.spec.ts
- [ ] T040 [P] [US3] Cubrir toasts de éxito/error en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.toast.spec.ts
- [ ] T041 [P] [US3] Cubrir ocultamiento de delete para sesión autenticada en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.spec.ts
- [ ] T042 [P] [US3] Cubrir bloqueo `403` de admin principal en src/test/java/com/example/empleados/service/EmpleadoDeleteServiceTest.java

### Implementation for User Story 3

- [ ] T043 [US3] Integrar flujo de confirmación previo a delete en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.ts
- [ ] T044 [US3] Implementar regla de visibilidad de botón eliminar por sesión en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.html
- [ ] T045 [US3] Integrar mensajes de toast para create/update/delete en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.ts
- [ ] T046 [US3] Mantener bloqueo de eliminación del admin principal en src/main/java/com/example/empleados/service/EmpleadoDeleteService.java
- [ ] T047 [US3] Asegurar mapeo de excepción a `403 Forbidden` en src/main/java/com/example/empleados/controller/ApiExceptionHandler.java

**Checkpoint**: US3 operativa con protección end-to-end y feedback UX completo.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cerrar trazabilidad documental, validación integral y consistencia de contratos.

- [ ] T048 [P] Actualizar contrato final de feature en specs/009-crud-empleados-completo/contracts/empleados-crud-management-contract.md
- [ ] T049 [P] Actualizar quickstart con flujo de verificación final en specs/009-crud-empleados-completo/quickstart.md
- [ ] T050 [P] Sincronizar reglas de entidades y relaciones en specs/009-crud-empleados-completo/data-model.md
- [X] T051 Ejecutar validación de pruebas backend de feature en src/test/java/com/example/empleados/controller/EmpleadoControllerIntegrationTest.java
- [X] T052 Ejecutar validación de pruebas frontend de feature en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.spec.ts
- [ ] T053 Confirmar trazabilidad FR-001..FR-032 en specs/009-crud-empleados-completo/tasks.md
- [ ] T057 Ejecutar validación final de arranque integral con docker-compose en docker/compose/docker-compose.yml (build + up + smoke test de backend/frontend)
- [ ] T058 Validar publicación de cambios en OpenAPI/Swagger UI para `email`, `LEFT JOIN` y errores `400/409` en src/main/java/com/example/empleados/controller/EmpleadoQueryController.java y src/main/java/com/example/empleados/controller/EmpleadoCreateController.java

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia sin dependencias.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea historias.
- **Phase 3-5 (US1-US3)**: dependen de Phase 2.
- **Phase 6 (Polish)**: depende de US1-US3.

### User Story Dependencies

- **US1 (P1)**: depende solo de Foundational.
- **US2 (P1)**: depende solo de Foundational; no bloquea US1.
- **US3 (P2)**: depende de Foundational y de flujo delete disponible en US1.

### Dependency Graph

- `Setup -> Foundational -> {US1, US2} -> US3 -> Polish`

---

## Parallel Execution Examples

### User Story 1

```bash
Task: "T016 [US1] Cubrir listado paginado y refresco de CRUD en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados-crud-flow.spec.ts"
Task: "T018 [US1] Cubrir fallback Sin correo para email null en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.spec.ts"
Task: "T019 [US1] Cubrir contrato de listado con email en src/test/java/com/example/empleados/controller/EmpleadoControllerIntegrationTest.java"
```

### User Story 2

```bash
Task: "T028 [US2] Cubrir validaciones de nombre, email corporativo y departamento en frontend-empleados/src/app/componentes/empleado-form/empleado-form.validations.spec.ts"
Task: "T030 [US2] Cubrir rechazo 400 por intento de cambio de email en src/test/java/com/example/empleados/service/EmpleadoUpdateServiceTest.java"
Task: "T034 [US2] Cargar departamentos al abrir formulario con Signals en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.ts"
```

### User Story 3

```bash
Task: "T039 [US3] Cubrir confirmación/cancelación de delete en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.confirm-delete.spec.ts"
Task: "T041 [US3] Cubrir ocultamiento de delete para sesión autenticada en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.spec.ts"
Task: "T042 [US3] Cubrir bloqueo 403 de admin principal en src/test/java/com/example/empleados/service/EmpleadoDeleteServiceTest.java"
```

---

## Implementation Strategy

### MVP First (US1)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar criterio independiente de US1.

### Incremental Delivery

1. Base técnica: Setup + Foundational.
2. Entrega 1: US1 (MVP).
3. Entrega 2: US2.
4. Entrega 3: US3.
5. Cierre: Polish.

### Parallel Team Strategy

1. Equipo cierra Setup + Foundational.
2. En paralelo:
   - Dev A: US1 (listado y tabla)
   - Dev B: US2 (formulario y validaciones)
3. Dev C toma US3 en cuanto flujo de delete esté estable.

---

## Independent Test Criteria by Story

- **US1**: CRUD reactivo con columna Departamento por nombre y columna Email visible con fallback `Sin correo`.
- **US2**: Formulario unificado create/edit, select de departamento, `POST` sin clave con generación backend `EMP-` secuencial, colisión de clave retorna `409`, email de edición `readonly`, update rechaza cambio de email con `400`.
- **US3**: Confirmación obligatoria de delete, toasts de éxito, ocultamiento de self-delete y bloqueo backend del admin principal con `403`.
