package giis.sisinfo.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
			actividades.add((String) row[0]);
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

		String sqlInscritosActivos = """
			SELECT COUNT(*)
			FROM Inscripcion_Actividad ia
			JOIN Actividad a ON ia.id_actividad = a.id_actividad
			WHERE a.nombre = ?
			  AND ia.estado = 'ACTIVA'
		""";

		List<Object[]> rowsInscritos = db.executeQueryArray(sqlInscritosActivos, nombreActividad);
		int inscritosActivos = 0;
		if (!rowsInscritos.isEmpty()) {
			inscritosActivos = ((Number) rowsInscritos.get(0)[0]).intValue();
		}

		int plazasDisponibles = aforo - inscritosActivos;
		if (plazasDisponibles < 0) {
			plazasDisponibles = 0;
		}

		LocalDate hoy = LocalDate.now();
		boolean periodoActivo = !hoy.isBefore(fechaInicioPeriodo) && !hoy.isAfter(fechaFinPeriodo);

		String estadoActualSocio = getEstadoInscripcionSocio(idSocio, nombreActividad);

		String estadoPeriodo;
		boolean inscripcionPermitida;

		if ("ACTIVA".equals(estadoActualSocio)) {
			estadoPeriodo = "INSCRIPCIÓN YA REALIZADA";
			inscripcionPermitida = false;
		} else if ("EN_ESPERA".equals(estadoActualSocio)) {
			estadoPeriodo = "EN LISTA DE ESPERA";
			inscripcionPermitida = false;
		} else if (periodoActivo) {
			estadoPeriodo = "ACTIVO";
			inscripcionPermitida = true;
		} else {
			estadoPeriodo = "CERRADO";
			inscripcionPermitida = false;
		}

		String horario = obtenerHorarioActividad(nombreActividad, fechaSeleccionada);

		return new DatosSesionInscripcion(
			instalacion,
			horario,
			plazasDisponibles,
			estadoPeriodo,
			inscripcionPermitida
		);
	}

	public boolean estaYaInscrito(int idSocio, String nombreActividad, String fechaSeleccionada) {
		return estaYaInscritoEnActividad(idSocio, nombreActividad);
	}

	private boolean estaYaInscritoEnActividad(int idSocio, String nombreActividad) {
		String sql = """
			SELECT COUNT(*)
			FROM Inscripcion_Actividad ia
			JOIN Actividad a ON ia.id_actividad = a.id_actividad
			WHERE ia.id_socio = ?
			  AND a.nombre = ?
			  AND ia.estado IN ('ACTIVA', 'EN_ESPERA')
		""";

		List<Object[]> rows = db.executeQueryArray(sql, idSocio, nombreActividad);
		if (rows.isEmpty()) {
			return false;
		}

		int count = ((Number) rows.get(0)[0]).intValue();
		return count > 0;
	}

	public String getEstadoInscripcionSocio(int idSocio, String nombreActividad) {
		String sql = """
			SELECT ia.estado
			FROM Inscripcion_Actividad ia
			JOIN Actividad a ON ia.id_actividad = a.id_actividad
			WHERE ia.id_socio = ?
			  AND a.nombre = ?
			  AND ia.estado IN ('ACTIVA', 'EN_ESPERA')
			ORDER BY ia.id_inscripcion DESC
			LIMIT 1
		""";

		List<Object[]> rows = db.executeQueryArray(sql, idSocio, nombreActividad);
		if (rows.isEmpty()) {
			return null;
		}

		return (String) rows.get(0)[0];
	}

	public boolean inscribirSocioEnActividad(int idSocio, String nombreActividad, String fechaSeleccionada) {
		try {
			if (estaYaInscritoEnActividad(idSocio, nombreActividad)) {
				return false;
			}

			String sqlActividad = """
				SELECT id_actividad, cuota_socio, aforo
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
			int aforo = ((Number) row[2]).intValue();

			String sqlActivos = """
				SELECT COUNT(*)
				FROM Inscripcion_Actividad
				WHERE id_actividad = ?
				  AND estado = 'ACTIVA'
			""";

			List<Object[]> rowsActivos = db.executeQueryArray(sqlActivos, idActividad);
			int inscritosActivos = ((Number) rowsActivos.get(0)[0]).intValue();

			String estadoInscripcion = inscritosActivos < aforo ? "ACTIVA" : "EN_ESPERA";

			String insertInscripcion = """
				INSERT INTO Inscripcion_Actividad
					(id_socio, id_actividad, fecha_inscripcion, estado)
				VALUES (?, ?, date('now'), ?)
			""";

			db.executeUpdate(insertInscripcion, idSocio, idActividad, estadoInscripcion);

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
			e.printStackTrace();
			return false;
		}
	}

	public boolean cancelarInscripcionYPromover(int idSocio, String nombreActividad) {
		try {
			String sqlDatos = """
				SELECT ia.id_inscripcion, ia.id_actividad
				FROM Inscripcion_Actividad ia
				JOIN Actividad a ON ia.id_actividad = a.id_actividad
				WHERE ia.id_socio = ?
				  AND a.nombre = ?
				  AND ia.estado IN ('ACTIVA', 'EN_ESPERA')
				ORDER BY ia.id_inscripcion DESC
				LIMIT 1
			""";

			List<Object[]> rows = db.executeQueryArray(sqlDatos, idSocio, nombreActividad);
			if (rows.isEmpty()) {
				return false;
			}

			int idInscripcion = ((Number) rows.get(0)[0]).intValue();
			int idActividad = ((Number) rows.get(0)[1]).intValue();

			String sqlCancelar = """
				UPDATE Inscripcion_Actividad
				SET estado = 'CANCELADA'
				WHERE id_inscripcion = ?
			""";

			db.executeUpdate(sqlCancelar, idInscripcion);

			String sqlPago = """
				UPDATE Pago
				SET estado = 'CANCELADO'
				WHERE id_inscripcion = ?
				  AND concepto = 'INSCRIPCION'
				  AND estado = 'PENDIENTE'
			""";

			db.executeUpdate(sqlPago, idInscripcion);

			promoverPrimeroListaEsperaYNotificar(idActividad, nombreActividad);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean promoverPrimeroListaEsperaYNotificar(int idActividad, String nombreActividad) {
		try {
			String sqlSiguiente = """
				SELECT ia.id_inscripcion, ia.id_socio, s.nombre, s.apellidos, s.dni
				FROM Inscripcion_Actividad ia
				JOIN Socio s ON ia.id_socio = s.id_socio
				WHERE ia.id_actividad = ?
				  AND ia.estado = 'EN_ESPERA'
				ORDER BY ia.fecha_inscripcion, ia.id_inscripcion
				LIMIT 1
			""";

			List<Object[]> rows = db.executeQueryArray(sqlSiguiente, idActividad);
			if (rows.isEmpty()) {
				return false;
			}

			int idInscripcion = ((Number) rows.get(0)[0]).intValue();
			int idSocio = ((Number) rows.get(0)[1]).intValue();
			String nombre = (String) rows.get(0)[2];
			String apellidos = (String) rows.get(0)[3];
			String dni = (String) rows.get(0)[4];

			String sqlPromover = """
				UPDATE Inscripcion_Actividad
				SET estado = 'ACTIVA'
				WHERE id_inscripcion = ?
			""";

			db.executeUpdate(sqlPromover, idInscripcion);

			crearAvisoTxtPromocion(idSocio, nombre, apellidos, dni, nombreActividad);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void crearAvisoTxtPromocion(int idSocio, String nombre, String apellidos, String dni, String nombreActividad) {
		FileWriter writer = null;

		try {
			File carpeta = new File("avisos");
			if (!carpeta.exists()) {
				carpeta.mkdirs();
			}

			String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
			String nombreFichero = "avisos/aviso_promocion_socio_" + idSocio + "_" + fechaHora + ".txt";

			writer = new FileWriter(nombreFichero);

			writer.write("AVISO DE PROMOCIÓN DESDE LISTA DE ESPERA\n");
			writer.write("=========================================\n\n");
			writer.write("Socio: " + nombre + " " + apellidos + "\n");
			writer.write("DNI: " + dni + "\n");
			writer.write("ID socio: " + idSocio + "\n\n");
			writer.write("Actividad: " + nombreActividad + "\n");
			writer.write("Fecha del aviso: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n\n");
			writer.write("Se ha liberado una plaza en la actividad y su inscripción ha pasado de EN_ESPERA a ACTIVA.\n");
			writer.write("Puede participar en la actividad.\n");

			writer.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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