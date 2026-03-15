# Implementation Plan: Validación de Paginación Real en BD

**Branch**: `002-validacion-paginacion-db` | **Date**: 2026-03-14 | **Spec**: `specs/002-validacion-paginacion-db/spec.md`

## Objective

Aportar evidencia automatizada de que `GET /api/v1/empleados` usa paginación/ordenamiento real sobre PostgreSQL,
evitar dependencia exclusiva de pruebas con servicios mockeados y mantener contrato vigente.

## Technical Approach

1. Añadir capa de pruebas de integración con PostgreSQL real.
2. Validar paginación en repositorio (`findAll(Pageable)`) con dataset controlado.
3. Validar endpoint HTTP autenticado con contexto Spring completo contra BD real.
4. Mantener contrato OpenAPI sin cambios funcionales (solo documentar evidencia técnica).

## Workstreams

### WS1 - Setup de entorno de pruebas
- Definir estrategia: `Testcontainers PostgreSQL` (preferido por aislamiento) o perfil de integración con contenedor Docker Compose.
- Asegurar ejecución de migraciones Flyway en pruebas.

### WS2 - Pruebas de repositorio
- Crear pruebas de integración para `EmpleadoRepository` con carga de datos de ejemplo.
- Verificar `page`, `size`, `sort` y metadatos de página.

### WS3 - Pruebas de endpoint
- Crear pruebas de integración para `GET /api/v1/empleados` con Basic Auth.
- Verificar contenido ordenado y metadatos paginados en respuesta JSON.
- Verificar error `422` para parámetros inválidos.

### WS4 - Cierre y evidencia
- Ejecutar suite de pruebas.
- Documentar resultados y comandos en `quickstart.md` de la spec.

## Risks & Mitigations

- **Riesgo**: pruebas inestables por dependencia externa de BD.
  - **Mitigación**: usar Testcontainers con ciclo de vida controlado.
- **Riesgo**: tiempo de ejecución mayor en CI.
  - **Mitigación**: separar pruebas de integración por perfil/tag cuando sea necesario.

## Constitution Check

- Stack: Spring Boot 3.x + Java 17 ✅
- Seguridad Basic Auth en endpoints de prueba ✅
- Persistencia PostgreSQL y migraciones versionadas ✅
- Reproducibilidad en Docker ✅
- API versionada `/api/v1` y colección con `Pageable` ✅
