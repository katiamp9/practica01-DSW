CREATE TABLE IF NOT EXISTS departamentos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_departamentos_nombre UNIQUE (nombre)
);

INSERT INTO departamentos (nombre)
VALUES ('Sistemas')
ON CONFLICT (nombre) DO NOTHING;

ALTER TABLE empleados
ADD COLUMN IF NOT EXISTS departamento_id BIGINT;

UPDATE empleados
SET departamento_id = (
    SELECT id
    FROM departamentos
    WHERE nombre = 'Sistemas'
)
WHERE departamento_id IS NULL;

ALTER TABLE empleados
ALTER COLUMN departamento_id SET NOT NULL;

ALTER TABLE empleados
ADD CONSTRAINT fk_empleados_departamento
FOREIGN KEY (departamento_id)
REFERENCES departamentos (id)
ON UPDATE RESTRICT
ON DELETE RESTRICT;
