# Implementation Plan: Estructura Frontend Angular 21 y Configuración de Integración

**Branch**: `005-frontend-angular21-setup` | **Date**: 2026-03-19 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/005-frontend-angular21-setup/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Preparar la base de frontend para iniciar autenticación en una siguiente entrega,
creando/normalizando la estructura en `/frontend-empleados`, definiendo un proxy
de desarrollo para `/api` y habilitando integración local segura con el backend
Spring (`http://localhost:8080`) mediante política CORS acotada a
`http://localhost:4200`. Además, dejar explícitas reglas de arquitectura frontend
en la constitución del proyecto para mantener consistencia evolutiva.

## Technical Context

**Language/Version**: Java 17 (backend), TypeScript + Angular 21 (frontend)  
**Primary Dependencies**: Spring Boot 3.3.x, Spring Security, Angular CLI 21, Angular Standalone Components/Signals/Control Flow  
**Storage**: PostgreSQL (sin cambios de modelo para esta feature)  
**Testing**: JUnit 5 + Spring Boot Test (backend), pruebas Angular (setup base)  
**Target Platform**: Linux local dev (backend `:8080`, frontend `:4200`)  
**Project Type**: aplicación web full-stack (backend monolítico + frontend SPA)  
**Performance Goals**: cero errores de conectividad frontend↔backend en dev local para llamadas de API  
**Constraints**: Basic Auth vigente, rutas `/api/v1`, CORS restringido a origen local de desarrollo, sin relajar seguridad existente  
**Scale/Scope**: bootstrap frontend + integración de red local + actualización de reglas arquitectónicas

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] Principio I (Stack): backend mantiene Java 17 + Spring Boot 3.x; frontend definido en Angular 21 sin afectar runtime backend.
- [x] Principio II (Seguridad): Basic Auth permanece activa; CORS solo habilita origen/métodos/headers necesarios de desarrollo.
- [x] Principio III (Persistencia): no se altera esquema ni estrategia PostgreSQL/Docker; cumplimiento intacto.
- [x] Principio IV (Contrato): se conserva versionado `/api/v1`; no se introducen rutas fuera del prefijo vigente.
- [x] Principio V (Calidad): se planifican verificaciones de conectividad, configuración y documentación reproducible.

## Project Structure

### Documentation (this feature)

```text
specs/005-frontend-angular21-setup/
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
├── main/
│   ├── java/com/example/empleados/
│   │   └── config/
│   └── resources/
└── test/
    └── java/com/example/empleados/

frontend-empleados/
├── src/
│   ├── app/
│   │   ├── componentes/
│   │   ├── servicios/
│   │   ├── modelos/
│   │   └── interceptores/
│   └── environments/
└── proxy.conf.json
```

**Structure Decision**: se adopta estructura web application con backend existente y un frontend dedicado en `/frontend-empleados`, manteniendo separación clara de responsabilidades sin mover código backend actual.

## Phase 0: Research Focus

- Definir estrategia de CORS segura para desarrollo local sin abrir orígenes no requeridos.
- Validar convenciones de Angular 21 para bootstrap inicial (Standalone + Signals + Control Flow).
- Establecer formato mínimo de proxy de desarrollo para mapear `/api` al backend local.
- Precisar criterios para actualizar reglas constitucionales sin contradecir principios backend actuales.

## Phase 1: Design Outputs

- `data-model.md` con entidades de configuración/integración de esta feature.
- `contracts/frontend-backend-integration.md` con contrato operativo de origen, headers, métodos y proxy en desarrollo.
- `quickstart.md` con pasos reproducibles para levantar backend+frontend y validar conectividad.

## Post-Design Constitution Check

- [x] Principio I (Stack): diseño mantiene compatibilidad Java 17 + Spring Boot 3.x y ubica frontend en capa separada.
- [x] Principio II (Seguridad): diseño conserva Basic Auth y acota CORS a entorno local con cabeceras controladas.
- [x] Principio III (Persistencia): no se proponen cambios de DB ni migraciones.
- [x] Principio IV (Contrato): diseño mantiene llamadas contra `/api/v1` y no rompe versionado.
- [x] Principio V (Calidad): se definen quickstart reproducible y validaciones específicas de integración local.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | N/A | N/A |

## Implementation Results (2026-03-19)

- Workspace frontend creado en `/frontend-empleados` con bootstrap standalone y estructura base de carpetas.
- Proxy de desarrollo aplicado en `frontend-empleados/proxy.conf.json` para rutas `/api`.
- Política CORS backend implementada en `SecurityConfig` con configuración por propiedades `app.cors.*`.
- Constitución actualizada con regla obligatoria de ubicación/estándares frontend.
- Validación backend: `mvn -DskipTests package` exitoso.
- Validación frontend: `npm install`, `npm run build` y `npm start` exitosos con Node `22.22.1`.
