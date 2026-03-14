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
- `SPRING_SECURITY_USER_PASSWORD=admin123`

## 3) Ejecutar aplicación con migraciones
```bash
./mvnw spring-boot:run
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
  -d '{"email":"empleado@example.com","password":"password-correcto"}'
```

Esperado: `200 OK` con `authenticated=true`.

## 6) Probar login fallido
```bash
curl -i -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"empleado@example.com","password":"incorrecto"}'
```

Esperado: `401 Unauthorized` con mensaje `Invalid email or password`.

## 7) Verificar seguridad y auditoría
- Confirmar que no se almacena ninguna contraseña en texto plano.
- Confirmar que cada intento (éxito/fallo) genera evento en `AuthenticationAuditService`.
- Confirmar que endpoints CRUD existentes siguen protegidos por Basic Auth.

## 8) Ejecutar validación automatizada
```bash
./mvnw test
```
