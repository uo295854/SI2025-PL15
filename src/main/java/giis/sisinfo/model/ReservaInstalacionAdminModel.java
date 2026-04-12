package giis.sisinfo.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import giis.sisinfo.dto.ActividadDTO;
import giis.sisinfo.dto.InstalacionDTO;
import giis.sisinfo.dto.ReservaAdminDTO;
import giis.sisinfo.dto.ReservaClienteDTO;
import giis.sisinfo.util.Database;

public class ReservaInstalacionAdminModel {
	
	private Database db = new Database();

	public List<InstalacionDTO> getInstalaciones() {
		String SQL = "SELECT nombre_instalacion AS nombreInstalacion FROM Instalacion";
		System.out.println("ReservaInstalacionAdminModel | Consulta SQL realizada");
		return db.executeQueryPojo(InstalacionDTO.class, SQL);
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
	
	public List<ReservaAdminDTO> getReservas_BloqueoPorActividad(String fechaHoraInicial, String fechaHoraFinal, String instalacion){
	    // Las fechas deben estar en formato YYYY-MM-DD HH:MM:SS
	    String SQL = "SELECT b.id_bloqueo AS idReserva, a.nombre AS NombreActividad, b.datetime_ini AS FechaHoraInicial, b.datetime_fin AS FechaHoraFinal "
	               + "FROM Bloqueo_por_Actividad b "
	               + "JOIN Actividad a ON b.id_actividad = a.id_actividad "
	               + "JOIN Instalacion i ON a.id_instalacion = i.id_instalacion "
	               + "WHERE i.nombre_instalacion = ? "
	               + "  AND b.datetime_ini < ? "
	               + "  AND b.datetime_fin > ?";

	    System.out.println("ReservaInstalacionAdminModel | getReservas_BloqueoPorActividad - Consulta SQL realizada");
	    return db.executeQueryPojo(ReservaAdminDTO.class, SQL, instalacion, fechaHoraFinal, fechaHoraInicial);
	}
	
	public void eliminarReservasConflictivas_BloqueoPorActividad(String fechaInicio, String fechaFinal) {
		String SQL = "DELETE FROM Bloqueo_por_Actividad "
	               + "WHERE datetime_ini < ? AND datetime_fin > ?";
		
		db.executeUpdate(SQL, fechaFinal, fechaInicio);
	}
	
	public List<ReservaClienteDTO> getReservas_ReservaInstalacion(String fechaHoraInicial, String fechaHoraFinal, String instalacion) {
		String SQL = "SELECT r.id_reservains AS idReserva, a.nombre AS NombreActividad, r.datetime_ini AS fechaInicial, r.datetime_fin AS fechaFinal, r.id_socio AS idSocio \r\n"
				+ "	               FROM Reserva_Instalacion r \r\n"
				+ "				   JOIN Actividad a ON i.id_instalacion = a.id_instalacion \r\n"
				+ "	               JOIN Instalacion i ON r.id_instalacion = i.id_instalacion \r\n"
				+ "	               WHERE i.nombre_instalacion = ?\r\n"
				+ "	                 AND r.datetime_ini < ? \r\n"
				+ "	                 AND r.datetime_fin > ?;";
		
		return db.executeQueryPojo(ReservaClienteDTO.class, SQL, instalacion, fechaHoraFinal, fechaHoraInicial);
	}
	
	public void eliminarReservasConflictivas_ReservaInstalacion(String fechaInicio, String fechaFinal) {
		String SQL = "DELETE FROM Reserva_Instalacion "
	               + "WHERE datetime_ini < ? AND datetime_fin > ?";
		
		db.executeUpdate(SQL, fechaFinal, fechaInicio);
		
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

}
