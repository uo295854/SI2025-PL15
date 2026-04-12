import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class test4 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	
	//TEST HECHO PARA COMPROBAR SI SE PUEDE MODIFICAR LA ESTRUCTURA DE UNA TABLA MEDIANTE ACCIONES COMO PRESIONAR UN BOTÓN

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test4 frame = new test4();
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
	public test4() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton layout1Button = new JButton("Layout1");
		layout1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getTable().setModel(new DefaultTableModel(
						new Object[][] {
							{"16", "17", "18", "19"},
							{"26", "27", "28", "29"},
							{"36", "37", "38", "39"},
						},
						new String[] {
							"6", "7", "8", "9"
						}
					));
				
			}
		});
		layout1Button.setBounds(30, 10, 84, 20);
		contentPane.add(layout1Button);
		
		JButton layout2Button = new JButton("Layout2");
		layout2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.setModel(new DefaultTableModel(
						new Object[][] {
							{"11", "12", "13", "14", "15"},
							{"21", "22", "23", "24", "25"},
							{"31", "32", "33", "34", "35"},
						},
						new String[] {
							"1", "2", "3", "4", "5"
						}
					));
			}
		});
		layout2Button.setBounds(193, 10, 84, 20);
		contentPane.add(layout2Button);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"11", "12", "13", "14", "15"},
				{"21", "22", "23", "24", "25"},
				{"31", "32", "33", "34", "35"},
			},
			new String[] {
				"1", "2", "3", "4", "5"
			}
		));
		table.getColumnModel().getColumn(2).setPreferredWidth(79);
		table.setBounds(10, 56, 300, 177);
		contentPane.add(table);

	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
	
	

}
