package giis.sisinfo.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CancelarActividadAdminView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTextPane DetallesActividad;
	private JTextArea DetallesCancelacion;
	JComboBox selectorInstalaciones;
	JComboBox selectorRazonCancelacion;
	JButton botonConfirmar;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CancelarActividadAdminView frame = new CancelarActividadAdminView();
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
	public CancelarActividadAdminView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 501);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Instalación:");
		lblNewLabel.setBounds(10, 26, 88, 14);
		contentPane.add(lblNewLabel);
		
		selectorInstalaciones = new JComboBox();
		selectorInstalaciones.setModel(new DefaultComboBoxModel(new String[] {"Pista Pádel 1", "Pista Tenis 1", "Sala Multiusos"}));
		selectorInstalaciones.setSelectedIndex(0);
		selectorInstalaciones.setToolTipText("");
		selectorInstalaciones.setBounds(98, 22, 185, 22);
		contentPane.add(selectorInstalaciones);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 55, 398, 174);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"IdActividad", "Nombre Actividad", "Fecha Inicio", "Fecha Fin"
			}
		));
		scrollPane.setViewportView(table);
		
		JLabel lblRaznDeCancelacin = new JLabel("Razón de cancelación");
		lblRaznDeCancelacin.setBounds(440, 68, 126, 14);
		contentPane.add(lblRaznDeCancelacin);
		
		selectorRazonCancelacion = new JComboBox();
		selectorRazonCancelacion.setModel(new DefaultComboBoxModel(new String[] {"Falta de participación", "Falta de material", "Falta de presupuesto", "Otro..."}));
		selectorRazonCancelacion.setSelectedIndex(0);
		selectorRazonCancelacion.setBounds(440, 84, 171, 22);
		contentPane.add(selectorRazonCancelacion);
		
		JLabel lblDetalles = new JLabel("Detalles:");
		lblDetalles.setBounds(440, 121, 126, 14);
		contentPane.add(lblDetalles);
		
		JLabel lblDetallesDeActividad = new JLabel("Detalles de actividad:");
		lblDetallesDeActividad.setBounds(10, 241, 126, 14);
		contentPane.add(lblDetallesDeActividad);
		
		DetallesActividad = new JTextPane();
		DetallesActividad.setBounds(20, 264, 398, 169);
		contentPane.add(DetallesActividad);
		
		DetallesCancelacion = new JTextArea();
		DetallesCancelacion.setFont(new Font("Tahoma", Font.PLAIN, 10));
		DetallesCancelacion.setBounds(440, 141, 184, 217);
		DetallesCancelacion.setLineWrap(true);
		contentPane.add(DetallesCancelacion);
		
		
		botonConfirmar = new JButton("Confirmar \r\nCancelación");
		botonConfirmar.setBounds(440, 369, 184, 64);
		contentPane.add(botonConfirmar);

	}

	public JTable getTable() {return table;}
	public void setTable(JTable table) {this.table = table;}
	public JTextPane getDetallesActividad() {return DetallesActividad;}
	public void setDetallesActividad(JTextPane detallesActividad) {DetallesActividad = detallesActividad;}
	public JTextArea getDetallesCancelacion() {return DetallesCancelacion;}
	public void setDetallesCancelacion(JTextArea detallesCancelacion) {DetallesCancelacion = detallesCancelacion;}
	public JComboBox getSelectorInstalaciones() {return selectorInstalaciones;}
	public void setSelectorInstalaciones(JComboBox selectorInstalaciones) {this.selectorInstalaciones = selectorInstalaciones;}
	public JComboBox getSelectorRazonCancelacion() {return selectorRazonCancelacion;}
	public void setSelectorRazonCancelacion(JComboBox selectorRazonCancelacion) {this.selectorRazonCancelacion = selectorRazonCancelacion;}
	public JButton getBotonConfirmar() {return botonConfirmar;}
	public void setBotonConfirmar(JButton botonConfirmar) {this.botonConfirmar = botonConfirmar;}
	
	
	
	
}
