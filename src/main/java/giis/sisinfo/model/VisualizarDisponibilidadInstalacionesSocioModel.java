package giis.sisinfo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import giis.sisinfo.dto.DiaReservaSocioDTO;
import giis.sisinfo.dto.HoraReservaSocioDTO;
import giis.sisinfo.util.Database;

public class VisualizarDisponibilidadInstalacionesSocioModel {

	private Database db = new Database();

	private static final LocalTime hora_apertura = LocalTime.of(9, 0);
	private static final LocalTime hora_cierre = LocalTime.of(22, 0);
	private static final int duracion = 60;
	
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

			resultado.add(new DiaReservaSocioDTO(diaSemana(fecha), fecha.toString(), estado));
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
	
	 public boolean estaLibre(int idInstalacion, LocalDateTime inicio, LocalDateTime fin) {

	        String sql1 = "SELECT COUNT(*) FROM Reserva_Instalacion " +
	                      "WHERE id_instalacion=? AND datetime_ini < ? AND datetime_fin > ?";

	        long c1 = ((Number) db.executeQueryArray(sql1, idInstalacion, fin.toString(), inicio.toString()).get(0)[0]).longValue();
	        if (c1 > 0) return false;

	        String sql2 = "SELECT COUNT(*) " +
	                      "FROM Bloqueo_por_Actividad b " +
	                      "JOIN Actividad a ON a.id_actividad=b.id_actividad " +
	                      "WHERE a.id_instalacion=? AND b.datetime_ini < ? AND b.datetime_fin > ?";

	        long c2 = ((Number) db.executeQueryArray(sql2, idInstalacion, fin.toString(), inicio.toString()).get(0)[0]).longValue();

	        return c2 == 0;
	    }
	 
	 private static LocalDateTime toLdt(Object o) {
			if (o == null)
				return null;
			String s = o.toString().replace('T', ' ').trim();
			if (s.length() == 16)
				s = s + ":00";
			return LocalDateTime.parse(s.replace(' ', 'T'));
		}
	 
	 public List<HoraReservaSocioDTO> getHorasDia(int idInstalacion, LocalDate fecha, int idSocioActual) {

	        String sqlReservas = "SELECT r.datetime_ini, r.datetime_fin, r.id_socio, s.nombre, s.apellidos FROM Reserva_Instalacion r JOIN Socio s ON s.id_socio = r.id_socio WHERE r.id_instalacion=? AND date(r.datetime_ini)=?";

	        List<Object[]> filasReservas = db.executeQueryArray(sqlReservas, idInstalacion, fecha.toString());
	        List<Object[]> reservas = new ArrayList<>();
	        
	        for (Object[] r : filasReservas) {
	            reservas.add(new Object[]{ toLdt(r[0]), toLdt(r[1]), ((Number) r[2]).intValue(),r[3]+ " " + r[4] });
	        }

	        String sqlBloqueos = "SELECT b.datetime_ini, b.datetime_fin,a.nombre " +
	                             "FROM Bloqueo_por_Actividad b " +
	                             "JOIN Actividad a ON a.id_actividad = b.id_actividad " +
	                             "WHERE a.id_instalacion=? AND date(b.datetime_ini)=?";

	        List<Object[]> filasBloqueos = db.executeQueryArray(sqlBloqueos, idInstalacion, fecha.toString());
	        List<Object[]> bloqueos = new ArrayList<>();
	        for (Object[] r : filasBloqueos) {
	            bloqueos.add(new Object[]{ toLdt(r[0]), toLdt(r[1]), r[2] });
	        }

	        List<HoraReservaSocioDTO> resultado = new ArrayList<>();

	        for (LocalTime hora = hora_apertura; hora.isBefore(hora_cierre); hora = hora.plusMinutes(duracion)) {
	            LocalDateTime inicioSlot = LocalDateTime.of(fecha, hora);
	            LocalDateTime finSlot = inicioSlot.plusMinutes(duracion);

	            Integer idSocioReserva = null;
	            String socioReserva = null;

	            for (Object[] r : reservas) {
	                LocalDateTime ini = (LocalDateTime) r[0];
	                LocalDateTime fin = (LocalDateTime) r[1];

	                if (ini.isBefore(finSlot) && fin.isAfter(inicioSlot)) {
	                	idSocioReserva = (Integer) r[2];
	                    socioReserva = (String) r[3];
	                    break;
	                }
	            }
	            String nombreActividad = null;

	            for (Object[] b : bloqueos) {
	                LocalDateTime ini = (LocalDateTime) b[0];
	                LocalDateTime fin = (LocalDateTime) b[1];

	                if (ini.isBefore(finSlot) && fin.isAfter(inicioSlot)) {
	                    nombreActividad = (String) b[2];
	                    break;
	                }
	            }


	            String estado;
	            String motivo;

	            if (nombreActividad != null) {
	                estado = "RESERVADA POR ACTIVIDAD";
	                motivo = nombreActividad;
	            } else if (idSocioReserva != null) {
	            	if(idSocioReserva == idSocioActual) {
	            		estado = "RESERVADA POR " + socioReserva;
	            		motivo = socioReserva;
	            	}else {
	            		estado = "RESERVADA POR SOCIO";
	            		motivo = "";
	            		
	            	}
	            } else {
	                estado = "LIBRE";
	                motivo = "";
	            }

	            resultado.add(new HoraReservaSocioDTO(
	                    hora.toString(),
	                    finSlot.toLocalTime().toString(),
	                    estado,
	                    motivo
	            ));
	        }

	        return resultado;
	    }
	 
	 private String diaSemana(LocalDate fecha) {
		    switch (fecha.getDayOfWeek()) {
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
