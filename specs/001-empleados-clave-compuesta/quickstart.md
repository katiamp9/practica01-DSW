# Quickstart: Clave Compuesta Empleados

## Prerrequisitos
- Java 17
- Maven (o wrapper del proyecto)
- Docker y Docker Compose

## 1) Levantar base de datos local
```bash
docker compose -f docker/compose/docker-compose.yml up -d postgres
```

## 2) Configurar variables de entorno
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/empleados`
- `SPRING_DATASOURCE_USERNAME=empleados_user`
- `SPRING_DATASOURCE_PASSWORD=empleados_pass`
- `SPRING_SECURITY_USER_NAME=admin`
- `SPRING_SECURITY_USER_PASSWORD=admin123`

## 3) Ejecutar aplicación
```bash
./mvnw spring-boot:run
```

## 4) Validar contrato
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI: `specs/001-empleados-clave-compuesta/contracts/empleados.openapi.yaml`

## 5) Probar flujo funcional mínimo
1. Crear empleado: `POST /api/v1/empleados`
2. Listar empleados: `GET /api/v1/empleados?page=0&sort=nombre,asc`
3. Consultar por clave: `GET /api/v1/empleados/{clave}`
4. Actualizar empleado: `PUT /api/v1/empleados/{clave}`
5. Eliminar empleado: `DELETE /api/v1/empleados/{clave}`

## 6) Validaciones esperadas
- Toda alta válida devuelve clave con patrón `EMP-<numero>`.
- Payload de alta con `clave` enviada manualmente se rechaza con `400`.
- Parámetros de listado inválidos (`page`, `size`, `sort`) se rechazan con `422`.
- Operaciones sobre clave inexistente devuelven `404`.
- Colisiones de consecutivo no recuperables devuelven `409`.

## 7) Ejecutar validación automatizada
```bash
./mvnw test
```
