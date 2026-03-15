# Phase 0 Research: Autenticación de empleados por correo y contraseña

## Decision 1: Referencia de `cuentas_empleado` hacia `empleados`
- Decision: `cuentas_empleado` referenciará la clave de negocio existente `empleados.clave` (VARCHAR) como FK única, y mantendrá su propio `id` UUID como PK técnica.
- Rationale: la identidad canónica actual del dominio es `clave` (p. ej. `EMP-1001`); usarla evita introducir migraciones de alto impacto para un identificador alternativo en `empleados` y preserva compatibilidad con CRUD existente.
- Alternatives considered:
  - Introducir `empleados.id UUID` como nuevo identificador alternativo y referenciarlo desde `cuentas_empleado`: descartado para este alcance por requerir migración transversal de entidad, repositorio, endpoints y contratos existentes.
  - FK compuesta (`prefijo`,`consecutivo`): descartado porque físicamente ya existe una clave única simple (`clave`) y añadir FK compuesta incrementa complejidad sin beneficio funcional.

## Decision 2: Modelo de tablas de autenticación
- Decision: crear dos tablas: `cuentas_empleado(id UUID, correo, clave_empleado)` y `credenciales_empleado(id UUID, cuenta_empleado_id, password_hash)` con relación 1:1.
- Rationale: separa datos de login (correo) de material sensible (hash), reduce acoplamiento y facilita endurecimiento de acceso futuro.
- Alternatives considered:
  - Una sola tabla con correo y hash: descartado por menor separación de responsabilidades y menor flexibilidad para rotación/estado de credenciales.

## Decision 3: Generación de UUID en PostgreSQL
- Decision: usar `gen_random_uuid()` en migración Flyway `V3` y habilitar `pgcrypto` si no existe.
- Rationale: cumple requerimiento explícito de la spec y mantiene generación de identificadores en BD de forma determinista y auditable.
- Alternatives considered:
  - UUID generado en aplicación Java: descartado por no cumplir el requerimiento técnico declarado para migración.

## Decision 4: Estrategia de hash de contraseñas
- Decision: usar `BCryptPasswordEncoder` para crear/validar `password_hash`.
- Rationale: alinea seguridad moderna de Spring Security y cumple requisito de no almacenar contraseñas en texto plano.
- Alternatives considered:
  - `NoOpPasswordEncoder`: descartado por inseguro y actualmente solo válido para entorno de demo.
  - SHA-256 manual sin sal adaptativa: descartado por menor resistencia a fuerza bruta.

## Decision 5: Integración con seguridad existente
- Decision: extender la configuración existente de `SecurityConfig` en lugar de crearla desde cero, manteniendo Basic Auth por defecto y permitiendo explícitamente `POST /api/v1/auth/login`.
- Rationale: el proyecto ya tiene `SecurityFilterChain`; extenderla minimiza riesgo y mantiene cumplimiento del Principio II de la Constitución.
- Alternatives considered:
  - Reemplazar toda la configuración de seguridad: descartado por mayor riesgo de regresiones en endpoints CRUD protegidos.

## Decision 6: Política de respuestas de login
- Decision: `POST /api/v1/auth/login` responderá `200 OK` en éxito y `401 Unauthorized` con mensaje uniforme `Invalid email or password` en cualquier fallo de credencial.
- Rationale: evita enumeración de cuentas y cumple requisitos funcionales de la spec.
- Alternatives considered:
  - Diferenciar mensajes entre correo inexistente y contraseña inválida: descartado por filtración de información.

## Decision 7: Auditoría de autenticación
- Decision: definir `AuthenticationAuditService` como servicio de aplicación para registrar todos los intentos (éxito/fallo) con metadatos mínimos.
- Rationale: actualmente no existe implementación en el código; introducirlo como abstracción dedicada permite trazabilidad y pruebas.
- Alternatives considered:
  - Registrar solo en logs de controlador: descartado por menor consistencia y dificultad de prueba.

## Decision 8: Validación de calidad
- Decision: agregar pruebas unitarias de servicio de autenticación y pruebas de integración de `POST /api/v1/auth/login` con escenarios éxito/fallo.
- Rationale: cubre flujo crítico de seguridad y evidencia constitucional de calidad verificable.
- Alternatives considered:
  - Solo pruebas manuales con curl: descartado por falta de reproducibilidad en pipeline.
