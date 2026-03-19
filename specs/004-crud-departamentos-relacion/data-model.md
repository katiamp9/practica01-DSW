# Data Model: CRUD de Departamentos y Relación Obligatoria

## Entity: Departamento (nueva)

### Description
Catálogo organizacional al que pertenece cada empleado.

### Fields
- `id` (long, PK)
  - Required: yes
  - Constraints: autogenerado, único
- `nombre` (string)
  - Required: yes
  - Constraints: no vacío, longitud máxima 100, único de negocio recomendado
- `createdAt` (timestamp)
  - Required: yes
- `updatedAt` (timestamp)
  - Required: yes

### Relationships
- `Departamento` (1) -> (N) `Empleado`

## Entity: Empleado (existente, extendida)

### Description
Registro de empleado con identidad de negocio existente (`clave`) y adscripción obligatoria a un departamento.

### Fields (impactadas)
- `clave` (string, PK)
  - Required: yes
- `nombre` (string)
  - Required: yes en create, opcional en update parcial
- `direccion` (string)
  - Required: yes en create, opcional en update parcial
- `telefono` (string)
  - Required: yes en create, opcional en update parcial
- `departamentoId` (long, FK -> `departamentos.id`)
  - Required: yes en create
  - Required: no en update parcial (si falta, se conserva valor actual)

### Relationships
- `Empleado` (N) -> (1) `Departamento`
- `Empleado` (1) -> (0..1) `CuentaEmpleado`

## Entity: CuentaEmpleado (existente)

### Description
Cuenta de acceso por correo vinculada de forma 1:1 al empleado.

### Fields relevantes
- `correo` (string)
  - Constraints: único case-insensitive
- `empleado_clave` (string, FK -> `empleados.clave`)
  - Required: yes

## Entity: CredencialEmpleado (existente)

### Description
Material de autenticación en hash BCrypt asociado a `CuentaEmpleado`.

### Fields relevantes
- `passwordHash` (string)
  - Required: yes
  - Constraints: hash no reversible

## API Input Models

### EmpleadoCreateRequest (extendido)
- `nombre`: string (required)
- `direccion`: string (required)
- `telefono`: string (required)
- `departamentoId`: long (required)
- `email`: string (optional)
- `password`: string (optional; si se envía, requiere `email`)

### EmpleadoUpdateRequest (extendido, parcial)
- `nombre`: string (optional)
- `direccion`: string (optional)
- `telefono`: string (optional)
- `departamentoId`: long (optional)
- `email`: string (optional)
- `password`: string (optional; si se envía, requiere `email`)

### DepartamentoRequest
- `nombre`: string (required)

### DepartamentoResponse
- `id`: long
- `nombre`: string

## Validation Rules
1. En creación de empleado, `departamentoId` es obligatorio.
2. En actualización de empleado, `departamentoId` solo se actualiza si llega en la petición.
3. `departamentoId` debe existir antes de persistir create/update de empleado.
4. Si se envía `password` en create/update, también debe enviarse `email`.
5. Correo de cuenta no puede duplicarse en otra cuenta (case-insensitive).
6. Operaciones empleado+cuenta en create/update deben ser transaccionales.

## State Transitions

### Activación diferida de cuenta por update
- `Empleado sin cuenta` -> `Empleado con cuenta activa`
  - Trigger: PUT con `email` + `password` válidos

### Actualización parcial de empleado
- `Empleado actual` -> `Empleado actualizado parcialmente`
  - Trigger: PUT con subset de campos no nulos
  - Rule: campos ausentes (`null`) conservan valor previo
