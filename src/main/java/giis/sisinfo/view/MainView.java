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

public class MainView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton btnGestionActividades;
	private JButton btnReservas;

	public MainView() {
		setTitle("SisInfo - Gestión de Actividades");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(520, 220));
		setLocationRelativeTo(null); // centrar
		setLayout(new BorderLayout(10, 10));

		// Título
		JLabel lblTitle = new JLabel("Menú principal", SwingConstants.CENTER);
		lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
		lblTitle.setBorder(new EmptyBorder(15, 10, 5, 10));
		add(lblTitle, BorderLayout.NORTH);

		// Panel central con botones
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 25));
		panelButtons.setBorder(new EmptyBorder(10, 10, 10, 10));

		btnGestionActividades = new JButton("Gestión de actividades");
		btnGestionActividades.setPreferredSize(new Dimension(210, 45));

		btnReservas = new JButton("Reservas / Inscripciones");
		btnReservas.setPreferredSize(new Dimension(210, 45));

		panelButtons.add(btnGestionActividades);
		panelButtons.add(btnReservas);

		add(panelButtons, BorderLayout.CENTER);

		// Pie
		JLabel lblFooter = new JLabel("Grupo SisInfo", SwingConstants.CENTER);
		lblFooter.setBorder(new EmptyBorder(0, 10, 10, 10));
		add(lblFooter, BorderLayout.SOUTH);
	}

	// Getters para que el Controller pueda enganchar listeners
	public JButton getBtnGestionActividades() {
		return btnGestionActividades;
	}

	public JButton getBtnReservas() {
		return btnReservas;
	}
}
