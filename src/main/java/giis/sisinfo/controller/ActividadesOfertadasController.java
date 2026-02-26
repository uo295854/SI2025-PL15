package giis.sisinfo.controller;

import java.util.ArrayList;
import java.util.List;

import giis.sisinfo.dto.ActividadesOfertadasDTO;
import giis.sisinfo.model.ActividadesOfertadasModel;
import giis.sisinfo.view.ActividadesOfertadasView;

public class ActividadesOfertadasController {
	
	private final ActividadesOfertadasView view;
	private final ActividadesOfertadasModel model;
	
	List<ActividadesOfertadasDTO> listaActividades;
	
	public ActividadesOfertadasController(ActividadesOfertadasView nview, ActividadesOfertadasModel nmodel){
		view = nview;
		model = nmodel;
		listaActividades = new ArrayList<>();
		
	}
	
	public void getListaActividades() {
		
	}
	

}
