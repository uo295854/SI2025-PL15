package giis.sisinfo.dto;

public class CargosMensualesDTO {
	
	int numSocio;
	String nombreSocio;
	String DNI;
	double cargosReservas;
	double cargosActividades;
	
	public CargosMensualesDTO() {}
	public CargosMensualesDTO(int nNumSocio, String nNombreSocio, String nDNI, double nCargosReservas, double nCargosActividades) {
		numSocio = nNumSocio;
		nombreSocio = nNombreSocio;
		DNI = nDNI;
		cargosReservas = nCargosReservas;
		cargosActividades = nCargosActividades;
	}
	
	public int getNumSocio() {return numSocio;}
	public void setNumSocio(int numSocio) {this.numSocio = numSocio;}
	
	public String getNombreSocio() {return nombreSocio;}
	public void setNombreSocio(String nombreSocio) {this.nombreSocio = nombreSocio;}
	
	public String getDNI() {return DNI;}
	public void setDNI(String dNI) {DNI = dNI;}
	
	public double getCargosReservas() {return cargosReservas;}
	public void setCargosReservas(double cargosReservas) {this.cargosReservas = cargosReservas;}
	
	public double getCargosActividades() {return cargosActividades;}
	public void setCargosActividades(double cargosActividades) {this.cargosActividades = cargosActividades;}
}
