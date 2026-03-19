# Feature Specification: Estructura Frontend Angular 21 y Configuración de Integración

**Feature Branch**: `005-frontend-angular21-setup`  
**Created**: 2026-03-19  
**Status**: In Progress  
**Input**: User description: "Funcionalidad 005: Estructura Frontend Angular 21 y Configuración de Integración"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Estructura base de frontend (Priority: P1)

Como desarrollador, quiero tener un frontend inicializado en `/frontend-empleados` con estructura base consistente para empezar a construir funcionalidades sin retrasos de setup.

**Why this priority**: Sin una base de frontend organizada no se puede iniciar de forma confiable la implementación del login.

**Independent Test**: Puede validarse verificando que exista `/frontend-empleados` con estructura mínima de `servicios`, `componentes`, `modelos` e `interceptores`, y que el proyecto pueda iniciar sin errores de estructura.

**Acceptance Scenarios**:

1. **Given** que el repositorio no tiene estructura de frontend utilizable, **When** se ejecuta la inicialización de la funcionalidad, **Then** se crea o completa la estructura base en `/frontend-empleados`.
2. **Given** que `/frontend-empleados` ya existe, **When** se aplica la funcionalidad, **Then** se conservan los archivos existentes y se completa únicamente la estructura faltante.

---

### User Story 2 - Integración frontend-backend local (Priority: P1)

Como desarrollador, quiero que el frontend se conecte al backend local sin errores de origen cruzado para poder consumir servicios en desarrollo.

**Why this priority**: La integración local es bloqueante para probar autenticación y flujo de login.

**Independent Test**: Puede validarse levantando backend y frontend en local y verificando que las peticiones funcionales no fallen por políticas de origen.

**Acceptance Scenarios**:

1. **Given** backend y frontend ejecutándose en local con puertos distintos, **When** el frontend invoca servicios protegidos, **Then** el backend permite solicitudes desde el origen frontend para operaciones de lectura y escritura.
2. **Given** una solicitud desde el frontend con cabecera de autorización, **When** el backend procesa la preflight o solicitud final, **Then** la cabecera `Authorization` es aceptada por la política CORS.

---

### User Story 3 - Proxy y reglas de arquitectura frontend (Priority: P2)

Como equipo de desarrollo, queremos definir reglas claras de arquitectura frontend y proxy local para mantener consistencia técnica desde el inicio.

**Why this priority**: Estándares tempranos reducen deuda técnica y aceleran la implementación de las siguientes features.

**Independent Test**: Puede validarse revisando reglas del proyecto actualizadas y verificando que el frontend redirija rutas de API al backend en desarrollo local.

**Acceptance Scenarios**:

1. **Given** la constitución del proyecto, **When** se actualizan las reglas, **Then** queda establecido que el frontend vive en `/frontend-empleados` y sigue los estándares frontend definidos para esta etapa.
2. **Given** un cliente frontend que llama rutas relativas `/api`, **When** el entorno de desarrollo usa proxy, **Then** las solicitudes se redirigen automáticamente al puerto 8080.

---

### Edge Cases

- El backend recibe solicitudes desde un origen distinto al autorizado durante desarrollo local.
- El frontend ya existe con estructura parcial y archivos personalizados.
- El proxy del frontend existe con reglas previas que podrían entrar en conflicto con `/api`.
- La configuración CORS permite métodos esperados pero omite `Authorization` en headers permitidos.
- El backend habilita CORS en desarrollo, pero no debe relajar reglas para otros orígenes no autorizados.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST definir `/frontend-empleados` como ubicación oficial del frontend del proyecto.
- **FR-002**: El sistema MUST crear o completar la estructura base de frontend con carpetas para `servicios`, `componentes`, `modelos` e `interceptores`.
- **FR-003**: El sistema MUST preservar archivos existentes al inicializar estructura si `/frontend-empleados` ya existe.
- **FR-004**: El sistema MUST incluir una configuración de proxy en frontend para redirigir rutas `/api` al backend local durante desarrollo.
- **FR-005**: El sistema MUST permitir solicitudes de origen cruzado desde el frontend local para operaciones de lectura y escritura comunes.
- **FR-006**: El sistema MUST aceptar la cabecera `Authorization` dentro de la configuración CORS del backend.
- **FR-007**: El sistema MUST actualizar la constitución del proyecto para establecer estándares obligatorios de arquitectura frontend para esta fase.
- **FR-008**: El sistema MUST dejar el entorno listo para iniciar la implementación de la especificación de Login sin errores de conexión frontend-backend.
- **FR-009**: El sistema MUST mantener la seguridad existente del backend sin deshabilitar autenticación por efecto de la configuración CORS.

### Key Entities *(include if feature involves data)*

- **FrontendWorkspace**: Representa la raíz de la aplicación frontend en `/frontend-empleados` y su estructura de carpetas base.
- **ProjectConstitutionRule**: Representa una regla de arquitectura del proyecto que define ubicación y estándares obligatorios del frontend en esta fase.
- **DevProxyConfig**: Representa la configuración de redirección local para rutas `/api` durante desarrollo.
- **BackendCorsPolicy**: Representa la política de orígenes, métodos y cabeceras permitidos para integración local frontend-backend.

## Assumptions

- Backend y frontend seguirán ejecutándose en local sobre puertos distintos durante desarrollo.
- El objetivo de esta funcionalidad es preparar infraestructura y conectividad; no incluye aún implementación visual/funcional del login.
- La configuración de proxy se limita al entorno de desarrollo local.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de los entornos de desarrollo configurados con esta feature pueden realizar llamadas frontend a `/api` sin error de políticas de origen.
- **SC-002**: El 100% de las solicitudes de prueba de lectura y escritura desde frontend local son aceptadas por la política de origen configurada.
- **SC-003**: La estructura base de `/frontend-empleados` queda disponible y validable en menos de 10 minutos desde un clon limpio del repositorio.
- **SC-004**: El equipo puede iniciar la siguiente feature de Login sin cambios adicionales de infraestructura de conexión en al menos 1 entorno local validado.

## Implementation Alignment

- Se creó el workspace frontend en `/frontend-empleados` con estructura base (`componentes`, `servicios`, `modelos`, `interceptores`).
- Se implementó proxy de desarrollo `/api -> http://localhost:8080`.
- Se configuró CORS en backend con origen/métodos/headers explícitos, incluyendo `Authorization`.
- Se actualizó la constitución para hacer obligatoria la ubicación del frontend y los estándares de Angular 21.
