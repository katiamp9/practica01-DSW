# Tasks: Admin Dashboard Home & Navigation

**Input**: Design documents from `/specs/008-admin-dashboard-navigation/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: Se incluyen tareas de pruebas unitarias para `EmpleadoQueryService` y componentes del módulo admin, además de validación funcional reproducible en `quickstart.md`.

**Organization**: Las tareas se agrupan por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura frontend para navegación admin y consumo paginado.

- [X] T001 Revisar rutas y puntos de integración actuales en frontend-empleados/src/app/app.routes.ts
- [X] T002 Crear modelos base de paginación de frontend en frontend-empleados/src/app/modelos/page-response.model.ts
- [X] T003 [P] Crear modelo de empleado listado en frontend-empleados/src/app/modelos/empleado-listado.model.ts
- [X] T004 [P] Crear modelo de estado de dashboard admin en frontend-empleados/src/app/modelos/admin-dashboard-state.model.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Implementar cimientos reutilizables para listado paginado, estado reactivo y protección de rutas admin.

**⚠️ CRITICAL**: Ninguna historia de usuario inicia antes de completar esta fase.

- [X] T005 Implementar servicio de consulta paginada de empleados en frontend-empleados/src/app/servicios/empleado-query.service.ts usando `HttpClient` para activar credenciales automáticas del `AuthInterceptor`
- [X] T006 Implementar mapeo y normalización de roles para badges en frontend-empleados/src/app/modelos/app-roles.ts
- [X] T007 [P] Implementar estado reactivo del dashboard con Signals en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.ts
- [X] T008 [P] Definir utilidades de parámetros `page`, `size`, `sort` para requests en frontend-empleados/src/app/servicios/empleado-query.service.ts
- [X] T009 Ajustar configuración global de rutas protegidas admin en frontend-empleados/src/app/app.routes.ts
- [X] T010 Crear componente placeholder de empleados admin en frontend-empleados/src/app/componentes/admin-empleados-placeholder/admin-empleados-placeholder.component.ts
- [X] T011 [P] Crear plantilla placeholder de empleados en frontend-empleados/src/app/componentes/admin-empleados-placeholder/admin-empleados-placeholder.component.html
- [X] T012 [P] Crear estilos placeholder de empleados en frontend-empleados/src/app/componentes/admin-empleados-placeholder/admin-empleados-placeholder.component.css
- [X] T013 Crear componente placeholder de departamentos admin en frontend-empleados/src/app/componentes/admin-departamentos-placeholder/admin-departamentos-placeholder.component.ts
- [X] T014 [P] Crear plantilla placeholder de departamentos en frontend-empleados/src/app/componentes/admin-departamentos-placeholder/admin-departamentos-placeholder.component.html
- [X] T015 [P] Crear estilos placeholder de departamentos en frontend-empleados/src/app/componentes/admin-departamentos-placeholder/admin-departamentos-placeholder.component.css
- [X] T016 Crear pruebas unitarias de EmpleadoQueryService en frontend-empleados/src/app/servicios/empleado-query.service.spec.ts
- [X] T017 [P] Crear pruebas unitarias de AdminDashboardComponent (carga/empty/error/retry) en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.spec.ts
- [X] T018 [P] Crear pruebas unitarias de placeholders admin en frontend-empleados/src/app/componentes/admin-empleados-placeholder/admin-empleados-placeholder.component.spec.ts
- [X] T019 [P] Crear pruebas unitarias de placeholders admin en frontend-empleados/src/app/componentes/admin-departamentos-placeholder/admin-departamentos-placeholder.component.spec.ts

**Checkpoint**: Infraestructura lista para implementar historias de usuario de forma independiente.

---

## Phase 3: User Story 1 - Visualización de empleados en dashboard admin (Priority: P1) 🎯 MVP

**Goal**: Mostrar listado paginado de empleados en dashboard con estados de carga, vacío, error y reintento.

**Independent Test**: Al iniciar sesión como admin y abrir dashboard, se visualiza tabla con `nombre`, `clave`, `rol`, paginación siguiente/anterior y recuperación con botón “Reintentar”.

### Implementation for User Story 1

- [X] T020 [US1] Integrar carga inicial de empleados (`page=0,size=10,sort=nombre,asc`) en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.ts
- [X] T021 [US1] Implementar render de tabla con columnas `nombre`, `clave`, `rol` en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.html
- [X] T022 [US1] Implementar badges de rol con fallback para rol desconocido en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.html
- [X] T023 [US1] Implementar estado vacío con mensaje amigable en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.html
- [X] T024 [US1] Implementar estado de error con botón visible “Reintentar” en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.html
- [X] T025 [US1] Implementar controles de paginación siguiente/anterior y handlers en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.ts
- [X] T026 [US1] Ajustar estilos base de tabla y bloques de estado en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css

**Checkpoint**: US1 funcional y validable sin depender de US2/US3.

---

## Phase 4: User Story 2 - Navegación superior administrativa (Priority: P1)

**Goal**: Proveer navegación admin consistente con accesos a Inicio, Empleados y Departamentos.

**Independent Test**: En dashboard admin se visualiza barra superior; cada enlace navega a su ruta esperada y placeholders muestran “Próximamente”.

### Implementation for User Story 2

- [X] T027 [US2] Implementar barra superior con links `Inicio`, `Empleados`, `Departamentos` en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.html
- [X] T028 [US2] Definir rutas `/admin/empleados` y `/admin/departamentos` protegidas por guard en frontend-empleados/src/app/app.routes.ts
- [X] T029 [US2] Registrar componente placeholder de empleados en routing en frontend-empleados/src/app/app.routes.ts
- [X] T030 [US2] Registrar componente placeholder de departamentos en routing en frontend-empleados/src/app/app.routes.ts
- [X] T031 [US2] Mostrar mensaje “Próximamente” en vista de empleados admin en frontend-empleados/src/app/componentes/admin-empleados-placeholder/admin-empleados-placeholder.component.html
- [X] T032 [US2] Mostrar mensaje “Próximamente” en vista de departamentos admin en frontend-empleados/src/app/componentes/admin-departamentos-placeholder/admin-departamentos-placeholder.component.html
- [X] T033 [US2] Ajustar estilo de navegación superior y estado activo en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css

**Checkpoint**: US2 funcional y validable sin depender de US3.

---

## Phase 5: User Story 3 - Presentación visual moderna del módulo admin (Priority: P2)

**Goal**: Mejorar legibilidad y jerarquía visual de navegación y tabla sin ampliar alcance funcional.

**Independent Test**: La interfaz admin muestra consistencia visual, hover por fila, badges distinguibles y jerarquía clara entre navegación y contenido.

### Implementation for User Story 3

- [X] T034 [US3] Aplicar refinamiento visual moderno de contenedor y jerarquía en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css
- [X] T035 [US3] Implementar feedback hover por fila en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css
- [X] T036 [US3] Refinar estilos de badges por rol para diferenciación clara en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css
- [X] T037 [US3] Ajustar espaciado y tipografía del header admin en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css
- [X] T038 [US3] Homologar estilos de placeholders admin con el dashboard en frontend-empleados/src/app/componentes/admin-empleados-placeholder/admin-empleados-placeholder.component.css
- [X] T039 [US3] Homologar estilos de placeholders admin con el dashboard en frontend-empleados/src/app/componentes/admin-departamentos-placeholder/admin-departamentos-placeholder.component.css

**Checkpoint**: US3 funcional y validable con revisión visual independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cerrar trazabilidad de feature y validación operativa reproducible.

- [X] T040 [P] Verificar consistencia de contrato implementado con especificación en specs/008-admin-dashboard-navigation/contracts/admin-dashboard-navigation-contract.md
- [X] T041 [P] Ejecutar y registrar checklist de validación funcional y métrica `<2s` en specs/008-admin-dashboard-navigation/quickstart.md
- [X] T042 [P] Actualizar resumen final de cumplimiento en specs/008-admin-dashboard-navigation/plan.md
- [X] T043 Verificar trazabilidad FR→tareas y orden de ejecución en specs/008-admin-dashboard-navigation/tasks.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: inicia sin dependencias.
- **Foundational (Phase 2)**: depende de Setup y bloquea historias.
- **User Stories (Phase 3+)**: dependen de Foundational.
- **Polish (Phase 6)**: depende de las historias seleccionadas como objetivo de entrega.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational; no depende de US2/US3.
- **US2 (P1)**: inicia tras Foundational; puede desarrollarse en paralelo con US1.
- **US3 (P2)**: inicia tras US1 y US2 para pulir componentes finales.

### Dependency Graph

- `Phase 1 → Phase 2 → {US1, US2} → US3 → Phase 6`

---

## Parallel Execution Examples

### User Story 1

```bash
Task: "T022 [US1] Implementar badges de rol con fallback para rol desconocido en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.html"
Task: "T026 [US1] Ajustar estilos base de tabla y bloques de estado en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css"
```

### User Story 2

```bash
Task: "T029 [US2] Registrar componente placeholder de empleados en routing en frontend-empleados/src/app/app.routes.ts"
Task: "T030 [US2] Registrar componente placeholder de departamentos en routing en frontend-empleados/src/app/app.routes.ts"
```

### User Story 3

```bash
Task: "T038 [US3] Homologar estilos de placeholders admin con el dashboard en frontend-empleados/src/app/componentes/admin-empleados-placeholder/admin-empleados-placeholder.component.css"
Task: "T039 [US3] Homologar estilos de placeholders admin con el dashboard en frontend-empleados/src/app/componentes/admin-departamentos-placeholder/admin-departamentos-placeholder.component.css"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar US1 (Phase 3).
4. Validar US1 con quickstart antes de continuar.

### Incremental Delivery

1. Setup + Foundational.
2. US1 (MVP visible del dashboard de empleados).
3. US2 (navegación admin y placeholders).
4. US3 (refinamiento visual moderno).
5. Polish (validación final y trazabilidad).

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego en paralelo:
   - Dev A: US1 (estado + tabla + paginación + reintento).
   - Dev B: US2 (navegación + rutas placeholder protegidas).
3. Dev C: US3 al integrar resultados de US1/US2.

---

## FR Traceability

- FR-001, FR-002, FR-003 → T027, T028, T033
- FR-004 → T028, T029, T030, T031, T032
- FR-005 → T005, T020
- FR-006 → T021
- FR-007 → T006, T022, T036
- FR-008 → T035
- FR-009 → T005 (HttpClient + AuthInterceptor)
- FR-010 → T023
- FR-011 → T007, T020, T025
- FR-012 → T009, T028
- FR-013 → T024
- FR-014 → T025
- FR-015 → T020
- FR-016 → T005, T008
- FR-017 → T016, T017, T018, T019

---

## Independent Test Criteria by Story

- **US1**: Dashboard carga empleados con `page=0,size=10,sort=nombre,asc`, muestra tabla con `nombre/clave/rol`, estados `empty/error` (mensaje amigable en `empty`) y botón “Reintentar”.
- **US2**: Barra superior visible con tres links; navegación correcta a dashboard y placeholders “Próximamente” en `/admin/empleados` y `/admin/departamentos`.
- **US3**: UI final con hover de filas, badges claramente diferenciadas y consistencia visual entre dashboard y placeholders.
