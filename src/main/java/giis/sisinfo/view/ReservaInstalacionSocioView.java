package giis.sisinfo.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReservaInstalacionSocioView extends JFrame {

	private static final long serialVersionUID = 1L;

	// Tablas
	private JTable tablaDias;
	private JTable tablaHoras;

	public JTable getTablaDias() { return tablaDias; }
	public JTable getTablaHoras() { return tablaHoras; }

	// Combo box
	private JComboBox<String> cbDeporte;
	private JComboBox<String> cbPista;

	public JComboBox<String> getCbDeporte() { return cbDeporte; }
	public JComboBox<String> getCbPista() { return cbPista; }

	// Resumen
	private JTextField textfieldDeporte;
	private JTextField textfieldLugar;
	private JTextField textfieldDia;
	private JTextField textfieldHora;
	private JTextField textfieldCoste;

	// Botones
	private JButton cancelar;
	private JButton reservar;

	public JButton getCancelar() { return cancelar; }
	public JButton getReservar() { return reservar; }

	// Forma de pago
	private JRadioButton rbPagoUso;
	private JRadioButton rbPagoMensual;

	public JRadioButton getRbPagoUso() { return rbPagoUso; }
	public JRadioButton getRbPagoMensual() { return rbPagoMensual; }

	public ReservaInstalacionSocioView() {
		setTitle("Reserva de una instalación");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		JPanel root = new JPanel(new BorderLayout(10, 10));
		root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setContentPane(root);

		// Parte superior: selección de instalación
		root.add(panelInstalacion(), BorderLayout.NORTH);

		// Parte central: días y horas
		JPanel centro = new JPanel(new GridLayout(1, 2, 10, 10));
		centro.add(panelDias());
		centro.add(panelHoras());
		root.add(centro, BorderLayout.CENTER);

		// Parte inferior: resumen
		root.add(panelAbajo(), BorderLayout.SOUTH);
	}

	private JPanel panelInstalacion() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new TitledBorder("Selección de instalación"));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(6, 6, 6, 6);
		c.anchor = GridBagConstraints.WEST;

		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Tipo deporte:"), c);

		cbDeporte = new JComboBox<>();
		cbDeporte.setPreferredSize(new Dimension(140, 25));
		c.gridx = 1;
		panel.add(cbDeporte, c);

		c.gridx = 2;
		panel.add(new JLabel("Módulo:"), c);

		cbPista = new JComboBox<>();
		cbPista.setPreferredSize(new Dimension(140, 25));
		c.gridx = 3;
		panel.add(cbPista, c);

		return panel;
	}

	private JPanel panelDias() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(new TitledBorder("Seleccionar fecha (lista de días)"));

		tablaDias = new JTable(new DefaultTableModel(
				new Object[] { "Día", "Fecha", "Estado" }, 0
		));
		tablaDias.setRowHeight(22);

		panel.add(new JScrollPane(tablaDias), BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(400, 220));
		return panel;
	}

	private JPanel panelHoras() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(new TitledBorder("Horas del día"));

		tablaHoras = new JTable(new DefaultTableModel(
				new Object[] { "Hora", "Estado", "Motivo" }, 0
		));
		tablaHoras.setRowHeight(22);

		panel.add(new JScrollPane(tablaHoras), BorderLayout.CENTER);
		panel.setPreferredSize(new Dimension(400, 220));
		return panel;
	}

	private JPanel panelAbajo() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new TitledBorder("Resumen"));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(4, 8, 4, 8);
		c.anchor = GridBagConstraints.WEST;

		textfieldDeporte = new JTextField(10);
		textfieldDeporte.setEditable(false);

		textfieldLugar = new JTextField(10);
		textfieldLugar.setEditable(false);

		textfieldDia = new JTextField(12);
		textfieldDia.setEditable(false);

		textfieldHora = new JTextField(10);
		textfieldHora.setEditable(false);

		textfieldCoste = new JTextField(6);
		textfieldCoste.setEditable(false);

		// Columna izquierda
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Deporte"), c);

		c.gridx = 1;
		panel.add(textfieldDeporte, c);

		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("Lugar"), c);

		c.gridx = 1;
		panel.add(textfieldLugar, c);

		c.gridx = 0;
		c.gridy = 2;
		panel.add(new JLabel("Día"), c);

		c.gridx = 1;
		panel.add(textfieldDia, c);

		// Columna central
		c.gridx = 2;
		c.gridy = 0;
		panel.add(new JLabel("Hora"), c);

		c.gridx = 3;
		panel.add(textfieldHora, c);

		c.gridx = 2;
		c.gridy = 1;
		panel.add(new JLabel("Coste"), c);

		c.gridx = 3;
		panel.add(textfieldCoste, c);

		// Forma de pago
		rbPagoUso = new JRadioButton("Pagar en el momento del uso");
		rbPagoMensual = new JRadioButton("Añadir a la cuota mensual");

		ButtonGroup grupoPago = new ButtonGroup();
		grupoPago.add(rbPagoUso);
		grupoPago.add(rbPagoMensual);

		c.gridx = 4;
		c.gridy = 0;
		panel.add(new JLabel("Forma de pago:"), c);

		c.gridy = 1;
		panel.add(rbPagoUso, c);

		c.gridy = 2;
		panel.add(rbPagoMensual, c);

		// Botones
		cancelar = new JButton("Cancelar");
		reservar = new JButton("Reservar");

		c.gridx = 4;
		c.gridy = 4;
		c.anchor = GridBagConstraints.EAST;
		panel.add(cancelar, c);

		c.gridx = 5;
		panel.add(reservar, c);

		return panel;
	}

	public void setResumenReserva(String deporte, String lugar, String dia, String hora, String coste) {
		textfieldDeporte.setText(deporte);
		textfieldLugar.setText(lugar);
		textfieldDia.setText(dia);
		textfieldHora.setText(hora);
		textfieldCoste.setText(coste);
	}

	public void limpiarHorasYResumenReserva() {
		((DefaultTableModel) tablaHoras.getModel()).setRowCount(0);
		tablaHoras.clearSelection();
		setResumenReserva("", "", "", "", "");
	}

	public String getEstadoPagoSeleccionado() {
		if (rbPagoUso.isSelected()) {
			return "PAGADO";
		}
		if (rbPagoMensual.isSelected()) {
			return "PENDIENTE";
		}
		return "";
	}

	public boolean hayFormaPagoSeleccionada() {
		return rbPagoUso.isSelected() || rbPagoMensual.isSelected();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new ReservaInstalacionSocioView().setVisible(true));
	}
}