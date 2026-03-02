package giis.sisinfo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import giis.sisinfo.dto.DiaReservaSocioDTO;
import giis.sisinfo.dto.HoraReservaSocioDTO;
import giis.sisinfo.model.VisualizarReservasInstalacionesAdminModel;
import giis.sisinfo.view.VisualizarReservasInstalacionesAdminView;
public class VisualizarReservasInstalacionesAdminController {

	private final VisualizarReservasInstalacionesAdminModel model;
	private final VisualizarReservasInstalacionesAdminView view;
	
	private String deporteSeleccionado;
	private String instalacionSeleccionada;
	private Integer idInstalacionSeleccionada;

	private LocalDate diaSeleccionado;
	
	public VisualizarReservasInstalacionesAdminController(VisualizarReservasInstalacionesAdminModel model,VisualizarReservasInstalacionesAdminView view) {
		this.model = model;
		this.view = view;
		
		configurarTablas();
		Eventos();
		cargarDeportes();
		
		view.setVisible(true);
	}
	
	private void configurarTablas() {
		view.gettablaDias().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		view.gettablaHoras().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private void Eventos() {

		//Cada vez que cambia deporte, carga instalaciones
		view.getcbDeporte().addActionListener(e-> onDeporteSeleccionado());
		
		//Cada vez que cambia la instalacion, carga dias
		view.getcbPista().addActionListener(e -> onInstalacionSeleccionada());
		
		
		//Seleccionar dia, cargar horas del dia
		view.gettablaDias().getSelectionModel().addListSelectionListener(e -> onSeleccionDia(e));
		
				
		//Atrás
		view.getAtras().addActionListener(e->view.dispose());
		
		
		//Filtro en la cabecera de la tabla
		view.gettablaDias().getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				
				int columna = view.gettablaDias().columnAtPoint(e.getPoint());
				
				if(columna == 0) {
					mostrarDesplegableFiltroDia(e);
				}
				else if(columna == 1) {
					mostrarDesplegableFiltroFechas(e);
				}
			}
		});
		
	}
	
	private void cargarDeportes() {
		List<String> deportes = model.getDeportes();
		DefaultComboBoxModel<String> c = new DefaultComboBoxModel<String>();
		
		for(String s: deportes)
			c.addElement(s);
		
		view.getcbDeporte().setModel(c);
		
		view.getcbPista().setModel(new DefaultComboBoxModel<>());
		limpiarTodo(view.gettablaDias());
		limpiarTodo(view.gettablaHoras());
		
	}
	
	
	private void limpiarTodo(JTable t) {
		DefaultTableModel tm = (DefaultTableModel) t.getModel();
		tm.setRowCount(0);
	}

	private void onDeporteSeleccionado() {
		Object elementoseleccionado = view.getcbDeporte().getSelectedItem();
		
		deporteSeleccionado = elementoseleccionado.toString();
		
		
		instalacionSeleccionada = null;
		idInstalacionSeleccionada = null;
		diaSeleccionado = null;
		
		
		limpiarTodo(view.gettablaDias());
		limpiarTodo(view.gettablaHoras());
		
		if(deporteSeleccionado == null || deporteSeleccionado.isBlank()) {
			view.getcbPista().setModel(new DefaultComboBoxModel<>());
			return;
		}
		
		List<String> instalaciones = model.getInstalacionesPorDeporte(deporteSeleccionado);
		DefaultComboBoxModel<String> c = new DefaultComboBoxModel<>();
		for(String i: instalaciones)
			c.addElement(i);
		
		view.getcbPista().setModel(c);
		
	}
	
	private void onInstalacionSeleccionada() {
		Object seleccionada = view.getcbPista().getSelectedItem();
		
		instalacionSeleccionada = seleccionada.toString();
		
		diaSeleccionado = null;
		
		limpiarTodo(view.gettablaDias());
		limpiarTodo(view.gettablaHoras());
		
		if(instalacionSeleccionada == null || instalacionSeleccionada.isBlank()) {
			idInstalacionSeleccionada=null;
			return;
		}
		
		idInstalacionSeleccionada = model.getIdInstalacionPorNombre(instalacionSeleccionada);
		if(idInstalacionSeleccionada == null) {
			JOptionPane.showMessageDialog(view, "No se puede encontrar la instalacion seleccionada");
			return;
		}
		
		
		List<DiaReservaSocioDTO> dias = model.getDiasDisponibilidad(idInstalacionSeleccionada);
		DefaultTableModel t = (DefaultTableModel) view.gettablaDias().getModel();
		limpiarTodo(view.gettablaDias());
		
		
		for(DiaReservaSocioDTO d: dias)
			t.addRow(new Object[] {
					d.getDia(),
					d.getFecha(),
					d.getEstado()
			});
		
	}
	
	 private void onSeleccionDia(ListSelectionEvent e) {
		 	//evitar que ocurra el evento 2 veces
	        if (e.getValueIsAdjusting()) return;

	        int fila = view.gettablaDias().getSelectedRow();
	        if (fila < 0) return;

	        DefaultTableModel t = (DefaultTableModel) view.gettablaDias().getModel();
	        String fecha = String.valueOf(t.getValueAt(fila, 1));
	        String estado = String.valueOf(t.getValueAt(fila, 2));

	        //si el día es no reservable o está completo, no se cargan horas
	        if ("NO_RESERVABLE".equalsIgnoreCase(estado)) {
	            limpiarTodo(view.gettablaHoras());
	            diaSeleccionado = null;
	            return;
	        }

	        

	        diaSeleccionado = LocalDate.parse(fecha);

	        List<HoraReservaSocioDTO> horas = model.getHorasDia(idInstalacionSeleccionada, diaSeleccionado);
	        DefaultTableModel th = (DefaultTableModel) view.gettablaHoras().getModel();
	        limpiarTodo(view.gettablaHoras());

	        for (HoraReservaSocioDTO h : horas) {
	            th.addRow(new Object[]{ h.getHoraInicio(), h.getHoraFin(), h.getEstado(), h.getMotivo() });
	        }

	        view.gettablaHoras().clearSelection();
	    }
	 
	 private String filtroDiaSemana = "Todos";

	 private void mostrarDesplegableFiltroDia(java.awt.event.MouseEvent e) {
	     JPopupMenu menu = new JPopupMenu();

	     String[] opciones = {"Todos","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};

	     for (String op : opciones) {
	         JMenuItem item = new JMenuItem(op);
	         item.addActionListener(a -> {
	             filtroDiaSemana = op;
	             aplicarFiltrosTablaDias();
	         });
	         menu.add(item);
	     }

	     menu.show(e.getComponent(), e.getX(), e.getY());
	 }
	 
	 private String filtroRango = "Próximos 30 días";

	 private void mostrarDesplegableFiltroFechas(java.awt.event.MouseEvent e) {
	     JPopupMenu menu = new JPopupMenu();

	     String[] opciones = {"Hoy","Próximos 7 días","Próximos 15 días","Próximos 30 días"};

	     for (String op : opciones) {
	         JMenuItem item = new JMenuItem(op);
	         item.addActionListener(a -> {
	             filtroRango = op;
	             aplicarFiltrosTablaDias();
	         });
	         menu.add(item);
	     }

	     menu.show(e.getComponent(), e.getX(), e.getY());
	 }
	 
	 private void aplicarFiltrosTablaDias() {

		    TableRowSorter<DefaultTableModel> sorter = view.getDesplegableDias();

		    RowFilter<DefaultTableModel, Integer> filtro = new RowFilter<>() {
		        @Override
		        public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {

		            String fechaTexto = entry.getStringValue(1);
		            LocalDate fecha = LocalDate.parse(fechaTexto);

		            LocalDate hoy = LocalDate.now();

		            int maxDias;

		            switch (filtroRango) {

		                case "Hoy":
		                    maxDias = 0;
		                    break;

		                case "Próximos 7 días":
		                    maxDias = 6;
		                    break;

		                case "Próximos 15 días":
		                    maxDias = 14;
		                    break;

		                default:
		                    maxDias = 29;
		            }

		            if (fecha.isBefore(hoy) || fecha.isAfter(hoy.plusDays(maxDias))) {
		                return false;
		            }

		            if (!filtroDiaSemana.equalsIgnoreCase("Todos")) {

		                String diaES = diaSemana(fecha);

		                if (!diaES.equalsIgnoreCase(filtroDiaSemana)) {
		                    return false;
		                }
		            }

		            return true;
		        }
		    };

		    sorter.setRowFilter(filtro);

		    view.gettablaDias().clearSelection();
		    view.gettablaHoras().clearSelection();;
		}
	 private String diaSemana(LocalDate fecha) {

		    switch (fecha.getDayOfWeek()) {

		        case MONDAY:
		            return "Lunes";

		        case TUESDAY:
		            return "Martes";

		        case WEDNESDAY:
		            return "Miércoles";

		        case THURSDAY:
		            return "Jueves";

		        case FRIDAY:
		            return "Viernes";

		        case SATURDAY:
		            return "Sábado";

		        case SUNDAY:
		            return "Domingo";

		        default:
		            return "";
		    }
		}
	 
}
