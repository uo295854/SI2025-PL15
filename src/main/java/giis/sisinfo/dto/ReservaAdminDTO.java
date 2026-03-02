package giis.sisinfo.dto;

public class ReservaAdminDTO {
	String NombreActividad;
	String FechaHoraInicial;
	String FechaHoraFinal;
	
	public ReservaAdminDTO() {}
	public ReservaAdminDTO(String nNA, String nFHI, String nFHF) {
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
	
	
	
}
