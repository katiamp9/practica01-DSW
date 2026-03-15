# Feature Specification: CRUD de Empleados

**Feature Branch**: `001-crud-empleados`  
**Created**: 2026-02-25  
**Status**: Draft  
**Input**: User description: "modifica la especificacion 001-crud-empleados donde el campo clave sea con un prefijo EMP- seguido de un auto numerico como PK compuesta"

## Clarifications

### Session 2026-03-05

- Q: ¿Qué política de paginación/ordenamiento aplica para GET de colecciones? → A: `size` por defecto 20 y máximo 100; `sort` permitido solo en `clave`, `nombre`, `direccion`, `telefono`.
- Q: ¿Qué código HTTP usar para parámetros de paginación/ordenamiento inválidos? → A: `422 Unprocessable Entity`.
- Q: ¿Cuáles parámetros de paginación son obligatorios? → A: `page` y `sort` obligatorios; `size` opcional con default `20`.
- Q: ¿Se mantienen rutas antiguas sin versión? → A: No; solo se soportan rutas versionadas `/api/v1/...`.

## User Scenarios & Testing *(mandatory)*

<!--
  IMPORTANT: User stories should be PRIORITIZED as user journeys ordered by importance.
  Each user story/journey must be INDEPENDENTLY TESTABLE - meaning if you implement just ONE of them,
  you should still have a viable MVP (Minimum Viable Product) that delivers value.
  
  Assign priorities (P1, P2, P3, etc.) to each story, where P1 is the most critical.
  Think of each story as a standalone slice of functionality that can be:
  - Developed independently
  - Tested independently
  - Deployed independently
  - Demonstrated to users independently
-->

### User Story 1 - Registrar empleados (Priority: P1)

Como usuario de administración, quiero registrar empleados con nombre,
dirección y teléfono para que el sistema asigne una clave compuesta válida y
mantener un padrón inicial confiable.

**Why this priority**: Sin alta de empleados no existe base de datos útil para
las demás operaciones del CRUD.

**Independent Test**: Se prueba creando un empleado válido y verificando que
queda registrado con clave compuesta `EMP-` + consecutivo numérico y datos
completos.

**Acceptance Scenarios**:

1. **Given** existe un consecutivo disponible, **When** se registra un empleado
  con nombre, dirección y teléfono válidos, **Then** el sistema confirma el
  alta, asigna una clave con formato `EMP-<numero>` y el empleado queda
  disponible para consulta.
2. **Given** el cliente intenta enviar una `clave` manual en el alta,
  **When** se procesa la solicitud, **Then** el sistema ignora o rechaza ese
  valor y mantiene la generación interna de clave compuesta.

---

### User Story 2 - Consultar empleados (Priority: P2)

Como usuario de administración, quiero listar y consultar empleados por clave
para localizar información de forma rápida y confiable.

**Why this priority**: Permite usar los datos creados y validar la consistencia
del registro de empleados.

**Independent Test**: Se prueba consultando la lista general y luego un empleado
específico por clave para confirmar exactitud de datos.

**Acceptance Scenarios**:

1. **Given** existen empleados registrados, **When** el usuario solicita el
  listado o una búsqueda por clave existente, **Then** el sistema devuelve la
  información correcta de los empleados solicitados.

---

### User Story 3 - Actualizar y eliminar empleados (Priority: P3)

Como usuario de administración, quiero actualizar nombre, dirección y teléfono,
y eliminar empleados cuando aplique, para mantener la información vigente.

**Why this priority**: Completa el ciclo de mantenimiento del catálogo sin
afectar la integridad de claves existentes.

**Independent Test**: Se prueba actualizando un empleado existente y luego
eliminándolo, verificando reflejo inmediato de cambios.

**Acceptance Scenarios**:

1. **Given** existe un empleado con clave válida, **When** se actualizan sus
  datos permitidos o se solicita su eliminación, **Then** el sistema aplica la
  operación y refleja el estado final esperado.

---

### Edge Cases

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right edge cases.
-->

- Intento de alta enviando `clave` manual en el payload.
- Intento de alta con nombre, dirección o teléfono superiores a 100 caracteres.
- Intento de actualización sobre un empleado inexistente.
- Intento de eliminar un empleado inexistente.
- Intento de colisión de clave por concurrencia en la generación autonumérica.
- Solicitud de listado con `size` mayor a 100.
- Solicitud de listado con `sort` en campos no permitidos.

## Requirements *(mandatory)*

<!--
  ACTION REQUIRED: The content in this section represents placeholders.
  Fill them out with the right functional requirements.
-->

### Functional Requirements

- **FR-001**: El sistema MUST permitir crear empleados con los campos `nombre`,
  `direccion` y `telefono`, generando internamente la `clave`.
- **FR-002**: El sistema MUST generar `clave` como identificador primario
  compuesto por prefijo fijo `EMP-` y un componente autonumérico consecutivo.
- **FR-003**: El sistema MUST garantizar que la `clave` compuesta sea única para
  cada empleado.
- **FR-004**: El sistema MUST validar que `nombre`, `direccion` y `telefono`
  tengan una longitud máxima de 100 caracteres cada uno.
- **FR-005**: El sistema MUST validar que `nombre`, `direccion` y `telefono`
  sean obligatorios y no vacíos.
- **FR-006**: El sistema MUST permitir consultar empleados con paginación, requiriendo `page` y `sort`, y aceptando `size` opcional.
- **FR-007**: El sistema MUST permitir consultar un empleado por su `clave`.
- **FR-008**: El sistema MUST permitir actualizar `nombre`, `direccion` y
  `telefono` de un empleado existente.
- **FR-009**: El sistema MUST no permitir actualizar manualmente la `clave` de
  un empleado existente.
- **FR-010**: El sistema MUST permitir eliminar un empleado por su `clave`.
- **FR-011**: El sistema MUST devolver un error claro cuando una operación se
  realice sobre una `clave` inexistente.
- **FR-012**: El sistema MUST devolver un error claro cuando los datos no
  cumplan validaciones de obligatoriedad o longitud.
- **FR-013**: En consultas de colecciones, el sistema MUST usar `size=20` por defecto cuando no se envíe explícitamente.
- **FR-014**: En consultas de colecciones, el sistema MUST rechazar valores de `size` mayores a 100.
- **FR-015**: En consultas de colecciones, el sistema MUST aceptar `sort` solo en `clave`, `nombre`, `direccion` y `telefono`.
- **FR-016**: En consultas de colecciones, el sistema MUST responder `422 Unprocessable Entity` cuando `page`, `size` o `sort` sean inválidos respecto a las reglas de paginación y ordenamiento.
- **FR-017**: El sistema MUST exponer únicamente endpoints versionados bajo el prefijo `/api/v1/...` para todas las operaciones del CRUD de empleados.

### Constitution Alignment *(mandatory)*

- **CA-001**: La feature MUST mantenerse compatible con la línea base
  constitucional del proyecto.
- **CA-002**: La feature MUST declarar impacto en endpoints protegidos y acceso
  autenticado.
- **CA-003**: La feature MUST declarar impacto sobre almacenamiento y estructura
  de datos persistidos.
- **CA-004**: La feature MUST mantener ejecución reproducible en entornos locales
  e integración.
- **CA-005**: La feature MUST actualizar la documentación contractual de la API
  para operaciones nuevas o modificadas.

### Assumptions

- La `clave` no se envía en la creación; se genera automáticamente con el
  formato `EMP-<numero>`.
- La parte autonumérica de la clave se incrementa por cada nuevo empleado y no
  se reutiliza tras eliminación.
- `nombre`, `direccion` y `telefono` son campos obligatorios para crear empleados.
- Las búsquedas por clave son exactas (sin búsqueda aproximada).

### Key Entities *(include if feature involves data)*

- **Empleado**: Representa a una persona registrada en el catálogo laboral.
  Atributos clave: `clave` (PK compuesta lógica: `prefijo` fijo `EMP-` +
  `consecutivo` autonumérico), `nombre`, `direccion`, `telefono`.

## Success Criteria *(mandatory)*

<!--
  ACTION REQUIRED: Define measurable success criteria.
  These must be technology-agnostic and measurable.
-->

### Measurable Outcomes

- **SC-001**: El 100% de altas con datos válidos se registran correctamente en un
  solo intento.
- **SC-002**: El 100% de altas válidas generan una `clave` con formato
  `EMP-<numero>` sin colisiones.
- **SC-003**: El 100% de intentos de alta con campos mayores a 100 caracteres son
  rechazados con mensaje de validación claro.
- **SC-004**: El 100% de consultas por `clave` existente devuelven el empleado
  correcto.
- **SC-005**: El 100% de actualizaciones y eliminaciones sobre `clave`
  inexistente retornan resultado de no encontrado, sin alterar otros registros.
- **SC-006**: El 100% de listados sin `size` explícito se procesan con tamaño por defecto de 20.
- **SC-007**: El 100% de listados con `size` mayor a 100 o `sort` fuera de campos permitidos son rechazados con estado `422`.
