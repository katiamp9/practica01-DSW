# Phase 0 Research — Feature 006

## Scope
Agregar soporte de roles y autorización en backend manteniendo compatibilidad con autenticación básica y datos existentes.

## Decision 1: Persistir rol como String en Empleado
- Decision: Usar campo `rol` persistido como `String` en la entidad `Empleado`.
- Rationale: Minimiza fricción de migración, evita cambios de conversión adicionales y permite introducir `Enum` en una fase posterior sin bloquear entrega actual.
- Alternatives considered:
  - `Enum` JPA inmediato: rechazado por costo de refactor transversal y mayor riesgo de incompatibilidad en migración inicial.

## Decision 2: Default operativo `ROLE_USER` para nuevos y existentes
- Decision: Asignar `ROLE_USER` por defecto en creación de empleados y aplicar backfill a registros existentes sin rol.
- Rationale: Garantiza consistencia funcional y evita nullability issues en autorización.
- Alternatives considered:
  - Permitir `NULL` temporal y completar luego: rechazado por riesgo de fallos en carga de authorities.

## Decision 3: Administrador inicial con `ROLE_ADMIN`
- Decision: Actualizar la migración SQL que inserta administrador inicial para incluir columna `rol='ROLE_ADMIN'`.
- Rationale: Conserva privilegios administrativos desde bootstrap y evita dependencias de scripts manuales.
- Alternatives considered:
  - Promoción posterior vía script aparte: rechazado por riesgo operativo y no determinismo entre entornos.

## Decision 4: Mapping rol persistido → GrantedAuthority
- Decision: Convertir `rol` almacenado a `GrantedAuthority` en el flujo de carga de usuario de Spring Security.
- Rationale: Alinea persistencia con mecanismo estándar de autorización y mantiene compatibilidad con controles por rol.
- Alternatives considered:
  - Authorities fijas en memoria sin leer DB: rechazado por desacople con rol real del empleado.

## Clarifications Status
- NEEDS CLARIFICATION en Technical Context: ninguno.
- Estrategia de respaldo para empleados existentes: definida con backfill en migración.
