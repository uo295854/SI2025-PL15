package giis.sisinfo.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import giis.sisinfo.util.Database;

public class ReservaInstalacionesModel {

	private Database db = new Database();

	public List<String> getActividadesPendientesDeReserva() {
		String sql =
			"SELECT DISTINCT a.nombre " +
			"FROM Actividad a " +
			"WHERE EXISTS ( " +
			"    SELECT 1 " +
			"    FROM Bloqueo_por_Actividad b1 " +
			"    JOIN Actividad a2 " +
			"      ON a2.id_instalacion = a.id_instalacion " +
			"     AND a2.id_actividad <> a.id_actividad " +
			"    JOIN Bloqueo_por_Actividad b2 " +
			"      ON b2.id_actividad = a2.id_actividad " +
			"    WHERE b1.id_actividad = a.id_actividad " +
			"      AND b2.datetime_ini < b1.datetime_fin " +
			"      AND b2.datetime_fin > b1.datetime_ini " +
			") " +
			"OR EXISTS ( " +
			"    SELECT 1 " +
			"    FROM Bloqueo_por_Actividad b " +
			"    JOIN Reserva_Instalacion r " +
			"      ON r.id_instalacion = a.id_instalacion " +
			"     AND r.datetime_ini < b.datetime_fin " +
			"     AND r.datetime_fin > b.datetime_ini " +
			"    WHERE b.id_actividad = a.id_actividad " +
			") " +
			"ORDER BY a.nombre";

		List<Object[]> rows = db.executeQueryArray(sql);
		List<String> actividades = new ArrayList<>();

		for (Object[] row : rows) {
			actividades.add((String) row[0]);
		}
		return actividades;
	}

	public ResultadoReserva reservarInstalacionParaActividad(String nombreActividad) {
		validarTexto(nombreActividad, "El nombre de la actividad no puede estar vacío.");

		ActividadInfo actividad = getActividadPorNombre(nombreActividad);
		List<BloqueActividad> bloques = getBloquesActividad(actividad.idActividad);
		List<Object[]> incidencias = new ArrayList<>();

		boolean hayAlgunConflicto = false;

		for (BloqueActividad bloque : bloques) {
			List<ConflictoActividad> conflictos = getConflictosDeBloque(actividad.idActividad, bloque.idBloqueo);

			if (!conflictos.isEmpty()) {
				hayAlgunConflicto = true;

				for (ConflictoActividad conflicto : conflictos) {
					incidencias.add(new Object[] {
						"-",
						actividad.nombre,
						actividad.nombreInstalacion,
						bloque.fecha,
						bloque.horas,
						"Conflicto con actividad: " + conflicto.nombreActividad
					});
				}
				continue;
			}

			List<ReservaSolapada> reservasSolapadas = getReservasSolapadasDeBloque(
				actividad.idActividad,
				bloque.idBloqueo
			);

			if (reservasSolapadas.isEmpty()) {
				incidencias.add(new Object[] {
					"-",
					actividad.nombre,
					actividad.nombreInstalacion,
					bloque.fecha,
					bloque.horas,
					"Reservada correctamente"
				});
				continue;
			}

			for (ReservaSolapada reserva : reservasSolapadas) {

				String estadoPago = gestionarPagoPorReservaCancelada(
					reserva.idReserva,
					reserva.idSocio,
					actividad.idActividad
				);

				
				generarAvisoCancelacionTXT(
					reserva.idSocio,
					reserva.socio,
					reserva.instalacion,
					reserva.fecha,
					reserva.horas,
					estadoPago
				);

				eliminarReservaSocio(reserva.idReserva);

				incidencias.add(new Object[] {
					reserva.socio,
					actividad.nombre,
					reserva.instalacion,
					reserva.fecha,
					reserva.horas,
					"Reserva socio eliminada + aviso generado"
				});
			}
		}

		if (incidencias.isEmpty()) {
			incidencias.add(new Object[] {
				"-",
				actividad.nombre,
				actividad.nombreInstalacion,
				"-",
				"-",
				"Sin incidencias"
			});
		}

		return new ResultadoReserva(hayAlgunConflicto, incidencias);
	}

	private ActividadInfo getActividadPorNombre(String nombreActividad) {
		String sql =
			"SELECT a.id_actividad, a.nombre, a.id_instalacion, i.nombre_instalacion " +
			"FROM Actividad a " +
			"JOIN Instalacion i ON i.id_instalacion = a.id_instalacion " +
			"WHERE a.nombre = ?";

		List<Object[]> rows = db.executeQueryArray(sql, nombreActividad);

		if (rows.isEmpty()) {
			throw new RuntimeException("No existe ninguna actividad con nombre: " + nombreActividad);
		}

		Object[] row = rows.get(0);
		return new ActividadInfo(
			toInt(row[0]),
			(String) row[1],
			toInt(row[2]),
			(String) row[3]
		);
	}

	private List<BloqueActividad> getBloquesActividad(int idActividad) {
		String sql =
			"SELECT " +
			"  id_bloqueo, " +
			"  datetime_ini, " +
			"  datetime_fin, " +
			"  substr(datetime_ini, 1, 10) AS fecha, " +
			"  substr(datetime_ini, 12, 5) || '-' || substr(datetime_fin, 12, 5) AS horas " +
			"FROM Bloqueo_por_Actividad " +
			"WHERE id_actividad = ? " +
			"ORDER BY datetime_ini";

		List<Object[]> rows = db.executeQueryArray(sql, idActividad);
		List<BloqueActividad> bloques = new ArrayList<>();

		for (Object[] row : rows) {
			bloques.add(new BloqueActividad(
				toInt(row[0]),
				(String) row[1],
				(String) row[2],
				(String) row[3],
				(String) row[4]
			));
		}
		return bloques;
	}

	private List<ConflictoActividad> getConflictosDeBloque(int idActividad, int idBloqueo) {
		String sql =
			"SELECT DISTINCT " +
			"  a2.nombre " +
			"FROM Bloqueo_por_Actividad b1 " +
			"JOIN Actividad a1 ON a1.id_actividad = b1.id_actividad " +
			"JOIN Actividad a2 " +
			"  ON a2.id_instalacion = a1.id_instalacion " +
			" AND a2.id_actividad <> a1.id_actividad " +
			"JOIN Bloqueo_por_Actividad b2 ON b2.id_actividad = a2.id_actividad " +
			"WHERE a1.id_actividad = ? " +
			"  AND b1.id_bloqueo = ? " +
			"  AND b2.datetime_ini < b1.datetime_fin " +
			"  AND b2.datetime_fin > b1.datetime_ini";

		List<Object[]> rows = db.executeQueryArray(sql, idActividad, idBloqueo);
		List<ConflictoActividad> conflictos = new ArrayList<>();

		for (Object[] row : rows) {
			conflictos.add(new ConflictoActividad((String) row[0]));
		}
		return conflictos;
	}

	private List<ReservaSolapada> getReservasSolapadasDeBloque(int idActividad, int idBloqueo) {
		String sql =
			"SELECT DISTINCT " +
			"  r.id_reservains, " +
			"  r.id_socio, " +
			"  s.nombre || ' ' || s.apellidos AS socio, " +
			"  i.nombre_instalacion, " +
			"  substr(r.datetime_ini, 1, 10) AS fecha, " +
			"  substr(r.datetime_ini, 12, 5) || '-' || substr(r.datetime_fin, 12, 5) AS horas " +
			"FROM Reserva_Instalacion r " +
			"JOIN Socio s ON s.id_socio = r.id_socio " +
			"JOIN Actividad a ON a.id_actividad = ? " +
			"JOIN Instalacion i ON i.id_instalacion = a.id_instalacion " +
			"JOIN Bloqueo_por_Actividad b ON b.id_bloqueo = ? " +
			"WHERE b.id_actividad = a.id_actividad " +
			"  AND r.id_instalacion = a.id_instalacion " +
			"  AND r.datetime_ini < b.datetime_fin " +
			"  AND r.datetime_fin > b.datetime_ini " +
			"ORDER BY r.datetime_ini";

		List<Object[]> rows = db.executeQueryArray(sql, idActividad, idBloqueo);
		List<ReservaSolapada> reservas = new ArrayList<>();

		for (Object[] row : rows) {
			reservas.add(new ReservaSolapada(
				toInt(row[0]),
				toInt(row[1]),
				(String) row[2],
				(String) row[3],
				(String) row[4],
				(String) row[5]
			));
		}
		return reservas;
	}

	private String gestionarPagoPorReservaCancelada(int idReserva, int idSocio, int idActividad) {
		Object[] pago = getPagoReserva(idReserva);

		if (pago == null) {
			return "- No existía pago asociado a la reserva.";
		}

		int idPago = toInt(pago[0]);
		double importe = toDouble(pago[1]);
		String estado = (String) pago[2];

		if ("PENDIENTE".equalsIgnoreCase(estado)) {
			cancelarPagoPendiente(idPago);
			return "- Su pago pendiente ha sido cancelado.";
		} 
		else if ("PAGADO".equalsIgnoreCase(estado)) {
			crearPagoDevolucion(idSocio, idActividad, importe);
			return "- Su pago ha sido devuelto (pendiente de procesamiento).";
		}

		return "- Estado de pago no reconocido.";
	}

	private Object[] getPagoReserva(int idReserva) {
		String sql =
			"SELECT id_pago, importe, estado " +
			"FROM Pago " +
			"WHERE id_reservains = ? " +
			"  AND concepto = 'RESERVA' " +
			"ORDER BY id_pago DESC";

		List<Object[]> rows = db.executeQueryArray(sql, idReserva);

		if (rows.isEmpty()) {
			return null;
		}
		return rows.get(0);
	}

	private void cancelarPagoPendiente(int idPago) {
		String sql =
			"UPDATE Pago " +
			"SET estado = 'CANCELADO' " +
			"WHERE id_pago = ?";

		db.executeUpdate(sql, idPago);
	}

	private void crearPagoDevolucion(int idSocio, int idActividad, double importe) {
		String sql =
			"INSERT INTO Pago (id_socio, id_actividad, id_reservains, importe, concepto, fecha, estado) " +
			"VALUES (?, ?, NULL, ?, 'DEVOLUCION', ?, 'PENDIENTE')";

		db.executeUpdate(sql, idSocio, idActividad, importe, nowAsText());
	}

	private void eliminarReservaSocio(int idReserva) {
		String sql = "DELETE FROM Reserva_Instalacion WHERE id_reservains = ?";
		db.executeUpdate(sql, idReserva);
	}

	private String nowAsText() {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return LocalDateTime.now().format(fmt);
	}

	private void validarTexto(String texto, String mensaje) {
		if (texto == null || texto.trim().isEmpty()) {
			throw new RuntimeException(mensaje);
		}
	}

	private int toInt(Object value) {
		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof Long) {
			return ((Long) value).intValue();
		}
		return Integer.parseInt(value.toString());
	}

	private double toDouble(Object value) {
		if (value instanceof Double) {
			return (Double) value;
		}
		if (value instanceof Integer) {
			return ((Integer) value).doubleValue();
		}
		if (value instanceof Long) {
			return ((Long) value).doubleValue();
		}
		return Double.parseDouble(value.toString());
	}

	public static class ResultadoReserva {
		private final boolean conflictoConActividad;
		private final List<Object[]> incidencias;

		public ResultadoReserva(boolean conflictoConActividad, List<Object[]> incidencias) {
			this.conflictoConActividad = conflictoConActividad;
			this.incidencias = incidencias;
		}

		public boolean isConflictoConActividad() {
			return conflictoConActividad;
		}

		public List<Object[]> getIncidencias() {
			return incidencias;
		}
	}

	private static class ActividadInfo {
		private final int idActividad;
		private final String nombre;
		private final int idInstalacion;
		private final String nombreInstalacion;

		public ActividadInfo(int idActividad, String nombre, int idInstalacion, String nombreInstalacion) {
			this.idActividad = idActividad;
			this.nombre = nombre;
			this.idInstalacion = idInstalacion;
			this.nombreInstalacion = nombreInstalacion;
		}
	}

	private static class BloqueActividad {
		private final int idBloqueo;
		private final String datetimeIni;
		private final String datetimeFin;
		private final String fecha;
		private final String horas;

		public BloqueActividad(int idBloqueo, String datetimeIni, String datetimeFin, String fecha, String horas) {
			this.idBloqueo = idBloqueo;
			this.datetimeIni = datetimeIni;
			this.datetimeFin = datetimeFin;
			this.fecha = fecha;
			this.horas = horas;
		}
	}

	private static class ConflictoActividad {
		private final String nombreActividad;

		public ConflictoActividad(String nombreActividad) {
			this.nombreActividad = nombreActividad;
		}
	}

	private static class ReservaSolapada {
		private final int idReserva;
		private final int idSocio;
		private final String socio;
		private final String instalacion;
		private final String fecha;
		private final String horas;

		public ReservaSolapada(int idReserva, int idSocio, String socio, String instalacion, String fecha, String horas) {
			this.idReserva = idReserva;
			this.idSocio = idSocio;
			this.socio = socio;
			this.instalacion = instalacion;
			this.fecha = fecha;
			this.horas = horas;
		}
	}
	
	private void generarAvisoCancelacionTXT(
			int idSocio,
			String nombreSocio,
			String instalacion,
			String fecha,
			String horas,
			String estadoPago) {

		try {
			String nombreArchivo = "aviso_socio_" + idSocio + "_" + System.currentTimeMillis() + ".txt";

			String contenido =
				"Estimado/a " + nombreSocio + ",\n\n" +
				"Le informamos de que su reserva de la instalación '" + instalacion + "'\n" +
				"el día " + fecha + " en horario " + horas + " ha sido cancelada.\n\n" +
				"Motivo:\n" +
				"La instalación ha sido asignada a una actividad prioritaria del centro.\n\n" +
				"Estado del pago:\n" +
				estadoPago + "\n\n" +
				"Disculpe las molestias.\n\n" +
				"Centro Deportivo";

			java.nio.file.Files.write(
				java.nio.file.Paths.get(nombreArchivo),
				contenido.getBytes()
			);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}