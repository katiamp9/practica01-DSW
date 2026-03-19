# Data Model — Feature 007

## Entity: LoginSession
- Description: Estado de sesión del frontend para navegación autenticada.
- Fields:
  - `isAuthenticated` (boolean, required)
  - `basicAuthToken` (string, required when authenticated)
  - `role` (string, required when authenticated)
  - `email` (string, optional)
- Validation Rules:
  - `role` MUST ser `ROLE_ADMIN` o `ROLE_USER` cuando `isAuthenticated=true`.
  - `basicAuthToken` MUST existir cuando `isAuthenticated=true`.
- State Transitions:
  - `anonymous` → `authenticated-admin` tras login válido con `ROLE_ADMIN`.
  - `anonymous` → `authenticated-user` tras login válido con `ROLE_USER`.
  - `authenticated-*` → `anonymous` por logout, sesión inválida o rol no permitido.

## Entity: UserIdentity
- Description: Identidad funcional para personalizar rutas y experiencia inicial.
- Fields:
  - `email` (string, required)
  - `role` (string, required)
- Validation Rules:
  - `email` MUST tener formato válido.
  - `role` MUST pertenecer al conjunto permitido inicial.
- State Transitions:
  - `unknown` → `resolved` al completar login exitoso.

## Entity: RouteAccessPolicy
- Description: Regla de autorización por ruta privada.
- Fields:
  - `path` (string, required)
  - `requiresAuthentication` (boolean, required)
  - `allowedRoles` (array<string>, optional)
  - `redirectOnDeny` (string, required)
- Validation Rules:
  - Si `requiresAuthentication=true`, MUST existir validación de sesión activa.
  - Si hay `allowedRoles`, el rol actual MUST pertenecer al conjunto.
- State Transitions:
  - `pending-check` → `granted` cuando sesión/rol cumplen.
  - `pending-check` → `denied` cuando sesión o rol no cumplen.

## Session Storage Rule
- El frontend MUST persistir credenciales Basic Auth y rol en localStorage solo durante sesión activa y MUST limpiar estos datos al detectar sesión inválida o acceso no autorizado.
