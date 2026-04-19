package giis.sisinfo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import giis.sisinfo.util.Database;

public class CancelarReservaInstalacionSocioModel {

	private Database db = new Database();

	
	 public List<Object[]> getReservasActivasSocio(int idSocio) {
	        String sql = "SELECT r.id_reservains, "
					+ "       i.tipo_deporte, "
					+ "       i.nombre_instalacion, "
					+ "       date(r.datetime_ini) as fecha, "
					+ "       time(r.datetime_ini) as hora_entrada, "
					+ "       p.estado "
					+ "FROM Reserva_Instalacion r "
					+ "JOIN Instalacion i ON i.id_instalacion = r.id_instalacion "
					+ "LEFT JOIN Pago p ON p.id_reservains = r.id_reservains "
					+ "WHERE r.id_socio = ? "
					+ "  AND r.estado = 'ACTIVA' "
					+ "  AND date(r.datetime_ini) >= ? "
					+ "ORDER BY r.datetime_ini";

	        List<Object[]> filas = db.executeQueryArray(sql, idSocio, LocalDate.now().toString());
	        List<Object[]> resultado = new ArrayList<>();

	        for (Object[] f : filas) {
	            int idReserva = ((Number) f[0]).intValue();
	            String deporte = String.valueOf(f[1]);
				String instalacion = String.valueOf(f[2]);
				String fecha = String.valueOf(f[3]);
				String dia = diaSemana(LocalDate.parse(fecha));
				String horaEntrada = String.valueOf(f[4]);
				String estadoPago = (f[5] == null) ? "" : String.valueOf(f[5]);

	            resultado.add(new Object[] {idReserva, deporte, instalacion, fecha, dia, horaEntrada, estadoPago});
	        }

	        return resultado;
	    }
	 
	 
	   public void cancelarReserva(int idReserva) {
		   Object[] datosReserva = getDatosReservaACancelar(idReserva);
		   
		   String estadoReserva = String.valueOf(datosReserva[0]);
		   LocalDateTime inicioReserva = toLdt(datosReserva[1]);
		   
		   if(!"ACTIVA".equalsIgnoreCase(estadoReserva)) {
			   throw new IllegalStateException("La reserva no está activa");
		   }
		   
		    if (!LocalDateTime.now().isBefore(inicioReserva)) {
	            throw new IllegalStateException("No se puede cancelar una reserva que ya ha comenzado");
	        }
		    
		    
	        actualizarEstadoPago(idReserva);
	        actualizarEstadoReserva(idReserva);

	   }
	   private Object[] getDatosReservaACancelar(int idReserva) {
	       String sql = "SELECT estado, datetime_ini "
	               + "FROM Reserva_Instalacion "
	               + "WHERE id_reservains = ?";

	       List<Object[]> filas = db.executeQueryArray(sql, idReserva);

	       if (filas.isEmpty()) {
	           throw new IllegalStateException("No existe la reserva seleccionada");
	       }

	       return filas.get(0);
	   }
	   
	   private void actualizarEstadoPago(int idReserva) {
		    String sql = "SELECT id_pago, estado "
	                + "FROM Pago "
	                + "WHERE id_reservains = ?";
		    
		    
		    List<Object[]> filas = db.executeQueryArray(sql, idReserva);
		    
		    if(filas.isEmpty()) {
		    	throw new IllegalStateException("No existe un pago asociado a la reserva");
		    }
		    
		    int idPago = ((Number) filas.get(0)[0]).intValue();
		    
		    String estadoPagoActual = String.valueOf(filas.get(0)[1]);
		    
		    
		    String nuevoEstadoPago;
		    
		    if("PAGADO".equalsIgnoreCase(estadoPagoActual)) {
		    	nuevoEstadoPago = "DEVUELTO";
		    }else if ("PENDIENTE".equalsIgnoreCase(estadoPagoActual)) {
		    	nuevoEstadoPago = "CANCELADO";
		    }else if("CANCELADO".equalsIgnoreCase(estadoPagoActual) || "DEVUELTO".equalsIgnoreCase(estadoPagoActual)) {
		    	throw new IllegalStateException("El pago asociado ya fue cancelado o devuelto");
		    }else {
		    	 throw new IllegalStateException("Estado de pago no válido para cancelar la reserva");
		    }
		    
		    
		    String sql2 = "UPDATE Pago SET estado = ? WHERE id_pago = ?";
		    db.executeUpdate(sql2, nuevoEstadoPago, idPago);
	   }
	   
	   
	   private void actualizarEstadoReserva(int idReserva) {
		    String sql = "UPDATE Reserva_Instalacion "
	                + "SET estado = 'CANCELADA' "
	                + "WHERE id_reservains = ?";

	        db.executeUpdate(sql, idReserva);
	   }
	   
	   private static LocalDateTime toLdt(Object o) {
			if (o == null)
				return null;
			String s = o.toString().replace('T', ' ').trim();
			if (s.length() == 16)
				s = s + ":00";
			return LocalDateTime.parse(s.replace(' ', 'T'));
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
