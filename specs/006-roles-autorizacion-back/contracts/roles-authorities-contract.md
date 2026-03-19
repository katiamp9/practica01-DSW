# Contract: Roles and Authorities (Feature 006)

## Purpose
Definir cómo se persisten roles en empleados y cómo se traducen a autoridades de Spring Security.

## Persistence Contract
- `Empleado` MUST incluir columna/campo `rol` no nulo.
- Valor por defecto para nuevos empleados: `ROLE_USER`.
- Registros existentes sin valor MUST backfillearse a `ROLE_USER` durante migración.

## Seed Contract
- El script de inserción del administrador inicial MUST incluir `rol='ROLE_ADMIN'`.
- El bootstrap de datos MUST ser determinista en todos los entornos.

## Security Mapping Contract
- La carga de usuario para autenticación MUST obtener el rol persistido.
- El rol persistido MUST mapearse a una `GrantedAuthority` equivalente.
- El resultado de autenticación MUST incluir al menos una authority derivada de `rol`.
- El usuario administrativo configurado por entorno MUST conservar fallback con `ROLE_ADMIN`.

## Allowed Role Set (initial)
- `ROLE_USER`
- `ROLE_ADMIN`

## Compatibility Guarantees
- No se deshabilita Basic Auth existente.
- No se rompen endpoints ni contratos de ruta actuales (`/api/v1`).
- No quedan empleados con `rol` nulo tras la migración.
- La configuración de seguridad MUST tolerar contextos de prueba donde el servicio persistido no esté disponible.
