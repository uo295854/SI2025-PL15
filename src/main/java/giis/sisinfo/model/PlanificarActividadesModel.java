package giis.sisinfo.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import giis.sisinfo.util.ApplicationException;
import giis.sisinfo.util.Database;
import giis.sisinfo.dto.ActividadDTO;
import giis.sisinfo.dto.PeriodoInscripcionDTO;

public class PlanificarActividadesModel {

	private final Database db = new Database();

	private static final DateTimeFormatter F_DATE = DateTimeFormatter.ISO_LOCAL_DATE;          // yyyy-MM-dd
	private static final DateTimeFormatter F_TIME = DateTimeFormatter.ofPattern("HH:mm");     // HH:mm
	private static final DateTimeFormatter F_DT   = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // sortable TEXT

	// ========== Datos para la View ==========
	public List<String> getNombresInstalaciones() {
		String sql = "SELECT nombre_instalacion FROM Instalacion ORDER BY nombre_instalacion";
		List<Object[]> rows = db.executeQueryArray(sql);
		List<String> res = new ArrayList<>();
		for (Object[] r : rows) res.add((String) r[0]);
		return res;
	}

	public List<String> getNombresPeriodosInscripcion() {
		String sql = "SELECT nombre FROM PeriodoInscripcion ORDER BY id_periodo_inscripcion";
		List<Object[]> rows = db.executeQueryArray(sql);
		List<String> res = new ArrayList<>();
		for (Object[] r : rows) res.add((String) r[0]);
		return res;
	}

	public PeriodoInscripcionDTO getPeriodoInscripcion(String nombre) {
		String sql = "SELECT nombre, fecha_inicio_socio, fecha_fin_socio, fecha_fin_nosocio "
				   + "FROM PeriodoInscripcion WHERE nombre=?";
		List<Object[]> rows = db.executeQueryArray(sql, nombre);
		if (rows.isEmpty())
			throw new ApplicationException("Periodo de inscripción no encontrado: " + nombre);
		Object[] r = rows.get(0);
		return new PeriodoInscripcionDTO((String) r[0], (String) r[1], (String) r[2], (String) r[3]);
	}

	// ========== HU: crear actividad + bloqueos ==========
	public void crearActividad(ActividadDTO dto) {
		Connection conn = null;
		try {
			conn = db.getConnection();
			conn.setAutoCommit(false);

			QueryRunner runner = new QueryRunner();

			long idInstalacion = getIdInstalacion(conn, runner, dto.getInstalacionNombre());
			long idPeriodoIns = getIdPeriodoInscripcion(conn, runner, dto.getPeriodoInscripcionNombre());

			long idPeriodoOficial = getIdPeriodoOficialPorFecha(conn, runner, dto.getFechaInicio(), dto.getFechaFin());

			String tipoBd = mapTipoUiToDb(dto.getTipoUi());

			int diasMask = buildDiasMask(dto.getHorariosPorDia());

			// Insert Actividad
			String sqlInsAct =
					"INSERT INTO Actividad (id_periodo_oficial, id_periodo_inscripcion, id_instalacion, "
				  + "nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion, cuota_socio, cuota_nosocio) "
				  + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

			runner.update(conn, sqlInsAct,
					idPeriodoOficial,
					idPeriodoIns,
					idInstalacion,
					dto.getNombre(),
					tipoBd,
					dto.getAforo(),
					diasMask,
					dto.getDuracionMinutos(),
					dto.getFechaInicio(),
					dto.getFechaFin(),
					dto.getDescripcion(),
					dto.getCuotaSocio(),
					dto.getCuotaNoSocio()
			);

			Number idActNum = runner.query(conn, "SELECT last_insert_rowid()", new ScalarHandler<Number>());
			if (idActNum == null) throw new ApplicationException("No se pudo obtener el id de la actividad creada.");
			long idActividad = idActNum.longValue();
			
			// Generar bloqueos
			LocalDate fIni = LocalDate.parse(dto.getFechaInicio(), F_DATE);
			LocalDate fFin = LocalDate.parse(dto.getFechaFin(), F_DATE);

			for (LocalDate d = fIni; !d.isAfter(fFin); d = d.plusDays(1)) {
				String diaLabel = mapDayOfWeekToLabel(d.getDayOfWeek());
				String[] tramo = dto.getHorariosPorDia().get(diaLabel);
				if (tramo == null) continue; // ese día no está seleccionado

				LocalTime tDe = LocalTime.parse(tramo[0], F_TIME);
				LocalTime tA  = LocalTime.parse(tramo[1], F_TIME);

				LocalDateTime dtIni = d.atTime(tDe);
				LocalDateTime dtFin = d.atTime(tA);

				String sIni = dtIni.format(F_DT);
				String sFin = dtFin.format(F_DT);

				// comprobar solapes con reservas y otros bloqueos de la misma instalacion
				checkConflicts(conn, runner, idInstalacion, sIni, sFin);

				String sqlInsBloq =
						"INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin) "
					  + "VALUES (?,?,?)";
				runner.update(conn, sqlInsBloq, idActividad, sIni, sFin);
			}

			conn.commit();

		} catch (SQLException e) {
			rollbackQuiet(conn);
			throw new ApplicationException("Error SQL creando actividad: " + e.getMessage());
		} catch (RuntimeException e) {
			rollbackQuiet(conn);
			throw e;
		} finally {
			if (conn != null) try { conn.close(); } catch (SQLException e) { /* ignore */ }
		}
	}

	private void rollbackQuiet(Connection conn) {
		if (conn == null) return;
		try { conn.rollback(); } catch (SQLException e) { /* ignore */ }
	}

	private long getIdInstalacion(Connection conn, QueryRunner runner, String nombreInstalacion) throws SQLException {
		String sql = "SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion=?";
		Number n = runner.query(conn, sql, new ScalarHandler<Number>(), nombreInstalacion);
		if (n == null) throw new ApplicationException("Instalación no encontrada: " + nombreInstalacion);
		return n.longValue();
	}

	private long getIdPeriodoInscripcion(Connection conn, QueryRunner runner, String nombrePeriodo) throws SQLException {
		String sql = "SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre=?";
		Number n = runner.query(conn, sql, new ScalarHandler<Number>(), nombrePeriodo);
		if (n == null) throw new ApplicationException("Periodo de inscripción no encontrado: " + nombrePeriodo);
		return n.longValue();
	}

	private long getIdPeriodoOficialPorFecha(Connection conn, QueryRunner runner, String fechaInicio, String fechaFin)
			throws SQLException {

		// Una actividad debe estar dentro de un periodo oficial
		String sql =
				"SELECT id_periodo_oficial FROM PeriodoOficial "
			  + "WHERE fecha_ini <= ? AND fecha_fin >= ? "
			  + "LIMIT 1";
		Number n = runner.query(conn, sql, new ScalarHandler<Number>(), fechaInicio, fechaFin);
		if (n == null) throw new ApplicationException("No existe PeriodoOficial que cubra el rango " + fechaInicio + " .. " + fechaFin);
		return n.longValue();
	}

	private void checkConflicts(Connection conn, QueryRunner runner, long idInstalacion, String ini, String fin)
			throws SQLException {

		String sqlReserva =
				"SELECT COUNT(*) FROM Reserva_Instalacion "
			  + "WHERE id_instalacion=? AND NOT (datetime_fin<=? OR datetime_ini>=?)";
		Number c1 = runner.query(conn, sqlReserva, new ScalarHandler<Number>(), idInstalacion, ini, fin);
		if (c1 != null && c1.longValue() > 0) {
			throw new ApplicationException("Conflicto: existe una reserva de instalación que solapa con " + ini + " - " + fin);
		}

		String sqlBloq =
				"SELECT COUNT(*) "
			  + "FROM Bloqueo_por_Actividad b "
			  + "JOIN Actividad a ON a.id_actividad=b.id_actividad "
			  + "WHERE a.id_instalacion=? AND NOT (b.datetime_fin<=? OR b.datetime_ini>=?)";
		Number c2 = runner.query(conn, sqlBloq, new ScalarHandler<Number>(), idInstalacion, ini, fin);
		if (c2 != null && c2.longValue() > 0) {
			throw new ApplicationException("Conflicto: existe otra actividad/bloqueo que solapa con " + ini + " - " + fin);
		}
	}

	private String mapTipoUiToDb(String tipoUi) {
		if (tipoUi == null) return "DEPORTIVA";
		switch (tipoUi.trim().toLowerCase()) {
			case "cultural": return "CULTURAL";
			case "deportiva": return "DEPORTIVA";
			case "formativa":
			case "clase": return "CLASE";
			case "campeonato": return "CAMPEONATO";
			default: throw new ApplicationException("Tipo de actividad no válido: " + tipoUi);
		}
	}

	private int buildDiasMask(Map<String, String[]> horariosPorDia) {
		int mask = 0;
		for (String dia : horariosPorDia.keySet()) {
			mask |= dayLabelToBit(dia);
		}
		return mask;
	}

	private int dayLabelToBit(String dia) {
		if (dia == null) return 0;
		switch (dia.trim().toLowerCase()) {
			case "lunes": return 1;
			case "martes": return 2;
			case "miércoles":
			case "miercoles": return 4;
			case "jueves": return 8;
			case "viernes": return 16;
			case "sábado":
			case "sabado": return 32;
			case "domingo": return 64;
			default: return 0;
		}
	}

	private String mapDayOfWeekToLabel(DayOfWeek dow) {
		switch (dow) {
			case MONDAY: return "Lunes";
			case TUESDAY: return "Martes";
			case WEDNESDAY: return "Miércoles";
			case THURSDAY: return "Jueves";
			case FRIDAY: return "Viernes";
			case SATURDAY: return "Sábado";
			case SUNDAY: return "Domingo";
			default: return "";
		}
	}
}