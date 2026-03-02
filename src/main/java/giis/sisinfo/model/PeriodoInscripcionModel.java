package giis.sisinfo.model;

import java.util.List;

import giis.sisinfo.util.Database;
import giis.sisinfo.dto.PeriodoInscripcion2DTO;
import giis.sisinfo.dto.PeriodoOficialItemDTO;

public class PeriodoInscripcionModel {

	private final Database db;

	public PeriodoInscripcionModel(Database db) {
		this.db = db;
	}

	public List<PeriodoOficialItemDTO> getPeriodosOficiales() {
		String sql =
			"SELECT id_periodo_oficial AS idPeriodoOficial, " +
			"       nombre AS nombre, " +
			"       fecha_ini AS fechaIni, " +
			"       fecha_fin AS fechaFin " +
			"  FROM PeriodoOficial " +
			" ORDER BY id_periodo_oficial";
		return db.executeQueryPojo(PeriodoOficialItemDTO.class, sql);
	}

	public boolean existeNombrePeriodoInscripcion(String nombre) {
		String sql = "SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre = ?";
		return !db.executeQueryArray(sql, nombre).isEmpty();
	}

	public void crearPeriodoInscripcion(PeriodoInscripcion2DTO dto) {
		String sql =
			"INSERT INTO PeriodoInscripcion(nombre, descripcion, fecha_inicio_socio, fecha_fin_socio, fecha_fin_nosocio) " +
			"VALUES(?, ?, ?, ?, ?)";
		db.executeUpdate(sql,
			dto.getNombre(),
			dto.getDescripcion(),
			dto.getFechaInicioSocioIso(),
			dto.getFechaFinSocioIso(),
			dto.getFechaFinNoSocioIso()
		);
	}
}