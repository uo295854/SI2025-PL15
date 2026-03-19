package giis.sisinfo.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ResguardoReservaAdminSocioDTO {

	private String nombreSocio;
    private String numSocio;
    private String telefono;
    private String email;

    private String centro;
    private String direccion;
    private String municipio;
    private String provincia;

    private String deporte;
    private String instalacion;
    private LocalDate fecha;
    private List<LocalTime> horas;
    private double cuota;
    private String estadoPago;
    
    public ResguardoReservaAdminSocioDTO(String nombreSocio, String numSocio, String telefono, String email,
            String centro, String direccion, String municipio, String provincia,
            String deporte, String instalacion, LocalDate fecha, List<LocalTime> horas,
            double cuota, String estadoPago) {
    	
        this.nombreSocio = nombreSocio;
        this.numSocio = numSocio;
        this.telefono = telefono;
        this.email = email;
        this.centro = centro;
        this.direccion = direccion;
        this.municipio = municipio;
        this.provincia = provincia;
        this.deporte = deporte;
        this.instalacion = instalacion;
        this.fecha = fecha;
        this.horas = horas;
        this.cuota = cuota;
        this.estadoPago = estadoPago;
    	
    	
    }

	public String getNombreSocio() {
		return nombreSocio;
	}

	public void setNombreSocio(String nombreSocio) {
		this.nombreSocio = nombreSocio;
	}

	public String getNumSocio() {
		return numSocio;
	}

	public void setNumSocio(String numSocio) {
		this.numSocio = numSocio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCentro() {
		return centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getDeporte() {
		return deporte;
	}

	public void setDeporte(String deporte) {
		this.deporte = deporte;
	}

	public String getInstalacion() {
		return instalacion;
	}

	public void setInstalacion(String instalacion) {
		this.instalacion = instalacion;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public List<LocalTime> getHoras() {
		return horas;
	}

	public void setHoras(List<LocalTime> horas) {
		this.horas = horas;
	}

	public double getCuota() {
		return cuota;
	}

	public void setCuota(double cuota) {
		this.cuota = cuota;
	}

	public String getEstadoPago() {
		return estadoPago;
	}

	public void setEstadoPago(String estadoPago) {
		this.estadoPago = estadoPago;
	}
    
    
}
