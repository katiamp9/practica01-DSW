# Quickstart — Feature 005

## Objective
Dejar listo el entorno local para integración frontend-backend sin errores de conexión, preparando la base para la siguiente feature de Login.

## Prerequisites
- Java 17 y Maven disponibles.
- Node.js `>= 20.19` y npm disponibles (requerido por Angular CLI 21).
- Backend ejecutable desde la raíz del proyecto.

## Steps
1. Levantar backend en `:8080` desde la raíz:
   - `mvn spring-boot:run`
2. Preparar frontend en `/frontend-empleados`:
   - crear o completar estructura base (`componentes`, `servicios`, `modelos`, `interceptores`).
3. Configurar proxy de desarrollo en frontend:
   - definir `proxy.conf.json` para reenviar `/api` a `http://localhost:8080`.
4. Levantar frontend en `:4200`:
   - `cd frontend-empleados && npm install`
   - `npm start`
5. Validar conectividad:
   - ejecutar una llamada de prueba del frontend a `/api/v1/**`.

## Validation Checklist
- [x] Frontend corre en `http://localhost:4200`.
- [x] Backend corre en `http://localhost:8080`.
- [x] Llamadas a `/api` no fallan por CORS (configuración backend aplicada).
- [x] Cabecera `Authorization` es aceptada en solicitudes desde frontend (configuración backend aplicada).
- [x] Seguridad Basic Auth sigue activa en endpoints protegidos.

## Validation Result (2026-03-19)
- `mvn -DskipTests package`: OK.
- `npm install` en `frontend-empleados`: OK.
- `npm run build` en `frontend-empleados` con Node `v22.22.1`: OK.
- `npm start` en `frontend-empleados` con Node `v22.22.1`: OK, sirviendo en `http://localhost:4200`.

## Expected Outcome
- Entorno de desarrollo reproducible para frontend + backend.
- Integración lista para implementar el flujo de Login en la siguiente feature.
