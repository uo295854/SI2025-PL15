package giis.sisinfo.model;

import java.util.ArrayList;
import java.util.List;

import giis.sisinfo.util.Database;

public class ReservaInstalacionesModel {

	private Database db = new Database();

	/**
	 * Devuelve las actividades que tienen algo pendiente de resolver:
	 * - conflicto con otra actividad
	 * - o reservas de socios que se solapan con sus bloqueos
	 */
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

	/**
	 * Procesa la reserva automática para una actividad concreta.
	 *
	 * 1. Comprueba si hay conflicto con otra actividad.
	 * 2. Si lo hay, no modifica reservas de socios.
	 * 3. Si no lo hay, elimina las reservas de socios que se solapen.
	 * 4. Devuelve las incidencias para mostrar en la tabla.
	 */
	public ResultadoReserva reservarInstalacionParaActividad(String nombreActividad) {
		validarTexto(nombreActividad, "El nombre de la actividad no puede estar vacío.");

		ActividadInfo actividad = getActividadPorNombre(nombreActividad);
		List<Object[]> incidencias = new ArrayList<>();

		List<Object[]> conflictos = getConflictosConActividades(actividad.idActividad);
		if (!conflictos.isEmpty()) {
			for (Object[] conflicto : conflictos) {
				incidencias.add(new Object[] {
					"-",
					actividad.nombre,
					conflicto[1],
					conflicto[2],
					conflicto[3],
					"Conflicto con actividad: " + conflicto[0]
				});
			}
			return new ResultadoReserva(true, incidencias);
		}

		List<ReservaSolapada> reservasSolapadas = getReservasSolapadas(actividad.idActividad);

		for (ReservaSolapada reserva : reservasSolapadas) {
			eliminarReservaSocio(reserva.idReserva);

			incidencias.add(new Object[] {
				reserva.socio,
				actividad.nombre,
				reserva.instalacion,
				reserva.fecha,
				reserva.horas,
				"Reserva eliminada"
			});
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

		return new ResultadoReserva(false, incidencias);
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

	/**
	 * Devuelve conflictos con otras actividades de la misma instalación.
	 *
	 * Cada fila contiene:
	 * [0] nombre actividad conflictiva
	 * [1] nombre instalación
	 * [2] fecha
	 * [3] horas
	 */
	private List<Object[]> getConflictosConActividades(int idActividad) {
		String sql =
			"SELECT DISTINCT " +
			"  a2.nombre AS actividad_conflictiva, " +
			"  i.nombre_instalacion, " +
			"  substr(b1.datetime_ini, 1, 10) AS fecha, " +
			"  substr(b1.datetime_ini, 12, 5) || '-' || substr(b1.datetime_fin, 12, 5) AS horas " +
			"FROM Bloqueo_por_Actividad b1 " +
			"JOIN Actividad a1 ON a1.id_actividad = b1.id_actividad " +
			"JOIN Instalacion i ON i.id_instalacion = a1.id_instalacion " +
			"JOIN Actividad a2 " +
			"  ON a2.id_instalacion = a1.id_instalacion " +
			" AND a2.id_actividad <> a1.id_actividad " +
			"JOIN Bloqueo_por_Actividad b2 ON b2.id_actividad = a2.id_actividad " +
			"WHERE a1.id_actividad = ? " +
			"  AND b2.datetime_ini < b1.datetime_fin " +
			"  AND b2.datetime_fin > b1.datetime_ini " +
			"ORDER BY fecha, horas";

		return db.executeQueryArray(sql, idActividad);
	}

	/**
	 * Busca reservas de socios que solapan con alguno de los bloqueos de la actividad.
	 */
	private List<ReservaSolapada> getReservasSolapadas(int idActividad) {
		String sql =
			"SELECT DISTINCT " +
			"  r.id_reservains, " +
			"  s.nombre || ' ' || s.apellidos AS socio, " +
			"  i.nombre_instalacion, " +
			"  substr(r.datetime_ini, 1, 10) AS fecha, " +
			"  substr(r.datetime_ini, 12, 5) || '-' || substr(r.datetime_fin, 12, 5) AS horas " +
			"FROM Reserva_Instalacion r " +
			"JOIN Socio s ON s.id_socio = r.id_socio " +
			"JOIN Actividad a ON a.id_actividad = ? " +
			"JOIN Instalacion i ON i.id_instalacion = a.id_instalacion " +
			"JOIN Bloqueo_por_Actividad b ON b.id_actividad = a.id_actividad " +
			"WHERE r.id_instalacion = a.id_instalacion " +
			"  AND r.datetime_ini < b.datetime_fin " +
			"  AND r.datetime_fin > b.datetime_ini " +
			"ORDER BY r.datetime_ini";

		List<Object[]> rows = db.executeQueryArray(sql, idActividad);
		List<ReservaSolapada> reservas = new ArrayList<>();

		for (Object[] row : rows) {
			reservas.add(new ReservaSolapada(
				toInt(row[0]),
				(String) row[1],
				(String) row[2],
				(String) row[3],
				(String) row[4]
			));
		}

		return reservas;
	}

	private void eliminarReservaSocio(int idReserva) {
		String sql = "DELETE FROM Reserva_Instalacion WHERE id_reservains = ?";
		db.executeUpdate(sql, idReserva);
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

	private static class ReservaSolapada {
		private final int idReserva;
		private final String socio;
		private final String instalacion;
		private final String fecha;
		private final String horas;

		public ReservaSolapada(int idReserva, String socio, String instalacion, String fecha, String horas) {
			this.idReserva = idReserva;
			this.socio = socio;
			this.instalacion = instalacion;
			this.fecha = fecha;
			this.horas = horas;
		}
	}
}