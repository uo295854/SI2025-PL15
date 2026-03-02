package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import giis.sisinfo.controller.ActividadesOfertadasController;
import giis.sisinfo.model.ActividadesOfertadasModel;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton btnGestionActividades;
	private JButton btnReservas;
	private JButton btnPeriodoInscripcion;              
	private JButton btnActividadesOfertadas;
	private JButton btnReservaInstalacionAdmin;
	private JButton btnReservaInstalacionAdminSocio;

	// BOTONES BD
	private JButton btnCrearBD;
	private JButton btnCargarDatos;

	public MainView() {
		setTitle("SisInfo - Gestión de Actividades");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(520, 560));
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(10, 10));

		// ===== TÍTULO =====
		JLabel lblTitle = new JLabel("Menú principal", SwingConstants.CENTER);
		lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
		lblTitle.setBorder(new EmptyBorder(15, 10, 5, 10));
		getContentPane().add(lblTitle, BorderLayout.NORTH);

		// ===== PANEL CENTRAL (app + BD) =====
		JPanel panelMain = new JPanel(new BorderLayout(10, 10));
		panelMain.setBorder(new EmptyBorder(10, 14, 10, 14));
		getContentPane().add(panelMain, BorderLayout.CENTER);

		// ==============================
		// PANEL BOTONES FUNCIONALES (APP)
		// ==============================
		JPanel panelAppButtons = new JPanel(new GridLayout(0, 1, 0, 10));
		panelMain.add(panelAppButtons, BorderLayout.CENTER);

		Dimension appBtnSize = new Dimension(360, 42);

		btnGestionActividades = new JButton("Gestión de actividades");
		btnGestionActividades.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnGestionActividades);

		btnReservas = new JButton("Reservas / Inscripciones");
		btnReservas.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnReservas);

		//  Botón Periodo de inscripción
		btnPeriodoInscripcion = new JButton("Periodo de inscripción");
		btnPeriodoInscripcion.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnPeriodoInscripcion);

		// Botón Actividades Ofertadas (mantengo tu lógica actual)
		btnActividadesOfertadas = new JButton("Lista Actividades Ofertadas");
		btnActividadesOfertadas.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnActividadesOfertadas);

		btnActividadesOfertadas.addActionListener(e -> {
			try {
				ActividadesOfertadasController controller =
						new ActividadesOfertadasController(new ActividadesOfertadasView(), new ActividadesOfertadasModel());
				controller.initController();
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this,
						"Error abriendo Actividades Ofertadas:\n" + ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		btnReservaInstalacionAdmin = new JButton("Reserva Instalaciones (Admin)");
		btnReservaInstalacionAdmin.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnReservaInstalacionAdmin);

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

		btnReservaInstalacionAdminSocio = new JButton("Reserva Instalaciones para los Socios (Admin)");
		btnReservaInstalacionAdminSocio.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnReservaInstalacionAdminSocio);

		// ==============================
		// BLOQUE BD SEPARADO
		// ==============================
		JPanel panelBDWrapper = new JPanel(new BorderLayout(0, 8));
		panelMain.add(panelBDWrapper, BorderLayout.SOUTH);

		JSeparator separator = new JSeparator();
		panelBDWrapper.add(separator, BorderLayout.NORTH);

		JPanel panelBD = new JPanel(new GridLayout(1, 2, 12, 0));
		panelBD.setBorder(BorderFactory.createTitledBorder("Administración Base de Datos"));
		panelBDWrapper.add(panelBD, BorderLayout.CENTER);

		btnCrearBD = new JButton("Crear BD (schema)");
		panelBD.add(btnCrearBD);

		btnCargarDatos = new JButton("Cargar datos (data)");
		panelBD.add(btnCargarDatos);

		// ===== PIE =====
		JLabel lblFooter = new JLabel("Grupo SisInfo", SwingConstants.CENTER);
		lblFooter.setBorder(new EmptyBorder(0, 10, 10, 10));
		getContentPane().add(lblFooter, BorderLayout.SOUTH);
	}

	// ===== Getters =====
	public JButton getBtnGestionActividades() { return btnGestionActividades; }
	public JButton getBtnReservas() { return btnReservas; }

	// ✅ Getter nuevo
	public JButton getBtnPeriodoInscripcion() { return btnPeriodoInscripcion; }

	public JButton getBtnReservaInstalacionAdminSocio() { return btnReservaInstalacionAdminSocio; }
	public JButton getBtnCrearBD() { return btnCrearBD; }
	public JButton getBtnCargarDatos() { return btnCargarDatos; }

	// Opcionales
	public JButton getBtnActividadesOfertadas() { return btnActividadesOfertadas; }
	public JButton getBtnReservaInstalacionAdmin() { return btnReservaInstalacionAdmin; }
}