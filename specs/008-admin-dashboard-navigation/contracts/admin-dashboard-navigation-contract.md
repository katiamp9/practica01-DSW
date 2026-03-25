# Contract: Admin Dashboard Navigation & Employee List (Feature 008)

## Purpose
Definir el contrato funcional entre frontend y backend para navegación administrativa, listado paginado de empleados y estados de error/recuperación en dashboard.

## Protected Route Contract
- Todas las rutas administrativas MUST requerir sesión autenticada y rol autorizado.
- Rutas mínimas cubiertas por esta feature:
  - `Inicio` (dashboard admin principal)
  - `/admin/empleados` (placeholder)
  - `/admin/departamentos` (placeholder)
- Si no hay sesión válida o rol permitido, frontend MUST redirigir a `/login` y limpiar estado de sesión inválido.

## Top Navigation Contract
- El dashboard MUST renderizar una barra superior con items:
  - `Inicio`
  - `Empleados`
  - `Departamentos`
- Los links MUST resolver exactamente a:
  - `Inicio` → dashboard admin principal
  - `Empleados` → `/admin/empleados`
  - `Departamentos` → `/admin/departamentos`
- Las rutas placeholder MUST mostrar texto visible “Próximamente”.

## Employee List API Contract
- Frontend MUST consultar `GET /api/v1/empleados`.
- Frontend MUST incluir parámetros obligatorios en cada request:
  - `page` (>= 0)
  - `size` (1..100)
  - `sort` (formato `campo,direccion`)
- Para esta feature, carga inicial MUST usar:
  - `page=0`
  - `size=10`
  - `sort=nombre,asc`
- Campos de sort permitidos por backend:
  - `clave`, `nombre`, `direccion`, `telefono`
- Direcciones válidas:
  - `asc`, `desc`

## Employee List Response Contract
- Frontend MUST soportar envelope paginado de Spring Data:
  - `content[]`
  - `number`
  - `size`
  - `totalElements`
  - `totalPages`
  - `first`
  - `last`
- Cada elemento de `content` para render del dashboard MUST considerar:
  - `clave` (identificador principal visible)
  - `nombre`
  - `rol`
  - (`direccion`, `telefono`, `departamentoId` opcionales para extensión futura)

## UI State Contract
- Durante la consulta, la pantalla MUST mostrar estado `loading`.
- Si `content` llega vacío, la pantalla MUST mostrar estado `empty` con mensaje claro.
- Si ocurre error de red o backend, la pantalla MUST mostrar estado `error` con:
  - mensaje entendible
  - botón manual “Reintentar”
- El botón “Reintentar” MUST relanzar la consulta con los mismos parámetros paginados vigentes.

## Pagination Interaction Contract
- El dashboard MUST exponer controles `Siguiente` y `Anterior`.
- `Siguiente` MUST avanzar página solo si existe siguiente página.
- `Anterior` MUST retroceder página solo si la página actual es mayor que 0.
- Cada cambio de página MUST refrescar tabla sin navegación fuera del dashboard.

## Role Badge Contract
- `rol` MUST mostrarse como badge visual diferenciada por valor.
- Valores esperados: `ROLE_ADMIN`, `ROLE_USER`.
- Valor no reconocido MUST usar badge fallback neutra sin bloquear render.

## Compatibility Guarantees
- No cambia contrato backend existente ni versión de ruta (`/api/v1`).
- No requiere cambios de esquema de base de datos.
- Mantiene integración con Basic Auth/interceptor existente.
