PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS Bloqueo_por_Actividad;
DROP TABLE IF EXISTS Reserva_Instalacion;
DROP TABLE IF EXISTS Actividad;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Admin;
DROP TABLE IF EXISTS NoSocio;
DROP TABLE IF EXISTS Socio;
DROP TABLE IF EXISTS PeriodoInscripcion;
DROP TABLE IF EXISTS PeriodoOficial;
DROP TABLE IF EXISTS Instalacion;

CREATE TABLE IF NOT EXISTS Socio (
  id_socio INTEGER PRIMARY KEY AUTOINCREMENT,
  num_socio INTEGER NOT NULL UNIQUE,
  nombre TEXT NOT NULL,
  apellidos TEXT NOT NULL,
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

CREATE TABLE IF NOT EXISTS Admin (
  id_admin INTEGER PRIMARY KEY AUTOINCREMENT,
  dni TEXT NOT NULL UNIQUE,
  nombre TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Usuario (
  id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
  email TEXT,
  username TEXT NOT NULL UNIQUE,
  password TEXT NOT NULL,
  rol TEXT NOT NULL CHECK (rol IN ('ADMIN','SOCIO','NOSOCIO')),
  id_socio INTEGER NULL,
  id_nosocio INTEGER NULL,
  id_admin INTEGER NULL,
  FOREIGN KEY (id_socio) REFERENCES Socio(id_socio)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (id_nosocio) REFERENCES NoSocio(id_nosocio)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (id_admin) REFERENCES Admin(id_admin)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS Instalacion (
  id_instalacion INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre_instalacion TEXT NOT NULL UNIQUE,
  tipo_deporte TEXT,
  tipo_instalacion TEXT NOT NULL CHECK (tipo_instalacion IN ('CANCHA','SALA','OTRA')),
  aforo_max INTEGER NOT NULL CHECK (aforo_max > 0),
  coste REAL NOT NULL DEFAULT 0 CHECK (coste >= 0)
);

CREATE TABLE IF NOT EXISTS PeriodoOficial (
  id_periodo_oficial INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre TEXT NOT NULL CHECK (nombre IN ('SEPTIEMBRE','ENERO','JUNIO')),
  fecha_ini TEXT NOT NULL,
  fecha_fin TEXT NOT NULL,
  CHECK (fecha_ini <= fecha_fin)
);

CREATE TABLE IF NOT EXISTS PeriodoInscripcion (
  id_periodo_inscripcion INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre TEXT NOT NULL UNIQUE,
  descripcion TEXT,
  fecha_inicio_socio TEXT NOT NULL,
  fecha_fin_socio TEXT NOT NULL,
  fecha_fin_nosocio TEXT NOT NULL,
  CHECK (fecha_inicio_socio <= fecha_fin_socio),
  CHECK (fecha_fin_socio <= fecha_fin_nosocio)
);

CREATE TABLE IF NOT EXISTS Actividad (
  id_actividad INTEGER PRIMARY KEY AUTOINCREMENT,
  id_periodo_oficial INTEGER NOT NULL,
  id_periodo_inscripcion INTEGER NOT NULL,
  id_instalacion INTEGER NOT NULL,
  nombre TEXT NOT NULL UNIQUE,
  tipo TEXT NOT NULL CHECK (tipo IN ('CULTURAL','DEPORTIVA','CLASE','CAMPEONATO')),
  aforo INTEGER NOT NULL CHECK (aforo > 0),
  dias INTEGER,
  duracion INTEGER,
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

CREATE TABLE IF NOT EXISTS Reserva_Instalacion (
  id_reservains INTEGER PRIMARY KEY AUTOINCREMENT,
  id_instalacion INTEGER NOT NULL,
  id_socio INTEGER NOT NULL,
  datetime_ini TEXT NOT NULL,
  datetime_fin TEXT NOT NULL,
  CHECK (datetime_ini < datetime_fin),
  FOREIGN KEY (id_instalacion) REFERENCES Instalacion(id_instalacion)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (id_socio) REFERENCES Socio(id_socio)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS Bloqueo_por_Actividad (
  id_bloqueo INTEGER PRIMARY KEY AUTOINCREMENT,
  id_actividad INTEGER NOT NULL,
  datetime_ini TEXT NOT NULL,
  datetime_fin TEXT NOT NULL,
  CHECK (datetime_ini < datetime_fin),
  FOREIGN KEY (id_actividad) REFERENCES Actividad(id_actividad)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_reserva_instalacion_rango
  ON Reserva_Instalacion(id_instalacion, datetime_ini, datetime_fin);

CREATE INDEX IF NOT EXISTS idx_bloqueo_actividad_rango
  ON Bloqueo_por_Actividad(id_actividad, datetime_ini, datetime_fin);

CREATE INDEX IF NOT EXISTS idx_actividad_instalacion
  ON Actividad(id_instalacion);

CREATE INDEX IF NOT EXISTS idx_actividad_periodo_oficial
  ON Actividad(id_periodo_oficial);