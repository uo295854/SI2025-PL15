package giis.sisinfo.dto;

public class ReservaClienteDTO {
	
	int idReserva;
	String nombreActividad;
	String fechaInicial; 	//Formato YYYY-MM-DD HH:MM
	String fechaFinal;		//Formato YYYY-MM-DD HH:MM
	int idSocio;
	
	public ReservaClienteDTO() {}
	
	public ReservaClienteDTO(int idReserva, String nombreActividad, String fechaInicial, String fechaFinal, int idSocio) {
		this.idReserva = idReserva;
		this.nombreActividad = nombreActividad;
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
		this.idSocio = idSocio;
	}
	
	public int getIdReserva() {return idReserva;}
	public void setIdReserva(int idReserva) {this.idReserva = idReserva;}
	public String getNombreActividad() {return nombreActividad;}
	public void setNombreActividad(String nombreActividad) {this.nombreActividad = nombreActividad;}
	public String getFechaInicial() {return fechaInicial;}
	public void setFechaInicial(String fechaInicial) {this.fechaInicial = fechaInicial;}
	public String getFechaFinal() {return fechaFinal;}
	public void setFechaFinal(String fechaFinal) {this.fechaFinal = fechaFinal;}
	public int getIdSocio() {return idSocio;}
	public void setIdSocio(int idSocio) {this.idSocio = idSocio;}
	

}
