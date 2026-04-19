package giis.sisinfo.controller;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import giis.sisinfo.model.CancelarReservaInstalacionSocioModel;
import giis.sisinfo.view.CancelarReservaInstalacionSocioView;

public class CancelarReservaInstalacionSocioController {

	private final CancelarReservaInstalacionSocioModel model;
	private final CancelarReservaInstalacionSocioView view;
	
	private final int idSocioActual;
	
	private List<Object[]> reservasMostradas;
	
	public CancelarReservaInstalacionSocioController(CancelarReservaInstalacionSocioModel model, CancelarReservaInstalacionSocioView view, int idSocioActual) {
		this.model = model;
		this.view = view;
		this.idSocioActual = idSocioActual;
		
		
		configurarTablas();
		Eventos();
		cargarReservasSocio();
		
		view.setVisible(true);
	}
	
	private void configurarTablas() {
		view.getTablaReservas().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

	}
	
	private void limpiarTodo(JTable t) {
		DefaultTableModel tm = (DefaultTableModel) t.getModel();
		tm.setRowCount(0);
	}
	
	public void Eventos() {
		
		//Cancelar reserva
		view.getCancelarReserva().addActionListener(e -> onCancelarReserva());
		
		//Atrás
		view.getAtras().addActionListener(e -> view.dispose());
		
		//Filtros en la tabla
		view.getTablaReservas().getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
		
		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {

			int columna = view.getTablaReservas().columnAtPoint(e.getPoint());

			if (columna == 0) {
				mostrarDesplegableFiltroDeporte(e);
			}
			else if (columna == 1) {
				mostrarDesplegableFiltroInstalacion(e);
			}
			else if (columna == 2) {
				mostrarDesplegableFiltroFechas(e);
			}
			else if (columna == 3) {
				mostrarDesplegableFiltroDia(e);
			}
		}
			
			
		});
	}
	
    private void cargarReservasSocio() {

        reservasMostradas = model.getReservasActivasSocio(idSocioActual);

        DefaultTableModel t = (DefaultTableModel) view.getTablaReservas().getModel();
        limpiarTodo(view.getTablaReservas());

        for (Object[] r : reservasMostradas) {
            t.addRow(new Object[] {
                    r[1], // deporte
                    r[2], // instalacion
                    r[3], // fecha
                    r[4], // día
                    r[5], // hora entrada
                    r[6]  // estado pago
            });
        }
        
        aplicarFiltrosTablaReservas();
    }
    
    
    
    private void onCancelarReserva() {

    	int[] filas = view.getTablaReservas().getSelectedRows();
    	if (filas == null || filas.length == 0) {
    		JOptionPane.showMessageDialog(view, "Tienes que seleccionar una reserva.");
    		return;
    	}

    	int canceladas = 0;
    	StringBuilder errores = new StringBuilder();

    	for (int filaVista : filas) {
    		int filaModelo = view.getTablaReservas().convertRowIndexToModel(filaVista);

    		try {
    			int idReserva = ((Number) reservasMostradas.get(filaModelo)[0]).intValue();
    			model.cancelarReserva(idReserva);

    			canceladas++;

    		} catch (Exception e) {
    			Object[] reserva = reservasMostradas.get(filaModelo);
    			errores.append("- ")
    				   .append(reserva[1]) //deporte
    				   .append(" | ")
    				   .append(reserva[2]) //instalación
    				   .append(" | ")
    				   .append(reserva[3]) //fecha
    				   .append(" | ")
    				   .append(reserva[5]) //hora entrada
    				   .append(": ")
    				   .append(e.getMessage())
    				   .append("\n");
    		}
    	}

    	cargarReservasSocio();

    	if (canceladas > 0 && errores.length() == 0) {
    		JOptionPane.showMessageDialog(view, "Se han cancelado correctamente " + canceladas + " reservas");
    	} else if (canceladas > 0) {
    		JOptionPane.showMessageDialog(view,
    				"Se cancelaron " + canceladas + " reservas\n\n"
    				+ "Pero no se pudieron cancelar estas:\n" + errores.toString(),
    				"Cancelación parcial",
    				JOptionPane.WARNING_MESSAGE);
    	} else {
    		JOptionPane.showMessageDialog(view,
    				"No se pudo cancelar ninguna reserva\n\n" + errores.toString(),
    				"Cancelación no realizada",
    				JOptionPane.ERROR_MESSAGE);
    	}

    	 view.dispose();
    }
    
	 private String filtroDiaSemana = "Todos";

	 private void mostrarDesplegableFiltroDia(java.awt.event.MouseEvent e) {
	     JPopupMenu menu = new JPopupMenu();

	     String[] opciones = {"Todos","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};

	     for (String op : opciones) {
	         JMenuItem item = new JMenuItem(op);
	         item.addActionListener(a -> {
	             filtroDiaSemana = op;
	             aplicarFiltrosTablaReservas();
	         });
	         menu.add(item);
	     }

	     menu.show(e.getComponent(), e.getX(), e.getY());
	 }
	 
	 private String filtroRango = "Próximos 30 días";

	 private void mostrarDesplegableFiltroFechas(java.awt.event.MouseEvent e) {
	     JPopupMenu menu = new JPopupMenu();

	     String[] opciones = {"Hoy","Próximos 7 días","Próximos 15 días","Próximos 30 días"};

	     for (String op : opciones) {
	         JMenuItem item = new JMenuItem(op);
	         item.addActionListener(a -> {
	             filtroRango = op;
	             aplicarFiltrosTablaReservas();
	         });
	         menu.add(item);
	     }

	     menu.show(e.getComponent(), e.getX(), e.getY());
	 }
	
	 private String filtroDeporte = "Todos";

	 private void mostrarDesplegableFiltroDeporte(java.awt.event.MouseEvent e) {
			JPopupMenu menu = new JPopupMenu();

			JMenuItem itemTodos = new JMenuItem("Todos");
			itemTodos.addActionListener(a -> {
				filtroDeporte = "Todos";
				aplicarFiltrosTablaReservas();
			});
			menu.add(itemTodos);

			if (reservasMostradas != null) {
				List<String> deportes = reservasMostradas.stream()
						.map(r -> String.valueOf(r[1]))
						.distinct()
						.sorted()
						.toList();

				for (String dep : deportes) {
					JMenuItem item = new JMenuItem(dep);
					item.addActionListener(a -> {
						filtroDeporte = dep;
						aplicarFiltrosTablaReservas();
					});
					menu.add(item);
				}
			}

			menu.show(e.getComponent(), e.getX(), e.getY());
		}

	private String filtroInstalacion = "Todas";

	private void mostrarDesplegableFiltroInstalacion(java.awt.event.MouseEvent e) {
			JPopupMenu menu = new JPopupMenu();

			JMenuItem itemTodas = new JMenuItem("Todas");
			itemTodas.addActionListener(a -> {
				filtroInstalacion = "Todas";
				aplicarFiltrosTablaReservas();
			});
			menu.add(itemTodas);

			if (reservasMostradas != null) {
				List<String> instalaciones = reservasMostradas.stream()
						.map(r -> String.valueOf(r[2]))
						.distinct()
						.sorted()
						.toList();

				for (String inst : instalaciones) {
					JMenuItem item = new JMenuItem(inst);
					item.addActionListener(a -> {
						filtroInstalacion = inst;
						aplicarFiltrosTablaReservas();
					});
					menu.add(item);
				}
			}

			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	
	
	private void aplicarFiltrosTablaReservas() {

		TableRowSorter<DefaultTableModel> sorter = view.getOrdenarReservas();

		RowFilter<DefaultTableModel, Integer> filtro = new RowFilter<>() {
			@Override
			public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {

				String deporte = entry.getStringValue(0);
				String instalacion = entry.getStringValue(1);
				String fechaTexto = entry.getStringValue(2);

				LocalDate fecha = LocalDate.parse(fechaTexto);
				LocalDate hoy = LocalDate.now();

				int maxDias;

				switch (filtroRango) {
					case "Hoy":
						maxDias = 0;
						break;
					case "Próximos 7 días":
						maxDias = 6;
						break;
					case "Próximos 15 días":
						maxDias = 14;
						break;
					default:
						maxDias = 29;
				}

				if (fecha.isBefore(hoy) || fecha.isAfter(hoy.plusDays(maxDias))) {
					return false;
				}

				if (!filtroDiaSemana.equalsIgnoreCase("Todos")) {
					String diaES = diaSemana(fecha);

					if (!diaES.equalsIgnoreCase(filtroDiaSemana)) {
						return false;
					}
				}

				if (!filtroDeporte.equalsIgnoreCase("Todos")) {
					if (!deporte.equalsIgnoreCase(filtroDeporte)) {
						return false;
					}
				}

				if (!filtroInstalacion.equalsIgnoreCase("Todas")) {
					if (!instalacion.equalsIgnoreCase(filtroInstalacion)) {
						return false;
					}
				}

				return true;
			}
		};

		sorter.setRowFilter(filtro);
		view.getTablaReservas().clearSelection();
	}
	
	
	 private String diaSemana(LocalDate fecha) {

		    switch (fecha.getDayOfWeek()) {

		        case MONDAY:
		            return "Lunes";

		        case TUESDAY:
		            return "Martes";

		        case WEDNESDAY:
		            return "Miércoles";

		        case THURSDAY:
		            return "Jueves";

		        case FRIDAY:
		            return "Viernes";

		        case SATURDAY:
		            return "Sábado";

		        case SUNDAY:
		            return "Domingo";

		        default:
		            return "";
		    }
		}

}
