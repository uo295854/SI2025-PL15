package giis.sisinfo.model;

import java.util.List;

import giis.sisinfo.dto.ActividadesOfertadasDTO;
import giis.sisinfo.util.Database;

public class ActividadesOfertadasModel {
	
	private Database db = new Database();

	public List<ActividadesOfertadasDTO> getListaActividadesOfertadas(String periodo, int año){
		String SQL = "SELECT "
				+ "    a.nombre                AS nombreActividad,"
				+ "    a.tipo                  AS tipoActividad,"
				+ "    i.nombre_instalacion    AS instalacion,"
				+ "    a.fecha_inicio          AS fechaInicial,"
				+ "    a.fecha_fin             AS fechaFinal,"
				+ "    a.aforo                 AS plazas,"
				+ "    a.cuota_socio           AS precioSocios,"
				+ "    a.cuota_nosocio         AS precioNoSocios,"
				+ "    a.descripcion           AS detalles "
				+ "FROM Actividad a "
				+ "JOIN Instalacion i "
				+ "    ON a.id_instalacion = i.id_instalacion "
				+ "JOIN PeriodoOficial po "
				+ "    ON a.id_periodo_oficial = po.id_periodo_oficial "
				+ "WHERE po.nombre = ? "
				+ "  AND strftime('%Y', po.fecha_ini) = ?;";
		
		
		return db.executeQueryPojo(ActividadesOfertadasDTO.class, SQL, periodo.toUpperCase(), String.valueOf(año));
	}
	
}
