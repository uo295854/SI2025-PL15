package giis.sisinfo.controller;

import javax.swing.JOptionPane;

import giis.sisinfo.model.PlanificarActividadesModel;
import giis.sisinfo.model.ReservaInstalacionAdminModel;
import giis.sisinfo.util.Database;
import giis.sisinfo.view.ActividadesOfertadasView;
import giis.sisinfo.view.CancelarActividadAdminView;
import giis.sisinfo.view.CancelarReservaInstalacionAdminView;
import giis.sisinfo.view.CancelarReservaInstalacionSocioView;
import giis.sisinfo.view.ConsultarReservasSocioView;
import giis.sisinfo.view.MainView;
import giis.sisinfo.view.PlanificarActividadView;
import giis.sisinfo.model.ReservaInstalacionAdminSocioModel;
import giis.sisinfo.model.ReservaInstalacionSocioModel;
import giis.sisinfo.model.VisualizarReservasInstalacionesAdminModel;
import giis.sisinfo.session.Session;
import giis.sisinfo.view.ReservaInstalacionAdminSocioView;
import giis.sisinfo.view.ReservaInstalacionAdminView;
import giis.sisinfo.view.ReservaInstalacionSocioView;
import giis.sisinfo.view.VisualizarReservasInstalacionesAdminView;
import giis.sisinfo.model.ActividadesOfertadasModel;
import giis.sisinfo.model.CancelarActividadAdminModel;
import giis.sisinfo.model.CancelarReservaInstalacionAdminModel;
import giis.sisinfo.model.CancelarReservaInstalacionSocioModel;
import giis.sisinfo.model.ConsultarReservasSocioModel;
import giis.sisinfo.model.PeriodoInscripcionModel;
import giis.sisinfo.view.PeriodoInscripcionView;
import giis.sisinfo.view.ReservaInstalacionesView;
import giis.sisinfo.view.VisualizarDisponibilidadInstalacionesSocioView;
import giis.sisinfo.model.ReservaInstalacionesModel;
import giis.sisinfo.model.VisualizarDisponibilidadInstalacionesSocioModel;
import giis.sisinfo.view.InscripcionActividadView;
import giis.sisinfo.view.InscripcionNoSocioActividadView;
import giis.sisinfo.view.ListadoCargosMensualesSocioView;
import giis.sisinfo.view.ListadoCargosMensualesView;
import giis.sisinfo.view.LoginView;
import giis.sisinfo.model.InscripcionActividadModel;
import giis.sisinfo.model.InscripcionNoSocioActividadModel;
import giis.sisinfo.model.ListadoCargosMensualesModel;
import giis.sisinfo.model.ListadoCargosMensualesSocioModel;
import giis.sisinfo.model.LoginModel;

public class MainController {

    private final MainView view;

    public MainController(MainView view) {
        this.view = view;
        initController();
        aplicarPermisosPorRol();
    }

    private void initController() {
        view.getBtnGestionActividades().addActionListener(e -> abrirGestionActividades());
        view.getBtnReservas().addActionListener(e -> abrirReservas());
        view.getBtnPeriodoInscripcion().addActionListener(e -> abrirPeriodoInscripcion());
        view.getBtnReservaInstalacionAdminSocio().addActionListener(e -> abrirReservaInstalacionAdminSocio());
        view.getBtnVisualizarReservasInstalaciones().addActionListener(e -> abrirVisualizarReservasInstalacionesAdmin());
        view.getBtnConsultarReservasSocioView().addActionListener(e -> abrirConsultasReservasSocio());
        view.getBtnReservaInstalacionAdmin().addActionListener(e -> abrirReservaInstalacionesAdmin());
        view.getBtnReservaInstalacionesAuto().addActionListener(e -> abrirReservaInstalacionesAuto());
        view.getBtnInscripcionActividad().addActionListener(e -> abrirInscripcionActividad());
        view.getBtnInscripcionNoSocioActividad().addActionListener(e -> abrirInscripcionNoSocioActividad());
        view.getBtnListadoCargosMensuales().addActionListener(e -> abrirCargosMensuales());
        view.getBtnReservaInstalacionSocio().addActionListener(e -> abrirReservaInstalacionSocio());
        view.getBtnVisualizarDisponibilidadInstalaciones().addActionListener(e -> abrirVisualizarDisponibilidadInstalacionesSocio());
        view.getBtnListadoCargosMensualesSocio().addActionListener(e -> abrirCargosMensualesSocio());
        view.getBtnActividadesOfertadas().addActionListener(e -> abrirActividadesOfertadas());
        view.getbtnCancelarActividadAdmin().addActionListener(e -> abrirCancelarActividadAdmin());
        view.getBtnCancelarReservaInstalacionesAdmin().addActionListener(e -> abrirCancelarReservasInstalacionesAdmin());
        view.getBtnCancelarReservaInstalacionesSocio().addActionListener(e -> abrirCancelarReservasInstalacionesSocio());

        view.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
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

    private void abrirReservaInstalacionesAdmin() {
        ReservaInstalacionAdminView v = new ReservaInstalacionAdminView();
        ReservaInstalacionAdminModel m = new ReservaInstalacionAdminModel();
        new ReservaInstalacionAdminController(v, m);
    }

    private void abrirPeriodoInscripcion() {
        try {
            PeriodoInscripcionView v = new PeriodoInscripcionView();
            Database db = new Database();
            PeriodoInscripcionModel m = new PeriodoInscripcionModel(db);
            new PeriodoInscripcionController(v, m);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Error abriendo Periodo de inscripción:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirConsultasReservasSocio() {
        ConsultarReservasSocioView v = new ConsultarReservasSocioView();
        ConsultarReservasSocioModel m = new ConsultarReservasSocioModel();
        new ConsultarReservasSocioController(m, v);
    }

    private void abrirReservaInstalacionesAuto() {
        ReservaInstalacionesView v = new ReservaInstalacionesView();
        ReservaInstalacionesModel model = new ReservaInstalacionesModel();
        new ReservaInstalacionesController(v, model);
        v.setVisible(true);
    }

    private void abrirInscripcionActividad() {
        InscripcionActividadView v = new InscripcionActividadView();
        InscripcionActividadModel model = new InscripcionActividadModel();
        int idSocio = Session.get().getIdSocio();
        new InscripcionActividadController(v, model, idSocio);
        v.setVisible(true);
    }

    private void abrirInscripcionNoSocioActividad() {
        InscripcionNoSocioActividadView v = new InscripcionNoSocioActividadView();
        InscripcionNoSocioActividadModel m = new InscripcionNoSocioActividadModel();
        new InscripcionNoSocioActividadController(v, m);
        v.setVisible(true);
    }

    private void abrirCargosMensuales() {
        ListadoCargosMensualesView v = new ListadoCargosMensualesView();
        ListadoCargosMensualesModel m = new ListadoCargosMensualesModel();
        new ListadoCargosMensualesController(m, v);
    }

    private void abrirCargosMensualesSocio() {
        ListadoCargosMensualesSocioView v = new ListadoCargosMensualesSocioView();
        ListadoCargosMensualesSocioModel m = new ListadoCargosMensualesSocioModel();
        new ListadoCargosMensualesSocioController(m, v);
    }

    private void abrirReservaInstalacionSocio() {
        ReservaInstalacionSocioView v = new ReservaInstalacionSocioView();
        ReservaInstalacionSocioModel m = new ReservaInstalacionSocioModel();

        int idSocio = Session.get().getIdSocio();
        new ReservaInstalacionSocioController(m, v, idSocio);
    }

    private void abrirVisualizarDisponibilidadInstalacionesSocio() {
        VisualizarDisponibilidadInstalacionesSocioView v = new VisualizarDisponibilidadInstalacionesSocioView();
        VisualizarDisponibilidadInstalacionesSocioModel m = new VisualizarDisponibilidadInstalacionesSocioModel();

        int idSocio = Session.get().getIdSocio();
        new VisualizarDisponibilidadInstalacionesSocioController(m, v, idSocio);
    }

    private void abrirActividadesOfertadas() {
        ActividadesOfertadasModel m = new ActividadesOfertadasModel();
        ActividadesOfertadasView v = new ActividadesOfertadasView();

        new ActividadesOfertadasController(v, m);
    }

    public void abrirCancelarActividadAdmin() {
        CancelarActividadAdminModel m = new CancelarActividadAdminModel();
        CancelarActividadAdminView v = new CancelarActividadAdminView();

        new CancelarActividadAdminController(v, m);
    }

    private void abrirCancelarReservasInstalacionesAdmin() {
        CancelarReservaInstalacionAdminModel m = new CancelarReservaInstalacionAdminModel();
        CancelarReservaInstalacionAdminView v = new CancelarReservaInstalacionAdminView();

        new CancelarReservaInstalacionAdminController(m, v);
    }

    private void abrirCancelarReservasInstalacionesSocio() {
        CancelarReservaInstalacionSocioModel m = new CancelarReservaInstalacionSocioModel();
        CancelarReservaInstalacionSocioView v = new CancelarReservaInstalacionSocioView();
        int idSocio = Session.get().getIdSocio();
        new CancelarReservaInstalacionSocioController(m, v, idSocio);
    }

    private void aplicarPermisosPorRol() {
        Session s = Session.get();

        boolean isAdmin = s.isAdmin();

        view.getBtnGestionActividades().setEnabled(isAdmin);
        view.getBtnPeriodoInscripcion().setEnabled(isAdmin);
        view.getBtnReservaInstalacionAdminSocio().setEnabled(isAdmin);
        view.getBtnVisualizarReservasInstalaciones().setEnabled(isAdmin);
        view.getBtnActividadesOfertadas().setEnabled(isAdmin);
        view.getBtnReservaInstalacionesAuto().setEnabled(isAdmin);
        view.getBtnInscripcionNoSocioActividad().setEnabled(isAdmin);

        view.getBtnInscripcionActividad().setEnabled(s.isSocio());

        view.getBtnConsultarReservasSocioView().setEnabled(s.isSocio());

        view.getBtnListadoCargosMensuales().setEnabled(isAdmin);

        view.getBtnReservaInstalacionSocio().setEnabled(s.isSocio());
        view.getBtnVisualizarDisponibilidadInstalaciones().setEnabled(s.isSocio());
        view.getBtnListadoCargosMensualesSocio().setEnabled(s.isSocio());

        view.getbtnCancelarActividadAdmin().setEnabled(isAdmin);
        view.getBtnCancelarReservaInstalacionesAdmin().setEnabled(isAdmin);
        view.getBtnCancelarReservaInstalacionesSocio().setEnabled(s.isSocio());
    }

    private void cerrarSesion() {
        Database db = Session.get().getDb();
        Session.get().clear();

        view.dispose();

        LoginView loginView = new LoginView();
        LoginModel loginModel = new LoginModel(db);
        new LoginController(loginView, loginModel, db);
    }
}