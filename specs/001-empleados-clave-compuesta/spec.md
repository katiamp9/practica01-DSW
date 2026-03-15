# Feature Specification: Clave Compuesta Empleados

**Feature Branch**: `001-empleados-clave-compuesta`  
**Created**: 2026-03-10  
**Status**: Draft  
**Input**: User description: "modifica la especificacion de empleados para que clave use prefijo EMP- seguido de autonumerico como PK compuesta"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Alta con clave automática (Priority: P1)

Como usuario administrativo, quiero registrar empleados sin capturar manualmente
la clave para que el sistema genere un identificador estándar y evite errores
de captura.

**Why this priority**: Es el cambio central del feature y habilita el patrón de
identificación para el resto de operaciones.

**Independent Test**: Se registra un empleado válido y se verifica que el
resultado incluya una clave con formato `EMP-<número>`, sin depender de otras
historias.

**Acceptance Scenarios**:

1. **Given** un formulario de alta con datos válidos, **When** se confirma el
   registro, **Then** el sistema crea el empleado y asigna una clave con prefijo
   `EMP-` y componente numérico incremental.
2. **Given** un intento de alta que incluye clave manual, **When** se procesa la
   solicitud, **Then** el sistema rechaza la captura manual y mantiene la
   generación interna de clave.

---

### User Story 2 - Consulta consistente por clave y listado (Priority: P2)

Como usuario administrativo, quiero consultar empleados por clave y en listados
ordenables para localizar registros de forma confiable después del cambio de
identificador.

**Why this priority**: Asegura continuidad operativa del catálogo con la nueva
estructura de clave.

**Independent Test**: Se consultan empleados por su clave generada y mediante
listado con paginación/ordenamiento para validar resultados correctos.

**Acceptance Scenarios**:

1. **Given** empleados existentes con clave compuesta, **When** se consulta una
   clave existente, **Then** se devuelve el empleado correcto.
2. **Given** una consulta de listado con parámetros válidos, **When** se obtiene
   la colección, **Then** la respuesta respeta paginación y ordenamiento
   definidos.

---

### User Story 3 - Mantenimiento sin alterar identificador (Priority: P3)

Como usuario administrativo, quiero actualizar y eliminar empleados sin modificar
su clave para mantener la integridad del identificador a lo largo del ciclo de
vida del registro.

**Why this priority**: Completa el CRUD y protege la inmutabilidad del
identificador de negocio.

**Independent Test**: Se actualiza y elimina un empleado existente, verificando
que la clave permanezca inmutable durante actualización y que los casos
inexistentes reporten no encontrado.

**Acceptance Scenarios**:

1. **Given** un empleado existente, **When** se actualizan datos permitidos,
   **Then** la operación se aplica sin cambiar la clave.
2. **Given** una clave inexistente, **When** se intenta actualizar o eliminar,
   **Then** el sistema informa que el registro no existe.

### Edge Cases

- Alta con campos obligatorios vacíos o solo espacios.
- Alta con longitud mayor al máximo permitido en campos de texto.
- Solicitud concurrente de altas que compiten por el siguiente consecutivo.
- Consulta con parámetros de paginación inválidos o fuera de límites.
- Solicitud sobre clave con formato inválido o inexistente.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST generar la clave del empleado de forma interna con
  formato `EMP-<consecutivo>`.
- **FR-002**: El sistema MUST garantizar unicidad de cada clave generada.
- **FR-003**: El sistema MUST tratar la clave como identificador inmutable una
  vez creado el registro.
- **FR-004**: El sistema MUST rechazar intentos de alta que incluyan clave
  capturada manualmente.
- **FR-005**: El sistema MUST permitir crear empleados con nombre, dirección y
  teléfono válidos.
- **FR-006**: El sistema MUST validar obligatoriedad y longitud máxima de los
  campos de entrada del empleado.
- **FR-007**: El sistema MUST permitir consulta individual por clave generada.
- **FR-008**: El sistema MUST permitir listado paginado y ordenado de empleados.
- **FR-009**: En listados, el sistema MUST requerir `page` y `sort` y aceptar
  `size` opcional con valor por defecto 20 y máximo 100.
- **FR-010**: En listados, el sistema MUST aceptar ordenamiento solo en
  `clave`, `nombre`, `direccion` y `telefono`.
- **FR-011**: El sistema MUST responder con estado 422 cuando los parámetros de
  paginación u ordenamiento no cumplan reglas.
- **FR-012**: El sistema MUST permitir actualizar datos permitidos sin alterar
  la clave.
- **FR-013**: El sistema MUST permitir eliminar registros por clave existente.
- **FR-014**: El sistema MUST responder con estado de no encontrado cuando la
  clave consultada/actualizada/eliminada no exista.
- **FR-015**: El sistema MUST exponer rutas bajo el prefijo de versión vigente
  `/api/v1` para operaciones de empleados.

### Constitution Alignment *(mandatory)*

- **CA-001**: Feature MUST remain compatible with Spring Boot 3.x and Java 17.
- **CA-002**: Feature MUST define Basic Auth impact (new protected/public endpoints,
  roles/rules, credential handling).
- **CA-003**: Feature MUST define PostgreSQL data impact and required migrations.
- **CA-004**: Feature MUST define Docker impact for local/integration reproducibility.
- **CA-005**: Feature MUST define OpenAPI/Swagger updates for API contract changes.
- **CA-006**: Feature MUST define API route versioning with URL prefix `/api/v{n}`;
  current baseline MUST be `/api/v1`.
- **CA-007**: Every GET endpoint returning collections MUST support Spring Data JPA
  `Pageable` parameters: `page`, `size`, and `sort`.

### Assumptions

- El consecutivo inicia desde un valor inicial definido por el sistema y siempre
  avanza sin reutilización.
- El prefijo de clave para este dominio se mantiene fijo en `EMP-`.
- No se requiere búsqueda difusa por clave; la consulta es exacta.
- Las reglas de paginación y ordenamiento aplican a todos los GET de
  colecciones de empleados.

### Key Entities *(include if feature involves data)*

- **Empleado**: Registro administrativo del catálogo, identificado por clave
  compuesta lógica (`prefijo` + `consecutivo`) y atributos de contacto.
- **ClaveEmpleado**: Identificador de negocio formado por prefijo fijo `EMP-` y
  consecutivo numérico único.
- **ConsultaListadoEmpleados**: Parámetros de consulta para colección (`page`,
  `size`, `sort`) con reglas de validación de límites y campos permitidos.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de altas válidas generan clave con formato `EMP-<número>`.
- **SC-002**: El 100% de altas que intentan enviar clave manual son rechazadas.
- **SC-003**: El 100% de consultas por clave existente recuperan el registro
  correcto.
- **SC-004**: El 100% de solicitudes con parámetros inválidos de paginación o
  ordenamiento son rechazadas con estado 422.
- **SC-005**: El 100% de actualizaciones exitosas conservan la misma clave del
  registro.
- **SC-006**: El 100% de operaciones sobre claves inexistentes devuelven estado
  de no encontrado.
