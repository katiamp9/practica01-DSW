# Feature Specification: Soporte de Roles y Autorización

**Feature Branch**: `006-roles-autorizacion-back`  
**Created**: 2026-03-19  
**Status**: Draft  
**Input**: User description: "Feature 006-BACK: Soporte de Roles y Autorización"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Rol por defecto en empleados (Priority: P1)

Como equipo de backend, queremos que cada empleado nuevo tenga un rol por defecto para asegurar que todas las cuentas tengan permisos básicos consistentes.

**Why this priority**: Es la base del modelo de autorización y evita registros sin definición de rol.

**Independent Test**: Crear un empleado nuevo y verificar que su rol queda asignado automáticamente como `ROLE_USER`.

**Acceptance Scenarios**:

1. **Given** una solicitud de alta de empleado válida, **When** se crea el empleado, **Then** el rol asignado es `ROLE_USER` aunque no se envíe rol explícito.
2. **Given** empleados ya existentes en la base de datos, **When** se aplica la actualización de la feature, **Then** todos quedan con rol no nulo y valor por defecto `ROLE_USER` si no tenían uno definido.

---

### User Story 2 - Administrador inicial con privilegios de administración (Priority: P1)

Como responsable del sistema, quiero que la cuenta administradora inicial quede registrada con rol administrativo para mantener el acceso de gestión desde el arranque.

**Why this priority**: Garantiza continuidad operativa y evita pérdida de privilegios del usuario administrador inicial.

**Independent Test**: Verificar el registro del administrador inicial y confirmar que su rol es `ROLE_ADMIN`.

**Acceptance Scenarios**:

1. **Given** la carga de datos iniciales del sistema, **When** se inserta el usuario administrador inicial, **Then** se guarda con rol `ROLE_ADMIN`.

---

### User Story 3 - Autorización basada en rol reconocido por seguridad (Priority: P2)

Como sistema de autenticación, quiero mapear el rol almacenado de cada usuario a autoridades de seguridad para aplicar reglas de acceso por rol.

**Why this priority**: Habilita la autorización efectiva usando la información de rol persistida.

**Independent Test**: Autenticar un usuario con rol persistido y comprobar que la capa de seguridad lo expone como autoridad utilizable en decisiones de acceso.

**Acceptance Scenarios**:

1. **Given** un usuario autenticado con rol persistido, **When** se carga su identidad en seguridad, **Then** su rol se reconoce como autoridad de acceso.

---

### Edge Cases

- Empleados existentes sin rol previo al despliegue de la feature.
- Intento de persistir un empleado nuevo sin campo de rol explícito.
- Usuario administrador inicial existente con datos previos que no incluyen rol.
- Valor de rol inválido o vacío recibido desde entradas externas.
- Carga de usuario para autenticación cuando el rol del registro no está normalizado.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST incluir un atributo de rol en la entidad de empleado.
- **FR-002**: El sistema MUST asignar `ROLE_USER` como rol por defecto para empleados creados.
- **FR-003**: El sistema MUST garantizar que ningún empleado quede sin rol tras la actualización de esta feature.
- **FR-004**: El sistema MUST actualizar la inserción del administrador inicial para registrar el rol `ROLE_ADMIN`.
- **FR-005**: El sistema MUST interpretar el rol almacenado del usuario como una autoridad válida para la capa de seguridad.
- **FR-006**: El sistema MUST preservar compatibilidad con datos existentes, aplicando respaldo de rol por defecto para evitar fallos funcionales.
- **FR-007**: El sistema MUST mantener el comportamiento actual de autenticación básica y solo extenderlo con soporte de autorización por rol.

### Key Entities *(include if feature involves data)*

- **Empleado**: Representa al trabajador del sistema e incluye su rol de autorización (`ROLE_USER` por defecto).
- **AdministradorInicial**: Representa el usuario administrativo preconfigurado que debe conservar privilegios de administración (`ROLE_ADMIN`).
- **RolDeAcceso**: Representa el conjunto de valores de autorización reconocidos por el sistema (`ROLE_USER`, `ROLE_ADMIN`).

## Assumptions

- Esta feature no redefine el flujo de autenticación existente; únicamente incorpora autorización basada en rol.
- El conjunto inicial de roles soportados se limita a `ROLE_USER` y `ROLE_ADMIN`.
- La actualización de datos históricos se ejecuta durante la migración de base de datos del proyecto.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de los empleados nuevos creados después del despliegue quedan con rol `ROLE_USER` cuando no se provee rol explícito.
- **SC-002**: El 100% de los registros de empleados existentes quedan con rol no nulo tras la migración.
- **SC-003**: El usuario administrador inicial queda con rol `ROLE_ADMIN` en el 100% de los entornos inicializados desde cero.
- **SC-004**: El 100% de los usuarios autenticados cargan al menos una autoridad derivada de su rol persistido.
