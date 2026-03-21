package giis.sisinfo.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.opencsv.CSVWriter;

import giis.sisinfo.dto.CargosMensualesDTO;
import giis.sisinfo.model.ListadoCargosMensualesModel;
import giis.sisinfo.util.DateConverter;
import giis.sisinfo.view.ListadoCargosMensualesView;


public class ListadoCargosMensualesController {
	
	private final ListadoCargosMensualesModel model;
	private final ListadoCargosMensualesView view;
	int añoSeleccionado;
	String mesSeleccionado;
	
	private List<CargosMensualesDTO> cargosMensuales;
	
	public ListadoCargosMensualesController(ListadoCargosMensualesModel nmodel, ListadoCargosMensualesView nview) {
		model = nmodel;
		view = nview;
		
		initView();
		initController();
	}
	
	public void initView() {
		view.setVisible(true);
	}
	
	public void initController() {
		
		//botón de buscar
		view.getBotonBuscar().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("ListadoCargosMensualesController | Botón de buscar presionado");
				añoSeleccionado = Integer.valueOf(view.getSelectorAño().getSelectedItem().toString());
				mesSeleccionado = DateConverter.mesTextoANumero(view.getSelectorMes().getSelectedItem().toString());
				System.out.println("ListadoCargosMensualesController | Mes: "+ mesSeleccionado + " Año: " +añoSeleccionado);
				
				cargosMensuales=model.getListadoCargosMensuales(añoSeleccionado,mesSeleccionado);
				System.out.println("ListadoCargosMensualesController | Cantidad de cargos a clientes: "+cargosMensuales.size());
				
				DefaultTableModel modelo = (DefaultTableModel) view.getTable().getModel();
				modelo.setRowCount(0);
				if (cargosMensuales==null) {
					return;
				}
				
				for (int i = 0; i<cargosMensuales.size(); i++) {
					CargosMensualesDTO cargo = cargosMensuales.get(i);
					System.out.println("ListadoCargosMensualesController | Cliente: "+i+ " Cargos Actividades: "+cargo.getCargosActividades()+" Cargo Reservas: "+cargo.getCargosReservas());
					modelo.addRow(new Object[] {
							cargo.getNumSocio(),
							cargo.getNombreSocio(),
							cargo.getDNI(),
							cargo.getCargosReservas(),
							cargo.getCargosActividades(),
							cargo.getCargosActividades()+cargo.getCargosReservas()
					});	
				}	
			}
		});
		
		
		//botón de exportar a .CSV
		view.getBotonExportarCSV().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("ListadoCargosMensualesController | Botón de exportar a CSV presionado");
				
				if (cargosMensuales==null) {
					System.out.println("ListadoCargosMensualesController | Hay que cargar primero los datos con el botón buscar");
					JOptionPane.showMessageDialog(view, "Hay que seleccionar y buscar un periodo antes de exportar como .CSV.");
					return;
				}
				
				
				String CSV_FILE_PATH = "./cargos-clientes-"+añoSeleccionado+"-"+mesSeleccionado+".csv";
				File file = new File(CSV_FILE_PATH);
				
				List<String[]> data = new ArrayList<String[]>();
				
				data.add(new String[] {"Numero de Socio","Nombre de Socio","DNI","CargosReservas","CargosActividades","CargosTotales"});
				for(int i = 0; i<cargosMensuales.size(); i++) {
					CargosMensualesDTO cargo = cargosMensuales.get(i);
					data.add(new String[] {
							String.valueOf(cargo.getNumSocio()),
							cargo.getNombreSocio(),
							cargo.getDNI(),
							String.valueOf(cargo.getCargosReservas()),
							String.valueOf(cargo.getCargosActividades()),
							String.valueOf(cargo.getCargosReservas()+cargo.getCargosActividades())});
				}
				
				try {
					//creamos un escritor de archivo
					FileWriter outputfile = new FileWriter(file);
					
					//creamos un escritor csv a partir del escritor de archivo
					CSVWriter writer = new CSVWriter(outputfile);
					
					//escribimos los datos
					writer.writeAll(data);
					
					//cerramos el escritor
					writer.close();
					outputfile.close();			
				}catch (IOException exception) {
					exception.printStackTrace();
				}
				
				System.out.println("ListadoCargosMensualesController | Archivo .csv escrito con éxito, ruta "+file.getAbsolutePath());
				JOptionPane.showMessageDialog(view, "Archivo .csv escrito con éxito, ruta "+file.getAbsolutePath());
	
			}
	
		});
	}

}
