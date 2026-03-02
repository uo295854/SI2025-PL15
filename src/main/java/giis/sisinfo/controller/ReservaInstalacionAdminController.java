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

import giis.sisinfo.dto.ActividadDTO;
import giis.sisinfo.dto.InstalacionDTO;
import giis.sisinfo.dto.ReservaAdminDTO;
import giis.sisinfo.model.ReservaInstalacionAdminModel;
import giis.sisinfo.view.ReservaInstalacionAdminView;

public class ReservaInstalacionAdminController {

	private ReservaInstalacionAdminView view;
	private ReservaInstalacionAdminModel model;
	
	private List<String> listaInstalaciones;
	private List<ActividadDTO> listaActividades;
	JComboBox miSelectorInstalaciones;
	JComboBox miSelectorActividades;
	
	public ReservaInstalacionAdminController(ReservaInstalacionAdminView nview, ReservaInstalacionAdminModel nmodel) {
		this.view = nview;
		this.model = nmodel;
		listaInstalaciones = this.getNombreInstalaciones();
		initSelectorInstalaciones();
		initView();
		initController();
	}
	
	public void initView() {
		view.setVisible(true);
	}
	
	public void initController() {
		//lanza comprobarConflictos() cuando se modifica el panel de seleccionar fecha
		view.getSelectorFechaInicial().getDateEditor().addPropertyChangeListener("date", evt -> {
		    Date fechaSeleccionada = (Date) evt.getNewValue();

		    if (fechaSeleccionada != null) {
		        System.out.println("ReservaInstalacionAdminController | Fecha Inicial Seleccionada: " + fechaSeleccionada);
		    }
		    comprobarConflictos();
		});
		
		view.getSelectorFechaFinal().getDateEditor().addPropertyChangeListener("date", evt -> {
		    Date fechaSeleccionada = (Date) evt.getNewValue();

		    if (fechaSeleccionada != null) {
		        System.out.println("ReservaInstalacionAdminController | Fecha Final Seleccionada: " + fechaSeleccionada);
		    }
		    comprobarConflictos();
		});
		
		view.getSelectorInstalaciones().addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ReservaInstalacionAdminController | Instalación seleccionada: " + view.getSelectorInstalaciones().getSelectedItem().toString());
				comprobarConflictos();
				actualizarActividadesDisponibles();	
			}
		});
		
		view.getCampoActividad().addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("ReservaInstalacionAdminController | Actividad seleccionada: " + view.getCampoActividad().getSelectedItem().toString());
				actualizarDetallesActividad();
				
			}
		});
		
		//botón de reservar
		view.getBotonReservar().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (comprobarReservaValida()) {
					try {
						reservarInstalacion();
					} catch (Exception e1){
						JOptionPane.showMessageDialog(null, "Error: No existe una actividad con ese nombre");
						throw new RuntimeException("ReservaInstalacionAdminController | Error comprobando actividad, no existe una actividad con ese nombre", e1);		
					}
					
				}
			}
		});
		
	}
	
	public void initSelectorInstalaciones() {
		System.out.println("Cantidad de Instalaciones: " + listaInstalaciones.size());
		miSelectorInstalaciones = view.getSelectorInstalaciones();
		miSelectorInstalaciones.removeAllItems();
		for (int i = 0; i<listaInstalaciones.size(); i++) {
			miSelectorInstalaciones.addItem(listaInstalaciones.get(i));
			System.out.println("Instalación " +i +": " + listaInstalaciones.get(i));
		}
	}
	
	public List<String> getNombreInstalaciones() {
		List<InstalacionDTO> listaInstalaciones = model.getInstalaciones();
		List<String> aux = new ArrayList<String>();
		if (listaInstalaciones.size()<=0) {
			return null;
		}
		for (int i = 0; i<listaInstalaciones.size(); i++) {
			aux.add(listaInstalaciones.get(i).getNombreInstalacion());
		}
		return aux;	
	}
	
	public List<ActividadDTO> getActividadesDisponibles(){
		String instalacionSeleccionada = view.getSelectorInstalaciones().getSelectedItem().toString();
		return model.getActividadesEnInstalacion(instalacionSeleccionada);
	} 
	
	public void actualizarActividadesDisponibles() {
		miSelectorActividades = view.getCampoActividad();
		miSelectorActividades.removeAllItems();
		listaActividades = getActividadesDisponibles();
		if (listaActividades.size()<=0) {
			System.out.println("ReservaInstalacionAdminController | No hay actividades disponibles para esta instalación");
			return;
		}
		for (int i = 0; i<getActividadesDisponibles().size(); i++) {
			miSelectorActividades.addItem(listaActividades.get(i).getNombre());
			System.out.println("Actividad " +i +": " + listaActividades.get(i).getNombre());
		}
	}
	
	public void actualizarDetallesActividad() {
		if (listaActividades.size()<=0) {
			return;
		}
		if (view.getCampoActividad().getSelectedIndex()<=0) {
			return;
		}
		view.getDetallesActividad().setText(listaActividades.get(view.getCampoActividad().getSelectedIndex()).getDescripcion());
	}
	
	
	public void comprobarConflictos() {
		System.out.println("ReservaInstalacionAdminController | Comprobando conflictos");
		
		String fechaInicial;
		String horaInicial;
		String fechaFinal;
		String horaFinal;
		String instalacion;
		
		
		//fecha inicial
		if (view.getSelectorFechaInicial().getDate() == null) {
			return;
		}
		fechaInicial = convertirFecha(view.getSelectorFechaInicial().getDate().toString());
		
		
		//fecha final
		if (view.getSelectorFechaFinal().getDate() == null) {
			return;
		}
		fechaFinal = convertirFecha(view.getSelectorFechaFinal().getDate().toString());
		
		//hora inicial
		if (view.getSelectorHoraInicial().getValue() == null) {
			return;
		}
		horaInicial = convertirHora(view.getSelectorHoraInicial().getValue().toString());
		
		
		//hora final
		if (view.getSelectorHoraFinal().getValue() == null) {
			return;
		}
		horaFinal = convertirHora(view.getSelectorHoraFinal().getValue().toString());
		
		
		//instalación
		if (view.getSelectorInstalaciones().getSelectedItem() == null) {
			return;
		}
		instalacion = view.getSelectorInstalaciones().getSelectedItem().toString();

		
		if (fechaFinal.isBlank() || fechaInicial.isBlank() || horaInicial.isBlank() || horaFinal.isBlank() || instalacion.isBlank()) {
			System.out.println("ReservaInstalacionAdminController | No estan rellenados todos los campos, no se comprueban conflictos");
			return;
		}
		
		System.out.printf("ReservaInstalacionAdminController | Valor de fechaInicial: '%s'\n",fechaInicial);
		System.out.printf("ReservaInstalacionAdminController | Valor de fechaFinal: '%s'\n",fechaFinal);
		System.out.printf("ReservaInstalacionAdminController | Valor de horaInicial: '%s'\n",horaInicial);
		System.out.printf("ReservaInstalacionAdminController | Valor de horaFinal: '%s'\n",horaFinal);
		System.out.printf("ReservaInstalacionAdminController | Valor de instalacion: '%s'\n",instalacion);
		
		String fechaHoraInicial = fechaInicial + " " + horaInicial;
		String fechaHoraFinal = fechaFinal + " " + horaFinal;
		
		List<ReservaAdminDTO> listaConflictos = model.getReservas(fechaHoraInicial, fechaHoraFinal, instalacion);
		
		System.out.println(listaConflictos.size());
		if (listaConflictos.size()<=0) {
			System.out.printf("ReservaInstalacionAdminController | No se han detectado conflictos\n");
			view.getPanelConflictos().setText("");
			return;
		}
		
		System.out.printf("ReservaInstalacionAdminController | Se han detectado %d conflictos\n",listaConflictos.size());
		String conflictos = "";
		for (int i = 0; i<listaConflictos.size(); i++) {
			ReservaAdminDTO reserva = listaConflictos.get(i);
			conflictos += String.format("Conflicto %d: %s\n"
										 + "	%s - %s\n\n", i+1, 
										 reserva.getNombreActividad(), 
										 reserva.getFechaHoraInicial(), 
										 reserva.getFechaHoraFinal());
		}
		
		view.getPanelConflictos().setText(conflictos);
	}
	
	public boolean comprobarReservaValida() {
		//comprobación 1: que las fechas sean válidas
		if (view.getSelectorFechaInicial().getDate() == null) {
			System.out.println("ReservaInstalacionAdminController | La fecha inicial es nula");
			JOptionPane.showMessageDialog(null, "Error: La fecha inicial está vacía");
			return false;
		}
		if (view.getSelectorFechaFinal().getDate() == null) {
			System.out.println("ReservaInstalacionAdminController | La fecha final es nula");
			JOptionPane.showMessageDialog(null, "Error: La fecha final está vacía");
			return false;
		}
		
		//comprobación 2: que la fecha inicial no sea mayor a la final
		Date fechaInicial = view.getSelectorFechaInicial().getDate();
		Date fechaFinal = view.getSelectorFechaFinal().getDate();
		if (fechaFinal.compareTo(fechaInicial)<0) {
			System.out.println("ReservaInstalacionAdminController | La fecha inicial es mayor a la fecha final");
			JOptionPane.showMessageDialog(null, "Error: La fecha final es anterior a la fecha inicial");
			return false;
		}
		
		//comprobación 3: si las fechas son iguales, que hora inicial no sea mayor a la final
		Date horaInicial = (Date) view.getSelectorHoraInicial().getValue();
		Date horaFinal = (Date) view.getSelectorHoraFinal().getValue();
		
		if (fechaFinal.compareTo(fechaInicial)==0) {
			if(horaFinal.compareTo(horaInicial)<0) {
				System.out.println("ReservaInstalacionAdminController | La hora inicial es mayor a la hora final");
				JOptionPane.showMessageDialog(null, "Error: La hora final es anterior a la hora inicial");
				return false;
			}
		}
		
		//comprobación 4: el nombre de la actividad no está vacío
		if (view.getCampoActividad().getSelectedItem().toString().isEmpty()) {
			System.out.println("ReservaInstalacionAdminController | El campo de la actividad está vacío");
			JOptionPane.showMessageDialog(null, "Error: El campo de nombre de la actividad está vacío");
			return false;
		}
		
		//pasa todas las comprobaciones
		return true;		
	}
	
	public void reservarInstalacion() {
		String fechaInicial = convertirFecha(view.getSelectorFechaInicial().getDate().toString());
		String fechaFinal = convertirFecha(view.getSelectorFechaFinal().getDate().toString());
		String horaInicial = convertirHora(view.getSelectorHoraInicial().getValue().toString());
		String horaFinal = convertirHora(view.getSelectorHoraFinal().getValue().toString());
		
		String fechaHoraInicial = fechaInicial + " " + horaInicial;
		String fechaHoraFinal = fechaFinal + " " + horaFinal;
		
		String instalacion = view.getSelectorInstalaciones().getSelectedItem().toString();
		
		String nombreActividad = view.getCampoActividad().getSelectedItem().toString();
		
		model.hacerReserva(fechaHoraInicial, fechaHoraFinal, instalacion, nombreActividad);
		System.out.println("ReservaInstalacionAdminController | Reserva realizada con éxito");
		JOptionPane.showMessageDialog(null, "Reserva realizada con éxito");
	}
	
	
    public static String convertirFecha(String textoFecha) {
        // Formato de la fecha inicial
        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern(
            "EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH
        );

        // Parseamos el string a ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(textoFecha, formatterEntrada);

        // Obtenemos solo la fecha
        LocalDate fecha = zonedDateTime.toLocalDate();

        // Formateamos a "yyyy-MM-dd"
        DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fecha.format(formatterSalida);
    }
    
    public static String convertirHora(String textoHora) {
    	// Formato de la fecha inicial
        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern(
            "EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH
        );
        
        // Parseamos el string a ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(textoHora, formatterEntrada);
        
     // Obtenemos solo la fecha
        LocalTime hora = zonedDateTime.toLocalTime();
        
        DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("HH:mm");
        return hora.format(formatterSalida);
    }
}
