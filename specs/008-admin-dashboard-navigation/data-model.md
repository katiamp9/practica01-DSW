# Data Model — Feature 008

## Entity: EmpleadoListado
- Description: Registro de empleado mostrado en la tabla del dashboard administrativo.
- Fields:
  - `clave` (string, required)
  - `nombre` (string, required)
  - `rol` (string, required)
  - `direccion` (string, optional for render extendido)
  - `telefono` (string, optional for render extendido)
  - `departamentoId` (number | null, optional)
- Validation Rules:
  - `clave` MUST mostrarse como identificador principal en la tabla.
  - `nombre` MUST mostrarse en cada fila.
  - `rol` MUST mapearse a etiqueta visual; si no coincide con roles conocidos, usar etiqueta fallback.
- State Transitions:
  - `unloaded` → `loaded` al completar consulta exitosa.
  - `loaded` → `loaded` al cambiar de página.

## Entity: PaginacionDashboard
- Description: Estado de paginación del listado en cliente.
- Fields:
  - `page` (number, required, inicial `0`)
  - `size` (number, required, inicial `10`)
  - `sort` (string, required, inicial `nombre,asc`)
  - `totalElements` (number, required)
  - `totalPages` (number, required)
  - `hasNext` (boolean, required)
  - `hasPrevious` (boolean, required)
- Validation Rules:
  - `page` MUST ser `>= 0`.
  - `size` MUST mantenerse en `10` para esta iteración.
  - `sort` MUST usar campos permitidos por backend (`clave`, `nombre`, `direccion`, `telefono`) con dirección `asc|desc`.
- State Transitions:
  - `initial` → `page-0-loaded` tras primera carga.
  - `page-n-loaded` ↔ `page-(n±1)-loaded` usando controles siguiente/anterior.

## Entity: ItemNavegacionAdmin
- Description: Elemento de navegación superior del módulo administrativo.
- Fields:
  - `label` (string, required; valores: `Inicio`, `Empleados`, `Departamentos`)
  - `route` (string, required)
  - `isActive` (boolean, derived)
- Validation Rules:
  - `Inicio` MUST resolver a dashboard admin principal.
  - `Empleados` MUST resolver a `/admin/empleados`.
  - `Departamentos` MUST resolver a `/admin/departamentos`.
- State Transitions:
  - `inactive` ↔ `active` según ruta actual.

## Entity: EstadoDashboardAdmin
- Description: Estado de presentación y carga de datos de la pantalla.
- Fields:
  - `status` (enum, required: `loading` | `data` | `empty` | `error`)
  - `message` (string, optional)
  - `lastRequestedPage` (number, optional)
- Validation Rules:
  - `error` MUST exponer mensaje claro y acción manual de reintento.
  - `empty` MUST mostrar estado sin datos sin romper navegación.
- State Transitions:
  - `loading` → `data` cuando llega contenido.
  - `loading` → `empty` cuando `content` está vacío.
  - `loading` → `error` ante fallo de red/backend.
  - `error` → `loading` al activar “Reintentar”.

## Contract Envelope: PageResponseEmpleado
- Description: Estructura esperada de respuesta paginada para render y controles.
- Fields:
  - `content` (array<EmpleadoListado>, required)
  - `number` (number, required)
  - `size` (number, required)
  - `totalElements` (number, required)
  - `totalPages` (number, required)
  - `first` (boolean, required)
  - `last` (boolean, required)
- Validation Rules:
  - `number` MUST sincronizar con `PaginacionDashboard.page`.
  - `size` SHOULD reflejar `10` en carga inicial de dashboard.
