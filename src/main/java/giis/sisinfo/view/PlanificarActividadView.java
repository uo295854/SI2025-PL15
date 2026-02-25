package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class PlanificarActividadView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextField textFieldActividad;
	private JTextField textHoraDe;
	private JTextField textHoraA;

	private JSpinner spinnerAforo;
	private JSpinner spinnerDuracion;

	private JComboBox<String> comboBoxInstalacion;
	private JComboBox<String> comboBoxTipoActividad;

	// Combo multiselección (controller manejará el modelo y renderer)
	private JComboBox<Object> comboBoxDiasMulti;

	private JComboBox<String> comboBoxPeriodoInscripcion;

	private JSpinner spinnerCuotaSocios;
	private JSpinner spinnerCuotaNoSocios;

	private JTextArea textAreaDescripcion;

	private JLabel lblHorario;

	private JButton btnCancelar;
	private JButton btnCrearActividad;

	public PlanificarActividadView() {
		setTitle("Planificar Actividad");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 650);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// NORTH
		JPanel panelTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		contentPane.add(panelTitle, BorderLayout.NORTH);

		JLabel lblPlanificarActividad = new JLabel("Planificar Actividad");
		lblPlanificarActividad.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelTitle.add(lblPlanificarActividad);

		// CENTER (2 columnas)
		JPanel panelCenter = new JPanel(new GridLayout(1, 2, 15, 0));
		contentPane.add(panelCenter, BorderLayout.CENTER);

		// IZQ
		JPanel panelIzq = new JPanel();
		panelCenter.add(panelIzq);
		panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));

		JPanel pActividad = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pActividad);
		pActividad.add(new JLabel("Actividad:"));
		textFieldActividad = new JTextField();
		textFieldActividad.setColumns(18);
		pActividad.add(textFieldActividad);

		JPanel pInstalacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pInstalacion);
		pInstalacion.add(new JLabel("Instalación:"));
		comboBoxInstalacion = new JComboBox<>(new String[] { "Cancha 1", "Cancha 2", "Pista 1", "Pabellón" });
		pInstalacion.add(comboBoxInstalacion);

		JPanel pTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pTipo);
		pTipo.add(new JLabel("Tipo de Actividad:"));
		comboBoxTipoActividad = new JComboBox<>(new String[] { "Deportiva", "Cultural", "Formativa" });
		pTipo.add(comboBoxTipoActividad);

		JPanel pAforo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pAforo);
		pAforo.add(new JLabel("Aforo:"));
		spinnerAforo = new JSpinner(new SpinnerNumberModel(20, 1, 999, 1));
		pAforo.add(spinnerAforo);

		JPanel pDia = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pDia);
		pDia.add(new JLabel("Días:"));

		// Controller le pondrá modelo + renderer
		comboBoxDiasMulti = new JComboBox<>();
		((DefaultComboBoxModel<Object>) comboBoxDiasMulti.getModel()).addElement("(Selecciona días)");
		pDia.add(comboBoxDiasMulti);

		JPanel pHorario = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pHorario);
		lblHorario = new JLabel("Horario:");
		pHorario.add(lblHorario);
		pHorario.add(new JLabel("de"));
		textHoraDe = new JTextField();
		textHoraDe.setColumns(5);
		pHorario.add(textHoraDe);
		pHorario.add(new JLabel("a"));
		textHoraA = new JTextField();
		textHoraA.setColumns(5);
		pHorario.add(textHoraA);

		JPanel pDuracion = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pDuracion);
		pDuracion.add(new JLabel("Duración:"));
		spinnerDuracion = new JSpinner(new SpinnerNumberModel(120, 1, 10000, 5));
		pDuracion.add(spinnerDuracion);
		pDuracion.add(new JLabel("minutos"));

		JPanel pPeriodo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pPeriodo);
		pPeriodo.add(new JLabel("Periodo inscripción:"));
		comboBoxPeriodoInscripcion = new JComboBox<>(new String[] { "Periodo 1", "Periodo 2", "Periodo 3" });
		pPeriodo.add(comboBoxPeriodoInscripcion);

		JPanel pFechasPeriodo1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panelIzq.add(pFechasPeriodo1);
		pFechasPeriodo1.add(new JLabel("Fecha inicio socios: 01/09"));

		JPanel pFechasPeriodo2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panelIzq.add(pFechasPeriodo2);
		pFechasPeriodo2.add(new JLabel("Fecha fin socios: 05/09"));

		JPanel pFechasPeriodo3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panelIzq.add(pFechasPeriodo3);
		pFechasPeriodo3.add(new JLabel("Fecha fin no socios: 08/09"));

		JPanel pFechaIni = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pFechaIni);
		pFechaIni.add(new JLabel("Fecha inicio:"));
		JTextField textFechaInicio = new JTextField();
		textFechaInicio.setColumns(10);
		pFechaIni.add(textFechaInicio);

		JPanel pFechaFin = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelIzq.add(pFechaFin);
		pFechaFin.add(new JLabel("Fecha fin:"));
		JTextField textFechaFin = new JTextField();
		textFechaFin.setColumns(10);
		pFechaFin.add(textFechaFin);

		// DER
		JPanel panelDer = new JPanel();
		panelCenter.add(panelDer);
		panelDer.setLayout(new BoxLayout(panelDer, BoxLayout.Y_AXIS));

		JPanel pCuotaSocios = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelDer.add(pCuotaSocios);
		pCuotaSocios.add(new JLabel("Cuota mensual socios:"));
		spinnerCuotaSocios = new JSpinner(new SpinnerNumberModel(20, 0, 9999, 1));
		pCuotaSocios.add(spinnerCuotaSocios);
		pCuotaSocios.add(new JLabel("€"));

		JPanel pCuotaNoSocios = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelDer.add(pCuotaNoSocios);
		pCuotaNoSocios.add(new JLabel("Cuota mensual no socios:"));
		spinnerCuotaNoSocios = new JSpinner(new SpinnerNumberModel(40, 0, 9999, 1));
		pCuotaNoSocios.add(spinnerCuotaNoSocios);
		pCuotaNoSocios.add(new JLabel("€"));

		JPanel pDescLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelDer.add(pDescLabel);
		pDescLabel.add(new JLabel("Descripción:"));

		textAreaDescripcion = new JTextArea(14, 28);
		textAreaDescripcion.setLineWrap(true);
		textAreaDescripcion.setWrapStyleWord(true);
		panelDer.add(new JScrollPane(textAreaDescripcion));

		// SOUTH
		JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		contentPane.add(panelBottom, BorderLayout.SOUTH);

		btnCancelar = new JButton("Cancelar");
		panelBottom.add(btnCancelar);

		btnCrearActividad = new JButton("Crear Actividad");
		panelBottom.add(btnCrearActividad);
	}

	// ===== Getters para el Controller =====
	public JTextField getTextFieldActividad() { return textFieldActividad; }
	public JTextField getTextHoraDe() { return textHoraDe; }
	public JTextField getTextHoraA() { return textHoraA; }

	public JSpinner getSpinnerAforo() { return spinnerAforo; }
	public JSpinner getSpinnerDuracion() { return spinnerDuracion; }

	public JComboBox<String> getComboBoxInstalacion() { return comboBoxInstalacion; }
	public JComboBox<String> getComboBoxTipoActividad() { return comboBoxTipoActividad; }

	public JComboBox<Object> getComboBoxDiasMulti() { return comboBoxDiasMulti; }

	public JComboBox<String> getComboBoxPeriodoInscripcion() { return comboBoxPeriodoInscripcion; }

	public JSpinner getSpinnerCuotaSocios() { return spinnerCuotaSocios; }
	public JSpinner getSpinnerCuotaNoSocios() { return spinnerCuotaNoSocios; }

	public JTextArea getTextAreaDescripcion() { return textAreaDescripcion; }

	public JLabel getLblHorario() { return lblHorario; }

	public JButton getBtnCancelar() { return btnCancelar; }
	public JButton getBtnCrearActividad() { return btnCrearActividad; }

	// Helpers de UI
	public void close() { dispose(); }
}