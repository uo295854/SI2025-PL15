package giis.sisinfo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import giis.sisinfo.dto.ActividadDTO;
import giis.sisinfo.dto.InstalacionDTO;
import giis.sisinfo.dto.ReservaAdminDTO;
import giis.sisinfo.model.ReservaInstalacionAdminModel;
import giis.sisinfo.view.ReservaInstalacionAdminView;
import giis.sisinfo.util.DateConverter;

public class ReservaInstalacionAdminController {

	private ReservaInstalacionAdminView view;
	private ReservaInstalacionAdminModel model;
	
	private List<InstalacionDTO> listaInstalaciones;
	private List<ActividadDTO> listaActividades;
	private List<String> diasActividad;
	private List<ReservaAdminDTO> listaConflictos;
	JComboBox miSelectorInstalaciones;
	JComboBox miSelectorActividades;
	ActividadDTO actividadSeleccionada;
	
	public ReservaInstalacionAdminController(ReservaInstalacionAdminView nview, ReservaInstalacionAdminModel nmodel) {
		this.view = nview;
		this.model = nmodel;

		initView();
		initController();
	}
	
	public void initView() {
		view.setVisible(true);
		actualizarSelectorInstalaciones();
		actualizarSelectorActividades();
		actualizarSelectorFechas();
		actualizarDetalles();
		actualizarConflictos();	
	}
	
	public void initController() {

		//cuando se selecciona una instalación en el selector de instalaciones
		view.getSelectorInstalaciones().addItemListener(e -> {
		    if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
		        if (view.getSelectorInstalaciones().isPopupVisible()) {
		            actualizarSelectorActividades();
		            actualizarSelectorFechas();
		            actualizarDetalles();
		            actualizarConflictos();
		        }
		    }
		});
		
		//cuando se selecciona una actividad
		view.getSelectorActividad().addItemListener(e -> {
		    if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
		        if (view.getSelectorActividad().isPopupVisible()) {
		            actualizarSelectorFechas();
		            actualizarDetalles();
		            actualizarConflictos();
		        }
		    }
		});
		
		//cuando se selecciona una fecha
		view.getSelectorFechas().addItemListener(e -> {
		    if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
		        if (view.getSelectorFechas().isPopupVisible()) {
		        	actualizarConflictos();
		        }
		    }
		});
		
		
		//botón de reservar
		view.getBotonReservar().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String horaInicial, horaFinal, fecha, fechaHoraInicial, fechaHoraFinal, nombreActividad;
				horaInicial = DateConverter.convertirHora(view.getSelectorHoraInicial().getValue().toString());
				horaFinal = DateConverter.convertirHora(view.getSelectorHoraFinal().getValue().toString());
				fecha = view.getSelectorFechas().getSelectedItem().toString();
				
				if (horaInicial.compareTo(horaFinal)>0) {
					JOptionPane.showMessageDialog(view, "ERROR: La hora final es anterior a la hora inicial.");
					System.out.println("ERROR: La hora final es anterior a la hora inicial.");
					return;
				}
			
				fechaHoraInicial = fecha +" "+horaInicial;
				fechaHoraFinal = fecha +" "+horaFinal;
				nombreActividad=view.getSelectorActividad().getSelectedItem().toString();
				
				if(listaConflictos.size()>0) {
					model.eliminarReservasConflictivas(fechaHoraInicial, fechaHoraFinal);
					System.out.println("Se han eliminado "+listaConflictos.size()+" reservas conflictivas");
				}
				
				
				model.hacerReserva(fechaHoraInicial, fechaHoraFinal, nombreActividad);
				
				System.out.println("Reserva hecha con los siguientes valores:\n"
						 + "	fechaHoraInicial: "+fechaHoraInicial +"\n"
						 + "	fechaHoraFinal:   "+fechaHoraFinal+"\n"
						 + "	nombreActividad:  "+nombreActividad+"\n");
				
				JOptionPane.showMessageDialog(view, "Reserva realizada con éxito");
				System.out.println("ReservaInstalacionAdminController | Reserva realizada con éxito en la tabla Bloqueo_por_Actividad");
				actualizarConflictos();		
			}
		});
		
		
		view.getSelectorHoraInicial().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				actualizarConflictos();	
			}
        });
		
		view.getSelectorHoraFinal().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				actualizarConflictos();	
			}
        });
		
	}
	
	
	public void actualizarSelectorInstalaciones() {
		actualizarListaInstalaciones();
		view.getSelectorInstalaciones().removeAllItems();
		for(int i = 0; i<listaInstalaciones.size(); i++) {
			view.getSelectorInstalaciones().addItem(listaInstalaciones.get(i).getNombreInstalacion());
		}
		
	}
	
	public void actualizarListaInstalaciones() {
		listaInstalaciones=model.getInstalaciones();
		System.out.println("Cantidad de Instalaciones: " + listaInstalaciones.size());
	}
	
	public void actualizarSelectorActividades() {
		actualizarListaActividades();
		view.getSelectorActividad().removeAllItems();
		for (int i =0; i<listaActividades.size(); i++) {
			System.out.println(i+" "+listaActividades.get(i).getNombre());
			view.getSelectorActividad().addItem(listaActividades.get(i).getNombre());
		}
		
	}
	
	public void actualizarListaActividades() {
		listaActividades=model.getActividadesEnInstalacion(view.getSelectorInstalaciones().getSelectedItem().toString());
		System.out.println("Cantidad de Actividades: " + listaActividades.size());
	}
	
	public void actualizarSelectorFechas() {
		actualizarListaFechas();
		view.getSelectorFechas().removeAllItems();
		for (int i = 0; i<diasActividad.size(); i++) {
			view.getSelectorFechas().addItem(diasActividad.get(i));
		}
	}
	
	public void actualizarListaFechas() {
		ActividadDTO actividadSeleccionada;
		actividadSeleccionada = listaActividades.get(view.getSelectorActividad().getSelectedIndex());
		
		
		String fechaInicial = actividadSeleccionada.getFechaInicio();
		String fechaFinal = actividadSeleccionada.getFechaFin();
		
		diasActividad = new ArrayList<String>();
		
		for(String fechaActual=fechaInicial; !fechaActual.equals(fechaFinal);) {
			diasActividad.add(fechaActual);
			fechaActual = LocalDate.parse(fechaActual).plusDays(1).toString();
		}
		diasActividad.add(fechaFinal);
	}
	
	public void actualizarDetalles() {
		if (listaActividades.size()<=0) {
			System.out.println("ReservaInstalacionAdminController | No hay detalles para imprimir porque no hay actividades ofertadas");
			return;
		}
		if (view.getSelectorActividad().getSelectedIndex()<0) {
			System.out.println("ReservaInstalacionAdminController | No hay detalles para imprimir porque no hay actividades ofertadas");
			return;
		}
		view.getDetallesActividad().setText(listaActividades.get(view.getSelectorActividad().getSelectedIndex()).getDescripcion());
		System.out.println("ReservaInstalacionAdminController | Detalles de actividades mostrados por pantalla");
	}
	
	public void actualizarConflictos() {
		
		String fecha, horaInicio, horaFin, nombreInstalacion;
		
		fecha=view.getSelectorFechas().getSelectedItem().toString();
		horaInicio=DateConverter.convertirHora(view.getSelectorHoraInicial().getValue().toString());
		horaFin=DateConverter.convertirHora(view.getSelectorHoraFinal().getValue().toString());
		nombreInstalacion = view.getSelectorInstalaciones().getSelectedItem().toString();

		System.out.printf("ReservaInstalacionAdminController | Valor de fecha: '%s'\n",fecha);
		System.out.printf("ReservaInstalacionAdminController | Valor de horaInicial: '%s'\n",horaInicio);
		System.out.printf("ReservaInstalacionAdminController | Valor de horaFinal: '%s'\n",horaFin);
		System.out.printf("ReservaInstalacionAdminController | Valor de instalacion: '%s'\n",nombreInstalacion);
		
		String fechaHoraInicial = fecha + " " + horaInicio;
		String fechaHoraFinal = fecha + " " + horaFin;
		System.out.println("fechaHoraInicial: "+fechaHoraInicial+" fechaHoraFinal: "+fechaHoraFinal);
		
		listaConflictos = new ArrayList<ReservaAdminDTO>();
		listaConflictos = model.getReservas(fechaHoraInicial, fechaHoraFinal, nombreInstalacion);
		
		System.out.println(listaConflictos.size());
		if (listaConflictos.size()<=0) {
			System.out.printf("ReservaInstalacionAdminController | No se han detectado conflictos\n");
			view.getPanelConflictos().setText("");
			view.getTxtrAvisoConflictos().setVisible(false);
			return;
		}
		
		System.out.printf("ReservaInstalacionAdminController | Se han detectado %d conflictos\n",listaConflictos.size());
		String conflictos = "";
		for (int i = 0; i<listaConflictos.size(); i++) {
			ReservaAdminDTO reserva = listaConflictos.get(i);
			conflictos += String.format("Conflicto Nº%d con reserva ID: %s\n"
									  + "	%s - %s\n\n", i+1, 
										 reserva.getIdReserva(),
										 reserva.getFechaHoraInicial(), 
										 reserva.getFechaHoraFinal());
		}
		
		view.getPanelConflictos().setText(conflictos);
		view.getTxtrAvisoConflictos().setVisible(true);
		
	}
	
	
}
