package giis.sisinfo.view;

import java.awt.EventQueue;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTextPane;
import javax.swing.SpinnerDateModel;

import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JDateChooser;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class ConsultarReservasSocioView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JSpinner selectorHoraInicial;
	private JSpinner selectorHoraFinal;
	private JTable reservasTable;
	JCheckBox checkBoxEstaSemana;
	JCheckBox checkBoxEsteMes;
	JCheckBox checkBoxPeriodoPersonalizado; 
	JDateChooser dateChooser;
	JButton buscarButton;
	JTextPane socioNombre;
	JButton closeButton; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsultarReservasSocioView frame = new ConsultarReservasSocioView();
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
	public ConsultarReservasSocioView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 583, 390);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		socioNombre = new JTextPane();
		socioNombre.setBounds(11, 11, 92, 19);
		socioNombre.setText("Socio: placeholder");
		contentPane.add(socioNombre);
		
		dateChooser = new JDateChooser();
		dateChooser.setBounds(202, 30, 92, 19);
		contentPane.add(dateChooser);
		
		JTextPane txtpnFecha = new JTextPane();
		txtpnFecha.setText("Fecha:");
		txtpnFecha.setBounds(154, 30, 38, 18);
		contentPane.add(txtpnFecha);
		
		JTextPane txtpnHoraInicial = new JTextPane();
		txtpnHoraInicial.setText("Hora Inicial:");
		txtpnHoraInicial.setBounds(130, 58, 62, 18);
		contentPane.add(txtpnHoraInicial);
		
		JTextPane txtpnHoraFinal = new JTextPane();
		txtpnHoraFinal.setText("Hora Final:");
		txtpnHoraFinal.setBounds(130, 82, 62, 18);
		contentPane.add(txtpnHoraFinal);
		
		SpinnerDateModel timeModelInicial; 
		selectorHoraInicial = new JSpinner(new SpinnerDateModel(new Date(1772459731163L), null, null, Calendar.MINUTE));
		JSpinner.DateEditor de_selectorHoraInicial = new JSpinner.DateEditor(selectorHoraInicial, "HH:mm");
		selectorHoraInicial.setEditor(de_selectorHoraInicial);
		selectorHoraInicial.setBounds(202, 59, 92, 18);
		contentPane.add(selectorHoraInicial);
		
		// Selector hora final
		SpinnerDateModel timeModelFinal;
		selectorHoraFinal = new JSpinner(new SpinnerDateModel(new Date(1772459757224L), null, null, Calendar.MINUTE));
		JSpinner.DateEditor de_selectorHoraFinal = new JSpinner.DateEditor(selectorHoraFinal, "HH:mm");
		selectorHoraFinal.setEditor(de_selectorHoraFinal);
		selectorHoraFinal.setBounds(202, 82, 92, 18);
		contentPane.add(selectorHoraFinal);
		
		checkBoxEstaSemana = new JCheckBox("Esta Semana");
		checkBoxEstaSemana.setBounds(11, 36, 92, 20);
		contentPane.add(checkBoxEstaSemana);
		
		checkBoxEsteMes = new JCheckBox("Este Mes");
		checkBoxEsteMes.setBounds(11, 58, 92, 20);
		contentPane.add(checkBoxEsteMes);
		
		checkBoxPeriodoPersonalizado = new JCheckBox("Periodo Personalizado");
		checkBoxPeriodoPersonalizado.setBounds(11, 80, 92, 20);
		contentPane.add(checkBoxPeriodoPersonalizado);
		
		buscarButton = new JButton("Buscar");
		buscarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buscarButton.setBounds(345, 30, 132, 27);
		contentPane.add(buscarButton);
		
		closeButton = new JButton("Cerrar");
		closeButton.setBounds(345, 66, 132, 27);
		contentPane.add(closeButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(11, 124, 537, 217);
		contentPane.add(scrollPane);
		
		reservasTable = new JTable();
		reservasTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Instalacion Reservada", "Fecha", "Hora Inicial", "Fecha Final"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Object.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		reservasTable.getColumnModel().getColumn(0).setResizable(false);
		scrollPane.setViewportView(reservasTable);

	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public JSpinner getSelectorHoraInicial() {
		return selectorHoraInicial;
	}

	public void setSelectorHoraInicial(JSpinner selectorHoraInicial) {
		this.selectorHoraInicial = selectorHoraInicial;
	}

	public JSpinner getSelectorHoraFinal() {
		return selectorHoraFinal;
	}

	public void setSelectorHoraFinal(JSpinner selectorHoraFinal) {
		this.selectorHoraFinal = selectorHoraFinal;
	}

	public JTable getReservasTable() {
		return reservasTable;
	}

	public void setReservasTable(JTable reservasTable) {
		this.reservasTable = reservasTable;
	}

	public JCheckBox getCheckBoxEstaSemana() {
		return checkBoxEstaSemana;
	}

	public void setCheckBoxEstaSemana(JCheckBox checkBoxEstaSemana) {
		this.checkBoxEstaSemana = checkBoxEstaSemana;
	}

	public JCheckBox getCheckBoxEsteMes() {
		return checkBoxEsteMes;
	}

	public void setCheckBoxEsteMes(JCheckBox checkBoxEsteMes) {
		this.checkBoxEsteMes = checkBoxEsteMes;
	}

	public JCheckBox getCheckBoxPeriodoPersonalizado() {
		return checkBoxPeriodoPersonalizado;
	}

	public void setCheckBoxPeriodoPersonalizado(JCheckBox checkBoxPeriodoPersonalizado) {
		this.checkBoxPeriodoPersonalizado = checkBoxPeriodoPersonalizado;
	}

	public JDateChooser getDateChooser() {
		return dateChooser;
	}

	public void setDateChooser(JDateChooser dateChooser) {
		this.dateChooser = dateChooser;
	}

	public JButton getBuscarButton() {
		return buscarButton;
	}

	public void setBuscarButton(JButton buscarButton) {
		this.buscarButton = buscarButton;
	}

	public JTextPane getSocioNombre() {
		return socioNombre;
	}

	public void setSocioNombre(JTextPane socioNombre) {
		this.socioNombre = socioNombre;
	}

	public JButton getCloseButton() {
		return closeButton;
	}

	public void setCloseButton(JButton closeButton) {
		this.closeButton = closeButton;
	}
	
	
}
