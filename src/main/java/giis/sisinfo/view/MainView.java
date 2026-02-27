package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton btnGestionActividades;
	private JButton btnReservas;
	private JButton btnActividadesOfertadas;
	private JButton btnReservaInstalacionAdmin;
	private JButton btnReservaInstalacionAdminSocio;

	// NUEVOS BOTONES BD
	private JButton btnCrearBD;
	private JButton btnCargarDatos;

	public MainView() {
		setTitle("SisInfo - Gestión de Actividades");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(520, 428));
		setLocationRelativeTo(null); // centrar
		getContentPane().setLayout(new BorderLayout(10, 10));

		// Título
		JLabel lblTitle = new JLabel("Menú principal", SwingConstants.CENTER);
		lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
		lblTitle.setBorder(new EmptyBorder(15, 10, 5, 10));
		getContentPane().add(lblTitle, BorderLayout.NORTH);

		// Panel central con botones
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));
		panelButtons.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(panelButtons, BorderLayout.CENTER);

		// Botón gestión actividades
		btnGestionActividades = new JButton("Gestión de actividades");
		btnGestionActividades.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnGestionActividades);

		// Botón Reservas / Inscripciones
		btnReservas = new JButton("Reservas / Inscripciones");
		btnReservas.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnReservas);

		// Botón Actividades Ofertadas
		btnActividadesOfertadas = new JButton("Lista Actividades Ofertadas");
		btnActividadesOfertadas.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnActividadesOfertadas);
		btnActividadesOfertadas.addActionListener(e -> {
			try {
				ActividadesOfertadasView ventanaActividadesOfertadas = new ActividadesOfertadasView();
				ventanaActividadesOfertadas.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this,
						"Error abriendo Actividades Ofertadas:\n" + ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Botón Reserva Instalaciones (Admin)
		btnReservaInstalacionAdmin = new JButton("Reserva Instalaciones (Admin)");
		btnReservaInstalacionAdmin.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnReservaInstalacionAdmin);
		btnReservaInstalacionAdmin.addActionListener(e -> {
			try {
				ReservaInstalacionAdminView ventanaReservaInstalacionAdmin = new ReservaInstalacionAdminView();
				ventanaReservaInstalacionAdmin.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this,
						"Error abriendo Reserva Instalaciones (Admin):\n" + ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Botón Reserva Instalaciones para los Socios (Admin)
		btnReservaInstalacionAdminSocio = new JButton("Reserva Instalaciones para los Socios (Admin)");
		btnReservaInstalacionAdminSocio.setPreferredSize(new Dimension(260, 45));
		panelButtons.add(btnReservaInstalacionAdminSocio);
		btnReservaInstalacionAdminSocio.addActionListener(e -> {
			try {
				ReservaInstalacionAdminSocioView ventana = new ReservaInstalacionAdminSocioView();
				ventana.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this,
						"Error abriendo Reserva Instalaciones (Admin Socio):\n" + ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ===== BOTONES BD (sin lógica aquí; el Controller engancha listeners) =====
		btnCrearBD = new JButton("Crear BD (schema)");
		btnCrearBD.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnCrearBD);

		btnCargarDatos = new JButton("Cargar datos (data)");
		btnCargarDatos.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnCargarDatos);

		// Pie
		JLabel lblFooter = new JLabel("Grupo SisInfo", SwingConstants.CENTER);
		lblFooter.setBorder(new EmptyBorder(0, 10, 10, 10));
		getContentPane().add(lblFooter, BorderLayout.SOUTH);
	}

	// Getters para que el Controller pueda enganchar listeners
	public JButton getBtnGestionActividades() {
		return btnGestionActividades;
	}

	public JButton getBtnReservas() {
		return btnReservas;
	}

	public JButton getBtnCrearBD() {
		return btnCrearBD;
	}

	public JButton getBtnCargarDatos() {
		return btnCargarDatos;
	}
}