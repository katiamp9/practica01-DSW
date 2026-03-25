# Data Model — Feature 011

## Entity: DesignTokenGlobal

- Description: Catálogo central de variables visuales globales para experiencia “GitHub Experience”.
- Fields:
  - `colorAppBg` (`#0d1117`)
  - `colorSurface` (`#161b22`)
  - `colorBorder` (`#30363d`)
  - `colorTextPrimary` (`#c9d1d9`)
  - `colorTextSecondary` (`#8b949e`)
  - `colorPrimaryAction` (`#238636`)
  - `fontStackNative` (system-ui + Segoe UI y equivalentes)
  - `radiusBox` (`6px`)
- Validation Rules:
  - Debe existir una única fuente de verdad para estos valores.
  - Los tokens deben aplicarse por herencia a toda la SPA.

## Entity: HeaderGlobalAdmin

- Description: Componente visual superior compartido de navegación administrativa.
- Fields:
  - `logo` (visual identity element)
  - `navItems` (lista de enlaces)
  - `backgroundToken` (`#161b22`)
- Validation Rules:
  - Debe mantener estructura y estilo consistente entre pantallas.
  - El contraste de navegación debe permanecer legible sobre fondo oscuro.

## Entity: BoxContainerStyle

- Description: Patrón reutilizable de contenedor de contenido.
- Fields:
  - `background` (`#161b22`)
  - `borderColor` (`colorBorder`)
  - `borderRadius` (`radiusBox`)
- Validation Rules:
  - Debe envolver secciones de tablas y formularios principales.
  - No debe degradar espaciado interno ni legibilidad.

## Entity: ButtonStyleVariant

- Description: Variantes globales de botones para jerarquía de acciones.
- Fields:
  - `primary` (`#238636`)
  - `secondary` (oscuro neutral)
  - `semanticCritical` (error/peligro/warning)
- Validation Rules:
  - Toda acción principal debe mapear a `primary`.
  - Acciones de apoyo/neutrales deben mapear a `secondary`.
  - Los colores semánticos solo se permiten en estados críticos.

## Entity: TableVisualStyle

- Description: Reglas visuales de tablas administrativas.
- Fields:
  - `headerBg` (`#161b22`)
  - `headerWeight` (`bold`)
  - `rowDivider` (horizontal tenue)
  - `verticalGrid` (mínima/no dominante)
  - `cellPadding` (espaciado consistente)
- Validation Rules:
  - Debe priorizar separación horizontal limpia.
  - Debe evitar líneas verticales pesadas.

## Entity: BadgeConteo

- Description: Indicador compacto de conteos operativos.
- Fields:
  - `shape` (pill/círculo pequeño)
  - `background` (gris oscuro neutral)
  - `text` (conteo)
- Validation Rules:
  - Debe ser visible y no competir con títulos/acciones.
  - Debe usarse de manera consistente en Empleados y Departamentos.

## Relationships

- `DesignTokenGlobal` gobierna `HeaderGlobalAdmin`, `BoxContainerStyle`, `ButtonStyleVariant`, `TableVisualStyle` y `BadgeConteo`.
- `HeaderGlobalAdmin` y `BoxContainerStyle` se consumen por toda la SPA (incluyendo Login/Home, Empleados y Departamentos).
