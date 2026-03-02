package giis.sisinfo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;

import giis.sisinfo.dto.InstalacionDTO;
import giis.sisinfo.dto.ReservaAdminDTO;
import giis.sisinfo.model.ReservaInstalacionAdminModel;
import giis.sisinfo.view.ReservaInstalacionAdminView;

public class ReservaInstalacionAdminController {

	private ReservaInstalacionAdminView view;
	private ReservaInstalacionAdminModel model;
	
	private List<String> listaInstalaciones;
	JComboBox miSelectorInstalaciones;
	
	public ReservaInstalacionAdminController(ReservaInstalacionAdminView nview, ReservaInstalacionAdminModel nmodel) {
		this.view = nview;
		this.model = nmodel;
		listaInstalaciones = this.getNombreInstalaciones();
		initSelectorInstalaciones();
		initView();
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
	
	public void initView() {
		view.setVisible(true);
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
		
		if (listaConflictos.size()<=0) {
			System.out.printf("ReservaInstalacionAdminController | No se han detectado conflictos");
			view.getPanelConflictos().setText("");
			return;
		}
		
		System.out.printf("ReservaInstalacionAdminController | Se han detectado %d conflictos",listaConflictos.size());
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
	
	
	public void initController() {
		//lanza comprobarConflictos() cuando se modifica el panel de seleccionar hora
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
		
	}
}
