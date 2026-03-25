# Phase 0 Research — Feature 007

## Scope
Implementar login frontend con redirección por rol y protección de rutas usando Angular 21, preservando Basic Auth del backend.

## Decision 1: Fuente de rol en login
- Decision: El frontend obtiene el rol directamente de la respuesta de login exitoso.
- Rationale: Evita round-trip adicional y simplifica flujo de redirección inicial y guard.
- Alternatives considered:
  - Llamada adicional para recuperar perfil/rol: rechazada por mayor latencia y complejidad.

## Decision 2: Manejo de rol no soportado
- Decision: Si el rol no es `ROLE_ADMIN` ni `ROLE_USER`, se limpia sesión y se redirige a `/login`.
- Rationale: Es la postura más segura y evita acceso accidental a rutas privadas.
- Alternatives considered:
  - Tratar rol desconocido como usuario estándar: rechazado por riesgo de autorización incorrecta.
  - Mostrar 403 con sesión activa: rechazado por estado inconsistente y mayor complejidad.

## Decision 3: Estrategia de guard
- Decision: El guard valida simultáneamente autenticación y autorización por rol por ruta.
- Rationale: Centraliza reglas de acceso y evita chequeos duplicados dentro de componentes.
- Alternatives considered:
  - Validar rol solo en componentes: rechazado por riesgo de renderizado parcial no autorizado.

## Decision 4: Estado de autenticación
- Decision: Mantener estado de autenticación con Signals y sincronizarlo con localStorage.
- Rationale: Simplifica consumo reactivo en Angular 21 y respeta la convención de la constitución.
- Alternatives considered:
  - Estado solo en localStorage sin signal: rechazado por baja reactividad y mayor acoplamiento.

## Decision 5: Interceptor de autenticación
- Decision: Adjuntar header `Authorization: Basic ...` en cada request al backend cuando haya sesión válida.
- Rationale: Alinea frontend con Basic Auth vigente sin exigir cambios en backend.
- Alternatives considered:
  - Adjuntar token/manual por servicio: rechazado por duplicación y mayor probabilidad de errores.

## Decision 6: Dirección visual de UI
- Decision: Usar un diseño moderno y profesional con layout consistente entre login, home y dashboard.
- Rationale: Mejora claridad percibida y da continuidad visual para las siguientes features.
- Alternatives considered:
  - UI mínima sin intención estética: rechazada por bajo valor de experiencia para esta feature.

## Clarifications Status
- NEEDS CLARIFICATION en la spec: ninguno.
- Reglas de rol, sesión inválida y obtención de rol: resueltas en clarificaciones de sesión 2026-03-19.
