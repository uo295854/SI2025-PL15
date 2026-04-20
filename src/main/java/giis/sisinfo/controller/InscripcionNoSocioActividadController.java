package giis.sisinfo.controller;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import giis.sisinfo.model.InscripcionNoSocioActividadModel;
import giis.sisinfo.model.InscripcionNoSocioActividadModel.ActividadDetalle;
import giis.sisinfo.model.InscripcionNoSocioActividadModel.ActividadItem;
import giis.sisinfo.model.InscripcionNoSocioActividadModel.FechaItem;
import giis.sisinfo.model.InscripcionNoSocioActividadModel.NoSocioItem;
import giis.sisinfo.view.InscripcionNoSocioActividadView;

public class InscripcionNoSocioActividadController {

	private final InscripcionNoSocioActividadView view;
	private final InscripcionNoSocioActividadModel model;

	public InscripcionNoSocioActividadController(InscripcionNoSocioActividadView view,
			InscripcionNoSocioActividadModel model) {
		this.view = view;
		this.model = model;

		initController();
		cargarDatosIniciales();
	}

	private void initController() {
		view.getBtnCancelar().addActionListener(e -> view.close());

		view.getComboBoxActividad().addActionListener(e -> cargarFechasYDetalleActividad());
		view.getComboBoxFecha().addActionListener(e -> actualizarDetalleActividad());
		view.getComboBoxNoSocioExistente().addActionListener(e -> actualizarNombreNoSocioExistente());

		view.getRdbtnNoSocioExistente().addActionListener(e -> actualizarModoVista());
		view.getRdbtnNuevoNoSocio().addActionListener(e -> actualizarModoVista());

		view.getBtnInscribir().addActionListener(e -> inscribirNoSocio());
	}

	private void cargarDatosIniciales() {
		cargarActividades();
		cargarNoSocios();
		actualizarModoVista();
	}

	private void cargarActividades() {
		try {
			List<ActividadItem> actividades = model.getActividadesDisponiblesParaNoSocio();

			DefaultComboBoxModel<ActividadItem> combo = new DefaultComboBoxModel<>();
			for (ActividadItem item : actividades) {
				combo.addElement(item);
			}
			view.getComboBoxActividad().setModel((DefaultComboBoxModel) combo);

			if (!actividades.isEmpty()) {
				view.getComboBoxActividad().setSelectedIndex(0);
				cargarFechasYDetalleActividad();
			} else {
				limpiarDetalleActividad();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarNoSocios() {
		try {
			List<NoSocioItem> noSocios = model.getNoSocios();

			DefaultComboBoxModel<NoSocioItem> combo = new DefaultComboBoxModel<>();
			for (NoSocioItem item : noSocios) {
				combo.addElement(item);
			}
			view.getComboBoxNoSocioExistente().setModel((DefaultComboBoxModel) combo);

			if (!noSocios.isEmpty()) {
				view.getComboBoxNoSocioExistente().setSelectedIndex(0);
				actualizarNombreNoSocioExistente();
			} else {
				view.setNombreExistente("");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarFechasYDetalleActividad() {
		try {
			ActividadItem actividad = getActividadSeleccionada();
			if (actividad == null) {
				limpiarDetalleActividad();
				return;
			}

			List<FechaItem> fechas = model.getFechasDeActividad(actividad.getIdActividad());

			DefaultComboBoxModel<FechaItem> comboFechas = new DefaultComboBoxModel<>();
			for (FechaItem item : fechas) {
				comboFechas.addElement(item);
			}
			view.getComboBoxFecha().setModel((DefaultComboBoxModel) comboFechas);

			if (!fechas.isEmpty()) {
				view.getComboBoxFecha().setSelectedIndex(0);
				actualizarDetalleActividad();
			} else {
				view.setDatosActividad("", "", "", "", "", "");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void actualizarDetalleActividad() {
		try {
			ActividadItem actividad = getActividadSeleccionada();
			FechaItem fecha = getFechaSeleccionada();

			if (actividad == null || fecha == null) {
				limpiarDetalleActividad();
				return;
			}

			ActividadDetalle detalle = model.getDetalleActividad(actividad.getIdActividad(), fecha.getFechaDb());

			view.setDatosActividad(
				detalle.getInstalacion(),
				detalle.getHorario(),
				detalle.getDuracion(),
				String.valueOf(detalle.getPlazasDisponibles()),
				String.valueOf(detalle.getCoste()),
				detalle.getPeriodoInscripcion()
			);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void actualizarNombreNoSocioExistente() {
		try {
			NoSocioItem noSocio = getNoSocioSeleccionado();
			if (noSocio == null) {
				view.setNombreExistente("");
				return;
			}

			view.setNombreExistente(noSocio.getNombre());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void actualizarModoVista() {
		if (view.isModoNoSocioExistente()) {
			actualizarNombreNoSocioExistente();
		} else {
			view.limpiarNuevoNoSocio();
		}
	}

	private void inscribirNoSocio() {
		try {
			ActividadItem actividad = getActividadSeleccionada();
			FechaItem fecha = getFechaSeleccionada();

			if (actividad == null) {
				JOptionPane.showMessageDialog(view, "Debes seleccionar una actividad.");
				return;
			}

			if (fecha == null) {
				JOptionPane.showMessageDialog(view, "Debes seleccionar una fecha.");
				return;
			}

			if (view.isModoNoSocioExistente()) {
				NoSocioItem noSocio = getNoSocioSeleccionado();
				if (noSocio == null) {
					JOptionPane.showMessageDialog(view, "Debes seleccionar un no socio existente.");
					return;
				}

				model.inscribirNoSocioExistente(
					actividad.getIdActividad(),
					fecha.getFechaDb(),
					noSocio.getIdNoSocio()
				);

			} else {
				String nombre = view.getTextFieldNombreNuevo().getText().trim();
				String dni = view.getTextFieldDniNuevo().getText().trim();

				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(view, "Debes introducir el nombre del no socio.");
					return;
				}

				if (dni.isEmpty()) {
					JOptionPane.showMessageDialog(view, "Debes introducir el DNI del no socio.");
					return;
				}

				model.inscribirNuevoNoSocio(
					actividad.getIdActividad(),
					fecha.getFechaDb(),
					nombre,
					dni
				);
			}

			JOptionPane.showMessageDialog(view, "Inscripción realizada correctamente. Se ha generado el recibo del pago.");
			cargarDatosIniciales();
			cargarFechasYDetalleActividad();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private ActividadItem getActividadSeleccionada() {
		Object selected = view.getComboBoxActividad().getSelectedItem();
		return (selected instanceof ActividadItem) ? (ActividadItem) selected : null;
	}

	private FechaItem getFechaSeleccionada() {
		Object selected = view.getComboBoxFecha().getSelectedItem();
		return (selected instanceof FechaItem) ? (FechaItem) selected : null;
	}

	private NoSocioItem getNoSocioSeleccionado() {
		Object selected = view.getComboBoxNoSocioExistente().getSelectedItem();
		return (selected instanceof NoSocioItem) ? (NoSocioItem) selected : null;
	}

	private void limpiarDetalleActividad() {
		view.setDatosActividad("", "", "", "", "", "");
	}
}