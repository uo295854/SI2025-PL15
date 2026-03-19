package giis.sisinfo.model;

import giis.sisinfo.util.Database;

import java.util.List;

import giis.sisinfo.dto.ConsultaReservasSocioDTO;

public class ConsultarReservasSocioModel {
	
	private Database db = new Database();
	
	public List<ConsultaReservasSocioDTO> getReservasSocio(int idUsuario, String fechaInicial, String fechaFinal){
		String SQL = "SELECT \r\n"
				+ "    i.nombre_instalacion AS instalacionReservada,\r\n"
				+ "    DATE(r.datetime_ini) AS fecha,\r\n"
				+ "    TIME(r.datetime_ini) AS horaInicial,\r\n"
				+ "    TIME(r.datetime_fin) AS horaFinal\r\n"
				+ "FROM Reserva_Instalacion r\r\n"
				+ "JOIN Instalacion i \r\n"
				+ "    ON r.id_instalacion = i.id_instalacion\r\n"
				+ "WHERE r.id_socio = ? \r\n"
				+ "  AND DATE(r.datetime_ini) BETWEEN ? AND ? \r\n"
				+ "ORDER BY r.datetime_ini";
		
		return db.executeQueryPojo(ConsultaReservasSocioDTO.class, SQL, idUsuario, fechaInicial, fechaFinal);
		
	}
	

}
