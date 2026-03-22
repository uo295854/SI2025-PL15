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
	    // Las fechas deben estar en formato YYYY-MM-DD HH:MM:SS
	    String SQL = "SELECT b.id_bloqueo AS idReserva, a.nombre AS NombreActividad, b.datetime_ini AS FechaHoraInicial, b.datetime_fin AS FechaHoraFinal "
	               + "FROM Bloqueo_por_Actividad b "
	               + "JOIN Actividad a ON b.id_actividad = a.id_actividad "
	               + "JOIN Instalacion i ON a.id_instalacion = i.id_instalacion "
	               + "WHERE i.nombre_instalacion = ? "
	               + "  AND b.datetime_ini < ? "
	               + "  AND b.datetime_fin > ?";

	    System.out.println("ReservaInstalacionAdminModel | Consulta SQL realizada");
	    return db.executeQueryPojo(ReservaAdminDTO.class, SQL, instalacion, fechaHoraFinal, fechaHoraInicial);
	}
	
	public void hacerReserva(String fechaHoraInicial, String fechaHoraFinal, String nombreActividad) {
		String SQL = "INSERT INTO Bloqueo_por_Actividad (id_actividad, datetime_ini, datetime_fin)\r\n"
				+ "VALUES (\r\n"
				+ "  (SELECT id_actividad FROM Actividad WHERE nombre = ?),\r\n"
				+ "  ?, \r\n"
				+ "  ?\r\n"
				+ ");";
		db.executeUpdate(SQL, nombreActividad,fechaHoraInicial,fechaHoraFinal);		
	}
	
	public List<ActividadDTO> getActividadesEnInstalacion(String instalacion) {
		String SQL = "SELECT \r\n"
				+ "    a.nombre AS nombreActividad,\r\n"
				+ "    a.descripcion AS descripcion,\r\n"
				+ "    a.fecha_inicio AS fechaInicio,\r\n"
				+ "    a.fecha_fin AS fechaFin\r\n"
				+ "FROM Actividad a\r\n"
				+ "JOIN Instalacion i ON a.id_instalacion = i.id_instalacion\r\n"
				+ "WHERE i.nombre_instalacion = ?";
		
		return db.executeQueryPojo(ActividadDTO.class, SQL, instalacion);
	}
	
	public void eliminarReservasConflictivas(String fechaInicio, String fechaFinal) {
		String SQL = "DELETE FROM Bloqueo_por_Actividad "
	               + "WHERE datetime_ini < ? AND datetime_fin > ?";
		
		db.executeUpdate(SQL, fechaFinal, fechaInicio);
	}


}
