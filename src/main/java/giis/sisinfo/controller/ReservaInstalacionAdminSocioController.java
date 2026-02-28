package giis.sisinfo.controller;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import giis.sisinfo.dto.DiaReservaSocioDTO;
import giis.sisinfo.dto.HoraReservaSocioDTO;
import giis.sisinfo.dto.SocioDTO;
import giis.sisinfo.model.ReservaInstalacionAdminSocioModel;
import giis.sisinfo.view.ReservaInstalacionAdminSocioView;

public class ReservaInstalacionAdminSocioController {

	private final ReservaInstalacionAdminSocioModel model;
	private final ReservaInstalacionAdminSocioView view;


	private List<SocioDTO> sociosMostrados;
	private SocioDTO socioSeleccionado;

	private String deporteSeleccionado;
	private String instalacionSeleccionada;
	private Integer idInstalacionSeleccionada;

	private LocalDate diaSeleccionado;
	private LocalTime horaInicioSeleccionada;
	
	
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
		view.gettablaHoras().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		
	}
	
}
