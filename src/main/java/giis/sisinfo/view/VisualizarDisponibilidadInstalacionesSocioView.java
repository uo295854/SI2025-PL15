package giis.sisinfo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class VisualizarDisponibilidadInstalacionesSocioView extends JFrame {

	private static final long serialVersionUID = 1L;
	
	//tablas
	private JTable tablaDías;
	private JTable tablaHoras;
		
	public JTable gettablaDias() { return tablaDías; }
	public JTable gettablaHoras() { return tablaHoras; }
		
		
	//combo box
	private JComboBox<String> cbDeporte;
	private JComboBox<String> cbPista;
		
	public JComboBox<String> getcbDeporte() { return cbDeporte; }
	public JComboBox<String> getcbPista() { return cbPista; }
		

	//boton atras
	private JButton atras;
	public JButton getAtras() { return atras;}
		
	private TableRowSorter<DefaultTableModel> desplegableDias;

	public TableRowSorter<DefaultTableModel> getDesplegableDias() { return desplegableDias; }
	
	
	public VisualizarDisponibilidadInstalacionesSocioView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1080,620);
		setLocationRelativeTo(null);
		
		JPanel root = new JPanel(new BorderLayout(10,10));
		root.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setContentPane(root);
		
		
		//Seleccion de instlacion
		root.add(panelInstalacion(), BorderLayout.NORTH);
		
		//Seleccion de día y de hora
		JPanel centro = new JPanel(new GridLayout(1,2,10,10));
		centro.add(panelDias());
		centro.add(panelHoras());
		root.add(centro,BorderLayout.CENTER);
		
		
		//Boton atrás
		root.add(panelAbajo(),BorderLayout.SOUTH);
	}
	
	

	private JPanel panelInstalacion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Selección de instalación"));
        
        //reglas para situar bien los combo box
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,12,6,12);
        c.fill = GridBagConstraints.WEST;
        
        c.gridx=0; c.gridy=0;
        panel.add(new JLabel("Tipo deporte:"), c);

        //crear un nuevo combobox
        cbDeporte = new JComboBox<>();
        cbDeporte.setPreferredSize(new Dimension(140,25));
        c.gridx=1;
        panel.add(cbDeporte, c);

        c.gridx=2;
        panel.add(new JLabel("Módulo:"), c);

        cbPista = new JComboBox<>();
        cbPista.setPreferredSize(new Dimension(140,25));
        c.gridx=3; 
        panel.add(cbPista, c);

        panel.setPreferredSize(new Dimension(300,200));
        
        return panel;
        
	}
	

	private JPanel panelDias() {
	      JPanel panel = new JPanel(new BorderLayout(5,5));
	        panel.setBorder(new TitledBorder("Seleccionar fecha (lista de días)"));

	        DefaultTableModel modeloDias = new DefaultTableModel(new Object[]{"Día ▼", "Fecha ▼", "Estado"}, 0);
	        tablaDías = new JTable(modeloDias);
	        tablaDías.setRowHeight(22);
	        
	        desplegableDias = new TableRowSorter<>(modeloDias);
	        tablaDías.setRowSorter(desplegableDias);
	        panel.add(new JScrollPane(tablaDías), BorderLayout.CENTER);
	       
	        panel.setPreferredSize(new Dimension(300,200));
	        return panel;
      
	}
	
	private JPanel panelHoras() {
		   JPanel panel = new JPanel(new BorderLayout(5,5));
	        panel.setBorder(new TitledBorder("Horas del día"));

	        tablaHoras = new JTable(new DefaultTableModel(
	                new Object[]{"Hora","Estado"}, 0));
	        tablaHoras.setRowHeight(22);
	        panel.add(new JScrollPane(tablaHoras), BorderLayout.CENTER);

	        return panel;
	}
	
	private JPanel panelAbajo() {
	        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        atras = new JButton("Atrás");
	        panel.add(atras);
	        return panel;
	    }
	    
    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new VisualizarDisponibilidadInstalacionesSocioView().setVisible(true));
	    }
}
