# Contract: CRUD de Departamentos y Gestión de Áreas (Feature 010)

## Purpose

Definir contrato frontend-backend para listado, creación, edición y eliminación de departamentos, incluyendo conteo dinámico de personal y reglas de borrado protegido por integridad.

## Protected Access Contract

- Todas las operaciones de departamentos MUST ejecutarse como solicitudes protegidas.
- Frontend MUST consumir endpoints vía `HttpClient` para que el `AuthInterceptor` adjunte credenciales Basic Auth.

## List Contract

- Endpoint: `GET /api/v1/departamentos`
- Request MUST incluir `page`, `size`, `sort`.
- Response MUST ser paginada e incluir por fila:
  - `id`
  - `nombre`
  - `totalEmpleados`
- `totalEmpleados` MUST calcularse en backend con consulta agregada (`LEFT JOIN + COUNT(e.id)`) para incluir departamentos sin empleados con valor `0`.

## Create Contract

- Endpoint: `POST /api/v1/departamentos`
- Request payload:
  - `nombre` (required)
- Response:
  - departamento creado con `id` y `nombre`.

## Update Contract

- Endpoint: `PUT /api/v1/departamentos/{id}`
- Request payload:
  - `nombre` (required)
- Response:
  - departamento actualizado con `id` y `nombre`.

## Delete Contract

- Endpoint: `DELETE /api/v1/departamentos/{id}`
- Success response:
  - `204 No Content` si `totalEmpleados = 0`.
- Protected failure:
  - `409 Conflict` si el departamento tiene empleados asociados.
  - mensaje de negocio explícito de bloqueo por integridad.

## Error Mapping Contract (UI)

- Para `409 Conflict` en DELETE por integridad, frontend MUST mostrar toast:
  - `No se puede eliminar: existen empleados asociados.`.
- Para not found y validaciones, frontend MUST mostrar feedback contextual y mantener consistencia de estado.

## UI Behavior Contract

- Tabla MUST incluir columnas: `Nombre`, `Personal`, `Acciones`.
- Si `totalEmpleados > 0`, botón eliminar MUST mostrarse deshabilitado y con tooltip explicativo.
- Búsqueda por nombre MUST aplicarse localmente en cliente sin nuevas solicitudes.
- Si hay búsqueda activa con datos paginados, la UI MUST mostrar aviso de que el filtro aplica sobre la página cargada.
- Create/Edit MUST usar modal reutilizable (`DepartamentoForm`) sin recarga de página.
- Si el modal tiene cambios sin guardar, la UI MUST confirmar antes de descartar al cerrar/cancelar.

## Compatibility Guarantees

- Se mantiene prefijo versionado `/api/v1`.
- Se mantiene semántica paginada de GET de colecciones.
- La feature es compatible con contrato de seguridad vigente.
