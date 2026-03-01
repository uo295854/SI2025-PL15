package giis.sisinfo.model;

import java.util.List;

import giis.sisinfo.dto.ActividadesOfertadasDTO;
import giis.sisinfo.util.Database;

public class ActividadesOfertadasModel {
	
	private Database db = new Database();

	public List<ActividadesOfertadasDTO> getListaActividadesOfertadas(String periodo, int año){
		//TODO terminar la consulta y la ejecución de la consulta
		String SQL = "SELECT \r\n"
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
		
		String fechaIni, fechaFin;
		fechaIni= año+"-";
		fechaFin= año+"-";
		
		
		if(periodo.equals("Enero")) {
			//Si el periodo es Enero, se coge el rango de fechas 01-01 a 31-05
			fechaIni+="01-01";
			fechaFin+="05-31";
			
		} else if (periodo.equals("Junio")) {
			//Si el periodo es Junio, se coge el rango de fechas 01-06 a 08-31
			
			
		} else {
			//Si el periodo es Septiembre, se coge el rango de fechas de 01-09 a 31-12
			
			
		}
		
		return db.executeQueryPojo(ActividadesOfertadasDTO.class, SQL, periodo.toUpperCase(), String.valueOf(año));
	}
	
}
