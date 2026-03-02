package giis.sisinfo.session;

public class Session {

	private static Session instance;

	private Integer idUsuario;
	private String username;
	private String rol; // ADMIN | SOCIO | NOSOCIO
	private Integer idSocio;
	private Integer idNoSocio;
	private Integer idAdmin;

	private Session() {}

	public static Session get() {
		if (instance == null) instance = new Session();
		return instance;
	}

	public void clear() {
		idUsuario = null;
		username = null;
		rol = null;
		idSocio = null;
		idNoSocio = null;
		idAdmin = null;
	}

	public boolean isLogged() { return idUsuario != null; }
	public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(rol); }
	public boolean isSocio() { return "SOCIO".equalsIgnoreCase(rol); }
	public boolean isNoSocio() { return "NOSOCIO".equalsIgnoreCase(rol); }

	// Getters/setters
	public Integer getIdUsuario() { return idUsuario; }
	public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getRol() { return rol; }
	public void setRol(String rol) { this.rol = rol; }

	public Integer getIdSocio() { return idSocio; }
	public void setIdSocio(Integer idSocio) { this.idSocio = idSocio; }

	public Integer getIdNoSocio() { return idNoSocio; }
	public void setIdNoSocio(Integer idNoSocio) { this.idNoSocio = idNoSocio; }

	public Integer getIdAdmin() { return idAdmin; }
	public void setIdAdmin(Integer idAdmin) { this.idAdmin = idAdmin; }
}