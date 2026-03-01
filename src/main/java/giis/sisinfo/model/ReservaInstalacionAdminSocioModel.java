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
	
	
	/*public List<SocioDTO> buscadorSocios(String apellidos, String nombre, String numSocio){
		
		String sql = 
		        "SELECT id_socio, num_socio, nombre " +
		                "FROM Socio " +
		                "WHERE estado='ACTIVO' AND al_corriente_pago=1 " +
		                "  AND (? IS NULL OR LOWER(num_socio) LIKE ?) " +
		                "  AND (? IS NULL OR LOWER(nombre) LIKE ?) " +
		                "  AND (? IS NULL OR LOWER(nombre) LIKE ?) " + 
		                "ORDER BY nombre";
		
		if(numSocio == null || numSocio.trim().isEmpty()) {
			return null;}
		else {
			return "%" + numSocio.trim().toLowerCase() + "%";}
		
		
		
		
	
		
		return null;
		
	}*/

}
