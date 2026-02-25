package giis.sisinfo.controller;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

import giis.sisinfo.dto.ActividadDTO;
import giis.sisinfo.model.ActividadesModel;
import giis.sisinfo.view.PlanificarActividadView;

public class PlanificarActividadController {

	private final PlanificarActividadView view;
	private final ActividadesModel model;

	private boolean updatingSummary = false;

	// Días (modelo del combo)
	private DefaultComboBoxModel<CheckListItem> modelDias;
	private JComboBox<Object> comboDias;

	// Horarios por día
	private final Map<String, Horario> horariosPorDia = new HashMap<>();
	private String diaActualHorario = null;

	public PlanificarActividadController(PlanificarActividadView view, ActividadesModel model) {
		this.view = view;
		this.model = model;

		initDiasCombo();
		initController();
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
		try {
			combo.setSelectedIndex(0);
		} finally {
			updatingSummary = false;
		}
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

	// ====== Crear actividad (placeholder) ======
	private void crearActividad() {
		guardarHorarioActualSiProcede();

		String act = view.getTextFieldActividad().getText().trim();
		if (act.isEmpty()) {
			JOptionPane.showMessageDialog(view, "Introduce el nombre de la actividad.");
			return;
		}

		if (buscarUltimoDiaMarcado() == null) {
			JOptionPane.showMessageDialog(view, "Selecciona al menos un día.");
			return;
		}

		String instalacion = (String) view.getComboBoxInstalacion().getSelectedItem();
		String tipo = (String) view.getComboBoxTipoActividad().getSelectedItem();
		int aforo = (int) view.getSpinnerAforo().getValue();
		int duracion = (int) view.getSpinnerDuracion().getValue();
		String periodo = (String) view.getComboBoxPeriodoInscripcion().getSelectedItem();
		int cuotaSocios = (int) view.getSpinnerCuotaSocios().getValue();
		int cuotaNoSocios = (int) view.getSpinnerCuotaNoSocios().getValue();
		String desc = view.getTextAreaDescripcion().getText();

		// Convertir horarios a mapa día -> [de,a]
		Map<String, String[]> horarios = new HashMap<>();
		for (Map.Entry<String, Horario> e : horariosPorDia.entrySet()) {
			horarios.put(e.getKey(), new String[] { e.getValue().de, e.getValue().a });
		}

		ActividadDTO dto = new ActividadDTO(
			act, instalacion, tipo, aforo,
			horarios, duracion, periodo,
			cuotaSocios, cuotaNoSocios, desc
		);

		model.crearActividad(dto);

		JOptionPane.showMessageDialog(view, "Actividad creada (stub).");
		view.close();
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