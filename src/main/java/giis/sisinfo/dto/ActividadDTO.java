package giis.sisinfo.dto;

import java.util.Map;

public class ActividadDTO {

	private final String nombre;
	private final String instalacionNombre;     // Instalacion.nombre_instalacion
	private final String tipoUi;                // "Deportiva", "Cultural", "Formativa", "Campeonato"...
	private final int aforo;

	// dia -> ["HH:mm","HH:mm"] (ej: "Lunes" -> ["10:00","11:30"])
	private final Map<String, String[]> horariosPorDia;

	private final int duracionMinutos;

	private final String periodoInscripcionNombre; // PeriodoInscripcion.nombre

	private final String fechaInicio; // yyyy-MM-dd
	private final String fechaFin;    // yyyy-MM-dd

	private final double cuotaSocio;
	private final double cuotaNoSocio;

	private final String descripcion;

	public ActividadDTO(
			String nombre,
			String instalacionNombre,
			String tipoUi,
			int aforo,
			Map<String, String[]> horariosPorDia,
			int duracionMinutos,
			String periodoInscripcionNombre,
			String fechaInicio,
			String fechaFin,
			double cuotaSocio,
			double cuotaNoSocio,
			String descripcion) {

		this.nombre = nombre;
		this.instalacionNombre = instalacionNombre;
		this.tipoUi = tipoUi;
		this.aforo = aforo;
		this.horariosPorDia = horariosPorDia;
		this.duracionMinutos = duracionMinutos;
		this.periodoInscripcionNombre = periodoInscripcionNombre;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.cuotaSocio = cuotaSocio;
		this.cuotaNoSocio = cuotaNoSocio;
		this.descripcion = descripcion;
	}

	public String getNombre() { return nombre; }
	public String getInstalacionNombre() { return instalacionNombre; }
	public String getTipoUi() { return tipoUi; }
	public int getAforo() { return aforo; }
	public Map<String, String[]> getHorariosPorDia() { return horariosPorDia; }
	public int getDuracionMinutos() { return duracionMinutos; }
	public String getPeriodoInscripcionNombre() { return periodoInscripcionNombre; }
	public String getFechaInicio() { return fechaInicio; }
	public String getFechaFin() { return fechaFin; }
	public double getCuotaSocio() { return cuotaSocio; }
	public double getCuotaNoSocio() { return cuotaNoSocio; }
	public String getDescripcion() { return descripcion; }
}