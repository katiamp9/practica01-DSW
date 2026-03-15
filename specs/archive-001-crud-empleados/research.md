# Phase 0 Research: CRUD de Empleados

## Decision 1: Stack principal del servicio
- Decision: Implementar con Java 17 + Spring Boot 3.x.
- Rationale: Cumple explícitamente la constitución del proyecto y reduce riesgo de incompatibilidad en seguridad y runtime.
- Alternatives considered:
  - Node.js/Express: descartado por no cumplir stack constitucional.
  - Spring Boot 2.x: descartado por incompatibilidad con baseline definido.

## Decision 2: Persistencia y migraciones
- Decision: Usar PostgreSQL 16 como base principal y Flyway para migraciones versionadas.
- Rationale: Mantiene trazabilidad de cambios de esquema y reproducibilidad entre local/integración.
- Alternatives considered:
  - H2 en producción: descartado por no alinearse con requisitos operativos.
  - Cambios manuales SQL fuera de migraciones: descartado por falta de trazabilidad.

## Decision 3: Definición exacta de `clave`
- Decision: `clave` será generada por el sistema con formato `EMP-<consecutivo>`, usando PK compuesta lógica (prefijo fijo + autonumérico), no editable después del alta.
- Rationale: Cumple el nuevo requerimiento de prefijo obligatorio y secuencia automática, evita errores de captura manual y mantiene unicidad determinista.
- Alternatives considered:
  - `clave` libre proporcionada por usuario: descartado por no cumplir formato requerido.
  - `clave` solo numérica sin prefijo: descartado por no cumplir regla de negocio `EMP-`.

## Decision 4: Validación de `telefono`
- Decision: `telefono` se modela como texto obligatorio con longitud máxima 100, sin regex de formato en esta fase.
- Rationale: El requerimiento pide límite de longitud, no formato internacional específico; evita rechazos no solicitados.
- Alternatives considered:
  - Regex estricto (E.164): descartado por no estar en requisitos.
  - Campo numérico: descartado porque perdería prefijos/símbolos comunes.

## Decision 5: Contrato de errores HTTP
- Decision: Estandarizar respuestas con:
  - `400 Bad Request` para validaciones y payload inválido.
  - `404 Not Found` para clave inexistente.
  - `409 Conflict` para colisiones de clave por concurrencia al generar consecutivos.
  - `422 Unprocessable Entity` para parámetros inválidos de paginación/ordenamiento en listados.
- Rationale: Mapea claramente FR-008/FR-009 a semántica HTTP estándar y facilita pruebas automatizadas.
- Alternatives considered:
  - Responder siempre `200` con mensaje de error: descartado por mala práctica REST.
  - Usar solo `400` para todo: descartado por pérdida de precisión semántica.

## Decision 6: Seguridad de endpoints
- Decision: Proteger endpoints de empleados con Basic Auth; permitir público solo health check y documentación en entorno `dev`.
- Rationale: Cumple principio constitucional de seguridad por defecto sin bloquear operación de desarrollo.
- Alternatives considered:
  - Endpoints públicos sin auth: descartado por incumplimiento constitucional.
  - OAuth2/JWT: descartado para MVP por sobrecomplejidad fuera del alcance.

## Decision 7: Estrategia de pruebas
- Decision: Combinar pruebas unitarias (servicio/validación) con integración para repositorio/controlador y flujo CRUD básico.
- Rationale: Cubre reglas de dominio y comportamiento observable con costo moderado.
- Alternatives considered:
  - Solo unitarias: descartado por baja confianza en integración real con persistencia.
  - Solo E2E: descartado por costo y menor granularidad diagnóstica.

## Decision 8: Objetivos no funcionales para este feature
- Decision: Definir objetivo de p95 < 200ms para operaciones CRUD simples con hasta 10k empleados y 10 usuarios concurrentes.
- Rationale: Proporciona umbral verificable y realista para alcance administrativo interno.
- Alternatives considered:
  - Sin objetivo de performance: descartado por dejar el plan sin criterio medible.
  - Objetivos de alta escala (1000+ rps): descartado por sobredimensionado para el dominio.

## Decision 9: Estrategia de versionamiento de rutas
- Decision: Exponer únicamente rutas versionadas bajo `/api/v1/...` para todas las operaciones CRUD.
- Rationale: Cumple la constitución y evita contratos duplicados/ambiguos con rutas legacy.
- Alternatives considered:
  - Mantener rutas antiguas y nuevas en paralelo: descartado por deuda de mantenimiento.
  - Redirección automática desde rutas legacy: descartado por acoplar transición en runtime sin necesidad.

## Decision 10: Política de paginación y ordenamiento en colecciones
- Decision: En `GET` de colecciones, `page` y `sort` son obligatorios; `size` es opcional con valor por defecto `20` y límite máximo `100`. El ordenamiento se restringe a `clave`, `nombre`, `direccion`, `telefono`.
- Rationale: Balancea control del cliente con límites operativos seguros y reglas explícitas de contrato.
- Alternatives considered:
  - Hacer `size` obligatorio: descartado por menor usabilidad.
  - Permitir sort libre por cualquier campo: descartado por riesgo de consultas no controladas.
