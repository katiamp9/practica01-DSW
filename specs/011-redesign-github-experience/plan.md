# Implementation Plan: Rediseño Global "GitHub Experience" Dark Mode

**Branch**: `011-redesign-github-experience` | **Date**: 2026-03-25 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/011-redesign-github-experience/spec.md`

## Summary

Aplicar una experiencia Dark Mode oficial de GitHub en toda la SPA (incluyendo Login/Home y módulos administrativos), mediante tokens globales en `styles.css` con herencia automática, sin cambiar contratos API ni lógica de negocio.

## Technical Context

**Language/Version**: TypeScript 5.9 + Angular 21 (frontend), Java 17 + Spring Boot 3.3.x (backend existente)  
**Primary Dependencies**: Angular Standalone Components, Signals, Control Flow nativo, CSS global en `styles.css`, Spring Security Basic Auth, Spring Data JPA Pageable  
**Storage**: PostgreSQL (backend, sin cambios de esquema)  
**Testing**: Jasmine/Karma (frontend), JUnit/Mockito/Spring Test (backend no impactado)  
**Target Platform**: Navegador desktop moderno para SPA administrativa
**Project Type**: Web app full-stack (SPA Angular + API REST Spring Boot)  
**Performance Goals**: Mantener p95 UI administrativa < 2s y evitar degradación de render tras migrar tema  
**Constraints**: Dark Mode fijo (sin toggle), paleta oficial (`#0d1117`, `#161b22`, `#30363d`, `#c9d1d9`, `#8b949e`, `#238636`), semánticos solo en estados críticos  
**Scale/Scope**: Tema visual global para toda la SPA con validación prioritaria en Login, Home, Dashboard, Empleados y Departamentos

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Stack y Versiones Obligatorias**: se mantiene Java 17 + Spring Boot 3.x y Angular 21.
- [x] **II. Seguridad por Defecto con Basic Auth**: no se alteran políticas de autenticación/autorización.
- [x] **III. Persistencia PostgreSQL y Entorno Docker**: no hay cambios de persistencia ni migraciones.
- [x] **IV. Contrato API Versionado y Documentado**: no se cambian endpoints `/api/v1`.
- [x] **V. Calidad Verificable y Compatibilidad Evolutiva**: se define validación funcional/visual para no romper CRUD ni navegación.

## Project Structure

### Documentation (this feature)

```text
specs/011-redesign-github-experience/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
└── tasks.md
```

### Source Code (repository root)

```text
src/
├── main/java/com/example/empleados/
│   ├── controller/
│   ├── service/
│   └── repository/
└── test/java/com/example/empleados/

frontend-empleados/
├── src/
│   ├── app/
│   │   ├── componentes/
│   │   ├── modelos/
│   │   ├── servicios/
│   │   └── app.routes.ts
│   └── styles.css
└── package.json
```

**Structure Decision**: Se mantiene la arquitectura actual backend + `frontend-empleados`, concentrando cambios en la capa de tema global y estilos de componentes para propagación automática de Dark Mode.

## Phase 0: Research & Decisions

- Establecer tokenización oficial Dark Mode GitHub para toda la SPA.
- Definir política de tema fijo sin selector Light/Dark.
- Definir límites de color semántico (solo error/peligro/warning).
- Definir estrategia de migración visual incremental sin romper funcionalidad.

## Phase 1: Design Artifacts

- `research.md`: decisiones de diseño Dark Mode y alternativas descartadas.
- `data-model.md`: entidades de tokenización y componentes visuales dark.
- `contracts/github-experience-ui-contract.md`: contrato visual global para toda la SPA.
- `quickstart.md`: validación funcional y visual end-to-end del tema dark.

## Post-Design Constitution Check

- [x] **I. Stack y Versiones Obligatorias**: diseño mantiene stack constitucional.
- [x] **II. Seguridad por Defecto con Basic Auth**: sin impacto en seguridad.
- [x] **III. Persistencia PostgreSQL y Entorno Docker**: sin impacto en datos.
- [x] **IV. Contrato API Versionado y Documentado**: sin cambios en contrato REST.
- [x] **V. Calidad Verificable y Compatibilidad Evolutiva**: quickstart define validación reproducible de no regresión.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |

## Implementation Evidence (2026-03-25)

- Frontend compilado exitosamente tras fases Setup, Foundational, US1, US2 y US3 con `npm run build`.
- Tokens dark globales aplicados en `frontend-empleados/src/styles.css` con paleta oficial y tema fijo.
- Cobertura visual dark aplicada en Login, Home, Dashboard, Empleados y Departamentos.
- Componentes compartidos (`ui-toast`, `confirm-delete-dialog`, `departamento-form-modal`) refactorizados para token-first.
- Revisión de regresión operativa en componentes TS de Empleados/Departamentos sin cambios de lógica de negocio.
- Auditoría de contraste y semántica completada: colores semánticos restringidos a estados críticos.

## Traceability FR-001..FR-016

- **FR-001, FR-009, FR-010**: cumplido con tokenización global y herencia automática en `styles.css`.
- **FR-002**: cumplido con estructura de header/navegación dark en vistas administrativas.
- **FR-003**: cumplido con `--surface-bg: #0d1117`.
- **FR-004**: cumplido con `--surface-card: #161b22` y `--border-color: #30363d`.
- **FR-005**: cumplido con stack tipográfico global nativo en `:root`.
- **FR-006**: cumplido con `--brand-primary: #238636` y variantes neutrales dark.
- **FR-007**: cumplido con `.gh-table` y divisores horizontales en tema dark.
- **FR-008**: cumplido con `.gh-badge` gris compacto en módulos objetivo.
- **FR-011**: cumplido por alineación visual general con GitHub Dark Mode.
- **FR-012**: cumplido; sin cambios en contratos API ni lógica funcional.
- **FR-013**: cumplido; no se implementó selector de tema.
- **FR-014**: cumplido; semánticos limitados a error/peligro/warning.
- **FR-015**: cumplido con `--text-primary: #c9d1d9` y `--text-muted: #8b949e`.
- **FR-016**: cumplido con inputs oscuros con borde/focus y encabezados de tabla en superficie dark con negrita.
