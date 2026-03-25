# Quickstart — Feature 011

## Objective

Validar la adopción global del rediseño “GitHub Experience” Dark Mode en toda la SPA, con aplicación automática en Login, Home, Empleados y Departamentos.

## Prerequisites

- Backend activo en `http://localhost:8080`.
- Frontend Angular activo en `frontend-empleados`.
- Usuario administrativo autenticado.
- Build frontend funcional sin errores de compilación.

## Steps

1. Abrir la aplicación y autenticar sesión administrativa.
2. Verificar en Login y Home que el fondo principal usa `#0d1117`, texto principal `#c9d1d9` y texto secundario `#8b949e`.
3. Navegar a dashboard admin y verificar header oscuro consistente con logo y enlaces limpios.
4. Confirmar que no existe selector de tema (Dark Mode fijo).
5. Ir a módulo de Empleados y confirmar:
   - contenedores tipo box (`#161b22`, borde `#30363d`, radio 6px),
   - botones primarios en `#238636` y secundarios oscuros,
   - tabla con divisores horizontales sutiles y header en negrita,
   - badges de conteo grises compactos.
6. Ir a módulo de Departamentos y confirmar mismos patrones visuales globales.
7. Validar inputs (buscar, formularios) con fondo oscuro y borde definido.
8. Comparar módulos y verificar herencia de tokens globales sin desalineaciones.
9. Ejecutar una acción CRUD de prueba en cada módulo para validar que el rediseño no afecta funcionalidad.
10. Revisar estados vacíos/errores/warning para confirmar que solo estados críticos usan color semántico.

## Validation Checklist

- [ ] Login y Home usan fondo `#0d1117` con tipografía legible.
- [ ] Header oscuro global visible en vistas administrativas objetivo.
- [ ] Logo y navegación superior se mantienen legibles y consistentes.
- [ ] Empleados usa contenedores tipo box con fondo `#161b22`, borde `#30363d` y radio 6px.
- [ ] Departamentos usa contenedores tipo box con fondo `#161b22`, borde `#30363d` y radio 6px.
- [ ] Botones primarios se muestran en verde `#238636`.
- [ ] Botones secundarios se muestran en tonos oscuros consistentes.
- [ ] Tablas conservan divisores horizontales tenues y sin verticales pesadas.
- [ ] Encabezados de tabla muestran fondo `#161b22` y texto en negrita.
- [ ] Badges de conteo se muestran grises y compactos.
- [ ] Inputs muestran fondo oscuro con borde definido y contraste correcto.
- [ ] Tipografía global usa stack nativo de sistema.
- [ ] No existe toggle/selector de tema visual.
- [ ] Solo estados críticos usan color semántico.
- [ ] No hay cambios de comportamiento en flujos CRUD y navegación.

## Expected Outcome

La SPA presenta una experiencia visual coherente de GitHub Dark Mode en todas las vistas objetivo, manteniendo intacta la funcionalidad existente.
