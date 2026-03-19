# Contract: Login Flow and Route Protection (Feature 007)

## Purpose
Definir el contrato funcional entre frontend y backend para login, sesión y autorización por rol.

## Login Request Contract
- Frontend MUST enviar credenciales al endpoint `POST /api/v1/auth/login`.
- El payload MUST tener la forma exacta:
	- `{ "username": "...", "password": "..." }`
- Frontend MUST normalizar correo (trim) antes de enviar.

## Login Success Contract
- El backend MUST responder autenticación exitosa incluyendo el rol del usuario.
- El rol MUST ser uno de: `ROLE_ADMIN`, `ROLE_USER`.
- La respuesta MUST incluir la forma mínima:
	- `{ "rol": "...", "nombre": "..." }`
- Frontend MUST construir y persistir credenciales Basic Auth para solicitudes posteriores.

## Login Failure Contract
- Ante credenciales inválidas, frontend MUST mantener estado no autenticado y mostrar feedback de error.
- Ante rol no permitido, frontend MUST limpiar sesión y redirigir a `/login`.

## Route Guard Contract
- Toda ruta privada MUST pasar por guard de autenticación.
- El guard MUST validar rol permitido por ruta (`/admin-dashboard` solo `ROLE_ADMIN`; `/home` solo `ROLE_USER` salvo reglas explícitas futuras).
- Si la validación falla (sin sesión o rol no permitido), frontend MUST limpiar sesión y redirigir a `/login`.

## Interceptor Contract
- Con sesión activa, el interceptor MUST agregar `Authorization: Basic <token>` en solicitudes al backend.
- El interceptor MUST omitir el header en `POST /api/v1/auth/login`.
- Sin sesión activa, el interceptor MUST no inventar credenciales.

## Compatibility Guarantees
- Basic Auth backend existente permanece intacta.
- No se alteran contratos de versión de ruta (`/api/v1`).
- El login frontend no requiere cambios de esquema de base de datos.
