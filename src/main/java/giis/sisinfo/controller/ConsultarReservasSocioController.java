package giis.sisinfo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import giis.sisinfo.dto.ConsultaReservasSocioDTO;
import giis.sisinfo.model.ConsultarReservasSocioModel;
import giis.sisinfo.session.Session;
import giis.sisinfo.util.DateConverter;
import giis.sisinfo.view.ConsultarReservasSocioView;

public class ConsultarReservasSocioController {
	
	private final ConsultarReservasSocioModel model;
	private final ConsultarReservasSocioView view;
	private Session sesion;
	private LocalDate fechaActual;
	
	public ConsultarReservasSocioController(ConsultarReservasSocioModel nmodel, ConsultarReservasSocioView nview) {
		model = nmodel;
		view = nview;
		sesion = Session.get();
		fechaActual = LocalDate.now();
		
		initView();
		initController();
	}
	
	public void initView() {
		view.setVisible(true);
		view.getSocioNombre().setText("Socio: "+sesion.getUsername());
		view.getTxtpnFechaActual().setText("Fecha Actual: "+fechaActual);
	}
	
	public void initController() {
		
		//checkbox "esta semana"
		view.getCheckBoxEstaSemana().addActionListener(e -> {
			if (view.getCheckBoxEstaSemana().isSelected()) {
				System.out.println("ConsultarReservasSocioController | getCheckBoxEstaSemana seleccionada");
				view.getCheckBoxEsteMes().setSelected(false);
				view.getCheckBoxPeriodoPersonalizado().setSelected(false);
				desactivarPeriodoPersonalizado();
			} else {
				System.out.println("ConsultarReservasSocioController | getCheckBoxEstaSemana deseleccionada");
			}
		});	
		
		//checkbox "este mes"
		view.getCheckBoxEsteMes().addActionListener(e -> {
			if (view.getCheckBoxEsteMes().isSelected()) {
				System.out.println("ConsultarReservasSocioController | getCheckBoxEsteMes seleccionada");
				view.getCheckBoxEstaSemana().setSelected(false);
				view.getCheckBoxPeriodoPersonalizado().setSelected(false);
				desactivarPeriodoPersonalizado();
			} else {
				System.out.println("ConsultarReservasSocioController | getCheckBoxEsteMes deseleccionada");
			}
		});	
		
		//checkbox "periodo personalizado"
		view.getCheckBoxPeriodoPersonalizado().addActionListener(e -> {
			if (view.getCheckBoxPeriodoPersonalizado().isSelected()) {
				System.out.println("ConsultarReservasSocioController | getCheckBoxPeriodoPersonalizado seleccionada");
				view.getCheckBoxEsteMes().setSelected(false);
				view.getCheckBoxEstaSemana().setSelected(false);
				activarPeriodoPersonalizado();
			} else {
				desactivarPeriodoPersonalizado();
				System.out.println("ConsultarReservasSocioController | getCheckBoxPeriodoPersonalizado deseleccionada");
			}
		});
		
		//botón buscar
		view.getBuscarButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//comprobar que al menos una checkbox está chequeada
				if (!(view.getCheckBoxEstaSemana().isSelected() ||
					  view.getCheckBoxEsteMes().isSelected() ||
					  view.getCheckBoxPeriodoPersonalizado().isSelected())) {
					System.out.println("ConsultarReservasSocioController | Se ha pulsado el botón de buscar sin haber seleccionado una checkbox");
					JOptionPane.showMessageDialog(null, "Error: No se ha seleccionado ningun periodo");
					return;
				}
				
				//comprobaciones de periodo personalizado
				if(view.getCheckBoxPeriodoPersonalizado().isSelected()) {
					//comprobar que los dos selectores de fechas estan seleccionados
					if (view.getDateChooserFechaFinal().getDate()==null || view.getDateChooserFechaInicial().getDate()==null) {
						System.out.println("ConsultarReservasSocioController | No se han seleccionado fecha final e inicial");
						JOptionPane.showMessageDialog(null, "Error: No has seleccionado fecha final e inicial");
						return;
					}
					
					//comprobar que la fecha final no es menor que la fecha inicial
					if (view.getDateChooserFechaFinal().getDate().compareTo(view.getDateChooserFechaInicial().getDate())<0) {
						System.out.println("ConsultarReservasSocioController | Periodo personalizado: la fecha final es anterior a la fecha inicial");
						JOptionPane.showMessageDialog(null, "Error: La fecha final seleccionada es anterior a la fecha inicial");
						return;
					}
				}
				
				//construcción y ejecución de la query
				int idUsuario = sesion.getIdSocio();
				String fechaInicial = "2000-01-01", fechaFinal = "2000-01-01"; //fechas placeholder para que java no se queje
				
				//periodo personalizado
				if (view.getCheckBoxPeriodoPersonalizado().isSelected()) {
					fechaInicial = DateConverter.convertirFecha(view.getDateChooserFechaInicial().getDate().toString());
					fechaFinal = DateConverter.convertirFecha(view.getDateChooserFechaFinal().getDate().toString());
				}
				
				//esta semana
				if(view.getCheckBoxEstaSemana().isSelected()) {
					fechaInicial=fechaActual.toString();
					fechaFinal=fechaActual.plusWeeks(1).toString();
				}
				
				//este mes
				if(view.getCheckBoxEsteMes().isSelected()) {
					fechaInicial=fechaActual.toString();
					fechaFinal=fechaActual.plusMonths(1).toString();
				}
				

				List<ConsultaReservasSocioDTO> listaInstalacionesReservadas = model.getReservasSocio(idUsuario, fechaInicial, fechaFinal);
				
				System.out.println("ConsultarReservasSocioController | Mostrando por pantalla las reservas del usuario con ID "+idUsuario+" entre las fechas "+fechaInicial+" y "+fechaFinal);
				
				DefaultTableModel tabla = (DefaultTableModel) view.getReservasTable().getModel();
				tabla.setRowCount(0);
				for(int i = 0; i<listaInstalacionesReservadas.size(); i++) {
					ConsultaReservasSocioDTO reserva = listaInstalacionesReservadas.get(i);
					tabla.addRow(new Object[] {
							reserva.getInstalacionReservada(),
							reserva.getFecha(),
							reserva.getHoraInicial(),
							reserva.getHoraFinal()
					});
					
				}
				
			}
		});
		
	}
	
	private void desactivarPeriodoPersonalizado() {
		view.getDateChooserFechaInicial().setEnabled(false);
		view.getDateChooserFechaFinal().setEnabled(false);
	}
	
	private void activarPeriodoPersonalizado(){
		view.getDateChooserFechaInicial().setEnabled(true);
		view.getDateChooserFechaFinal().setEnabled(true);
	}

}
