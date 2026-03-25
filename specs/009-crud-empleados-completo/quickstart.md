# Quickstart — Feature 009

## Objective
Validar CRUD completo de empleados en módulo administrativo con formulario unificado, carga reactiva de departamentos, representación legible de departamento y email en tabla, confirmación de eliminación, protección de cuentas críticas y notificaciones de éxito.

## Prerequisites
- Backend activo en `http://localhost:8080`.
- Frontend Angular 21 activo en `/frontend-empleados`.
- Usuario con permisos de administración autenticado.
- Endpoints disponibles:
  - `GET /api/v1/empleados`
  - `POST /api/v1/empleados`
  - `PUT /api/v1/empleados/{clave}`
  - `DELETE /api/v1/empleados/{clave}`
  - `GET /api/v1/departamentos`

## Steps
1. Abrir módulo admin en ruta de gestión de empleados.
2. Confirmar que el placeholder fue reemplazado por vista de gestión CRUD.
3. Abrir formulario en modo crear y verificar carga reactiva de departamentos al abrir.
4. Validar que el campo Departamento se renderiza como select con nombre visible.
5. Intentar guardar con nombre vacío y verificar bloqueo por validación.
6. Intentar guardar con email no corporativo y verificar validación contextual.
7. Crear empleado válido y confirmar toast de éxito + actualización de lista.
8. Editar empleado existente usando el mismo formulario y confirmar actualización + toast.
9. Verificar en payload de create/update que se envía `departamentoId` (ID), no nombre de departamento.
10. Verificar en payload de `POST /api/v1/empleados` que NO se envía `clave`.
11. Ejecutar eliminación y confirmar diálogo previo; cancelar para validar no eliminación.
12. Repetir eliminación confirmando acción y validar borrado + toast de éxito.
13. Simular caso sin departamentos y validar mensaje de advertencia visible en formulario.
14. Confirmar en tabla que la columna Departamento muestra `nombre` (no `departamentoId`) y fallback `Sin asignar` cuando no hay coincidencia.
15. Confirmar que la columna Email muestra valor de `email` desde backend y que, si llega `null`, UI muestra `Sin correo`.
16. Abrir edición de un empleado y validar que `email` aparece precargado y en modo `readonly`.
17. Intentar forzar un cambio de `email` en `PUT /api/v1/empleados/{clave}` (por API/tooling) y validar `400 Bad Request`.
18. Validar que no se muestra botón eliminar para el registro cuyo email coincide con la sesión autenticada.
19. Intentar eliminación directa (API) del admin principal y validar respuesta `403 Forbidden`.

## Validation Checklist
- [ ] Vista de empleados reemplaza placeholder con gestión CRUD.
- [ ] Formulario standalone sirve para crear y editar.
- [ ] Campo Departamento es select con nombre visible.
- [ ] El payload de guardado envía `departamentoId`.
- [ ] El payload de `POST /api/v1/empleados` NO envía `clave`.
- [ ] Carga de departamentos usa estado reactivo con Signals al abrir formulario.
- [ ] La columna Departamento en tabla muestra nombre legible y usa fallback `Sin asignar`.
- [ ] El listado de empleados incluye `email` por contrato y la UI muestra `Sin correo` cuando viene `null`.
- [ ] En edición, `email` se precarga y se mantiene `readonly`.
- [ ] Un intento de modificar `email` en update retorna `400 Bad Request`.
- [ ] Sin departamentos disponibles, se muestra advertencia al usuario.
- [ ] Validación de nombre obligatorio funciona.
- [ ] Validación de email corporativo funciona.
- [ ] Eliminación requiere confirmación explícita previa.
- [ ] La UI no muestra acción de eliminar para el usuario autenticado en su propia fila.
- [ ] El backend responde `403 Forbidden` al intentar eliminar `admin@empresa.com`.
- [ ] Operaciones exitosas muestran toast de éxito.
- [ ] Todas las operaciones CRUD pasan por solicitudes autenticadas (interceptor).

## Performance Validation
- Medir carga de lista de empleados en 5 ejecuciones consecutivas.
- Considerar cumplimiento si al menos 95% de cargas exitosas completa en menos de 2 segundos.

## Execution Evidence (2026-03-21)
- Build frontend ejecutado con éxito:
  - `npm run build`
  - Resultado: `Application bundle generation complete`
- Test suite frontend global ejecutada con éxito:
  - `npm test -- --watch=false --browsers=ChromeHeadless`
  - Resultado: `TOTAL: 25 SUCCESS`
- Test suite frontend dirigida (US1/US2/US3) ejecutada con éxito:
  - `npm test -- --watch=false --browsers=ChromeHeadless --include src/app/componentes/admin-empleados/admin-empleados.component.spec.ts --include src/app/componentes/admin-empleados/admin-empleados-crud-flow.spec.ts`
  - `npm test -- --watch=false --browsers=ChromeHeadless --include src/app/componentes/empleado-form/empleado-form.component.spec.ts --include src/app/componentes/empleado-form/empleado-form.validations.spec.ts --include src/app/componentes/admin-empleados/admin-empleados-crud-flow.spec.ts`
  - `npm test -- --watch=false --browsers=ChromeHeadless --include src/app/componentes/admin-empleados/admin-empleados.confirm-delete.spec.ts --include src/app/componentes/admin-empleados/admin-empleados.toast.spec.ts --include src/app/componentes/admin-empleados/admin-empleados.component.spec.ts --include src/app/componentes/admin-empleados/admin-empleados-crud-flow.spec.ts`
  - Resultados: `TOTAL: 3 SUCCESS`, `TOTAL: 7 SUCCESS`, `TOTAL: 9 SUCCESS`.
- Test suite backend de eliminación protegida ejecutada con éxito:
  - pruebas unitarias + integración enfocadas en delete protegido (`EmpleadoDeleteServiceTest` y `EmpleadoControllerIntegrationTest`)
  - resultado: `passed=17 failed=0`.
- Cobertura de contrato validada en implementación:
  - Requests paginadas (`page`, `size`, `sort`) para `GET /api/v1/empleados` y `GET /api/v1/departamentos`.
  - Payload create/update enviando `departamentoId`.
  - Confirmación previa de eliminación y toast de éxito para operaciones exitosas.
  - UI sin botón eliminar para la fila del usuario autenticado.
  - Backend con respuesta `403 Forbidden` al intentar eliminar `admin@empresa.com`.

## Expected Outcome
- El administrador puede completar flujo CRUD de empleados sin salir de la vista, con validaciones claras, seguridad consistente y feedback UX inmediato.
