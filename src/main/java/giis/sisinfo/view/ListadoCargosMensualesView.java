package giis.sisinfo.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ListadoCargosMensualesView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	JComboBox selectorAño;
	JComboBox selectorMes;
	JButton botonBuscar;
	JButton botonExportarCSV;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListadoCargosMensualesView frame = new ListadoCargosMensualesView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ListadoCargosMensualesView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 695, 489);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Año:");
		lblNewLabel.setBounds(62, 45, 31, 24);
		contentPane.add(lblNewLabel);
		
		selectorAño = new JComboBox();
		selectorAño.setModel(new DefaultComboBoxModel(new String[] {"2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031"}));
		selectorAño.setSelectedIndex(5);
		selectorAño.setBounds(103, 46, 80, 22);
		contentPane.add(selectorAño);
		
		selectorMes = new JComboBox();
		selectorMes.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Ferbrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
		selectorMes.setSelectedIndex(0);
		selectorMes.setBounds(272, 46, 74, 22);
		contentPane.add(selectorMes);
		
		JLabel lblMes = new JLabel("Mes:");
		lblMes.setBounds(229, 45, 55, 24);
		contentPane.add(lblMes);
		
		botonBuscar = new JButton("Buscar");
		botonBuscar.setBounds(478, 38, 127, 38);
		contentPane.add(botonBuscar);
		
		botonExportarCSV = new JButton("Exportar a CSV");
		botonExportarCSV.setBounds(266, 409, 147, 30);
		contentPane.add(botonExportarCSV);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 101, 659, 294);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Numero de Socio", "Nombre de Socio", "DNI", "Reservas", "Actividades", "Total"
			}
		));
		scrollPane.setViewportView(table);

	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JComboBox getSelectorAño() {
		return selectorAño;
	}

	public void setSelectorAño(JComboBox selectorAño) {
		this.selectorAño = selectorAño;
	}

	public JComboBox getSelectorMes() {
		return selectorMes;
	}

	public void setSelectorMes(JComboBox selectorMes) {
		this.selectorMes = selectorMes;
	}

	public JButton getBotonBuscar() {
		return botonBuscar;
	}

	public void setBotonBuscar(JButton botonBuscar) {
		this.botonBuscar = botonBuscar;
	}

	public JButton getBotonExportarCSV() {
		return botonExportarCSV;
	}

	public void setBotonExportarCSV(JButton botonExportarCSV) {
		this.botonExportarCSV = botonExportarCSV;
	}
	
	
}
