PRAGMA foreign_keys = ON;

-- =========================
-- SOCIO / NO SOCIO
-- =========================
CREATE TABLE IF NOT EXISTS Socio (
  id_socio INTEGER PRIMARY KEY AUTOINCREMENT,
  num_socio INTEGER NOT NULL UNIQUE,
  nombre TEXT NOT NULL,
  dni TEXT NOT NULL UNIQUE,
  email TEXT,
  telefono TEXT,
  estado TEXT NOT NULL CHECK (estado IN ('ACTIVO','BAJA')),
  al_corriente_pago INTEGER NOT NULL DEFAULT 1 CHECK (al_corriente_pago IN (0,1))
);

CREATE TABLE IF NOT EXISTS NoSocio (
  id_nosocio INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre TEXT NOT NULL,
  dni TEXT NOT NULL UNIQUE
);

-- =========================
-- USUARIO (login)
--  Regla lógica (en app):
--   rol=ADMIN -> id_socio e id_nosocio NULL
--   rol=SOCIO -> id_socio NOT NULL, id_nosocio NULL
--   rol=NOSOCIO -> id_nosocio NOT NULL, id_socio NULL
-- =========================
CREATE TABLE IF NOT EXISTS Usuario (
  id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
  id_socio INTEGER NULL,
  id_nosocio INTEGER NULL,
  username TEXT NOT NULL UNIQUE,
  password TEXT NOT NULL,
  rol TEXT NOT NULL CHECK (rol IN ('ADMIN','SOCIO','NOSOCIO')),
  FOREIGN KEY (id_socio) REFERENCES Socio(id_socio),
  FOREIGN KEY (id_nosocio) REFERENCES NoSocio(id_nosocio)
);

-- =========================
-- INSTALACION
-- =========================
CREATE TABLE IF NOT EXISTS Instalacion (
  id_instalacion INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre_instalacion TEXT NOT NULL UNIQUE,
  tipo_deporte TEXT,
  tipo_instalacion TEXT NOT NULL CHECK (tipo_instalacion IN ('CANCHA','SALA','OTRA')),
  aforo_max INTEGER NOT NULL CHECK (aforo_max > 0)
);

-- =========================
-- PERIODOS
-- =========================
CREATE TABLE IF NOT EXISTS PeriodoOficial (
  id_periodo_oficial INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre TEXT NOT NULL CHECK (nombre IN ('SEPTIEMBRE','ENERO','JUNIO')),
  fecha_ini TEXT NOT NULL, -- 'YYYY-MM-DD'
  fecha_fin TEXT NOT NULL, -- 'YYYY-MM-DD'
  CHECK (fecha_ini <= fecha_fin)
);

CREATE TABLE IF NOT EXISTS PeriodoInscripcion (
  id_periodo_inscripcion INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre TEXT NOT NULL,
  descripcion TEXT,
  fecha_inicio_socio TEXT NOT NULL,
  fecha_fin_socio TEXT NOT NULL,
  fecha_fin_nosocio TEXT NOT NULL,
  CHECK (fecha_inicio_socio <= fecha_fin_socio),
  CHECK (fecha_fin_socio <= fecha_fin_nosocio)
);

-- =========================
-- ACTIVIDAD (1 actividad -> 1 instalación)
-- =========================
CREATE TABLE IF NOT EXISTS Actividad (
  id_actividad INTEGER PRIMARY KEY AUTOINCREMENT,
  id_periodo_oficial INTEGER NOT NULL,
  id_periodo_inscripcion INTEGER NOT NULL,
  id_instalacion INTEGER NOT NULL,

  nombre TEXT NOT NULL,
  tipo TEXT NOT NULL CHECK (tipo IN ('CULTURAL','DEPORTIVA','CLASE','CAMPEONATO')),
  aforo INTEGER NOT NULL CHECK (aforo > 0),
  dias INTEGER,
  duracion INTEGER, -- minutos (recomendado)
  fecha_inicio TEXT NOT NULL,
  fecha_fin TEXT NOT NULL,
  descripcion TEXT,
  cuota_socio REAL NOT NULL DEFAULT 0 CHECK (cuota_socio >= 0),
  cuota_nosocio REAL NOT NULL DEFAULT 0 CHECK (cuota_nosocio >= 0),

  CHECK (fecha_inicio <= fecha_fin),

  FOREIGN KEY (id_periodo_oficial) REFERENCES PeriodoOficial(id_periodo_oficial)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (id_periodo_inscripcion) REFERENCES PeriodoInscripcion(id_periodo_inscripcion)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (id_instalacion) REFERENCES Instalacion(id_instalacion)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =========================
-- RESERVA INSTALACION (hecha por socio)
-- =========================
CREATE TABLE IF NOT EXISTS Reserva_Instalacion (
  id_reservains INTEGER PRIMARY KEY AUTOINCREMENT,
  id_instalacion INTEGER NOT NULL,
  id_socio INTEGER NOT NULL,
  inicio_datetime TEXT NOT NULL, -- 'YYYY-MM-DD HH:MM'
  fin_datetime TEXT NOT NULL,    -- 'YYYY-MM-DD HH:MM'
  CHECK (inicio_datetime < fin_datetime),

  FOREIGN KEY (id_instalacion) REFERENCES Instalacion(id_instalacion)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (id_socio) REFERENCES Socio(id_socio)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =========================
-- BLOQUEO POR ACTIVIDAD (ocupación del centro)
--  Nota: no metemos id_instalacion aquí porque ya viene por Actividad.id_instalacion,
--  así evitamos inconsistencias.
-- =========================
CREATE TABLE IF NOT EXISTS Reserva_Actividad_Instalacion (
  id_reserva INTEGER PRIMARY KEY AUTOINCREMENT,
  id_actividad INTEGER NOT NULL,
  inicio_datetime TEXT NOT NULL,
  fin_datetime TEXT NOT NULL,
  CHECK (inicio_datetime < fin_datetime),
  FOREIGN KEY (id_actividad) REFERENCES Actividad(id_actividad)
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- =========================
-- PARAMETROS (config)
-- =========================
CREATE TABLE IF NOT EXISTS Parametro (
  clave TEXT PRIMARY KEY,
  valor TEXT NOT NULL
);

-- =========================
-- Índices útiles (rendimiento en búsquedas y conflictos)
-- =========================
CREATE INDEX IF NOT EXISTS idx_reserva_instalacion_rango
  ON Reserva_Instalacion(id_instalacion, inicio_datetime, fin_datetime);

CREATE INDEX IF NOT EXISTS idx_bloqueo_actividad_rango
  ON Reserva_Actividad_Instalacion(id_actividad, inicio_datetime, fin_datetime);

CREATE INDEX IF NOT EXISTS idx_actividad_periodo_oficial
  ON Actividad(id_periodo_oficial);

CREATE INDEX IF NOT EXISTS idx_actividad_instalacion
  ON Actividad(id_instalacion);