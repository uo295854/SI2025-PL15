package giis.sisinfo.dto;

public class ReservaAdminDTO {
	String idReserva;
	String idSocio;
	String NombreActividad;
	String FechaHoraInicial;
	String FechaHoraFinal;
	
	public ReservaAdminDTO() {}
	public ReservaAdminDTO(String nidR, String nidS, String nNA, String nFHI, String nFHF) {
		idReserva=nidR;
		idSocio=nidS;
		NombreActividad=nNA;
		FechaHoraInicial=nFHI;
		FechaHoraFinal=nFHF;
	}
	public String getNombreActividad() {
		return NombreActividad;
	}
	public String getFechaHoraInicial() {
		return FechaHoraInicial;
	}
	public String getFechaHoraFinal() {
		return FechaHoraFinal;
	}
	public void setNombreActividad(String nombreActividad) {
		NombreActividad = nombreActividad;
	}
	public void setFechaHoraInicial(String fechaHoraInicial) {
		FechaHoraInicial = fechaHoraInicial;
	}
	public void setFechaHoraFinal(String fechaHoraFinal) {
		FechaHoraFinal = fechaHoraFinal;
	}
	public String getIdReserva() {
		return idReserva;
	}
	public void setIdReserva(String idReserva) {
		this.idReserva = idReserva;
	}
	public String getIdSocio() {
		return idSocio;
	}
	public void setIdSocio(String idSocio) {
		this.idSocio = idSocio;
	}

}
