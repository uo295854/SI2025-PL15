PRAGMA foreign_keys = ON;

DELETE FROM Pago;
DELETE FROM Inscripcion_Actividad;
DELETE FROM Reserva_Instalacion;
DELETE FROM Bloqueo_por_Actividad;
DELETE FROM Actividad;
DELETE FROM Usuario;
DELETE FROM Admin;
DELETE FROM NoSocio;
DELETE FROM Socio;
DELETE FROM PeriodoInscripcion;
DELETE FROM PeriodoOficial;
DELETE FROM Instalacion;

INSERT OR IGNORE INTO Instalacion (nombre_instalacion, tipo_deporte, tipo_instalacion, aforo_max, coste) VALUES
  ('Pista Pádel 1', 'padel', 'CANCHA', 4, 8.0),
  ('Pista Tenis 1', 'tenis', 'CANCHA', 4, 10.0),
  ('Sala Multiusos', NULL, 'SALA', 50, 0.0);

INSERT OR IGNORE INTO PeriodoOficial (nombre, fecha_ini, fecha_fin) VALUES
  ('SEPTIEMBRE', '2025-09-01', '2025-12-31'),
  ('ENERO',      '2026-01-01', '2026-05-31'),
  ('JUNIO',      '2026-06-01', '2026-08-31');

INSERT OR IGNORE INTO PeriodoInscripcion (nombre, descripcion, fecha_inicio_socio, fecha_fin_socio, fecha_fin_nosocio) VALUES
  ('Inscripción Septiembre', 'Socios primero, luego no socios', '2025-09-01', '2025-09-05', '2025-09-10');

INSERT OR IGNORE INTO Socio (num_socio, nombre, apellidos, dni, email, telefono, estado, al_corriente_pago) VALUES
  (1001, 'Juan', 'Perez', '11111111A', 'juanp@gmail.com', '985342403', 'ACTIVO', 1),
  (1002, 'María', 'Fernandez', '22222222B', 'maria@gmail.com', '970345612', 'ACTIVO', 1),
  (1003, 'Pedro', 'Mora', '33333333C', 'pedr@gmail.com', '980123456', 'ACTIVO', 0);

INSERT OR IGNORE INTO NoSocio (nombre, dni) VALUES
  ('Carlos García', '44444444D');

INSERT OR IGNORE INTO Admin (dni, nombre) VALUES
  ('99999999Z', 'Admin Centro');

INSERT OR IGNORE INTO Usuario (email, username, password, rol, id_admin, id_socio, id_nosocio) VALUES
  ('admin@centro.local', 'admin', 'admin', 'ADMIN',
   (SELECT id_admin FROM Admin WHERE dni='99999999Z'),
   NULL, NULL);

INSERT OR IGNORE INTO Usuario (email, username, password, rol, id_admin, id_socio, id_nosocio) VALUES
  ('juan@example.com', 'socio1', 'socio1', 'SOCIO',
   NULL,
   (SELECT id_socio FROM Socio WHERE dni='11111111A'),
   NULL);

INSERT OR IGNORE INTO Usuario (email, username, password, rol, id_admin, id_socio, id_nosocio) VALUES
  ('carlos@example.com', 'nosocio1', 'nosocio1', 'NOSOCIO',
   NULL,
   NULL,
   (SELECT id_nosocio FROM NoSocio WHERE dni='44444444D'));

INSERT OR IGNORE INTO Usuario (email, username, password, rol, id_admin, id_socio, id_nosocio) VALUES
  ('maria@example.com', 'socio2', 'socio2', 'SOCIO',
   NULL,
   (SELECT id_socio FROM Socio WHERE dni='22222222B'),
   NULL);

-- ACTIVIDAD SIN INCIDENCIAS
INSERT OR IGNORE INTO Actividad (
  id_periodo_oficial, id_periodo_inscripcion, id_instalacion,
  nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
) VALUES (
  (SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre='SEPTIEMBRE'),
  (SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre='Inscripción Septiembre'),
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Sala Multiusos'),
  'Conferencia Nutrición Deportiva', 'CULTURAL', 50, 1, 90, '2025-09-20', '2025-09-20',
  'Charla abierta con inscripción previa', 0, 5
);

INSERT OR IGNORE INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) VALUES
  ((SELECT id_actividad FROM Actividad WHERE nombre='Conferencia Nutrición Deportiva'),
   '2025-09-20 10:00', '2025-09-20 11:30');

-- ACTIVIDAD QUE PISA UNA RESERVA DE SOCIO
INSERT OR IGNORE INTO Actividad (
  id_periodo_oficial, id_periodo_inscripcion, id_instalacion,
  nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
) VALUES (
  (SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre='SEPTIEMBRE'),
  (SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre='Inscripción Septiembre'),
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Pádel 1'),
  'Clases Pádel Iniciación', 'CLASE', 12, 4, 60, '2025-09-15', '2025-09-18',
  'Clases grupales en pista', 10, 20
);

INSERT OR IGNORE INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) VALUES
  ((SELECT id_actividad FROM Actividad WHERE nombre='Clases Pádel Iniciación'),
   '2025-09-15 18:00', '2025-09-15 19:00');

INSERT OR IGNORE INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) VALUES
  ((SELECT id_actividad FROM Actividad WHERE nombre='Clases Pádel Iniciación'),
   '2025-09-16 18:00', '2025-09-16 19:00');

-- RESERVA DE SOCIO QUE SÍ SOLAPA CON LA ACTIVIDAD DE PÁDEL
INSERT OR IGNORE INTO Reserva_Instalacion (id_instalacion, id_socio, datetime_ini, datetime_fin) VALUES
  ((SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Pádel 1'),
   (SELECT id_socio FROM Socio WHERE dni='11111111A'),
   '2025-09-15 18:30', '2025-09-15 19:30');

-- OTRA RESERVA NORMAL SIN RELACIÓN
INSERT OR IGNORE INTO Reserva_Instalacion (id_instalacion, id_socio, datetime_ini, datetime_fin) VALUES
  ((SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Tenis 1'),
   (SELECT id_socio FROM Socio WHERE dni='22222222B'),
   '2025-09-16 10:00', '2025-09-16 11:00');

-- DOS ACTIVIDADES EN CONFLICTO ENTRE SÍ
INSERT OR IGNORE INTO Actividad (
  id_periodo_oficial, id_periodo_inscripcion, id_instalacion,
  nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
) VALUES (
  (SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre='SEPTIEMBRE'),
  (SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre='Inscripción Septiembre'),
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Tenis 1'),
  'Torneo Tenis', 'CAMPEONATO', 8, 1, 120, '2025-09-18', '2025-09-18',
  'Torneo interno del club', 0, 0
);

INSERT OR IGNORE INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) VALUES
  ((SELECT id_actividad FROM Actividad WHERE nombre='Torneo Tenis'),
   '2025-09-18 17:00', '2025-09-18 19:00');

INSERT OR IGNORE INTO Actividad (
  id_periodo_oficial, id_periodo_inscripcion, id_instalacion,
  nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
) VALUES (
  (SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre='SEPTIEMBRE'),
  (SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre='Inscripción Septiembre'),
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Tenis 1'),
  'Clases Tenis Avanzado', 'CLASE', 10, 1, 60, '2025-09-18', '2025-09-18',
  'Clase avanzada de tenis', 15, 25
);

INSERT OR IGNORE INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) VALUES
  ((SELECT id_actividad FROM Actividad WHERE nombre='Clases Tenis Avanzado'),
   '2025-09-18 18:00', '2025-09-18 19:30');

INSERT INTO Pago (id_socio, id_reservains, importe, concepto, fecha, estado)
VALUES (
  (SELECT id_socio FROM Socio WHERE dni='11111111A'),
  (SELECT id_reservains FROM Reserva_Instalacion LIMIT 1),
  8.0,
  'RESERVA',
  '2025-09-10 18:00',
  'PAGADO'
);

-- =========================================================
-- ACTIVIDAD A (la que vamos a "reservar automáticamente")
-- =========================================================

INSERT OR IGNORE INTO Actividad (
  id_periodo_oficial, id_periodo_inscripcion, id_instalacion,
  nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
) VALUES (
  (SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre='SEPTIEMBRE'),
  (SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre='Inscripción Septiembre'),
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Tenis 1'),
  'Entrenamiento Tenis Avanzado', 'CLASE', 8, 3, 60, '2025-09-20', '2025-09-22',
  'Entrenamiento intensivo', 15, 25
);

INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin)
VALUES (
  (SELECT id_actividad FROM Actividad WHERE nombre='Entrenamiento Tenis Avanzado'),
  '2025-09-20 18:00', '2025-09-20 19:00'
);

INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin)
VALUES (
  (SELECT id_actividad FROM Actividad WHERE nombre='Entrenamiento Tenis Avanzado'),
  '2025-09-21 18:00', '2025-09-21 19:00'
);

INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin)
VALUES (
  (SELECT id_actividad FROM Actividad WHERE nombre='Entrenamiento Tenis Avanzado'),
  '2025-09-22 18:00', '2025-09-22 19:00'
);

-- =========================================================
-- ACTIVIDAD B (para generar CONFLICTO)
-- =========================================================

INSERT OR IGNORE INTO Actividad (
  id_periodo_oficial, id_periodo_inscripcion, id_instalacion,
  nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
) VALUES (
  (SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre='SEPTIEMBRE'),
  (SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre='Inscripción Septiembre'),
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Tenis 1'),
  'Partido Liga Tenis', 'CAMPEONATO', 4, 1, 60, '2025-09-21', '2025-09-21',
  'Partido oficial', 0, 0
);

INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin)
VALUES (
  (SELECT id_actividad FROM Actividad WHERE nombre='Partido Liga Tenis'),
  '2025-09-21 18:00', '2025-09-21 19:00'
);

-- =========================================================
-- RESERVA DE SOCIO (para ser eliminada)
-- =========================================================

INSERT INTO Reserva_Instalacion (
  id_instalacion, id_socio, datetime_ini, datetime_fin
) VALUES (
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Tenis 1'),
  (SELECT id_socio FROM Socio WHERE dni='11111111A'),
  '2025-09-22 18:00', '2025-09-22 19:00'
);

INSERT INTO Pago (
  id_socio, id_actividad, id_reservains, importe, concepto, fecha, estado
) VALUES (
  (SELECT id_socio FROM Socio WHERE dni='11111111A'),
  NULL,
  (SELECT id_reservains FROM Reserva_Instalacion
     WHERE datetime_ini='2025-09-22 18:00'),
  10.0,
  'RESERVA',
  '2025-09-10 10:00',
  'PAGADO'
);

-- =========================================================
-- INSCRIPCIONES A ACTIVIDADES
-- Ahora son por actividad completa, no por fecha_sesion
-- =========================================================

INSERT INTO Inscripcion_Actividad (
  id_socio, id_actividad, fecha_inscripcion, estado
) VALUES (
  (SELECT id_socio FROM Socio WHERE dni='11111111A'),
  (SELECT id_actividad FROM Actividad WHERE nombre='Conferencia Nutrición Deportiva'),
  '2025-09-02',
  'ACTIVA'
);

INSERT INTO Inscripcion_Actividad (
  id_socio, id_actividad, fecha_inscripcion, estado
) VALUES (
  (SELECT id_socio FROM Socio WHERE dni='22222222B'),
  (SELECT id_actividad FROM Actividad WHERE nombre='Clases Pádel Iniciación'),
  '2025-09-03',
  'ACTIVA'
);

INSERT INTO Inscripcion_Actividad (
  id_socio, id_actividad, fecha_inscripcion, estado
) VALUES (
  (SELECT id_socio FROM Socio WHERE dni='33333333C'),
  (SELECT id_actividad FROM Actividad WHERE nombre='Entrenamiento Tenis Avanzado'),
  '2025-09-05',
  'ACTIVA'
);

-- =========================================================
-- PAGOS ASOCIADOS A INSCRIPCIONES
-- =========================================================

INSERT INTO Pago (
  id_socio, id_actividad, id_inscripcion, importe, concepto, fecha, estado
) VALUES (
  (SELECT id_socio FROM Socio WHERE dni='11111111A'),
  (SELECT id_actividad FROM Actividad WHERE nombre='Conferencia Nutrición Deportiva'),
  (SELECT id_inscripcion FROM Inscripcion_Actividad
     WHERE id_socio = (SELECT id_socio FROM Socio WHERE dni='11111111A')
       AND id_actividad = (SELECT id_actividad FROM Actividad WHERE nombre='Conferencia Nutrición Deportiva')),
  0.0,
  'INSCRIPCION',
  '2025-09-02',
  'PENDIENTE'
);

INSERT INTO Pago (
  id_socio, id_actividad, id_inscripcion, importe, concepto, fecha, estado
) VALUES (
  (SELECT id_socio FROM Socio WHERE dni='22222222B'),
  (SELECT id_actividad FROM Actividad WHERE nombre='Clases Pádel Iniciación'),
  (SELECT id_inscripcion FROM Inscripcion_Actividad
     WHERE id_socio = (SELECT id_socio FROM Socio WHERE dni='22222222B')
       AND id_actividad = (SELECT id_actividad FROM Actividad WHERE nombre='Clases Pádel Iniciación')),
  10.0,
  'INSCRIPCION',
  '2025-09-03',
  'PENDIENTE'
);

INSERT INTO Pago (
  id_socio, id_actividad, id_inscripcion, importe, concepto, fecha, estado
) VALUES (
  (SELECT id_socio FROM Socio WHERE dni='33333333C'),
  (SELECT id_actividad FROM Actividad WHERE nombre='Entrenamiento Tenis Avanzado'),
  (SELECT id_inscripcion FROM Inscripcion_Actividad
     WHERE id_socio = (SELECT id_socio FROM Socio WHERE dni='33333333C')
       AND id_actividad = (SELECT id_actividad FROM Actividad WHERE nombre='Entrenamiento Tenis Avanzado')),
  15.0,
  'INSCRIPCION',
  '2025-09-05',
  'PENDIENTE'
);

-- =========================================================
-- EJEMPLO H.U. INSCRIPCION EN ACTIVIDAD
-- Actividad con varias sesiones dentro de las próximas dos semanas
-- =========================================================

INSERT OR IGNORE INTO PeriodoInscripcion (nombre, descripcion, fecha_inicio_socio, fecha_fin_socio, fecha_fin_nosocio) VALUES
  ('Inscripción Primavera 2026', 'Periodo abierto para pruebas de inscripción', '2026-03-15', '2026-04-05', '2026-04-10');

INSERT OR IGNORE INTO Actividad (
  id_periodo_oficial, id_periodo_inscripcion, id_instalacion,
  nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion,
  cuota_socio, cuota_nosocio
) VALUES (
  (SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre='ENERO'),
  (SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre='Inscripción Primavera 2026'),
  (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Sala Multiusos'),
  'Yoga Mañanas Marzo', 'CLASE', 12, 5, 60, '2026-03-23', '2026-04-03',
  'Actividad de prueba para inscripciones en varias fechas', 12, 20
);

INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) VALUES
  ((SELECT id_actividad FROM Actividad WHERE nombre='Yoga Mañanas Marzo'),
   '2026-03-23 10:00', '2026-03-23 11:00');

INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) VALUES
  ((SELECT id_actividad FROM Actividad WHERE nombre='Yoga Mañanas Marzo'),
   '2026-03-25 10:00', '2026-03-25 11:00');

INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) VALUES
  ((SELECT id_actividad FROM Actividad WHERE nombre='Yoga Mañanas Marzo'),
   '2026-03-30 10:00', '2026-03-30 11:00');

INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) VALUES
  ((SELECT id_actividad FROM Actividad WHERE nombre='Yoga Mañanas Marzo'),
   '2026-04-01 10:00', '2026-04-01 11:00');

-- Juan ya está inscrito en toda la actividad
INSERT INTO Inscripcion_Actividad (
  id_socio, id_actividad, fecha_inscripcion, estado
) VALUES (
  (SELECT id_socio FROM Socio WHERE dni='11111111A'),
  (SELECT id_actividad FROM Actividad WHERE nombre='Yoga Mañanas Marzo'),
  '2026-03-19',
  'ACTIVA'
);

INSERT INTO Pago (
  id_socio, id_actividad, id_inscripcion, importe, concepto, fecha, estado
) VALUES (
  (SELECT id_socio FROM Socio WHERE dni='11111111A'),
  (SELECT id_actividad FROM Actividad WHERE nombre='Yoga Mañanas Marzo'),
  (SELECT id_inscripcion FROM Inscripcion_Actividad
     WHERE id_socio = (SELECT id_socio FROM Socio WHERE dni='11111111A')
       AND id_actividad = (SELECT id_actividad FROM Actividad WHERE nombre='Yoga Mañanas Marzo')),
  12.0,
  'INSCRIPCION',
  '2026-03-19',
  'PENDIENTE'
);