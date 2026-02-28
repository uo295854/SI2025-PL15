package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import giis.sisinfo.controller.ActividadesOfertadasController;
import giis.sisinfo.model.ActividadesOfertadasModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton btnGestionActividades;
	private JButton btnReservas;
	private JButton btnActividadesOfertadas;
	private JButton btnReservaInstalacionAdmin;
	private JButton btnReservaInstalacionAdminSocio;

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
		
		//Botón gestión actividades
		btnGestionActividades = new JButton("Gestión de actividades");
		btnGestionActividades.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnGestionActividades);
		
		//Botón Reservas / Inscripciones
		btnReservas = new JButton("Reservas / Inscripciones");
		btnReservas.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnReservas);

		//Botón Actividades Ofertadas - Alex - HU33746 
		btnActividadesOfertadas = new JButton("Lista Actividades Ofertadas");
		btnActividadesOfertadas.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnActividadesOfertadas);
		btnActividadesOfertadas.addActionListener(e ->{
			ActividadesOfertadasController controller = new ActividadesOfertadasController(new ActividadesOfertadasView(), new ActividadesOfertadasModel());
			controller.initController();
			ActividadesOfertadasView ventanaActividadesOfertadas = new ActividadesOfertadasView();
			//ventanaActividadesOfertadas.setVisible(true);
		});
		
		btnReservaInstalacionAdmin = new JButton("Reserva Instalaciones (Admin)");
		btnReservaInstalacionAdmin.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnReservaInstalacionAdmin);
		btnReservaInstalacionAdmin.addActionListener(e->{
			ReservaInstalacionAdminView ventanaReservaInstalacionAdmin = new ReservaInstalacionAdminView();
			ventanaReservaInstalacionAdmin.setVisible(true);
			
		});
		
		btnReservaInstalacionAdminSocio = new JButton("Reserva Instalaciones para los Socios (Admin)");
		btnReservaInstalacionAdminSocio.setPreferredSize(new Dimension(210, 45));
		panelButtons.add(btnReservaInstalacionAdminSocio);
		btnReservaInstalacionAdminSocio.addActionListener(e -> {
		    ReservaInstalacionAdminSocioView ventana = new ReservaInstalacionAdminSocioView();
		    ventana.setVisible(true);
		});
		
		
		
		
		//Botón Reserva Instalación

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
}
