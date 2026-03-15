# Release Checklist - Spec 003

## Estado general
- [x] Implementación funcional de US1, US2 y US3 completada
- [x] Tests automáticos en verde (`34 passed, 0 failed`)
- [x] Contrato OpenAPI sincronizado con comportamiento real
- [x] Quickstart actualizado con evidencia y troubleshooting
- [x] Tasks de la Spec 003 marcadas como completadas

## Validaciones técnicas
- [x] Endpoint público de login definido en `/api/v1/auth/login`
- [x] Respuesta de éxito exacta: `{"authenticated": true}`
- [x] Respuesta de error uniforme en login fallido (`401`, sin filtrar causa)
- [x] PasswordEncoder en BCrypt
- [x] Sin fallback de contraseñas en claro en SecurityConfig
- [x] Persistencia de credenciales solo en hash
- [x] Unicidad de correo case-insensitive en creación de cuenta
- [x] Relación `CuentaEmpleado -> Empleado` por `empleado_clave -> clave`

## Gate de Arranque (Constitución)
- [x] Build: `mvn -DskipTests package`
- [x] Test suite: `mvn test`
- [x] Docker Compose Postgres: `sudo docker compose -f docker/compose/docker-compose.yml up -d postgres`

> Nota: en este entorno, el comando sin `sudo` sigue fallando por permisos de socket Docker.

## Acciones finales para cierre oficial
1. (Opcional) Corregir permisos de Docker para evitar `sudo` en el flujo normal.
2. Ejecutar una última vez: `mvn test`.
3. Confirmar archivos finales de Spec 003:
   - `specs/003-003-auth-empleado-login/spec.md`
   - `specs/003-003-auth-empleado-login/plan.md`
   - `specs/003-003-auth-empleado-login/tasks.md`
   - `specs/003-003-auth-empleado-login/quickstart.md`
   - `specs/003-003-auth-empleado-login/contracts/auth.openapi.yaml`
   - `specs/003-003-auth-empleado-login/release-checklist.md`
4. Commit final y push.

## Comando sugerido de commit
```bash
git add .
git commit -m "feat(auth): complete spec 003 login/auth hardening and release docs"
git push origin 003-003-auth-empleado-login
```
