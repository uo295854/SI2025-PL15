package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class CancelarReservaInstalacionAdminView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
    // Tabla socios
    private JTable tablaSocios;
    private JButton Buscar;
    
    // Socio seleccionado
    private JTextField txtSocioSeleccionado;

    // Tabla reservas
    private JTable tablaReservas;

    // Motivo cancelación
    private JTextArea txtMotivoCancelacion;

    // Botones
    private JButton Atras;
    private JButton CancelarReserva;
    
    // Getters y setters
	public JTable getTablaSocios() { return tablaSocios; }
	public void setTablaSocios(JTable tablaSocios) { this.tablaSocios = tablaSocios; }
	
	public JButton getBtnBuscarSocio() {return Buscar;}
	public void setBtnBuscarSocio(JButton btnBuscarSocio) {this.Buscar = btnBuscarSocio;}
	
	public JTextField getTxtSocioSeleccionado() {return txtSocioSeleccionado;}
	public void setTxtSocioSeleccionado(JTextField txtSocioSeleccionado) {this.txtSocioSeleccionado = txtSocioSeleccionado;}
	
	public JTable getTablaReservas() {return tablaReservas;}
	public void setTablaReservas(JTable tablaReservas) {this.tablaReservas = tablaReservas;}
	
	public JTextArea getTxtMotivoCancelacion() {return txtMotivoCancelacion;}
	public void setTxtMotivoCancelacion(JTextArea txtMotivoCancelacion) {this.txtMotivoCancelacion = txtMotivoCancelacion;}
	
	public JButton getBtnAtras() {return Atras;}
	public void setBtnAtras(JButton btnAtras) {this.Atras = btnAtras;}
	
	public JButton getBtnCancelarReserva() {return CancelarReserva;}
	public void setBtnCancelarReserva(JButton btnCancelarReserva) {this.CancelarReserva = btnCancelarReserva;}
	
	
	public CancelarReservaInstalacionAdminView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(980, 620);
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

		    
		    panel.setPreferredSize(new Dimension(800,200));
		    return panel;
	}
	
	
	private JPanel panelInferior() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new TitledBorder("Seleccionar reserva"));

        panel.add(panelDatosReserva(), BorderLayout.CENTER);
        panel.add(panelMotivo(), BorderLayout.EAST);

        return panel;
	}
	
	private JPanel panelDatosReserva() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		
		JPanel panelSocio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSocio.add(new JLabel("Socio:"));

        txtSocioSeleccionado = new JTextField(30);
        txtSocioSeleccionado.setEditable(false);
        panelSocio.add(txtSocioSeleccionado);

        panel.add(panelSocio, BorderLayout.NORTH);

        DefaultTableModel modeloReservas = new DefaultTableModel(
                new Object[]{"Instalación", "Fecha", "Día", "Hora Entrada", "Hora Salida", "Estado Pago"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaReservas = new JTable(modeloReservas);
        tablaReservas.setRowHeight(24);
        JScrollPane scrollReservas = new JScrollPane(tablaReservas);
        panel.add(scrollReservas, BorderLayout.CENTER);

        return panel;
	}
	
	private JPanel panelMotivo() {
		  JPanel panel = new JPanel(new BorderLayout(10, 10));
	        panel.setPreferredSize(new Dimension(300, 0));

	        JPanel panelMotivo = new JPanel(new BorderLayout(5, 5));
	        panelMotivo.setBorder(new TitledBorder("Motivo de la cancelación"));

	        txtMotivoCancelacion = new JTextArea(12, 20);
	        txtMotivoCancelacion.setLineWrap(true);
	        txtMotivoCancelacion.setWrapStyleWord(true);

	        JScrollPane scrollMotivo = new JScrollPane(txtMotivoCancelacion);
	        panelMotivo.add(scrollMotivo, BorderLayout.CENTER);

	        panel.add(panelMotivo, BorderLayout.CENTER);
	        panel.add(panelBotones(), BorderLayout.SOUTH);

	        return panel;
	}
	
	  private JPanel panelBotones() {
	        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

	        Atras = new JButton("Atrás");
	        CancelarReserva = new JButton("Cancelar Reserva");

	        panel.add(Atras);
	        panel.add(CancelarReserva);

	        return panel;
	    }
	    public void setSocioSeleccionado(String socio) {
	        txtSocioSeleccionado.setText(socio);
	    }

	    public void limpiarReservas() {
	        ((DefaultTableModel) tablaReservas.getModel()).setRowCount(0);
	    }

	    public void limpiarMotivoCancelacion() {
	        txtMotivoCancelacion.setText("");
	    }

	    public void limpiarTodo() {
	        txtSocioSeleccionado.setText("");
	        limpiarReservas();
	        limpiarMotivoCancelacion();
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            new CancelarReservaInstalacionAdminView().setVisible(true);
	        });
	    }

	
}
