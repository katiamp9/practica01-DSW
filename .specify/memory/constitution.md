<!--
Sync Impact Report
- Version change: 1.1.0 → 1.2.0
- Modified principles:
	- IV. Contrato API Primero con Swagger/OpenAPI → IV. Contrato API Versionado y Documentado
	- V. Calidad Verificable y Compatibilidad Evolutiva (actualizada para Cypress obligatorio)
- Added sections:
	- Ninguna
- Removed sections:
	- Ninguna
- Templates requiring updates:
	- ✅ .specify/templates/plan-template.md
	- ✅ .specify/templates/spec-template.md
	- ✅ .specify/templates/tasks-template.md
	- ✅ specs/archive-001-crud-empleados/quickstart.md
	- ✅ .specify/templates/commands/*.md (N/A: directorio no existe)
- Follow-up TODOs:
	- Ninguno
-->

# Spring Backend Constitution

## Core Principles

### I. Stack y Versiones Obligatorias
Todo desarrollo del backend MUST usar Spring Boot 3.x sobre Java 17. El código fuente,
dependencias, plugins de build y despliegue MUST ser compatibles con esta base y no
pueden introducir runtime alternativos sin enmienda explícita a esta constitución.
Rationale: un stack único reduce riesgo operativo, simplifica soporte y acelera
onboarding.

### II. Seguridad por Defecto con Basic Auth
Todos los endpoints HTTP expuestos por la aplicación MUST estar protegidos con
Spring Security usando autenticación básica, excepto health checks o endpoints
marcados explícitamente como públicos en la especificación. Las credenciales MUST
gestionarse por variables de entorno o secretos; el repositorio MUST NOT contener
credenciales en texto plano.
Rationale: seguridad mínima consistente desde el primer despliegue.

### III. Persistencia PostgreSQL y Entorno Docker
La persistencia principal MUST ser PostgreSQL. El entorno local y de integración MUST
ser reproducible con Docker Compose incluyendo, como mínimo, el servicio de base de
datos y su configuración de red/puertos. Los cambios de esquema MUST gestionarse con
migraciones versionadas (Flyway o Liquibase) y nunca manualmente.
Rationale: consistencia entre entornos y trazabilidad de cambios de datos.

### IV. Contrato API Versionado y Documentado
Toda API REST MUST exponerse con versionamiento por prefijo en la URL usando el
patrón `/api/v{n}`. La versión vigente del sistema MUST ser `v1`, por lo que los
endpoints nuevos o modificados MUST publicarse bajo `/api/v1/...` salvo enmienda
explícita de esta constitución. Toda API REST MUST documentarse mediante OpenAPI
(Swagger UI habilitado en entorno de desarrollo) incluyendo descripción,
request/response, códigos de estado, versión de ruta y requisitos de autenticación.
Rationale: el versionamiento por ruta permite evolución sin romper consumidores y
mantiene contratos explícitos por versión.

### V. Calidad Verificable y Compatibilidad Evolutiva
Todo cambio MUST incluir pruebas automatizadas proporcionales al riesgo. Para el
frontend, el estándar obligatorio de validación de flujos de usuario CRUD MUST ser
pruebas de Integración y E2E con Cypress. Jasmine/Karma deja de ser el estándar de
validación para Definition of Done de flujos de usuario.
Antes de merge, el pipeline MUST validar build, tests y arranque básico del stack
Docker. Todo endpoint HTTP GET que retorne colecciones MUST implementar paginación y
ordenamiento usando estándar Spring Data JPA `Pageable`, aceptando los parámetros
`page`, `size` y `sort`; esta capacidad MUST estar validada por pruebas y reflejada
en el contrato OpenAPI. Ningún cambio se considera listo si no puede ejecutarse de
forma reproducible con instrucciones de proyecto.
Rationale: estabilidad continua y reducción de regresiones.

## Restricciones Técnicas y Operativas

- Build tool MUST ser Maven o Gradle con wrapper versionado en repositorio.
- La configuración MUST seguir perfiles por entorno (`dev`, `test`, `prod`) y evitar
	hardcode de secretos, hosts o puertos sensibles.
- Observabilidad mínima MUST incluir logs estructurados y endpoint de salud operativo.
- La documentación técnica MUST indicar cómo levantar PostgreSQL vía Docker y cómo
	acceder a Swagger UI.
- El frontend del proyecto MUST ubicarse en `/frontend-empleados` y nuevas piezas
	MUST usar Angular Standalone Components, Signals y Control Flow nativo.
- Los flujos de usuario frontend MUST validarse con Cypress en
	`frontend-empleados/cypress/e2e/`, incluyendo escenarios CRUD y errores de negocio
	críticos (por ejemplo `409 Conflict` al borrar entidades en uso).
- Las rutas de API MUST mantener prefijo versionado `/api/v{n}`; cambios de versión
	MUST publicarse como nueva versión de ruta.
- Los endpoints GET de colecciones MUST declarar y soportar `page`, `size` y `sort`
	bajo semántica `Pageable`.

## Flujo de Desarrollo y Puertas de Calidad

- Toda funcionalidad inicia con una especificación que identifique impacto en
	seguridad, datos y contrato OpenAPI.
- El plan de implementación MUST incluir un Constitution Check explícito y evidencia
	de cumplimiento de los cinco principios.
- Las tareas MUST separar trabajo fundacional (seguridad, BD, configuración Docker,
	documentación API) de historias de usuario para permitir validación incremental.
- En revisión de código, los PRs MUST demostrar:
	1) autenticación básica aplicada,
	2) compatibilidad Java 17 + Spring Boot 3,
	3) persistencia PostgreSQL en entorno Docker,
	4) versionamiento por prefijo `/api/v{n}` aplicado,
	5) GET de colecciones con `Pageable` (`page`, `size`, `sort`),
	6) documentación Swagger actualizada,
	7) pruebas automatizadas pasando,
	8) ejecución en verde de Cypress para flujos CRUD frontend.

## Governance

Esta constitución prevalece sobre prácticas informales del proyecto. Toda enmienda
MUST incluir: motivo, impacto, versión objetivo y plan de migración si aplica.

Política de versionado constitucional:
- MAJOR: eliminación o redefinición incompatible de principios.
- MINOR: nuevo principio o expansión normativa material.
- PATCH: aclaraciones editoriales sin cambio normativo.

Cumplimiento:
- Todo PR MUST declarar conformidad constitucional en su checklist de revisión.
- Cualquier excepción MUST documentarse y aprobarse explícitamente antes del merge.
- Las plantillas de `spec`, `plan` y `tasks` MUST mantenerse sincronizadas con este
	documento.
- Toda revisión de cumplimiento MUST verificar versionado de rutas API y paginación/
	ordenamiento obligatorio en GET de colecciones.
- Toda revisión de cumplimiento frontend MUST verificar que Cypress pasa en verde y
	que los escenarios críticos de UX/error estén cubiertos.

**Version**: 1.2.0 | **Ratified**: 2026-02-25 | **Last Amended**: 2026-03-25
