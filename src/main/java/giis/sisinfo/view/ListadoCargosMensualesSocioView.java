package giis.sisinfo.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.DefaultComboBoxModel;

public class ListadoCargosMensualesSocioView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	JComboBox selectorMes;
	JComboBox selectorAño;
	JComboBox selectorCargos;
	JButton botonBuscar;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListadoCargosMensualesSocioView frame = new ListadoCargosMensualesSocioView();
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
	public ListadoCargosMensualesSocioView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 708, 446);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		selectorMes = new JComboBox();
		selectorMes.setModel(new DefaultComboBoxModel(new String[] {"2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032"}));
		selectorMes.setBounds(55, 20, 80, 20);
		contentPane.add(selectorMes);
		
		selectorAño = new JComboBox();
		selectorAño.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Febrero", "Marzo", "Junio", "Julio", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Noviembre", "Diciembre"}));
		selectorAño.setBounds(228, 20, 70, 20);
		contentPane.add(selectorAño);
		
		botonBuscar = new JButton("Buscar");
		botonBuscar.setBounds(537, 10, 106, 40);
		contentPane.add(botonBuscar);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column", "New column"
			}
		));
		table.setBounds(10, 69, 633, 282);
		contentPane.add(table);
		
		selectorCargos = new JComboBox();
		selectorCargos.setBounds(418, 19, 67, 22);
		contentPane.add(selectorCargos);
		
		JTextPane txtpnMes = new JTextPane();
		txtpnMes.setEditable(false);
		txtpnMes.setText("Mes");
		txtpnMes.setBounds(182, 20, 44, 20);
		contentPane.add(txtpnMes);
		
		JTextPane txtpnAnyo = new JTextPane();
		txtpnAnyo.setText("Año");
		txtpnAnyo.setEditable(false);
		txtpnAnyo.setBounds(10, 20, 44, 20);
		contentPane.add(txtpnAnyo);
		
		JTextPane txtpnCargos = new JTextPane();
		txtpnCargos.setText("Cargos");
		txtpnCargos.setEditable(false);
		txtpnCargos.setBounds(332, 20, 44, 20);
		contentPane.add(txtpnCargos);

	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
}
