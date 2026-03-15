# Feature Specification: Autenticación de empleados por correo y contraseña

**Feature Branch**: `003-003-auth-empleado-login`  
**Created**: 2026-03-14  
**Status**: Implemented (Pending Release Gate)  
**Input**: User description: "Funcionalidad 003: Autenticación de empleados mediante correo y contraseña"

## Clarifications

### Session 2026-03-14

- Q: ¿La contraseña inicial se insertará como hash BCrypt de `${INITIAL_ADMIN_PASSWORD_HASH}` y se documentará en `.env.example`? → A: Sí, la migración V3 insertará credencial inicial con hash BCrypt de `${INITIAL_ADMIN_PASSWORD_HASH}` y el acceso inicial quedará documentado en `.env.example`.
- Q: ¿La relación de `cuentas_empleado` será directa contra la columna `clave` de `empleados`? → A: Sí, `cuentas_empleado` referenciará directamente `empleados.clave` como identidad canónica (Clave Compuesta Lógica).
- Q: ¿Cómo se desactiva el fallback de contraseña en claro sin romper Basic Auth constitucional? → A: Se elimina el fallback en código y Basic Auth seguirá activo usando credenciales provistas por variables de entorno/secretos en formato hash BCrypt.
- Q: ¿La cuenta inicial `admin@empresa.com` se crea solo en dev/test o en todos los entornos? → A: Se crea en todos los entornos; la contraseña inicial se gestiona por variables de entorno/secretos (sin credenciales en texto plano en repositorio).

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Inicio de sesión válido (Priority: P1)

Como empleado, quiero iniciar sesión con mi correo y contraseña para acceder de forma segura al sistema.

**Why this priority**: Es el flujo principal sin el cual no existe acceso autenticado a la aplicación.

**Independent Test**: Puede probarse enviando un correo registrado y una contraseña correcta al endpoint de autenticación y verificando respuesta exitosa.

**Acceptance Scenarios**:

1. **Given** un empleado con cuenta activa y contraseña válida, **When** envía sus credenciales al endpoint de login, **Then** el sistema responde con autenticación exitosa.
2. **Given** un empleado autenticado correctamente, **When** finaliza la solicitud de login, **Then** el sistema registra el evento de autenticación en auditoría.

---

### User Story 2 - Rechazo seguro de credenciales inválidas (Priority: P1)

Como empleado, quiero recibir una respuesta clara y segura cuando mis credenciales no son válidas para corregir el intento sin exponer información sensible.

**Why this priority**: Evita accesos no autorizados y reduce filtración de información sobre cuentas existentes.

**Independent Test**: Puede probarse enviando combinaciones incorrectas de correo y contraseña y verificando que siempre se devuelve respuesta no autorizada con mensaje genérico.

**Acceptance Scenarios**:

1. **Given** un correo no registrado, **When** se intenta iniciar sesión, **Then** el sistema responde con no autorizado y el mensaje "Invalid email or password".
2. **Given** un correo registrado con contraseña incorrecta, **When** se intenta iniciar sesión, **Then** el sistema responde con no autorizado y el mensaje "Invalid email or password".

---

### User Story 3 - Gestión segura de credenciales (Priority: P2)

Como responsable del sistema, quiero que las contraseñas se almacenen de forma no reversible para proteger a los empleados ante exposición de datos.

**Why this priority**: Cumple con principios de seguridad y minimiza impacto ante incidentes.

**Independent Test**: Puede probarse revisando los registros persistidos de credenciales y verificando que no existe almacenamiento de contraseñas en texto plano.

**Acceptance Scenarios**:

1. **Given** una nueva credencial de empleado, **When** se persiste en el sistema, **Then** se almacena únicamente una representación hash no reversible de la contraseña.

### Edge Cases

- Intento de login con campos vacíos o faltantes debe ser rechazado sin autenticar al usuario.
- Correos con diferencias de mayúsculas/minúsculas deben tratarse de manera consistente para evitar falsos rechazos.
- Múltiples intentos fallidos consecutivos deben registrarse en auditoría sin revelar si el correo existe.
- Si existe inconsistencia entre cuenta y credencial (cuenta sin credencial asociada), el sistema debe responder como credencial inválida.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir autenticación de empleados mediante correo y contraseña a través de una operación de inicio de sesión dedicada.
- **FR-002**: El sistema MUST validar que el correo recibido corresponda a una cuenta de empleado registrada.
- **FR-003**: El sistema MUST validar la contraseña comparando la entrada del usuario contra una credencial almacenada en formato hash no reversible.
- **FR-004**: El sistema MUST rechazar cualquier autenticación con correo inexistente o contraseña inválida usando una respuesta uniforme de no autorizado.
- **FR-005**: El sistema MUST devolver el mensaje "Invalid email or password" para fallos de autenticación sin distinguir la causa exacta.
- **FR-006**: El sistema MUST devolver una respuesta de éxito cuando correo y contraseña sean válidos.
- **FR-007**: El sistema MUST registrar en auditoría cada intento de autenticación, incluyendo resultado exitoso o fallido.
- **FR-008**: El sistema MUST mantener la relación entre empleado, cuenta de acceso y credenciales para asegurar trazabilidad de identidad.
- **FR-009**: El sistema MUST impedir almacenamiento o exposición de contraseñas en texto plano en cualquier operación de autenticación.
- **FR-010**: El sistema MUST recibir email y password como entrada obligatoria en la operación de inicio de sesión.
- **FR-011**: El sistema MUST persistir la información de cuentas y credenciales de empleado en entidades de datos separadas vinculadas al empleado.
- **FR-012**: El sistema MUST asignar identificadores únicos globales a registros de cuenta y credenciales para evitar colisiones.
- **FR-013**: El sistema MUST exponer `POST /api/v1/auth/login` como endpoint público explícito; todos los demás endpoints mantienen protección por Basic Auth salvo excepciones definidas por especificación.
- **FR-014**: El sistema MUST responder éxito de login con estado `200` y payload exacto `{"authenticated": true}`.
- **FR-015**: El sistema MUST crear en migración V3 un acceso inicial con correo `admin@empresa.com` en todos los entornos y credencial almacenada exclusivamente como hash BCrypt obtenido de `${INITIAL_ADMIN_PASSWORD_HASH}`.
- **FR-016**: El sistema MUST gestionar la contraseña inicial de `admin@empresa.com` mediante variables de entorno/secretos, sin almacenar credenciales en texto plano en el repositorio.
- **FR-017**: El sistema MUST relacionar `cuentas_empleado` con `empleados.clave`, tratada como Clave Compuesta Lógica en este dominio.
- **FR-018**: El sistema MUST eliminar cualquier fallback de credenciales en texto plano embebido en código fuente para autenticación básica.
- **FR-019**: El sistema MUST documentar en `.env.example` el acceso inicial y el mecanismo de provisión de secretos para el equipo de desarrollo.

### Key Entities *(include if feature involves data)*

- **Empleado**: Persona dada de alta en el sistema, identificada por su Clave Compuesta Lógica (`clave`) y vinculada a una única cuenta de acceso.
- **CuentaEmpleado**: Registro de acceso del empleado que contiene identificador único, correo y referencia al empleado.
- **CredencialEmpleado**: Registro protegido de autenticación con identificador único y hash de contraseña asociado a una cuenta de empleado.
- **EventoAuditoriaAutenticacion**: Registro de trazabilidad que guarda cada intento de login, su resultado y datos mínimos para seguimiento operativo.

## Assumptions

- La funcionalidad se aplica solo a empleados ya existentes en el CRUD actual.
- Cada empleado tiene una cuenta de acceso única para login por correo.
- `POST /api/v1/auth/login` es público para bootstrap de autenticación; la emisión de sesiones o tokens se define en una fase posterior.
- Las reglas de auditoría siguen la política vigente del proyecto y se integran con el servicio de auditoría existente.
- La cuenta inicial `admin@empresa.com` se crea en todos los entornos, y la contraseña inicial se provee por secretos/variables de entorno de cada entorno.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de intentos con credenciales válidas reciben respuesta de éxito en pruebas funcionales.
- **SC-002**: El 100% de intentos con correo inexistente o contraseña inválida reciben respuesta no autorizada con el mensaje definido.
- **SC-003**: El 100% de intentos de login (exitosos y fallidos) quedan registrados en auditoría durante pruebas de aceptación.
- **SC-004**: En revisión de datos de prueba, el 100% de contraseñas persistidas se almacenan como hash no reversible y nunca en texto plano.
- **SC-005**: Al menos 95% de usuarios de prueba completan el flujo de inicio de sesión en su primer intento cuando usan credenciales correctas.
