package giis.sisinfo.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import giis.sisinfo.dto.ActividadDTO;
import giis.sisinfo.dto.InstalacionDTO;
import giis.sisinfo.dto.ReservaAdminDTO;
import giis.sisinfo.util.Database;

public class ReservaInstalacionAdminModel {
	
	private Database db = new Database();

	public List<InstalacionDTO> getInstalaciones() {
		String SQL = "SELECT nombre_instalacion AS nombreInstalacion FROM Instalacion";
		System.out.println("ReservaInstalacionAdminModel | Consulta SQL realizada");
		return db.executeQueryPojo(InstalacionDTO.class, SQL);
	}
	
	public List<ReservaAdminDTO> getReservas(String fechaHoraInicial, String fechaHoraFinal, String instalacion){
		//las fechas deben estar en formato YYYY-MM-DD HH:MM:SS

		String SQL = "WITH parametros AS (\r\n"
				+ "    SELECT\r\n"
				+ "        (SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion=?) AS id_instalacion,\r\n"
				+ "        ? AS nuevo_inicio,\r\n"
				+ "        ? AS nuevo_fin\r\n"
				+ ")\r\n"
				+ "SELECT \r\n"
				+ "    a.nombre AS NombreActividad,\r\n"
				+ "    r.datetime_ini AS FechaHoraInicial,\r\n"
				+ "    r.datetime_fin AS FechaHoraFinal\r\n"
				+ "FROM Reserva_Instalacion r\r\n"
				+ "JOIN Instalacion i ON r.id_instalacion = i.id_instalacion\r\n"
				+ "JOIN parametros p ON r.id_instalacion = p.id_instalacion\r\n"
				+ "JOIN Actividad a ON a.id_instalacion = r.id_instalacion\r\n"
				+ "WHERE \r\n"
				+ "    r.datetime_ini < p.nuevo_fin\r\n"
				+ "    AND r.datetime_fin > p.nuevo_inicio\r\n"
				+ "ORDER BY r.datetime_ini;";
		
		System.out.println("ReservaInstalacionAdminModel | Consulta SQL realizada");
		return db.executeQueryPojo(ReservaAdminDTO.class, SQL, instalacion, fechaHoraInicial, fechaHoraFinal);
	}
	
	public void hacerReserva(String fechaHoraInicial, String fechaHoraFinal, String instalacion, String nombreActividad) {
		String SQL = "INSERT INTO Reserva_Actividad_Instalacion (\r\n"
				+ "    id_actividad,\r\n"
				+ "    inicio_datetime,\r\n"
				+ "    fin_datetime\r\n"
				+ ")\r\n"
				+ "VALUES (\r\n"
				+ "    (\r\n"
				+ "        SELECT a.id_actividad\r\n"
				+ "        FROM Actividad a\r\n"
				+ "        JOIN Instalacion i ON a.id_instalacion = i.id_instalacion\r\n"
				+ "        WHERE a.nombre = ?\r\n"
				+ "          AND i.nombre_instalacion = ?\r\n"
				+ "    ),\r\n"
				+ "    ?,\r\n"
				+ "    ?\r\n"
				+ ");";
		db.executeUpdate(SQL, nombreActividad,instalacion,fechaHoraInicial,fechaHoraFinal);		
	}
	
	public List<ActividadDTO> getActividadesEnInstalacion(String instalacion) {
		String SQL = "SELECT a.nombre AS nombreActividad, a.descripcion AS descripcion\r\n"
				+ "FROM Actividad a\r\n"
				+ "JOIN Instalacion i \r\n"
				+ "    ON a.id_instalacion = i.id_instalacion\r\n"
				+ "WHERE i.nombre_instalacion = ?\r\n"
				+ "ORDER BY a.nombre;";
		
		return db.executeQueryPojo(ActividadDTO.class, SQL, instalacion);
	}


}
