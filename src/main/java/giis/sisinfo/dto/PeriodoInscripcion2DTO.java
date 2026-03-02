package giis.sisinfo.dto;

public class PeriodoInscripcion2DTO {

	private String nombre;
	private String descripcion;
	private String fechaInicioSocioIso; // yyyy-MM-dd
	private String fechaFinSocioIso;    // yyyy-MM-dd
	private String fechaFinNoSocioIso;  // yyyy-MM-dd

	public PeriodoInscripcion2DTO() { }

	public PeriodoInscripcion2DTO(String nombre, String descripcion,
			String fechaInicioSocioIso, String fechaFinSocioIso, String fechaFinNoSocioIso) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaInicioSocioIso = fechaInicioSocioIso;
		this.fechaFinSocioIso = fechaFinSocioIso;
		this.fechaFinNoSocioIso = fechaFinNoSocioIso;
	}

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	public String getFechaInicioSocioIso() { return fechaInicioSocioIso; }
	public void setFechaInicioSocioIso(String fechaInicioSocioIso) { this.fechaInicioSocioIso = fechaInicioSocioIso; }

	public String getFechaFinSocioIso() { return fechaFinSocioIso; }
	public void setFechaFinSocioIso(String fechaFinSocioIso) { this.fechaFinSocioIso = fechaFinSocioIso; }

	public String getFechaFinNoSocioIso() { return fechaFinNoSocioIso; }
	public void setFechaFinNoSocioIso(String fechaFinNoSocioIso) { this.fechaFinNoSocioIso = fechaFinNoSocioIso; }
}