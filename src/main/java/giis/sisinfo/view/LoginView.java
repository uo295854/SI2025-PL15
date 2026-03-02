package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LoginView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField txtUsername;
	private JPasswordField txtPassword;

	private JButton btnLogin;
	private JButton btnSalir;

	// ✅ Botones BD (antes del login)
	private JButton btnCrearBD;
	private JButton btnCargarDatos;

	public LoginView() {
		setTitle("Login - SisInfo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(460, 330));
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(10, 10));

		JLabel title = new JLabel("Iniciar sesión", SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
		title.setBorder(new EmptyBorder(15, 10, 0, 10));
		getContentPane().add(title, BorderLayout.NORTH);

		JPanel center = new JPanel(new BorderLayout(10, 10));
		center.setBorder(new EmptyBorder(10, 18, 10, 18));
		getContentPane().add(center, BorderLayout.CENTER);

		// ===== Panel credenciales =====
		JPanel panelCred = new JPanel(new GridLayout(2, 2, 10, 10));
		center.add(panelCred, BorderLayout.NORTH);

		panelCred.add(new JLabel("Usuario:"));
		txtUsername = new JTextField();
		panelCred.add(txtUsername);

		panelCred.add(new JLabel("Contraseña:"));
		txtPassword = new JPasswordField();
		panelCred.add(txtPassword);

		// ===== Separador =====
		center.add(new JSeparator(), BorderLayout.CENTER);

		// ===== Panel BD =====
		JPanel panelBD = new JPanel(new GridLayout(1, 2, 12, 0));
		panelBD.setBorder(javax.swing.BorderFactory.createTitledBorder("Administración Base de Datos"));

		btnCrearBD = new JButton("Crear BD (schema)");
		btnCargarDatos = new JButton("Cargar datos (data)");

		panelBD.add(btnCrearBD);
		panelBD.add(btnCargarDatos);

		center.add(panelBD, BorderLayout.SOUTH);

		// ===== Botones login =====
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		bottom.setBorder(new EmptyBorder(0, 10, 15, 10));
		getContentPane().add(bottom, BorderLayout.SOUTH);

		btnSalir = new JButton("Salir");
		btnLogin = new JButton("Entrar");

		bottom.add(btnSalir);
		bottom.add(btnLogin);
	}

	public JTextField getTxtUsername() { return txtUsername; }
	public JPasswordField getTxtPassword() { return txtPassword; }
	public JButton getBtnLogin() { return btnLogin; }
	public JButton getBtnSalir() { return btnSalir; }

	public JButton getBtnCrearBD() { return btnCrearBD; }
	public JButton getBtnCargarDatos() { return btnCargarDatos; }

	public void close() { dispose(); }
}