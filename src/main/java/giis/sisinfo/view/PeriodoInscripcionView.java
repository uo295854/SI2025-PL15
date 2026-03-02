package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class PeriodoInscripcionView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JTextField txtNombre;
	private JComboBox<String> cmbPeriodoOficial;

	private JTextArea txtDescripcion;

	private JTextField txtFechaInicioSocios;
	private JTextField txtFechaFinSocios;

	private JTextField txtFechaFinNoSocios;

	private JButton btnConfirmar;
	private JButton btnCancelar;

	public PeriodoInscripcionView() {
		setTitle("Periodo de inscripción");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 520);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 10));

		// ===== NORTH: título =====
		JPanel panelTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		JLabel lblTitle = new JLabel("Periodo de inscripción");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
		panelTitle.add(lblTitle);
		contentPane.add(panelTitle, BorderLayout.NORTH);

		// ===== CENTER: contenido (top + bottom) =====
		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new BorderLayout(0, 10));
		contentPane.add(panelCenter, BorderLayout.CENTER);

		// --- Top: (Nombre/Periodo) + Descripción ---
		JPanel panelTop = new JPanel(new GridLayout(1, 2, 20, 0));
		panelCenter.add(panelTop, BorderLayout.NORTH);

		// Izquierda: Nombre + Periodo
		JPanel panelTopLeft = new JPanel();
		panelTopLeft.setLayout(new GridLayout(2, 1, 0, 10));
		panelTop.add(panelTopLeft);

		JPanel pNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		pNombre.add(new JLabel("Nombre:"));
		txtNombre = new JTextField();
		txtNombre.setPreferredSize(new Dimension(260, 26));
		pNombre.add(txtNombre);
		panelTopLeft.add(pNombre);

		JPanel pPeriodo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		pPeriodo.add(new JLabel("Periodo de inscripción:"));
		cmbPeriodoOficial = new JComboBox<>(new String[] { "Septiembre", "Enero", "Junio" });
		cmbPeriodoOficial.setPreferredSize(new Dimension(180, 26));
		pPeriodo.add(cmbPeriodoOficial);
		panelTopLeft.add(pPeriodo);

		// Derecha: Descripción
		JPanel panelTopRight = new JPanel(new BorderLayout(0, 5));
		panelTop.add(panelTopRight);

		JPanel pDescLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		pDescLabel.add(new JLabel("Descripción:"));
		panelTopRight.add(pDescLabel, BorderLayout.NORTH);

		txtDescripcion = new JTextArea(6, 35);
		txtDescripcion.setLineWrap(true);
		txtDescripcion.setWrapStyleWord(true);
		panelTopRight.add(new JScrollPane(txtDescripcion), BorderLayout.CENTER);

		// --- Bottom: Socios / No socios ---
		JPanel panelBottomCenter = new JPanel(new GridLayout(1, 2, 30, 0));
		panelCenter.add(panelBottomCenter, BorderLayout.CENTER);

		// Socios
		JPanel panelSocios = new JPanel();
		panelSocios.setBorder(new TitledBorder("Socios"));
		panelSocios.setLayout(new GridLayout(2, 1, 0, 10));
		panelBottomCenter.add(panelSocios);

		JPanel pIniSocios = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
		pIniSocios.add(new JLabel("Fecha de inicio:"));
		txtFechaInicioSocios = new JTextField();
		txtFechaInicioSocios.setPreferredSize(new Dimension(140, 26));
		pIniSocios.add(txtFechaInicioSocios);
		panelSocios.add(pIniSocios);

		JPanel pFinSocios = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
		pFinSocios.add(new JLabel("Fecha fin:"));
		txtFechaFinSocios = new JTextField();
		txtFechaFinSocios.setPreferredSize(new Dimension(140, 26));
		pFinSocios.add(txtFechaFinSocios);
		panelSocios.add(pFinSocios);

		// No socios
		JPanel panelNoSocios = new JPanel();
		panelNoSocios.setBorder(new TitledBorder("No socios"));
		panelNoSocios.setLayout(new GridLayout(2, 1, 0, 10));
		panelBottomCenter.add(panelNoSocios);

		// Para parecerse al wireframe: dejamos “aire” arriba y luego la fecha fin
		JPanel pSpacer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
		pSpacer.add(new JLabel("")); // espaciador visual
		panelNoSocios.add(pSpacer);

		JPanel pFinNoSocios = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
		pFinNoSocios.add(new JLabel("Fecha fin:"));
		txtFechaFinNoSocios = new JTextField();
		txtFechaFinNoSocios.setPreferredSize(new Dimension(140, 26));
		pFinNoSocios.add(txtFechaFinNoSocios);
		panelNoSocios.add(pFinNoSocios);

		// ===== SOUTH: botones =====
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
		contentPane.add(panelButtons, BorderLayout.SOUTH);

		btnConfirmar = new JButton("Confirmar");
		btnCancelar = new JButton("Cancelar");

		panelButtons.add(btnConfirmar);
		panelButtons.add(btnCancelar);
	}

	// ===== Getters para el Controller =====
	public JTextField getTxtNombre() { return txtNombre; }
	public JComboBox<String> getCmbPeriodoOficial() { return cmbPeriodoOficial; }

	public JTextArea getTxtDescripcion() { return txtDescripcion; }

	public JTextField getTxtFechaInicioSocios() { return txtFechaInicioSocios; }
	public JTextField getTxtFechaFinSocios() { return txtFechaFinSocios; }
	public JTextField getTxtFechaFinNoSocios() { return txtFechaFinNoSocios; }

	public JButton getBtnConfirmar() { return btnConfirmar; }
	public JButton getBtnCancelar() { return btnCancelar; }

	// Helper
	public void close() { dispose(); }
}