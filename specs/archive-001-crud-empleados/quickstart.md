# Quickstart: CRUD de Empleados

## Prerrequisitos
- Java 17
- Maven o Gradle con wrapper del proyecto
- Docker y Docker Compose

## 1) Levantar PostgreSQL local
```bash
docker compose -f docker/compose/docker-compose.yml up -d postgres
```

## 2) Configurar credenciales y variables
Definir variables por entorno (ejemplo):
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/empleados`
- `SPRING_DATASOURCE_USERNAME=empleados_user`
- `SPRING_DATASOURCE_PASSWORD=empleados_pass`
- `SPRING_SECURITY_USER_NAME=admin`
- `SPRING_SECURITY_USER_PASSWORD=admin123`

## 3) Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

## 4) Verificar contrato API
- Swagger UI (entorno dev): `http://localhost:8080/swagger-ui/index.html`
- Contrato fuente: `specs/archive-001-crud-empleados/contracts/empleados.openapi.yaml`

## 5) Probar flujo mínimo CRUD
1. Crear empleado (`POST /api/v1/empleados`)
2. Listar empleados (`GET /api/v1/empleados?page=0&sort=nombre,asc`)
3. Consultar por clave (`GET /api/v1/empleados/{clave}`)
4. Actualizar (`PUT /api/v1/empleados/{clave}`)
5. Eliminar (`DELETE /api/v1/empleados/{clave}`)

### Parámetros de paginación y ordenamiento para listados
- Todo `GET` de colecciones usa estándar `Pageable`.
- Parámetros obligatorios: `page`, `sort`.
- Parámetro opcional: `size` (default `20`, máximo `100`).
- Campos permitidos en `sort`: `clave`, `nombre`, `direccion`, `telefono`.
- Ejemplo alterno de ordenamiento múltiple:
	`GET /api/v1/empleados?page=0&size=10&sort=nombre,asc&sort=clave,desc`

## 6) Ejecutar pruebas
```bash
./mvnw test
```

## 7) Validaciones mínimas esperadas
- Generación de `clave` automática con formato `EMP-<numero>` en altas válidas.
- Rechazo de `clave` enviada manualmente en alta con `400`.
- Rechazo de campos vacíos o >100 caracteres con `400`.
- Operaciones sobre clave inexistente con `404`.
- Parámetros inválidos de paginación/ordenamiento (`page`, `size`, `sort`) con `422`.
