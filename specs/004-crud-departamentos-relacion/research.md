# Research: CRUD de Departamentos y Relación Obligatoria

## Decision 1: Introducir relación obligatoria `empleado -> departamento` con Flyway en dos migraciones (`V4` y `V5`)
- **Rationale**: La especificación exige reset de base de datos y estructura nueva. Separar estructura (`V4`) y datos semilla (`V5`) mantiene trazabilidad y rollback conceptual claros.
- **Alternatives considered**:
  - Una sola migración para estructura + semillas: descartada por menor claridad de mantenimiento.
  - Carga manual de semillas fuera de Flyway: descartada por reproducibilidad deficiente.

## Decision 2: `departamentoId` obligatorio en creación y opcional en actualización parcial
- **Rationale**: Cumple exactamente con los requerimientos FR-005 y FR-006: integridad fuerte al crear, flexibilidad en PUT parcial para preservar datos existentes.
- **Alternatives considered**:
  - Obligatorio también en actualización: descartado porque rompe actualización parcial ya adoptada.
  - Opcional también en creación: descartado por riesgo de empleados huérfanos.

## Decision 3: Validar existencia de departamento en capa de servicio antes de persistir empleado
- **Rationale**: Permite errores de dominio explícitos y consistentes antes de violaciones de FK; mejora UX de API y testabilidad.
- **Alternatives considered**:
  - Confiar únicamente en FK de base de datos: descartado por mensajes de error menos claros y menor control de negocio.

## Decision 4: CRUD de Departamentos completo con restricciones de eliminación por integridad referencial
- **Rationale**: Permite gestión autónoma del catálogo y protege consistencia cuando hay empleados asociados.
- **Alternatives considered**:
  - Soft-delete de departamentos: descartado por no estar en alcance y aumentar complejidad.

## Decision 5: Mantener lógica de acceso de cuenta (email/password) en update de empleado con transaccionalidad
- **Rationale**: Se preserva funcionalidad previa (activación diferida), y la transacción evita persistencias parciales si falla duplicidad de correo o validación.
- **Alternatives considered**:
  - Actualizar empleado y cuenta en transacciones separadas: descartado por riesgo de estados inconsistentes.

## Decision 6: Refactor total de pruebas afectadas por `departamentoId` obligatorio en alta
- **Rationale**: El requerimiento explícito solicita actualizar TODAS las pruebas unitarias e integrales para compilar y validar nuevo contrato.
- **Alternatives considered**:
  - Ajustar solo tests que fallen en build: descartado por cobertura incompleta y riesgo de regresiones ocultas.

## Open Questions Resolution

No quedan `NEEDS CLARIFICATION` en esta fase. La especificación define de forma explícita migraciones, obligatoriedad/opcionalidad de campos y reglas transaccionales.
