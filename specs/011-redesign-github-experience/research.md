# Phase 0 Research — Feature 011

## Scope

Definir lineamientos de diseño para un rediseño Dark Mode global inspirado en GitHub, con aplicación automática en toda la SPA.

## Decision 1: Tokenización Dark Mode global en `styles.css`

- Decision: Centralizar tokens de Dark Mode oficial (`#0d1117`, `#161b22`, `#30363d`, `#c9d1d9`, `#8b949e`, `#238636`) en variables CSS globales.
- Rationale: Permite herencia automática para toda la SPA y evita divergencias entre módulos.
- Alternatives considered:
  - Estilos por componente: descartado por inconsistencia y deuda de mantenimiento.
  - Librería externa de tema completa: descartado para evitar sobrealcance y ruptura visual.

## Decision 2: Dark Mode fijo sin selector de tema

- Decision: Operar exclusivamente en Dark Mode, sin interruptor Light/Dark.
- Rationale: Cumple la decisión de negocio y simplifica mantenimiento visual.
- Alternatives considered:
  - Toggle manual de tema: descartado por no alinearse al alcance aprobado.
  - Detección automática por sistema operativo: descartado por introducir ambigüedad de experiencia.

## Decision 3: Header oscuro como patrón compartido

- Decision: Definir un patrón único de header con tono base `#24292f`, logo y enlaces limpios.
- Rationale: El encabezado unifica identidad y navegación transversal.
- Alternatives considered:
  - Mantener múltiples headers por módulo: descartado por fragmentación visual.

## Decision 4: Contenedores tipo Box en paleta Dark

- Decision: Estandarizar paneles en fondo `#161b22`, borde `#30363d` y radio `6px`.
- Rationale: Mantiene jerarquía visual y contraste correcto en entornos oscuros.
- Alternatives considered:
  - Superficies totalmente planas sin borde: descartado por pérdida de separación visual.

## Decision 5: Jerarquía de botones, inputs y tablas

- Decision: Botón primario en `#238636`, secundarios oscuros; inputs oscuros con borde definido; encabezados de tabla con fondo `#161b22` y texto en negrita.
- Rationale: Preserva affordance y legibilidad bajo Dark Mode.
- Alternatives considered:
  - Monocromía total sin color de acción: descartado por pérdida de jerarquía de acciones.

## Decision 6: Política de color semántico restringida

- Decision: Limitar colores semánticos a estados críticos (error, peligro, warning).
- Rationale: Reduce ruido visual manteniendo señales de riesgo necesarias.
- Alternatives considered:
  - Semántica de color extendida para estados no críticos: descartado por romper uniformidad dark.

## Decision 7: Badges de conteo compactos

- Decision: Usar badges grises pequeños para conteos de empleados/departamentos.
- Rationale: Mantiene información visible sin competir con contenido principal.
- Alternatives considered:
  - Chips coloridos por módulo: descartado por romper uniformidad “GitHub Experience”.

## Decision 8: Aplicación incremental sin ruptura funcional

- Decision: Mantener estructura y lógica de componentes actuales, sustituyendo solo capa visual global y estilos locales conflictivos.
- Rationale: Reduce riesgo y acelera entrega verificable.
- Alternatives considered:
  - Reescritura total de componentes: descartado por costo/riesgo sin valor funcional adicional.

## Clarifications Status

- No hay `NEEDS CLARIFICATION` activos en la especificación.
- Alcance confirmado: Dark Mode global para toda la SPA, sin cambios de negocio/API.
