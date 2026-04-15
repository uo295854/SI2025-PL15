package giis.sisinfo.dto;


public class EmailCancelarReservaInstalacionAdminDTO {


	private String nombreSocio;
    private String numSocio;
    private String telefonoSocio;
    private String emailSocio;
    
    private String centro;
    private String direccion;
    private String municipio;
    private String provincia;
    private String emailCentro;
    private String telefonoCentro;
    
    private String motivo;
    private String instalacion;
    private String fecha;
    private String horaEntrada;
    private String dia;
    
    public EmailCancelarReservaInstalacionAdminDTO(String nombreSocio, String numSocio, String emailSocio, String telefonoSocio,
            String instalacion, String fecha, String dia, String horaEntrada, String motivo,
            String centro, String direccion, String municipio, String provincia,
            String emailCentro, String telefonoCentro) {
    	
    	this.centro = centro;
    	this.nombreSocio = nombreSocio;
    	this.numSocio = numSocio;
    	this.telefonoSocio = telefonoSocio;
    	this.emailSocio = emailSocio;
    	this.direccion = direccion;
    	this.municipio = municipio;
    	this.provincia = provincia;
    	this.emailCentro = emailCentro;
    	this.telefonoCentro = telefonoCentro;
    	this.motivo = motivo;
    	this.instalacion = instalacion;
    	this.fecha = fecha;
    	this.horaEntrada = horaEntrada;
    	this.dia = dia;
    	
    	
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

	public String getTelefonoSocio() {
		return telefonoSocio;
	}

	public void setTelefonoSocio(String telefonoSocio) {
		this.telefonoSocio = telefonoSocio;
	}

	public String getEmailSocio() {
		return emailSocio;
	}

	public void setEmailSocio(String emailSocio) {
		this.emailSocio = emailSocio;
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

	public String getEmailCentro() {
		return emailCentro;
	}

	public void setEmailCentro(String emailCentro) {
		this.emailCentro = emailCentro;
	}

	public String getTelefonoCentro() {
		return telefonoCentro;
	}

	public void setTelefonoCentro(String telefonoCentro) {
		this.telefonoCentro = telefonoCentro;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getInstalacion() {
		return instalacion;
	}

	public void setInstalacion(String instalacion) {
		this.instalacion = instalacion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}
}
