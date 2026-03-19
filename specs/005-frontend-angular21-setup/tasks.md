# Tasks: Estructura Frontend Angular 21 y Configuración de Integración

**Input**: Design documents from `/specs/005-frontend-angular21-setup/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: No se incluyen tareas de pruebas automatizadas porque la especificación no solicita enfoque TDD explícito; la validación se realiza con criterios independientes por historia y quickstart.

**Organization**: Las tareas se agrupan por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inicializar el workspace frontend y su configuración mínima de ejecución.

- [X] T001 Crear esqueleto de workspace Angular 21 en `frontend-empleados/angular.json`
- [X] T002 Crear manifiesto inicial de frontend con scripts de desarrollo en `frontend-empleados/package.json`
- [X] T003 [P] Crear estructura base de aplicación (`componentes`, `servicios`, `modelos`, `interceptores`) en `frontend-empleados/src/app/`
- [X] T004 [P] Inicializar configuración base de TypeScript para frontend en `frontend-empleados/tsconfig.json`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Configuración transversal que bloquea todas las historias de usuario.

**⚠️ CRITICAL**: Ninguna historia de usuario inicia antes de completar esta fase.

- [X] T005 Implementar política CORS de desarrollo para origen frontend local en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T006 [P] Declarar propiedades de integración local frontend-backend en `src/main/resources/application.yml`
- [X] T007 [P] Crear configuración de proxy `/api` hacia backend local en `frontend-empleados/proxy.conf.json`
- [X] T008 Crear configuración de entorno frontend para consumo de `/api` en `frontend-empleados/src/environments/environment.ts`
- [X] T009 Actualizar regla constitucional de arquitectura frontend en `.specify/memory/constitution.md`
- [X] T010 Alinear contrato operativo de integración local con configuración final en `specs/005-frontend-angular21-setup/contracts/frontend-backend-integration.md`

**Checkpoint**: Base de integración lista; las historias US1/US2/US3 pueden avanzar.

---

## Phase 3: User Story 1 - Estructura base de frontend (Priority: P1) 🎯 MVP

**Goal**: Disponer de una base frontend organizada y ejecutable en `/frontend-empleados`.

**Independent Test**: Verificar que existe la estructura mínima (`componentes`, `servicios`, `modelos`, `interceptores`) y que el proyecto frontend arranca sin errores de estructura.

### Implementation for User Story 1

- [X] T011 [US1] Crear componente raíz standalone inicial en `frontend-empleados/src/app/app.component.ts`
- [X] T012 [US1] Configurar bootstrap de aplicación standalone en `frontend-empleados/src/main.ts`
- [X] T013 [P] [US1] Crear punto de entrada para componentes compartidos en `frontend-empleados/src/app/componentes/index.ts`
- [X] T014 [P] [US1] Crear punto de entrada para modelos de frontend en `frontend-empleados/src/app/modelos/index.ts`
- [X] T015 [P] [US1] Crear punto de entrada para servicios de frontend en `frontend-empleados/src/app/servicios/index.ts`
- [X] T016 [US1] Documentar estructura base y convenciones de carpetas en `frontend-empleados/README.md`

**Checkpoint**: US1 funcional y validable de forma independiente.

---

## Phase 4: User Story 2 - Integración frontend-backend local (Priority: P1)

**Goal**: Permitir llamadas frontend a backend local sin errores de origen cruzado y manteniendo seguridad.

**Independent Test**: Con backend en `:8080` y frontend en `:4200`, ejecutar una llamada a `/api/v1/**` desde frontend y confirmar ausencia de error CORS y mantenimiento de autenticación.

### Implementation for User Story 2

- [X] T017 [US2] Configurar `HttpClient` en la app frontend para llamadas API en `frontend-empleados/src/app/app.config.ts`
- [X] T018 [P] [US2] Implementar interceptor de autorización para peticiones API en `frontend-empleados/src/app/interceptores/auth.interceptor.ts`
- [X] T019 [US2] Registrar interceptores y providers HTTP globales en `frontend-empleados/src/app/app.config.ts`
- [X] T020 [US2] Crear servicio de verificación de conectividad backend en `frontend-empleados/src/app/servicios/backend-health.service.ts`
- [X] T021 [US2] Endurecer validación de headers/métodos CORS requeridos para integración en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T022 [US2] Añadir flujo de validación manual de integración local en `specs/005-frontend-angular21-setup/quickstart.md`

**Checkpoint**: US2 funcional y validable de forma independiente.

---

## Phase 5: User Story 3 - Proxy y reglas de arquitectura frontend (Priority: P2)

**Goal**: Establecer reglas de arquitectura frontend y uso consistente de proxy local para `/api`.

**Independent Test**: Confirmar que `ng serve` usa proxy hacia backend, que las llamadas relativas `/api` funcionan y que las reglas arquitectónicas quedan documentadas.

### Implementation for User Story 3

- [X] T023 [US3] Configurar dev-server Angular para usar `proxy.conf.json` en `frontend-empleados/angular.json`
- [X] T024 [P] [US3] Definir abstracción de cliente API para rutas relativas `/api` en `frontend-empleados/src/app/servicios/api-client.service.ts`
- [X] T025 [US3] Registrar estándares obligatorios de Angular 21 para esta feature en `.specify/memory/constitution.md`
- [X] T026 [US3] Sincronizar lineamientos de arquitectura frontend en `specs/005-frontend-angular21-setup/spec.md`
- [X] T027 [US3] Actualizar contrato de integración con estrategia final de proxy en `specs/005-frontend-angular21-setup/contracts/frontend-backend-integration.md`

**Checkpoint**: US3 funcional y validable de forma independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de documentación, consistencia y handoff a la siguiente feature (Login).

- [X] T028 [P] Ejecutar validación completa de quickstart y registrar resultado final en `specs/005-frontend-angular21-setup/quickstart.md`
- [X] T029 [P] Consolidar decisiones finales de diseño e implementación en `specs/005-frontend-angular21-setup/plan.md`
- [X] T030 Verificar trazabilidad FR→tareas y consistencia de rutas en `specs/005-frontend-angular21-setup/tasks.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: sin dependencias, inicia de inmediato.
- **Foundational (Phase 2)**: depende de Setup y bloquea todas las historias.
- **User Stories (Phase 3+)**: dependen de Foundational; pueden avanzar en paralelo o por prioridad.
- **Polish (Phase 6)**: depende de las historias seleccionadas como objetivo del release.

### User Story Dependencies

- **US1 (P1)**: inicia después de Foundational, sin dependencia funcional de otras historias.
- **US2 (P1)**: inicia después de Foundational, independiente de US1 para validar conectividad.
- **US3 (P2)**: inicia después de Foundational; se beneficia de US1/US2 pero mantiene criterio de prueba independiente.

### Dependency Graph

- `Phase 1 → Phase 2 → {US1, US2, US3} → Phase 6`

---

## Parallel Execution Examples

### US1

```bash
Task: "T013 [US1] Crear punto de entrada para componentes compartidos en frontend-empleados/src/app/componentes/index.ts"
Task: "T014 [US1] Crear punto de entrada para modelos de frontend en frontend-empleados/src/app/modelos/index.ts"
Task: "T015 [US1] Crear punto de entrada para servicios de frontend en frontend-empleados/src/app/servicios/index.ts"
```

### US2

```bash
Task: "T018 [US2] Implementar interceptor de autorización para peticiones API en frontend-empleados/src/app/interceptores/auth.interceptor.ts"
Task: "T020 [US2] Crear servicio de verificación de conectividad backend en frontend-empleados/src/app/servicios/backend-health.service.ts"
```

### US3

```bash
Task: "T024 [US3] Definir abstracción de cliente API para rutas relativas /api en frontend-empleados/src/app/servicios/api-client.service.ts"
Task: "T027 [US3] Actualizar contrato de integración con estrategia final de proxy en specs/005-frontend-angular21-setup/contracts/frontend-backend-integration.md"
```

---

## Implementation Strategy

### MVP First (US1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar independencia de US1 antes de expandir alcance.

### Incremental Delivery

1. Setup + Foundational para desbloquear la base.
2. Entregar US1 (estructura frontend funcional).
3. Entregar US2 (conectividad local sin errores de origen).
4. Entregar US3 (proxy y reglas arquitectónicas).
5. Cerrar con Polish y handoff a Login.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego en paralelo:
   - Dev A: US1 (estructura y bootstrap).
   - Dev B: US2 (integración y CORS).
   - Dev C: US3 (proxy y gobernanza frontend).

---

## FR Traceability Check

- FR-001, FR-002, FR-003 → T001, T003, T011–T016
- FR-004 → T007, T023, T024, T027
- FR-005, FR-006, FR-009 → T005, T006, T021
- FR-007 → T009, T025, T026
- FR-008 → T017–T022, T028, T029
