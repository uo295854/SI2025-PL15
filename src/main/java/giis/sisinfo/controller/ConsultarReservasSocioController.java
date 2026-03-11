package giis.sisinfo.controller;

import giis.sisinfo.model.ConsultarReservasSocioModel;
import giis.sisinfo.view.ConsultarReservasSocioView;

public class ConsultarReservasSocioController {
	
	private final ConsultarReservasSocioModel model;
	private final ConsultarReservasSocioView view;
	
	public ConsultarReservasSocioController(ConsultarReservasSocioModel nmodel, ConsultarReservasSocioView nview) {
		model = nmodel;
		view = nview;
		
		initView();
	}
	
	public void initView() {
		view.setVisible(true);
	}

}
