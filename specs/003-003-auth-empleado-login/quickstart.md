# Quickstart: Autenticación de empleados por correo y contraseña

## Prerrequisitos
- Java 17
- Maven (o wrapper del proyecto)
- Docker y Docker Compose

## 1) Levantar PostgreSQL local
```bash
docker compose -f docker/compose/docker-compose.yml up -d postgres
```

## 2) Configurar variables de entorno
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/empleados`
- `SPRING_DATASOURCE_USERNAME=empleados_user`
- `SPRING_DATASOURCE_PASSWORD=empleados_pass`
- `SPRING_SECURITY_USER_NAME=admin`
- `SPRING_SECURITY_USER_PASSWORD_HASH=<hash BCrypt para Basic Auth técnico>`
- `INITIAL_ADMIN_PASSWORD_HASH=<hash BCrypt para admin@empresa.com>`

## 3) Ejecutar aplicación con migraciones
```bash
mvn spring-boot:run
```

Verificar que Flyway aplica `V3__...` para crear:
- `cuentas_empleado`
- `credenciales_empleado`

## 4) Consultar contrato de autenticación
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Contrato OpenAPI: `specs/003-003-auth-empleado-login/contracts/auth.openapi.yaml`

## 5) Probar login exitoso
```bash
curl -i -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@empresa.com","password":"admin123"}'
```

Esperado: `200 OK` con body exacto:

```json
{"authenticated": true}
```

## 6) Probar login fallido
```bash
curl -i -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"noexiste@empresa.com","password":"incorrecto"}'
```

Esperado: `401 Unauthorized` con mensaje uniforme (sin revelar si existe el correo):

```json
{"code":"AUTH_INVALID_CREDENTIALS","message":"Invalid email or password"}
```

## 7) Verificar seguridad y auditoría
- Confirmar que no se almacena ninguna contraseña en texto plano.
- Confirmar que cada intento (éxito/fallo) genera evento en `AuthenticationAuditService`.
- Confirmar que endpoints CRUD existentes siguen protegidos por Basic Auth.

## 8) Ejecutar validación automatizada
```bash
mvn test
```

## 9) Evidencia de ejecución (Gate de Arranque)
Ejecución registrada el **2026-03-14**:

1. Build

```bash
mvn -DskipTests package
```

Resultado: ✅ `BUILD SUCCESS`

2. Tests

```bash
mvn test
```

Resultado: ✅ `34 passed, 0 failed`

3. Docker Compose (PostgreSQL)

```bash
docker compose -f docker/compose/docker-compose.yml up -d postgres
```

Resultado: ✅ verificado manualmente en entorno local con:

```bash
sudo docker compose -f docker/compose/docker-compose.yml up -d postgres
```

Nota: en este entorno, sin `sudo`, puede persistir error de permisos de socket Docker.

## Troubleshooting

### Problema: `pgcrypto` falla al levantar por primera vez
Síntomas comunes:
- Error al aplicar migración V3 indicando que `gen_random_uuid()` no existe.
- Base arrancó, pero Flyway falla en `CREATE EXTENSION pgcrypto`.

Pasos recomendados:
1. Verifica que el contenedor PostgreSQL esté arriba:

```bash
docker compose -f docker/compose/docker-compose.yml ps
```

2. Si Docker está activo, crea/extiende manualmente en la base:

```bash
docker compose -f docker/compose/docker-compose.yml exec -T postgres \
  psql -U empleados_user -d empleados -c "CREATE EXTENSION IF NOT EXISTS pgcrypto;"
```

3. Reinicia la app para que Flyway reintente la migración.

Si falla el paso `docker compose` por permisos de socket:
- Ejecuta temporalmente con `sudo`, o
- agrega tu usuario al grupo `docker` y reabre sesión:

```bash
sudo usermod -aG docker "$USER"
```

### Cómo generar hash BCrypt si Paola cambia la contraseña inicial

Opción A (comando simple en Linux/macOS con `htpasswd`):

```bash
htpasswd -bnBC 10 "" "NuevaClaveSegura" | tr -d ':\n'
```

Usa el resultado en:
- `INITIAL_ADMIN_PASSWORD_HASH`
- `SPRING_SECURITY_USER_PASSWORD_HASH` (si también quieres cambiar el usuario técnico Basic Auth)

Opción B (herramienta online):
- usar un generador BCrypt confiable y pegar la contraseña en local.
- seleccionar costo `10`.
- validar que el hash resultante comience con `$2a$`, `$2b$` o `$2y$`.

Después de actualizar variables, reinicia aplicación para aplicar cambios.
