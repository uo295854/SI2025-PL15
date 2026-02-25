package giis.sisinfo.dto;

import java.util.Map;

public class ActividadDTO {

	private String nombreActividad;
	private String instalacion;
	private String tipoActividad;
	private int aforo;
	private Map<String, String[]> horariosPorDia;
	private int duracionMin;
	private String periodoInscripcion;
	private int cuotaSocios;
	private int cuotaNoSocios;
	private String descripcion;

	public ActividadDTO(String nombreActividad, String instalacion, String tipoActividad,
			int aforo, Map<String, String[]> horariosPorDia,
			int duracionMin, String periodoInscripcion,
			int cuotaSocios, int cuotaNoSocios, String descripcion) {

		this.nombreActividad = nombreActividad;
		this.instalacion = instalacion;
		this.tipoActividad = tipoActividad;
		this.aforo = aforo;
		this.horariosPorDia = horariosPorDia;
		this.duracionMin = duracionMin;
		this.periodoInscripcion = periodoInscripcion;
		this.cuotaSocios = cuotaSocios;
		this.cuotaNoSocios = cuotaNoSocios;
		this.descripcion = descripcion;
	}

	public String getNombreActividad() { return nombreActividad; }
	public String getInstalacion() { return instalacion; }
	public String getTipoActividad() { return tipoActividad; }
	public int getAforo() { return aforo; }
	public Map<String, String[]> getHorariosPorDia() { return horariosPorDia; }
	public int getDuracionMin() { return duracionMin; }
	public String getPeriodoInscripcion() { return periodoInscripcion; }
	public int getCuotaSocios() { return cuotaSocios; }
	public int getCuotaNoSocios() { return cuotaNoSocios; }
	public String getDescripcion() { return descripcion; }
}