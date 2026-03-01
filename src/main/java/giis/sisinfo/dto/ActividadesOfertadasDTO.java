package giis.sisinfo.dto;

import java.time.*;

public class ActividadesOfertadasDTO {
	
	String nombreActividad;
	String tipoActividad;
	String instalacion;
	String fechaInicial;
	String fechaFinal;
	int plazas;
	double precioSocios;
	double precioNoSocios;
	String detalles;
	
	public ActividadesOfertadasDTO() {}
	public ActividadesOfertadasDTO(String n, String t, String i, String fI, String fF, int p, double pS, double pNS, String d) {
		nombreActividad=n;
		tipoActividad=t;
		instalacion=i;
		fechaInicial=fI;
		fechaFinal=fF;
		plazas = p;
		precioSocios = pS;
		precioNoSocios = pNS;
		detalles = d;
	}
	
	public String toString() {
		String result = String.format(
				"nombreActividad: %s\n"
				+ "tipoActividad: %s\n"
				+ "instalacion: %s\n"
				+ "fechaInicial: %s\n"
				+ "fechaFinal: %s\n"
				+ "plazas: %s\n"
				+ "precioSocios: %s\n"
				+ "precioNoSocios: %s\n"
				+ "detalles: %s\n",
				nombreActividad, tipoActividad, instalacion, fechaInicial, fechaFinal, plazas, precioSocios, precioNoSocios, detalles);
		return result;
	}
	
	
	
	
	public String getNombreActividad() {
		return nombreActividad;
	}
	public void setNombreActividad(String nombreActividad) {
		this.nombreActividad = nombreActividad;
	}
	public String getTipoActividad() {
		return tipoActividad;
	}
	public void setTipoActividad(String tipoActividad) {
		this.tipoActividad = tipoActividad;
	}
	public String getInstalacion() {
		return instalacion;
	}
	public void setInstalacion(String instalacion) {
		this.instalacion = instalacion;
	}
	public String getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public String getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public int getPlazas() {
		return plazas;
	}
	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}
	public double getPrecioSocios() {
		return precioSocios;
	}
	public void setPrecioSocios(double precioSocios) {
		this.precioSocios = precioSocios;
	}
	public double getPrecioNoSocios() {
		return precioNoSocios;
	}
	public void setPrecioNoSocios(double precioNoSocios) {
		this.precioNoSocios = precioNoSocios;
	}
	public String getDetalles() {
		return detalles;
	}
	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
	
	
	
	
}
