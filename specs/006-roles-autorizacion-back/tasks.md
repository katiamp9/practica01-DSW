# Tasks: Soporte de Roles y Autorización

**Input**: Design documents from `/specs/006-roles-autorizacion-back/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: No se agregan tareas de pruebas automáticas explícitas porque la especificación no solicita enfoque TDD; la validación funcional queda cubierta por quickstart y criterios independientes por historia.

**Organization**: Las tareas se agrupan por historia de usuario para implementación y verificación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar contexto técnico y puntos de cambio para roles.

- [X] T001 Revisar contrato actual de autenticación y carga de usuario en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T002 Identificar y documentar inserción de administrador inicial en `src/main/resources/db/migration/V3__auth_empleado.sql`
- [X] T003 [P] Confirmar estructura actual de `Empleado` y servicios de alta en `src/main/java/com/example/empleados/domain/Empleado.java`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Cambios base de persistencia y dominio requeridos por todas las historias.

**⚠️ CRITICAL**: Ninguna historia inicia antes de completar esta fase.

- [X] T004 Crear migración Flyway para agregar columna `rol` a empleados con default `ROLE_USER` en `src/main/resources/db/migration/V6__add_rol_to_empleados.sql`
- [X] T005 [P] Incluir backfill para empleados existentes sin rol en `src/main/resources/db/migration/V6__add_rol_to_empleados.sql`
- [X] T006 [P] Actualizar entidad `Empleado` con atributo `rol` y default operativo en `src/main/java/com/example/empleados/domain/Empleado.java`
- [X] T007 Definir constantes de roles del sistema en `src/main/java/com/example/empleados/service/Roles.java`
- [X] T008 Ajustar mapeos/serialización de empleado para contemplar rol en `src/main/java/com/example/empleados/controller/EmpleadoMapper.java`

**Checkpoint**: Base de datos y dominio soportan roles de forma consistente.

---

## Phase 3: User Story 1 - Rol por defecto en empleados (Priority: P1) 🎯 MVP

**Goal**: Todo empleado nuevo se crea con `ROLE_USER` si no se especifica otro valor válido.

**Independent Test**: Crear empleado nuevo y verificar en persistencia que el rol asignado sea `ROLE_USER` por defecto.

### Implementation for User Story 1

- [X] T009 [US1] Asignar `ROLE_USER` por defecto en flujo de alta de empleado en `src/main/java/com/example/empleados/service/EmpleadoCreateService.java`
- [X] T010 [US1] Proteger actualización parcial para no dejar rol nulo o vacío en `src/main/java/com/example/empleados/service/EmpleadoUpdateService.java`
- [X] T011 [P] [US1] Validar reglas de rol permitido en validaciones de empleado en `src/main/java/com/example/empleados/service/EmpleadoValidationService.java`
- [X] T012 [US1] Ajustar DTOs de empleado para reflejar rol en respuestas necesarias en `src/main/java/com/example/empleados/controller/dto/EmpleadoDtos.java`

**Checkpoint**: Empleado nuevo y existente mantienen rol válido con default consistente.

---

## Phase 4: User Story 2 - Administrador inicial con privilegios de administración (Priority: P1)

**Goal**: El administrador inicial queda persistido con `ROLE_ADMIN` en inicialización y entornos existentes.

**Independent Test**: Verificar que el registro de administrador inicial tiene rol `ROLE_ADMIN` tras migración/arranque.

### Implementation for User Story 2

- [X] T013 [US2] Actualizar INSERT del administrador inicial para incluir columna `rol='ROLE_ADMIN'` en `src/main/resources/db/migration/V3__auth_empleado.sql`
- [X] T014 [US2] Añadir resguardo para forzar `ROLE_ADMIN` del administrador inicial en migración de roles en `src/main/resources/db/migration/V6__add_rol_to_empleados.sql`
- [X] T015 [P] [US2] Alinear documentación de migración y bootstrap de admin en `specs/006-roles-autorizacion-back/quickstart.md`

**Checkpoint**: Administrador inicial mantiene privilegio administrativo en bootstrap y retrocompatibilidad.

---

## Phase 5: User Story 3 - Autorización basada en rol reconocido por seguridad (Priority: P2)

**Goal**: Spring Security interpreta el rol persistido como `GrantedAuthority` para autorización.

**Independent Test**: Autenticación de usuario devuelve authorities derivadas del rol persistido (`ROLE_USER`/`ROLE_ADMIN`).

### Implementation for User Story 3

- [X] T016 [US3] Implementar `UserDetailsService` basado en persistencia de cuenta/credencial/rol en `src/main/java/com/example/empleados/service/EmpleadoUserDetailsService.java`
- [X] T017 [US3] Mapear `rol` de empleado a `GrantedAuthority` en `src/main/java/com/example/empleados/service/EmpleadoUserDetailsService.java`
- [X] T018 [US3] Integrar `EmpleadoUserDetailsService` en configuración de seguridad en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T019 [P] [US3] Ajustar repositorio de cuentas para carga eficiente de empleado con rol en `src/main/java/com/example/empleados/repository/CuentaEmpleadoRepository.java`
- [X] T020 [US3] Mantener fallback seguro para usuario `admin` con autoridad `ROLE_ADMIN` en `src/main/java/com/example/empleados/config/SecurityConfig.java`

**Checkpoint**: Autorización por rol activa sin romper autenticación básica existente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre documental, verificación de consistencia y handoff.

- [X] T021 [P] Sincronizar contrato de roles/authorities con implementación final en `specs/006-roles-autorizacion-back/contracts/roles-authorities-contract.md`
- [X] T022 [P] Consolidar decisiones y resultados en `specs/006-roles-autorizacion-back/plan.md`
- [X] T023 Ejecutar validación de build backend y registrar evidencia en `specs/006-roles-autorizacion-back/quickstart.md`
- [X] T024 Verificar trazabilidad FR→tareas y consistencia de rutas en `specs/006-roles-autorizacion-back/tasks.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: sin dependencias, inicia inmediatamente.
- **Foundational (Phase 2)**: depende de Setup y bloquea todas las historias.
- **User Stories (Phase 3+)**: dependen de Foundational; pueden ejecutarse en paralelo por capacidad del equipo.
- **Polish (Phase 6)**: depende de historias objetivo completadas.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational, sin dependencia funcional de otras historias.
- **US2 (P1)**: inicia tras Foundational y usa migraciones base de rol.
- **US3 (P2)**: inicia tras Foundational; se apoya en modelo de rol ya persistido por US1/US2.

### Dependency Graph

- `Phase 1 → Phase 2 → {US1, US2, US3} → Phase 6`

---

## Parallel Execution Examples

### US1

```bash
Task: "T011 [US1] Validar reglas de rol permitido en validaciones de empleado en src/main/java/com/example/empleados/service/EmpleadoValidationService.java"
Task: "T012 [US1] Ajustar DTOs de empleado para reflejar rol en respuestas necesarias en src/main/java/com/example/empleados/controller/dto/EmpleadoDtos.java"
```

### US2

```bash
Task: "T014 [US2] Añadir resguardo para forzar ROLE_ADMIN del administrador inicial en migración de roles en src/main/resources/db/migration/V6__add_rol_to_empleados.sql"
Task: "T015 [US2] Alinear documentación de migración y bootstrap de admin en specs/006-roles-autorizacion-back/quickstart.md"
```

### US3

```bash
Task: "T017 [US3] Mapear rol de empleado a GrantedAuthority en src/main/java/com/example/empleados/service/EmpleadoUserDetailsService.java"
Task: "T019 [US3] Ajustar repositorio de cuentas para carga eficiente de empleado con rol en src/main/java/com/example/empleados/repository/CuentaEmpleadoRepository.java"
```

---

## Implementation Strategy

### MVP First (US1)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar default de rol y compatibilidad de datos.

### Incremental Delivery

1. Setup + Foundational.
2. US1 (default role y compatibilidad de empleados).
3. US2 (admin inicial con ROLE_ADMIN).
4. US3 (roles como authorities en seguridad).
5. Polish + evidencia final.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. En paralelo por historia:
   - Dev A: US1 (dominio/servicios/dtos).
   - Dev B: US2 (migraciones y bootstrap admin).
   - Dev C: US3 (security + user details + repositorio).

---

## FR Traceability Check

- FR-001, FR-003, FR-006 → T004, T005, T006, T010
- FR-002 → T007, T009, T011
- FR-004 → T013, T014
- FR-005 → T016, T017, T018, T019
- FR-007 → T018, T020
