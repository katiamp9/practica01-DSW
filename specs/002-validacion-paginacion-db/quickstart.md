# Quickstart: Validación de Paginación Real en BD

## Objetivo

Demostrar con pruebas automáticas que `GET /api/v1/empleados` ejecuta paginación/ordenamiento real en PostgreSQL.

## Pasos sugeridos

1. Levantar PostgreSQL local (si la estrategia elegida lo requiere):
   - `docker compose -f docker/compose/docker-compose.yml up -d postgres`
2. Ejecutar pruebas de integración:
   - `mvn test`
3. Revisar resultados esperados:
   - Pruebas verdes en repositorio (`EmpleadoRepository` con `Pageable`).
   - Pruebas verdes de endpoint `GET /api/v1/empleados` con Basic Auth.
   - Validaciones `422` para parámetros inválidos.

## Evidencia mínima

- Captura/salida de consola de `mvn test`.
- Casos que verifiquen orden asc/desc y segunda página (`page > 0`).
- Confirmación de metadatos (`totalElements`, `totalPages`, `number`, `size`).
