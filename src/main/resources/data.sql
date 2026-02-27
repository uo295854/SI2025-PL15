PRAGMA foreign_keys = ON;

-- =========================
-- PARAMETROS
-- =========================
INSERT OR IGNORE INTO Parametro (clave, valor) VALUES
  ('MIN_ANTELACION_HORAS', '48'),
  ('MAX_RESERVAS_ACTIVAS', '3');

-- =========================
-- INSTALACIONES
-- =========================
INSERT OR IGNORE INTO Instalacion (nombre_instalacion, tipo_deporte, tipo_instalacion, aforo_max) VALUES
  ('Pista Pádel 1', 'padel', 'CANCHA', 4),
  ('Pista Tenis 1', 'tenis', 'CANCHA', 4),
  ('Sala Multiusos', NULL, 'SALA', 50);

-- =========================
-- PERIODOS OFICIALES
-- =========================
INSERT OR IGNORE INTO PeriodoOficial (nombre, fecha_ini, fecha_fin) VALUES
  ('SEPTIEMBRE', '2025-09-01', '2025-12-31'),
  ('ENERO',      '2026-01-01', '2026-05-31'),
  ('JUNIO',      '2026-06-01', '2026-08-31');

-- =========================
-- PERIODOS DE INSCRIPCION
-- =========================
INSERT OR IGNORE INTO PeriodoInscripcion (
  nombre, descripcion,
  fecha_inicio_socio, fecha_fin_socio, fecha_fin_nosocio
) VALUES
  ('Inscripción Septiembre', 'Socios primero, luego no socios',
   '2025-09-01', '2025-09-05', '2025-09-10');

-- =========================
-- SOCIOS
-- =========================
INSERT OR IGNORE INTO Socio (num_socio, nombre, dni, email, telefono, estado, al_corriente_pago) VALUES
  (1001, 'Juan Pérez',  '11111111A', 'juan@example.com',  '600111111', 'ACTIVO', 1),
  (1002, 'María López', '22222222B', 'maria@example.com', '600222222', 'ACTIVO', 1),
  (1003, 'Pedro Mora',  '33333333C', NULL, NULL, 'ACTIVO', 0);

-- =========================
-- NO SOCIOS
-- =========================
INSERT OR IGNORE INTO NoSocio (nombre, dni) VALUES
  ('Carlos García', '44444444D');

-- =========================
-- USUARIOS (login)
-- =========================
INSERT OR IGNORE INTO Usuario (id_socio, id_nosocio, username, password, rol)
VALUES (NULL, NULL, 'admin', 'admin', 'ADMIN');

INSERT OR IGNORE INTO Usuario (id_socio, id_nosocio, username, password, rol)
VALUES (
  (SELECT id_socio FROM Socio WHERE dni='11111111A'),
  NULL,
  'socio1',
  'socio1',
  'SOCIO'
);

INSERT OR IGNORE INTO Usuario (id_socio, id_nosocio, username, password, rol)
VALUES (
  NULL,
  (SELECT id_nosocio FROM NoSocio WHERE dni='44444444D'),
  'nosocio1',
  'nosocio1',
  'NOSOCIO'
);

-- =========================
-- ACTIVIDADES
-- =========================
INSERT OR IGNORE INTO Actividad (
  id_periodo_oficial,
  id_periodo_inscripcion,
  id_instalacion,
  nombre, tipo, aforo, dias, duracion,
  fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
)
VALUES (
  (SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre='SEPTIEMBRE'),
  (SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre='Inscripción Septiembre'),
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Sala Multiusos'),
  'Conferencia Nutrición Deportiva',
  'CULTURAL',
  50, 1, 90,
  '2025-09-20',
  '2025-09-20',
  'Charla abierta con inscripción previa',
  0, 5
);

INSERT OR IGNORE INTO Actividad (
  id_periodo_oficial,
  id_periodo_inscripcion,
  id_instalacion,
  nombre, tipo, aforo, dias, duracion,
  fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
)
VALUES (
  (SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre='SEPTIEMBRE'),
  (SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre='Inscripción Septiembre'),
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Pádel 1'),
  'Clases Pádel Iniciación',
  'CLASE',
  12, 4, 60,
  '2025-09-15',
  '2025-09-18',
  'Clases grupales en pista',
  10, 20
);

-- =========================
-- BLOQUEOS POR ACTIVIDAD
-- =========================
INSERT OR IGNORE INTO Reserva_Actividad_Instalacion (
  id_actividad,
  inicio_datetime,
  fin_datetime
)
VALUES (
  (SELECT id_actividad FROM Actividad WHERE nombre='Clases Pádel Iniciación'),
  '2025-09-15 18:00',
  '2025-09-15 19:00'
);

INSERT OR IGNORE INTO Reserva_Actividad_Instalacion (
  id_actividad,
  inicio_datetime,
  fin_datetime
)
VALUES (
  (SELECT id_actividad FROM Actividad WHERE nombre='Clases Pádel Iniciación'),
  '2025-09-16 18:00',
  '2025-09-16 19:00'
);

-- =========================
-- RESERVAS DE SOCIOS
-- =========================
INSERT OR IGNORE INTO Reserva_Instalacion (
  id_instalacion,
  id_socio,
  inicio_datetime,
  fin_datetime
)
VALUES (
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Pádel 1'),
  (SELECT id_socio FROM Socio WHERE dni='11111111A'),
  '2025-09-15 17:00',
  '2025-09-15 18:00'
);

INSERT OR IGNORE INTO Reserva_Instalacion (
  id_instalacion,
  id_socio,
  inicio_datetime,
  fin_datetime
)
VALUES (
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Tenis 1'),
  (SELECT id_socio FROM Socio WHERE dni='22222222B'),
  '2025-09-16 10:00',
  '2025-09-16 11:00'
);