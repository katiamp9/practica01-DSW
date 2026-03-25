# Phase 0 Research — Feature 010

## Scope

Definir decisiones técnicas para CRUD de departamentos con conteo dinámico de personal,
borrado protegido por integridad y UX reactiva en frontend.

## Decision 1: Conteo dinámico en backend

- Decision: Exponer `totalEmpleados` en `GET /api/v1/departamentos` usando cálculo en base de datos mediante `LEFT JOIN` + `COUNT(e.id)` en JPQL/proyección.
- Rationale: evita N+1 queries y mantiene el cómputo cerca de los datos para mayor eficiencia.
- Alternatives considered:
  - Contar empleados por cada fila en capa de servicio: descartado por costo de consultas adicionales.
  - Endpoint separado por departamento para conteo: descartado por complejidad y latencia adicional en UI.

## Decision 2: Alcance de `totalEmpleados` en contrato

- Decision: incluir `totalEmpleados` solo en el endpoint de listado.
- Rationale: satisface necesidad de tabla y reglas de delete sin ampliar contratos no requeridos.
- Alternatives considered:
  - Incluirlo también en `GET /{id}`: válido pero innecesario para objetivos actuales.

## Decision 3: Política de delete protegido

- Decision: cuando `totalEmpleados > 0`, backend responde `409 Conflict` en `DELETE /api/v1/departamentos/{id}`.
- Rationale: representa conflicto de integridad (recurso en uso) y es semánticamente más claro que `400`.
- Alternatives considered:
  - `400 Bad Request`: descartado por menor precisión semántica.

## Decision 4: Mapeo de error de integridad en UI

- Decision: capturar explícitamente el `409` y mostrar toast: `Error: Este departamento tiene empleados asociados y no puede ser eliminado`.
- Rationale: comunica causa de negocio y acción esperada del administrador.
- Alternatives considered:
  - Mostrar mensaje genérico de error: descartado por baja utilidad operativa.

## Decision 5: UX preventiva de borrado

- Decision: si `totalEmpleados > 0`, deshabilitar botón eliminar y mostrar tooltip explicativo.
- Rationale: previene intentos inválidos y reduce ruido de errores evitables.
- Alternatives considered:
  - Mantener botón activo y depender solo del backend: descartado por peor experiencia de uso.

## Decision 6: Gestión reactiva y búsqueda local

- Decision: usar Signals para estado de datos, filtro y feedback; búsqueda local por `nombre` sin roundtrip al servidor.
- Rationale: respuesta instantánea y menor carga del backend para interacción administrativa.
- Alternatives considered:
  - búsqueda remota por cada tecla: descartado por latencia y costo de red innecesario.

## Clarifications Status

- No quedan `NEEDS CLARIFICATION` para esta fase.
- Quedó fijado: `409 Conflict` en delete protegido y `totalEmpleados` solo en listado.
