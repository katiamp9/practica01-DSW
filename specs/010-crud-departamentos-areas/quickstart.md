# Quickstart — Feature 010

## Objective

Validar CRUD de departamentos con columna `Personal`, búsqueda local reactiva, modal reutilizable y delete protegido por integridad.

## Prerequisites

- Backend Spring Boot activo en `http://localhost:8080`.
- Frontend Angular activo en `frontend-empleados`.
- Usuario con rol administrativo autenticado.
- Endpoints disponibles:
  - `GET /api/v1/departamentos`
  - `POST /api/v1/departamentos`
  - `PUT /api/v1/departamentos/{id}`
  - `DELETE /api/v1/departamentos/{id}`

## Steps

1. Abrir la pantalla de gestión de departamentos como administrador.
2. Verificar carga inicial de tabla con columnas `Nombre`, `Personal` y `Acciones`.
3. Confirmar que `Personal` muestra conteo por fila (`totalEmpleados`) incluyendo `0` cuando no hay empleados.
4. Escribir texto en barra de búsqueda y validar filtrado local instantáneo por `nombre`.
5. Validar nota visual indicando que el filtro aplica sobre la página cargada cuando hay búsqueda activa.
6. Abrir modal en modo crear, guardar un departamento válido y confirmar actualización de tabla + toast de éxito.
7. Abrir modal en modo editar, modificar nombre y confirmar refresco de lista + toast de éxito.
8. Con cambios sin guardar en modal, intentar cerrar/cancelar y confirmar diálogo de descarte.
9. Intentar borrar un departamento con `Personal = 0` y confirmar `204` + actualización de tabla.
10. Intentar borrar un departamento con `Personal > 0` y confirmar:
   - botón eliminar deshabilitado,
   - tooltip explicativo visible,
   - si se fuerza llamada al backend, respuesta `409` y toast:
  `No se puede eliminar: existen empleados asociados.`.

## Validation Checklist

- [ ] Listado paginado de departamentos funcional con `page`, `size`, `sort`.
- [ ] Columna `Personal` renderiza `totalEmpleados` correctamente.
- [ ] Create con modal funciona sin recarga completa.
- [ ] Update con modal funciona sin recarga completa.
- [ ] Búsqueda local por `nombre` funciona sin solicitudes extra.
- [ ] Con búsqueda activa se muestra nota de alcance sobre la página cargada.
- [ ] Delete exitoso (`204`) solo para departamentos sin empleados.
- [ ] Delete protegido (`409`) mapeado a toast informativo requerido.
- [ ] Botón eliminar se deshabilita visualmente cuando `totalEmpleados > 0`.
- [ ] Tooltip explicativo aparece en filas bloqueadas.
- [ ] Modal confirma descarte cuando existen cambios sin guardar.
- [ ] Solicitudes protegidas pasan por `AuthInterceptor`.

## Expected Outcome

El módulo de departamentos permite gestión completa con UX fluida, visibilidad inmediata del personal asociado y prevención robusta de borrados inválidos por integridad.

## Execution Evidence (2026-03-24)

- Validación automatizada frontend enfocada de US1/US2/US3 de departamentos:
  - `npm test -- --watch=false --browsers=ChromeHeadless --include src/app/componentes/admin-departamentos/admin-departamentos.component.spec.ts --include src/app/componentes/admin-departamentos/admin-departamentos.search.spec.ts --include src/app/componentes/admin-departamentos/admin-departamentos.delete-protected.spec.ts --include src/app/componentes/departamento-form-modal/departamento-form-modal.component.spec.ts`
  - Resultado: `TOTAL: 8 SUCCESS`.
- Validación automatizada backend enfocada de servicio/controlador de departamentos:
  - `DepartamentoServiceTest` + `DepartamentoControllerIntegrationTest`
  - Resultado: `passed=22 failed=0`.
