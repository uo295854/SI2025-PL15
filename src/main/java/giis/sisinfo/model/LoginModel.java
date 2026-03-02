package giis.sisinfo.model;

import java.util.List;

import giis.sisinfo.dto.UsuarioDTO;
import giis.sisinfo.util.Database;

public class LoginModel {

	private final Database db;

	public LoginModel(Database db) {
		this.db = db;
	}

	public UsuarioDTO autenticar(String username, String password) {
		String sql =
			"SELECT id_usuario AS idUsuario, username AS username, rol AS rol, " +
			"       id_socio AS idSocio, id_nosocio AS idNosocio, id_admin AS idAdmin " +
			"  FROM Usuario " +
			" WHERE username = ? AND password = ?";

		List<UsuarioDTO> res = db.executeQueryPojo(UsuarioDTO.class, sql, username, password);
		return res.isEmpty() ? null : res.get(0);
	}
}