package giis.sisinfo.dto;

public class SocioDTO {
	private int idSocio;
	private String apellidos;
    private String nombre;
    private String numSocio;
    private String email;
    private String telefono;
    
    public SocioDTO() {}

    public SocioDTO(int idSocio,String apellidos, String nombre, String numSocio,
                    String email, String telefono) {
    	this.idSocio = idSocio;
        this.apellidos = apellidos;
        this.nombre = nombre;
        this.numSocio = numSocio;
        this.email = email;
        this.telefono = telefono;
    }
    
    public int getIdSocio() { return idSocio; }
    public String getApellidos() { return apellidos; }
    public String getNombre() { return nombre; }
    public String getNumSocio() { return numSocio; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }

	public void setIdSocio(int idSocio) {this.idSocio = idSocio;}
	public void setApellidos(String apellidos) {this.apellidos = apellidos;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	public void setNumSocio(String numSocio) {this.numSocio = numSocio;}
	public void setEmail(String email) {this.email = email;}
	public void setTelefono(String telefono) {this.telefono = telefono;}
    
}
