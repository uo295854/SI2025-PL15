package giis.sisinfo.controller;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import giis.sisinfo.dto.DiaReservaSocioDTO;
import giis.sisinfo.dto.HoraReservaSocioDTO;
import giis.sisinfo.dto.SocioDTO;
import giis.sisinfo.model.ReservaInstalacionAdminSocioModel;
import giis.sisinfo.view.ReservaInstalacionAdminSocioView;
import java.util.ArrayList;

public class ReservaInstalacionAdminSocioController {

	private final ReservaInstalacionAdminSocioModel model;
	private final ReservaInstalacionAdminSocioView view;


	private List<SocioDTO> sociosMostrados;
	private SocioDTO socioSeleccionado;

	private String deporteSeleccionado;
	private String instalacionSeleccionada;
	private Integer idInstalacionSeleccionada;

	private LocalDate diaSeleccionado;
	private List<LocalTime> horasSeleccionadas;
	
	
	private double costeSeleccionado;
	
	
	public ReservaInstalacionAdminSocioController(ReservaInstalacionAdminSocioModel model, ReservaInstalacionAdminSocioView view) {
		
		this.model = model;
		this.view = view;
		
		configurarTablas();
		Eventos();
		cargarDeportes();
		
		view.setVisible(true);
	}
	
	private void configurarTablas() {
		view.gettablaSocios().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		view.gettablaDias().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		view.gettablaHoras().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
	
	private void Eventos() {
		//Buscar socios
		view.getBuscar().addActionListener(e -> onBuscarSocios());
		
		//Seleccionar socio
		view.gettablaSocios().getSelectionModel().addListSelectionListener(e->onSeleccionSocio(e));
		
		//Cada vez que cambia deporte, carga instalaciones
		view.getcbDeporte().addActionListener(e-> onDeporteSeleccionado());
		
		//Cada vez que cambia la instalacion, carga dias
		view.getcbPista().addActionListener(e -> onInstalacionSeleccionada());
		
		
		//Seleccionar dia, cargar horas del dia
		view.gettablaDias().getSelectionModel().addListSelectionListener(e -> onSeleccionDia(e));
		
		//Seleccionar hora y actualizar el resumen
		view.gettablaHoras().getSelectionModel().addListSelectionListener(e->onSeleccionHora(e));
		
		
		//Hacer la reserva
		view.getreservar().addActionListener(e->onReservar());
		
		
		//Cancelar
		view.getcancelar().addActionListener(e->view.dispose());
		
	}
	
	
	private void onBuscarSocios() {
		  if (view.gettablaSocios().isEditing()) {
		        view.gettablaSocios().getCellEditor().stopCellEditing();
		    }
		DefaultTableModel t = (DefaultTableModel) view.gettablaSocios().getModel();
		
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
		view.setResumenSocio("", "", "", "");
	}
	
	private void onSeleccionSocio(ListSelectionEvent e) {
		//para que el evento no se dispare dos veces
		if(e.getValueIsAdjusting()) return;
		
		int fila = view.gettablaSocios().getSelectedRow();
		if(fila<2) return; //evitar coger la fila 0 de busqueda y fila 1 separadora
		
		int indice = fila-2;
		if(sociosMostrados == null || indice<0 || indice >= sociosMostrados.size()) return; //evitar salir de la tabla
		
		
		socioSeleccionado = sociosMostrados.get(indice);
		view.setResumenSocio(
			    socioSeleccionado.getNombre()+ " " + socioSeleccionado.getApellidos(),
			    socioSeleccionado.getNumSocio(),
			    socioSeleccionado.getEmail(),
			    socioSeleccionado.getTelefono()
			);
	}
	
	
	private void onDeporteSeleccionado() {
		Object elementoseleccionado = view.getcbDeporte().getSelectedItem();
		
		deporteSeleccionado = elementoseleccionado.toString();
		
		
		instalacionSeleccionada = null;
		idInstalacionSeleccionada = null;
		diaSeleccionado = null;
		horasSeleccionadas = null;
		
		
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
		view.setResumenReserva(
			    deporteSeleccionado == null ? "" : deporteSeleccionado,
			    "", "", "", ""
			);
			
	}
	
	private void onInstalacionSeleccionada() {
		Object seleccionada = view.getcbPista().getSelectedItem();
		
		instalacionSeleccionada = seleccionada.toString();
		
		diaSeleccionado = null;
		horasSeleccionadas = null;
		
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

		int fila = view.gettablaDias().getSelectedRow();
		if (fila < 0) return;

		DefaultTableModel t = (DefaultTableModel) view.gettablaDias().getModel();
		String fecha = String.valueOf(t.getValueAt(fila, 1));
		String estado   = String.valueOf(t.getValueAt(fila, 2));

		//si el día es no reservable o está completo, no se cargan horas
		if ("NO_RESERVABLE".equalsIgnoreCase(estado) || "COMPLETO".equalsIgnoreCase(estado)) {
			limpiarTodo(view.gettablaHoras());
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
		DefaultTableModel th = (DefaultTableModel) view.gettablaHoras().getModel();
		limpiarTodo(view.gettablaHoras());

		for (HoraReservaSocioDTO h : horas) {
			th.addRow(new Object[] { h.getHoraInicio(), h.getHoraFin(), h.getEstado(), h.getMotivo() });
		}

		view.gettablaHoras().clearSelection();

	}
	
	private void onSeleccionHora(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;

		int[] filas = view.gettablaHoras().getSelectedRows();
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

		DefaultTableModel t = (DefaultTableModel) view.gettablaHoras().getModel();
		List<LocalTime> horas = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<filas.length;i++) {
			int fila = filas[i];
			String estado= String.valueOf(t.getValueAt(fila, 2));
			
			//Si no está libre no se puede seleccionar
			if (!"LIBRE".equalsIgnoreCase(estado)) {
				view.gettablaHoras().clearSelection();
				horasSeleccionadas = null;
				JOptionPane.showMessageDialog(view, "Solo se pueden seleccionar horas libres");
				return;
			}
			
			String horaInicio = String.valueOf(t.getValueAt(fila, 0));
			String horaFin = String.valueOf(t.getValueAt(fila, 1));
			
			horas.add(LocalTime.parse(horaInicio));
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
		if(socioSeleccionado == null) {
			JOptionPane.showMessageDialog(view, "Tienes que seleccionar un socio");
			return;
		}
		
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
		
		try {
			model.crearReserva(idInstalacionSeleccionada, socioSeleccionado.getIdSocio(), diaSeleccionado, horasSeleccionadas);
			JOptionPane.showMessageDialog(view, "Reserva realizada.");
			view.dispose();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "No se pudo reservar", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
