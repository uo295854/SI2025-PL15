package giis.sisinfo.dto;

import java.time.*;

public class ActividadesOfertadasDTO {
	
	String nombreActividad;
	String tipoActividad;
	String instalacion;
	LocalDateTime fechaInicial;
	LocalDateTime fechaFinal;
	int plazas;
	int precioSocios;
	int precioNoSocios;
	String detalles;
	
	public ActividadesOfertadasDTO() {}
	public ActividadesOfertadasDTO(String n, String t, String i, LocalDateTime fI, LocalDateTime fF, int p, int pS, int pNS, String d) {
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
	public LocalDateTime getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaInicial(LocalDateTime fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public LocalDateTime getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(LocalDateTime fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public int getPlazas() {
		return plazas;
	}
	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}
	public int getPrecioSocios() {
		return precioSocios;
	}
	public void setPrecioSocios(int precioSocios) {
		this.precioSocios = precioSocios;
	}
	public int getPrecioNoSocios() {
		return precioNoSocios;
	}
	public void setPrecioNoSocios(int precioNoSocios) {
		this.precioNoSocios = precioNoSocios;
	}
	public String getDetalles() {
		return detalles;
	}
	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
	
	
	
	
}
