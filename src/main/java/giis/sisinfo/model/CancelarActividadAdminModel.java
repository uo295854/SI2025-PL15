package giis.sisinfo.model;

import java.util.List;

import giis.sisinfo.dto.ActividadCanceladaDTO;
import giis.sisinfo.dto.InstalacionDTO;
import giis.sisinfo.dto.SocioDTO;
import giis.sisinfo.util.Database;

public class CancelarActividadAdminModel {
	
	private Database db = new Database();
	
	public List<InstalacionDTO> getInstalaciones(){
		String SQL = "SELECT nombre_instalacion AS nombreInstalacion, tipo_deporte AS tipoDeporte, tipo_instalacion AS tipoInstalacion, aforo_max AS aforoMaximo\r\n"
				+ "FROM Instalacion";
		
		return db.executeQueryPojo(InstalacionDTO.class, SQL);	
	}
	
	public List<ActividadCanceladaDTO> getActividades(String instalacion){
		//TODO arreglar consulta para que UNICAMENTE coja las actividades que estan activas
		
		String SQL = "SELECT \r\n"
				+ "    a.id_actividad AS idActividad,\r\n"
				+ "    a.nombre AS nombreActividad,\r\n"
				+ "    a.fecha_inicio AS fechaInicio,\r\n"
				+ "    a.fecha_fin AS fechaFin,\r\n"
				+ "    a.tipo AS tipoActividad,\r\n"
				+ "    a.aforo AS aforo,\r\n"
				+ "    COUNT(ia.id_inscripcion) AS participantes,\r\n"
				+ "    a.duracion AS duracion,\r\n"
				+ "    a.descripcion AS descripcion,\r\n"
				+ "    a.estado AS estado\r\n"
				+ "FROM Actividad a\r\n"
				+ "JOIN Instalacion i \r\n"
				+ "    ON a.id_instalacion = i.id_instalacion\r\n"
				+ "LEFT JOIN Inscripcion_Actividad ia \r\n"
				+ "    ON a.id_actividad = ia.id_actividad\r\n"
				+ "    AND ia.estado = 'ACTIVA'\r\n"
				+ "WHERE i.nombre_instalacion = ?\r\n"
				+ "  AND a.estado != 'CANCELADA'\r\n"
				+ "GROUP BY \r\n"
				+ "    a.id_actividad;";
		
		return db.executeQueryPojo(ActividadCanceladaDTO.class, SQL, instalacion);
	}
	
	public List<SocioDTO> getListaSociosANotificar(int idActividad){
		String SQL = "SELECT s.id_socio AS idSocio, s.nombre as nombre, s.apellidos AS apellidos, s.email AS email, s.telefono AS telefono\r\n"
				+ "FROM Socio s\r\n"
				+ "	JOIN Inscripcion_Actividad ia \r\n"
				+ "	ON ia.id_socio = s.id_socio\r\n"
				+ "	WHERE ia.id_actividad = ?";
		
		return db.executeQueryPojo(SocioDTO.class, SQL, idActividad);
	}
	
	public void devolverPagosActividad(int idActividad) {
		//marcar los pagos relacionados con la actividad como devueltos
		String SQL = "UPDATE Pago\r\n"
				+ "SET  estado = 'DEVUELTO'\r\n"
				+ "WHERE id_actividad = ?";
		
		db.executeUpdate(SQL, idActividad);
	}
	
	public void cancelarActividad(int idActividad) {
		//cambiar el estado de la actividad a Cancelada
		String SQL = "UPDATE Actividad\r\n"
				+ "SET  estado = 'CANCELADA'\r\n"
				+ "WHERE id_actividad = ?";
		
		db.executeUpdate(SQL, idActividad);
	}
	
	public void cancelarInscripciones(int idActividad) {
		String SQL = "UPDATE Inscripcion_Actividad\r\n"
				+ "SET  estado = 'CANCELADA' \r\n"
				+ "WHERE id_actividad = ?";
		
		db.executeUpdate(SQL, idActividad);
		
	}
	

}
