package giis.sisinfo.controller;

import javax.swing.JOptionPane;

import giis.sisinfo.model.PlanificarActividadesModel;
import giis.sisinfo.util.Database;
import giis.sisinfo.view.MainView;
import giis.sisinfo.view.PlanificarActividadView;
import giis.sisinfo.model.ReservaInstalacionAdminSocioModel;
import giis.sisinfo.view.ReservaInstalacionAdminSocioView;
import giis.sisinfo.model.PeriodoInscripcionModel;
import giis.sisinfo.view.PeriodoInscripcionView;
import giis.sisinfo.session.Session;

public class MainController {

    private final MainView view;


    public MainController(MainView view) {
        this.view = view;
        initController();
        aplicarPermisosPorRol(); // ✅
    }

    private void initController() {
        view.getBtnGestionActividades().addActionListener(e -> abrirGestionActividades());
        view.getBtnReservas().addActionListener(e -> abrirReservas());
        view.getBtnPeriodoInscripcion().addActionListener(e -> abrirPeriodoInscripcion());
        view.getBtnReservaInstalacionAdminSocio().addActionListener(e -> abrirReservaInstalacionAdminSocio());
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
    
    private void abrirPeriodoInscripcion() {
        try {
            PeriodoInscripcionView v = new PeriodoInscripcionView();
            Database db = new Database();
            PeriodoInscripcionModel m = new PeriodoInscripcionModel(db);
            new PeriodoInscripcionController(v, m);
            // El controller ya hace v.setVisible(true) en initView
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Error abriendo Periodo de inscripción:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void aplicarPermisosPorRol() {
        Session s = Session.get();

        // Si por lo que sea no hay sesión, bloquea todo lo sensible
        boolean isAdmin = s.isAdmin();

        // Ejemplos típicos (ajusta a vuestras HUs):
        // Admin-only
        view.getBtnGestionActividades().setEnabled(isAdmin);
        view.getBtnPeriodoInscripcion().setEnabled(isAdmin);
        
        // Reserva instalaciones para socios (admin)
        view.getBtnReservaInstalacionAdminSocio().setEnabled(isAdmin);

        // Reservas / Inscripciones: normalmente accesible para todos (o al menos socios)
        // Si quieres solo SOCIO:
        // view.getBtnReservas().setEnabled(s.isSocio());
    }
}