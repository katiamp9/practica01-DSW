# Feature Specification: Validación de Paginación Real en BD

**Feature Branch**: `002-validacion-paginacion-db`  
**Created**: 2026-03-14  
**Status**: Draft  
**Input**: User description: "confirmar que paginación está conectada a la base de datos y no solo en Swagger"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Evidencia de paginación real en PostgreSQL (Priority: P1)

Como desarrolladora, quiero pruebas automatizadas contra PostgreSQL real para confirmar
que `page`, `size` y `sort` se traducen a consultas paginadas/ordenadas en base de datos.

**Why this priority**: Evita falsos positivos de contratos/documentación y valida comportamiento real de persistencia.

**Independent Test**: Con dataset conocido en PostgreSQL, al consultar `GET /api/v1/empleados?page=1&size=2&sort=nombre,asc`,
la respuesta contiene exactamente la página esperada y el orden correcto.

**Acceptance Scenarios**:

1. **Given** registros precargados en PostgreSQL, **When** se consulta una página válida,
   **Then** se devuelve el subconjunto correcto con metadatos (`number`, `size`, `totalElements`, `totalPages`) coherentes.
2. **Given** registros precargados en PostgreSQL, **When** se solicita ordenamiento por un campo permitido,
   **Then** la colección respeta el orden solicitado (`asc`/`desc`) en la base de datos.
3. **Given** parámetros inválidos de paginación/ordenamiento, **When** se consulta la colección,
   **Then** se mantiene respuesta `422` y no se ejecuta flujo exitoso de consulta.

---

### User Story 2 - Cobertura de integración por capa (Priority: P2)

Como equipo, queremos pruebas de integración en capa repositorio y API para detectar
regresiones donde `Pageable` deje de llegar a JPA/PostgreSQL.

**Why this priority**: Complementa pruebas de controlador con `@MockBean`, que hoy no validan SQL real.

**Independent Test**: Suite de integración verde con al menos un test de repositorio y un test de endpoint usando BD real.

**Acceptance Scenarios**:

1. **Given** `EmpleadoRepository.findAll(Pageable)`, **When** se ejecuta con `PageRequest`,
   **Then** el resultado respeta límite, desplazamiento y orden en PostgreSQL.
2. **Given** `GET /api/v1/empleados` con autenticación válida y parámetros válidos,
   **When** se ejecuta en test de integración con contexto completo,
   **Then** la respuesta refleja exactamente el comportamiento paginado persistido.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST conservar la paginación y ordenamiento actuales del endpoint `GET /api/v1/empleados`.
- **FR-002**: El proyecto MUST incluir pruebas de integración con PostgreSQL real para validar `Pageable` end-to-end.
- **FR-003**: Las pruebas MUST verificar metadatos de página y contenido retornado.
- **FR-004**: Las pruebas MUST cubrir al menos un caso de `sort=nombre,asc` y un caso `sort=nombre,desc`.
- **FR-005**: Las pruebas MUST confirmar que parámetros inválidos continúan retornando `422`.

### Constitution Alignment *(mandatory)*

- **CA-001**: Compatible con Spring Boot 3.x y Java 17.
- **CA-002**: Endpoints protegidos con Basic Auth en pruebas de integración HTTP.
- **CA-003**: Validación contra PostgreSQL real y migraciones activas (Flyway).
- **CA-004**: Ejecución reproducible local mediante Docker Compose para BD.
- **CA-005**: Si cambia contrato observable, OpenAPI MUST actualizarse; si no cambia, documentar “sin cambios de contrato”.
- **CA-006**: Rutas versionadas deben mantenerse bajo `/api/v1`.
- **CA-007**: GET de colección mantiene estándar `Pageable` (`page`, `size`, `sort`).

### Assumptions

- Ya existe `docker/compose/docker-compose.yml` funcional para PostgreSQL local.
- No se requiere modificar reglas de negocio de paginación, solo verificar su ejecución real.
- Se puede usar Testcontainers PostgreSQL o perfil de integración apuntando a contenedor local.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Existe al menos 1 prueba de integración de repositorio validando `Pageable` en PostgreSQL.
- **SC-002**: Existe al menos 1 prueba de integración HTTP validando paginación/ordenamiento con BD real.
- **SC-003**: `mvn test` ejecuta dichas pruebas y pasa en entorno local reproducible.
- **SC-004**: Se documenta evidencia de que la paginación no es solo visual en Swagger.
