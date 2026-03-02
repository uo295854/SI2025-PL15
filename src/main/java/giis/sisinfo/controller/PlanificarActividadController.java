package giis.sisinfo.controller;

import java.awt.Color;
import java.awt.Component;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

import giis.sisinfo.util.ApplicationException;
import giis.sisinfo.dto.ActividadDTO;
import giis.sisinfo.dto.PeriodoInscripcionDTO;
import giis.sisinfo.model.PlanificarActividadesModel;
import giis.sisinfo.view.PlanificarActividadView;

public class PlanificarActividadController {

	private final PlanificarActividadView view;
	private final PlanificarActividadesModel model;

	private boolean updatingSummary = false;

	// Días (modelo del combo)
	private DefaultComboBoxModel<CheckListItem> modelDias;
	private JComboBox<Object> comboDias;

	// Horarios por día
	private final Map<String, Horario> horariosPorDia = new HashMap<>();
	private String diaActualHorario = null;

	private static final DateTimeFormatter F_DATE = DateTimeFormatter.ISO_LOCAL_DATE;      // yyyy-MM-dd
	private static final DateTimeFormatter F_TIME = DateTimeFormatter.ofPattern("HH:mm"); // HH:mm

	public PlanificarActividadController(PlanificarActividadView view, PlanificarActividadesModel model) {
		this.view = view;
		this.model = model;

		initDiasCombo();
		initViewData();   // carga combos desde BD
		initController();
	}

	private void initViewData() {
		// Instalaciones
		List<String> instalaciones = model.getNombresInstalaciones();
		view.getComboBoxInstalacion().setModel(new DefaultComboBoxModel<>(instalaciones.toArray(new String[0])));

		// Periodos inscripción
		List<String> periodos = model.getNombresPeriodosInscripcion();
		view.getComboBoxPeriodoInscripcion().setModel(new DefaultComboBoxModel<>(periodos.toArray(new String[0])));

		// Pintar fechas del periodo seleccionado inicialmente
		actualizarFechasPeriodoSeleccionado();

		// Listener para actualizar labels cuando cambia el periodo
		view.getComboBoxPeriodoInscripcion().addActionListener(e -> actualizarFechasPeriodoSeleccionado());
	}

	private void actualizarFechasPeriodoSeleccionado() {
		String nombre = (String) view.getComboBoxPeriodoInscripcion().getSelectedItem();
		if (nombre == null) {
			view.getLblFechaInicioSocio().setText("Fecha inicio socios: -");
			view.getLblFechaFinSocio().setText("Fecha fin socios: -");
			view.getLblFechaFinNoSocio().setText("Fecha fin no socios: -");
			return;
		}
		PeriodoInscripcionDTO p = model.getPeriodoInscripcion(nombre);
		view.getLblFechaInicioSocio().setText("Fecha inicio socios: " + p.getFechaInicioSocio());
		view.getLblFechaFinSocio().setText("Fecha fin socios: " + p.getFechaFinSocio());
		view.getLblFechaFinNoSocio().setText("Fecha fin no socios: " + p.getFechaFinNoSocio());
	}

	private void initController() {
		view.getBtnCancelar().addActionListener(e -> view.close());
		view.getBtnCrearActividad().addActionListener(e -> crearActividad());
	}

	// ====== DÍAS MULTISELECCIÓN ======
	private void initDiasCombo() {
		comboDias = view.getComboBoxDiasMulti();

		modelDias = new DefaultComboBoxModel<>();
		String[] dias = { "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo" };
		for (String d : dias) modelDias.addElement(new CheckListItem(d, false));

		CheckListItem summaryItem = new CheckListItem("(Selecciona días)", false);
		summaryItem.setSummary(true);
		modelDias.insertElementAt(summaryItem, 0);

		@SuppressWarnings("unchecked")
		JComboBox<CheckListItem> typed = (JComboBox<CheckListItem>) (JComboBox<?>) comboDias;
		typed.setModel(modelDias);
		typed.setRenderer(new CheckBoxComboRenderer());
		typed.setSelectedIndex(0);

		typed.addActionListener(e -> {
			if (updatingSummary) return;

			Object sel = typed.getSelectedItem();
			if (!(sel instanceof CheckListItem)) return;

			CheckListItem item = (CheckListItem) sel;
			if (item.isSummary()) return;

			// Toggle
			item.setSelected(!item.isSelected());

			// Guardar horario del día anterior antes de cambiar
			guardarHorarioActualSiProcede();

			if (item.isSelected()) {
				diaActualHorario = item.getLabel();
				view.getLblHorario().setText("Horario (" + diaActualHorario + "):");
				cargarHorarioDeDia(diaActualHorario);
			} else {
				if (item.getLabel().equals(diaActualHorario)) {
					diaActualHorario = buscarUltimoDiaMarcado();
					if (diaActualHorario == null) {
						view.getLblHorario().setText("Horario:");
						view.getTextHoraDe().setText("");
						view.getTextHoraA().setText("");
					} else {
						view.getLblHorario().setText("Horario (" + diaActualHorario + "):");
						cargarHorarioDeDia(diaActualHorario);
					}
				}
			}

			updateDiasSummary();
			selectSummarySilently(typed);
		});

		updateDiasSummary();
		selectSummarySilently(typed);
	}

	private void updateDiasSummary() {
		String texto = getDiasSeleccionadosComoTexto();
		CheckListItem summary = modelDias.getElementAt(0);
		summary.setLabel(texto.isBlank() ? "(Selecciona días)" : texto);
		comboDias.repaint();
	}

	private void selectSummarySilently(JComboBox<CheckListItem> combo) {
		updatingSummary = true;
		try { combo.setSelectedIndex(0); }
		finally { updatingSummary = false; }
	}

	private String getDiasSeleccionadosComoTexto() {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < modelDias.getSize(); i++) {
			CheckListItem it = modelDias.getElementAt(i);
			if (it.isSelected()) {
				if (sb.length() > 0) sb.append(", ");
				sb.append(it.getLabel());
			}
		}
		return sb.toString();
	}

	private String buscarUltimoDiaMarcado() {
		for (int i = modelDias.getSize() - 1; i >= 1; i--) {
			CheckListItem it = modelDias.getElementAt(i);
			if (it.isSelected()) return it.getLabel();
		}
		return null;
	}

	// ====== Horarios por día ======
	private void guardarHorarioActualSiProcede() {
		if (diaActualHorario == null) return;
		horariosPorDia.put(diaActualHorario, new Horario(view.getTextHoraDe().getText(), view.getTextHoraA().getText()));
	}

	private void cargarHorarioDeDia(String dia) {
		Horario h = horariosPorDia.get(dia);
		if (h == null) {
			view.getTextHoraDe().setText("");
			view.getTextHoraA().setText("");
		} else {
			view.getTextHoraDe().setText(h.de);
			view.getTextHoraA().setText(h.a);
		}
	}

	// ====== Crear actividad ======
	private void crearActividad() {
		guardarHorarioActualSiProcede();

		try {
			String nombre = view.getTextFieldActividad().getText().trim();
			if (nombre.isEmpty()) throw new ApplicationException("Introduce el nombre de la actividad.");

			if (buscarUltimoDiaMarcado() == null)
				throw new ApplicationException("Selecciona al menos un día.");

			String instalacion = (String) view.getComboBoxInstalacion().getSelectedItem();
			if (instalacion == null || instalacion.isBlank())
				throw new ApplicationException("Selecciona una instalación.");

			String tipoUi = (String) view.getComboBoxTipoActividad().getSelectedItem();
			int aforo = (int) view.getSpinnerAforo().getValue();
			int duracion = (int) view.getSpinnerDuracion().getValue();
			String periodo = (String) view.getComboBoxPeriodoInscripcion().getSelectedItem();
			if (periodo == null || periodo.isBlank())
				throw new ApplicationException("Selecciona un periodo de inscripción.");

			String fechaInicioTxt = view.getTextFechaInicio().getText().trim();
			String fechaFinTxt = view.getTextFechaFin().getText().trim();

			LocalDate fechaInicio = parseDateOrFail(fechaInicioTxt, "Fecha inicio inválida (usa yyyy-MM-dd).");
			LocalDate fechaFin = parseDateOrFail(fechaFinTxt, "Fecha fin inválida (usa yyyy-MM-dd).");
			if (fechaFin.isBefore(fechaInicio))
				throw new ApplicationException("La fecha fin no puede ser anterior a la fecha inicio.");

			double cuotaSocio = ((Number) view.getSpinnerCuotaSocios().getValue()).doubleValue();
			double cuotaNoSocio = ((Number) view.getSpinnerCuotaNoSocios().getValue()).doubleValue();

			String desc = view.getTextAreaDescripcion().getText();

			// Validar horarios seleccionados y convertir a map día -> [de,a]
			Map<String, String[]> horarios = new HashMap<>();
			for (int i = 1; i < modelDias.getSize(); i++) {
				CheckListItem it = modelDias.getElementAt(i);
				if (!it.isSelected()) continue;

				String dia = it.getLabel();
				Horario h = horariosPorDia.get(dia);
				if (h == null || h.de.isBlank() || h.a.isBlank())
					throw new ApplicationException("Falta horario (de/a) para " + dia + " (formato HH:mm).");

				LocalTime tDe = parseTimeOrFail(h.de.trim(), "Hora 'de' inválida para " + dia + " (usa HH:mm).");
				LocalTime tA  = parseTimeOrFail(h.a.trim(),  "Hora 'a' inválida para " + dia + " (usa HH:mm).");

				if (!tA.isAfter(tDe))
					throw new ApplicationException("En " + dia + ", la hora 'a' debe ser posterior a la hora 'de'.");

				// (opcional) comprobar duración mínima
				long mins = java.time.Duration.between(tDe, tA).toMinutes();
				if (mins < duracion)
					throw new ApplicationException("En " + dia + ", el tramo " + h.de + "-" + h.a
							+ " es menor que la duración (" + duracion + " min).");

				horarios.put(dia, new String[] { h.de.trim(), h.a.trim() });
			}

			ActividadDTO dto = new ActividadDTO(
					nombre,
					instalacion,
					tipoUi,
					aforo,
					horarios,
					duracion,
					periodo,
					fechaInicio.format(F_DATE),
					fechaFin.format(F_DATE),
					cuotaSocio,
					cuotaNoSocio,
					desc
			);

			model.crearActividad(dto);

			JOptionPane.showMessageDialog(view, "Actividad creada correctamente.");
			view.close();

		} catch (ApplicationException ex) {
			JOptionPane.showMessageDialog(view, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(view, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private LocalDate parseDateOrFail(String txt, String msg) {
		try { return LocalDate.parse(txt, F_DATE); }
		catch (DateTimeParseException ex) { throw new ApplicationException(msg); }
	}

	private LocalTime parseTimeOrFail(String txt, String msg) {
		try { return LocalTime.parse(txt, F_TIME); }
		catch (DateTimeParseException ex) { throw new ApplicationException(msg); }
	}

	// ===== Tipos auxiliares =====
	private static class Horario {
		String de, a;
		Horario(String de, String a) { this.de = de; this.a = a; }
	}

	private static class CheckListItem {
		private String label;
		private boolean selected;
		private boolean summary;

		CheckListItem(String label, boolean selected) {
			this.label = label;
			this.selected = selected;
		}

		public String getLabel() { return label; }
		public void setLabel(String label) { this.label = label; }

		public boolean isSelected() { return selected; }
		public void setSelected(boolean selected) { this.selected = selected; }

		public boolean isSummary() { return summary; }
		public void setSummary(boolean summary) { this.summary = summary; }

		@Override public String toString() { return label; }
	}

	private static class CheckBoxComboRenderer extends JCheckBox implements ListCellRenderer<CheckListItem> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<? extends CheckListItem> list, CheckListItem value, int index,
				boolean isSelected, boolean cellHasFocus) {

			if (index == -1) { // combo cerrado
				setText(value.getLabel());
				setSelected(false);
			} else {
				setText(value.getLabel());
				setSelected(value.isSummary() ? false : value.isSelected());
			}
			setBackground(Color.WHITE);
			return this;
		}
	}
}