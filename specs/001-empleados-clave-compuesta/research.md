# Phase 0 Research: Clave Compuesta Empleados

## Decision 1: Estrategia de generación de clave
- Decision: Generar clave con formato `EMP-<consecutivo>` en backend, sin aceptar clave manual del cliente.
- Rationale: Garantiza consistencia de formato, evita errores de captura y centraliza la regla de negocio.
- Alternatives considered:
  - Clave manual enviada por cliente: descartado por riesgo de inconsistencias.
  - Clave UUID: descartado por no cumplir requerimiento de prefijo legible.

## Decision 2: Persistencia de componentes de clave
- Decision: Persistir `clave` como identificador primario y mantener `prefijo` + `consecutivo` como componentes lógicos trazables.
- Rationale: Permite consultas directas por clave y auditoría del mecanismo de numeración.
- Alternatives considered:
  - Guardar solo `clave` sin componentes: descartado por menor trazabilidad operativa.
  - PK compuesta física en JPA: descartado por mayor complejidad de mapeo para un caso que no la requiere.

## Decision 3: Concurrencia en generación de consecutivo
- Decision: Reintentar automáticamente hasta 3 veces la operación de alta ante colisión de consecutivo; si persiste, devolver `409 Conflict`.
- Rationale: Reduce fallos transitorios por carrera sin ocultar conflictos reales de unicidad.
- Alternatives considered:
  - Sin reintentos (fallo inmediato): descartado por mayor tasa de error bajo concurrencia.
  - Bloqueo global serializado: descartado por impacto en throughput y escalabilidad.

## Decision 4: Política de errores HTTP
- Decision: Usar `400` para payload inválido, `404` para clave inexistente, `409` para colisión de clave y `422` para paginación/ordenamiento inválidos.
- Rationale: Alinea semántica HTTP con comportamiento observable y facilita pruebas de contrato.
- Alternatives considered:
  - Un único código `400` para todo: descartado por ambigüedad funcional.

## Decision 5: Paginación y ordenamiento en colecciones
- Decision: Mantener `page` y `sort` obligatorios; `size` opcional con default 20 y máximo 100; whitelist de `sort` en `clave`, `nombre`, `direccion`, `telefono`.
- Rationale: Controla carga de consultas, evita ordenamientos arbitrarios y mantiene compatibilidad constitucional.
- Alternatives considered:
  - Permitir sort libre: descartado por riesgo de degradación y comportamiento inconsistente.

## Decision 6: Seguridad y contrato
- Decision: Mantener Basic Auth en endpoints de negocio y documentar en OpenAPI de `/api/v1`.
- Rationale: Cumplimiento directo de constitución y trazabilidad de integración.
- Alternatives considered:
  - Endpoints públicos para CRUD: descartado por incumplir principio de seguridad por defecto.

## Decision 7: Validación de calidad
- Decision: Validar cambios con pruebas unitarias e integración en flujo de build (`mvn test`) antes de cierre.
- Rationale: Cumple el principio de calidad verificable y reduce riesgo de regresión.
- Alternatives considered:
  - Solo validación manual: descartado por baja reproducibilidad.
