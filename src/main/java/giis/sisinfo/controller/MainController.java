package giis.sisinfo.controller;

import javax.swing.JOptionPane;

import giis.sisinfo.model.PlanificarActividadesModel;
import giis.sisinfo.model.ReservaInstalacionAdminModel;
import giis.sisinfo.util.Database;
import giis.sisinfo.view.ActividadesOfertadasView;
import giis.sisinfo.view.CancelarActividadAdminView;
import giis.sisinfo.view.CancelarReservaInstalacionAdminView;
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
import giis.sisinfo.model.ConsultarReservasSocioModel;
import giis.sisinfo.model.PeriodoInscripcionModel;
import giis.sisinfo.view.PeriodoInscripcionView;
import giis.sisinfo.view.ReservaInstalacionesView;
import giis.sisinfo.view.VisualizarDisponibilidadInstalacionesSocioView;
import giis.sisinfo.model.ReservaInstalacionesModel;
import giis.sisinfo.model.VisualizarDisponibilidadInstalacionesSocioModel;
import giis.sisinfo.view.InscripcionActividadView;
import giis.sisinfo.view.ListadoCargosMensualesSocioView;
import giis.sisinfo.view.ListadoCargosMensualesView;
import giis.sisinfo.view.LoginView;
import giis.sisinfo.model.InscripcionActividadModel;
import giis.sisinfo.model.ListadoCargosMensualesModel;
import giis.sisinfo.model.ListadoCargosMensualesSocioModel;
import giis.sisinfo.model.LoginModel;

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
        view.getBtnVisualizarReservasInstalaciones().addActionListener(e -> abrirVisualizarReservasInstalacionesAdmin());
        view.getBtnConsultarReservasSocioView().addActionListener(e->abrirConsultasReservasSocio());
        view.getBtnReservaInstalacionAdmin().addActionListener(e->abrirReservaInstalacionesAdmin());
        view.getBtnReservaInstalacionesAuto().addActionListener(e -> abrirReservaInstalacionesAuto());
        view.getBtnInscripcionActividad().addActionListener(e -> abrirInscripcionActividad());
        view.getBtnListadoCargosMensuales().addActionListener(e -> abrirCargosMensuales());
        view.getBtnReservaInstalacionSocio().addActionListener(e-> abrirReservaInstalacionSocio());
        view.getBtnVisualizarDisponibilidadInstalaciones().addActionListener(e-> abrirVisualizarDisponibilidadInstalacionesSocio());
        view.getBtnListadoCargosMensualesSocio().addActionListener(e -> abrirCargosMensualesSocio());
        view.getBtnActividadesOfertadas().addActionListener(e-> abrirActividadesOfertadas());
        view.getbtnCancelarActividadAdmin().addActionListener(e -> abrirCancelarActividadAdmin());
        view.getBtnCancelarReservaInstalacionesAdmin().addActionListener(e-> abrirCancelarReservasInstalacionesAdmin());

        
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
            // El controller ya hace v.setVisible(true) en initView
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
    	new ConsultarReservasSocioController(m,v);
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
		// El idSocio lo obtienes de la sesión (o lo pasas al abrir la pantalla tras el login)
		int idSocio = Session.get().getIdSocio();
		new InscripcionActividadController(v, model, idSocio);
		v.setVisible(true);
	}
	 
	 private void abrirCargosMensuales() {
		 ListadoCargosMensualesView v = new ListadoCargosMensualesView();
		 ListadoCargosMensualesModel m = new ListadoCargosMensualesModel();
		 new ListadoCargosMensualesController(m,v);
	 }
	 
	 private void abrirCargosMensualesSocio() {
		 ListadoCargosMensualesSocioView v = new ListadoCargosMensualesSocioView();
		 ListadoCargosMensualesSocioModel m = new ListadoCargosMensualesSocioModel();
		 new ListadoCargosMensualesSocioController(m,v);
	 }
		 

	 private void abrirReservaInstalacionSocio() {
		 ReservaInstalacionSocioView v = new ReservaInstalacionSocioView();
	        ReservaInstalacionSocioModel m = new ReservaInstalacionSocioModel();
	        
	        int idSocio = Session.get().getIdSocio();
	        new ReservaInstalacionSocioController(m, v,idSocio);
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
		 
		 new ActividadesOfertadasController(v,m);
	 }
	 
	 public void abrirCancelarActividadAdmin() {
		 CancelarActividadAdminModel m = new CancelarActividadAdminModel();
		 CancelarActividadAdminView v = new CancelarActividadAdminView();
		 
		 new CancelarActividadAdminController(v,m);
	 }
	 
	 
	 private void abrirCancelarReservasInstalacionesAdmin() {
		 CancelarReservaInstalacionAdminModel m = new CancelarReservaInstalacionAdminModel();
		 CancelarReservaInstalacionAdminView v = new CancelarReservaInstalacionAdminView();
		 
		 new CancelarReservaInstalacionAdminController(m,v);
	 }
    
    
    // Método para habilitar/deshabilitar botones según el rol del usuario
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
        
        // Visualizar reservas de instalaciones (admin)
        view.getBtnVisualizarReservasInstalaciones().setEnabled(isAdmin);

        // Obtener la lista de actividades ofertadas en un determinado periodo
        view.getBtnActividadesOfertadas().setEnabled(isAdmin);
        
        // Reservar una instalacion para una actividad en un periodo determinado
        view.getBtnReservaInstalacionesAuto().setEnabled(isAdmin);
        
        view.getBtnInscripcionActividad().setEnabled(s.isSocio()); // accesible para todos los socios (o incluso público)
        
        // Reservas / Inscripciones: normalmente accesible para todos (o al menos socios)
        // Si quieres solo SOCIO:
        // view.getBtnReservas().setEnabled(s.isSocio());
        
        //Consultar Reservas hechas por Socios
        view.getBtnConsultarReservasSocioView().setEnabled(s.isSocio());
        /*
        if(isAdmin) { //para depuración y testeo, se borrará mas adelante
        	view.getBtnConsultarReservasSocioView().setEnabled(true);
        	System.out.println("MainController | Se habilita botón de Consultar Reservas de Socio siendo el usuario Admin");
        }
        */
        

        //Consultar cargos mensuales de los socios
        view.getBtnListadoCargosMensuales().setEnabled(isAdmin);

        // Reservar una instalacion para una fecha determinada por parte de un Socio
        view.getBtnReservaInstalacionSocio().setEnabled(s.isSocio());
        
        // Visualizar la disponibilidad de las instalaciones por parte de un Socio
        view.getBtnVisualizarDisponibilidadInstalaciones().setEnabled(s.isSocio());
        
        //Consultar cargos mensuales de los socios
        view.getBtnListadoCargosMensualesSocio().setEnabled(s.isSocio());
        
        //Cancelar Actividades como un administrador
        view.getbtnCancelarActividadAdmin().setEnabled(isAdmin);
        
        //Cancelar Reservas de Instalaciones como administrador
        view.getBtnCancelarReservaInstalacionesAdmin().setEnabled(isAdmin);
        
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