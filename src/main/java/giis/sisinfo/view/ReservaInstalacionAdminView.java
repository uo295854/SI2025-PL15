package giis.sisinfo.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.SpinnerDateModel;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import java.awt.TextField;
import com.toedter.calendar.JDateChooser;
import javax.swing.JSpinner;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.Calendar;
import java.awt.TextArea;
import java.awt.Choice;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import java.awt.Font;

public class ReservaInstalacionAdminView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JComboBox selectorInstalaciones;
	JSpinner selectorHoraFinal;
	JSpinner selectorHoraInicial;
	TextArea panelConflictos;
	Button botonReservar;
	JComboBox selectorActividad;
	TextField detallesActividad;
	JComboBox selectorFechas;
	JTextArea txtrAvisoConflictos;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReservaInstalacionAdminView frame = new ReservaInstalacionAdminView();
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
	public ReservaInstalacionAdminView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 590, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextPane txtpnInstalacinAReservar = new JTextPane();
		txtpnInstalacinAReservar.setEditable(false);
		txtpnInstalacinAReservar.setText("Instalación a reservar:");
		txtpnInstalacinAReservar.setBackground(new Color(240, 240, 240));
		txtpnInstalacinAReservar.setBounds(10, 10, 138, 18);
		contentPane.add(txtpnInstalacinAReservar);
		
		selectorInstalaciones = new JComboBox();
		selectorInstalaciones.setBounds(144, 10, 198, 20);
		contentPane.add(selectorInstalaciones);
		
		JTextPane txtpnMotivoDeReserva = new JTextPane();
		txtpnMotivoDeReserva.setEditable(false);
		txtpnMotivoDeReserva.setText("Actividad:");
		txtpnMotivoDeReserva.setBackground(UIManager.getColor("Button.background"));
		txtpnMotivoDeReserva.setBounds(58, 43, 65, 18);
		contentPane.add(txtpnMotivoDeReserva);
		
		selectorActividad = new JComboBox();
		selectorActividad.setBounds(144, 41, 198, 20);
		contentPane.add(selectorActividad);
		
		JTextPane txtpnHoraInicial = new JTextPane();
		txtpnHoraInicial.setEditable(false);
		txtpnHoraInicial.setText("Hora inicial:");
		txtpnHoraInicial.setBackground(UIManager.getColor("Button.background"));
		txtpnHoraInicial.setBounds(44, 100, 79, 18);
		contentPane.add(txtpnHoraInicial);
		
		JTextPane txtpnHoraFinal = new JTextPane();
		txtpnHoraFinal.setEditable(false);
		txtpnHoraFinal.setText("Hora final:");
		txtpnHoraFinal.setBackground(UIManager.getColor("Button.background"));
		txtpnHoraFinal.setBounds(44, 126, 79, 18);
		contentPane.add(txtpnHoraFinal);
		
		// Selector hora final
		SpinnerDateModel timeModelFinal;
		selectorHoraFinal = new JSpinner(new SpinnerDateModel(new Date(1772459757224L), null, null, Calendar.MINUTE));
		JSpinner.DateEditor de_selectorHoraFinal = new JSpinner.DateEditor(selectorHoraFinal, "HH:mm");
		selectorHoraFinal.setEditor(de_selectorHoraFinal);
		selectorHoraFinal.setBounds(144, 126, 72, 18);
		contentPane.add(selectorHoraFinal);
		
		// Selector hora inicial
		SpinnerDateModel timeModelInicial; 
		selectorHoraInicial = new JSpinner(new SpinnerDateModel(new Date(1772459731163L), null, null, Calendar.MINUTE));
		JSpinner.DateEditor de_selectorHoraInicial = new JSpinner.DateEditor(selectorHoraInicial, "HH:mm");
		selectorHoraInicial.setEditor(de_selectorHoraInicial);
		selectorHoraInicial.setBounds(144, 100, 72, 18);
		contentPane.add(selectorHoraInicial);
		
		JTextPane txtpnDetalles = new JTextPane();
		txtpnDetalles.setText("Detalles:");
		txtpnDetalles.setEditable(false);
		txtpnDetalles.setBackground(UIManager.getColor("Button.background"));
		txtpnDetalles.setBounds(10, 155, 86, 18);
		contentPane.add(txtpnDetalles);
		
		detallesActividad = new TextField();
		detallesActividad.setBounds(10, 179, 546, 94);
		contentPane.add(detallesActividad);
		
		panelConflictos = new TextArea();
		panelConflictos.setEditable(false);
		panelConflictos.setBounds(10, 303, 546, 94);
		contentPane.add(panelConflictos);
		
		JTextPane txtpnConflictos = new JTextPane();
		txtpnConflictos.setText("Conflictos:");
		txtpnConflictos.setEditable(false);
		txtpnConflictos.setBackground(UIManager.getColor("Button.background"));
		txtpnConflictos.setBounds(10, 279, 86, 18);
		contentPane.add(txtpnConflictos);
		
		botonReservar = new Button("Reservar");
		botonReservar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		botonReservar.setBounds(401, 43, 86, 35);
		contentPane.add(botonReservar);
		
		JTextPane txtpnFecha = new JTextPane();
		txtpnFecha.setText("Fecha:");
		txtpnFecha.setEditable(false);
		txtpnFecha.setBackground(UIManager.getColor("Button.background"));
		txtpnFecha.setBounds(51, 72, 72, 18);
		contentPane.add(txtpnFecha);
		
		selectorFechas = new JComboBox();
		selectorFechas.setModel(new DefaultComboBoxModel(new String[] {"2000-01-01"}));
		selectorFechas.setSelectedIndex(0);
		selectorFechas.setBounds(144, 72, 154, 22);
		contentPane.add(selectorFechas);
		
		txtrAvisoConflictos = new JTextArea();
		txtrAvisoConflictos.setFont(new Font("Arial", Font.PLAIN, 9));
		txtrAvisoConflictos.setText("¡Si presionas el botón \"Reservar\"\r\ncuando hay conflictos, se borrarán \r\nlas reservas conflictivas!");
		txtrAvisoConflictos.setEditable(false);
		txtrAvisoConflictos.setBounds(376, 100, 162, 44);
		contentPane.add(txtrAvisoConflictos);
		txtrAvisoConflictos.setVisible(false);

	}
	
	
	public JComboBox getSelectorInstalaciones() {
		return selectorInstalaciones;
	}

	public void setSelectorInstalaciones(JComboBox selectorInstalaciones) {
		this.selectorInstalaciones = selectorInstalaciones;
	}
	
	public JSpinner getSelectorHoraFinal() {
		return selectorHoraFinal;
	}

	public void setSelectorHoraFinal(JSpinner selectorHoraFinal) {
		this.selectorHoraFinal = selectorHoraFinal;
	}

	public JSpinner getSelectorHoraInicial() {
		return selectorHoraInicial;
	}

	public void setSelectorHoraInicial(JSpinner selectorHoraInicial) {
		this.selectorHoraInicial = selectorHoraInicial;
	}

	public TextArea getPanelConflictos() {
		return panelConflictos;
	}

	public void setPanelConflictos(TextArea panelConflictos) {
		this.panelConflictos = panelConflictos;
	}

	public Button getBotonReservar() {
		return botonReservar;
	}

	public void setBotonReservar(Button botonReservar) {
		this.botonReservar = botonReservar;
	}

	public JComboBox getSelectorActividad() {
		return selectorActividad;
	}

	public void setSelectorActividad(JComboBox campoActividad) {
		this.selectorActividad = campoActividad;
	}

	public TextField getDetallesActividad() {
		return detallesActividad;
	}

	public void setDetallesActividad(TextField detallesActividad) {
		this.detallesActividad = detallesActividad;
	}

	public JComboBox getSelectorFechas() {
		return selectorFechas;
	}

	public void setSelectorFechas(JComboBox selectorFechas) {
		this.selectorFechas = selectorFechas;
	}

	public JTextArea getTxtrAvisoConflictos() {
		return txtrAvisoConflictos;
	}

	public void setTxtrAvisoConflictos(JTextArea txtrAvisoConflictos) {
		this.txtrAvisoConflictos = txtrAvisoConflictos;
	}

}
