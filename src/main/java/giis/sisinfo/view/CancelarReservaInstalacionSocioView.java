package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CancelarReservaInstalacionSocioView extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Tabla reservas
    private JTable tablaReservas;
    private TableRowSorter<DefaultTableModel> ordenarReservas;
    
    // Botones
    private JButton Atras;
    private JButton CancelarReserva;
    
    
    public JTable getTablaReservas() {
		return tablaReservas;
	}
	public void setTablaReservas(JTable tablaReservas) {
		this.tablaReservas = tablaReservas;
	}
	public TableRowSorter<DefaultTableModel> getOrdenarReservas() {
		return ordenarReservas;
	}
	public void setOrdenarReservas(TableRowSorter<DefaultTableModel> ordenarReservas) {
		this.ordenarReservas = ordenarReservas;
	}
	public JButton getAtras() {
		return Atras;
	}
	public void setAtras(JButton atras) {
		Atras = atras;
	}
	public JButton getCancelarReserva() {
		return CancelarReserva;
	}
	public void setCancelarReserva(JButton cancelarReserva) {
		CancelarReserva = cancelarReserva;
	}
	
	public CancelarReservaInstalacionSocioView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(980, 620);
		 setLocationRelativeTo(null);
		 
		 JPanel root = new JPanel(new BorderLayout(10, 10));
	        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        setContentPane(root);
	        
	        root.add(panelReservas(), BorderLayout.CENTER);
	        root.add(panelAbajo(), BorderLayout.SOUTH);
	}
	
	private JPanel panelReservas() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(new TitledBorder("Listado de reservas"));

		DefaultTableModel modelo = new DefaultTableModel(new Object[] { "Deporte", "Instalación", "Fecha", "Día", "Hora Entrada", "Estado Pago" }, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
	};
		
	tablaReservas = new JTable(modelo);
	tablaReservas.setRowHeight(22);

	ordenarReservas = new TableRowSorter<>(modelo);
	tablaReservas.setRowSorter(ordenarReservas);

	panel.add(new JScrollPane(tablaReservas), BorderLayout.CENTER);
	return panel;
		
}
	
	  private JPanel panelAbajo() {
	        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

	        Atras = new JButton("Atrás");
	        CancelarReserva = new JButton("Cancelar Reserva");

	        panel.add(Atras);
	        panel.add(CancelarReserva);

	        return panel;
	    }
	
	
	    public void limpiarReservas() {
	        ((DefaultTableModel) tablaReservas.getModel()).setRowCount(0);
	    }
	
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            new CancelarReservaInstalacionSocioView().setVisible(true);
	        });
	    }
    
}
