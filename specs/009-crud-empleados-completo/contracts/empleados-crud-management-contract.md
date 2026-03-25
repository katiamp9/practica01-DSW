# Contract: CRUD de Empleados y Formulario Unificado (Feature 009)

## Purpose
Definir el contrato funcional frontend-backend para gestión completa de empleados (create, update, delete), carga de departamentos para select y reglas de estado/UX del formulario administrativo.

## Protected Access Contract
- La vista de gestión de empleados MUST ser accesible solo con sesión válida y rol administrativo.
- Todas las operaciones CRUD MUST ejecutarse como solicitudes protegidas usando el mecanismo actual de autenticación.
- Frontend MUST consumir endpoints vía `HttpClient` para que `AuthInterceptor` adjunte credenciales automáticamente.

## Employee List Contract
- Frontend MUST consultar listado de empleados en `GET /api/v1/empleados`.
- Request MUST incluir parámetros paginados requeridos por backend:
  - `page`
  - `size`
  - `sort`
- Backend MUST construir el listado con `LEFT JOIN` entre empleados y cuentas para no excluir empleados sin cuenta.
- Response esperada:
  - envelope paginado con `content`, `number`, `size`, `totalElements`, `totalPages`, `first`, `last`.
  - cada item de `content` MUST incluir el campo `email` (canónico), con valor `string` o `null`.
- Regla de presentación:
  - columna Departamento MUST mostrar nombre legible, no ID numérico.
  - nombre MUST resolverse desde catálogo de departamentos cargado reactivamente.
  - si no existe coincidencia por ID, UI MUST mostrar `Sin asignar`.
  - columna Email MUST mostrar `Sin correo` cuando `email` sea `null`.

## Form Mode Contract
- El componente `EmpleadoForm` MUST soportar modo `create` y modo `edit`.
- En ambos modos se reutiliza misma estructura de validación y feedback.
- En modo `edit`, `email` MUST cargarse desde datos del empleado y mostrarse como `readonly`.

## Department Select Contract
- Frontend MUST cargar departamentos al abrir formulario desde `GET /api/v1/departamentos`.
- Request MUST incluir parámetros requeridos por backend:
  - `page`
  - `size`
  - `sort`
- El campo Departamento MUST renderizar:
  - etiqueta visible: `nombre`
  - valor funcional: `id`
- En create/update, payload MUST enviar `departamentoId` y MUST NOT enviar nombre textual como identificador.

## Create Contract
- Endpoint: `POST /api/v1/empleados`
- Request payload esperado (mínimo relevante):
  - `nombre`
  - `direccion`
  - `telefono`
  - `departamentoId`
  - `email`
  - `password` (si aplica política de acceso)
- En modo create, frontend MUST NOT incluir `clave` en el payload.
- Response esperada:
  - `EmpleadoResponse` con datos normalizados del empleado creado.

## Update Contract
- Endpoint: `PUT /api/v1/empleados/{clave}`
- Request payload esperado (mínimo relevante):
  - `nombre`
  - `direccion`
  - `telefono`
  - `departamentoId`
  - `email`
  - `password` (si aplica actualización de acceso)
- Response esperada:
  - `EmpleadoResponse` actualizado.
- Regla de inmutabilidad:
  - si el `email` del request difiere del `email` persistido de la cuenta asociada, backend MUST rechazar con `400 Bad Request` y mensaje explícito.

## Delete Contract
- Endpoint: `DELETE /api/v1/empleados/{clave}`
- Response esperada:
  - `204 No Content`.
- Response excepcional:
  - `403 Forbidden` cuando el empleado objetivo corresponde al admin principal protegido (`admin@empresa.com`), con mensaje explícito de protección de cuenta.
- Regla de UX asociada:
  - delete MUST ejecutarse solo tras confirmación explícita en diálogo de confirmación.
  - UI MUST NOT mostrar acción de eliminar para el registro cuyo correo coincide con correo de sesión autenticada.
  - si por cualquier vía se invoca la acción sobre una fila no permitida, el flujo de confirmación MUST NOT abrirse.

## Validation Contract
- Form MUST bloquear guardado cuando:
  - `nombre` vacío.
  - `email` no corporativo.
  - `clave` duplicada (detectada por respuesta de conflicto backend en create).
- Errores de validación MUST mostrarse en contexto del campo o de la operación.

## UX Feedback Contract
- Operación create/update/delete exitosa MUST disparar toast de éxito.
- Al cancelar confirmación de eliminación MUST preservarse estado actual sin cambios de datos.
- Si el catálogo de departamentos está vacío, formulario MUST mostrar advertencia visible.

## Compatibility Guarantees
- Se mantiene prefijo versionado `/api/v1` en todas las rutas consumidas.
- No se requieren cambios de esquema de base de datos para esta iteración.
- El contrato preserva seguridad básica y políticas de paginación del backend existente.
- El contrato canónico para identidad de cuenta en administración usa el nombre de campo `email` (no `correo`).
