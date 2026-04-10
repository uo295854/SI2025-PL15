package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class CancelarReservaInstalacionAdminView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
    // Tabla socios
    private JTable tablaSocios;
    private JButton btnBuscar;
    
    // Socio seleccionado
    private JTextField txtSocioSeleccionado;

    // Tabla reservas
    private JTable tablaReservas;

    // Motivo cancelación
    private JTextArea txtMotivoCancelacion;

    // Botones
    private JButton btnAtras;
    private JButton btnCancelarReserva;
    
    // Getters y setters
	public JTable getTablaSocios() { return tablaSocios; }
	public void setTablaSocios(JTable tablaSocios) { this.tablaSocios = tablaSocios; }
	
	public JButton getBtnBuscarSocio() {return btnBuscar;}
	public void setBtnBuscarSocio(JButton btnBuscarSocio) {this.btnBuscar = btnBuscarSocio;}
	
	public JTextField getTxtSocioSeleccionado() {return txtSocioSeleccionado;}
	public void setTxtSocioSeleccionado(JTextField txtSocioSeleccionado) {this.txtSocioSeleccionado = txtSocioSeleccionado;}
	
	public JTable getTablaReservas() {return tablaReservas;}
	public void setTablaReservas(JTable tablaReservas) {this.tablaReservas = tablaReservas;}
	
	public JTextArea getTxtMotivoCancelacion() {return txtMotivoCancelacion;}
	public void setTxtMotivoCancelacion(JTextArea txtMotivoCancelacion) {this.txtMotivoCancelacion = txtMotivoCancelacion;}
	
	public JButton getBtnAtras() {return btnAtras;}
	public void setBtnAtras(JButton btnAtras) {this.btnAtras = btnAtras;}
	
	public JButton getBtnCancelarReserva() {return btnCancelarReserva;}
	public void setBtnCancelarReserva(JButton btnCancelarReserva) {this.btnCancelarReserva = btnCancelarReserva;}
	
	
	public CancelarReservaInstalacionAdminView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1200, 750);
		 setLocationRelativeTo(null);
		 
		 JPanel root = new JPanel(new BorderLayout(10, 10));
	        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        setContentPane(root);

	        root.add(panelSuperior(), BorderLayout.NORTH);
	        root.add(panelInferior(), BorderLayout.CENTER);
	}
	
	private JPanel panelSuperior() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		  panel.setBorder(new TitledBorder("Seleccionar un socio"));
		  
		  //tabla de los datos
		    DefaultTableModel tabladatos = new DefaultTableModel(new Object[]{"Apellidos", "Nombre", "Nº socio"}, 0) {
		        @Override
		        public boolean isCellEditable(int row, int col) {
		            // Solo la fila 0 es editable porque es la fila donde buscamos
		            return row == 0;
		        }
		    };

		    // Fila donde se busca en cualquiera de los tres campos
		    tabladatos.addRow(new Object[]{"", "", ""});

		    tablaSocios = new JTable(tabladatos);
		    tablaSocios.setRowHeight(22);


			//fila separadora
			 tabladatos.addRow(new Object[]{"", "", ""}); 
			 tablaSocios.setRowHeight(22);
			 tablaSocios.setRowHeight(1, 6); 
		    
			//panel de scroll
		    JScrollPane scroll = new JScrollPane(tablaSocios);
		    panel.add(scroll, BorderLayout.CENTER);

		    // Botón lupa
		    Buscar = new JButton("🔍");
		    JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		    south.add(Buscar);
		    panel.add(south, BorderLayout.EAST);

		    
		    panel.setPreferredSize(new Dimension(300,200));
		    return panel;
	}
	
	
	
	

	
}
