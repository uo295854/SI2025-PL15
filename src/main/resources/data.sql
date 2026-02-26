PRAGMA foreign_keys = ON;

-- =========================
-- PARAMETROS INICIALES
-- =========================
INSERT OR IGNORE INTO Parametro (clave, valor) VALUES
  ('MIN_ANTELACION_HORAS', '48'),
  ('MAX_RESERVAS_ACTIVAS', '3');

-- =========================
-- INSTALACIONES
-- =========================
INSERT OR IGNORE INTO Instalacion (id_instalacion, nombre_instalacion, tipo_deporte, tipo_instalacion, aforo_max) VALUES
  (1, 'Pista Pádel 1', 'padel', 'CANCHA', 4),
  (2, 'Pista Tenis 1', 'tenis', 'CANCHA', 4),
  (3, 'Sala Multiusos', NULL, 'SALA', 50);

-- =========================
-- PERIODOS OFICIALES (ejemplo curso 2025/26)
-- =========================
INSERT OR IGNORE INTO PeriodoOficial (id_periodo_oficial, nombre, fecha_ini, fecha_fin) VALUES
  (1, 'SEPTIEMBRE', '2025-09-01', '2025-12-31'),
  (2, 'ENERO',      '2026-01-01', '2026-05-31'),
  (3, 'JUNIO',      '2026-06-01', '2026-08-31');

-- =========================
-- PERIODOS DE INSCRIPCIÓN
-- =========================
INSERT OR IGNORE INTO PeriodoInscripcion (
  id_periodo_inscripcion, nombre, descripcion,
  fecha_inicio_socio, fecha_fin_socio, fecha_fin_nosocio
) VALUES
  (1, 'Inscripción Septiembre', 'Socios primero, luego no socios',
   '2025-09-01', '2025-09-05', '2025-09-10');

-- =========================
-- SOCIOS / NO SOCIOS
-- =========================
INSERT OR IGNORE INTO Socio (id_socio, num_socio, nombre, dni, email, telefono, estado, al_corriente_pago) VALUES
  (1, 1001, 'Juan Pérez',  '11111111A', 'juan@example.com',  '600111111', 'ACTIVO', 1),
  (2, 1002, 'María López', '22222222B', 'maria@example.com', '600222222', 'ACTIVO', 1),
  (3, 1003, 'Pedro Mora',  '33333333C', NULL, NULL, 'ACTIVO', 0); -- no al corriente

INSERT OR IGNORE INTO NoSocio (id_nosocio, nombre, dni) VALUES
  (1, 'Carlos García', '44444444D');

-- =========================
-- USUARIOS (login)
--  Nota: passwords en claro solo para demo.
--  En el proyecto real: hash.
-- =========================
INSERT OR IGNORE INTO Usuario (id_usuario, id_socio, id_nosocio, username, password, rol) VALUES
  (1, NULL, NULL, 'admin', 'admin', 'ADMIN'),
  (2, 1, NULL,    'socio1', 'socio1', 'SOCIO'),
  (3, NULL, 1,    'nosocio1', 'nosocio1', 'NOSOCIO');

-- =========================
-- ACTIVIDADES
-- =========================
INSERT OR IGNORE INTO Actividad (
  id_actividad, id_periodo_oficial, id_periodo_inscripcion, id_instalacion,
  nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
) VALUES
  (1, 1, 1, 3,
   'Conferencia Nutrición Deportiva', 'CULTURAL', 50, 1, 90, '2025-09-20', '2025-09-20',
   'Charla abierta con inscripción previa', 0, 5),
  (2, 1, 1, 1,
   'Clases Pádel Iniciación', 'CLASE', 12, 4, 60, '2025-09-15', '2025-09-18',
   'Clases grupales en pista', 10, 20);

-- =========================
-- BLOQUEOS (reserva de instalación por actividad)
-- (ejemplo: actividad 2 ocupa la pista pádel en ciertos horarios)
-- =========================
INSERT OR IGNORE INTO Reserva_Actividad_Instalacion (id_reserva, id_actividad, inicio_datetime, fin_datetime) VALUES
  (1, 2, '2025-09-15 18:00', '2025-09-15 19:00'),
  (2, 2, '2025-09-16 18:00', '2025-09-16 19:00');

-- =========================
-- RESERVAS DE SOCIOS
-- =========================
INSERT OR IGNORE INTO Reserva_Instalacion (id_reservains, id_instalacion, id_socio, inicio_datetime, fin_datetime) VALUES
  (1, 1, 1, '2025-09-15 17:00', '2025-09-15 18:00'),
  (2, 2, 2, '2025-09-16 10:00', '2025-09-16 11:00');