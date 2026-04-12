package giis.sisinfo.dto;

public class ActividadCanceladaDTO {
	
	int idActividad;
	String nombreActividad;
	String fechaInicio;
	String fechaFin;
	String tipoActividad;
	int aforo;
	int participantes;
	int duracion;
	String descripcion;
	String estado;
	
	public ActividadCanceladaDTO() {}
	
	
	public ActividadCanceladaDTO(int idActividad, String nombreActividad, String fechaInicio, String fechaFin,
			String tipoActividad, int aforo, int participantes, int duracion, String descripcion, String estado) {
		super();
		this.idActividad = idActividad;
		this.nombreActividad = nombreActividad;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.tipoActividad = tipoActividad;
		this.aforo = aforo;
		this.participantes = participantes;
		this.duracion = duracion;
		this.descripcion = descripcion;
		this.estado = estado;
	}


	public int getIdActividad() {
		return idActividad;
	}


	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}


	public String getNombreActividad() {
		return nombreActividad;
	}


	public void setNombreActividad(String nombreActividad) {
		this.nombreActividad = nombreActividad;
	}


	public String getFechaInicio() {
		return fechaInicio;
	}


	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	public String getFechaFin() {
		return fechaFin;
	}


	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}


	public String getTipoActividad() {
		return tipoActividad;
	}


	public void setTipoActividad(String tipoActividad) {
		this.tipoActividad = tipoActividad;
	}


	public int getAforo() {
		return aforo;
	}


	public void setAforo(int aforo) {
		this.aforo = aforo;
	}


	public int getParticipantes() {
		return participantes;
	}


	public void setParticipantes(int participantes) {
		this.participantes = participantes;
	}


	public int getDuracion() {
		return duracion;
	}


	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	
}
