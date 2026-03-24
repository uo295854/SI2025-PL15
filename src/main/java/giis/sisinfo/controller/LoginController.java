package giis.sisinfo.controller;

import javax.swing.JOptionPane;

import giis.sisinfo.dto.UsuarioDTO;
import giis.sisinfo.model.LoginModel;
import giis.sisinfo.session.Session;
import giis.sisinfo.util.Database;
import giis.sisinfo.view.LoginView;
import giis.sisinfo.view.MainView;

public class LoginController {

	private final LoginView view;
	private final LoginModel model;
	private Database db;

	public LoginController(LoginView view, LoginModel model, Database db) {
		this.view = view;
		this.model = model;
		this.db = db;

		Session.get().setDb(db);
		initController();
		view.setVisible(true);
	}

	private void initController() {
		view.getBtnSalir().addActionListener(e -> System.exit(0));
		view.getBtnLogin().addActionListener(e -> login());
		view.getTxtPassword().addActionListener(e -> login());

		// ✅ Botones BD antes del login
		view.getBtnCrearBD().addActionListener(e -> crearBD());
		view.getBtnCargarDatos().addActionListener(e -> cargarDatos());
	}

	private void crearBD() {
		try {
			db.createDatabase(false);
			JOptionPane.showMessageDialog(view, "Tablas creadas correctamente.");
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view,
					"Error creando la BD:\n" + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarDatos() {
		try {
			db.loadDatabase();
			JOptionPane.showMessageDialog(view, "Datos cargados correctamente.");
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view,
					"Error cargando datos:\n" + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void login() {
		String username = view.getTxtUsername().getText().trim();
		String password = new String(view.getTxtPassword().getPassword());

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Introduce usuario y contraseña.");
			return;
		}

		UsuarioDTO u;
		try {
			u = model.autenticar(username, password);
		} catch (Exception ex) {
			// Esto cubre el caso típico: tablas no creadas
			ex.printStackTrace();
			JOptionPane.showMessageDialog(view,
					"No se pudo acceder a la BD. ¿Has pulsado 'Crear BD' y 'Cargar datos'?\n\n" + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (u == null) {
			JOptionPane.showMessageDialog(view, "Credenciales incorrectas.");
			return;
		}

		// Guardar sesión
		Session s = Session.get();
		s.setIdUsuario(u.getIdUsuario());
		s.setUsername(u.getUsername());
		s.setRol(u.getRol());
		s.setIdSocio(u.getIdSocio());
		s.setIdNoSocio(u.getIdNosocio());
		s.setIdAdmin(u.getIdAdmin());

		// Abrir Main
		MainView mainView = new MainView();
		new MainController(mainView);
		mainView.setVisible(true);

		view.close();
	}
	
	public Database getDb() {
	    return db;
	}

	public void setDb(Database db) {
	    this.db = db;
	}
}