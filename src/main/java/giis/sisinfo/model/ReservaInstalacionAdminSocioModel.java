package giis.sisinfo.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import giis.sisinfo.dto.DiaReservaSocioDTO;
import giis.sisinfo.dto.HoraReservaSocioDTO;
import giis.sisinfo.dto.SocioDTO;
import giis.sisinfo.util.Database;

public class ReservaInstalacionAdminSocioModel {
	
	private Database db = new Database();
	
	private static final LocalTime hora_apertura = LocalTime.of(9, 0);
	private static final LocalTime hora_cierre = LocalTime.of(22,0);
	private static final int duracion = 60;
	
	
	public List<SocioDTO> buscadorSocios(String apellidos, String nombre, String numSocio){
		
		String sql = 
		        "SELECT id_socio, num_socio, nombre, email, telefono " +
		                "FROM Socio " +
		                "WHERE estado='ACTIVO' AND al_corriente_pago=1 " +
		                "  AND (? IS NULL OR CAST(num_socio AS TEXT) LIKE ?) " +
		                "  AND (? IS NULL OR LOWER(nombre) LIKE ?) " +
		                "  AND (? IS NULL OR LOWER(nombre) LIKE ?) " + 
		                "ORDER BY nombre";
		
		String nsocio = comprobador(numSocio);
		String nom = comprobador(nombre);
		String apell = comprobador(apellidos);
		
		
		List<Object[]> filas = db.executeQueryArray(sql,
				nsocio, nsocio,
			    nom, nom,
			    apell, apell
			);

		List<SocioDTO> resultado = new ArrayList<>();
		
		for(Object[] f: filas) {
			//posicion 0 para id_socio
			//posicion 1 para num_socio
			//posicion 2 para nombre
			
			int idSocio = ((Number) f[0]).intValue();
			String nu = String.valueOf(f[1]);
			String nombreSocio = String.valueOf(f[2]);
			String email = (f[3] == null) ? "" : f[3].toString();
			String tel   = (f[4] == null) ? "" : f[4].toString();

			
			resultado.add(new SocioDTO(idSocio, "", nombreSocio, nu,email, tel));
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
		
		if(filas.isEmpty()) 
			return false;
		
		
		
		String estado = String.valueOf(filas.get(0)[0]);
		
		boolean alcorriente = ((Number) filas.get(0)[1]).intValue() == 1;

		
		 return "ACTIVO".equalsIgnoreCase(estado) && alcorriente;
	}
	
	
	
	public List<String> getDeportes(){
		String sql = "SELECT DISTINCT tipo_deporte FROM Instalacion WHERE tipo_deporte IS NOT NULL ORDER BY tipo_deporte";
		List<Object[]> filas = db.executeQueryArray(sql);
		
		List<String> resultado = new ArrayList<>();
		for(Object[] f: filas)
			resultado.add(String.valueOf(f[0]));
		
		
		return resultado;
	}
	
	
	public List<String> getInstalacionesPorDeporte(String tipoDeporte){
		String sql=
				"SELECT nombre_instalacion " +
			            "FROM Instalacion " +                 
			            "WHERE tipo_deporte=? " +
			            "ORDER BY nombre_instalacion";
		
		List<Object[]> filas = db.executeQueryArray(sql, tipoDeporte);
		List<String> resultado = new ArrayList<>();
		
		for(Object[] f: filas)
			resultado.add(String.valueOf(f[0]));
		
		
		return resultado;
	}


    public Integer getIdInstalacionPorNombre(String nombreInstalacion) {
        String sql = "SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion=?"; 
        List<Object[]> filas = db.executeQueryArray(sql, nombreInstalacion);
        if (filas.isEmpty()) return null;
        
        return ((Number) filas.get(0)[0]).intValue();
    }
    
    
    public List<DiaReservaSocioDTO> getDiasDisponibilidad(int idInstalacion) {
        LocalDate hoy = LocalDate.now();
        List<DiaReservaSocioDTO> resultado = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            LocalDate fecha = hoy.plusDays(i);

            String estado;
            if (i > 15) estado = "NO_RESERVABLE";
            else estado = diaCompleto(idInstalacion, fecha) ? "COMPLETO" : "DISPONIBLE";

            resultado.add(new DiaReservaSocioDTO(
                fecha.getDayOfWeek().toString(),
                fecha.toString(),
                estado
            ));
        }
        return resultado;
    }
    
    private boolean diaCompleto(int idInstalacion, LocalDate fecha) {
        for (LocalTime t = hora_apertura; t.isBefore(hora_cierre); t = t.plusMinutes(duracion)) {
            LocalDateTime ini = LocalDateTime.of(fecha, t);
            LocalDateTime fin = ini.plusMinutes(duracion);
            if (estaLibre(idInstalacion, ini, fin)) return false;
        }
        return true;
    }
    
    public List<HoraReservaSocioDTO> getHorasDia(int idInstalacion, LocalDate fecha) {

        String sql =
         "SELECT datetime_ini, datetime_fin " +
        	"FROM Reserva_Instalacion " +
        	"WHERE id_instalacion=? " +
        	"AND datetime_ini >= ? " +
        	"AND datetime_ini < ? ";

        LocalDateTime iniDia = LocalDateTime.of(fecha, LocalTime.MIN);
        LocalDateTime finDia = iniDia.plusDays(1);

        List<Object[]> filasreservas = db.executeQueryArray(sql,
            idInstalacion,
            Timestamp.valueOf(iniDia),
            Timestamp.valueOf(finDia)
        );

        List<LocalDateTime[]> reservas = new ArrayList<>();
        for (Object[] r : filasreservas) {
            LocalDateTime ini = toLdt(r[0]);
            LocalDateTime fin = toLdt(r[1]);
            reservas.add(new LocalDateTime[] { ini, fin });
        }
        
        
        String sqlBloqueos =
        	    "SELECT b.datetime_ini, b.datetime_fin " +
        	    	    "FROM Bloqueo_por_Actividad b " +
        	    	    "JOIN Actividad a ON a.id_actividad = b.id_actividad " +
        	    	    "WHERE a.id_instalacion=? " +
        	    	    "AND b.datetime_ini >= ? " +
        	    	    "AND b.datetime_ini < ?";

        List<Object[]> rowsBloqueos =
            db.executeQueryArray(sqlBloqueos, idInstalacion, fecha.toString());

        List<LocalDateTime[]> bloqueos = new ArrayList<>();

        for (Object[] r : rowsBloqueos) {
            LocalDateTime ini = toLdt(r[0]);
            LocalDateTime fin = toLdt(r[1]);
            bloqueos.add(new LocalDateTime[]{ini, fin});
        }


        List<HoraReservaSocioDTO> resultado = new ArrayList<>();

        for (LocalTime hora = hora_apertura;
             hora.isBefore(hora_cierre);
             hora = hora.plusMinutes(duracion)) {

            LocalDateTime inicioSlot = LocalDateTime.of(fecha, hora);
            LocalDateTime finSlot = inicioSlot.plusMinutes(duracion);

            boolean ocupadaReserva = solapa(reservas, inicioSlot, finSlot);
            boolean ocupadaActividad = solapa(bloqueos, inicioSlot, finSlot);

            String estado;
            String motivo = "";

            if (ocupadaReserva) {
                estado = "OCUPADA";
                motivo = "Reservada";
            } else if (ocupadaActividad) {
                estado = "OCUPADA";
                motivo = "Actividad";
            } else {
                estado = "LIBRE";
            }

            resultado.add(new HoraReservaSocioDTO(
                hora.toString(),                     // hora entrada
                finSlot.toLocalTime().toString(),   // hora salida
                estado,
                motivo
            ));
        }

        return resultado;
    }

    
    public void crearReserva(int idInstalacion, int idSocio, LocalDate fecha, LocalTime horaInicio) {

        if (!puedeSocioReservar(idSocio)) {
            throw new IllegalStateException("Socio no autorizado para reservar.");
        }

        LocalDate hoy = LocalDate.now();
        if (fecha.isAfter(hoy.plusDays(15))) {
            throw new IllegalStateException("No se puede reservar con más de 15 días de antelación.");
        }

        LocalDateTime inicio = LocalDateTime.of(fecha, horaInicio);
        LocalDateTime fin = inicio.plusMinutes(duracion);

        if (!estaLibre(idInstalacion, inicio, fin)) {
            throw new IllegalStateException("La franja horaria ya no está disponible.");
        }

        String sql =
            "INSERT INTO Reserva_Instalacion (id_instalacion, id_socio, datetime_ini, datetime_fin) " +
            "VALUES (?,?,?,?)";

        db.executeUpdate(sql,
            idInstalacion,
            idSocio,
            Timestamp.valueOf(inicio),
            Timestamp.valueOf(fin)
        );
    }


    public boolean estaLibre(int idInstalacion, LocalDateTime inicio, LocalDateTime fin) {

        String sql1 =
            "SELECT COUNT(*) " +
            "FROM Reserva_Instalacion " +
            "WHERE id_instalacion=? " +
            "AND datetime_ini < ? " +
            "AND datetime_fin > ?";

        long c1 = ((Number) db.executeQueryArray(sql1,
            idInstalacion,
            Timestamp.valueOf(fin),
            Timestamp.valueOf(inicio)
        ).get(0)[0]).longValue();

        if (c1 > 0) return false;

        String sql2 =
            "SELECT COUNT(*) " +
            "FROM Bloqueo_por_Actividad rai " +
            "JOIN Actividad a ON a.id_actividad = rai.id_actividad " +
            "WHERE a.id_instalacion=? " +
            "AND rai.datetime_ini < ? " +
            "AND rai.datetime_fin > ?";

        long c2 = ((Number) db.executeQueryArray(sql2,
            idInstalacion,
            Timestamp.valueOf(fin),
            Timestamp.valueOf(inicio)
        ).get(0)[0]).longValue();

        return c2 == 0;
    }


    private static boolean solapa(List<LocalDateTime[]> rangos, LocalDateTime ini, LocalDateTime fin) {
        for (LocalDateTime[] r : rangos) {
            if (r[0].isBefore(fin) && r[1].isAfter(ini)) return true;
        }
        return false;
    }

    private static LocalDateTime toLdt(Object o) {
        if (o == null) return null;
        if (o instanceof Timestamp) return ((Timestamp) o).toLocalDateTime();

        String s = o.toString().replace('T', ' ');
        if (s.length() == 16) s = s + ":00";
        return LocalDateTime.parse(s.replace(' ', 'T'));
    }
}
    

