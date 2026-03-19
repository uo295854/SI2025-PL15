package giis.sisinfo.dto;

public class ConsultaReservasSocioDTO {
	String instalacionReservada;
	String fecha;
	String horaInicial;
	String horaFinal;
	
	public ConsultaReservasSocioDTO() {}
	public ConsultaReservasSocioDTO(String nInst, String nFech, String nHI, String nHF){
		instalacionReservada=nInst;
		fecha=nFech;
		horaInicial=nHI;
		horaFinal=nHF;
	}
	
	public String getInstalacionReservada() {
		return instalacionReservada;
	}
	public void setInstalacionReservada(String instalacionReservada) {
		this.instalacionReservada = instalacionReservada;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHoraInicial() {
		return horaInicial;
	}
	public void setHoraInicial(String horaInicial) {
		this.horaInicial = horaInicial;
	}
	public String getHoraFinal() {
		return horaFinal;
	}
	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}
	
	
	
}
