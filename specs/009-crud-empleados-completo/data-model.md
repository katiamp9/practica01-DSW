# Data Model — Feature 009

## Entity: EmpleadoGestion
- Description: Registro administrable de empleado en la vista CRUD.
- Fields:
  - `clave` (string, PK lógica; persistido; no visible en create)
  - `nombre` (string, required)
  - `direccion` (string, optional)
  - `telefono` (string, optional)
  - `departamentoId` (number, required)
  - `email` (string | null, derivado por join con cuenta)
- Validation Rules:
  - `clave` MUST ser única.
  - `nombre` MUST ser no vacío.
  - `departamentoId` MUST referenciar departamento válido.
  - `email` MAY ser `null` si no existe cuenta asociada.
- State Transitions:
  - `draft` → `created` (alta)
  - `created|updated` → `updated` (edición)
  - `created|updated` → `deleted` (eliminación confirmada)

## Entity: CuentaEmpleadoIdentity
- Description: Identidad de acceso asociada opcionalmente a un empleado.
- Fields:
  - `id` (UUID, required)
  - `empleadoClave` (string, required, unique)
  - `email` (string, required, unique)
  - `passwordHash` (string, required)
- Validation Rules:
  - `email` es identificador de cuenta y MUST ser inmutable desde edición de empleado.
  - Para listados administrativos, su resolución hacia `EmpleadoGestion` se realiza por `LEFT JOIN`.
- State Transitions:
  - `linked` → `removed` al eliminar cuenta asociada.

## Entity: EmpleadoListadoItem
- Description: DTO de respuesta de listado en backend para la tabla administrativa.
- Fields:
  - `clave` (string)
  - `nombre` (string)
  - `direccion` (string | null)
  - `telefono` (string | null)
  - `departamentoId` (number | null)
  - `email` (string | null)
- Validation Rules:
  - El campo `email` MUST existir en el DTO (aunque sea `null`).
  - El empleado MUST aparecer incluso si `email = null`.

## Entity: DepartamentoOption
- Description: Opción de catálogo para combo de departamento.
- Fields:
  - `id` (number, required)
  - `nombre` (string, required)
- Validation Rules:
  - UI muestra `nombre` y envía `departamentoId=id` en create/update.
- State Transitions:
  - `unloaded` → `loaded|empty|error`

## Entity: EstadoFormularioEmpleado
- Description: Estado reactivo del formulario create/edit.
- Fields:
  - `mode` (`create` | `edit`)
  - `isSubmitting` (boolean)
  - `errors` (map<string,string>)
  - `emailControlState` (`editable` en create, `readonly` en edit)
- Validation Rules:
  - En `create`: `clave` MUST NOT mostrarse ni enviarse.
  - En `edit`: `email` MUST precargarse y MUST mostrarse `readonly`.
  - Si request de update cambia `email`, backend MUST rechazar con `400`.

## Entity: EmailDisplayPolicy
- Description: Política de render para columna Email en tabla.
- Fields:
  - `emailRaw` (string | null)
  - `emailDisplay` (string, derived)
- Validation Rules:
  - Si `emailRaw != null`: `emailDisplay=emailRaw`.
  - Si `emailRaw == null`: `emailDisplay='Sin correo'`.

## Entity: ConfirmacionEliminacion
- Description: Estado del diálogo de confirmación previo al borrado.
- Fields:
  - `isOpen` (boolean)
  - `empleadoClave` (string | null)
  - `empleadoNombre` (string | null)
- Validation Rules:
  - Requiere confirmación positiva para ejecutar delete.
  - UI no ofrece delete para fila cuyo `email` coincide con sesión.

## Relationships
- `EmpleadoGestion (1) — (0..1) CuentaEmpleadoIdentity` por `empleado.clave = cuenta.empleado_clave`.
- `EmpleadoGestion (N) — (1) DepartamentoOption` por `departamentoId`.
