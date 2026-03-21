package giis.sisinfo.model;

import java.util.List;

import giis.sisinfo.dto.CargosMensualesDTO;
import giis.sisinfo.util.Database;

public class ListadoCargosMensualesModel {
	
	private Database db = new Database();
	
	public List<CargosMensualesDTO> getListadoCargosMensuales(int año, String mes){
		
		String SQL = "SELECT \r\n"
				+ "    s.id_socio AS numSocio,\r\n"
				+ "    s.nombre AS nombreSocio,\r\n"
				+ "    s.dni AS DNI,\r\n"
				+ "\r\n"
				+ "    COALESCE(SUM(CASE \r\n"
				+ "        WHEN p.concepto = 'RESERVA' THEN p.importe \r\n"
				+ "    END), 0) AS cargosReservas,\r\n"
				+ "\r\n"
				+ "    COALESCE(SUM(CASE \r\n"
				+ "        WHEN p.concepto = 'INSCRIPCION' THEN p.importe \r\n"
				+ "    END), 0) AS cargosActividades\r\n"
				+ "\r\n"
				+ "FROM Socio s\r\n"
				+ "LEFT JOIN Pago p \r\n"
				+ "    ON p.id_socio = s.id_socio\r\n"
				+ "    AND strftime('%m', p.fecha) = ?\r\n"
				+ "    AND strftime('%Y', p.fecha) = ?\r\n"
				+ "\r\n"
				+ "GROUP BY s.id_socio, s.nombre, s.dni\r\n"
				+ "ORDER BY s.nombre;";
		
		return db.executeQueryPojo(CargosMensualesDTO.class,SQL, String.valueOf(mes),String.valueOf(año));
	}

}
