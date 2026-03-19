# Data Model — Feature 006

## Entity: Empleado
- Description: Registro de empleado del sistema con datos personales, relación departamental y rol de acceso.
- Fields:
  - `clave` (string, required)
  - `nombre` (string, required)
  - `direccion` (string, required)
  - `telefono` (string, required)
  - `departamentoId` (uuid/int, required)
  - `rol` (string, required, default `ROLE_USER`)
- Validation Rules:
  - `rol` MUST ser no nulo.
  - `rol` MUST pertenecer al conjunto permitido inicial (`ROLE_USER`, `ROLE_ADMIN`).
- State Transitions:
  - `created` → `active` con `rol=ROLE_USER` por defecto si no se especifica.

## Entity: AdministradorInicial
- Description: Usuario administrativo preconfigurado durante bootstrap de datos.
- Fields:
  - `username` (string, required)
  - `passwordHash` (string, required)
  - `rol` (string, required = `ROLE_ADMIN`)
- Validation Rules:
  - `rol` MUST ser `ROLE_ADMIN` para registro inicial.
- State Transitions:
  - `seed-pending` → `seeded-admin` con rol administrativo.

## Entity: RolDeAcceso
- Description: Valor de negocio usado para autorización en capa de seguridad.
- Fields:
  - `codigo` (string, required)
  - `grantedAuthority` (string, required)
- Validation Rules:
  - `codigo` MUST mapear 1:1 a una `GrantedAuthority` válida.
- State Transitions:
  - `persisted-role` → `runtime-authority` al cargar usuario en seguridad.

## Backfill Rule
- Todos los empleados existentes sin rol MUST actualizarse a `ROLE_USER` durante migración.
