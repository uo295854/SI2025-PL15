package giis.sisinfo.model;

import java.util.List;

import giis.sisinfo.dto.CargosActividadesSocioDTO;
import giis.sisinfo.dto.CargosReservasSocioDTO;
import giis.sisinfo.util.Database;
import giis.sisinfo.view.ListadoCargosMensualesSocioView;
import giis.sisinfo.view.ListadoCargosMensualesView;

public class ListadoCargosMensualesSocioModel {
	
	private Database db = new Database();
	
	public List<CargosActividadesSocioDTO> getCargosActividadSocio(int idSocio, String año, String mes){
		
		String sql = "SELECT \r\n"
				+ "    a.id_actividad AS idActividad,\r\n"
				+ "    a.nombre AS NombreActividad,\r\n"
				+ "    p.concepto AS ConceptoPago,\r\n"
				+ "    p.fecha AS Fecha,\r\n"
				+ "    p.estado AS Estado,\r\n"
				+ "    p.importe AS importe\r\n"
				+ "FROM Pago p\r\n"
				+ "JOIN Actividad a ON p.id_actividad = a.id_actividad\r\n"
				+ "WHERE p.id_socio = ?\r\n"
				+ "  AND p.id_actividad IS NOT NULL\r\n"
				+ "  AND strftime('%m', p.fecha) = printf('%02d', ?)\r\n"
				+ "  AND strftime('%Y', p.fecha) = ?\r\n"
				+ "ORDER BY p.fecha;";
		
		return db.executeQueryPojo(CargosActividadesSocioDTO.class, sql, idSocio, mes, año);
	}
	
	public List<CargosReservasSocioDTO> getCargosReservaSocio(int idSocio, String año, String mes){
		
		String sql = "SELECT \r\n"
				+ "    r.id_reservains AS idReserva,\r\n"
				+ "    i.nombre_instalacion AS NombreInstalacion,\r\n"
				+ "    p.concepto AS ConceptoPago,\r\n"
				+ "    p.fecha AS Fecha,\r\n"
				+ "    p.estado AS Estado,\r\n"
				+ "    p.importe AS importe\r\n"
				+ "FROM Pago p\r\n"
				+ "LEFT JOIN Reserva_Instalacion r ON p.id_reservains = r.id_reservains\r\n"
				+ "JOIN Instalacion i ON  r.id_instalacion = i.id_instalacion\r\n"
				+ "WHERE p.id_socio = ?\r\n"
				+ "  AND p.id_reservains IS NOT NULL\r\n"
				+ "  AND strftime('%m', p.fecha) = ?\r\n"
				+ "  AND strftime('%Y', p.fecha) = ?\r\n"
				+ "ORDER BY p.fecha;";
		
		return db.executeQueryPojo(CargosReservasSocioDTO.class, sql, idSocio, mes, año);
	}
	

	

}
