package giis.sisinfo.dto;

public class PeriodoInscripcionDTO {
	private final String nombre;
	private final String fechaInicioSocio; // yyyy-MM-dd
	private final String fechaFinSocio;    // yyyy-MM-dd
	private final String fechaFinNoSocio;  // yyyy-MM-dd

	public PeriodoInscripcionDTO(String nombre, String fechaInicioSocio, String fechaFinSocio, String fechaFinNoSocio) {
		this.nombre = nombre;
		this.fechaInicioSocio = fechaInicioSocio;
		this.fechaFinSocio = fechaFinSocio;
		this.fechaFinNoSocio = fechaFinNoSocio;
	}

	public String getNombre() { return nombre; }
	public String getFechaInicioSocio() { return fechaInicioSocio; }
	public String getFechaFinSocio() { return fechaFinSocio; }
	public String getFechaFinNoSocio() { return fechaFinNoSocio; }
}