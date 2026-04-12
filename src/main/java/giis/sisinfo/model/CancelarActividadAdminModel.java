package giis.sisinfo.model;

import java.util.List;

import giis.sisinfo.dto.ActividadCanceladaDTO;
import giis.sisinfo.dto.InstalacionDTO;
import giis.sisinfo.util.Database;

public class CancelarActividadAdminModel {
	
	private Database db = new Database();
	
	public List<InstalacionDTO> getInstalaciones(){
		String SQL = "SELECT nombre_instalacion AS nombreInstalacion, tipo_deporte AS tipoDeporte, tipo_instalacion AS tipoInstalacion, aforo_max AS aforoMaximo\r\n"
				+ "FROM Instalacion";
		
		return db.executeQueryPojo(InstalacionDTO.class, SQL);	
	}
	
	public List<ActividadCanceladaDTO> getActividades(String instalacion){
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
				+ "LEFT JOIN Inscripcion_Actividad ia \r\n"
				+ "    ON a.id_actividad = ia.id_actividad\r\n"
				+ "    AND ia.estado = 'ACTIVA'\r\n"
				+ "JOIN Instalacion i \r\n"
				+ "	ON a.id_instalacion = i.id_instalacion\r\n"
				+ "WHERE i.nombre_instalacion = ?\r\n"
				+ "GROUP BY a.id_actividad;";
		
		return db.executeQueryPojo(ActividadCanceladaDTO.class, SQL, instalacion);
	}

}
