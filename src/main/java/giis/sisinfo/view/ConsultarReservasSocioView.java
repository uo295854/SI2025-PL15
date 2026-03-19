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
	JDateChooser dateChooserFechaInicial;
	JButton buscarButton;
	JTextPane socioNombre;
	JButton closeButton; 
	JDateChooser dateChooserFechaFinal;
	private JTextPane txtpnFechaActual;

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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 604, 390);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		socioNombre = new JTextPane();
		socioNombre.setEditable(false);
		socioNombre.setBounds(11, 11, 103, 19);
		socioNombre.setText("Socio: placeholder");
		contentPane.add(socioNombre);
		
		dateChooserFechaInicial = new JDateChooser();
		dateChooserFechaInicial.setBounds(275, 52, 92, 19);
		dateChooserFechaInicial.setEnabled(false);
		contentPane.add(dateChooserFechaInicial);
		
		JTextPane txtpnFechaInicial = new JTextPane();
		txtpnFechaInicial.setEditable(false);
		txtpnFechaInicial.setBounds(183, 52, 82, 18);
		txtpnFechaInicial.setText("Fecha Inicial:");
		contentPane.add(txtpnFechaInicial);
		
		checkBoxEstaSemana = new JCheckBox("Esta Semana");
		checkBoxEstaSemana.setSelected(true);
		checkBoxEstaSemana.setBounds(11, 36, 132, 20);
		contentPane.add(checkBoxEstaSemana);
		
		checkBoxEsteMes = new JCheckBox("Este Mes");
		checkBoxEsteMes.setBounds(11, 58, 132, 20);
		contentPane.add(checkBoxEsteMes);
		
		checkBoxPeriodoPersonalizado = new JCheckBox("Periodo Personalizado");
		checkBoxPeriodoPersonalizado.setBounds(11, 80, 173, 20);
		contentPane.add(checkBoxPeriodoPersonalizado);
		
		buscarButton = new JButton("Buscar");
		buscarButton.setBounds(416, 30, 132, 27);
		buscarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(buscarButton);
		
		closeButton = new JButton("Cerrar");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		closeButton.setBounds(416, 66, 132, 27);
		contentPane.add(closeButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(11, 124, 569, 217);
		contentPane.add(scrollPane);
		
		reservasTable = new JTable();
		reservasTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Instalacion Reservada", "Fecha", "Hora Inicial", "Hora Final"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		reservasTable.getColumnModel().getColumn(0).setResizable(false);
		scrollPane.setViewportView(reservasTable);
		
		dateChooserFechaFinal = new JDateChooser();
		dateChooserFechaFinal.setEnabled(false);
		dateChooserFechaFinal.setBounds(275, 81, 92, 19);
		contentPane.add(dateChooserFechaFinal);
		
		JTextPane txtpnFechaFinal = new JTextPane();
		txtpnFechaFinal.setText("Fecha Inicial:");
		txtpnFechaFinal.setEditable(false);
		txtpnFechaFinal.setBounds(183, 82, 82, 18);
		contentPane.add(txtpnFechaFinal);
		
		txtpnFechaActual = new JTextPane();
		txtpnFechaActual.setText("Fecha Actual:");
		txtpnFechaActual.setEditable(false);
		txtpnFechaActual.setBounds(194, 11, 173, 18);
		contentPane.add(txtpnFechaActual);

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

	public JDateChooser getDateChooserFechaInicial() {
		return dateChooserFechaInicial;
	}

	public void setDateChooserFechaInicial(JDateChooser dateChooser) {
		this.dateChooserFechaInicial = dateChooser;
	}

	public JDateChooser getDateChooserFechaFinal() {
		return dateChooserFechaFinal;
	}

	public void setDateChooserFechaFinal(JDateChooser dateChooserFechaFinal) {
		this.dateChooserFechaFinal = dateChooserFechaFinal;
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

	public JTextPane getTxtpnFechaActual() {
		return txtpnFechaActual;
	}

	public void setTxtpnFechaActual(JTextPane txtpnFechaActual) {
		this.txtpnFechaActual = txtpnFechaActual;
	}
	
}
