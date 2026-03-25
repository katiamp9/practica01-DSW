# Feature Specification: Rediseño Global "GitHub Experience"

**Feature Branch**: `011-redesign-github-experience`  
**Created**: 2026-03-25  
**Status**: Draft  
**Input**: User description: "Feature 011: Rediseño Global GitHub Experience"

## Clarifications

### Session 2026-03-25

- Q: ¿Cuál será el alcance del Dark Mode? → A: Toda la SPA (incluyendo Login/Home y vistas futuras).
- Q: ¿Cómo se gestionará el tema visual? → A: Dark Mode fijo (sin interruptor Light/Dark).
- Q: ¿Qué política de color semántico aplicará el Dark Mode? → A: Base Dark Mode GitHub + colores semánticos solo para estados críticos.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Navegación principal estilo GitHub (Priority: P1)

Como usuario administrativo, quiero un encabezado oscuro y limpio al estilo GitHub para orientarme rápidamente entre secciones y percibir una interfaz consistente.

**Why this priority**: El encabezado es el punto de entrada visual y de navegación; define la identidad del rediseño desde el primer segundo.

**Independent Test**: Puede validarse abriendo cualquier pantalla principal y verificando header con fondo oscuro, logo y enlaces de navegación legibles y consistentes.

**Acceptance Scenarios**:

1. **Given** un usuario autenticado en la aplicación, **When** carga una vista administrativa, **Then** ve un header oscuro con navegación superior limpia y logo visible.
2. **Given** el usuario navega entre Empleados y Departamentos, **When** cambia de sección, **Then** la estructura del header se mantiene consistente sin variaciones de estilo inesperadas.

---

### User Story 2 - Contenedores y componentes visuales unificados (Priority: P1)

Como usuario administrativo, quiero que los bloques de contenido usen cajas oscuras, bordes definidos y componentes homogéneos para leer datos más rápido y con menor fatiga visual.

**Why this priority**: El flujo operativo principal ocurre en tablas, formularios y acciones; una base visual unificada impacta directamente en productividad.

**Independent Test**: Puede validarse inspeccionando las pantallas de Empleados y Departamentos para confirmar contenedores tipo "box", botones primarios/secundarios, tablas limpias y badges de conteo coherentes.

**Acceptance Scenarios**:

1. **Given** una vista de listado con datos, **When** se renderiza la pantalla, **Then** los paneles aparecen en cajas `#161b22` con borde `#30363d` y esquinas redondeadas de 6px.
2. **Given** acciones principales y secundarias visibles, **When** el usuario revisa botones, **Then** identifica primarios en verde GitHub `#238636` y secundarios en tonos oscuros consistentes.
3. **Given** una tabla de empleados o departamentos, **When** se muestran filas y columnas, **Then** predomina separación horizontal sutil sin líneas verticales pesadas.
4. **Given** valores de conteo relevantes, **When** se muestran badges, **Then** aparecen como indicadores grises pequeños y legibles.

---

### User Story 3 - Sistema global de estilos reutilizable (Priority: P2)

Como equipo de producto, queremos un sistema global de tokens visuales para que Empleados y Departamentos hereden automáticamente la experiencia GitHub sin ajustes manuales repetitivos.

**Why this priority**: Evita deuda visual, reduce divergencias entre módulos y simplifica mantenimiento del rediseño.

**Independent Test**: Puede validarse aplicando cambios de estilo global una sola vez y comprobando que ambos módulos reflejan la actualización sin reglas duplicadas por pantalla.

**Acceptance Scenarios**:

1. **Given** variables de estilo globales definidas, **When** se abre Empleados y Departamentos, **Then** ambos módulos comparten tipografía, colores base, contenedores y componentes visuales.
2. **Given** una actualización de token global, **When** se recompila la interfaz, **Then** el cambio se refleja de forma uniforme en los dos módulos objetivo.

---

### Edge Cases

- Pantallas con tablas vacías deben conservar estructura visual sin colapsar espaciados ni contenedores.
- Títulos largos o textos de navegación extensos no deben romper el header ni superponer acciones.
- Diferencias de resolución (desktop/laptop) deben mantener legibilidad de botones, tablas y badges.
- Elementos heredados con estilos previos no deben sobrescribir la capa visual global definida para esta experiencia.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST aplicar un rediseño visual global con identidad "GitHub Experience" en toda la SPA.
- **FR-002**: El layout principal MUST incluir un header oscuro con navegación superior limpia y logo visible.
- **FR-003**: El fondo principal de la aplicación MUST usar `#0d1117` como tono base Dark Mode.
- **FR-004**: Los contenedores de contenido MUST usar diseño tipo box con fondo `#161b22`, borde fino `#30363d` y esquinas de 6px.
- **FR-005**: La tipografía global MUST usar una pila de fuentes nativa de sistema con comportamiento consistente en toda la aplicación.
- **FR-006**: Los botones de acción principal MUST mostrarse en verde GitHub `#238636` y los secundarios en tonos oscuros de la paleta base.
- **FR-007**: Las tablas MUST priorizar divisores horizontales suaves y evitar líneas verticales pesadas.
- **FR-008**: Los conteos de empleados y departamentos MUST representarse con badges grises pequeños y legibles.
- **FR-009**: El sistema de estilos global MUST aplicarse automáticamente a todos los módulos y vistas de la SPA (incluyendo Login y Home).
- **FR-010**: Las reglas visuales globales MUST centralizarse en una capa común para evitar duplicación por componente.
- **FR-011**: La referencia visual objetivo del rediseño MUST alinearse con la experiencia de la página de repositorios de GitHub.
- **FR-012**: El rediseño MUST mantener la navegabilidad y operaciones actuales sin cambiar el alcance funcional de negocio.
- **FR-013**: La aplicación MUST operar en Dark Mode fijo y no MUST exponer selector de tema Light/Dark al usuario.
- **FR-014**: La paleta MUST usar base Dark Mode GitHub y limitar colores semánticos a estados críticos (error, peligro, warning).
- **FR-015**: El texto principal MUST usar `#c9d1d9` y el texto secundario MUST usar `#8b949e`.
- **FR-016**: Inputs y campos de texto MUST usar fondo oscuro con borde definido y los encabezados de tabla MUST usar fondo `#161b22` con texto en negrita.

### Key Entities *(include if feature involves data)*

- **HeaderGlobalAdmin**: Estructura visual superior compartida con logo, navegación y estilo oscuro consistente.
- **DesignTokenGlobal**: Conjunto de variables visuales de color, borde, radio, tipografía y componentes reutilizables.
- **BoxContainerStyle**: Patrón de panel de contenido reutilizable para tablas, formularios y bloques administrativos.
- **BadgeConteo**: Indicador compacto de recuento para métricas operativas en módulos administrativos.

## Assumptions

- El rediseño solicitado es visual/UX y no modifica reglas de negocio ni contratos de API.
- La experiencia objetivo prioriza vista de escritorio administrativa en primera iteración.
- Empleados y Departamentos son módulos críticos de validación, sin excluir la cobertura del resto de vistas de la SPA.
- Las decisiones de layout se aplican sobre componentes existentes, manteniendo rutas y flujos actuales.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de vistas objetivo de la SPA muestra el tema oscuro consistente según los tokens globales definidos.
- **SC-002**: El 100% de módulos críticos (Empleados y Departamentos) usa contenedores tipo box con borde y radio definidos, manteniendo consistencia con el tema global.
- **SC-003**: El 100% de botones principales y secundarios cumple la codificación visual definida para distinguir jerarquía de acción.
- **SC-004**: Al menos 90% de usuarios de validación interna identifica visualmente la nueva experiencia como consistente en su primer recorrido.
- **SC-005**: Al menos 90% de evaluaciones internas reporta mejora de legibilidad en tablas y badges frente a la versión previa.
- **SC-006**: El 100% de vistas objetivo no presenta controles de cambio de tema y mantiene Dark Mode activo de forma consistente.
- **SC-007**: El 100% de estados críticos visibles usa colores semánticos permitidos, y los estados no críticos permanecen en paleta base Dark Mode.
- **SC-008**: El 100% de vistas objetivo aplica los tokens globales de color definidos (`#0d1117`, `#161b22`, `#30363d`, `#c9d1d9`, `#8b949e`, `#238636`) sin hardcodes contradictorios.
