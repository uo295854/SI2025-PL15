package giis.sisinfo.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
public class ReservaInstalacionAdminSocioView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//tablas
	private JTable tablaSocios;
	private JTable tablaDías;
	private JTable tablaHoras;
	
	public JTable gettablaSocios() { return tablaSocios; }
	public JTable gettablaDias() { return tablaDías; }
	public JTable gettablaHoras() { return tablaHoras; }
	
	//combo box
	private JComboBox<String> cbDeporte;
	private JComboBox<String> cbPista;
	
	public JComboBox<String> getcbDeporte() { return cbDeporte; }
	public JComboBox<String> getcbPista() { return cbPista; }
	

	

	
	//Resumen, lista de días
	private JTextField textfieldNombre, textfieldNumSocio, textfieldEmail, textfieldTelefono;
	
	
	//Resumen, horas del día
	private JTextField textfieldDeporte, textfieldLugar, textfieldDia, textfieldHora,textfieldCoste;
	
	

	
	//Botones cancelar y reservar
	private JButton cancelar,reservar;
	public JButton getcancelar() { return cancelar; }
	public JButton getreservar() { return reservar; }
	
	//Boton busar
	private JButton Buscar;
	public JButton getBuscar() { return Buscar; }
	
	
	//Botones para que se seleccione si se paga al momento del uso o se queda pendiente el pago
	private JRadioButton rbPagoUso;
	private JRadioButton rbPagoMensual;
	
	public JRadioButton getRbPagoUso() { return rbPagoUso; }
	public JRadioButton getRbPagoMensual() { return rbPagoMensual; }

	
	
	public ReservaInstalacionAdminSocioView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(980,620);
		setLocationRelativeTo(null);
		
		JPanel root = new JPanel(new BorderLayout(10,10));
		root.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setContentPane(root);
		
		
		//Seleccion de socios y de instlacion
		JPanel arriba = new JPanel(new GridLayout(1,2,10,10));
		arriba.add(panelSocios());
		arriba.add(panelInstalacion());
		root.add(arriba, BorderLayout.NORTH);
		
		
		//Seleccion de día y de hora
		JPanel centro = new JPanel(new GridLayout(1,2,10,10));
		centro.add(panelDias());
		centro.add(panelHoras());
		root.add(centro,BorderLayout.CENTER);
		
		
		//Resumen
		root.add(panelAbajo(),BorderLayout.SOUTH);
	}
	
	private JPanel panelSocios() {

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

	

	private JPanel panelInstalacion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Selección de instalación"));
        
        //reglas para situar bien los combo box
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.WEST;

        c.gridx=0; c.gridy=0;
        panel.add(new JLabel("Tipo deporte:"), c);

        //crear un nuevo combobox
        cbDeporte = new JComboBox<>();
        cbDeporte.setPreferredSize(new Dimension(120,25));
        c.gridx=1;
        panel.add(cbDeporte, c);

        c.gridx=2;
        panel.add(new JLabel("Módulo:"), c);

        cbPista = new JComboBox<>();
        cbPista.setPreferredSize(new Dimension(120,25));
        c.gridx=3; 
        panel.add(cbPista, c);

        panel.setPreferredSize(new Dimension(300,200));
        
        return panel;
	}
	
	private JPanel panelDias() {
	      JPanel panel = new JPanel(new BorderLayout(5,5));
	        panel.setBorder(new TitledBorder("Seleccionar fecha (lista de días)"));

	        tablaDías = new JTable(new DefaultTableModel(
	                new Object[]{"Día", "Fecha", "Estado"}, 0));
	        panel.add(new JScrollPane(tablaDías), BorderLayout.CENTER);
	       
	        panel.setPreferredSize(new Dimension(300,200));
	        return panel;
        
	}
	
	private JPanel panelHoras() {
		   JPanel panel = new JPanel(new BorderLayout(5,5));
	        panel.setBorder(new TitledBorder("Horas del día"));

	        tablaHoras = new JTable(new DefaultTableModel(
	                new Object[]{"Hora entrada", "Hora salida", "Estado", "Motivo"}, 0));
	        panel.add(new JScrollPane(tablaHoras), BorderLayout.CENTER);

	        return panel;
	}
	
	private JPanel panelAbajo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Resumen"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,8,4,8);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.NONE;


        textfieldNombre = new JTextField(18); 
        textfieldNombre.setEditable(false);
        
        textfieldNumSocio = new JTextField(10); 
        textfieldNumSocio.setEditable(false);
        textfieldEmail = new JTextField(18); 
        textfieldEmail.setEditable(false);
        textfieldTelefono = new JTextField(12); 
        textfieldTelefono.setEditable(false);


        textfieldDeporte = new JTextField(10); 
        textfieldDeporte.setEditable(false);
        textfieldLugar = new JTextField(10); 
        textfieldLugar.setEditable(false);
        textfieldDia = new JTextField(10); 
        textfieldDia.setEditable(false);
        textfieldHora = new JTextField(12); 
        textfieldHora.setEditable(false);
        textfieldCoste = new JTextField(6); 
        textfieldCoste.setEditable(false);

        
        //Columna izquierda con ajustes
        c.gridx = 0; 
        c.gridy = 0; 
        panel.add(new JLabel("Nombre"), c);
        
        c.gridx = 1; 
        panel.add(textfieldNombre, c);

        c.gridx = 0; 
        c.gridy = 1; 
        panel.add(new JLabel("Nº socio"), c);
        
        c.gridx = 1; 
        panel.add(textfieldNumSocio, c);

        c.gridx = 0; 
        c.gridy = 2; 
        panel.add(new JLabel("Email"), c);
        
        c.gridx = 1; 
        panel.add(textfieldEmail, c);

        c.gridx = 0; 
        c.gridy = 3; 
        panel.add(new JLabel("Teléfono"), c);
        
        c.gridx = 1; 
        panel.add(textfieldTelefono, c);

        //Columna derecha con ajustes
        c.gridx = 3; 
        c.gridy = 0; 
        panel.add(new JLabel("Deporte"), c);
        
        c.gridx = 4; 
        panel.add(textfieldDeporte, c);

        c.gridx = 3; 
        c.gridy = 1; 
        panel.add(new JLabel("Lugar"), c);
        
        c.gridx = 4; 
        panel.add(textfieldLugar, c);

        c.gridx = 3; 
        c.gridy = 2; 
        panel.add(new JLabel("Día"), c);
        
        c.gridx = 4; 
        panel.add(textfieldDia, c);

        c.gridx = 3; 
        c.gridy = 3; 
        panel.add(new JLabel("Hora"), c);
        
        c.gridx = 4; 
        panel.add(textfieldHora, c);

        c.gridx = 3; 
        c.gridy = 4; 
        panel.add(new JLabel("Coste"), c);
        
        c.gridx = 4; 
        panel.add(textfieldCoste, c);
        
        
        cancelar = new JButton("Cancelar");
        reservar = new JButton("Reservar");

     // Cancelar
        c.gridx = 5;
        c.gridy = 4;
        c.weightx = 24;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(cancelar, c);

        // Reservar
        c.gridx = 6;
        c.weightx = 1;          
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        panel.add(reservar, c);

        
        rbPagoUso = new JRadioButton("Pagar en el momento del uso");
        rbPagoMensual = new JRadioButton("Añadir a la cuota mensual");
        
        ButtonGroup grupopago = new ButtonGroup();
        grupopago.add(rbPagoUso);
        grupopago.add(rbPagoMensual);
        
        c.gridx = 5;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Forma de pago:"), c);

        c.gridy = 1;
        panel.add(rbPagoUso, c);

        c.gridy = 2;
        panel.add(rbPagoMensual, c);
       
        return panel;
	}

	
	 public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new ReservaInstalacionAdminSocioView().setVisible(true));
	    }
	 
	 
	 public void setResumenSocio(String nombre, String num, String email, String tel) {
		    textfieldNombre.setText(nombre);
		    textfieldNumSocio.setText(num);
		    textfieldEmail.setText(email);
		    textfieldTelefono.setText(tel);
		}

		public void setResumenReserva(String deporte, String lugar, String dia, String hora, String coste) {
		    textfieldDeporte.setText(deporte);
		    textfieldLugar.setText(lugar);
		    textfieldDia.setText(dia);
		    textfieldHora.setText(hora);
		    textfieldCoste.setText(coste);
		}

		public void limpiarHorasYResumenReserva() {
		    ((DefaultTableModel) tablaHoras.getModel()).setRowCount(0);
		    tablaHoras.clearSelection();
		    setResumenReserva("", "", "", "", "");
		}
		
		public String getEstadoPagoSeleccionado() {
		    if (rbPagoUso.isSelected())
		        return "PAGADO";
		    return "PENDIENTE";
		}
	
}
