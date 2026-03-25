# Quickstart — Feature 008

## Objective
Validar dashboard administrativo con navegación superior, tabla paginada de empleados y estados de error/recuperación mediante “Reintentar”.

## Prerequisites
- Backend ejecutándose en `http://localhost:8080`.
- Frontend Angular 21 en `/frontend-empleados`.
- Usuario autenticable con rol administrativo.
- Proxy de desarrollo `/api` configurado hacia backend.

## Steps
1. Iniciar backend desde la raíz del proyecto.
2. Iniciar frontend en `frontend-empleados`.
3. Iniciar sesión con credenciales de administrador.
4. Confirmar que el dashboard muestra barra superior con `Inicio`, `Empleados`, `Departamentos`.
5. Verificar carga inicial del listado con `page=0`, `size=10` y `sort=nombre,asc`.
6. Confirmar que la tabla muestra `nombre`, `clave` y `rol` por fila.
7. Validar etiqueta visual por rol (`ROLE_ADMIN`, `ROLE_USER`) y fallback visual para roles no reconocidos.
8. Usar paginación siguiente/anterior y confirmar actualización de datos sin salir del dashboard.
9. Forzar error de red en consulta de empleados y verificar mensaje claro + botón manual “Reintentar”.
10. Abrir `/admin/empleados` y `/admin/departamentos` y confirmar placeholder “Próximamente”.
11. Cerrar sesión e intentar acceso directo a rutas admin para verificar protección por guard.

## Validation Checklist
- [x] Barra de navegación admin visible con los 3 accesos requeridos.
- [x] Rutas `Empleados` y `Departamentos` muestran placeholder “Próximamente”.
- [x] Tabla inicial usa tamaño de página 10.
- [x] Tabla muestra identificador principal `clave`.
- [x] Etiquetas de rol son distinguibles visualmente.
- [x] Hover de filas funciona y mejora lectura.
- [x] Estado vacío se presenta con mensaje amigable.
- [x] Estado de error muestra acción manual “Reintentar”.
- [x] Paginación siguiente/anterior funciona de forma consistente.
- [x] Rutas admin permanecen protegidas para usuarios no autorizados.

## Performance Validation
- Ejecutar al menos 5 cargas iniciales del dashboard en entorno local estable.
- Medir tiempo desde apertura del dashboard hasta render de tabla con datos.
- Considerar cumplimiento si al menos 95% de cargas exitosas terminan en menos de 2 segundos.

## Expected Outcome
- El módulo admin ofrece navegación predecible, listado paginado legible y recuperación de errores sin recargar la aplicación completa.

## Implementation Evidence (2026-03-21)
- Se implementó `EmpleadoQueryService` con `HttpClient` y parámetros obligatorios `page`, `size`, `sort` para activar autenticación automática vía `AuthInterceptor`.
- Se implementó dashboard admin con estados `loading`, `data`, `empty` (mensaje amigable) y `error` (botón “Reintentar”).
- Se implementaron rutas protegidas `/admin/empleados` y `/admin/departamentos` con placeholders “Próximamente”.
- Se añadieron pruebas unitarias:
	- `empleado-query.service.spec.ts`
	- `admin-dashboard.component.spec.ts`
	- `admin-empleados-placeholder.component.spec.ts`
	- `admin-departamentos-placeholder.component.spec.ts`
- Validación automatizada ejecutada con éxito:
	- `npm test -- --watch=false --browsers=ChromeHeadless` → `TOTAL: 13 SUCCESS`
	- `npm run build` → compilación exitosa (`ng build`)
