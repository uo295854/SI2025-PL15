package giis.sisinfo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import giis.sisinfo.dto.SocioDTO;
import giis.sisinfo.util.Database;

public class CancelarReservaInstalacionAdminModel {

	private Database db = new Database();
	
	
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
	
   public List<Object[]> getReservasActivasSocio(int idSocio) {
	        String sql = "SELECT r.id_reservains, i.nombre_instalacion, "
	                + "       date(r.datetime_ini) as fecha, "
	                + "       time(r.datetime_ini) as hora_entrada, "
	                + "       time(r.datetime_fin) as hora_salida, "
	                + "       p.estado "
	                + "FROM Reserva_Instalacion r "
	                + "JOIN Instalacion i ON i.id_instalacion = r.id_instalacion "
	                + "LEFT JOIN Pago p ON p.id_reservains = r.id_reservains "
	                + "WHERE r.id_socio = ? "
	                + "  AND r.estado = 'ACTIVA' "
	                + "  AND date(r.datetime_ini) = ? "
	                + "ORDER BY r.datetime_ini";

	        List<Object[]> filas = db.executeQueryArray(sql, idSocio, LocalDate.now().toString());
	        List<Object[]> resultado = new ArrayList<>();

	        for (Object[] f : filas) {
	            int idReserva = ((Number) f[0]).intValue();
	            String instalacion = String.valueOf(f[1]);
	            String fecha = String.valueOf(f[2]);
	            String dia = diaSemana(LocalDate.parse(fecha));
	            String horaEntrada = String.valueOf(f[3]);
	            String horaSalida = String.valueOf(f[4]);
	            String estadoPago = (f[5] == null) ? "" : String.valueOf(f[5]);

	            resultado.add(new Object[] {idReserva, instalacion, fecha, dia, horaEntrada, horaSalida, estadoPago});
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
	
	public Object[] getDatosAvisoCancelacion(int idReserva) {
	    String sql = "SELECT s.nombre, s.apellidos, s.num_socio, s.email, s.telefono, "
	            + "       i.nombre_instalacion, "
	            + "       date(r.datetime_ini) as fecha, "
	            + "       time(r.datetime_ini) as hora_entrada "
	            + "FROM Reserva_Instalacion r "
	            + "JOIN Socio s ON s.id_socio = r.id_socio "
	            + "JOIN Instalacion i ON i.id_instalacion = r.id_instalacion "
	            + "WHERE r.id_reservains = ?";

	    List<Object[]> filas = db.executeQueryArray(sql, idReserva);

	    if (filas.isEmpty()) {
	        throw new IllegalStateException("No se encontraron datos para generar el aviso de cancelación");
	    }

	    Object[] f = filas.get(0);

	    String nombre = String.valueOf(f[0]);
	    String apellidos = String.valueOf(f[1]);
	    String numSocio = String.valueOf(f[2]);
	    String email = (f[3] == null) ? "" : String.valueOf(f[3]);
	    String telefono = (f[4] == null) ? "" : String.valueOf(f[4]);
	    String instalacion = String.valueOf(f[5]);
	    String fecha = String.valueOf(f[6]);
	    String dia = diaSemana(LocalDate.parse(fecha));
	    String horaEntrada = String.valueOf(f[7]);

	    return new Object[] {nombre, apellidos, numSocio, email, telefono, instalacion, fecha, dia, horaEntrada};
	}
   
}
