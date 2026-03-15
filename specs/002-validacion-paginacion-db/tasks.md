# Tasks: Validación de Paginación Real en BD

**Input**: Design documents from `/specs/002-validacion-paginacion-db/`  
**Prerequisites**: `plan.md` (required), `spec.md` (required)

**Tests**: Esta especificación sí exige nuevas pruebas de integración contra PostgreSQL real.

## Phase 1: Setup

- [ ] T001 Definir estrategia de BD de integración (Testcontainers o perfil docker) en `pom.xml` y configuración de tests
- [ ] T002 Verificar que Flyway aplique migraciones en entorno de pruebas de integración

## Phase 2: Repository Integration

- [ ] T003 Crear prueba de integración de `EmpleadoRepository` en `src/test/java/com/example/empleados/repository/EmpleadoRepositoryIntegrationTest.java`
- [ ] T004 Sembrar dataset de empleados para pruebas de paginación/ordenamiento en setup de test
- [ ] T005 Validar `findAll(PageRequest.of(...))` para `page`, `size`, `sort asc/desc` y metadatos de página

## Phase 3: API Integration

- [ ] T006 Crear prueba de integración HTTP con contexto completo en `src/test/java/com/example/empleados/controller/EmpleadoPaginationIntegrationTest.java`
- [ ] T007 Validar `GET /api/v1/empleados?page=...&size=...&sort=...` con Basic Auth y resultados reales de BD
- [ ] T008 Validar `422` para parámetros inválidos (`page`, `size`, `sort`) en entorno real

## Phase 4: Documentation & Validation

- [ ] T009 Documentar evidencia y comandos de ejecución en `specs/002-validacion-paginacion-db/quickstart.md`
- [ ] T010 Ejecutar `mvn test` y registrar resultado de pruebas de integración

## Dependencies & Order

Setup → Repository Integration → API Integration → Documentation & Validation

## Definition of Done

- Hay evidencia automatizada de que la paginación no es solo visual en Swagger.
- Las pruebas nuevas pasan localmente y son reproducibles.
- No hay cambios de contrato API observables sin actualización documental.
