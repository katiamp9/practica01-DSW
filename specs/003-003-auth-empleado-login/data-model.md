# Data Model: Autenticación de empleados

## Entity: Empleado (existente)

### Description
Entidad de negocio existente identificada por clave de negocio `clave`.

### Relevant Fields
- `clave` (string, PK)
  - Required: yes
  - Constraints: patrón `EMP-[0-9]+`, único, inmutable
- `nombre`, `direccion`, `telefono` (string)
  - Required: yes

## Entity: CuentaEmpleado (nueva)

### Description
Cuenta de acceso asociada a un empleado para autenticación por correo.

### Fields
- `id` (uuid, PK)
  - Required: yes
  - Generation: `gen_random_uuid()` en PostgreSQL
- `correo` (string)
  - Required: yes
  - Constraints:
    - formato email válido
    - único global (normalizado en minúsculas)
- `claveEmpleado` (string, FK -> `empleados.clave`)
  - Required: yes
  - Constraints:
    - referencia obligatoria a empleado existente
    - único (relación 1:1 empleado-cuenta)
- `createdAt` (timestamp)
  - Required: yes
- `updatedAt` (timestamp)
  - Required: yes

### Relationships
- `CuentaEmpleado` (1) -> (1) `Empleado` por `claveEmpleado`.
- `CuentaEmpleado` (1) -> (1) `CredencialEmpleado` por `cuentaEmpleadoId`.

## Entity: CredencialEmpleado (nueva)

### Description
Material de autenticación protegido asociado a una cuenta.

### Fields
- `id` (uuid, PK)
  - Required: yes
  - Generation: `gen_random_uuid()` en PostgreSQL
- `cuentaEmpleadoId` (uuid, FK -> `cuentas_empleado.id`)
  - Required: yes
  - Constraints: único (1:1)
- `passwordHash` (string)
  - Required: yes
  - Constraints:
    - hash BCrypt
    - nunca almacenar password plano
- `createdAt` (timestamp)
  - Required: yes
- `updatedAt` (timestamp)
  - Required: yes

## Entity: AuthenticationAuditEvent (nueva, aplicación/auditoría)

### Description
Evento de auditoría de intento de autenticación.

### Fields
- `timestamp` (datetime)
- `email` (string, puede enmascararse)
- `outcome` (enum: `SUCCESS`, `FAILURE`)
- `reasonCode` (enum: `EMAIL_NOT_FOUND`, `INVALID_PASSWORD`, `ACCOUNT_INCONSISTENT`)
- `source` (string opcional, p. ej. ip/user-agent)

## API Input Model: LoginRequest

### Fields
- `email` (string)
  - Required: yes
  - Validation: no vacío, formato email
- `password` (string)
  - Required: yes
  - Validation: no vacío

## API Output Model: LoginResponse

### Success
- `authenticated` (boolean = true)
- `employeeKey` (string, opcional según contrato final)

### Error
- `code` (string)
- `message` (string fijo: `Invalid email or password`)

## Validation Rules
1. El correo de login se normaliza (trim/lowercase) antes de búsqueda.
2. Si no existe `CuentaEmpleado` por correo, respuesta `401` con mensaje uniforme.
3. Si existe cuenta pero no credencial asociada, respuesta `401` con mensaje uniforme.
4. Si BCrypt no valida el hash, respuesta `401` con mensaje uniforme.
5. Cada intento de login debe registrar evento en `AuthenticationAuditService`.
6. Ningún flujo de aplicación persiste ni retorna contraseñas en texto plano.

## State Transitions

### Login Attempt Lifecycle
- `RECEIVED` -> `SUCCESS`
  - Trigger: cuenta existe + hash válido
- `RECEIVED` -> `FAILURE`
  - Trigger: cuenta inexistente, credencial inconsistente o hash inválido

### Credential Lifecycle
- `NOT_CREATED` -> `ACTIVE`
  - Trigger: alta inicial de credencial con hash BCrypt
- `ACTIVE` -> `ACTIVE`
  - Trigger: rotación de contraseña (nuevo hash)
