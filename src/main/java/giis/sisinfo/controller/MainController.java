package giis.sisinfo.controller;

import javax.swing.JOptionPane;
import giis.sisinfo.view.PlanificarActividadView;
import giis.sisinfo.model.ActividadesModel;
import giis.sisinfo.controller.PlanificarActividadController;

import giis.sisinfo.view.MainView;

public class MainController {

	private final MainView view;

	public MainController(MainView view) {
		this.view = view;
		initController();
	}

	private void initController() {
		view.getBtnGestionActividades().addActionListener(e -> abrirGestionActividades());
		view.getBtnReservas().addActionListener(e -> abrirReservas());
	}

	private void abrirGestionActividades() {

	    // Crear MVC de la nueva pantalla
	    PlanificarActividadView view = new PlanificarActividadView();
	    ActividadesModel model = new ActividadesModel();
	    new PlanificarActividadController(view, model);

	    view.setVisible(true);
	}

	private void abrirReservas() {
		// TODO: aquí engancharás tu HU: reserva/inscripción (socio al corriente de pago, cupos, fechas...)
		JOptionPane.showMessageDialog(view, "Pendiente: pantalla de Reservas / Inscripciones");
	}
}