# Phase 0 Research — Feature 008

## Scope
Definir el diseño funcional y técnico del dashboard administrativo con navegación superior, tabla paginada de empleados, estados de carga/error y placeholders de navegación protegidos.

## Decision 1: Consumo de listado con paginación obligatoria
- Decision: Consumir `GET /api/v1/empleados` enviando siempre `page`, `size` y `sort` en cada consulta.
- Rationale: El backend valida estos parámetros como obligatorios y falla si faltan, por lo que el frontend debe declararlos explícitamente desde el primer request.
- Alternatives considered:
  - Omitir parámetros y depender de defaults del backend: rechazada porque `page` y `sort` son obligatorios en validación.
  - Usar endpoint alterno sin paginación: rechazada porque no existe contrato equivalente para colección de empleados.

## Decision 2: Tamaño inicial de página
- Decision: Usar tamaño inicial fijo de `10` empleados por página en dashboard.
- Rationale: Está definido por clarificación de requerimientos y alinea experiencia de lectura con el objetivo de navegación rápida.
- Alternatives considered:
  - Mantener default backend `20`: rechazada por inconsistencia con decisión de negocio ya aclarada.

## Decision 3: Estrategia de ordenamiento inicial
- Decision: Usar ordenamiento inicial `sort=nombre,asc` en la primera carga.
- Rationale: Provee lectura predecible para usuarios administrativos y cumple validación de campo permitido.
- Alternatives considered:
  - Ordenar por `clave,asc`: viable, pero se prioriza legibilidad por nombre en esta iteración.
  - Orden dinámico configurable en UI: rechazada para evitar sobrealcance de la feature.

## Decision 4: Modelo de estado reactivo en dashboard
- Decision: Gestionar estado de pantalla con Signals y cuatro estados explícitos: `loading`, `data`, `empty`, `error`.
- Rationale: Reduce lógica condicional difusa y permite render declarativo coherente con Angular 21.
- Alternatives considered:
  - Estado implícito basado en longitud de arreglo y bandera de error: rechazada por menor claridad de transición.

## Decision 5: Manejo de error con reintento manual
- Decision: Mostrar mensaje claro y botón manual “Reintentar” que relanza la última consulta paginada.
- Rationale: Responde al criterio funcional FR-013 y mejora recuperación de errores temporales de red.
- Alternatives considered:
  - Reintento automático con backoff: rechazado por no estar requerido en esta iteración.
  - Solo mensaje sin acción: rechazado por no cumplir clarificación explícita.

## Decision 6: Navegación administrativa y placeholders
- Decision: Exponer barra superior con rutas `Inicio`, `Empleados`, `Departamentos`; usar `/admin/empleados` y `/admin/departamentos` para placeholders con texto “Próximamente”.
- Rationale: Asegura estructura de navegación extensible sin bloquear avance por funcionalidades no implementadas.
- Alternatives considered:
  - Links deshabilitados sin rutas: rechazada por menor testabilidad de flujo de navegación.

## Decision 7: Etiquetado visual de rol
- Decision: Mostrar etiquetas visuales diferenciadas por rol y fallback visual neutro para rol desconocido.
- Rationale: Cubre casos esperados (`ROLE_ADMIN`, `ROLE_USER`) y edge case de rol no reconocido sin romper UI.
- Alternatives considered:
  - Renderizar rol en texto plano: rechazada por menor escaneabilidad y menor cumplimiento de requerimiento visual.

## Clarifications Status
- NEEDS CLARIFICATION en Technical Context: ninguno.
- Decisiones de identificador principal, rutas placeholder, estrategia de error/reintento y paginación: resueltas en sesión 2026-03-21.
