package giis.sisinfo.controller;

import javax.swing.JOptionPane;

import giis.sisinfo.model.ActividadesModel;
import giis.sisinfo.util.CentroDB;
import giis.sisinfo.view.MainView;
import giis.sisinfo.view.PlanificarActividadView;
import giis.sisinfo.model.ReservaInstalacionAdminSocioModel;
import giis.sisinfo.view.ReservaInstalacionAdminSocioView;

public class MainController {

    private final MainView view;
    private final CentroDB db = new CentroDB();

    public MainController(MainView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.getBtnGestionActividades().addActionListener(e -> abrirGestionActividades());
        view.getBtnReservas().addActionListener(e -> abrirReservas());

        view.getBtnCrearBD().addActionListener(e -> {
            try {
                crearBD();
                JOptionPane.showMessageDialog(view, "Tablas creadas correctamente.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error creando la BD:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        view.getBtnCargarDatos().addActionListener(e -> {
            try {
                cargarDatos();
                JOptionPane.showMessageDialog(view, "Datos cargados correctamente.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error cargando datos:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        view.getBtnReservaInstalacionAdminSocio().addActionListener(e -> abrirReservaInstalacionAdminSocio());
    }

    private void abrirGestionActividades() {
        PlanificarActividadView v = new PlanificarActividadView();
        ActividadesModel model = new ActividadesModel();
        new PlanificarActividadController(v, model);
        v.setVisible(true);
    }

    private void abrirReservas() {
        JOptionPane.showMessageDialog(view, "Pendiente: pantalla de Reservas / Inscripciones");
    }

    public void crearBD() {
        db.executeScript("src/main/resources/schema.sql");
    }

    public void cargarDatos() {
        db.executeScript("src/main/resources/data.sql");
    }

    public void inicializarTodo() {
        crearBD();
        cargarDatos();
    }
    
    private void abrirReservaInstalacionAdminSocio() {
        ReservaInstalacionAdminSocioView v = new ReservaInstalacionAdminSocioView();
        ReservaInstalacionAdminSocioModel m = new ReservaInstalacionAdminSocioModel();
        new ReservaInstalacionAdminSocioController(m, v);
    }
}