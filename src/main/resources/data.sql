PRAGMA foreign_keys = ON;

DELETE FROM Reserva_Instalacion;
DELETE FROM Bloqueo_por_Actividad;
DELETE FROM Actividad;
DELETE FROM Usuario;
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

INSERT OR IGNORE INTO Socio (num_socio, nombre,apellidos, dni, email,telefono,estado, al_corriente_pago) VALUES
  (1001, 'Juan','Perez', '11111111A','juanp@gmail.com','985342403', 'ACTIVO', 1),
  (1002, 'María','Fernandez', '22222222B','maria@gmail.com','970345612',  'ACTIVO', 1),
  (1003, 'Pedro','Mora',  '33333333C', 'pedr@gmail.com','980123456', 'ACTIVO', 0);

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

INSERT OR IGNORE INTO Reserva_Instalacion (id_instalacion, id_socio, datetime_ini, datetime_fin) VALUES
  ((SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Pádel 1'),
   (SELECT id_socio FROM Socio WHERE dni='11111111A'),
   '2025-09-15 17:00', '2025-09-15 18:00');

INSERT OR IGNORE INTO Reserva_Instalacion (id_instalacion, id_socio, datetime_ini, datetime_fin) VALUES
  ((SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion='Pista Tenis 1'),
   (SELECT id_socio FROM Socio WHERE dni='22222222B'),
   '2025-09-16 10:00', '2025-09-16 11:00');