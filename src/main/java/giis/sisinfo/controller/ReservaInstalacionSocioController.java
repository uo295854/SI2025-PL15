package giis.sisinfo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
import giis.sisinfo.dto.ResguardoReservaSocioDTO;
import giis.sisinfo.model.ReservaInstalacionSocioModel;
import giis.sisinfo.util.ResguardoReservaSocioPdf;
import giis.sisinfo.view.ReservaInstalacionSocioView;

public class ReservaInstalacionSocioController {

	private final ReservaInstalacionSocioModel model;
	private final ReservaInstalacionSocioView view;
	private final int idSocio;
	
	private String deporteSeleccionado;
	private String instalacionSeleccionada;
	private Integer idInstalacionSeleccionada;

	private LocalDate diaSeleccionado;
	private List<LocalTime> horasSeleccionadas;
	
	
	private double costeSeleccionado;
	
	public ReservaInstalacionSocioController(ReservaInstalacionSocioModel model,ReservaInstalacionSocioView view,int idSocio) {
		
		
		this.model = model;
		this.view = view;
		this.idSocio = idSocio;
		
		configurarTablas();
		Eventos();
		cargarDeportes();
		
		view.setVisible(true);
	}
	
	private void configurarTablas() {
		view.getTablaDias().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		view.getTablaHoras().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	
	private void cargarDeportes() {
		List<String> deportes = model.getDeportes();
		DefaultComboBoxModel<String> c = new DefaultComboBoxModel<String>();
		
		for(String s: deportes)
			c.addElement(s);
		
		view.getCbDeporte().setModel(c);
		
		view.getCbPista().setModel(new DefaultComboBoxModel<>());
		limpiarTodo(view.getTablaDias());
		limpiarTodo(view.getTablaHoras());
		
	}
	
	private void limpiarTodo(JTable t) {
		DefaultTableModel tm = (DefaultTableModel) t.getModel();
		tm.setRowCount(0);
	}
	
	private void Eventos() {
		
		//Cada vez que cambia deporte, carga instalaciones
		view.getCbDeporte().addActionListener(e-> onDeporteSeleccionado());
		
		//Cada vez que cambia la instalacion, carga dias
		view.getCbPista().addActionListener(e -> onInstalacionSeleccionada());
		
		
		//Seleccionar dia, cargar horas del dia
		view.getTablaDias().getSelectionModel().addListSelectionListener(e -> onSeleccionDia(e));
		
		//Seleccionar hora y actualizar el resumen
		view.getTablaHoras().getSelectionModel().addListSelectionListener(e->onSeleccionHora(e));
		
		
		//Hacer la reserva
		view.getReservar().addActionListener(e->onReservar());
		
		
		//Cancelar
		view.getCancelar().addActionListener(e->view.dispose());
		
		//Filtro en la cabecera de la tabla
		view.getTablaDias().getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				int columna = view.getTablaDias().columnAtPoint(e.getPoint());

				if (columna == 0) {
					mostrarDesplegableFiltroDia(e);
				} else if (columna == 1) {
					mostrarDesplegableFiltroFechas(e);
				}
			}
		});
		
	}
	
	
	private void onDeporteSeleccionado() {
		Object elementoseleccionado = view.getCbDeporte().getSelectedItem();
		deporteSeleccionado = elementoseleccionado.toString();
		
		
		instalacionSeleccionada = null;
		idInstalacionSeleccionada = null;
		diaSeleccionado = null;
		horasSeleccionadas = null;
		
		
		limpiarTodo(view.getTablaDias());
		limpiarTodo(view.getTablaHoras());
		
		if(deporteSeleccionado == null || deporteSeleccionado.isBlank()) {
			view.getCbPista().setModel(new DefaultComboBoxModel<>());
			return;
		}
		
		List<String> instalaciones = model.getInstalacionesPorDeporte(deporteSeleccionado);
		DefaultComboBoxModel<String> c = new DefaultComboBoxModel<>();
		for(String i: instalaciones)
			c.addElement(i);
		
		view.getCbPista().setModel(c);
		view.setResumenReserva(
			    deporteSeleccionado == null ? "" : deporteSeleccionado,
			    "", "", "", ""
			);
			
	}
	
	private void onInstalacionSeleccionada() {
		Object seleccionada = view.getCbPista().getSelectedItem();
		
		instalacionSeleccionada = seleccionada.toString();
		
		diaSeleccionado = null;
		horasSeleccionadas = null;
		
		limpiarTodo(view.getTablaDias());
		limpiarTodo(view.getTablaHoras());
		
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
		DefaultTableModel t = (DefaultTableModel) view.getTablaDias().getModel();
		limpiarTodo(view.getTablaDias());
		
		costeSeleccionado = model.getCosteInstalacion(idInstalacionSeleccionada);
		
		for(DiaReservaSocioDTO d: dias)
			t.addRow(new Object[] {
					d.getDia(),
					d.getFecha(),
					d.getEstado()
			});
		view.setResumenReserva(
			    deporteSeleccionado == null ? "" : deporteSeleccionado,
			    instalacionSeleccionada == null ? "" : instalacionSeleccionada,
			    "", "", ""
			);
	}
	
	private void onSeleccionDia(ListSelectionEvent e) {
		//evitar que ocurra el evento 2 veces
		if (e.getValueIsAdjusting()) return;

	    int filaVista = view.getTablaDias().getSelectedRow();
	    if (filaVista < 0) return;

	    int fila = view.getTablaDias().convertRowIndexToModel(filaVista);

		DefaultTableModel t = (DefaultTableModel) view.getTablaDias().getModel();
		String fecha = String.valueOf(t.getValueAt(fila, 1));
		String estado   = String.valueOf(t.getValueAt(fila, 2));

		//si el día es no reservable o está completo, no se cargan horas
		if ("NO_RESERVABLE".equalsIgnoreCase(estado) || "COMPLETO".equalsIgnoreCase(estado)) {
			limpiarTodo(view.getTablaHoras());
			diaSeleccionado = null;
			horasSeleccionadas = null;
			return;
		}

		diaSeleccionado = LocalDate.parse(fecha);
		view.setResumenReserva(
			    deporteSeleccionado == null ? "" : deporteSeleccionado,
			    instalacionSeleccionada == null ? "" : instalacionSeleccionada,
			    diaSeleccionado.toString(),
			    "",
			    ""
			);

		List<HoraReservaSocioDTO> horas = model.getHorasDia(idInstalacionSeleccionada, diaSeleccionado);
		DefaultTableModel th = (DefaultTableModel) view.getTablaHoras().getModel();
		limpiarTodo(view.getTablaHoras());

		for (HoraReservaSocioDTO h : horas) {
			th.addRow(new Object[] { h.getHoraInicio(), h.getEstado(), h.getMotivo() });
		}

		view.getTablaHoras().clearSelection();

	}
	
	
	private void onSeleccionHora(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;

		int[] filas = view.getTablaHoras().getSelectedRows();
		if (filas == null || filas.length==0) {
			horasSeleccionadas=null;
			view.setResumenReserva(
				    deporteSeleccionado == null ? "" : deporteSeleccionado,
				    instalacionSeleccionada == null ? "" : instalacionSeleccionada,
				    diaSeleccionado == null ? "" : diaSeleccionado.toString(),
				    "",
				    ""
				);
			return;
		}

		DefaultTableModel t = (DefaultTableModel) view.getTablaHoras().getModel();
		List<LocalTime> horas = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<filas.length;i++) {
			int fila = filas[i];
			String estado= String.valueOf(t.getValueAt(fila, 1));
			
			//Si no está libre no se puede seleccionar
			if (!"LIBRE".equalsIgnoreCase(estado)) {
				view.getTablaHoras().clearSelection();
				horasSeleccionadas = null;
				JOptionPane.showMessageDialog(view, "Solo se pueden seleccionar horas libres");
				return;
			}
			
			String horaInicio = String.valueOf(t.getValueAt(fila, 0));
			LocalTime hora = LocalTime.parse(horaInicio);
	        LocalTime horaFin = hora.plusHours(1);
			
			horas.add(hora);
			if (i > 0) 
				sb.append(", ");
			
			sb.append(horaInicio).append("-").append(horaFin);
		}
		

	

		horas.sort(null);
		horasSeleccionadas = horas;
		
		double costeTotal = costeSeleccionado *horasSeleccionadas.size();

		view.setResumenReserva(
		    deporteSeleccionado == null ? "" : deporteSeleccionado,
		    instalacionSeleccionada == null ? "" : instalacionSeleccionada,
		    diaSeleccionado == null ? "" : diaSeleccionado.toString(),
		    sb.toString(),
		    costeTotal + " €"
		);
		
		

	}
	
	
	
private void onReservar() {

		
		if(idInstalacionSeleccionada == null) {
			JOptionPane.showMessageDialog(view, "Tienes que seleccionar una instalacion");
			return;
		}
		
		if(diaSeleccionado == null) {
			JOptionPane.showMessageDialog(view, "Tienes que seleccionar un dia");
			return;
		}
		
		if(horasSeleccionadas == null || horasSeleccionadas.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Tienes que seleccionar una o varias horas libres");
			return;
		}
		
		if (!view.hayFormaPagoSeleccionada()) {
		    JOptionPane.showMessageDialog(view, "Debes seleccionar una forma de pago");
		    return;
		}
		
		try {
			String estadoPago = view.getEstadoPagoSeleccionado();
			model.crearReserva(idInstalacionSeleccionada, idSocio, diaSeleccionado, horasSeleccionadas, estadoPago);
			
			ResguardoReservaSocioDTO resguardo = new ResguardoReservaSocioDTO(
					"Complejo Deportivo La Cruz",
					"Calle Río Eo 12 4º I",
					"Gijón",
					"Asturias",
					deporteSeleccionado,
					instalacionSeleccionada,
					diaSeleccionado,
					horasSeleccionadas,
					costeSeleccionado*horasSeleccionadas.size(),
					estadoPago,
					LocalDate.now());
			String rutadelpdf = new ResguardoReservaSocioPdf().generar(resguardo);
			
			JOptionPane.showMessageDialog(view, "Reserva realizada.\nResguardo generado en:\n" + rutadelpdf);
			view.dispose();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "No se pudo reservar", JOptionPane.ERROR_MESSAGE);
		}
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

	    view.getTablaDias().clearSelection();
	    view.getTablaHoras().clearSelection();;
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
