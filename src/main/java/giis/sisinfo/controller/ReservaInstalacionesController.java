package giis.sisinfo.controller;

import java.util.List;

import javax.swing.JOptionPane;

import giis.sisinfo.model.ReservaInstalacionesModel;
import giis.sisinfo.model.ReservaInstalacionesModel.ResultadoReserva;
import giis.sisinfo.view.ReservaInstalacionesView;

public class ReservaInstalacionesController {

	private ReservaInstalacionesView view;
	private ReservaInstalacionesModel model;

	public ReservaInstalacionesController(ReservaInstalacionesView view, ReservaInstalacionesModel model) {
		this.view = view;
		this.model = model;
		this.initView();
		this.initController();
	}

	public void initView() {
		cargarActividadesPendientes();
		view.setVisible(true);
	}

	public void initController() {
		view.getBtnCancelar().addActionListener(e -> cerrarVentana());
		view.getBtnReservar().addActionListener(e -> reservarInstalacionActividad());
	}

	private void cerrarVentana() {
		view.dispose();
	}

	private void cargarActividadesPendientes() {
		view.getComboBoxActividadesPendientes().removeAllItems();

		List<String> actividades = model.getActividadesPendientesDeReserva();

		for (String actividad : actividades) {
			view.getComboBoxActividadesPendientes().addItem(actividad);
		}

		if (actividades.isEmpty()) {
			JOptionPane.showMessageDialog(view,
				"No hay actividades pendientes de reserva.",
				"Información",
				JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void reservarInstalacionActividad() {
		String actividadSeleccionada = (String) view.getComboBoxActividadesPendientes().getSelectedItem();

		if (actividadSeleccionada == null || actividadSeleccionada.trim().isEmpty()) {
			JOptionPane.showMessageDialog(view,
				"Debes seleccionar una actividad.",
				"Validación",
				JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			view.limpiarTablaIncidencias();

			ResultadoReserva resultado = model.reservarInstalacionParaActividad(actividadSeleccionada);

			cargarIncidenciasEnTabla(resultado.getIncidencias());

			if (resultado.isConflictoConActividad()) {
				JOptionPane.showMessageDialog(view,
					"Se ha detectado un conflicto con otra actividad planificada.\n"
					+ "Consulta la lista de incidencias.",
					"Conflicto detectado",
					JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(view,
					"Reserva automática de instalación realizada correctamente.",
					"Operación completada",
					JOptionPane.INFORMATION_MESSAGE);
			}

			cargarActividadesPendientes();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(view,
				ex.getMessage(),
				"Error",
				JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarIncidenciasEnTabla(List<Object[]> incidencias) {
		for (Object[] fila : incidencias) {
			view.addIncidenciaRow(fila);
		}
	}
}