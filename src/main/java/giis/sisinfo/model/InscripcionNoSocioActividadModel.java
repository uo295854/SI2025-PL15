package giis.sisinfo.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import giis.sisinfo.util.Database;

public class InscripcionNoSocioActividadModel {

	private final Database db = new Database();
	private final QueryRunner runner = new QueryRunner();

	private static final DateTimeFormatter DB_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter UI_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter DB_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public List<ActividadItem> getActividadesDisponiblesParaNoSocio() {
		String sql = """
			SELECT DISTINCT a.id_actividad, a.nombre
			FROM Actividad a
			INNER JOIN PeriodoInscripcion pi
				ON pi.id_periodo_inscripcion = a.id_periodo_inscripcion
			WHERE a.estado = 'ACTIVA'
			  AND date('now') <= date(pi.fecha_fin_nosocio)
			ORDER BY a.nombre
			""";

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			List<Object[]> rows = runner.query(con, sql, new ArrayListHandler());
			List<ActividadItem> res = new ArrayList<>();

			for (Object[] r : rows) {
				res.add(new ActividadItem(
					((Number) r[0]).intValue(),
					(String) r[1]
				));
			}
			return res;
		} catch (SQLException e) {
			throw new RuntimeException("Error al cargar actividades para no socios", e);
		}
	}

	public List<FechaItem> getFechasDeActividad(int idActividad) {
		String sql = """
			SELECT DISTINCT date(b.datetime_ini) as fecha
			FROM Bloqueo_por_Actividad b
			WHERE b.id_actividad = ?
			ORDER BY fecha
			""";

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			List<Object[]> rows = runner.query(con, sql, new ArrayListHandler(), idActividad);
			List<FechaItem> res = new ArrayList<>();

			for (Object[] r : rows) {
				String fechaDb = (String) r[0];
				LocalDate fecha = LocalDate.parse(fechaDb, DB_DATE);
				res.add(new FechaItem(fechaDb, fecha.format(UI_DATE)));
			}
			return res;
		} catch (SQLException e) {
			throw new RuntimeException("Error al cargar fechas de la actividad", e);
		}
	}

	public ActividadDetalle getDetalleActividad(int idActividad, String fechaDb) {
		String sqlActividad = """
			SELECT
				a.id_actividad,
				a.nombre,
				i.nombre_instalacion,
				a.duracion,
				a.aforo,
				a.cuota_nosocio,
				pi.fecha_fin_socio,
				pi.fecha_fin_nosocio
			FROM Actividad a
			INNER JOIN Instalacion i
				ON i.id_instalacion = a.id_instalacion
			INNER JOIN PeriodoInscripcion pi
				ON pi.id_periodo_inscripcion = a.id_periodo_inscripcion
			WHERE a.id_actividad = ?
			""";

		String sqlHorario = """
			SELECT time(MIN(b.datetime_ini)), time(MAX(b.datetime_fin))
			FROM Bloqueo_por_Actividad b
			WHERE b.id_actividad = ?
			  AND date(b.datetime_ini) = ?
			""";

		String sqlInscritos = """
			SELECT COUNT(*)
			FROM Inscripcion_Actividad ia
			WHERE ia.id_actividad = ?
			  AND date(ia.fecha_sesion) = ?
			  AND ia.estado IN ('ACTIVA','EN_ESPERA')
			""";

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			List<Object[]> actividadRows = runner.query(con, sqlActividad, new ArrayListHandler(), idActividad);
			if (actividadRows.isEmpty()) {
				throw new RuntimeException("Actividad no encontrada");
			}
			Object[] act = actividadRows.get(0);

			List<Object[]> horarioRows = runner.query(con, sqlHorario, new ArrayListHandler(), idActividad, fechaDb);
			Object[] horario = horarioRows.isEmpty() ? new Object[] { null, null } : horarioRows.get(0);

			Number inscritosNum = runner.query(con, sqlInscritos, new ScalarHandler<Number>(), idActividad, fechaDb);
			long inscritos = inscritosNum != null ? inscritosNum.longValue() : 0L;

			int aforo = ((Number) act[4]).intValue();
			int plazasDisponibles = Math.max(0, aforo - (int) inscritos);

			LocalDate inicioNoSocio = LocalDate.parse((String) act[6], DB_DATE).plusDays(1);
			LocalDate finNoSocio = LocalDate.parse((String) act[7], DB_DATE);

			String fechaInicioPeriodo = inicioNoSocio.format(UI_DATE);
			String fechaFinPeriodo = finNoSocio.format(UI_DATE);

			String horaIni = horario[0] != null ? ((String) horario[0]).substring(0, 5) : "";
			String horaFin = horario[1] != null ? ((String) horario[1]).substring(0, 5) : "";
			String horarioTexto = (horaIni.isBlank() || horaFin.isBlank()) ? "" : (horaIni + " - " + horaFin);

			return new ActividadDetalle(
				((Number) act[0]).intValue(),
				(String) act[1],
				(String) act[2],
				horarioTexto,
				((Number) act[3]).intValue() + " min",
				plazasDisponibles,
				((Number) act[5]).doubleValue(),
				fechaInicioPeriodo + " - " + fechaFinPeriodo
			);

		} catch (SQLException e) {
			throw new RuntimeException("Error al cargar el detalle de la actividad", e);
		}
	}

	public List<NoSocioItem> getNoSocios() {
		String sql = """
			SELECT id_nosocio, dni, nombre
			FROM NoSocio
			ORDER BY nombre, dni
			""";

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			List<Object[]> rows = runner.query(con, sql, new ArrayListHandler());
			List<NoSocioItem> res = new ArrayList<>();

			for (Object[] r : rows) {
				res.add(new NoSocioItem(
					((Number) r[0]).intValue(),
					(String) r[1],
					(String) r[2]
				));
			}
			return res;
		} catch (SQLException e) {
			throw new RuntimeException("Error al cargar no socios", e);
		}
	}

	public String getNombreNoSocio(int idNoSocio) {
		String sql = "SELECT nombre FROM NoSocio WHERE id_nosocio = ?";

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			String nombre = runner.query(con, sql, new ScalarHandler<String>(), idNoSocio);
			return nombre != null ? nombre : "";
		} catch (SQLException e) {
			throw new RuntimeException("Error al obtener el nombre del no socio", e);
		}
	}

	public void inscribirNoSocioExistente(int idActividad, String fechaDb, int idNoSocio) {
		validarPrecondicionesInscripcion(idActividad, fechaDb, idNoSocio);

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			con.setAutoCommit(false);

			try {
				int idInscripcion = insertarInscripcion(con, idActividad, fechaDb, null, idNoSocio);
				insertarPagoYReciboNoSocio(con, idActividad, idInscripcion, idNoSocio, fechaDb);
				con.commit();
			} catch (Exception e) {
				con.rollback();
				throw e;
			} finally {
				con.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error al inscribir no socio existente", e);
		}
	}

	public void inscribirNuevoNoSocio(int idActividad, String fechaDb, String nombre, String dni) {
		validarDatosNuevoNoSocio(nombre, dni);

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			con.setAutoCommit(false);

			try {
				Integer idExistente = buscarIdNoSocioPorDni(con, dni.trim());
				int idNoSocio = (idExistente != null) ? idExistente : insertarNoSocio(con, nombre.trim(), dni.trim());

				validarPrecondicionesInscripcion(idActividad, fechaDb, idNoSocio);

				int idInscripcion = insertarInscripcion(con, idActividad, fechaDb, null, idNoSocio);
				insertarPagoYReciboNoSocio(con, idActividad, idInscripcion, idNoSocio, fechaDb);

				con.commit();
			} catch (Exception e) {
				con.rollback();
				throw e;
			} finally {
				con.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error al inscribir nuevo no socio", e);
		}
	}

	private void validarPrecondicionesInscripcion(int idActividad, String fechaDb, int idNoSocio) {
		if (!estaEnPeriodoNoSocio(idActividad)) {
			throw new RuntimeException("La inscripción solo puede realizarse durante el periodo de inscripción de no socios.");
		}

		if (getPlazasDisponibles(idActividad, fechaDb) <= 0) {
			throw new RuntimeException("No hay plazas disponibles para la fecha seleccionada.");
		}

		if (existeInscripcionNoSocio(idActividad, fechaDb, idNoSocio)) {
			throw new RuntimeException("Ese no socio ya está inscrito en esta actividad para la fecha seleccionada.");
		}
	}

	private void validarDatosNuevoNoSocio(String nombre, String dni) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new RuntimeException("El nombre del no socio es obligatorio.");
		}
		if (dni == null || dni.trim().isEmpty()) {
			throw new RuntimeException("El DNI del no socio es obligatorio.");
		}
	}

	private boolean estaEnPeriodoNoSocio(int idActividad) {
		String sql = """
			SELECT COUNT(*)
			FROM Actividad a
			INNER JOIN PeriodoInscripcion pi
				ON pi.id_periodo_inscripcion = a.id_periodo_inscripcion
			WHERE a.id_actividad = ?
			  AND date('now') > date(pi.fecha_fin_socio)
			  AND date('now') <= date(pi.fecha_fin_nosocio)
			""";

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			Number countNum = runner.query(con, sql, new ScalarHandler<Number>(), idActividad);
			long count = countNum != null ? countNum.longValue() : 0L;
			return count > 0;
		} catch (SQLException e) {
			throw new RuntimeException("Error al validar el periodo de inscripción", e);
		}
	}

	private int getPlazasDisponibles(int idActividad, String fechaDb) {
		String sqlAforo = "SELECT aforo FROM Actividad WHERE id_actividad = ?";
		String sqlOcupadas = """
			SELECT COUNT(*)
			FROM Inscripcion_Actividad
			WHERE id_actividad = ?
			  AND date(fecha_sesion) = ?
			  AND estado IN ('ACTIVA','EN_ESPERA')
			""";

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			Number aforoNum = runner.query(con, sqlAforo, new ScalarHandler<Number>(), idActividad);
			int aforo = aforoNum != null ? aforoNum.intValue() : 0;

			Number ocupadasNum = runner.query(con, sqlOcupadas, new ScalarHandler<Number>(), idActividad, fechaDb);
			long ocupadas = ocupadasNum != null ? ocupadasNum.longValue() : 0L;

			return Math.max(0, aforo - (int) ocupadas);
		} catch (SQLException e) {
			throw new RuntimeException("Error al consultar plazas disponibles", e);
		}
	}

	private boolean existeInscripcionNoSocio(int idActividad, String fechaDb, int idNoSocio) {
		String sql = """
			SELECT COUNT(*)
			FROM Inscripcion_Actividad
			WHERE id_actividad = ?
			  AND date(fecha_sesion) = ?
			  AND id_nosocio = ?
			  AND estado IN ('ACTIVA','EN_ESPERA')
			""";

		try (Connection con = DriverManager.getConnection(db.getUrl())) {
			Number countNum = runner.query(con, sql, new ScalarHandler<Number>(), idActividad, fechaDb, idNoSocio);
			long count = countNum != null ? countNum.longValue() : 0L;
			return count > 0;
		} catch (SQLException e) {
			throw new RuntimeException("Error al comprobar inscripciones existentes", e);
		}
	}

	private Integer buscarIdNoSocioPorDni(Connection con, String dni) throws SQLException {
		String sql = "SELECT id_nosocio FROM NoSocio WHERE dni = ?";
		Number idNum = runner.query(con, sql, new ScalarHandler<Number>(), dni);
		return idNum != null ? idNum.intValue() : null;
	}

	private int insertarNoSocio(Connection con, String nombre, String dni) throws SQLException {
		String sql = "INSERT INTO NoSocio(nombre, dni) VALUES(?, ?)";
		runner.update(con, sql, nombre, dni);

		Number idNum = runner.query(con,
				"SELECT id_nosocio FROM NoSocio WHERE dni = ?",
				new ScalarHandler<Number>(), dni);

		if (idNum == null) {
			throw new RuntimeException("No se pudo recuperar el id del no socio insertado.");
		}
		return idNum.intValue();
	}

	private int insertarInscripcion(Connection con, int idActividad, String fechaDb, Integer idSocio, Integer idNoSocio)
			throws SQLException {

		String fechaInscripcion = LocalDateTime.now().format(DB_DATETIME);
		String fechaSesion = LocalDate.parse(fechaDb, DB_DATE).atStartOfDay().format(DB_DATETIME);

		String sql = """
			INSERT INTO Inscripcion_Actividad
				(id_socio, id_nosocio, id_actividad, fecha_inscripcion, fecha_sesion, estado)
			VALUES (?, ?, ?, ?, ?, 'ACTIVA')
			""";

		runner.update(con, sql, idSocio, idNoSocio, idActividad, fechaInscripcion, fechaSesion);

		String sqlId = """
			SELECT id_inscripcion
			FROM Inscripcion_Actividad
			WHERE id_actividad = ?
			  AND fecha_inscripcion = ?
			  AND fecha_sesion = ?
			  AND (
			    (id_socio IS NULL AND ? IS NULL) OR id_socio = ?
			  )
			  AND (
			    (id_nosocio IS NULL AND ? IS NULL) OR id_nosocio = ?
			  )
			ORDER BY id_inscripcion DESC
			LIMIT 1
			""";

		Number idInscripcionNum = runner.query(con, sqlId, new ScalarHandler<Number>(),
				idActividad, fechaInscripcion, fechaSesion,
				idSocio, idSocio,
				idNoSocio, idNoSocio);

		if (idInscripcionNum == null) {
			throw new RuntimeException("No se pudo recuperar la inscripción insertada.");
		}
		return idInscripcionNum.intValue();
	}

	private void insertarPagoYReciboNoSocio(Connection con, int idActividad, int idInscripcion, int idNoSocio, String fechaDb)
			throws SQLException {

		String sqlDatos = """
			SELECT 
				a.nombre,
				a.cuota_nosocio,
				a.duracion,
				i.nombre_instalacion,
				ns.nombre,
				ns.dni,
				time(MIN(b.datetime_ini)) AS hora_inicio,
				time(MAX(b.datetime_fin)) AS hora_fin
			FROM Actividad a
			JOIN Instalacion i ON i.id_instalacion = a.id_instalacion
			JOIN NoSocio ns ON ns.id_nosocio = ?
			LEFT JOIN Bloqueo_por_Actividad b 
				ON b.id_actividad = a.id_actividad
			   AND date(b.datetime_ini) = ?
			WHERE a.id_actividad = ?
			GROUP BY a.id_actividad, a.nombre, a.cuota_nosocio, a.duracion, i.nombre_instalacion, ns.nombre, ns.dni
			""";

		List<Object[]> rows = runner.query(con, sqlDatos, new ArrayListHandler(), idNoSocio, fechaDb, idActividad);
		if (rows.isEmpty()) {
			throw new RuntimeException("No se pudieron recuperar los datos del recibo.");
		}

		Object[] row = rows.get(0);

		String nombreActividad = (String) row[0];
		double importe = ((Number) row[1]).doubleValue();
		int duracion = row[2] != null ? ((Number) row[2]).intValue() : 0;
		String nombreInstalacion = (String) row[3];
		String nombreNoSocio = (String) row[4];
		String dniNoSocio = (String) row[5];
		String horaInicio = row[6] != null ? ((String) row[6]).substring(0, 5) : "";
		String horaFin = row[7] != null ? ((String) row[7]).substring(0, 5) : "";

		String fechaPago = LocalDateTime.now().format(DB_DATETIME);

		String sqlPago = """
			INSERT INTO Pago(id_socio, id_nosocio, id_actividad, id_inscripcion, importe, concepto, fecha, estado)
			VALUES (?, ?, ?, ?, ?, 'INSCRIPCION', ?, 'PAGADO')
			""";

		runner.update(con, sqlPago, null, idNoSocio, idActividad, idInscripcion, importe, fechaPago);

		generarReciboTxt(
			idInscripcion,
			nombreNoSocio,
			dniNoSocio,
			nombreActividad,
			nombreInstalacion,
			fechaDb,
			horaInicio,
			horaFin,
			duracion,
			importe,
			fechaPago
		);
	}
	
	private void generarReciboTxt(int idInscripcion, String nombreNoSocio, String dniNoSocio,
			String nombreActividad, String nombreInstalacion, String fechaSesionDb,
			String horaInicio, String horaFin, int duracion, double importe, String fechaPagoDb) {

		try {
			Path carpeta = Paths.get(System.getProperty("user.dir"), "recibos");
			Files.createDirectories(carpeta);

			String fechaSesionUi = LocalDate.parse(fechaSesionDb, DB_DATE).format(UI_DATE);

			StringBuilder sb = new StringBuilder();

			sb.append("CENTRO DEPORTIVO SISINFO").append(System.lineSeparator());
			sb.append("Recibo de inscripción de actividad").append(System.lineSeparator());
			sb.append("==================================================").append(System.lineSeparator());
			sb.append("Nº recibo: REC-INS-").append(idInscripcion).append(System.lineSeparator());
			sb.append("Fecha de emisión: ").append(fechaPagoDb).append(System.lineSeparator());
			sb.append(System.lineSeparator());

			sb.append("DATOS DEL INSCRITO").append(System.lineSeparator());
			sb.append("--------------------------------------------------").append(System.lineSeparator());
			sb.append("Nombre: ").append(nombreNoSocio).append(System.lineSeparator());
			sb.append("DNI: ").append(dniNoSocio).append(System.lineSeparator());
			sb.append("Tipo de usuario: No socio").append(System.lineSeparator());
			sb.append(System.lineSeparator());

			sb.append("DATOS DE LA ACTIVIDAD").append(System.lineSeparator());
			sb.append("--------------------------------------------------").append(System.lineSeparator());
			sb.append("Actividad: ").append(nombreActividad).append(System.lineSeparator());
			sb.append("Instalación: ").append(nombreInstalacion).append(System.lineSeparator());
			sb.append("Fecha sesión: ").append(fechaSesionUi).append(System.lineSeparator());
			sb.append("Horario: ").append(horaInicio).append(" - ").append(horaFin).append(System.lineSeparator());
			sb.append("Duración: ").append(duracion).append(" min").append(System.lineSeparator());
			sb.append(System.lineSeparator());

			sb.append("DATOS ECONÓMICOS").append(System.lineSeparator());
			sb.append("--------------------------------------------------").append(System.lineSeparator());
			sb.append("Concepto: INSCRIPCION").append(System.lineSeparator());
			sb.append("Importe abonado: ").append(String.format("%.2f", importe)).append(" €").append(System.lineSeparator());
			sb.append("Estado del pago: PAGADO").append(System.lineSeparator());
			sb.append(System.lineSeparator());

			sb.append("OBSERVACIONES").append(System.lineSeparator());
			sb.append("--------------------------------------------------").append(System.lineSeparator());
			sb.append("Este documento acredita el pago y la inscripción en la actividad indicada.")
			  .append(System.lineSeparator());
			sb.append("Conserve este recibo como justificante.").append(System.lineSeparator());
			sb.append(System.lineSeparator());

			sb.append("Centro Deportivo SisInfo").append(System.lineSeparator());

			String nombreFichero = "recibo_inscripcion_" + idInscripcion + ".txt";
			Path fichero = carpeta.resolve(nombreFichero);

			Files.writeString(fichero, sb.toString(), StandardCharsets.UTF_8);

		} catch (IOException e) {
			throw new RuntimeException("La inscripción se realizó, pero no se pudo generar el recibo TXT.", e);
		}
	}

	// ===== Clases auxiliares =====

	public static class ActividadItem {
		private final int idActividad;
		private final String nombre;

		public ActividadItem(int idActividad, String nombre) {
			this.idActividad = idActividad;
			this.nombre = nombre;
		}

		public int getIdActividad() {
			return idActividad;
		}

		public String getNombre() {
			return nombre;
		}

		@Override
		public String toString() {
			return nombre;
		}
	}

	public static class FechaItem {
		private final String fechaDb;
		private final String fechaUi;

		public FechaItem(String fechaDb, String fechaUi) {
			this.fechaDb = fechaDb;
			this.fechaUi = fechaUi;
		}

		public String getFechaDb() {
			return fechaDb;
		}

		public String getFechaUi() {
			return fechaUi;
		}

		@Override
		public String toString() {
			return fechaUi;
		}
	}

	public static class NoSocioItem {
		private final int idNoSocio;
		private final String dni;
		private final String nombre;

		public NoSocioItem(int idNoSocio, String dni, String nombre) {
			this.idNoSocio = idNoSocio;
			this.dni = dni;
			this.nombre = nombre;
		}

		public int getIdNoSocio() {
			return idNoSocio;
		}

		public String getDni() {
			return dni;
		}

		public String getNombre() {
			return nombre;
		}

		@Override
		public String toString() {
			return dni;
		}
	}

	public static class ActividadDetalle {
		private final int idActividad;
		private final String nombreActividad;
		private final String instalacion;
		private final String horario;
		private final String duracion;
		private final int plazasDisponibles;
		private final double coste;
		private final String periodoInscripcion;

		public ActividadDetalle(int idActividad, String nombreActividad, String instalacion, String horario,
				String duracion, int plazasDisponibles, double coste, String periodoInscripcion) {
			this.idActividad = idActividad;
			this.nombreActividad = nombreActividad;
			this.instalacion = instalacion;
			this.horario = horario;
			this.duracion = duracion;
			this.plazasDisponibles = plazasDisponibles;
			this.coste = coste;
			this.periodoInscripcion = periodoInscripcion;
		}

		public int getIdActividad() {
			return idActividad;
		}

		public String getNombreActividad() {
			return nombreActividad;
		}

		public String getInstalacion() {
			return instalacion;
		}

		public String getHorario() {
			return horario;
		}

		public String getDuracion() {
			return duracion;
		}

		public int getPlazasDisponibles() {
			return plazasDisponibles;
		}

		public double getCoste() {
			return coste;
		}

		public String getPeriodoInscripcion() {
			return periodoInscripcion;
		}
	}
}