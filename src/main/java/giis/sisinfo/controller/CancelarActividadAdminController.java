package giis.sisinfo.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

import giis.sisinfo.dto.ActividadCanceladaDTO;
import giis.sisinfo.dto.InstalacionDTO;
import giis.sisinfo.model.CancelarActividadAdminModel;
import giis.sisinfo.view.CancelarActividadAdminView;

public class CancelarActividadAdminController {
	CancelarActividadAdminView view;
	CancelarActividadAdminModel model;
	
	List<InstalacionDTO> instalaciones;
	List<ActividadCanceladaDTO> actividades;

	public CancelarActividadAdminController(CancelarActividadAdminView v, CancelarActividadAdminModel m) {
		// TODO Auto-generated constructor stub
		view = v;
		model = m;
		
		actualizarInstalaciones();
		actualizarSelectorInstalaciones();
		actualizarListaActividades();
		actualizarTablaActividades();
		
		initView();
		initController();
	}
	
	public void initView() {
		view.setVisible(true);
	}
	
	public void initController() {
		
		//cuando se selecciona una instalacion
		view.getSelectorInstalaciones().addItemListener(e -> {
		    if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				actualizarListaActividades();
				actualizarTablaActividades();
		    }
		});
		
		//cuando se hace click en una fila de la tabla, se copian los datos de esa actividad
		view.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = view.getTable().getSelectedRow();
				System.out.println("CancelarActividadAdminController | Click en fila: "+fila);
				updateDetalles();
			}
		});
	}
	
	public void actualizarInstalaciones() {
		instalaciones = model.getInstalaciones();
	}
	
	public void actualizarSelectorInstalaciones() {
		JComboBox selectorInstalaciones = view.getSelectorInstalaciones();
		selectorInstalaciones.removeAllItems();
		for(int i = 0; i<instalaciones.size(); i++){
			InstalacionDTO instalacion = instalaciones.get(i);
			selectorInstalaciones.addItem(instalacion.getNombreInstalacion());
		}
	}
	
	public void actualizarListaActividades() {
		actividades = model.getActividades(view.getSelectorInstalaciones().getSelectedItem().toString());
	}
	
	public void actualizarTablaActividades() {
		DefaultTableModel tabla = (DefaultTableModel) view.getTable().getModel();
		tabla.setRowCount(0);
		for (int i = 0; i<actividades.size(); i++) {
			ActividadCanceladaDTO actividad = actividades.get(i);
			tabla.addRow(new Object[] {
					actividad.getIdActividad(),
					actividad.getNombreActividad(),
					actividad.getFechaInicio(),
					actividad.getFechaFin()
			});
		}
	}
	
	public void updateDetalles() {
		int filaSeleccionada = view.getTable().getSelectedRow();
		ActividadCanceladaDTO actividad = actividades.get(filaSeleccionada);
		String detalles = "Tipo de actividad: "+actividad.getTipoActividad()+"\n"
						+ "\n"
						+ ""
						+ "Aforo: "+actividad.getAforo()+"       Personas Apuntadas: "+actividad.getParticipantes()+"\n"
						+ "\n"
						+ "Duracion: "+actividad.getDuracion()+"\n"
						+ "\n"
						+ "Descripción:\n"
						+ actividad.getDescripcion()+"\n"
						;
		view.getDetallesActividad().setText(detalles);
	}
	

}
