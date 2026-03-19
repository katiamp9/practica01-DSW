# Contract: Frontend-Backend Local Integration (Feature 005)

## Purpose
Definir el contrato operativo de integración local entre frontend y backend para eliminar errores de conexión durante desarrollo.

## Participants
- Frontend dev server: `http://localhost:4200`
- Backend API server: `http://localhost:8080`

## Route Contract
- Frontend MUST consumir rutas relativas bajo prefijo `/api`.
- Dev proxy MUST reenviar `/api/*` hacia backend local.
- Backend MUST mantener contrato de recursos bajo `/api/v1/*`.

## CORS Contract (Development)
- Allowed Origin: `http://localhost:4200`
- Allowed Methods: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`
- Allowed Headers: incluye `Authorization` y `Content-Type`
- Credentials: permitido solo si la implementación lo requiere explícitamente
- Config source: propiedades `app.cors.*` en `application.yml` (`APP_CORS_ALLOWED_ORIGINS`, `APP_CORS_ALLOWED_METHODS`, `APP_CORS_ALLOWED_HEADERS`)

## Proxy Contract (Implementation)
- File: `frontend-empleados/proxy.conf.json`
- Route prefix: `/api`
- Target: `http://localhost:8080`
- Flags: `changeOrigin=true`, `secure=false`

## Security Contract
- Basic Auth del backend permanece obligatoria para endpoints protegidos.
- CORS NO implica deshabilitar autenticación/autorización.
- CORS se limita a integración local de desarrollo y no define política productiva.

## Verification Contract
- Dado backend y frontend levantados localmente, una solicitud frontend a `/api/v1/**`:
  - no falla por CORS,
  - respeta autenticación requerida,
  - mantiene semántica de respuesta del endpoint original.

## Out of Scope
- Definición de nuevas operaciones REST.
- Cambios en modelo de datos o migraciones de base de datos.
- Implementación funcional de Login (se habilita como siguiente feature).
