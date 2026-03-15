# Data Model: Clave Compuesta Empleados

## Entity: Empleado

### Description
Registro administrativo de empleado identificado por una clave de negocio compuesta lógica.

### Fields
- `clave` (string, PK)
  - Required: yes
  - Constraints:
    - formato `EMP-<consecutivo>`
    - único e inmutable
- `prefijo` (string)
  - Required: yes
  - Constraints:
    - valor fijo `EMP-`
- `consecutivo` (integer)
  - Required: yes
  - Constraints:
    - entero positivo autonumérico
    - único
- `nombre` (string)
  - Required: yes
  - Constraints:
    - no vacío tras trim
    - máximo 100 caracteres
- `direccion` (string)
  - Required: yes
  - Constraints:
    - no vacío tras trim
    - máximo 100 caracteres
- `telefono` (string)
  - Required: yes
  - Constraints:
    - no vacío tras trim
    - máximo 100 caracteres

### Derived/Operational Attributes
- `createdAt` (timestamp)
- `updatedAt` (timestamp)

## Relationships
- No requiere relaciones obligatorias con otras entidades en este feature.

## Query Model: EmpleadoListQuery

### Description
Parámetros de consulta para endpoint de listado paginado.

### Fields
- `page` (integer)
  - Required: yes
  - Constraints:
    - mínimo 0
- `size` (integer)
  - Required: no
  - Default: 20
  - Constraints:
    - mínimo 1
    - máximo 100
- `sort` (array/string)
  - Required: yes
  - Constraints:
    - formato `campo,direccion`
    - campos permitidos: `clave`, `nombre`, `direccion`, `telefono`
    - dirección: `asc` o `desc`

## Validation Rules
1. `clave` no se recibe en create; siempre se genera internamente.
2. `clave` debe seguir el patrón `EMP-[0-9]+`.
3. `prefijo` debe ser `EMP-`.
4. `consecutivo` debe ser único y no reutilizable.
5. `nombre`, `direccion`, `telefono` son obligatorios y máximo 100.
6. En update solo se modifican `nombre`, `direccion`, `telefono`.
7. Clave inexistente en query/update/delete produce estado no encontrado.
8. Parámetros de listado inválidos (`page/size/sort`) producen `422`.

## State Transitions

### Empleado Lifecycle
- `NON_EXISTENT` -> `ACTIVE`
  - Trigger: alta válida
- `ACTIVE` -> `ACTIVE`
  - Trigger: actualización de datos permitidos
- `ACTIVE` -> `NON_EXISTENT`
  - Trigger: eliminación

### Invalid Transitions
- `NON_EXISTENT` -> `ACTIVE` con colisión persistente de consecutivo: conflicto.
- `ACTIVE` -> `ACTIVE` cambiando clave: rechazado.
- `NON_EXISTENT` -> `NON_EXISTENT` en update/delete: no encontrado.
