# Phase 0 Research — Feature 009

## Scope
Consolidar decisiones técnicas para CRUD administrativo de empleados con enfoque en visibilidad y gobernanza de `email`, preservando reglas ya aprobadas de departamentos, confirmación de eliminación y seguridad.

## Decision 1: Estrategia de listado con `email`
- Decision: El backend usará `LEFT JOIN` entre empleados y cuentas al listar empleados para incluir siempre todos los empleados.
- Rationale: Evita ocultar empleados sin cuenta asociada y corrige la columna Email vacía por falta de join.
- Alternatives considered:
  - `INNER JOIN`: descartado porque excluye empleados sin cuenta y rompe visibilidad de padrón.
  - Resolver `email` con consultas separadas por fila: descartado por costo y complejidad innecesaria.

## Decision 2: Contrato canónico del campo
- Decision: El contrato JSON de administración normaliza el campo como `email`.
- Rationale: Elimina ambigüedad con `correo` y simplifica mapeos frontend/backend.
- Alternatives considered:
  - Mantener `correo`: descartado por inconsistencia con la UI actual.
  - Exponer ambos campos (`email` y `correo`): descartado por deuda técnica de compatibilidad temporal.

## Decision 3: Semántica para empleados sin cuenta
- Decision: Backend devuelve `email = null` cuando no hay cuenta; frontend muestra `Sin correo`.
- Rationale: Separa semántica de datos (`null`) de representación UX (`Sin correo`) y evita celdas vacías ambiguas.
- Alternatives considered:
  - Devolver string vacío `""`: descartado por ocultar distinción entre dato ausente y dato vacío.
  - Excluir empleados sin cuenta: descartado por pérdida de trazabilidad operacional.

## Decision 4: Edición de `email` en formulario
- Decision: En modo edición, `email` se precarga y se muestra como `readonly`.
- Rationale: Mantiene visibilidad del identificador de cuenta sin permitir alteración desde pantalla de datos de empleado.
- Alternatives considered:
  - `disabled`: descartado por fricción en serialización del form y pruebas.
  - Ocultar campo: descartado por pérdida de contexto para administradores.

## Decision 5: Inmutabilidad backend de `email`
- Decision: Si una actualización intenta cambiar `email`, el backend rechaza con `400 Bad Request` y mensaje explícito.
- Rationale: Defensa en profundidad ante bypass de UI y coherencia entre identidad de cuenta y perfil de empleado.
- Alternatives considered:
  - Ignorar cambio silenciosamente: descartado por comportamiento implícito difícil de auditar.
  - Permitir cambio: descartado por contradecir regla de negocio del identificador.

## Decision 6: Protección de eliminación
- Decision: Mantener protección dual: UI oculta self-delete y backend rechaza borrar `admin@empresa.com` con `403`.
- Rationale: Equilibrio entre UX segura y enforcement server-side.
- Alternatives considered:
  - Solo protección frontend: descartado por vulnerabilidad ante llamadas directas.
  - Solo protección backend: descartado por UX menos preventiva.

## Decision 7: Catálogo de departamentos reactivo
- Decision: Cargar departamentos en apertura del formulario con Signals y mapear `departamentoId`↔nombre.
- Rationale: Permite select correcto y render legible en tabla sin recarga total.
- Alternatives considered:
  - Carga tardía por foco: descartada por latencia percibida.
  - Nombre en payload: descartado por contrato de ID.

## Clarifications Status
- NEEDS CLARIFICATION en Technical Context: ninguno.
- Todas las decisiones de contrato, seguridad y UX para `email` quedaron resueltas en sesión 2026-03-21.
