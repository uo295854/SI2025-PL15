package giis.sisinfo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.sisinfo.controller.InscripcionActividadController.DatosSesionInscripcion;
import giis.sisinfo.util.Database;

public class InscripcionActividadModel {

	private Database db = new Database();

	public List<String> getActividadesInscribiblesParaSocio(int idSocio) {
		List<String> actividades = new ArrayList<>();

		String sql = """
			SELECT DISTINCT a.nombre
			FROM Actividad a
			JOIN PeriodoInscripcion pi
				ON a.id_periodo_inscripcion = pi.id_periodo_inscripcion
			WHERE date('now') BETWEEN pi.fecha_inicio_socio AND pi.fecha_fin_socio
			ORDER BY a.nombre
		""";

		List<Object[]> rows = db.executeQueryArray(sql);
		for (Object[] row : rows) {
			String nombreActividad = (String) row[0];
			List<String> fechas = getFechasDisponiblesDeActividad(nombreActividad);

			boolean tieneAlgunaFechaLibre = false;
			for (String fecha : fechas) {
				if (!estaYaInscrito(idSocio, nombreActividad, fecha)) {
					tieneAlgunaFechaLibre = true;
					break;
				}
			}

			if (tieneAlgunaFechaLibre) {
				actividades.add(nombreActividad);
			}
		}

		return actividades;
	}

	public List<String> getFechasDisponiblesDeActividad(String nombreActividad) {
		List<String> fechas = new ArrayList<>();

		String sql = """
			SELECT fecha_inicio, fecha_fin, dias
			FROM Actividad
			WHERE nombre = ?
		""";

		List<Object[]> rows = db.executeQueryArray(sql, nombreActividad);
		if (rows.isEmpty()) {
			return fechas;
		}

		Object[] row = rows.get(0);

		LocalDate inicio = LocalDate.parse((String) row[0]);
		LocalDate fin = LocalDate.parse((String) row[1]);
		int diasMask = ((Number) row[2]).intValue();

		LocalDate fecha = inicio;
		while (!fecha.isAfter(fin)) {
			if (coincideConDiasSeleccionados(fecha, diasMask)) {
				fechas.add(fecha.toString());
			}
			fecha = fecha.plusDays(1);
		}

		return fechas;
	}

	private boolean coincideConDiasSeleccionados(LocalDate fecha, int diasMask) {
		int bitDia = convertirDayOfWeekABit(fecha.getDayOfWeek().getValue());
		return (diasMask & bitDia) != 0;
	}

	private int convertirDayOfWeekABit(int dayOfWeek) {
		switch (dayOfWeek) {
		case 1: return 1;
		case 2: return 2;
		case 3: return 4;
		case 4: return 8;
		case 5: return 16;
		case 6: return 32;
		case 7: return 64;
		default: return 0;
		}
	}

		
	public DatosSesionInscripcion getDatosSesion(String nombreActividad, String fechaSeleccionada, int idSocio) {
		String sql = """
			SELECT i.nombre_instalacion,
			       a.aforo,
			       pi.fecha_inicio_socio,
			       pi.fecha_fin_socio
			FROM Actividad a
			JOIN Instalacion i
				ON a.id_instalacion = i.id_instalacion
			JOIN PeriodoInscripcion pi
				ON a.id_periodo_inscripcion = pi.id_periodo_inscripcion
			WHERE a.nombre = ?
		""";

		List<Object[]> rows = db.executeQueryArray(sql, nombreActividad);
		if (rows.isEmpty()) {
			return null;
		}

		Object[] row = rows.get(0);

		String instalacion = (String) row[0];
		int aforo = ((Number) row[1]).intValue();
		LocalDate fechaInicioPeriodo = LocalDate.parse((String) row[2]);
		LocalDate fechaFinPeriodo = LocalDate.parse((String) row[3]);

		String sqlInscritos = """
			SELECT COUNT(*)
			FROM Inscripcion_Actividad ia
			JOIN Actividad a ON ia.id_actividad = a.id_actividad
			WHERE a.nombre = ?
			  AND ia.fecha_sesion = ?
			  AND ia.estado = 'ACTIVA'
		""";

		List<Object[]> rowsInscritos = db.executeQueryArray(sqlInscritos, nombreActividad, fechaSeleccionada);
		int inscritos = 0;
		if (!rowsInscritos.isEmpty()) {
			inscritos = ((Number) rowsInscritos.get(0)[0]).intValue();
		}

		int plazasDisponibles = aforo - inscritos;
		if (plazasDisponibles < 0) {
			plazasDisponibles = 0;
		}

		LocalDate hoy = LocalDate.now();
		boolean periodoActivo = !hoy.isBefore(fechaInicioPeriodo) && !hoy.isAfter(fechaFinPeriodo);
		boolean yaInscrito = estaYaInscrito(idSocio, nombreActividad, fechaSeleccionada);

		String estadoPeriodo;
		if (yaInscrito) {
			estadoPeriodo = "YA INSCRITO";
		} else if (periodoActivo) {
			estadoPeriodo = "ACTIVO";
		} else {
			estadoPeriodo = "CERRADO";
		}

		String horario = obtenerHorarioActividad(nombreActividad, fechaSeleccionada);

		return new DatosSesionInscripcion(
			instalacion,
			horario,
			plazasDisponibles,
			estadoPeriodo,
			periodoActivo && !yaInscrito
		);
	}

	public boolean estaYaInscrito(int idSocio, String nombreActividad, String fechaSeleccionada) {
		String sql = """
			SELECT COUNT(*)
			FROM Inscripcion_Actividad ia
			JOIN Actividad a ON ia.id_actividad = a.id_actividad
			WHERE ia.id_socio = ?
			  AND a.nombre = ?
			  AND ia.fecha_sesion = ?
			  AND ia.estado = 'ACTIVA'
		""";

		List<Object[]> rows = db.executeQueryArray(sql, idSocio, nombreActividad, fechaSeleccionada);
		if (rows.isEmpty()) {
			return false;
		}

		int count = ((Number) rows.get(0)[0]).intValue();
		return count > 0;
	}

	public boolean inscribirSocioEnActividad(int idSocio, String nombreActividad, String fechaSeleccionada) {
		try {
			String sqlActividad = """
				SELECT id_actividad, cuota_socio
				FROM Actividad
				WHERE nombre = ?
			""";

			List<Object[]> rows = db.executeQueryArray(sqlActividad, nombreActividad);
			if (rows.isEmpty()) {
				return false;
			}

			Object[] row = rows.get(0);
			int idActividad = ((Number) row[0]).intValue();
			double importe = ((Number) row[1]).doubleValue();

			String insertInscripcion = """
				INSERT INTO Inscripcion_Actividad
					(id_socio, id_actividad, fecha_sesion, fecha_inscripcion, estado)
				VALUES (?, ?, ?, date('now'), 'ACTIVA')
			""";

			db.executeUpdate(insertInscripcion, idSocio, idActividad, fechaSeleccionada);

			String sqlUltimaInscripcion = "SELECT last_insert_rowid()";
			List<Object[]> idRows = db.executeQueryArray(sqlUltimaInscripcion);
			int idInscripcion = ((Number) idRows.get(0)[0]).intValue();

			String insertPago = """
				INSERT INTO Pago
					(id_socio, id_actividad, id_inscripcion, importe, concepto, fecha, estado)
				VALUES (?, ?, ?, ?, 'INSCRIPCION', date('now'), 'PENDIENTE')
			""";

			db.executeUpdate(insertPago, idSocio, idActividad, idInscripcion, importe);
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	private String obtenerHorarioActividad(String nombreActividad, String fechaSeleccionada) {
		String sql = """
			SELECT time(b.datetime_ini), time(b.datetime_fin)
			FROM Bloqueo_por_Actividad b
			JOIN Actividad a
				ON b.id_actividad = a.id_actividad
			WHERE a.nombre = ?
			  AND date(b.datetime_ini) = ?
			ORDER BY b.datetime_ini
			LIMIT 1
		""";

		List<Object[]> rows = db.executeQueryArray(sql, nombreActividad, fechaSeleccionada);

		if (rows.isEmpty() || rows.get(0)[0] == null || rows.get(0)[1] == null) {
			return "";
		}

		String horaIni = rows.get(0)[0].toString();
		String horaFin = rows.get(0)[1].toString();

		if (horaIni.length() >= 5) {
			horaIni = horaIni.substring(0, 5);
		}
		if (horaFin.length() >= 5) {
			horaFin = horaFin.substring(0, 5);
		}

		return horaIni + " - " + horaFin;
	}
}