package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InscripcionActividadView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JComboBox<String> cbActividad;
	private JComboBox<String> cbFecha;

	private JLabel lblInstalacionValor;
	private JLabel lblHorarioValor;
	private JLabel lblPlazasValor;
	private JLabel lblPeriodoValor;
	private JLabel lblPagoInfo;

	private JButton btnInscribirse;
	private JButton btnCancelar;

	public InscripcionActividadView() {
		setTitle("Inscripción en Actividades");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 650);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		contentPane.add(panelTitulo, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("Inscripción en Actividades");
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panelTitulo.add(lblTitulo);

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(null);
		contentPane.add(panelCentral, BorderLayout.CENTER);

		JLabel lblActividad = new JLabel("Actividad:");
		lblActividad.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblActividad.setBounds(80, 40, 140, 35);
		panelCentral.add(lblActividad);

		cbActividad = new JComboBox<String>();
		cbActividad.setFont(new Font("Tahoma", Font.PLAIN, 18));
		cbActividad.setBounds(230, 40, 470, 40);
		panelCentral.add(cbActividad);

		JLabel lblInstalacion = new JLabel("Instalación:");
		lblInstalacion.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblInstalacion.setBounds(80, 120, 140, 35);
		panelCentral.add(lblInstalacion);

		lblInstalacionValor = new JLabel("");
		lblInstalacionValor.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblInstalacionValor.setBounds(230, 120, 300, 35);
		panelCentral.add(lblInstalacionValor);

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFecha.setBounds(80, 170, 140, 35);
		panelCentral.add(lblFecha);

		cbFecha = new JComboBox<String>();
		cbFecha.setFont(new Font("Tahoma", Font.PLAIN, 18));
		cbFecha.setBounds(170, 170, 220, 40);
		panelCentral.add(cbFecha);

		JLabel lblHorario = new JLabel("Horario:");
		lblHorario.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblHorario.setBounds(80, 220, 140, 35);
		panelCentral.add(lblHorario);

		lblHorarioValor = new JLabel("");
		lblHorarioValor.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblHorarioValor.setBounds(210, 220, 300, 35);
		panelCentral.add(lblHorarioValor);

		JLabel lblPlazas = new JLabel("Plazas disponibles:");
		lblPlazas.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlazas.setBounds(80, 270, 220, 35);
		panelCentral.add(lblPlazas);

		lblPlazasValor = new JLabel("");
		lblPlazasValor.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPlazasValor.setBounds(300, 270, 100, 35);
		panelCentral.add(lblPlazasValor);

		JLabel lblPeriodo = new JLabel("Periodo de inscripción:");
		lblPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPeriodo.setBounds(80, 360, 260, 35);
		panelCentral.add(lblPeriodo);

		lblPeriodoValor = new JLabel("");
		lblPeriodoValor.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPeriodoValor.setBounds(340, 360, 200, 35);
		panelCentral.add(lblPeriodoValor);

		lblPagoInfo = new JLabel("El pago se añadirá al recibo mensual del socio");
		lblPagoInfo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPagoInfo.setBounds(80, 470, 500, 35);
		panelCentral.add(lblPagoInfo);

		btnInscribirse = new JButton("Inscribirse");
		btnInscribirse.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnInscribirse.setBounds(510, 520, 160, 45);
		panelCentral.add(btnInscribirse);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnCancelar.setBounds(700, 520, 150, 45);
		panelCentral.add(btnCancelar);
	}

	public JComboBox<String> getCbActividad() {
		return cbActividad;
	}

	public JComboBox<String> getCbFecha() {
		return cbFecha;
	}

	public JLabel getLblInstalacionValor() {
		return lblInstalacionValor;
	}

	public JLabel getLblHorarioValor() {
		return lblHorarioValor;
	}

	public JLabel getLblPlazasValor() {
		return lblPlazasValor;
	}

	public JLabel getLblPeriodoValor() {
		return lblPeriodoValor;
	}

	public JLabel getLblPagoInfo() {
		return lblPagoInfo;
	}

	public JButton getBtnInscribirse() {
		return btnInscribirse;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	public void setInstalacion(String instalacion) {
		lblInstalacionValor.setText(instalacion);
	}

	public void setHorario(String horario) {
		lblHorarioValor.setText(horario);
	}

	public void setPlazasDisponibles(String plazas) {
		lblPlazasValor.setText(plazas);
	}

	public void setPeriodoInscripcion(String periodo) {
		lblPeriodoValor.setText(periodo);
	}

	public void limpiarDatosActividad() {
		lblInstalacionValor.setText("");
		lblHorarioValor.setText("");
		lblPlazasValor.setText("");
		lblPeriodoValor.setText("");
	}

	public void close() {
		dispose();
	}
}