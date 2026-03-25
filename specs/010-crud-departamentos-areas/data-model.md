# Data Model — Feature 010

## Entity: DepartamentoGestion

- Description: Registro administrable del catálogo de áreas.
- Fields:
  - `id` (number, required)
  - `nombre` (string, required)
- Validation Rules:
  - `nombre` MUST ser no vacío.
  - `nombre` MUST ser único (case-insensitive en regla de negocio).
- State Transitions:
  - `draft` → `created`
  - `created|updated` → `updated`
  - `created|updated` → `deleted` (solo si no hay empleados asociados)

## Entity: DepartamentoListaDto

- Description: DTO de salida para listado paginado de departamentos con información operativa de personal.
- Fields:
  - `id` (number, required)
  - `nombre` (string, required)
  - `totalEmpleados` (number, required, `>= 0`)
- Validation Rules:
  - `totalEmpleados` MUST calcularse en backend (consulta agregada).
  - `totalEmpleados` MUST estar presente en cada fila del listado.

## Entity: DepartamentoFormState

- Description: Estado reactivo del modal reutilizable de creación/edición.
- Fields:
  - `mode` (`create` | `edit`)
  - `isOpen` (boolean)
  - `isSubmitting` (boolean)
  - `nombre` (string)
  - `errors` (map<string,string>)
- Validation Rules:
  - en `create`, formulario inicia limpio.
  - en `edit`, `nombre` MUST precargarse.

## Entity: DepartamentoListState

- Description: Estado reactivo de tabla y filtros en la pantalla administrativa.
- Fields:
  - `status` (`loading` | `data` | `empty` | `error`)
  - `items` (array<DepartamentoListaDto>)
  - `searchTerm` (string)
  - `filteredItems` (array<DepartamentoListaDto>, derived)
  - `pagination` (`page`, `size`, `sort`, `totalElements`, `totalPages`)
- Validation Rules:
  - `filteredItems` MUST aplicarse localmente sobre `items`.
  - `status` MUST reflejar transiciones de carga y error.

## Entity: DeletePolicy

- Description: Regla operativa para acción de borrado por fila.
- Fields:
  - `canDelete` (boolean, derived from `totalEmpleados`)
  - `disableReason` (string, optional)
- Validation Rules:
  - si `totalEmpleados > 0`, `canDelete=false`.
  - si `canDelete=false`, la UI MUST mostrar tooltip explicativo.

## Relationships

- `DepartamentoGestion (1) — (N) Empleado`: la cardinalidad se refleja en `totalEmpleados` para operaciones de delete.
