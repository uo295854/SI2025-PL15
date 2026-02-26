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
import javax.swing.SpinnerModel;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ReservaInstalacionAdminView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(133, 10, 198, 20);
		contentPane.add(comboBox);
		
		JTextPane txtpnMotivoDeReserva = new JTextPane();
		txtpnMotivoDeReserva.setEditable(false);
		txtpnMotivoDeReserva.setText("Actividad:");
		txtpnMotivoDeReserva.setBackground(UIManager.getColor("Button.background"));
		txtpnMotivoDeReserva.setBounds(58, 43, 65, 18);
		contentPane.add(txtpnMotivoDeReserva);
		
		TextField textField = new TextField();
		textField.setBounds(133, 41, 198, 20);
		contentPane.add(textField);
		
		JTextPane txtpnFechaYHora = new JTextPane();
		txtpnFechaYHora.setEditable(false);
		txtpnFechaYHora.setText("Fecha y hora inicial:");
		txtpnFechaYHora.setBackground(UIManager.getColor("Button.background"));
		txtpnFechaYHora.setBounds(10, 71, 113, 18);
		contentPane.add(txtpnFechaYHora);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(133, 71, 124, 18);
		contentPane.add(dateChooser);
		
		JTextPane txtpnFechaYHora_2 = new JTextPane();
		txtpnFechaYHora_2.setEditable(false);
		txtpnFechaYHora_2.setText("Fecha y hora final:");
		txtpnFechaYHora_2.setBackground(UIManager.getColor("Button.background"));
		txtpnFechaYHora_2.setBounds(10, 99, 113, 18);
		contentPane.add(txtpnFechaYHora_2);
		
		JDateChooser dateChooser_1 = new JDateChooser();
		dateChooser_1.setBounds(133, 99, 124, 18);
		contentPane.add(dateChooser_1);
		
		// Time spinner 1
		SpinnerDateModel timeModel = new SpinnerDateModel();
		JSpinner timeSpinner1 = new JSpinner(timeModel);
		JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(timeSpinner1, "HH:mm:ss");
		timeSpinner1.setEditor(timeEditor1);
		timeSpinner1.setBounds(259, 99, 72, 18);
		contentPane.add(timeSpinner1);
		
		// Time spinner 2
		JSpinner timeSpinner2 = new JSpinner(timeModel);
		JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(timeSpinner2, "HH:mm:ss");
		timeSpinner2.setEditor(timeEditor2);
		timeSpinner2.setBounds(259, 71, 72, 18);
		contentPane.add(timeSpinner2);
		
		JTextPane txtpnDetalles = new JTextPane();
		txtpnDetalles.setText("Detalles:");
		txtpnDetalles.setEditable(false);
		txtpnDetalles.setBackground(UIManager.getColor("Button.background"));
		txtpnDetalles.setBounds(10, 127, 86, 18);
		contentPane.add(txtpnDetalles);
		
		TextField textField_1 = new TextField();
		textField_1.setBounds(10, 151, 546, 94);
		contentPane.add(textField_1);
		
		TextField textField_1_1 = new TextField();
		textField_1_1.setEditable(false);
		textField_1_1.setBounds(10, 275, 546, 94);
		contentPane.add(textField_1_1);
		
		JTextPane txtpnConflictos = new JTextPane();
		txtpnConflictos.setText("Conflictos:");
		txtpnConflictos.setEditable(false);
		txtpnConflictos.setBackground(UIManager.getColor("Button.background"));
		txtpnConflictos.setBounds(10, 251, 86, 18);
		contentPane.add(txtpnConflictos);
		
		Button button = new Button("Reservar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setBounds(401, 43, 86, 35);
		contentPane.add(button);

	}
}
