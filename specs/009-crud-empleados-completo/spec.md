# Feature Specification: CRUD Completo de Empleados

**Feature Branch**: `009-crud-empleados-completo`  
**Created**: 2026-03-21  
**Status**: Draft  
**Input**: User description: "Feature 009: CRUD Completo de Empleados"

## Clarifications

### Session 2026-03-21

- Q: ¿Cómo debe capturarse el departamento en el formulario? → A: Como Combo Box (Select).
- Q: ¿Desde dónde se obtiene la lista de departamentos? → A: Desde backend en `GET /api/v1/departamentos`.
- Q: ¿Qué valor se muestra y cuál se envía en payload? → A: Se muestra nombre de departamento y se envía el ID seleccionado.
- Q: ¿Cuándo debe cargarse la lista de departamentos en UI? → A: Al abrir el formulario usando Signals.
- Q: ¿Qué ocurre si no existen departamentos creados? → A: El formulario muestra mensaje de advertencia.
- Q: ¿Debe mostrarse y enviarse la clave en creación de empleado? → A: No; en modo create el campo `clave` no se muestra y el POST no incluye `clave`.
- Q: ¿Cómo se identifica al “admin principal” para bloqueo defensivo de eliminación? → A: Por correo exacto `admin@empresa.com`.
- Q: ¿Qué código HTTP debe devolver backend al bloquear eliminación del admin principal? → A: `403 Forbidden` con mensaje de protección de cuenta.
- Q: ¿Qué estrategia de join debe usarse para poblar `email` al listar empleados? → A: `LEFT JOIN`; listar todos los empleados y devolver `email = null` cuando no exista cuenta asociada.
- Q: ¿Cómo debe comportarse el campo `email` en formulario de edición? → A: Debe mostrarse precargado y en modo `readonly`.
- Q: ¿Qué debe mostrarse en la tabla cuando `email` sea `null`? → A: Texto `Sin correo`.
- Q: ¿Cuál debe ser el nombre canónico del campo en el contrato JSON para cuenta de empleado? → A: `email`.
- Q: Si en edición llega un cambio de `email`, ¿cómo debe responder backend? → A: Rechazar la actualización con `400 Bad Request`.
- Q: ¿Cómo se genera `clave` cuando el frontend no la envía en create? → A: El backend genera `clave` con formato `EMP-` + secuencia numérica única y maneja colisiones con reintento controlado y error de conflicto si persiste.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Gestión integral de empleados (Priority: P1)

Como administrador, quiero una vista profesional para crear, consultar, editar y eliminar empleados para mantener actualizado el padrón sin salir del módulo de administración.

**Why this priority**: Es el núcleo funcional del módulo; sin CRUD completo la administración de empleados queda incompleta.

**Independent Test**: Puede validarse creando un empleado, editándolo y eliminándolo desde la misma vista, confirmando que la lista refleja cada cambio.

**Acceptance Scenarios**:

1. **Given** un administrador autenticado en la gestión de empleados, **When** crea un empleado válido, **Then** el nuevo registro aparece en la lista sin recargar toda la aplicación.
2. **Given** un empleado existente, **When** el administrador lo edita y guarda cambios válidos, **Then** la lista muestra los datos actualizados.
3. **Given** un empleado existente, **When** el administrador confirma la eliminación, **Then** el registro se elimina y la lista se actualiza correctamente.

---

### User Story 2 - Formulario unificado con validaciones (Priority: P1)

Como administrador, quiero usar un único formulario para crear y editar empleados con validaciones claras para evitar errores de captura.

**Why this priority**: La calidad de datos depende del formulario; errores en alta/edición afectan integridad operativa.

**Independent Test**: Puede validarse usando el mismo formulario en modo creación y edición, probando campos válidos e inválidos.

**Acceptance Scenarios**:

1. **Given** el formulario en modo creación, **When** el administrador ingresa email corporativo válido y nombre obligatorio, **Then** el sistema permite guardar y el backend asigna `clave` automáticamente.
2. **Given** el formulario en modo creación o edición, **When** el administrador ingresa email no corporativo o nombre vacío, **Then** el sistema bloquea guardado y muestra validación contextual.
3. **Given** el formulario en modo creación, **When** ocurre una colisión en la `clave` autogenerada por backend, **Then** el sistema rechaza la operación e informa conflicto de clave.
4. **Given** el formulario de alta o edición abierto, **When** se cargan departamentos desde backend, **Then** el campo Departamento se presenta como select mostrando nombre y almacenando ID.
5. **Given** el formulario de alta o edición abierto, **When** no existen departamentos disponibles, **Then** se muestra una advertencia visible y se evita envío inválido del formulario.

---

### User Story 3 - Confirmación y feedback de operaciones (Priority: P2)

Como administrador, quiero confirmación antes de eliminar y notificaciones de éxito para operar con seguridad y confianza.

**Why this priority**: Reduce eliminaciones accidentales y mejora percepción de control del usuario.

**Independent Test**: Puede validarse eliminando un empleado con confirmación y ejecutando operaciones exitosas para comprobar notificaciones visibles.

**Acceptance Scenarios**:

1. **Given** una acción de eliminación iniciada, **When** el administrador recibe el diálogo de confirmación y cancela, **Then** no se elimina ningún registro.
2. **Given** una operación de crear, editar o eliminar exitosa, **When** finaliza la operación, **Then** se muestra una notificación tipo toast de éxito.
3. **Given** una operación protegida de empleados, **When** se envía la solicitud al backend, **Then** se respeta el mecanismo de autenticación vigente de solicitudes protegidas.

---

### Edge Cases

- Intento de crear empleado con clave duplicada detectada durante guardado.
- Intento de editar empleado cuya clave quedó ocupada por otra operación concurrente.
- Cancelación explícita del diálogo de eliminación.
- Error de red en alta, edición o eliminación con preservación del estado de formulario.
- Sesión expirada o sin permisos durante operación CRUD protegida.
- Lista vacía tras eliminación del último empleado de la página actual.
- Respuesta de `GET /api/v1/departamentos` sin elementos disponibles para poblar el select.
- Falla temporal al cargar departamentos al abrir formulario.
- Intento de eliminar al admin principal identificado por correo `admin@empresa.com`.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST reemplazar el placeholder de empleados por una vista de gestión profesional con acciones de crear, editar y eliminar.
- **FR-002**: El sistema MUST ofrecer un formulario único de empleado reutilizable para alta y edición.
- **FR-003**: El formulario MUST validar que el nombre sea obligatorio antes de permitir guardar.
- **FR-004**: El formulario MUST validar que el correo cumpla formato de correo corporativo definido por la organización.
- **FR-005**: El backend MUST garantizar unicidad de `clave` de empleado generada automáticamente y bloquear guardado en caso de duplicidad no recuperable.
- **FR-006**: El sistema MUST exponer operaciones de crear, actualizar y eliminar empleados desde el servicio de datos de empleados.
- **FR-007**: La vista de gestión MUST mantener estado reactivo de lista y formulario para reflejar cambios sin recarga completa.
- **FR-008**: Antes de eliminar, el sistema MUST mostrar un diálogo de confirmación explícita con opción de cancelar.
- **FR-009**: Tras una operación exitosa de crear, editar o eliminar, el sistema MUST mostrar notificación tipo toast de éxito.
- **FR-010**: Toda operación CRUD de empleados MUST usar el mecanismo vigente de autenticación de solicitudes protegidas.
- **FR-011**: La lista de empleados MUST actualizarse en pantalla inmediatamente después de operaciones exitosas de crear, editar o eliminar.
- **FR-012**: El formulario MUST incluir el campo Departamento como control tipo select (Combo Box).
- **FR-013**: La lista de departamentos del select MUST cargarse desde `GET /api/v1/departamentos` al abrir el formulario.
- **FR-014**: El select de Departamento MUST mostrar el nombre del departamento y al guardar MUST enviar el ID seleccionado en el payload.
- **FR-015**: La carga de departamentos para el formulario MUST gestionarse con Signals en estado reactivo.
- **FR-016**: Si no hay departamentos disponibles, el formulario MUST mostrar advertencia visible al usuario.
- **FR-017**: En modo creación, el formulario MUST NOT mostrar el campo `clave` y la solicitud `POST /api/v1/empleados` MUST NOT incluir `clave` en el payload.
- **FR-018**: La tabla de empleados MUST mostrar el nombre del departamento (no el ID), resolviendo `departamentoId` contra el catálogo cargado reactivo y usando `Sin asignar` cuando no exista coincidencia.
- **FR-019**: La tabla de empleados MUST actualizar la representación del nombre de departamento automáticamente cuando finalice la carga del catálogo de departamentos desde backend.
- **FR-020**: En la vista de administración, el sistema MUST NOT mostrar la acción de eliminar para el empleado cuyo correo coincida con la sesión autenticada.
- **FR-021**: El backend MUST rechazar la eliminación del admin principal identificado por correo exacto `admin@empresa.com`.
- **FR-022**: Cuando se bloquee la eliminación del admin principal, el backend MUST responder con `403 Forbidden` y mensaje explícito de protección de cuenta.
- **FR-023**: En el listado de empleados, backend MUST obtener `email` mediante `LEFT JOIN` entre empleados y cuentas, preservando empleados sin cuenta asociada.
- **FR-024**: El DTO de respuesta de listado de empleados MUST incluir el campo `email`.
- **FR-025**: Cuando un empleado no tenga cuenta asociada, el backend MUST devolver `email = null` (no omitir el empleado del listado).
- **FR-026**: Al abrir el formulario en modo edición, el sistema MUST precargar el valor de `email` correspondiente al empleado.
- **FR-027**: En modo edición, el campo `email` MUST mostrarse como `readonly` (visible y no editable).
- **FR-028**: En la tabla de gestión, cuando `email` sea `null`, la columna Email MUST mostrar el texto `Sin correo`.
- **FR-029**: El contrato JSON de backend para gestión de empleados MUST usar `email` como nombre canónico del campo en respuestas y payloads relevantes de la vista administrativa.
- **FR-030**: En operaciones de edición de empleado, el backend MUST rechazar solicitudes que intenten modificar `email` y responder `400 Bad Request` con mensaje explícito de campo inmutable.
- **FR-031**: En `POST /api/v1/empleados`, cuando no se envíe `clave`, el backend MUST generar `clave` con formato `EMP-` + secuencia numérica única.
- **FR-032**: Si ocurre colisión de `clave` durante la generación automática y no se resuelve en el reintento controlado, el backend MUST responder `409 Conflict` con mensaje explícito.

### Key Entities *(include if feature involves data)*

- **EmpleadoGestion**: Registro administrable de empleado con clave única, nombre y correo corporativo, junto con datos auxiliares de presentación.
- **EstadoFormularioEmpleado**: Estado reactivo del formulario en modo crear o editar, incluyendo validez, valores actuales y mensajes de error.
- **DepartamentoOption**: Opción de departamento en formulario con `id` como valor funcional y `nombre` como etiqueta visible en el select.
- **OperacionCrudEmpleado**: Acción de negocio de alta, edición o eliminación con resultado (éxito/error), mensaje de feedback y actualización de lista.
- **ConfirmacionEliminacion**: Estado de diálogo para confirmar o cancelar eliminación de un empleado específico.

## Dependencies

- Servicio backend de empleados con operaciones de consulta, alta, actualización y eliminación accesible para rol administrativo.
- Endpoint de departamentos disponible para consulta de catálogo en `GET /api/v1/departamentos`.
- Sesión administrativa válida y autorización vigente para rutas/solicitudes protegidas.
- Reglas organizacionales para validación de correo corporativo.

## Assumptions

- El dominio corporativo aceptado está definido en la configuración de negocio y es estable para esta iteración.
- El backend devuelve error de conflicto claro cuando una clave de empleado ya existe.
- El flujo de autenticación actual ya adjunta credenciales automáticamente en solicitudes protegidas.
- El diseño visual de confirmación y toast se integra en la misma vista sin navegación adicional.
- El endpoint de departamentos retorna datos con identificador y nombre suficientes para poblar el select.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 95% de operaciones de alta y edición válidas finaliza en menos de 2 minutos desde que el usuario abre el formulario hasta que confirma guardado.
- **SC-002**: El 95% de cargas de la lista de empleados completa en menos de 2 segundos en entorno operativo normal.
- **SC-003**: El 100% de intentos de eliminación muestra confirmación explícita antes de ejecutar borrado.
- **SC-004**: El 100% de operaciones exitosas de crear, editar y eliminar muestra notificación visible de éxito.
- **SC-005**: Al menos 90% de usuarios administrativos de validación interna completa un flujo CRUD completo en su primer intento sin asistencia.
- **SC-006**: El 100% de envíos válidos desde formulario incluye `departamentoId` y no el nombre textual del departamento.
- **SC-007**: El 100% de aperturas de formulario carga el catálogo de departamentos en estado reactivo; si no hay elementos, se muestra advertencia visible.
