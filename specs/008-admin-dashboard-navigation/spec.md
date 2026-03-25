# Feature Specification: Admin Dashboard Home & Navigation

**Feature Branch**: `008-admin-dashboard-navigation`  
**Created**: 2026-03-21  
**Status**: Draft  
**Input**: User description: "Feature 008: Admin Dashboard Home & Navigation"

## Clarifications

### Session 2026-03-21

- Q: ¿Qué identificador debe mostrarse en la tabla de empleados? → A: Mostrar `clave` como identificador principal.
- Q: ¿Qué rutas deben usar los placeholders de navegación admin? → A: Usar `/admin/empleados` y `/admin/departamentos`.
- Q: ¿Cómo se maneja error al cargar empleados en dashboard? → A: Mostrar mensaje claro y botón manual “Reintentar”.
- Q: ¿Cómo se mostrará la lista cuando haya muchos empleados? → A: Mostrar primera página con controles de paginación siguiente/anterior.
- Q: ¿Cuál será el tamaño inicial de página en dashboard? → A: 10 empleados por página.

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

### User Story 1 - Visualización de empleados en dashboard admin (Priority: P1)

Como administrador autenticado, quiero ver una lista de empleados dentro del dashboard para consultar rápidamente quiénes existen y su rol.

**Why this priority**: Es la funcionalidad central de valor para la pantalla de administración y habilita decisiones inmediatas sin navegación adicional.

**Independent Test**: Puede validarse iniciando sesión como administrador y confirmando que la pantalla muestra una lista de empleados cargada desde el backend con sus datos principales.

**Acceptance Scenarios**:

1. **Given** un administrador autenticado, **When** abre el dashboard, **Then** la lista de empleados se carga y se muestra con nombre, identificador y rol.
2. **Given** una lista de empleados disponible, **When** el dashboard renderiza la tabla, **Then** cada fila presenta una etiqueta visual de rol diferenciada.
3. **Given** que no hay empleados o falla la carga, **When** el dashboard intenta mostrar la lista, **Then** se informa el estado de forma clara sin romper la navegación.
4. **Given** múltiples páginas de empleados, **When** el administrador usa controles de paginación, **Then** puede navegar siguiente/anterior sin salir del dashboard.
5. **Given** que la carga falla, **When** se muestra estado de error, **Then** aparece un botón visible “Reintentar” para relanzar la consulta.
6. **Given** que la lista está vacía, **When** se muestra estado sin datos, **Then** se presenta un mensaje amigable orientado al usuario.

---

### User Story 2 - Navegación superior administrativa (Priority: P1)

Como administrador, quiero una barra de navegación superior con accesos a Inicio, Empleados y Departamentos para moverme de forma predecible entre secciones.

**Why this priority**: Sin navegación consistente, la experiencia administrativa se vuelve frágil y poco escalable para próximas funcionalidades.

**Independent Test**: Puede validarse verificando que la barra aparece en la parte superior del dashboard y que cada link navega a su destino esperado.

**Acceptance Scenarios**:

1. **Given** un administrador autenticado en el dashboard, **When** visualiza la parte superior de la pantalla, **Then** encuentra una barra con links a Inicio, Empleados y Departamentos.
2. **Given** que el usuario selecciona el link Inicio, **When** se procesa la navegación, **Then** permanece o vuelve al dashboard principal de administración.
3. **Given** que el usuario selecciona Empleados o Departamentos, **When** se procesa la navegación, **Then** llega a una vista placeholder con el mensaje “Próximamente”.

---

### User Story 3 - Presentación visual moderna del módulo admin (Priority: P2)

Como usuario administrativo, quiero que la tabla y la navegación tengan una apariencia moderna y clara para interpretar información rápidamente y con confianza.

**Why this priority**: Mejora legibilidad y percepción de calidad del sistema, aunque depende de que la navegación y datos ya estén funcionales.

**Independent Test**: Puede validarse revisando visualmente estilo de tabla, estados hover y etiquetas de rol, sin depender de lógica adicional.

**Acceptance Scenarios**:

1. **Given** una lista de empleados visible, **When** el usuario recorre filas de la tabla, **Then** observa feedback visual de hover por fila.
2. **Given** roles distintos en la lista, **When** se muestran en la tabla, **Then** cada rol usa una etiqueta visual distinguible.
3. **Given** la navegación superior y la tabla presentes, **When** se renderiza el dashboard, **Then** el diseño mantiene consistencia visual y jerarquía clara.

---

### Edge Cases
- Respuesta del backend vacía o con lista sin elementos.
- Rol de empleado no reconocido para etiquetado visual.
- Error temporal de red al cargar empleados en dashboard.
- Error temporal de red al cargar empleados en dashboard con opción visible de “Reintentar”.
- Usuario no autorizado intentando acceder a rutas administrativas.
- Navegación directa a rutas placeholder sin sesión válida.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST mostrar una barra de navegación superior en el dashboard de administración.
- **FR-002**: La barra de navegación MUST incluir accesos a Inicio, Empleados y Departamentos.
- **FR-003**: El acceso Inicio MUST dirigir al dashboard principal de administración.
- **FR-004**: Los accesos Empleados y Departamentos MUST dirigir a `/admin/empleados` y `/admin/departamentos` respectivamente, mostrando vistas placeholder con el mensaje “Próximamente”.
- **FR-005**: El dashboard de administración MUST cargar la lista de empleados desde el servicio de consulta de empleados del backend.
- **FR-006**: La tabla de empleados MUST mostrar al menos nombre, `clave` como identificador principal y rol por cada registro.
- **FR-007**: Los roles en la tabla MUST mostrarse mediante etiquetas visualmente diferenciadas.
- **FR-008**: Las filas de la tabla MUST proporcionar feedback visual al pasar el cursor.
- **FR-009**: La carga de empleados MUST respetar el mecanismo vigente de autenticación de solicitudes protegidas.
- **FR-010**: La pantalla MUST mostrar estado `empty` con mensaje amigable cuando no existan empleados para la consulta actual.
- **FR-013**: Ante error de carga de empleados, la pantalla MUST ofrecer una acción manual de “Reintentar” para relanzar la consulta.
- **FR-011**: La lista de empleados en cliente MUST mantenerse en estado reactivo para reflejar actualizaciones de datos sin recargar la página completa.
- **FR-012**: El acceso a dashboard, navegación y placeholders administrativos MUST mantenerse protegido por sesión válida y permisos de rol.
- **FR-014**: El dashboard MUST mostrar controles de paginación siguiente/anterior para navegar páginas de empleados.
- **FR-015**: El dashboard MUST usar tamaño inicial de 10 empleados por página en la vista de listado.
- **FR-016**: El `EmpleadoQueryService` en frontend MUST ejecutar consultas exclusivamente vía `HttpClient` para que el `AuthInterceptor` adjunte credenciales automáticamente en solicitudes protegidas.
- **FR-017**: La feature MUST incluir pruebas unitarias para `EmpleadoQueryService` y para los componentes principales del módulo administrativo.

### Key Entities *(include if feature involves data)*

- **EmpleadoListado**: Representa un registro mostrado en la tabla administrativa con `clave` como identificador principal, nombre y rol.
- **ItemNavegacionAdmin**: Representa cada opción de la barra superior (Inicio, Empleados, Departamentos) con destino y estado activo.
- **EstadoDashboardAdmin**: Representa el estado funcional de la vista (cargando, con datos, vacío o con error) durante la consulta de empleados.

## Dependencies

- Servicio de listado de empleados del backend disponible para usuarios autenticados con permisos administrativos.
- Sesión autenticada y autorización por rol vigentes en frontend.
- Datos de empleados existentes en base para validar render de tabla y etiquetas de rol.

## Assumptions

- El endpoint de empleados retorna información suficiente para construir la tabla administrativa sin llamadas adicionales.
- El texto “Próximamente” es suficiente para placeholders de Empleados y Departamentos en esta iteración.
- El dashboard actual ya cuenta con contexto de usuario autenticado para aplicar protección de navegación.
- El tamaño de página inicial para listado administrativo es fijo en 10 durante esta iteración.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de sesiones administrativas válidas visualiza la barra de navegación con los tres accesos requeridos.
- **SC-002**: El 95% de cargas exitosas del dashboard muestra la tabla inicial de empleados en menos de 2 segundos desde la apertura de la vista.
- **SC-003**: El 100% de clics en Empleados y Departamentos navega a su placeholder correspondiente mostrando “Próximamente”.
- **SC-004**: El 100% de empleados listados muestra su rol con etiqueta visual diferenciada.
- **SC-005**: Al menos el 90% de usuarios de validación interna reporta que la navegación y lectura de la tabla es clara en el primer intento.
