package giis.sisinfo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

import giis.sisinfo.dto.ActividadCanceladaDTO;
import giis.sisinfo.dto.InstalacionDTO;
import giis.sisinfo.dto.SocioDTO;
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
		
		//cuando se presiona el botón de confirmar
		view.getBotonConfirmar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActividadCanceladaDTO actividad = actividades.get(view.getTable().getSelectedRow());
				int idActividad = actividad.getIdActividad();
				
				//avisar a todos los que estuviesen apuntados
				notificarSociosDeCancelación(actividad);
				
				//marcar como devueltos todos los pagos relacionados
				model.devolverPagosActividad(idActividad);
				
				//marcar actividad e inscripcion como canceladas
				model.cancelarActividad(idActividad);
				model.cancelarInscripciones(idActividad);
				
				System.out.println("Actividad Cancelada correctamente");
				//TODO mostrar una ventana de aviso diciendo que la actividad se canceló con éxito
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
	
	public void notificarSociosDeCancelación(ActividadCanceladaDTO actividad) {
		List<SocioDTO> sociosANotificar = model.getListaSociosANotificar(actividad.getIdActividad());
		String fileBase = "./notificaciones/ActividadesCanceladas/notificacion_";
		
		if (sociosANotificar.size()<1) {
			System.out.println("CancelarActividadAdminController | No habia socios a notificar");
			return;
		}
		
		for(int i = 0; i<sociosANotificar.size(); i++) {
			SocioDTO socio = sociosANotificar.get(i);
			String notificacion = getNotificacion(actividad, socio);
			try {
				File newFile = new File(fileBase+actividad.getIdActividad()+"_"+socio.getNombre()+"_"+socio.getApellidos()+".txt");
			
				if (!newFile.exists()){newFile.createNewFile(); }
			
				FileWriter fw = new FileWriter(newFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
				bw.write(notificacion);
				bw.close();
				
			} catch (IOException e) {
		        e.printStackTrace();
			}
		}

	}
	
	public String getNotificacion(ActividadCanceladaDTO actividad, SocioDTO socio) {
		return String.format(	"Estimado "+socio.getNombre()+" "+socio.getApellidos()+"\n"+
								"\n"+
								"Le informamos que la actividad "+actividad.getNombreActividad()+" con ID "+actividad.getIdActividad()+"\n"+
								"que tenia pensado celebrarse el día "+actividad.getFechaInicio()+" ha sido cancelada\n"+
								"\n"+
								"Motivo: "+view.getSelectorRazonCancelacion().getSelectedItem().toString()+"\n"+
								view.getDetallesCancelacion().getText()+"\n"+
								"\n"+
								"El pago de su inscripción, en caso de haberlo, será reembolsado en breve\n"+
								"\n"+
								"Pedimos disculpas por las molestias"+
								"\n"+
								"El equipo del Centro Deportivo"+
								"\n"+
								"Correo enviado a las: "+LocalTime.now()+" del "+LocalDate.now()
				);
	}

}
