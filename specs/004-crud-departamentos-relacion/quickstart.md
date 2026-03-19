# Quickstart: Funcionalidad 004 - CRUD Departamentos y Relación Obligatoria

## Prerrequisitos
- Java 17
- Maven
- Docker + Docker Compose
- Variables de entorno de seguridad configuradas (`SPRING_SECURITY_USER_NAME`, `SPRING_SECURITY_USER_PASSWORD_HASH`, `INITIAL_ADMIN_PASSWORD_HASH`)

## 1) Reset de base de datos y arranque PostgreSQL

```bash
docker compose -f docker/compose/docker-compose.yml down -v
docker compose -f docker/compose/docker-compose.yml up -d postgres
```

Nota: este reset es obligatorio para aplicar `V4`/`V5` sin residuos de esquemas previos.

## 2) Ejecutar aplicación y migraciones V4/V5

```bash
mvn spring-boot:run
```

Resultado esperado:
- Flyway ejecuta `V4__create_departamentos_table.sql`
- Flyway ejecuta `V5__insert_default_departments.sql`
- Tabla `empleados` contiene `departamento_id` NOT NULL

## 3) Verificar semillas iniciales de departamentos

Consulta esperada (por API o SQL) con al menos:
- Sistemas
- RH
- Ventas

## 4) Probar CRUD de departamentos

### Crear departamento
```bash
curl -i -X POST http://localhost:8080/api/v1/departamentos \
  -u admin:admin123 \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Finanzas"}'
```

### Listar departamentos
```bash
curl -i -X GET "http://localhost:8080/api/v1/departamentos?page=0&size=20&sort=nombre,asc" -u admin:admin123
```

### Actualizar departamento
```bash
curl -i -X PUT http://localhost:8080/api/v1/departamentos/1 \
  -u admin:admin123 \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Sistemas TI"}'
```

### Eliminar departamento
```bash
curl -i -X DELETE http://localhost:8080/api/v1/departamentos/4 -u admin:admin123
```

## 5) Probar creación de empleado con departamento obligatorio

### Caso válido
```bash
curl -i -X POST http://localhost:8080/api/v1/empleados \
  -u admin:admin123 \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Ana","direccion":"Calle 1","telefono":"555-0101","departamentoId":1}'
```

### Caso inválido (sin `departamentoId`)
```bash
curl -i -X POST http://localhost:8080/api/v1/empleados \
  -u admin:admin123 \
  -H 'Content-Type: application/json' \
  -d '{"nombre":"Ana","direccion":"Calle 1","telefono":"555-0101"}'
```

## 6) Probar actualización parcial de empleado

### Solo activar acceso (sin tocar nombre/dirección/teléfono/departamento)
```bash
curl -i -X PUT http://localhost:8080/api/v1/empleados/EMP-1001 \
  -u admin:admin123 \
  -H 'Content-Type: application/json' \
  -d '{"email":"empleado@empresa.com","password":"claveSegura"}'
```

### Caso inválido: `password` sin `email`
```bash
curl -i -X PUT http://localhost:8080/api/v1/empleados/EMP-1001 \
  -u admin:admin123 \
  -H 'Content-Type: application/json' \
  -d '{"password":"claveSegura"}'
```

Resultado esperado: `400 BAD_REQUEST` con mensaje `si envías password también debes enviar email`.

### Actualizar solo departamento
```bash
curl -i -X PUT http://localhost:8080/api/v1/empleados/EMP-1001 \
  -u admin:admin123 \
  -H 'Content-Type: application/json' \
  -d '{"departamentoId":2}'
```

## 7) Validar quality gate

```bash
mvn -DskipTests package
mvn test
```

Ejecución registrada (2026-03-19):
- `mvn -DskipTests package` ✅ BUILD SUCCESS
- `mvn test` ✅ 75 tests passed, 0 failed

## 8) Contrato API
- Archivo: `specs/004-crud-departamentos-relacion/contracts/departamentos-empleados.openapi.yaml`
- Swagger UI local: `http://localhost:8080/swagger-ui/index.html`
