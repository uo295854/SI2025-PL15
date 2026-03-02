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

public class ReservaInstalacionAdminView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JComboBox selectorInstalaciones;
	JSpinner selectorHoraFinal;
	JSpinner selectorHoraInicial;
	JDateChooser selectorFechaFinal;
	JDateChooser selectorFechaInicial;
	TextArea panelConflictos;
	Button botonReservar;
	JComboBox campoActividad;
	TextField detallesActividad;


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
		setBounds(100, 100, 580, 412);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextPane txtpnInstalacinAReservar = new JTextPane();
		txtpnInstalacinAReservar.setEditable(false);
		txtpnInstalacinAReservar.setText("Instalación a reservar:");
		txtpnInstalacinAReservar.setBackground(new Color(240, 240, 240));
		txtpnInstalacinAReservar.setBounds(10, 10, 124, 18);
		contentPane.add(txtpnInstalacinAReservar);
		
		selectorInstalaciones = new JComboBox();
		selectorInstalaciones.setBounds(133, 10, 198, 20);
		contentPane.add(selectorInstalaciones);
		
		JTextPane txtpnMotivoDeReserva = new JTextPane();
		txtpnMotivoDeReserva.setEditable(false);
		txtpnMotivoDeReserva.setText("Actividad:");
		txtpnMotivoDeReserva.setBackground(UIManager.getColor("Button.background"));
		txtpnMotivoDeReserva.setBounds(58, 43, 65, 18);
		contentPane.add(txtpnMotivoDeReserva);
		
		campoActividad = new JComboBox();
		campoActividad.setBounds(133, 41, 198, 20);
		contentPane.add(campoActividad);
		
		JTextPane txtpnFechaYHora = new JTextPane();
		txtpnFechaYHora.setEditable(false);
		txtpnFechaYHora.setText("Fecha y hora inicial:");
		txtpnFechaYHora.setBackground(UIManager.getColor("Button.background"));
		txtpnFechaYHora.setBounds(10, 71, 113, 18);
		contentPane.add(txtpnFechaYHora);
		
		selectorFechaInicial = new JDateChooser();
		selectorFechaInicial.setBounds(133, 71, 124, 18);
		contentPane.add(selectorFechaInicial);
		
		JTextPane txtpnFechaYHora_2 = new JTextPane();
		txtpnFechaYHora_2.setEditable(false);
		txtpnFechaYHora_2.setText("Fecha y hora final:");
		txtpnFechaYHora_2.setBackground(UIManager.getColor("Button.background"));
		txtpnFechaYHora_2.setBounds(10, 99, 113, 18);
		contentPane.add(txtpnFechaYHora_2);
		
		selectorFechaFinal = new JDateChooser();
		selectorFechaFinal.setBounds(133, 99, 124, 18);
		contentPane.add(selectorFechaFinal);
		
		// Selector hora final
		SpinnerDateModel timeModelFinal;
		selectorHoraFinal = new JSpinner(new SpinnerDateModel(new Date(1772459757224L), null, null, Calendar.MINUTE));
		JSpinner.DateEditor de_selectorHoraFinal = new JSpinner.DateEditor(selectorHoraFinal, "HH:mm");
		selectorHoraFinal.setEditor(de_selectorHoraFinal);
		selectorHoraFinal.setBounds(259, 99, 72, 18);
		contentPane.add(selectorHoraFinal);
		
		// Selector hora inicial
		SpinnerDateModel timeModelInicial; 
		selectorHoraInicial = new JSpinner(new SpinnerDateModel(new Date(1772459731163L), null, null, Calendar.MINUTE));
		JSpinner.DateEditor de_selectorHoraInicial = new JSpinner.DateEditor(selectorHoraInicial, "HH:mm");
		selectorHoraInicial.setEditor(de_selectorHoraInicial);
		selectorHoraInicial.setBounds(259, 71, 72, 18);
		contentPane.add(selectorHoraInicial);
		
		JTextPane txtpnDetalles = new JTextPane();
		txtpnDetalles.setText("Detalles:");
		txtpnDetalles.setEditable(false);
		txtpnDetalles.setBackground(UIManager.getColor("Button.background"));
		txtpnDetalles.setBounds(10, 127, 86, 18);
		contentPane.add(txtpnDetalles);
		
		detallesActividad = new TextField();
		detallesActividad.setBounds(10, 151, 546, 94);
		contentPane.add(detallesActividad);
		
		panelConflictos = new TextArea();
		panelConflictos.setEditable(false);
		panelConflictos.setBounds(10, 275, 546, 94);
		contentPane.add(panelConflictos);
		
		JTextPane txtpnConflictos = new JTextPane();
		txtpnConflictos.setText("Conflictos:");
		txtpnConflictos.setEditable(false);
		txtpnConflictos.setBackground(UIManager.getColor("Button.background"));
		txtpnConflictos.setBounds(10, 251, 86, 18);
		contentPane.add(txtpnConflictos);
		
		botonReservar = new Button("Reservar");
		botonReservar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		botonReservar.setBounds(401, 43, 86, 35);
		contentPane.add(botonReservar);

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

	public JDateChooser getSelectorFechaFinal() {
		return selectorFechaFinal;
	}

	public void setSelectorFechaFinal(JDateChooser selectorFechaFinal) {
		this.selectorFechaFinal = selectorFechaFinal;
	}

	public JDateChooser getSelectorFechaInicial() {
		return selectorFechaInicial;
	}

	public void setSelectorFechaInicial(JDateChooser selectorFechaInicial) {
		this.selectorFechaInicial = selectorFechaInicial;
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

	public JComboBox getCampoActividad() {
		return campoActividad;
	}

	public void setCampoActividad(JComboBox campoActividad) {
		this.campoActividad = campoActividad;
	}

	public TextField getDetallesActividad() {
		return detallesActividad;
	}

	public void setDetallesActividad(TextField detallesActividad) {
		this.detallesActividad = detallesActividad;
	}
	
	
	
	
}
