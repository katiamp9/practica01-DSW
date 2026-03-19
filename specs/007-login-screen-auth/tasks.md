# Tasks: Diseño del Login

**Input**: Design documents from `/specs/007-login-screen-auth/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: Se incluyen tareas de pruebas unitarias para `AuthService`, `AuthGuard` y `AuthInterceptor`, y evidencia de ejecución exitosa antes de merge.

**Organization**: Las tareas se agrupan por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar base de routing/auth en frontend para habilitar login y navegación por rol.

- [X] T001 Revisar puntos actuales de bootstrap/configuración en `frontend-empleados/src/main.ts`
- [X] T002 Definir archivo de rutas raíz en `frontend-empleados/src/app/app.routes.ts`
- [X] T003 [P] Crear estructura inicial de feature login en `frontend-empleados/src/app/componentes/login/login.component.ts`
- [X] T004 [P] Crear modelo base de sesión autenticada en `frontend-empleados/src/app/modelos/auth-session.model.ts`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Implementar infraestructura base de sesión, guard e interceptor necesaria para todas las historias.

**⚠️ CRITICAL**: Ninguna historia de usuario inicia antes de completar esta fase.

- [X] T005 Implementar `AuthService` con Signals y persistencia local en `frontend-empleados/src/app/servicios/auth.service.ts`
- [X] T006 [P] Definir contrato frontend de autenticación en `frontend-empleados/src/app/modelos/auth-login.model.ts`
- [X] T007 [P] Implementar servicio de login contra backend en `frontend-empleados/src/app/servicios/auth-api.service.ts`
- [X] T008 Implementar guard de autenticación para rutas privadas en `frontend-empleados/src/app/guards/auth.guard.ts`
- [X] T009 [P] Implementar política de autorización por rol en `frontend-empleados/src/app/guards/role.guard.ts`
- [X] T010 Actualizar interceptor para inyectar `Authorization` desde sesión activa en `frontend-empleados/src/app/interceptores/auth.interceptor.ts`
- [X] T011 Configurar `provideRouter` y providers de auth en `frontend-empleados/src/app/app.config.ts`
- [X] T012 Configurar endpoints/rutas base de autenticación en `frontend-empleados/src/environments/environment.ts`
- [X] T013 [P] Crear unit tests de `AuthService` en `frontend-empleados/src/app/servicios/auth.service.spec.ts`
- [X] T014 [P] Crear unit tests de `AuthGuard` en `frontend-empleados/src/app/guards/auth.guard.spec.ts`
- [X] T015 [P] Crear unit tests de `AuthInterceptor` en `frontend-empleados/src/app/interceptores/auth.interceptor.spec.ts`
- [X] T016 Ejecutar suite unitaria de auth y registrar evidencia pre-merge en `specs/007-login-screen-auth/quickstart.md`

**Checkpoint**: Frontend tiene sesión reactiva, guard e interceptor listos para historias funcionales.

---

## Phase 3: User Story 1 - Inicio de sesión y redirección por rol (Priority: P1) 🎯 MVP

**Goal**: Permitir login válido y redirección automática a `/admin-dashboard` o `/home` según rol.

**Independent Test**: Iniciar sesión con usuario `ROLE_ADMIN` y `ROLE_USER` y confirmar redirección correcta; validar error con credenciales inválidas.

### Implementation for User Story 1

- [X] T017 [US1] Implementar formulario reactivo de login en `frontend-empleados/src/app/componentes/login/login.component.ts`
- [X] T018 [P] [US1] Construir vista del formulario con mensajes de validación en `frontend-empleados/src/app/componentes/login/login.component.html`
- [X] T019 [P] [US1] Aplicar estilos base de login profesional en `frontend-empleados/src/app/componentes/login/login.component.css`
- [X] T020 [US1] Implementar submit de login y redirección por rol en `frontend-empleados/src/app/componentes/login/login.component.ts`
- [X] T021 [US1] Manejar rol no permitido limpiando sesión y volviendo a login en `frontend-empleados/src/app/servicios/auth.service.ts`
- [X] T022 [US1] Registrar ruta pública de login en `frontend-empleados/src/app/app.routes.ts`

**Checkpoint**: Login funcional y redirección por rol operativa de forma independiente.

---

## Phase 4: User Story 2 - Sesión persistente y navegación protegida (Priority: P1)

**Goal**: Mantener sesión entre navegaciones y proteger rutas privadas por autenticación y rol.

**Independent Test**: Acceder a rutas privadas con/sin sesión y con rol incorrecto, verificando bloqueo y redirección a login.

### Implementation for User Story 2

- [X] T023 [US2] Restaurar sesión desde localStorage en arranque de app en `frontend-empleados/src/app/servicios/auth.service.ts`
- [X] T024 [US2] Aplicar limpieza de sesión y redirección en guard de autenticación en `frontend-empleados/src/app/guards/auth.guard.ts`
- [X] T025 [P] [US2] Validar acceso por rol por ruta en `frontend-empleados/src/app/guards/role.guard.ts`
- [X] T026 [US2] Proteger rutas `/admin-dashboard` y `/home` con guards en `frontend-empleados/src/app/app.routes.ts`
- [X] T027 [P] [US2] Ajustar interceptor para no enviar credenciales sin sesión activa en `frontend-empleados/src/app/interceptores/auth.interceptor.ts`
- [X] T028 [US2] Exponer acción de logout y limpieza de storage en `frontend-empleados/src/app/servicios/auth.service.ts`

**Checkpoint**: Navegación protegida por sesión+rol y recuperación/limpieza de sesión consistente.

---

## Phase 5: User Story 3 - Experiencia visual profesional del login y vistas destino (Priority: P2)

**Goal**: Entregar interfaz moderna y consistente para login, home y dashboard usando lineamientos de diseño frontend.

**Independent Test**: Verificar legibilidad, jerarquía visual, mensajes de validación y coherencia estética entre login y vistas destino.

### Implementation for User Story 3

- [X] T029 [US3] Refinar diseño del login con dirección visual profesional en `frontend-empleados/src/app/componentes/login/login.component.css`
- [X] T030 [P] [US3] Crear componente standalone de panel admin en `frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.ts`
- [X] T031 [P] [US3] Crear plantilla visual de panel admin en `frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.html`
- [X] T032 [P] [US3] Crear estilos de panel admin en `frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.css`
- [X] T033 [P] [US3] Crear componente standalone de home de usuario en `frontend-empleados/src/app/componentes/home/home.component.ts`
- [X] T034 [P] [US3] Crear plantilla visual de home en `frontend-empleados/src/app/componentes/home/home.component.html`
- [X] T035 [P] [US3] Crear estilos de home en `frontend-empleados/src/app/componentes/home/home.component.css`
- [X] T036 [US3] Integrar rutas de vistas destino y navegación inicial en `frontend-empleados/src/app/app.routes.ts`
- [X] T037 [US3] Ajustar tema global para consistencia visual en `frontend-empleados/src/styles.css`

**Checkpoint**: Login, home y dashboard con acabado profesional y coherencia visual.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cerrar documentación, trazabilidad y validación manual reproducible.

- [X] T038 [P] Sincronizar contrato de login/guard/interceptor con implementación final en `specs/007-login-screen-auth/contracts/login-flow-contract.md`
- [X] T039 [P] Actualizar pasos y checklist de verificación final en `specs/007-login-screen-auth/quickstart.md`
- [X] T040 [P] Consolidar decisiones y resultado de implementación en `specs/007-login-screen-auth/plan.md`
- [X] T041 Verificar trazabilidad FR→tareas y consistencia de rutas en `specs/007-login-screen-auth/tasks.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: inicia inmediatamente, sin dependencias.
- **Foundational (Phase 2)**: depende de Setup y bloquea historias.
- **User Stories (Phase 3+)**: dependen de Foundational; US1 y US2 son P1, US3 depende funcionalmente de rutas/guards de US1-US2.
- **Polish (Phase 6)**: depende de historias objetivo completadas.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational; habilita flujo de login y redirección.
- **US2 (P1)**: inicia tras Foundational; requiere artefactos de sesión/auth base y consolida protección de rutas.
- **US3 (P2)**: inicia cuando US1/US2 ya exponen rutas y estado de sesión para maquetar vistas destino reales.

### Dependency Graph

- `Phase 1 → Phase 2 → {US1, US2} → US3 → Phase 6`

---

## Parallel Execution Examples

### US1

```bash
Task: "T014 [US1] Construir vista del formulario con mensajes de validación en frontend-empleados/src/app/componentes/login/login.component.html"
Task: "T019 [US1] Aplicar estilos base de login profesional en frontend-empleados/src/app/componentes/login/login.component.css"
```

### US2

```bash
Task: "T025 [US2] Validar acceso por rol por ruta en frontend-empleados/src/app/guards/role.guard.ts"
Task: "T027 [US2] Ajustar interceptor para no enviar credenciales sin sesión activa en frontend-empleados/src/app/interceptores/auth.interceptor.ts"
```

### US3

```bash
Task: "T031 [US3] Crear plantilla visual de panel admin en frontend-empleados/src/app/componentes/admin-dashboard/admin-dashboard.component.html"
Task: "T034 [US3] Crear plantilla visual de home en frontend-empleados/src/app/componentes/home/home.component.html"
```

---

## Implementation Strategy

### MVP First (US1)

1. Completar Setup (Phase 1).
2. Completar Foundational (Phase 2).
3. Completar US1 (Phase 3).
4. Validar login y redirección por rol como incremento MVP.

### Incremental Delivery

1. Setup + Foundational.
2. US1: login funcional con redirección.
3. US2: persistencia y protección de rutas por sesión+rol.
4. US3: experiencia visual profesional y vistas destino.
5. Polish: documentación final y trazabilidad.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego en paralelo:
   - Dev A: US1 (formulario/login/redirect).
   - Dev B: US2 (guard/interceptor/sesión).
   - Dev C: US3 (UI profesional login + dashboards) una vez listas rutas base.

---

## FR Traceability Check

- FR-001, FR-002, FR-003, FR-011 → T017, T018, T019, T020
- FR-004, FR-014 → T007, T020, T021
- FR-005, FR-006 → T005, T023, T028
- FR-007, FR-008, FR-012 → T030–T036
- FR-009, FR-015, FR-016 → T008, T024, T025, T026
- FR-010 → T010, T027
- FR-013 → T021, T024
