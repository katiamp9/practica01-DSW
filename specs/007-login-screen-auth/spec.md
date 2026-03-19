# Feature Specification: Diseño del Login

**Feature Branch**: `007-login-screen-auth`  
**Created**: 2026-03-19  
**Status**: Draft  
**Input**: User description: "Feature 007: Diseño del Login"

## Clarifications

### Session 2026-03-19

- Q: ¿Qué hacer si el backend autentica pero devuelve un rol no soportado? → A: Invalidar sesión y redirigir a `/login` con mensaje de rol no permitido.
- Q: ¿Cómo obtiene frontend el rol para redirección inicial? → A: El endpoint de login responde éxito más rol (`ROLE_ADMIN`/`ROLE_USER`).
- Q: ¿Cuál es el contrato exacto del login? → A: `POST /api/v1/auth/login` con payload `{ "username": "...", "password": "..." }` y respuesta mínima `{ "rol": "...", "nombre": "..." }`.
- Q: ¿Qué valida el guard de rutas privadas? → A: Debe validar sesión autenticada y rol permitido por ruta.
- Q: ¿Qué hacer ante sesión inválida o rol no autorizado al entrar a ruta privada? → A: Redirigir a `/login` y limpiar sesión.

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

### User Story 1 - Inicio de sesión y redirección por rol (Priority: P1)

Como usuario del sistema, quiero iniciar sesión con mis credenciales y ser redirigido automáticamente según mi rol para llegar al área correcta sin pasos manuales.

**Why this priority**: Es el núcleo de acceso a la aplicación; sin este flujo no se puede usar ninguna funcionalidad autenticada.

**Independent Test**: Puede validarse iniciando sesión con un usuario administrador y con un usuario estándar, comprobando redirecciones correctas y estado autenticado activo.

**Acceptance Scenarios**:

1. **Given** un usuario con rol administrativo y credenciales válidas, **When** inicia sesión, **Then** el sistema lo redirige al panel administrativo.
2. **Given** un usuario con rol estándar y credenciales válidas, **When** inicia sesión, **Then** el sistema lo redirige a la pantalla principal de usuario.
3. **Given** credenciales inválidas, **When** el usuario intenta iniciar sesión, **Then** el sistema muestra un mensaje claro y no inicia sesión.
4. **Given** un usuario autenticado cuyo rol no pertenece al conjunto permitido, **When** el sistema procesa la sesión, **Then** invalida la sesión y redirige a login con mensaje de rol no autorizado.

---

### User Story 2 - Sesión persistente y navegación protegida (Priority: P1)

Como usuario autenticado, quiero que mi sesión se mantenga durante la navegación y que las rutas privadas estén protegidas para evitar accesos no autorizados.

**Why this priority**: Asegura continuidad de uso y protección básica del sistema tras autenticación.

**Independent Test**: Puede validarse accediendo a rutas privadas con sesión activa y sin sesión, verificando acceso permitido o bloqueo según corresponda.

**Acceptance Scenarios**:

1. **Given** un usuario autenticado, **When** navega por rutas protegidas, **Then** el acceso es permitido sin volver a autenticarse en cada vista.
2. **Given** un usuario no autenticado, **When** intenta abrir una ruta protegida, **Then** el sistema bloquea el acceso y redirige al login.
3. **Given** una sesión autenticada, **When** se realizan solicitudes al backend, **Then** cada solicitud incluye credenciales de autenticación de forma consistente.

---

### User Story 3 - Experiencia visual profesional del login y vistas destino (Priority: P2)

Como usuario final, quiero una pantalla de login moderna y clara, junto con vistas iniciales diferenciadas por rol, para tener confianza y orientación inmediata al entrar.

**Why this priority**: Mejora adopción, reduce errores de uso y establece una base visual sólida para las siguientes features.

**Independent Test**: Puede validarse revisando la pantalla de login y vistas de destino con criterios de legibilidad, jerarquía visual y mensajes de validación claros.

**Acceptance Scenarios**:

1. **Given** un usuario en la pantalla de login, **When** interactúa con campos vacíos o formato inválido, **Then** recibe validaciones visibles y comprensibles.
2. **Given** un administrador autenticado, **When** llega a su vista inicial, **Then** encuentra una interfaz coherente con su contexto de administración.
3. **Given** un usuario estándar autenticado, **When** llega a su vista inicial, **Then** encuentra una interfaz coherente con su contexto operativo.

---

### Edge Cases
- Credenciales con espacios accidentales al inicio o final.
- Correo con formato inválido o en mayúsculas mixtas.
- Rol no reconocido en la respuesta de autenticación.
- Usuario autenticado que intenta acceder manualmente a una ruta de otro rol.
- Sesión incompleta o corrupta en almacenamiento local.

## Requirements *(mandatory)*

### Functional Requirements
- **FR-001**: El sistema MUST proporcionar una pantalla de login con campos de correo y contraseña.
- **FR-002**: El sistema MUST validar que ambos campos sean obligatorios antes de enviar autenticación.
- **FR-003**: El sistema MUST validar formato de correo antes de permitir envío.
- **FR-004**: El sistema MUST autenticar al usuario contra `POST /api/v1/auth/login` usando payload `{ "username": "...", "password": "..." }`.
- **FR-005**: El sistema MUST almacenar el estado de sesión autenticada para mantener navegación privada.
- **FR-006**: El sistema MUST almacenar en sesión el rol del usuario autenticado.
- **FR-007**: El sistema MUST redirigir automáticamente a administradores a la ruta de panel administrativo.
- **FR-008**: El sistema MUST redirigir automáticamente a usuarios estándar a la ruta principal de usuario.
- **FR-009**: El sistema MUST proteger todas las rutas privadas, bloqueando acceso cuando no exista sesión válida.
- **FR-010**: El sistema MUST adjuntar credenciales de autenticación a todas las solicitudes hacia el backend durante sesión activa.
- **FR-011**: El sistema MUST mostrar feedback claro cuando la autenticación falle.
- **FR-012**: El sistema MUST proporcionar vistas iniciales diferenciadas para perfil administrativo y perfil estándar.
- **FR-013**: El sistema MUST invalidar la sesión y redirigir a login cuando el rol autenticado no sea reconocido como permitido.
- **FR-014**: El sistema MUST obtener el rol y nombre del usuario directamente en la respuesta del login exitoso `{ "rol": "...", "nombre": "..." }` para decidir redirección inicial y estado de sesión.
- **FR-015**: El sistema MUST validar en el guard tanto autenticación como autorización por rol para cada ruta privada.
- **FR-016**: El sistema MUST limpiar sesión y redirigir a login al detectar sesión inválida o acceso por rol no autorizado en rutas privadas.

### Key Entities *(include if feature involves data)*

- **LoginSession**: Representa la sesión activa del usuario, incluyendo indicador de autenticación, credenciales de acceso y rol.
- **UserIdentity**: Representa al usuario autenticado con atributos de identificación y rol autorizado (`ROLE_ADMIN` o `ROLE_USER`).
- **RouteAccessPolicy**: Representa reglas de acceso y redirección para rutas privadas según estado de sesión y rol.

## Dependencies

- Backend de autenticación disponible y accesible desde el frontend.
- Backend devuelve rol del usuario autenticado en la respuesta de login de forma consistente.
- Rutas de destino para perfil administrativo y perfil estándar definidas en la aplicación.

## Assumptions

- Los roles iniciales válidos para login son `ROLE_ADMIN` y `ROLE_USER`.
- La sesión se mantiene en almacenamiento local del navegador durante el ciclo de uso normal.
- El alcance de esta feature se limita a login, guardado de sesión, protección de rutas y vistas iniciales por rol.

## Success Criteria *(mandatory)*

### Measurable Outcomes
- **SC-001**: El 95% de usuarios con credenciales válidas completa login y llega a su ruta correspondiente en menos de 20 segundos.
- **SC-002**: El 100% de intentos de acceso directo a rutas privadas sin sesión válida se bloquea y redirige a login.
- **SC-003**: El 100% de solicitudes autenticadas al backend desde la aplicación activa incluye credenciales de acceso.
- **SC-004**: Al menos 90% de usuarios de prueba completa el flujo de login sin asistencia en el primer intento.
- **SC-005**: El 100% de sesiones con rol no permitido se bloquea y redirige a login sin acceso a rutas privadas.
- **SC-006**: El 100% de intentos de acceso a rutas de rol no autorizado se bloquea por guard sin renderizar contenido privado.
- **SC-007**: El 100% de eventos de sesión inválida en rutas privadas termina en limpieza de sesión y redirección a login.
