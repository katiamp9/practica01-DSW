# Data Model: CRUD de Empleados

## Entity: Empleado

### Description
Representa un registro administrativo de empleado dentro del catálogo del sistema.

### Fields
- `clave` (string, PK)
  - Required: yes
  - Constraints:
    - formato obligatorio: `EMP-<consecutivo>`
    - prefijo fijo: `EMP-`
    - componente numérico: entero positivo autonumérico
    - única en todo el sistema
    - generada por el sistema e inmutable después de crear
- `prefijo` (string, componente de PK compuesta lógica)
  - Required: yes
  - Constraints:
    - valor fijo `EMP-`
- `consecutivo` (integer, componente de PK compuesta lógica)
  - Required: yes
  - Constraints:
    - autonumérico
    - único
    - no reutilizable
- `nombre` (string)
  - Required: yes
  - Constraints:
    - longitud mínima: 1 (sin espacios en blanco al recortar)
    - longitud máxima: 100
- `direccion` (string)
  - Required: yes
  - Constraints:
    - longitud mínima: 1 (sin espacios en blanco al recortar)
    - longitud máxima: 100
- `telefono` (string)
  - Required: yes
  - Constraints:
    - longitud mínima: 1 (sin espacios en blanco al recortar)
    - longitud máxima: 100

### Derived/Operational Attributes
- `createdAt` (timestamp, generado por sistema)
- `updatedAt` (timestamp, generado por sistema)

## Relationships
- No existen relaciones obligatorias con otras entidades para este feature.

## Query Model: EmpleadoListQuery

### Description
Representa los parámetros de consulta para el endpoint de listado paginado de empleados.

### Fields
- `page` (integer)
  - Required: yes
  - Constraints:
    - mínimo: 0
- `size` (integer)
  - Required: no
  - Default: 20
  - Constraints:
    - mínimo: 1
    - máximo: 100
- `sort` (array/string)
  - Required: yes
  - Constraints:
    - formato Spring Data: `campo,direccion`
    - campos permitidos: `clave`, `nombre`, `direccion`, `telefono`
    - dirección permitida: `asc` o `desc`

## Validation Rules
1. Ningún campo obligatorio de entrada (`nombre`, `direccion`, `telefono`) puede ser nulo o vacío tras `trim`.
2. La `clave` se genera internamente con patrón `EMP-<consecutivo>`.
3. `prefijo` debe ser `EMP-` y `consecutivo` debe ser entero positivo autonumérico.
4. La combinación lógica (`prefijo`, `consecutivo`) debe ser única.
5. `nombre`, `direccion` y `telefono` deben cumplir máximo 100 caracteres.
6. Actualización solo permite modificar `nombre`, `direccion`, `telefono`.
7. Operaciones sobre `clave` inexistente resultan en estado "no encontrado".
8. En listado, `page` y `sort` son obligatorios; ausencia o formato inválido produce error.
9. En listado, `size` por defecto es 20 cuando no se envía.
10. En listado, `size > 100` o `sort` fuera de campos permitidos produce `422`.

## State Transitions

### Empleado Lifecycle
- `NON_EXISTENT` -> `ACTIVE`
  - Trigger: creación exitosa (`POST /api/v1/empleados`)
- `ACTIVE` -> `ACTIVE`
  - Trigger: actualización de datos (`PUT /api/v1/empleados/{clave}`)
- `ACTIVE` -> `NON_EXISTENT`
  - Trigger: eliminación (`DELETE /api/v1/empleados/{clave}`)

### Invalid Transitions
- `NON_EXISTENT` -> `ACTIVE` con colisión de `consecutivo`: rechazado (conflicto).
- `NON_EXISTENT` -> `NON_EXISTENT` en `PUT`/`DELETE`: rechazado (no encontrado).
