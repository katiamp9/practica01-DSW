# Tasks: Rediseño Global "GitHub Experience" Dark Mode

**Input**: Design documents from `/specs/011-redesign-github-experience/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required for user stories), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: No se generan tareas de pruebas unitarias dedicadas porque la especificación no exige TDD explícito; se incluyen tareas de validación funcional y de regresión visual/operativa.

**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar el inventario de vistas y componentes a migrar a Dark Mode global.

- [X] T001 Revisar flujo de rutas para cobertura total SPA en frontend-empleados/src/app/app.routes.ts
- [X] T002 Identificar estado actual de tokens globales en frontend-empleados/src/styles.css
- [X] T003 [P] Revisar estilos de Login para migración dark en frontend-empleados/src/app/componentes/login/login.component.css
- [X] T004 [P] Revisar estilos de Home para migración dark en frontend-empleados/src/app/componentes/home/home.component.css
- [X] T005 [P] Revisar estilos de Dashboard para consistencia dark en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css
- [X] T006 [P] Revisar estilos de Empleados para consistencia dark en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css
- [X] T007 [P] Revisar estilos de Departamentos para consistencia dark en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Definir sistema de tokens y utilidades Dark Mode oficial para toda la SPA.

**⚠️ CRITICAL**: Ninguna historia de usuario inicia antes de completar esta fase.

- [X] T008 Definir tokens globales Dark Mode oficial (`#0d1117`, `#161b22`, `#30363d`, `#c9d1d9`, `#8b949e`, `#238636`) en frontend-empleados/src/styles.css
- [X] T009 [P] Definir utilidades globales de superficie/box dark en frontend-empleados/src/styles.css
- [X] T010 [P] Definir variantes globales de botones dark (primario/neutral) en frontend-empleados/src/styles.css
- [X] T011 [P] Definir patrón global de inputs oscuros con borde/focus en frontend-empleados/src/styles.css
- [X] T012 [P] Definir patrón global de tablas dark con header en negrita en frontend-empleados/src/styles.css
- [X] T013 [P] Definir patrón global de badges grises compactos en frontend-empleados/src/styles.css
- [X] T014 Consolidar tipografía global y contraste base en frontend-empleados/src/styles.css
- [X] T015 Implementar reglas globales de estados críticos (error/peligro/warning) en frontend-empleados/src/styles.css
- [X] T016 Verificar ausencia de toggle de tema y mantener Dark Mode fijo en frontend-empleados/src/app/app.routes.ts
- [X] T017 [P] Confirmar no impacto funcional en consumo API de empleados en frontend-empleados/src/app/servicios/empleado.service.ts
- [X] T018 [P] Confirmar no impacto funcional en consumo API de departamentos en frontend-empleados/src/app/servicios/departamento.service.ts

**Checkpoint**: Sistema visual dark global listo para ser consumido por las historias.

---

## Phase 3: User Story 1 - Navegación principal y cobertura SPA Dark (Priority: P1) 🎯 MVP

**Goal**: Garantizar estructura visual oscura consistente en Login/Home y navegación administrativa.

**Independent Test**: Recorrer Login → Home → Dashboard y comprobar tema dark consistente, sin selector de tema y con navegación legible.

### Implementation for User Story 1

- [X] T019 [US1] Aplicar fondo base dark y jerarquía tipográfica en login en frontend-empleados/src/app/componentes/login/login.component.css
- [X] T020 [US1] Ajustar layout visual dark de login sin cambiar flujo funcional en frontend-empleados/src/app/componentes/login/login.component.html
- [X] T021 [US1] Aplicar estilo dark en home con herencia de tokens globales en frontend-empleados/src/app/componentes/home/home.component.css
- [X] T022 [US1] Ajustar estructura visual de home para consistencia dark en frontend-empleados/src/app/componentes/home/home.component.html
- [X] T023 [P] [US1] Ajustar header y navegación dark en dashboard en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css
- [X] T024 [P] [US1] Ajustar header y navegación dark en empleados en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css
- [X] T025 [P] [US1] Ajustar header y navegación dark en departamentos en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css
- [X] T026 [US1] Homologar estado activo/inactivo de navegación administrativa en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css

**Checkpoint**: Dark Mode fijo visible y consistente en rutas clave de la SPA.

---

## Phase 4: User Story 2 - Contenedores y componentes visuales unificados (Priority: P1)

**Goal**: Unificar superficies, botones, tablas, badges e inputs dark en Empleados y Departamentos.

**Independent Test**: Validar en Empleados/Departamentos contenedores `#161b22`, bordes `#30363d`, botones `#238636`, tablas legibles y badges grises compactos.

### Implementation for User Story 2

- [X] T027 [US2] Migrar contenedores principales de empleados a superficie dark en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css
- [X] T028 [US2] Migrar contenedores principales de departamentos a superficie dark en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css
- [X] T029 [P] [US2] Alinear botones principales/secundarios en empleados en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css
- [X] T030 [P] [US2] Alinear botones principales/secundarios en departamentos en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css
- [X] T031 [US2] Unificar render de tabla de empleados con encabezado dark en negrita en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css
- [X] T032 [US2] Unificar render de tabla de departamentos con encabezado dark en negrita en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css
- [X] T033 [US2] Aplicar badges grises compactos de conteo en empleados en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.html
- [X] T034 [US2] Aplicar badges grises compactos de conteo en departamentos en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.html
- [X] T035 [US2] Alinear inputs de búsqueda/formulario en departamentos al patrón dark en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css
- [X] T036 [US2] Asegurar legibilidad de estados vacíos/error bajo dark mode en empleados en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css
- [X] T037 [US2] Asegurar legibilidad de estados vacíos/error bajo dark mode en departamentos en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css

**Checkpoint**: Componentes operativos de Empleados y Departamentos homogéneos en Dark Mode.

---

## Phase 5: User Story 3 - Sistema global de estilos reutilizable (Priority: P2)

**Goal**: Asegurar herencia automática de tokens dark y mantenimiento centralizado sin hardcodes contradictorios.

**Independent Test**: Modificar un token en `styles.css` y comprobar propagación visual a Login/Home/Empleados/Departamentos sin ediciones duplicadas.

### Implementation for User Story 3

- [X] T038 [US3] Refactorizar login para depender exclusivamente de tokens globales en frontend-empleados/src/app/componentes/login/login.component.css
- [X] T039 [US3] Refactorizar home para depender exclusivamente de tokens globales en frontend-empleados/src/app/componentes/home/home.component.css
- [X] T040 [US3] Refactorizar empleados para eliminar hardcodes de color residuales en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css
- [X] T041 [US3] Refactorizar departamentos para eliminar hardcodes de color residuales en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css
- [X] T042 [P] [US3] Refactorizar dashboard para depender de tokens globales en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css
- [X] T043 [P] [US3] Refactorizar toast para política semántica crítica en frontend-empleados/src/app/componentes/ui-toast/ui-toast.component.css
- [X] T044 [P] [US3] Refactorizar confirm dialog para consistencia dark en frontend-empleados/src/app/componentes/confirm-delete-dialog/confirm-delete-dialog.component.css
- [X] T045 [P] [US3] Refactorizar modal de departamento para consistencia dark en frontend-empleados/src/app/componentes/departamento-form-modal/departamento-form-modal.component.css
- [X] T046 [US3] Documentar convenciones de uso de tokens dark en frontend-empleados/src/styles.css

**Checkpoint**: Sistema de estilos dark reutilizable y mantenible centralmente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de validación funcional/visual y trazabilidad final.

- [ ] T047 [P] Ejecutar validación funcional completa del quickstart en specs/011-redesign-github-experience/quickstart.md
- [X] T048 [P] Revisar regresión visual/operativa de CRUD en empleados en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.ts
- [X] T049 [P] Revisar regresión visual/operativa de CRUD en departamentos en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.ts
- [X] T050 [P] Verificar contraste global y estados críticos/no críticos en frontend-empleados/src/styles.css
- [X] T051 [P] Actualizar evidencias de cumplimiento final en specs/011-redesign-github-experience/plan.md
- [X] T052 Confirmar trazabilidad FR-001..FR-016 contra tareas en specs/011-redesign-github-experience/tasks.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia sin dependencias.
- **Phase 2 (Foundational)**: depende de Setup y bloquea todas las historias.
- **Phase 3 (US1)**: depende de Foundational; habilita MVP de cobertura dark en SPA.
- **Phase 4 (US2)**: depende de Foundational; puede avanzar en paralelo parcial con US1 tras ajustes base de navegación.
- **Phase 5 (US3)**: depende de Foundational y de componentes ya migrados por US1/US2.
- **Phase 6 (Polish)**: depende de historias completadas.

### User Story Dependencies

- **US1 (P1)**: no depende de otras historias; establece cobertura dark transversal de rutas clave.
- **US2 (P1)**: no depende de US1 a nivel funcional, pero se beneficia de tokens y navegación ya estabilizados.
- **US3 (P2)**: depende de tener US1 y US2 aplicadas para consolidar herencia token-first.

### Dependency Graph

- `Setup -> Foundational -> US1`
- `Setup -> Foundational -> US2`
- `US1 + US2 -> US3 -> Polish`

---

## Parallel Execution Examples

### User Story 1

```bash
Task: "T023 [US1] Ajustar header y navegación dark en dashboard en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css"
Task: "T024 [US1] Ajustar header y navegación dark en empleados en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css"
Task: "T025 [US1] Ajustar header y navegación dark en departamentos en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css"
```

### User Story 2

```bash
Task: "T029 [US2] Alinear botones principales/secundarios en empleados en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.css"
Task: "T030 [US2] Alinear botones principales/secundarios en departamentos en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.css"
Task: "T033 [US2] Aplicar badges grises compactos de conteo en empleados en frontend-empleados/src/app/componentes/admin-empleados/admin-empleados.component.html"
Task: "T034 [US2] Aplicar badges grises compactos de conteo en departamentos en frontend-empleados/src/app/componentes/admin-departamentos/admin-departamentos.component.html"
```

### User Story 3

```bash
Task: "T043 [US3] Refactorizar toast para política semántica crítica en frontend-empleados/src/app/componentes/ui-toast/ui-toast.component.css"
Task: "T044 [US3] Refactorizar confirm dialog para consistencia dark en frontend-empleados/src/app/componentes/confirm-delete-dialog/confirm-delete-dialog.component.css"
Task: "T045 [US3] Refactorizar modal de departamento para consistencia dark en frontend-empleados/src/app/componentes/departamento-form-modal/departamento-form-modal.component.css"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar cobertura dark en Login/Home/Dashboard como MVP.

### Incremental Delivery

1. Setup + Foundational.
2. US1 (cobertura dark transversal).
3. US2 (componentes operativos unificados en dark).
4. US3 (consolidación token-first y mantenimiento global).
5. Polish (validación integral y trazabilidad).

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego en paralelo:
   - Dev A: US1 (Login/Home/Navegación).
   - Dev B: US2 (Empleados visual dark).
   - Dev C: US2 (Departamentos visual dark).
3. Integración transversal en US3 y cierre conjunto en Polish.

---

## Independent Test Criteria by Story

- **US1**: Login/Home/Dashboard muestran Dark Mode consistente y no existe toggle de tema.
- **US2**: Empleados y Departamentos muestran superficies dark, botones jerárquicos correctos, tablas legibles y badges compactos.
- **US3**: Un cambio de token global se propaga a las vistas objetivo sin hardcodes contradictorios.
