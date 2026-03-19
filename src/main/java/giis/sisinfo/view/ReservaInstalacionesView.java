package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ReservaInstalacionesView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JComboBox<String> comboBoxActividadesPendientes;
	private JButton btnReservar;
	private JButton btnCancelar;
	private JTable tableIncidencias;
	private DefaultTableModel tableModelIncidencias;

	public ReservaInstalacionesView() {
		setTitle("Reserva de Instalaciones");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// PANEL SUPERIOR
		JPanel panelTop = new JPanel();
		panelTop.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(panelTop, BorderLayout.NORTH);
		panelTop.setLayout(new BorderLayout(0, 0));

		JLabel lblTitulo = new JLabel("Reserva de Instalaciones");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelTop.add(lblTitulo, BorderLayout.NORTH);

		JPanel panelControles = new JPanel(new BorderLayout(20, 0));
		panelControles.setBorder(new EmptyBorder(25, 0, 10, 0));
		panelTop.add(panelControles, BorderLayout.CENTER);

		JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		panelControles.add(panelIzquierdo, BorderLayout.WEST);

		JLabel lblActividadesPendientes = new JLabel("Actividades pendientes de reserva:");
		lblActividadesPendientes.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelIzquierdo.add(lblActividadesPendientes);

		comboBoxActividadesPendientes = new JComboBox<>();
		comboBoxActividadesPendientes.setPreferredSize(new Dimension(260, 28));
		panelIzquierdo.add(comboBoxActividadesPendientes);

		// Ejemplo provisional para pruebas visuales
		// comboBoxActividadesPendientes.addItem("Partido de baloncesto");

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
		panelControles.add(panelBotones, BorderLayout.EAST);

		btnReservar = new JButton("Reservar");
		btnReservar.setPreferredSize(new Dimension(170, 35));
		panelBotones.add(btnReservar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setPreferredSize(new Dimension(170, 35));
		panelBotones.add(btnCancelar);

		// PANEL CENTRAL
		JPanel panelCenter = new JPanel(new BorderLayout(0, 10));
		panelCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(panelCenter, BorderLayout.CENTER);

		JLabel lblListaIncidencias = new JLabel("Lista de incidencias:");
		lblListaIncidencias.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelCenter.add(lblListaIncidencias, BorderLayout.NORTH);

		tableModelIncidencias = new DefaultTableModel(
			new Object[][] {},
			new String[] { "Socio", "Actividad", "Instalación", "Fecha", "Horas", "Estado" }
		) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableIncidencias = new JTable(tableModelIncidencias);
		tableIncidencias.setRowHeight(28);
		tableIncidencias.getTableHeader().setReorderingAllowed(false);
		tableIncidencias.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(tableIncidencias);
		panelCenter.add(scrollPane, BorderLayout.CENTER);
	}

	// =========================
	// GETTERS PARA EL CONTROLLER
	// =========================

	public JComboBox<String> getComboBoxActividadesPendientes() {
		return comboBoxActividadesPendientes;
	}

	public JButton getBtnReservar() {
		return btnReservar;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	public JTable getTableIncidencias() {
		return tableIncidencias;
	}

	public DefaultTableModel getTableModelIncidencias() {
		return tableModelIncidencias;
	}

	// =========================
	// HELPERS DE UI
	// =========================

	public void close() {
		dispose();
	}

	public void limpiarTablaIncidencias() {
		tableModelIncidencias.setRowCount(0);
	}

	public void addIncidenciaRow(Object[] rowData) {
		tableModelIncidencias.addRow(rowData);
	}
}