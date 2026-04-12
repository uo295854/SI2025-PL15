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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class ListadoCargosMensualesSocioView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	JComboBox selectorMes;
	JComboBox selectorAño;
	JComboBox selectorCargos;
	JButton botonBuscar;
	JTextPane txtpnMes;
	JTextPane txtpnAnyo;
	JTextPane txtpnCargos; 
	JTextPane txtpnPendienteDePago;
	private JScrollPane scrollPane;
	
	
	
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 975, 428);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		selectorMes = new JComboBox();
		selectorMes.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Febrero", "Marzo", "Junio", "Julio", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
		selectorMes.setBounds(261, 29, 80, 20);
		contentPane.add(selectorMes);
		
		selectorAño = new JComboBox();
		selectorAño.setModel(new DefaultComboBoxModel(new String[] {"2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032"}));
		selectorAño.setBounds(88, 29, 70, 20);
		contentPane.add(selectorAño);
		
		botonBuscar = new JButton("Buscar");
		botonBuscar.setBounds(711, 10, 106, 40);
		contentPane.add(botonBuscar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 69, 942, 282);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel());
		
		selectorCargos = new JComboBox();
		selectorCargos.setModel(new DefaultComboBoxModel(new String[] {"Actividades", "Reservas", "Ambos"}));
		selectorCargos.setBounds(434, 28, 67, 22);
		contentPane.add(selectorCargos);
		
		JTextPane txtpnMes = new JTextPane();
		txtpnMes.setEditable(false);
		txtpnMes.setText("Mes");
		txtpnMes.setBounds(213, 29, 44, 20);
		contentPane.add(txtpnMes);
		
		JTextPane txtpnAnyo = new JTextPane();
		txtpnAnyo.setText("Año");
		txtpnAnyo.setEditable(false);
		txtpnAnyo.setBounds(41, 29, 44, 20);
		contentPane.add(txtpnAnyo);
		
		JTextPane txtpnCargos = new JTextPane();
		txtpnCargos.setText("Cargos");
		txtpnCargos.setEditable(false);
		txtpnCargos.setBounds(380, 29, 44, 20);
		contentPane.add(txtpnCargos);
		
		txtpnPendienteDePago = new JTextPane();
		txtpnPendienteDePago.setText("Total pendiente de pago: 0,0€");
		txtpnPendienteDePago.setBounds(726, 358, 214, 20);
		contentPane.add(txtpnPendienteDePago);

	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JComboBox getSelectorMes() {
		return selectorMes;
	}

	public void setSelectorMes(JComboBox selectorMes) {
		this.selectorMes = selectorMes;
	}

	public JComboBox getSelectorAño() {
		return selectorAño;
	}

	public void setSelectorAño(JComboBox selectorAño) {
		this.selectorAño = selectorAño;
	}

	public JComboBox getSelectorCargos() {
		return selectorCargos;
	}

	public void setSelectorCargos(JComboBox selectorCargos) {
		this.selectorCargos = selectorCargos;
	}

	public JButton getBotonBuscar() {
		return botonBuscar;
	}

	public void setBotonBuscar(JButton botonBuscar) {
		this.botonBuscar = botonBuscar;
	}

	public JTextPane getTxtpnPendienteDePago() {
		return txtpnPendienteDePago;
	}

	public void setTxtpnPendienteDePago(JTextPane txtpnPendienteDePago) {
		this.txtpnPendienteDePago = txtpnPendienteDePago;
	}
	
}
