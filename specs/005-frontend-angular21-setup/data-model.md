# Data Model — Feature 005

## Entity: FrontendWorkspace
- Description: Representa el espacio de trabajo frontend en `/frontend-empleados`.
- Fields:
  - `rootPath` (string, required): ruta raíz del frontend.
  - `initialized` (boolean, required): indica si la estructura base está lista.
  - `folders` (array<string>, required): lista de carpetas mínimas (`componentes`, `servicios`, `modelos`, `interceptores`).
- Validation Rules:
  - `rootPath` MUST ser `/frontend-empleados`.
  - `folders` MUST contener como mínimo las carpetas requeridas.
- State Transitions:
  - `not-initialized` → `initialized` al completar estructura base.

## Entity: BackendCorsPolicy
- Description: Configuración de orígenes y cabeceras permitidos para integración local.
- Fields:
  - `allowedOrigins` (array<string>, required)
  - `allowedMethods` (array<string>, required)
  - `allowedHeaders` (array<string>, required)
  - `allowCredentials` (boolean, required)
- Validation Rules:
  - `allowedOrigins` MUST incluir `http://localhost:4200`.
  - `allowedMethods` MUST cubrir lectura/escritura necesarias en dev.
  - `allowedHeaders` MUST incluir `Authorization`.
- State Transitions:
  - `default-security-policy` → `dev-integration-policy` manteniendo Basic Auth activa.

## Entity: DevProxyConfig
- Description: Mapeo de rutas frontend hacia backend en entorno local.
- Fields:
  - `routePrefix` (string, required): prefijo de rutas proxied (`/api`).
  - `targetHost` (string, required): URL del backend local.
  - `changeOrigin` (boolean, optional)
  - `secure` (boolean, optional)
- Validation Rules:
  - `routePrefix` MUST ser `/api`.
  - `targetHost` MUST apuntar al backend local en desarrollo.
- State Transitions:
  - `absent` → `configured` al crear `proxy.conf.json`.

## Entity: FrontendArchitectureRule
- Description: Regla constitucional para prácticas frontend de esta fase.
- Fields:
  - `frontendLocation` (string, required)
  - `mandatoryPatterns` (array<string>, required)
  - `effectiveFromFeature` (string, required)
- Validation Rules:
  - `frontendLocation` MUST ser `/frontend-empleados`.
  - `mandatoryPatterns` MUST incluir Standalone Components, Signals y Control Flow nativo.
- State Transitions:
  - `undefined` → `enforced` al registrar en constitución del proyecto.
