package giis.sisinfo.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import giis.sisinfo.dto.DiaReservaSocioDTO;
import giis.sisinfo.dto.HoraReservaSocioDTO;
import giis.sisinfo.dto.SocioDTO;
import giis.sisinfo.util.Database;

public class ReservaInstalacionAdminSocioModel {

	private Database db = new Database();

	private static final LocalTime hora_apertura = LocalTime.of(9, 0);
	private static final LocalTime hora_cierre = LocalTime.of(22, 0);
	private static final int duracion = 60;

	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public List<SocioDTO> buscadorSocios(String apellidos, String nombre, String numSocio) {

		String sql = "SELECT id_socio, num_socio, nombre,apellidos, email, telefono " + "FROM Socio "
				+ "WHERE estado='ACTIVO' AND al_corriente_pago=1 "
				+ "  AND (? IS NULL OR CAST(num_socio AS TEXT) LIKE ?) " + "  AND (? IS NULL OR LOWER(nombre) LIKE ?) "
				+ "  AND (? IS NULL OR LOWER(apellidos) LIKE ?) " + "ORDER BY apellidos,nombre";

		String nsocio = comprobador(numSocio);
		String nom = comprobador(nombre);
		String apell = comprobador(apellidos);

		List<Object[]> filas = db.executeQueryArray(sql, nsocio, nsocio, nom, nom, apell, apell);

		List<SocioDTO> resultado = new ArrayList<>();

		for (Object[] f : filas) {
			// posicion 0 para id_socio
			// posicion 1 para num_socio
			// posicion 2 para nombre
			// posicion 3 para apellidos
			// posicion 4 para email
			// posicion 5 para telefono

			int idSocio = ((Number) f[0]).intValue();
			String nu = String.valueOf(f[1]);
			String nombreSocio = String.valueOf(f[2]);
			String apellidosSocio = String.valueOf(f[3]);
			String email = (f[4] == null) ? "" : f[4].toString();
			String tel = (f[5] == null) ? "" : f[5].toString();

			resultado.add(new SocioDTO(idSocio, apellidosSocio, nombreSocio, nu, email, tel));
		}

		return resultado;
	}

	private String comprobador(String s) {
		if (s == null || s.trim().isEmpty())
			return null;

		return "%" + s.trim().toLowerCase() + "%";
	}

	public boolean puedeSocioReservar(int id_socio) {
		String sql = "SELECT estado, al_corriente_pago FROM Socio WHERE id_socio=?";
		List<Object[]> filas = db.executeQueryArray(sql, id_socio);

		if (filas.isEmpty())
			return false;

		String estado = String.valueOf(filas.get(0)[0]);

		boolean alcorriente = ((Number) filas.get(0)[1]).intValue() == 1;

		return "ACTIVO".equalsIgnoreCase(estado) && alcorriente;
	}

	public List<String> getDeportes() {
		String sql = "SELECT DISTINCT tipo_deporte FROM Instalacion WHERE tipo_deporte IS NOT NULL ORDER BY tipo_deporte";
		List<Object[]> filas = db.executeQueryArray(sql);

		List<String> resultado = new ArrayList<>();
		for (Object[] f : filas)
			resultado.add(String.valueOf(f[0]));

		return resultado;
	}

	public List<String> getInstalacionesPorDeporte(String tipoDeporte) {
		String sql = "SELECT nombre_instalacion " + "FROM Instalacion " + "WHERE tipo_deporte=? "
				+ "ORDER BY nombre_instalacion";

		List<Object[]> filas = db.executeQueryArray(sql, tipoDeporte);
		List<String> resultado = new ArrayList<>();

		for (Object[] f : filas)
			resultado.add(String.valueOf(f[0]));

		return resultado;
	}

	public Integer getIdInstalacionPorNombre(String nombreInstalacion) {
		String sql = "SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion=?";
		List<Object[]> filas = db.executeQueryArray(sql, nombreInstalacion);
		if (filas.isEmpty())
			return null;

		return ((Number) filas.get(0)[0]).intValue();
	}

	public List<DiaReservaSocioDTO> getDiasDisponibilidad(int idInstalacion) {
		LocalDate hoy = LocalDate.now();
		List<DiaReservaSocioDTO> resultado = new ArrayList<>();

		for (int i = 0; i < 30; i++) {
			LocalDate fecha = hoy.plusDays(i);

			String estado;
			if (i > 15)
				estado = "NO_RESERVABLE";
			else
				estado = diaCompleto(idInstalacion, fecha) ? "COMPLETO" : "DISPONIBLE";

			resultado.add(new DiaReservaSocioDTO(fecha.getDayOfWeek().toString(), fecha.toString(), estado));
		}
		return resultado;
	}

	private boolean diaCompleto(int idInstalacion, LocalDate fecha) {
		for (LocalTime t = hora_apertura; t.isBefore(hora_cierre); t = t.plusMinutes(duracion)) {
			LocalDateTime ini = LocalDateTime.of(fecha, t);
			LocalDateTime fin = ini.plusMinutes(duracion);
			if (estaLibre(idInstalacion, ini, fin))
				return false;
		}
		return true;
	}

	public List<HoraReservaSocioDTO> getHorasDia(int idInstalacion, LocalDate fecha) {


		String sqlReservas = "SELECT datetime_ini, datetime_fin " + "FROM Reserva_Instalacion "
				+ "WHERE id_instalacion=? " + "AND date(datetime_ini)=?";

		List<Object[]> filasReservas = db.executeQueryArray(sqlReservas, idInstalacion, fecha.toString() 
		);

		List<LocalDateTime[]> reservas = new ArrayList<>();

		for (Object[] r : filasReservas) {
			reservas.add(new LocalDateTime[] { toLdt(r[0]), toLdt(r[1]) });
		}



		String sqlBloqueos = "SELECT b.datetime_ini, b.datetime_fin " + "FROM Bloqueo_por_Actividad b "
				+ "JOIN Actividad a ON a.id_actividad = b.id_actividad " + "WHERE a.id_instalacion=? "
				+ "AND date(b.datetime_ini)=?";

		List<Object[]> filasBloqueos = db.executeQueryArray(sqlBloqueos, idInstalacion, fecha.toString());

		List<LocalDateTime[]> bloqueos = new ArrayList<>();

		for (Object[] r : filasBloqueos) {
			bloqueos.add(new LocalDateTime[] { toLdt(r[0]), toLdt(r[1]) });
		}

		List<HoraReservaSocioDTO> resultado = new ArrayList<>();

		for (LocalTime hora = hora_apertura; hora.isBefore(hora_cierre); hora = hora.plusMinutes(duracion)) {

			LocalDateTime inicioSlot = LocalDateTime.of(fecha, hora);
			LocalDateTime finSlot = inicioSlot.plusMinutes(duracion);

			boolean ocupadaReserva = solapa(reservas, inicioSlot, finSlot);
			boolean ocupadaActividad = solapa(bloqueos, inicioSlot, finSlot);

			String estado = (ocupadaReserva || ocupadaActividad) ? "OCUPADA" : "LIBRE";
			String motivo = ocupadaReserva ? "Reservada: Socio" : (ocupadaActividad ? "Actividad" : "");

			resultado.add(new HoraReservaSocioDTO(hora.toString(), finSlot.toLocalTime().toString(), estado, motivo));
		}

		return resultado;
	}

	public double getCosteInstalacion(int idInstalacion) {
		String sql = "SELECT coste FROM Instalacion WHERE id_instalacion=?";
		List<Object[]> filas = db.executeQueryArray(sql, idInstalacion);
		if (filas.isEmpty())
			return 0.0;

		return ((Number) filas.get(0)[0]).doubleValue();
	}

	public void crearReserva(int idInstalacion, int idSocio, LocalDate fecha, List<LocalTime> horasInicio) {

		if (!puedeSocioReservar(idSocio))
			throw new IllegalStateException("Socio no autorizado para reservar.");

		LocalDate hoy = LocalDate.now();
		if (fecha.isAfter(hoy.plusDays(15)))
			throw new IllegalStateException("No se puede reservar con más de 15 días de antelación.");
		

		List<LocalTime> horas = new ArrayList<>();
		for (LocalTime h : horasInicio) {
			if (h != null && !horas.contains(h)) {
				horas.add(h);
			}
		}
		
		if (horas.isEmpty()) {
			throw new IllegalStateException("Debes seleccionar al menos una hora válida.");
		}

		horas.sort(null);
		
		for(LocalTime h: horas) {
			if(h.isBefore(hora_apertura) || h.plusMinutes(duracion).isAfter(hora_cierre)) {
				throw new IllegalStateException("Alguna de las horas seleccionadas está fuera del horario permitido ");
			}
			LocalDateTime inicio = LocalDateTime.of(fecha, h);
			LocalDateTime fin = inicio.plusMinutes(duracion);
			
			if (!estaLibre(idInstalacion, inicio, fin))
				throw new IllegalStateException("La franja horaria ya no está disponible.");
		}
		
		validarSocioSinSolapamientosConsigoMismo(idSocio,fecha,horas);
		validarMaximoDosHorasSeguidas(idSocio,idInstalacion,fecha,horas);
		validarMaximoTresHorasDia(idSocio,fecha,horas);
		validarMaximoReservas15Dias(idSocio,fecha,horas);

		String sql = "INSERT INTO Reserva_Instalacion (id_instalacion, id_socio, datetime_ini, datetime_fin) "
				+ "VALUES (?,?,?,?)";

		for(LocalTime h: horas) {
			
			LocalDateTime inicio = LocalDateTime.of(fecha, h);
			LocalDateTime fin = inicio.plusMinutes(duracion);
			db.executeUpdate(sql, idInstalacion, idSocio, inicio.format(FMT), fin.format(FMT));

		}
	}

	private void validarMaximoReservas15Dias(int idSocio, LocalDate fecha, List<LocalTime> horas) {
		LocalDate hoy = fecha.minusDays(14);
		LocalDate finPeriodo = fecha;
		
		String sql = "SELECT COUNT(*) FROM Reserva_Instalacion "
				+ "WHERE id_socio=? "
				+ "AND date(datetime_ini) >= ? "
				+ "AND date(datetime_ini) <= ?";
		
		long reservadas = ((Number) db.executeQueryArray(
				sql, idSocio, hoy.toString(), finPeriodo.toString()).get(0)[0]).longValue();
		
		long total = reservadas + horas.size();
		
		if(total>8) {
			throw new IllegalStateException("No puedes realizar más de 8 reservas cada 15 días ");
		}
	}

	private void validarMaximoTresHorasDia(int idSocio, LocalDate fecha, List<LocalTime> horas) {
		
		String sql = "SELECT COUNT(*) FROM Reserva_Instalacion "
				+ "WHERE id_socio=? AND date(datetime_ini)=?";
		
		long reservadas = ((Number) db.executeQueryArray(
				sql, idSocio, fecha.toString()).get(0)[0]).longValue();
		
		long total = reservadas + horas.size();
		
		if(total>3) {
			throw new IllegalStateException("No puedes reservar más de 3 horas al día ");
		}
		
	}

	private void validarMaximoDosHorasSeguidas(int idSocio, int idInstalacion, LocalDate fecha, List<LocalTime> horas) {

		String sql= "SELECT datetime_ini FROM Reserva_Instalacion "
		+ "WHERE id_socio=? AND id_instalacion=? AND date(datetime_ini)=?";
		
		List<Object[]> filas = db.executeQueryArray(sql, idSocio, idInstalacion, fecha.toString());
		
		List<LocalTime> todashoras = new ArrayList<>();
		
		for (Object[] f : filas) {
			todashoras.add(toLdt(f[0]).toLocalTime());
		}
		
		for (LocalTime h : horas) {
			if (!todashoras.contains(h)) {
				todashoras.add(h);
			}
		}
		
		todashoras.sort(null);

		int consecutivas = 1;

		for (int i = 1; i < todashoras.size(); i++) {
			if (todashoras.get(i - 1).plusMinutes(duracion).equals(todashoras.get(i))) {
				consecutivas++;
				if (consecutivas > 2) {
					throw new IllegalStateException(
						"Un socio no puede reservar más de 2 horas seguidas en la misma instalación"
					);
				}
			} else {
				consecutivas = 1;
			}
		}

		
	}

	private void validarSocioSinSolapamientosConsigoMismo(int idSocio, LocalDate fecha, List<LocalTime> horas) {

		String sql = "SELECT COUNT(*) FROM Reserva_Instalacion "
				+ "WHERE id_socio=? AND datetime_ini < ? AND datetime_fin > ?";
		
		for (LocalTime h : horas) {
			LocalDateTime ini = LocalDateTime.of(fecha, h);
			LocalDateTime fin = ini.plusMinutes(duracion);

			long c = ((Number) db.executeQueryArray(sql, idSocio, fin.format(FMT), ini.format(FMT)).get(0)[0]).longValue();

			if (c > 0) {
				throw new IllegalStateException("El socio ya tiene otra reserva en alguna de las horas seleccionadas.");
			}
		}
		
	}

	public boolean estaLibre(int idInstalacion, LocalDateTime inicio, LocalDateTime fin) {

		String sql1 = "SELECT COUNT(*) FROM Reserva_Instalacion " + "WHERE id_instalacion=? " + "AND datetime_ini < ? "
				+ "AND datetime_fin > ?";

		long c1 = ((Number) db.executeQueryArray(sql1, idInstalacion, fin.format(FMT), inicio.format(FMT)).get(0)[0])
				.longValue();

		if (c1 > 0)
			return false;

		String sql2 = "SELECT COUNT(*) " + "FROM Bloqueo_por_Actividad b "
				+ "JOIN Actividad a ON a.id_actividad=b.id_actividad " + "WHERE a.id_instalacion=? "
				+ "AND b.datetime_ini < ? " + "AND b.datetime_fin > ?";

		long c2 = ((Number) db.executeQueryArray(sql2, idInstalacion, fin.format(FMT), inicio.format(FMT)).get(0)[0])
				.longValue();

		return c2 == 0;
	}

	private static boolean solapa(List<LocalDateTime[]> rangos, LocalDateTime ini, LocalDateTime fin) {
		for (LocalDateTime[] r : rangos) {
			if (r[0].isBefore(fin) && r[1].isAfter(ini))
				return true;
		}
		return false;
	}

	private static LocalDateTime toLdt(Object o) {
		if (o == null)
			return null;
		String s = o.toString().replace('T', ' ').trim();
		if (s.length() == 16)
			s = s + ":00";
		return LocalDateTime.parse(s.replace(' ', 'T'));
	}
	
	
}
