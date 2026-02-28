package giis.sisinfo.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
		
		initView();
		
		listaActividades = new ArrayList<>();
		this.rellenarActividadesOfertadasEjemplo();
		
	}
	
	
	public void initView() {
		view.setVisible(true); ;
	}

	//función para testear, se comentará una vez se implemente la base de datos 
	private void rellenarActividadesOfertadasEjemplo() {
		listaActividades = new ArrayList<>();
		for (int i = 0; i<10; i++) {
			listaActividades.add(i, new ActividadesOfertadasDTO(
					"Nombre Actividad "+i, "Tipo Actividad "+i, "Instalación "+i, 
					LocalDateTime.now(), LocalDateTime.now(), 64, 0, 15, 
					"Descripcion Actividad "+i));
		}
		setListaActividades();
	}
	
	
	public void initController() {
		//cuando se hace click en una fila de la tabla, se copian los detalles de esa actividad
		view.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = view.getTable().getSelectedRow();
				System.out.println("ActividadesOfertadasController | Click en fila: "+fila);
				view.setDetallesActividad(getDetallesActividadController(fila));
			}
		});
		
		
		//cuando se hace click en el botón de buscar, se buscan actividades en ese periodo
		view.getSearchButton().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int año = Integer.valueOf(view.getYearSelector().getSelectedItem().toString());
				String periodo = view.getPeriodSelector().getSelectedItem().toString();
				System.out.printf("ActividadesOfertadasController | Año: %d, Periodo: %s\n",año,periodo);
				//TODO: descomentar cuando este implementada la BBDD
				//listaActividades = model.getListaActividadesOfertadas(periodo,año);
				System.out.printf("ActividadesOfertadasController | Se han cargado %d actividades\n",listaActividades.size());
				setListaActividades();
			}
			
		});
		
		
	}
	
	
	public void setListaActividades() {
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
	
	public String getDetallesActividadController(int index) {
		return listaActividades.get(index).getDetalles();
	}
	

}
