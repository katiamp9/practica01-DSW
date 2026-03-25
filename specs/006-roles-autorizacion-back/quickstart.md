# Quickstart — Feature 006

## Objective
Habilitar soporte de roles persistidos y autorización en backend con compatibilidad para datos existentes.

## Prerequisites
- Java 17 y Maven disponibles.
- PostgreSQL disponible (Docker Compose recomendado).
- Variables de entorno de seguridad configuradas (`SPRING_SECURITY_USER_NAME`, `SPRING_SECURITY_USER_PASSWORD_HASH`).

## Steps
1. Aplicar migraciones Flyway incluyendo la migración de roles.
2. Verificar en base de datos que:
   - empleados existentes sin rol quedaron con `ROLE_USER`;
   - administrador inicial está con `ROLE_ADMIN`.
3. Crear un empleado nuevo y confirmar rol por defecto `ROLE_USER`.
4. Ejecutar autenticación de prueba y validar que el usuario cargado contiene authority derivada de su rol.
5. Ejecutar build y pruebas automatizadas.

## Validation Checklist
- [x] Campo `rol` presente y no nulo en empleados.
- [x] Empleados existentes respaldados con `ROLE_USER`.
- [x] Administrador inicial con `ROLE_ADMIN`.
- [x] Authorities cargadas desde rol persistido.
- [x] Basic Auth continúa operativa.

## Validation Result (2026-03-19)
- `mvn test`: OK (`Tests run: 62, Failures: 0, Errors: 0, Skipped: 0`).
- Integración de seguridad validada en controladores y auth con carga de authorities por rol persistido.
- Configuración de fallback para usuario `admin` validada sin romper pruebas de slice web.

## Expected Outcome
- Modelo de roles activo en backend.
- Autorización preparada para reglas por rol sin ruptura de compatibilidad.
