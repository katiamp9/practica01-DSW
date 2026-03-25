# Feature Specification: CRUD Completo de Departamentos (Gestión de Áreas)

**Feature Branch**: `010-crud-departamentos-areas`  
**Created**: 2026-03-24  
**Status**: Draft  
**Input**: User description: "Feature 010: CRUD Completo de Departamentos (Gestión de Áreas)"

## Clarifications

### Session 2026-03-24

- Q: ¿Qué código HTTP usar al bloquear DELETE de departamento con personal asociado (`totalEmpleados > 0`)? → A: `409 Conflict`.
- Q: ¿Dónde debe incluirse `totalEmpleados` en el contrato de departamentos? → A: Solo en `GET /api/v1/departamentos` (listado).
- Q: ¿Dónde registrar el incidente de creación de empleados (`DataIntegrityViolationException` por clave duplicada) y la estabilización de serialización de páginas? → A: En la spec existente de empleados 009, no en esta feature 010.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Gestión CRUD de departamentos (Priority: P1)

Como administrador, quiero crear, listar, editar y borrar departamentos desde una sola vista para mantener actualizado el catálogo de áreas sin fricción.

**Why this priority**: Es el flujo núcleo de negocio; sin CRUD completo no existe gestión operativa de áreas.

**Independent Test**: Se valida creando un departamento, editándolo y eliminándolo desde la misma pantalla, confirmando que la tabla refleja cada cambio en tiempo real.

**Acceptance Scenarios**:

1. **Given** un administrador autenticado en la vista de departamentos, **When** registra un nombre válido, **Then** el nuevo departamento aparece en la lista sin recargar la aplicación.
2. **Given** un departamento existente, **When** el administrador guarda una edición válida, **Then** la tabla muestra el nuevo nombre inmediatamente.
3. **Given** un departamento sin empleados asociados, **When** el administrador confirma borrar, **Then** el sistema elimina el registro y actualiza la lista.
4. **Given** una consulta de departamentos paginada, **When** el usuario navega entre páginas, **Then** mantiene consistencia de resultados y ordenamiento.
5. **Given** la tabla de departamentos cargada, **When** se renderiza cada fila, **Then** se muestra la columna `Personal` con el total de empleados asociado.

---

### User Story 2 - Flujo UX fluido con modal y búsqueda local (Priority: P1)

Como administrador, quiero editar y crear departamentos desde un modal reutilizable y buscar por nombre en tiempo real para operar más rápido y sin recargas de página.

**Why this priority**: Impacta directamente la productividad y reduce cambios de contexto en tareas frecuentes.

**Independent Test**: Se valida abriendo el modal en modo crear/editar y usando la barra de búsqueda para filtrar localmente la lista sin nuevas solicitudes al servidor.

**Acceptance Scenarios**:

1. **Given** la tabla de departamentos cargada, **When** el usuario escribe en la barra de búsqueda, **Then** la lista se filtra instantáneamente por coincidencia de nombre sin nueva llamada al backend.
2. **Given** el modal en modo creación, **When** el usuario cancela, **Then** no se modifica la lista ni se pierde el estado de búsqueda activo.
3. **Given** el modal en modo edición, **When** el usuario abre un departamento existente, **Then** ve los datos precargados y puede guardar cambios válidos.
4. **Given** el modal tiene cambios sin guardar, **When** el usuario intenta cerrar o cancelar, **Then** el sistema solicita confirmación antes de descartar.

---

### User Story 3 - Integridad y manejo de errores de borrado (Priority: P2)

Como administrador, quiero recibir mensajes claros cuando no se puede eliminar un departamento con empleados activos para entender el bloqueo y tomar acción.

**Why this priority**: Protege integridad de datos y evita operaciones destructivas inválidas.

**Independent Test**: Se valida intentando borrar un departamento con empleados asociados y confirmando mensaje informativo en toast.

**Acceptance Scenarios**:

1. **Given** un departamento con empleados asociados, **When** el administrador intenta eliminarlo, **Then** el backend rechaza la operación con conflicto y la UI muestra un toast informativo.
2. **Given** un departamento inexistente, **When** se intenta editar o borrar, **Then** el sistema informa claramente que el recurso no existe.
3. **Given** un departamento con `totalEmpleados > 0`, **When** el administrador visualiza la tabla, **Then** la acción eliminar aparece deshabilitada y con tooltip explicativo.

---

### Edge Cases

- Intento de crear departamento con nombre vacío o solo espacios.
- Intento de crear/editar departamento con nombre duplicado (ignorando mayúsculas/minúsculas).
- Intento de borrar departamento con empleados activos asociados.
- Intento de editar o borrar departamento inexistente por concurrencia.
- Resultado vacío en listado o en búsqueda local.
- Sesión expirada durante operaciones CRUD protegidas.
- Cierre del modal con cambios sin guardar por acción accidental del usuario.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST exponer una vista administrativa para listar departamentos con paginación y ordenamiento.
- **FR-002**: La operación de listado MUST consumir `GET /api/v1/departamentos` con parámetros `page`, `size` y `sort`.
- **FR-002A**: La respuesta de `GET /api/v1/departamentos` MUST incluir `totalEmpleados` por departamento para visualización del personal asociado.
- **FR-003**: El sistema MUST permitir crear departamentos mediante `POST /api/v1/departamentos`.
- **FR-004**: El sistema MUST permitir actualizar departamentos mediante `PUT /api/v1/departamentos/{id}`.
- **FR-005**: El sistema MUST permitir eliminar departamentos mediante `DELETE /api/v1/departamentos/{id}`.
- **FR-006**: El modelo funcional de departamento MUST usar los campos canónicos `id` y `nombre`.
- **FR-007**: La validación de negocio MUST impedir nombres vacíos y nombres duplicados de departamento.
- **FR-008**: El flujo de creación y edición MUST ejecutarse en un modal reutilizable sin recarga de página completa.
- **FR-009**: La vista MUST incluir una barra de búsqueda con filtrado local por `nombre` en tiempo real, sin peticiones adicionales al servidor.
- **FR-010**: El estado de lista, búsqueda, carga, error y éxito MUST gestionarse con Signals en frontend.
- **FR-011**: Toda solicitud de la feature MUST pasar por el mecanismo de autenticación existente para incluir credenciales en peticiones protegidas.
- **FR-012**: Si la respuesta de borrado retorna conflicto por integridad, la UI MUST mostrar el toast canónico: "No se puede eliminar: existen empleados asociados.".
- **FR-013**: El backend MUST tratar el caso de departamento en uso como conflicto de integridad y devolver respuesta de conflicto para esa operación.
- **FR-014**: El sistema MUST mostrar notificaciones tipo toast para operaciones exitosas y errores de negocio.
- **FR-015**: La tabla MUST incluir acciones de editar y borrar por fila.
- **FR-016**: Si `totalEmpleados > 0`, la UI MUST deshabilitar visualmente el botón de eliminar y mostrar un tooltip explicativo.
- **FR-017**: El modal de creación/edición MUST pedir confirmación antes de cerrar cuando existan cambios sin guardar.
- **FR-018**: Cuando exista texto de búsqueda activo sobre una lista paginada, la UI MUST mostrar una nota visual indicando que el filtro aplica únicamente sobre la página cargada.

### API Contract (Feature Scope)

- **GET** `/api/v1/departamentos`: lista paginada de departamentos con `id`, `nombre` y `totalEmpleados`.
- **POST** `/api/v1/departamentos`: crea un departamento con `nombre`.
- **PUT** `/api/v1/departamentos/{id}`: actualiza `nombre` del departamento.
- **DELETE** `/api/v1/departamentos/{id}`: elimina departamento si no tiene empleados asociados; si tiene personal asociado MUST responder `409 Conflict`.

### Error Handling Contract

- Recurso no encontrado MUST devolverse como error de no encontrado.
- Validaciones de entrada y reglas de negocio (por ejemplo, nombre inválido/duplicado) MUST devolverse como error de solicitud inválida.
- Departamento en uso (con empleados asociados) MUST devolverse como `409 Conflict`.
- La UI MUST mapear explícitamente ese `409 Conflict` de borrado a un toast informativo de integridad.

### Key Entities *(include if feature involves data)*

- **DepartamentoGestion**: Entidad administrable de área, con `id` y `nombre` como atributos principales.
- **DepartamentoListaDto**: DTO de salida del listado con `id`, `nombre` y `totalEmpleados` calculado en backend.
- **DepartamentoFormState**: Estado del modal reutilizable (modo crear/editar, validación, envío, errores).
- **DepartamentoListState**: Estado reactivo de tabla (datos, carga, error, búsqueda local, paginación).
- **DepartamentoOperationResult**: Resultado de operación CRUD para feedback de usuario (éxito/error, mensaje).

## Assumptions

- El nombre de campo canónico en backend y contrato JSON para departamentos es `nombre`.
- El endpoint de listado de departamentos incluirá `totalEmpleados` para soportar columna `Personal` y reglas visuales de borrado protegido.
- El backend mantiene restricción de integridad para evitar borrado de departamentos con empleados asociados.
- Los ajustes de creación de empleados (generación de `clave`, manejo de colisiones y configuración global de serialización de `Page`) quedan fuera del alcance funcional de la feature 010 y se documentan en la spec 009 de empleados.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 95% de operaciones de crear o editar departamento se completa en menos de 90 segundos desde la apertura del modal.
- **SC-002**: El 95% de cargas exitosas del listado de departamentos se muestra en menos de 2 segundos en entorno operativo normal.
- **SC-003**: El 100% de intentos de eliminar departamentos con empleados activos muestra feedback informativo claro al usuario.
- **SC-004**: El 100% de operaciones CRUD exitosas muestra confirmación visual de éxito.
- **SC-005**: Al menos 90% de usuarios administrativos encuentra un departamento por nombre en el primer intento usando la búsqueda local.
