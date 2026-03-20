package giis.sisinfo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import giis.sisinfo.dto.DiaReservaSocioDTO;
import giis.sisinfo.dto.HoraReservaSocioDTO;
import giis.sisinfo.dto.ResguardoReservaSocioDTO;
import giis.sisinfo.model.ReservaInstalacionSocioModel;
import giis.sisinfo.util.ResguardoReservaAdminSocioPdf;
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
		view.getTablaHoras().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

		int fila = view.getTablaDias().getSelectedRow();
		if (fila < 0) return;

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
}
