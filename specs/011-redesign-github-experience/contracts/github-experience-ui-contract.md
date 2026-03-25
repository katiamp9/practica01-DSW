# Contract: Global GitHub Experience UI (Feature 011)

## Purpose

Definir el contrato visual global para la SPA inspirada en GitHub Dark Mode, asegurando aplicación consistente en Login, Home y módulos administrativos.

## Global Theming Contract

- El sistema MUST definir tokens visuales globales en `styles.css`.
- El fondo principal MUST usar `#0d1117`.
- Las superficies/tarjetas MUST usar `#161b22`.
- El borde de contenedores MUST usar `#30363d`.
- El texto principal MUST usar `#c9d1d9` y secundario `#8b949e`.
- El token de acción primaria MUST usar `#238636`.
- La tipografía global MUST usar stack nativo de sistema.
- El tema MUST ser fijo en modo oscuro (sin selector de tema).

## Header Contract

- Toda vista de la SPA MUST renderizar header/estructura oscura consistente donde aplique navegación superior.
- El header MUST incluir:
  - logo visual,
  - navegación superior limpia,
  - estado legible de enlaces.
- El header no MUST cambiar comportamiento de rutas existentes.

## Box Container Contract

- Los paneles principales MUST usar:
  - fondo `#161b22`,
  - borde `#30363d`,
  - `border-radius: 6px`.
- El patrón debe aplicarse de forma uniforme a secciones de datos y formularios.

## Button Contract

- Botones de acción principal MUST mapearse a variante verde.
- Botones secundarios/neutros MUST mapearse a variante oscura neutral.
- Colores semánticos MUST usarse solo para estados críticos (error, peligro, warning).
- La jerarquía visual MUST ser consistente en toda la SPA.

## Table Contract

- Tablas MUST usar divisores horizontales tenues.
- Tablas MUST evitar grillas verticales pesadas.
- Encabezados de tabla MUST usar fondo `#161b22` y texto en negrita.
- Padding y alineación de celdas MUST ser homogéneos en listados administrativos.

## Input Contract

- Inputs MUST usar fondo oscuro con borde definido y texto legible.
- Estados focus/disabled MUST mantener contraste suficiente en paleta dark.

## Badge Contract

- Conteos de empleados/departamentos MUST mostrarse en badges grises compactos.
- El tamaño y contraste del badge MUST preservar legibilidad sin dominar la interfaz.

## Scope Contract

- Este rediseño es visual/UX y MUST NOT modificar:
  - contratos API,
  - rutas de backend,
  - reglas de negocio,
  - políticas de autenticación.

## Compatibility Guarantees

- Mantiene consumo de endpoints versionados `/api/v1`.
- Mantiene flujos actuales de CRUD y navegación.
- Garantiza aplicación automática de estilo global en toda la SPA.
