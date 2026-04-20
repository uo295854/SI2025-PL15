package giis.sisinfo.controller;

import java.util.List;

import javax.swing.JOptionPane;

import giis.sisinfo.model.InscripcionActividadModel;
import giis.sisinfo.view.InscripcionActividadView;

public class InscripcionActividadController {

	private final InscripcionActividadView view;
	private final InscripcionActividadModel model;
	private final int idSocio;

	public InscripcionActividadController(InscripcionActividadView view, InscripcionActividadModel model, int idSocio) {
		this.view = view;
		this.model = model;
		this.idSocio = idSocio;

		initController();
		cargarActividades();
	}

	private void initController() {
		view.getBtnCancelar().addActionListener(e -> view.close());

		view.getCbActividad().addActionListener(e -> {
			if (view.getCbActividad().getSelectedItem() != null) {
				cargarFechasDeActividad();
			}
		});

		view.getCbFecha().addActionListener(e -> {
			if (view.getCbActividad().getSelectedItem() != null
					&& view.getCbFecha().getSelectedItem() != null) {
				cargarDatosSesion();
			}
		});

		view.getBtnInscribirse().addActionListener(e -> gestionarAccionActividad());
	}

	private void cargarActividades() {
		view.getCbActividad().removeAllItems();
		view.getCbFecha().removeAllItems();
		view.limpiarDatosActividad();
		view.getBtnInscribirse().setEnabled(false);
		view.getBtnInscribirse().setText("Inscribirse");

		List<String> actividades = model.getActividadesInscribiblesParaSocio(idSocio);

		if (actividades == null || actividades.isEmpty()) {
			JOptionPane.showMessageDialog(view,
					"No hay actividades disponibles para inscripción en este momento.");
			return;
		}

		for (String actividad : actividades) {
			view.getCbActividad().addItem(actividad);
		}

		if (view.getCbActividad().getItemCount() > 0) {
			view.getCbActividad().setSelectedIndex(0);
			cargarFechasDeActividad();
		}
	}

	private void cargarFechasDeActividad() {
		view.getCbFecha().removeAllItems();
		view.limpiarDatosActividad();
		view.getBtnInscribirse().setEnabled(false);
		view.getBtnInscribirse().setText("Inscribirse");

		String nombreActividad = (String) view.getCbActividad().getSelectedItem();
		if (nombreActividad == null || nombreActividad.trim().isEmpty()) {
			return;
		}

		List<String> fechas = model.getFechasDisponiblesDeActividad(nombreActividad);

		if (fechas == null || fechas.isEmpty()) {
			JOptionPane.showMessageDialog(view,
					"La actividad seleccionada no tiene sesiones disponibles.");
			return;
		}

		for (String fecha : fechas) {
			view.getCbFecha().addItem(fecha);
		}

		if (view.getCbFecha().getItemCount() > 0) {
			view.getCbFecha().setSelectedIndex(0);
			cargarDatosSesion();
		}
	}

	private void cargarDatosSesion() {
		String nombreActividad = (String) view.getCbActividad().getSelectedItem();
		String fechaSeleccionada = (String) view.getCbFecha().getSelectedItem();

		if (nombreActividad == null || fechaSeleccionada == null) {
			view.limpiarDatosActividad();
			view.getBtnInscribirse().setEnabled(false);
			view.getBtnInscribirse().setText("Inscribirse");
			return;
		}

		DatosSesionInscripcion datos = model.getDatosSesion(nombreActividad, fechaSeleccionada, idSocio);

		if (datos == null) {
			view.limpiarDatosActividad();
			view.getBtnInscribirse().setEnabled(false);
			view.getBtnInscribirse().setText("Inscribirse");
			JOptionPane.showMessageDialog(view,
					"No se pudieron cargar los datos de la sesión seleccionada.");
			return;
		}

		view.setInstalacion(datos.getInstalacion());
		view.setHorario(datos.getHorario());
		view.setPlazasDisponibles(String.valueOf(datos.getPlazasDisponibles()));
		view.setPeriodoInscripcion(datos.getEstadoPeriodo());

		String estadoSocio = model.getEstadoInscripcionSocio(idSocio, nombreActividad);

		if ("ACTIVA".equals(estadoSocio) || "EN_ESPERA".equals(estadoSocio)) {
			view.getBtnInscribirse().setText("Desinscribirse");
			view.getBtnInscribirse().setEnabled(true);
		} else {
			view.getBtnInscribirse().setText("Inscribirse");
			view.getBtnInscribirse().setEnabled(datos.isPeriodoActivo());
		}
	}

	private void gestionarAccionActividad() {
		String nombreActividad = (String) view.getCbActividad().getSelectedItem();
		String fechaSeleccionada = (String) view.getCbFecha().getSelectedItem();

		if (nombreActividad == null || fechaSeleccionada == null) {
			JOptionPane.showMessageDialog(view,
					"Debes seleccionar una actividad y una fecha.");
			return;
		}

		String estadoSocio = model.getEstadoInscripcionSocio(idSocio, nombreActividad);

		if ("ACTIVA".equals(estadoSocio) || "EN_ESPERA".equals(estadoSocio)) {
			boolean ok = model.cancelarInscripcionYPromover(idSocio, nombreActividad);

			if (ok) {
				JOptionPane.showMessageDialog(view,
						"Te has desinscrito correctamente de la actividad.");
				cargarActividades();
			} else {
				JOptionPane.showMessageDialog(view,
						"No se pudo realizar la desinscripción.");
			}
			return;
		}

		DatosSesionInscripcion datos = model.getDatosSesion(nombreActividad, fechaSeleccionada, idSocio);
		if (datos == null) {
			JOptionPane.showMessageDialog(view,
					"No se pudo validar la actividad seleccionada.");
			return;
		}

		if (!datos.isPeriodoActivo()) {
			JOptionPane.showMessageDialog(view,
					"El periodo de inscripción no está activo.");
			return;
		}

		boolean ok = model.inscribirSocioEnActividad(idSocio, nombreActividad, fechaSeleccionada);

		if (ok) {
			String estado = model.getEstadoInscripcionSocio(idSocio, nombreActividad);

			if ("ACTIVA".equals(estado)) {
				JOptionPane.showMessageDialog(view,
						"Inscripción realizada correctamente.\nEl pago se añadirá al recibo mensual del socio.");
			} else if ("EN_ESPERA".equals(estado)) {
				JOptionPane.showMessageDialog(view,
						"No había plazas disponibles.\nHas sido añadido a la lista de espera.");
			} else {
				JOptionPane.showMessageDialog(view,
						"Inscripción registrada correctamente.");
			}

			cargarActividades();
		} else {
			JOptionPane.showMessageDialog(view,
					"No se pudo realizar la inscripción.");
		}
	}

	public static class DatosSesionInscripcion {
		private String instalacion;
		private String horario;
		private int plazasDisponibles;
		private String estadoPeriodo;
		private boolean periodoActivo;

		public DatosSesionInscripcion(String instalacion, String horario, int plazasDisponibles,
				String estadoPeriodo, boolean periodoActivo) {
			this.instalacion = instalacion;
			this.horario = horario;
			this.plazasDisponibles = plazasDisponibles;
			this.estadoPeriodo = estadoPeriodo;
			this.periodoActivo = periodoActivo;
		}

		public String getInstalacion() {
			return instalacion;
		}

		public String getHorario() {
			return horario;
		}

		public int getPlazasDisponibles() {
			return plazasDisponibles;
		}

		public String getEstadoPeriodo() {
			return estadoPeriodo;
		}

		public boolean isPeriodoActivo() {
			return periodoActivo;
		}
	}
}