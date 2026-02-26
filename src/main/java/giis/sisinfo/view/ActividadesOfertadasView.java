package giis.sisinfo.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.TextField;
import java.awt.Color;
import javax.swing.UIManager;
import java.awt.ScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Point;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ActividadesOfertadasView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ActividadesOfertadasView frame = new ActividadesOfertadasView();
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
	public ActividadesOfertadasView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 860, 686);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton searchButton = new JButton("Buscar");
		searchButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		searchButton.setBounds(340, 13, 124, 38);
		contentPane.add(searchButton);
		
		JComboBox periodSelector = new JComboBox();
		periodSelector.setModel(new DefaultComboBoxModel(new String[] {"Enero", "Junio", "Septiembre"}));
		periodSelector.setBounds(79, 23, 78, 22);
		contentPane.add(periodSelector);
		
		JComboBox yearSelector = new JComboBox();
		yearSelector.setModel(new DefaultComboBoxModel(new String[] {"2025", "2026", "2027", "2028", "2029", "2030", "2031"}));
		yearSelector.setBounds(228, 23, 78, 22);
		contentPane.add(yearSelector);
		
		JTextPane txtpnPeriodo = new JTextPane();
		txtpnPeriodo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtpnPeriodo.setBackground(new Color(240, 240, 240));
		txtpnPeriodo.setEditable(false);
		txtpnPeriodo.setText("Periodo:");
		txtpnPeriodo.setBounds(21, 23, 51, 22);
		contentPane.add(txtpnPeriodo);
		
		JTextPane txtpnAo = new JTextPane();
		txtpnAo.setText("Año:");
		txtpnAo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtpnAo.setEditable(false);
		txtpnAo.setBackground(UIManager.getColor("Button.background"));
		txtpnAo.setBounds(193, 23, 33, 22);
		contentPane.add(txtpnAo);
		
		JButton closeButton = new JButton("Cerrar");
		closeButton.addActionListener(e -> dispose());
		closeButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		closeButton.setBounds(506, 13, 124, 38);
		contentPane.add(closeButton);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 62, 822, 306);
		contentPane.add(scrollPane_1);
		
		
		//Detalles de la actividad
		JTextPane detallesActividad = new JTextPane();
		detallesActividad.setText("Texto de ejemplo");
		detallesActividad.setBounds(10, 410, 822, 214);
		contentPane.add(detallesActividad);
		
		//Texto que dice "Detalles de la actividad" NO SE MODIFICA
		JTextPane detallesActividadNoModificable = new JTextPane();
		detallesActividadNoModificable.setFont(new Font("Tahoma", Font.PLAIN, 16));
		detallesActividadNoModificable.setText("Detalles de la Actividad");
		detallesActividadNoModificable.setBounds(10, 379, 822, 27);
		contentPane.add(detallesActividadNoModificable);

		//Tabla
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = table.getSelectedRow();
				System.out.println("Click en fila: "+fila);
				detallesActividad.setText(String.valueOf(fila));
			}
		});
		
		table.setDefaultEditor(Object.class, null); //Hace que no se pueda editar las tablas pero si seleccionarlas
		scrollPane_1.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Liga Local Equipo A vs Equipo B", "Partido de Futbol", "Campo de Futbol Cubierto", "22/2/2026", "22/2/2026", "36", "Gratis", "5"},
				{"Gij\u00F3n Tenis Open", "Torneo de Tenis", "Canchas de Tenis 1-20", "20/2/2026", "22/2/2026", "128", "10", "15"},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"NombreActividad", "Tipo de Actividad", "Instalaci\u00F3n", "Fecha Inicial", "Fecha Final", "Numero de Plazas", "Precio Socios", "Precio No Socios"
			}
		));
		
		


	}
}
