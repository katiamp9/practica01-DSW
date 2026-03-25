ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS rol VARCHAR(50);

UPDATE empleados
SET rol = 'ROLE_USER'
WHERE rol IS NULL OR btrim(rol) = '';

UPDATE empleados
SET rol = 'ROLE_ADMIN'
WHERE clave = 'EMP-1001';

ALTER TABLE empleados
    ALTER COLUMN rol SET DEFAULT 'ROLE_USER';

ALTER TABLE empleados
    ALTER COLUMN rol SET NOT NULL;
