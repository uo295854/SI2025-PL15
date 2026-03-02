package giis.sisinfo.controller;

import java.time.LocalDate;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import giis.sisinfo.dto.PeriodoInscripcion2DTO;
import giis.sisinfo.dto.PeriodoOficialItemDTO;
import giis.sisinfo.model.PeriodoInscripcionModel;
import giis.sisinfo.util.DateUtil;
import giis.sisinfo.view.PeriodoInscripcionView;

public class PeriodoInscripcionController {

	private final PeriodoInscripcionView view;
	private final PeriodoInscripcionModel model;

	public PeriodoInscripcionController(PeriodoInscripcionView view, PeriodoInscripcionModel model) {
		this.view = view;
		this.model = model;
		initView();
		initController();
	}

	private void initView() {
		cargarPeriodosOficiales();
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}

	private void initController() {
		view.getBtnCancelar().addActionListener(e -> view.close());
		view.getBtnConfirmar().addActionListener(e -> onConfirmar());
	}

	private void cargarPeriodosOficiales() {
		List<PeriodoOficialItemDTO> periodos = model.getPeriodosOficiales();
		DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
		// Si prefieres tener el DTO como item del combo, también vale.
		// Aquí lo dejo como String para no tocar tu View.
		for (PeriodoOficialItemDTO p : periodos) {
			m.addElement(p.toString());
		}
		view.getCmbPeriodoOficial().setModel(m);
	}

	private void onConfirmar() {
		try {
			// 1) Leer campos
			String nombre = view.getTxtNombre().getText().trim();
			String descripcion = view.getTxtDescripcion().getText().trim();

			String fIniSocioTxt = view.getTxtFechaInicioSocios().getText().trim();
			String fFinSocioTxt = view.getTxtFechaFinSocios().getText().trim();
			String fFinNoSocioTxt = view.getTxtFechaFinNoSocios().getText().trim();

			// 2) Validaciones básicas
			if (nombre.isEmpty()) {
				JOptionPane.showMessageDialog(view, "El nombre del periodo de inscripción es obligatorio.");
				return;
			}
			if (model.existeNombrePeriodoInscripcion(nombre)) {
				JOptionPane.showMessageDialog(view, "Ya existe un periodo de inscripción con ese nombre.");
				return;
			}

			LocalDate iniSocio = DateUtil.parseFlexible(fIniSocioTxt);
			LocalDate finSocio = DateUtil.parseFlexible(fFinSocioTxt);
			LocalDate finNoSocio = DateUtil.parseFlexible(fFinNoSocioTxt);

			if (iniSocio.isAfter(finSocio)) {
				JOptionPane.showMessageDialog(view, "La fecha de inicio (socios) no puede ser posterior a la fecha fin (socios).");
				return;
			}
			if (finSocio.isAfter(finNoSocio)) {
				JOptionPane.showMessageDialog(view, "La fecha fin (socios) no puede ser posterior a la fecha fin (no socios).");
				return;
			}

			// 3) Persistir (guardamos en ISO para SQLite TEXT y checks lexicográficos)
			PeriodoInscripcion2DTO dto = new PeriodoInscripcion2DTO(
				nombre,
				descripcion.isEmpty() ? null : descripcion,
				DateUtil.toIso(iniSocio),
				DateUtil.toIso(finSocio),
				DateUtil.toIso(finNoSocio)
			);

			model.crearPeriodoInscripcion(dto);

			JOptionPane.showMessageDialog(view, "Periodo de inscripción creado correctamente.");
			view.close();

		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(view, ex.getMessage());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(view, "Error al crear el periodo de inscripción: " + ex.getMessage());
		}
	}
}