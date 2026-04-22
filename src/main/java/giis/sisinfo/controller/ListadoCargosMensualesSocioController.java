package giis.sisinfo.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import giis.sisinfo.dto.CargosActividadesSocioDTO;
import giis.sisinfo.dto.CargosReservasSocioDTO;
import giis.sisinfo.dto.CargosSocioDTO;
import giis.sisinfo.model.ListadoCargosMensualesSocioModel;
import giis.sisinfo.session.Session;
import giis.sisinfo.util.DateConverter;
import giis.sisinfo.view.ListadoCargosMensualesSocioView;

public class ListadoCargosMensualesSocioController {
	
	private final ListadoCargosMensualesSocioModel model;
	private final ListadoCargosMensualesSocioView view;
	private Session sesion;
	
	private List<CargosActividadesSocioDTO> cargosActividades;
	private List<CargosReservasSocioDTO> cargosReservas;
	
	
	public ListadoCargosMensualesSocioController(ListadoCargosMensualesSocioModel m, ListadoCargosMensualesSocioView v) {
		model = m;
		view = v;
		sesion = Session.get();
		
		initView();
		initController();
	}
	
	public void initView() {
		view.setVisible(true);
	}
	
	public void initController() {
		view.getBotonBuscar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String año = view.getSelectorAño().getSelectedItem().toString();
				String mes = view.getSelectorMes().getSelectedItem().toString();
				mes = DateConverter.mesTextoANumero(mes);
				String cargos = view.getSelectorCargos().getSelectedItem().toString();
				int idSocio = sesion.getIdSocio();
								
				//System.out.println(cargos);
				switch(cargos) {
					//actividades
					case "Actividades":
						System.out.println("ListadoCargosMensualesSocioModel | Buscando cargos de Actividades para el socio "+ idSocio +" en el mes "+mes+" del año "+año);
						updateCargosActividades(año, mes, idSocio);
						updateTableActividades();
						cargosReservas=null;
						updatePendienteDePago();
						break;
					//reservas
					case "Reservas":
						System.out.println("ListadoCargosMensualesSocioModel | Buscando cargos de Reservas para el socio "+ idSocio +" en el mes "+mes+" del año "+año);
						updateCargosReservas(año, mes, idSocio);
						updateTableReservas();	
						cargosActividades=null;
						updatePendienteDePago();
						break;
				
					//ambos
					case "Ambos":
						System.out.println("ListadoCargosMensualesSocioModel | Buscando cargos de Ambos para el socio "+ idSocio +" en el mes "+mes+" del año "+año);
						updateCargosAmbos(año, mes, idSocio);
						updateTableAmbos();
						updatePendienteDePago();
						break;
					
					
					default:
						System.out.println("ListadoCargosMensualesSocioModel | Error: no hay ningun elemento seleccionado en el selector de cargos");
						break;
				}
			}
		});
	}
	
	public void updateCargosActividades(String año, String mes, int idSocio) {
		cargosActividades = model.getCargosActividadSocio(idSocio, año, mes);
	}
	
	public void updateTableActividades() {
		JTable table =  view.getTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"idActividad", "Nombre Actividad", "Concepto Pago", "Fecha", "Estado", "Importe"
			}
		));
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		for (int i = 0; i<cargosActividades.size(); i++) {
			CargosActividadesSocioDTO cargoActividad = cargosActividades.get(i);
			tableModel.addRow(new Object[] {
					cargoActividad.getIdActividad(),
					cargoActividad.getNombreActividad(),
					cargoActividad.getConceptoPago(),
					cargoActividad.getFecha(),
					cargoActividad.getEstado(),
					cargoActividad.getImporte()
			});
		}	
	}
	
	public void updateCargosReservas(String año, String mes, int idSocio) {
		cargosReservas = model.getCargosReservaSocio(idSocio, año, mes);
	}
	
	public void updateTableReservas() {
		JTable table =  view.getTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"idReserva", "Nombre Instalacion", "Concepto Pago", "Fecha", "Estado", "Importe"
			}
		));
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		for (int i = 0; i<cargosReservas.size(); i++) {
			CargosReservasSocioDTO cargoReserva = cargosReservas.get(i);
			tableModel.addRow(new Object[] {
					cargoReserva.getIdReserva(),
					cargoReserva.getNombreInstalacion(),
					cargoReserva.getConceptoPago(),
					cargoReserva.getFecha(),
					cargoReserva.getEstado(),
					cargoReserva.getImporte()
			});
		}	
	}
	
	public void updateCargosAmbos(String año, String mes, int idSocio) {
		updateCargosActividades(año, mes, idSocio);
		updateCargosReservas(año, mes, idSocio);
	}

	public void updateTableAmbos() {
		JTable table =  view.getTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"idReserva", "Nombre Instalacion", "idReserva", "Nombre Instalacion", "Concepto Pago", "Fecha", "Estado", "Importe"
			}
		));
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		for (int i = 0; i<cargosActividades.size(); i++) {
			CargosActividadesSocioDTO cargoActividad = cargosActividades.get(i);
			tableModel.addRow(new Object[] {
					cargoActividad.getIdActividad(),
					cargoActividad.getNombreActividad(),
					null,
					null,
					cargoActividad.getConceptoPago(),
					cargoActividad.getFecha(),
					cargoActividad.getEstado(),
					cargoActividad.getImporte()
			});
		}	
		for (int i = 0; i<cargosReservas.size(); i++) {
			CargosReservasSocioDTO cargoReserva = cargosReservas.get(i);
			tableModel.addRow(new Object[] {
					null, 
					null,
					cargoReserva.getIdReserva(),
					cargoReserva.getNombreInstalacion(),
					cargoReserva.getConceptoPago(),
					cargoReserva.getFecha(),
					cargoReserva.getEstado(),
					cargoReserva.getImporte()
			});
		}			
	}
	
	public void updatePendienteDePago() {
		double total=0.0;
		if (cargosActividades!=null) {
			for(int i=0; i<cargosActividades.size(); i++) {
				if(cargosActividades.get(i).getEstado().equals("PENDIENTE")) {
					total+=cargosActividades.get(i).getImporte();
				}
				if(cargosActividades.get(i).getEstado().equals("DEVUELTO")) {
					total-=cargosActividades.get(i).getImporte();
				}
			}
		}
		if (cargosReservas!=null) {
			for(int i=0; i<cargosReservas.size(); i++) {
				if(cargosReservas.get(i).getEstado().equals("PENDIENTE")) {
					total+=cargosReservas.get(i).getImporte();
				}
				if(cargosReservas.get(i).getEstado().equals("DEVUELTO")) {
					total-=cargosReservas.get(i).getImporte();
				}
			}
		}
		view.getTxtpnPendienteDePago().setText("Total pendiente de pago: "+total+"€");
	}
	
}
