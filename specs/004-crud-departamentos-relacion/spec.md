# Feature Specification: CRUD de Departamentos y Relación Obligatoria

**Feature Branch**: `004-crud-departamentos-relacion`  
**Created**: 2026-03-16  
**Status**: Draft  
**Input**: User description: "Funcionalidad 004: CRUD de Departamentos y Relación Obligatoria"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Gestión de departamentos (Priority: P1)

Como administrador, quiero crear, consultar, editar y eliminar departamentos para mantener la estructura organizacional actualizada.

**Why this priority**: Sin catálogo de departamentos no se puede cumplir la relación obligatoria en empleados.

**Independent Test**: Puede probarse ejecutando operaciones de alta, consulta, actualización y baja de departamentos desde API y verificando persistencia correcta.

**Acceptance Scenarios**:

1. **Given** que no existe un departamento con el nombre indicado, **When** el administrador crea un departamento, **Then** el sistema lo registra y lo retorna con su identificador.
2. **Given** que existe un departamento, **When** el administrador lo actualiza, **Then** el sistema persiste el nuevo valor y lo refleja en la respuesta.
3. **Given** que existe un departamento sin empleados asociados, **When** el administrador lo elimina, **Then** el sistema confirma la eliminación.

---

### User Story 2 - Alta de empleado con departamento obligatorio (Priority: P1)

Como administrador, quiero que cada nuevo empleado se cree con un departamento obligatorio para evitar registros huérfanos y mantener integridad organizacional.

**Why this priority**: Garantiza calidad de datos desde el momento de creación de empleado.

**Independent Test**: Puede probarse intentando crear empleados con y sin departamento, verificando rechazo cuando falta departamento y éxito cuando el departamento existe.

**Acceptance Scenarios**:

1. **Given** un departamento válido, **When** se crea un empleado con `departamentoId`, **Then** el empleado se guarda correctamente vinculado al departamento.
2. **Given** un `departamentoId` inexistente, **When** se intenta crear un empleado, **Then** el sistema rechaza la operación con mensaje claro.
3. **Given** que se envían `email` y `password` en la creación de empleado, **When** el empleado se guarda, **Then** se crea automáticamente su acceso vinculado al empleado.

---

### User Story 3 - Actualización parcial y activación diferida (Priority: P2)

Como administrador, quiero actualizar solo los campos necesarios del empleado (incluyendo cuenta) para activar empleados antiguos sin sobrescribir datos existentes.

**Why this priority**: Permite evolución progresiva de datos y evita cambios involuntarios en información ya capturada.

**Independent Test**: Puede probarse actualizando un empleado con solo `email/password` y verificando que nombre, dirección y teléfono permanecen intactos.

**Acceptance Scenarios**:

1. **Given** un empleado sin cuenta, **When** se actualiza enviando solo `email` y `password`, **Then** el sistema crea su acceso y conserva los demás campos del empleado sin cambios.
2. **Given** un empleado con cuenta existente, **When** se actualiza con nuevo `email` y/o `password`, **Then** el sistema actualiza solo los datos de acceso enviados.
3. **Given** un correo ya usado por otra cuenta, **When** se intenta crear o actualizar acceso, **Then** el sistema rechaza la operación completa sin persistir cambios parciales.

### Edge Cases

- Intento de crear empleado sin `departamentoId`.
- Intento de crear o actualizar empleado con `departamentoId` inexistente.
- Actualización parcial con campos de empleado en `null` y solo datos de acceso presentes.
- Envío de `password` sin `email` en actualización parcial.
- Intento de asignar correo ya existente en otra cuenta durante alta diferida o actualización.
- Eliminación de departamento que aún tiene empleados asociados.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST exponer operaciones CRUD completas para departamentos.
- **FR-002**: El sistema MUST introducir una relación obligatoria entre empleado y departamento a nivel de datos.
- **FR-003**: El sistema MUST aplicar el cambio estructural en dos pasos de migración consecutivos: uno para estructura de departamentos y relación obligatoria, y otro para carga inicial de departamentos.
- **FR-004**: El sistema MUST cargar datos iniciales de departamentos con al menos: Sistemas, RH y Ventas.
- **FR-005**: El sistema MUST requerir `departamentoId` en creación de empleado.
- **FR-006**: El sistema MUST permitir que `departamentoId` sea opcional en actualización de empleado y mantener el valor actual si no se envía.
- **FR-007**: El sistema MUST validar que el departamento exista antes de crear o actualizar la relación del empleado.
- **FR-008**: El sistema MUST permitir actualización parcial de empleado, modificando únicamente los campos presentes en la petición.
- **FR-009**: El sistema MUST permitir activación diferida de cuenta de empleado durante actualización cuando se envían `email` y `password` y el empleado no tiene cuenta previa.
- **FR-010**: El sistema MUST actualizar correo y/o contraseña de cuenta existente cuando se envían en actualización de empleado.
- **FR-011**: El sistema MUST exigir `email` cuando se envía `password` en actualización de empleado.
- **FR-012**: El sistema MUST impedir correos duplicados entre cuentas distintas en creación y actualización de acceso.
- **FR-013**: El sistema MUST tratar la creación/actualización de empleado y cuenta como operación transaccional, de forma que ante error no queden datos parciales persistidos.
- **FR-014**: El sistema MUST almacenar contraseñas de cuenta únicamente en formato hash no reversible.
- **FR-015**: El sistema MUST actualizar y mantener vigentes todos los tests unitarios e integrales afectados por la nueva relación obligatoria y cambios de DTO.

### Key Entities *(include if feature involves data)*

- **Departamento**: Unidad organizacional con identificador único y nombre de negocio.
- **Empleado**: Registro de persona empleada que debe pertenecer obligatoriamente a un departamento.
- **CuentaEmpleado**: Registro de acceso vinculado a un empleado para autenticación por correo.
- **CredencialEmpleado**: Registro de contraseña en hash asociada a una cuenta de empleado.

## Assumptions

- Se realizará reinicio de base de datos, por lo que no se requiere compatibilidad con datos históricos previos al cambio estructural.
- El flujo de autenticación existente por correo/contraseña continúa vigente y se extiende para soportar activación diferida.
- El catálogo inicial de departamentos puede ampliarse posteriormente sin alterar las reglas de obligatoriedad de empleado.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de altas de empleado sin `departamentoId` son rechazadas con error de validación.
- **SC-002**: El 100% de altas y actualizaciones con `departamentoId` inválido son rechazadas sin persistencia parcial.
- **SC-003**: En pruebas de actualización parcial, el 100% de solicitudes que omiten `nombre`, `direccion` y `telefono` conservan los valores existentes en esos campos.
- **SC-004**: El 100% de intentos de asignar correo duplicado en cuentas distintas son rechazados de forma transaccional.
- **SC-005**: La suite de pruebas automatizadas del proyecto se ejecuta completa y permanece en verde tras el refactor.
