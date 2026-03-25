# Quickstart — Feature 007

## Objective
Validar login frontend con redirección por rol, guard de rutas y autenticación Basic Auth hacia backend.

## Prerequisites
- Backend ejecutándose en `http://localhost:8080`.
- Frontend Angular 21 en `/frontend-empleados` con Node compatible.
- Proxy de desarrollo `/api` activo hacia backend local.

## Steps
1. Iniciar backend desde la raíz del proyecto.
2. Iniciar frontend en `frontend-empleados`.
3. Abrir pantalla de login en el navegador.
4. Probar login con usuario `ROLE_ADMIN` y verificar redirección a `/admin-dashboard`.
5. Probar login con usuario `ROLE_USER` y verificar redirección a `/home`.
6. Intentar acceso manual a ruta no permitida para cada rol y verificar bloqueo por guard.
7. Verificar en llamadas de red que el header `Authorization` se envía en sesión activa.
8. Forzar sesión inválida (limpiar/alterar storage) e intentar ruta privada para validar limpieza y regreso a login.
9. Medir tiempo de login exitoso (clic en enviar → render de vista destino) y registrar resultado.

## Validation Checklist
- [ ] El formulario valida campos obligatorios y formato de correo.
- [ ] Login exitoso redirige por rol correcto.
- [ ] Login fallido muestra feedback sin autenticar sesión.
- [ ] AuthGuard bloquea rutas privadas sin sesión.
- [ ] AuthGuard bloquea rutas privadas por rol incorrecto.
- [ ] AuthInterceptor inyecta `Authorization` con sesión activa.
- [ ] Sesión inválida limpia storage y redirige a login.
- [ ] Login exitoso (ROLE_ADMIN y ROLE_USER) completa redirección en menos de 20 segundos.

## Performance Validation
- Repetir 5 intentos por rol (`ROLE_ADMIN`, `ROLE_USER`) en entorno local estable.
- Registrar tiempo por intento desde envío de formulario hasta render de ruta final.
- Considerar criterio cumplido si el 95% de intentos quedan por debajo de 20s.

## Expected Outcome
- Experiencia de login profesional y consistente.
- Navegación privada protegida por autenticación+rol.
- Integración frontend-backend alineada con Basic Auth existente.

## Implementation Evidence (2026-03-19)
- Implementación completada en frontend para rutas `/login`, `/admin-dashboard`, `/home` con `AuthService` basado en Signals y persistencia en `localStorage`.
- Tests unitarios creados: `auth.service.spec.ts`, `auth.guard.spec.ts`, `auth.interceptor.spec.ts`.
- Resultado de ejecución en entorno actualizado:
	- Node `v22.22.1` + npm `v10.9.4`.
	- `npm run build` ejecutado con éxito.
	- `npm test -- --watch=false --browsers=ChromeHeadless` ejecutado con éxito (`TOTAL: 7 SUCCESS`).
	- Se añadieron dependencias de test para runner Karma en `package.json`: `karma`, `karma-chrome-launcher`, `karma-jasmine`, `karma-jasmine-html-reporter`, `karma-coverage`.
