package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	private JButton btnVisualizarReservasInstalacionesAdmin;
	private JButton btnConsultarReservasSocioView;
	private JButton btnReservaInstalacionesAuto;




	public MainView() {
		setTitle("SisInfo - Gestión de Actividades");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(620, 528));
		setLocationRelativeTo(null); // centrar
		getContentPane().setLayout(new BorderLayout(10, 10));

		JLabel lblTitle = new JLabel("Menú principal", SwingConstants.CENTER);
		lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
		lblTitle.setBorder(new EmptyBorder(15, 10, 5, 10));
		getContentPane().add(lblTitle, BorderLayout.NORTH);

		JPanel panelMain = new JPanel(new BorderLayout(10, 10));
		panelMain.setBorder(new EmptyBorder(10, 14, 10, 14));
		getContentPane().add(panelMain, BorderLayout.CENTER);

		JPanel panelAppButtons = new JPanel(new GridLayout(0, 1, 0, 10));
		panelMain.add(panelAppButtons, BorderLayout.CENTER);

		Dimension appBtnSize = new Dimension(360, 42);

		btnGestionActividades = new JButton("Gestión de actividades");
		btnGestionActividades.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnGestionActividades);

		btnReservas = new JButton("Reservas / Inscripciones");
		btnReservas.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnReservas);

		btnPeriodoInscripcion = new JButton("Periodo de inscripción");
		btnPeriodoInscripcion.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnPeriodoInscripcion);

		btnActividadesOfertadas = new JButton("Lista Actividades Ofertadas");
		btnActividadesOfertadas.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnActividadesOfertadas);

		// (Ideal moverlo a MainController)
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

		btnReservaInstalacionAdmin = new JButton("Reserva Instalaciones para una Actividad");
		btnReservaInstalacionAdmin.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnReservaInstalacionAdmin);


		btnReservaInstalacionAdminSocio = new JButton("Reserva Instalaciones para los Socios");
		btnReservaInstalacionAdminSocio.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnReservaInstalacionAdminSocio);
		
		btnVisualizarReservasInstalacionesAdmin = new JButton("Visualizar Reservas de Instalaciones");
		btnVisualizarReservasInstalacionesAdmin.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnVisualizarReservasInstalacionesAdmin);
		
		// Botón Consultar Reservas de instalaciones (Socio)
		btnConsultarReservasSocioView = new JButton("Consultar Reservas (Socio)");
		btnConsultarReservasSocioView.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnConsultarReservasSocioView);

		btnReservaInstalacionesAuto = new JButton("Reserva automática de instalaciones para actividades");
		btnReservaInstalacionesAuto.setPreferredSize(appBtnSize);
		panelAppButtons.add(btnReservaInstalacionesAuto);

		// ===== BOTONES BD (sin lógica aquí; el Controller engancha listeners) =====

		JLabel lblFooter = new JLabel("Grupo SisInfo", SwingConstants.CENTER);
		lblFooter.setBorder(new EmptyBorder(0, 10, 10, 10));
		getContentPane().add(lblFooter, BorderLayout.SOUTH);
	}

	public JButton getBtnGestionActividades() { return btnGestionActividades; }
	public JButton getBtnReservas() { return btnReservas; }
	public JButton getBtnPeriodoInscripcion() { return btnPeriodoInscripcion; }


	
	public JButton getBtnReservaInstalacionAdminSocio() {
	    return btnReservaInstalacionAdminSocio;
	}
	public JButton getBtnVisualizarReservasInstalaciones() {
		return btnVisualizarReservasInstalacionesAdmin;
	}
	public JButton getBtnReservaInstalacionesAuto() {
		return btnReservaInstalacionesAuto;
	}

	public JButton getBtnConsultarReservasSocioView() {
		return btnConsultarReservasSocioView;
	}

	// opcionales
	public JButton getBtnActividadesOfertadas() { return btnActividadesOfertadas; }
	public JButton getBtnReservaInstalacionAdmin() { return btnReservaInstalacionAdmin; }
}