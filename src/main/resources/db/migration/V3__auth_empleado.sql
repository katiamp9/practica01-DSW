CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS cuentas_empleado (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    correo VARCHAR(254) NOT NULL,
    empleado_clave VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_cuentas_empleado_correo UNIQUE (correo),
    CONSTRAINT uq_cuentas_empleado_empleado_clave UNIQUE (empleado_clave),
    CONSTRAINT fk_cuentas_empleado_empleado
        FOREIGN KEY (empleado_clave)
        REFERENCES empleados (clave)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS credenciales_empleado (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cuenta_empleado_id UUID NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_credenciales_empleado_cuenta UNIQUE (cuenta_empleado_id),
    CONSTRAINT fk_credenciales_empleado_cuenta
        FOREIGN KEY (cuenta_empleado_id)
        REFERENCES cuentas_empleado (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

INSERT INTO empleados (clave, prefijo, consecutivo, nombre, direccion, telefono)
VALUES ('EMP-1001', 'EMP-', 1001, 'Administrador Inicial', 'N/A', 'N/A')
ON CONFLICT (clave) DO NOTHING;

INSERT INTO cuentas_empleado (correo, empleado_clave)
VALUES ('admin@empresa.com', 'EMP-1001')
ON CONFLICT (correo) DO NOTHING;

INSERT INTO credenciales_empleado (cuenta_empleado_id, password_hash)
SELECT ce.id,
    '${initial_admin_password_hash}'
FROM cuentas_empleado ce
WHERE ce.correo = 'admin@empresa.com'
ON CONFLICT (cuenta_empleado_id) DO NOTHING;