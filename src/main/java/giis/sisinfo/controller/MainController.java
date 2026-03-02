package giis.sisinfo.controller;

import javax.swing.JOptionPane;

import giis.sisinfo.model.PlanificarActividadesModel;
import giis.sisinfo.util.Database;
import giis.sisinfo.view.MainView;
import giis.sisinfo.view.PlanificarActividadView;
import giis.sisinfo.model.ReservaInstalacionAdminSocioModel;
import giis.sisinfo.model.VisualizarReservasInstalacionesAdminModel;
import giis.sisinfo.view.ReservaInstalacionAdminSocioView;
import giis.sisinfo.view.VisualizarReservasInstalacionesAdminView;

public class MainController {

    private final MainView view;


    public MainController(MainView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.getBtnGestionActividades().addActionListener(e -> abrirGestionActividades());
        view.getBtnReservas().addActionListener(e -> abrirReservas());

        view.getBtnCrearBD().addActionListener(e -> {
            try {
            	Database db = new Database();
                db.createDatabase(false);
                JOptionPane.showMessageDialog(view, "Tablas creadas correctamente.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error creando la BD:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        view.getBtnCargarDatos().addActionListener(e -> {
            try {
                Database db = new Database();
                db.loadDatabase();
                JOptionPane.showMessageDialog(view, "Datos cargados correctamente.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error cargando datos:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        view.getBtnReservaInstalacionAdminSocio().addActionListener(e -> abrirReservaInstalacionAdminSocio());
        view.getBtnVisualizarReservasInstalaciones().addActionListener(e -> abrirVisualizarReservasInstalacionesAdmin());
    }

    private void abrirGestionActividades() {
        PlanificarActividadView v = new PlanificarActividadView();
        PlanificarActividadesModel model = new PlanificarActividadesModel();
        new PlanificarActividadController(v, model);
        v.setVisible(true);
    }

    private void abrirReservas() {
        JOptionPane.showMessageDialog(view, "Pendiente: pantalla de Reservas / Inscripciones");
    }



    private void abrirReservaInstalacionAdminSocio() {
        ReservaInstalacionAdminSocioView v = new ReservaInstalacionAdminSocioView();
        ReservaInstalacionAdminSocioModel m = new ReservaInstalacionAdminSocioModel();
        new ReservaInstalacionAdminSocioController(m, v);
    }
    private void abrirVisualizarReservasInstalacionesAdmin() {
    	VisualizarReservasInstalacionesAdminView v = new VisualizarReservasInstalacionesAdminView();
    	VisualizarReservasInstalacionesAdminModel m = new VisualizarReservasInstalacionesAdminModel();
    	new VisualizarReservasInstalacionesAdminController(m, v);
    }
}