package giis.sisinfo.controller;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import giis.sisinfo.dto.SocioDTO;
import giis.sisinfo.model.CancelarReservaInstalacionAdminModel;
import giis.sisinfo.view.CancelarReservaInstalacionAdminView;

public class CancelarReservaInstalacionAdminController {

	private final CancelarReservaInstalacionAdminModel model;
	private final CancelarReservaInstalacionAdminView view;
	
	private List<SocioDTO> sociosMostrados;
	private SocioDTO socioSeleccionado;
	
	private List<Object[]> reservasMostradas;
	
	public CancelarReservaInstalacionAdminController(CancelarReservaInstalacionAdminModel model, CancelarReservaInstalacionAdminView view) {
		this.model = model;
		this.view = view;
		
		
		configurarTablas();
		Eventos();
		
		
		view.setVisible(true);
	}
	
	private void configurarTablas() {
		view.getTablaSocios().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		view.getTablaReservas().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

	}
	
	private void limpiarTodo(JTable t) {
		DefaultTableModel tm = (DefaultTableModel) t.getModel();
		tm.setRowCount(0);
	}
	
	private void Eventos() {
		//Buscar socios
		view.getBtnBuscarSocio().addActionListener(e -> onBuscarSocios());
				
		//Seleccionar socio
		view.getTablaSocios().getSelectionModel().addListSelectionListener(e->onSeleccionSocio(e));
		
		//Cancelar la Reserva
		view.getBtnCancelarReserva().addActionListener(e->onCancelarReserva());
		
		//Botón atrás
		view.getBtnAtras().addActionListener(e ->view.dispose());
		
	}
	private void onBuscarSocios() {
		  if (view.getTablaSocios().isEditing()) {
		        view.getTablaSocios().getCellEditor().stopCellEditing();
		    }
		DefaultTableModel t = (DefaultTableModel) view.getTablaSocios().getModel();
		
		String apellidos = String.valueOf(t.getValueAt(0, 0)).trim();
		String nombre = String.valueOf(t.getValueAt(0, 1)).trim();
		String nsocio = String.valueOf(t.getValueAt(0, 2)).trim();
		
		
		sociosMostrados = model.buscadorSocios(apellidos, nombre, nsocio);
		
		//borrar la filas a partir de la fila de busqueda y la separadora
		while(t.getRowCount() > 2)
			t.removeRow(2);
		
		
		for(SocioDTO s: sociosMostrados) {
			t.addRow(new Object[] {
					s.getApellidos(),
					s.getNombre(),
					s.getNumSocio()
			});
		}

		
	 //se resetea el socio seleccionado
		socioSeleccionado = null;
        reservasMostradas = null;
        
        view.setSocioSeleccionado("");
        view.limpiarReservas();
        view.limpiarMotivoCancelacion();
	}
	
	private void onSeleccionSocio(ListSelectionEvent e) {
		//para que el evento no se dispare dos veces
		if(e.getValueIsAdjusting()) return;
		
		int fila = view.getTablaSocios().getSelectedRow();
		if(fila<2) return; //evitar coger la fila 0 de busqueda y fila 1 separadora
		
		int indice = fila-2;
		if(sociosMostrados == null || indice<0 || indice >= sociosMostrados.size()) return; //evitar salir de la tabla
		
		
		socioSeleccionado = sociosMostrados.get(indice);
		view.limpiarMotivoCancelacion();
		view.setSocioSeleccionado(socioSeleccionado.getNombre() + " " + socioSeleccionado.getApellidos());
		cargarReservasSocio();
	}
	
    private void cargarReservasSocio() {
        if (socioSeleccionado == null) {
            return;
        }

        reservasMostradas = model.getReservasActivasSocio(socioSeleccionado.getIdSocio());

        DefaultTableModel t = (DefaultTableModel) view.getTablaReservas().getModel();
        limpiarTodo(view.getTablaReservas());

        for (Object[] r : reservasMostradas) {
            t.addRow(new Object[] {
                    r[1], // instalación
                    r[2], // fecha
                    r[3], // hora entrada
                    r[4], // hora salida
                    r[5]  // estado pago
            });
        }
    }
	
    
    private void onCancelarReserva() {
    	if(socioSeleccionado == null) {
			JOptionPane.showMessageDialog(view, "Tienes que seleccionar un socio");
			return;
		}
    	
    	int[] filas = view.getTablaReservas().getSelectedRows();
    	if (filas == null || filas.length == 0) {
             JOptionPane.showMessageDialog(view, "Tienes que seleccionar una reserva.");
             return;
         }
    	
        String motivo = view.getTxtMotivoCancelacion().getText();
        if (motivo == null || motivo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tienes que indicar un motivo de cancelación.");
            return;
        }
        
        int canceladas = 0;
        StringBuilder errores = new StringBuilder();

        for (int fila : filas) {
            try {
                int idReserva = ((Number) reservasMostradas.get(fila)[0]).intValue();
                model.cancelarReserva(idReserva);
                canceladas++;
            } catch (Exception e) {
                Object[] reserva = reservasMostradas.get(fila);
                errores.append("- ")
                	   .append(reserva[1]) //instalación
                       .append(" | ")
                       .append(reserva[2]) //fecha
                       .append(" | ")
                       .append(reserva[3]) //hora de entrada
                       .append(": ")
                       .append(e.getMessage())
                       .append("\n");
            	}
        }
        
        cargarReservasSocio();
        view.limpiarMotivoCancelacion();

        if (canceladas > 0 && errores.length() == 0) {
            JOptionPane.showMessageDialog(view, "Se ha cancelado correctamente " + canceladas + " reservas");
        } else if (canceladas > 0) {
            JOptionPane.showMessageDialog(view, "Se cancelaron " + canceladas + " reservas\n\n" + "Pero no se pudieron cancelar estas:\n" + errores.toString(),"Cancelación parcial", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "No se pudo cancelar ninguna reserva\n\n" + errores.toString(), "Cancelación no realizada", JOptionPane.ERROR_MESSAGE);
        }
	
  }
    
    
}
