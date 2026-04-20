package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class InscripcionNoSocioActividadView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JComboBox<String> comboBoxActividad;
	private JComboBox<String> comboBoxFecha;

	private JLabel lblInstalacionValor;
	private JLabel lblHorarioValor;
	private JLabel lblDuracionValor;
	private JLabel lblPlazasDisponiblesValor;
	private JLabel lblCosteValor;
	private JLabel lblPeriodoInscripcionValor;

	private JRadioButton rdbtnNoSocioExistente;
	private JRadioButton rdbtnNuevoNoSocio;

	private JComboBox<String> comboBoxNoSocioExistente;
	private JLabel lblNombreExistenteValor;

	private JTextField textFieldNombreNuevo;
	private JTextField textFieldDniNuevo;

	private JButton btnInscribir;
	private JButton btnCancelar;

	public InscripcionNoSocioActividadView() {
		setTitle("Inscripción en Actividades");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 980, 700);
		setMinimumSize(new Dimension(900, 650));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 15));

		JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		contentPane.add(panelTitulo, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("Inscripción en Actividades");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
		panelTitulo.add(lblTitulo);

		JPanel panelCentral = new JPanel();
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new GridLayout(1, 2, 20, 0));

		JPanel panelIzquierdo = new JPanel();
		panelIzquierdo.setLayout(null);
		panelIzquierdo.setBorder(BorderFactory.createEtchedBorder());
		panelCentral.add(panelIzquierdo);

		JLabel lblActividad = new JLabel("Actividad:");
		lblActividad.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblActividad.setBounds(25, 25, 100, 30);
		panelIzquierdo.add(lblActividad);

		comboBoxActividad = new JComboBox<>();
		comboBoxActividad.setModel(new DefaultComboBoxModel<>(new String[] {
				"Entrenamiento de Baloncesto"
		}));
		comboBoxActividad.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxActividad.setBounds(135, 25, 300, 32);
		panelIzquierdo.add(comboBoxActividad);

		JLabel lblInstalacion = new JLabel("Instalación:");
		lblInstalacion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInstalacion.setBounds(25, 85, 100, 30);
		panelIzquierdo.add(lblInstalacion);

		lblInstalacionValor = new JLabel("Cancha 1");
		lblInstalacionValor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInstalacionValor.setBounds(135, 85, 250, 30);
		panelIzquierdo.add(lblInstalacionValor);

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFecha.setBounds(25, 125, 100, 30);
		panelIzquierdo.add(lblFecha);

		comboBoxFecha = new JComboBox<>();
		comboBoxFecha.setModel(new DefaultComboBoxModel<>(new String[] {
				"03/09/2024"
		}));
		comboBoxFecha.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboBoxFecha.setBounds(135, 125, 150, 32);
		panelIzquierdo.add(comboBoxFecha);

		JLabel lblHorario = new JLabel("Horario:");
		lblHorario.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblHorario.setBounds(25, 165, 100, 30);
		panelIzquierdo.add(lblHorario);

		lblHorarioValor = new JLabel("18:00 - 20:00");
		lblHorarioValor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblHorarioValor.setBounds(135, 165, 250, 30);
		panelIzquierdo.add(lblHorarioValor);

		JLabel lblDuracion = new JLabel("Duración:");
		lblDuracion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDuracion.setBounds(25, 215, 100, 30);
		panelIzquierdo.add(lblDuracion);

		lblDuracionValor = new JLabel("120 min");
		lblDuracionValor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDuracionValor.setBounds(135, 215, 200, 30);
		panelIzquierdo.add(lblDuracionValor);

		JLabel lblPlazasDisponibles = new JLabel("Plazas disponibles:");
		lblPlazasDisponibles.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblPlazasDisponibles.setBounds(25, 265, 180, 35);
		panelIzquierdo.add(lblPlazasDisponibles);

		lblPlazasDisponiblesValor = new JLabel("5");
		lblPlazasDisponiblesValor.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblPlazasDisponiblesValor.setBounds(215, 265, 60, 35);
		panelIzquierdo.add(lblPlazasDisponiblesValor);

		JLabel lblCoste = new JLabel("Coste:");
		lblCoste.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCoste.setBounds(25, 315, 100, 30);
		panelIzquierdo.add(lblCoste);

		lblCosteValor = new JLabel("20");
		lblCosteValor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCosteValor.setBounds(135, 315, 100, 30);
		panelIzquierdo.add(lblCosteValor);

		JLabel lblPeriodoInscripcion = new JLabel("Periodo de inscripción:");
		lblPeriodoInscripcion.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblPeriodoInscripcion.setBounds(25, 375, 240, 35);
		panelIzquierdo.add(lblPeriodoInscripcion);

		lblPeriodoInscripcionValor = new JLabel("17/04/2026 - 25/04/2026");
		lblPeriodoInscripcionValor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPeriodoInscripcionValor.setBounds(25, 415, 300, 30);
		panelIzquierdo.add(lblPeriodoInscripcionValor);

		JLabel lblAvisoPago = new JLabel("Se generará un recibo del pago");
		lblAvisoPago.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblAvisoPago.setBounds(25, 520, 280, 30);
		panelIzquierdo.add(lblAvisoPago);

		JPanel panelDerecho = new JPanel();
		panelDerecho.setLayout(null);
		panelDerecho.setBorder(BorderFactory.createEtchedBorder());
		panelCentral.add(panelDerecho);

		rdbtnNoSocioExistente = new JRadioButton("No Socio existente");
		rdbtnNoSocioExistente.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rdbtnNoSocioExistente.setBounds(25, 30, 170, 30);
		panelDerecho.add(rdbtnNoSocioExistente);

		rdbtnNuevoNoSocio = new JRadioButton("Nuevo");
		rdbtnNuevoNoSocio.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rdbtnNuevoNoSocio.setBounds(220, 30, 100, 30);
		panelDerecho.add(rdbtnNuevoNoSocio);

		ButtonGroup grupoTipoNoSocio = new ButtonGroup();
		grupoTipoNoSocio.add(rdbtnNoSocioExistente);
		grupoTipoNoSocio.add(rdbtnNuevoNoSocio);
		rdbtnNoSocioExistente.setSelected(true);

		JPanel panelExistente = new JPanel();
		panelExistente.setBorder(new TitledBorder(null, "No socio existente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelExistente.setBounds(25, 80, 390, 150);
		panelExistente.setLayout(null);
		panelDerecho.add(panelExistente);

		JLabel lblNoSocioExistente = new JLabel("No Socio:");
		lblNoSocioExistente.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNoSocioExistente.setBounds(15, 30, 90, 25);
		panelExistente.add(lblNoSocioExistente);

		comboBoxNoSocioExistente = new JComboBox<>();
		comboBoxNoSocioExistente.setModel(new DefaultComboBoxModel<>(new String[] {
				"44444444D"
		}));
		comboBoxNoSocioExistente.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBoxNoSocioExistente.setBounds(105, 28, 180, 28);
		panelExistente.add(comboBoxNoSocioExistente);

		JLabel lblNombreExistente = new JLabel("Nombre:");
		lblNombreExistente.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNombreExistente.setBounds(15, 80, 90, 25);
		panelExistente.add(lblNombreExistente);

		lblNombreExistenteValor = new JLabel("Carlos García");
		lblNombreExistenteValor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNombreExistenteValor.setBounds(105, 80, 220, 25);
		panelExistente.add(lblNombreExistenteValor);

		JPanel panelNuevo = new JPanel();
		panelNuevo.setBorder(new TitledBorder(null, "Nuevo no socio", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelNuevo.setBounds(25, 255, 390, 180);
		panelNuevo.setLayout(null);
		panelDerecho.add(panelNuevo);

		JLabel lblNombreNuevo = new JLabel("Nombre:");
		lblNombreNuevo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNombreNuevo.setBounds(15, 45, 90, 25);
		panelNuevo.add(lblNombreNuevo);

		textFieldNombreNuevo = new JTextField();
		textFieldNombreNuevo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldNombreNuevo.setBounds(105, 43, 180, 28);
		panelNuevo.add(textFieldNombreNuevo);
		textFieldNombreNuevo.setColumns(10);

		JLabel lblDniNuevo = new JLabel("DNI:");
		lblDniNuevo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDniNuevo.setBounds(15, 95, 90, 25);
		panelNuevo.add(lblDniNuevo);

		textFieldDniNuevo = new JTextField();
		textFieldDniNuevo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFieldDniNuevo.setBounds(105, 93, 180, 28);
		panelNuevo.add(textFieldDniNuevo);
		textFieldDniNuevo.setColumns(10);

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		contentPane.add(panelBotones, BorderLayout.SOUTH);

		btnInscribir = new JButton("Inscribir");
		btnInscribir.setPreferredSize(new Dimension(140, 40));
		btnInscribir.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelBotones.add(btnInscribir);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setPreferredSize(new Dimension(140, 40));
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelBotones.add(btnCancelar);

		actualizarModoNoSocio();
		initView();
	}

	private void initView() {
		rdbtnNoSocioExistente.addActionListener(e -> actualizarModoNoSocio());
		rdbtnNuevoNoSocio.addActionListener(e -> actualizarModoNoSocio());
	}

	private void actualizarModoNoSocio() {
		boolean existente = rdbtnNoSocioExistente.isSelected();

		comboBoxNoSocioExistente.setEnabled(existente);

		textFieldNombreNuevo.setEnabled(!existente);
		textFieldDniNuevo.setEnabled(!existente);

		lblNombreExistenteValor.setEnabled(existente);
	}

	public void close() {
		dispose();
	}

	// ===== Getters para el Controller =====

	public JComboBox<String> getComboBoxActividad() {
		return comboBoxActividad;
	}

	public JComboBox<String> getComboBoxFecha() {
		return comboBoxFecha;
	}

	public JLabel getLblInstalacionValor() {
		return lblInstalacionValor;
	}

	public JLabel getLblHorarioValor() {
		return lblHorarioValor;
	}

	public JLabel getLblDuracionValor() {
		return lblDuracionValor;
	}

	public JLabel getLblPlazasDisponiblesValor() {
		return lblPlazasDisponiblesValor;
	}

	public JLabel getLblCosteValor() {
		return lblCosteValor;
	}

	public JLabel getLblPeriodoInscripcionValor() {
		return lblPeriodoInscripcionValor;
	}

	public JRadioButton getRdbtnNoSocioExistente() {
		return rdbtnNoSocioExistente;
	}

	public JRadioButton getRdbtnNuevoNoSocio() {
		return rdbtnNuevoNoSocio;
	}

	public JComboBox<String> getComboBoxNoSocioExistente() {
		return comboBoxNoSocioExistente;
	}

	public JLabel getLblNombreExistenteValor() {
		return lblNombreExistenteValor;
	}

	public JTextField getTextFieldNombreNuevo() {
		return textFieldNombreNuevo;
	}

	public JTextField getTextFieldDniNuevo() {
		return textFieldDniNuevo;
	}

	public JButton getBtnInscribir() {
		return btnInscribir;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	// ===== Helpers para el controller =====

	public boolean isModoNoSocioExistente() {
		return rdbtnNoSocioExistente.isSelected();
	}

	public boolean isModoNuevoNoSocio() {
		return rdbtnNuevoNoSocio.isSelected();
	}

	public void setNombreExistente(String nombre) {
		lblNombreExistenteValor.setText(nombre != null ? nombre : "");
	}

	public void setDatosActividad(String instalacion, String horario, String duracion,
			String plazasDisponibles, String coste, String periodoInscripcion) {
		lblInstalacionValor.setText(instalacion != null ? instalacion : "");
		lblHorarioValor.setText(horario != null ? horario : "");
		lblDuracionValor.setText(duracion != null ? duracion : "");
		lblPlazasDisponiblesValor.setText(plazasDisponibles != null ? plazasDisponibles : "");
		lblCosteValor.setText(coste != null ? coste : "");
		lblPeriodoInscripcionValor.setText(periodoInscripcion != null ? periodoInscripcion : "");
	}

	public void limpiarNuevoNoSocio() {
		textFieldNombreNuevo.setText("");
		textFieldDniNuevo.setText("");
	}

	public void setTituloCentradoBotones() {
		btnInscribir.setHorizontalAlignment(SwingConstants.CENTER);
		btnCancelar.setHorizontalAlignment(SwingConstants.CENTER);
	}
}