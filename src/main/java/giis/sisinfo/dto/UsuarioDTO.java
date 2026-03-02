package giis.sisinfo.dto;

public class UsuarioDTO {
	private int idUsuario;
	private String username;
	private String rol;
	private Integer idSocio;
	private Integer idNosocio;
	private Integer idAdmin;

	public UsuarioDTO() {}

	public int getIdUsuario() { return idUsuario; }
	public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getRol() { return rol; }
	public void setRol(String rol) { this.rol = rol; }

	public Integer getIdSocio() { return idSocio; }
	public void setIdSocio(Integer idSocio) { this.idSocio = idSocio; }

	public Integer getIdNosocio() { return idNosocio; }
	public void setIdNosocio(Integer idNosocio) { this.idNosocio = idNosocio; }

	public Integer getIdAdmin() { return idAdmin; }
	public void setIdAdmin(Integer idAdmin) { this.idAdmin = idAdmin; }
}