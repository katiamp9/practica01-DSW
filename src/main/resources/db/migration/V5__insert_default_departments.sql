INSERT INTO departamentos (nombre)
VALUES ('Sistemas')
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO departamentos (nombre)
VALUES ('RH')
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO departamentos (nombre)
VALUES ('Ventas')
ON CONFLICT (nombre) DO NOTHING;
