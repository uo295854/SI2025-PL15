package giis.sisinfo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import giis.sisinfo.dto.ActividadesOfertadasDTO;
import giis.sisinfo.model.ActividadesOfertadasModel;
import giis.sisinfo.view.ActividadesOfertadasView;

public class ActividadesOfertadasController {
	
	private final ActividadesOfertadasView view;
	private final ActividadesOfertadasModel model;
	
	List<ActividadesOfertadasDTO> listaActividades;
	
	public ActividadesOfertadasController(ActividadesOfertadasView nview, ActividadesOfertadasModel nmodel){
		view = nview;
		model = nmodel;
		listaActividades = new ArrayList<>();
		
	}

	private void rellenarActividadesOfertadasEjemplo() {
		listaActividades = new ArrayList<>();
		for (int i = 0; i<10; i++) {
			listaActividades.set(i, new ActividadesOfertadasDTO(
					"Nombre Actividad "+i, "Tipo Actividad "+i, "Instalación "+i, 
					LocalDateTime.now(), LocalDateTime.now(), 64, 0, 15, 
					"Descripcion Actividad "+i));
		}						
	}
	
	
	public void getListaActividades() {
		JTable table = view.getTable();
		DefaultTableModel modelo = (DefaultTableModel) table.getModel();
		modelo.setRowCount(0);
		if ((listaActividades==null) || listaActividades.isEmpty()){
			return;
		}
		for (int i = 0; i<listaActividades.size(); i++) {
			ActividadesOfertadasDTO actividad = listaActividades.get(i);
			modelo.addRow(new Object[] {
					actividad.getNombreActividad(),
					actividad.getTipoActividad(),
					actividad.getInstalacion(),
					actividad.getFechaInicial(),
					actividad.getFechaInicial(),
					actividad.getPlazas(),
					actividad.getPrecioSocios(),
					actividad.getPrecioNoSocios()
					});
		}
		view.setTable(table);
	}
	
	public void getDetallesActividad(int index) {
		JTextPane panel = view.getDetallesActividad();
		panel.setText(listaActividades.get(index).getDetalles());
		view.setDetallesActividad(panel);
	}
	

}
