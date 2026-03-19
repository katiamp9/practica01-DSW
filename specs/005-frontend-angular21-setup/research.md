# Phase 0 Research — Feature 005

## Scope
Preparar estructura frontend Angular 21 e integración local segura con backend Spring Boot existente.

## Decision 1: Política CORS de desarrollo acotada
- Decision: Habilitar CORS únicamente para `http://localhost:4200` con métodos `GET, POST, PUT, DELETE, OPTIONS` y cabeceras necesarias incluyendo `Authorization`.
- Rationale: Cumple la integración local solicitada sin debilitar el principio de seguridad por defecto ni abrir orígenes no requeridos.
- Alternatives considered:
  - Permitir `*` como origen: rechazado por riesgo de exposición innecesaria.
  - Deshabilitar CORS y depender de cliente separado: rechazado por bloquear pruebas de integración frontend-backend.

## Decision 2: Estándares Angular 21 obligatorios desde bootstrap
- Decision: Establecer en constitución para frontend el uso de Standalone Components, Signals y Control Flow nativo para nuevas piezas.
- Rationale: Uniforma estilo de desarrollo y evita deuda técnica temprana para siguientes funcionalidades (incluido login).
- Alternatives considered:
  - Permitir mezcla con patrones legados basados en NgModules: rechazado para evitar inconsistencias.
  - Postergar estándares hasta feature de login: rechazado porque retrasa alineación del equipo.

## Decision 3: Proxy local para prefijo `/api`
- Decision: Configurar `proxy.conf.json` en frontend para redirigir `/api` a backend local `http://localhost:8080`.
- Rationale: Evita hardcode de hosts en cada llamada y simplifica pruebas locales reproducibles.
- Alternatives considered:
  - URLs absolutas por entorno en servicios: rechazado por mayor fricción y riesgo de inconsistencias.
  - Variable de entorno por desarrollador sin proxy: rechazado por menor estandarización.

## Decision 4: Contrato de integración sin cambio de endpoints
- Decision: Documentar contrato de integración frontend-backend (origen, headers, métodos, proxy) sin modificar contrato funcional OpenAPI existente.
- Rationale: La feature es de infraestructura y conectividad, no introduce nuevos recursos REST.
- Alternatives considered:
  - Actualizar OpenAPI con secciones no funcionales de CORS: rechazado por no ser el vehículo principal para reglas operativas de dev proxy.
  - No documentar contrato de integración: rechazado por afectar reproducibilidad.

## Clarifications Status
- NEEDS CLARIFICATION en contexto técnico: ninguno.
- Dependencias principales y patrones de integración: resueltos en decisiones anteriores.
