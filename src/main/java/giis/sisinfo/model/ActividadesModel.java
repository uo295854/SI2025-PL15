package giis.sisinfo.model;

import java.util.Map;

import giis.sisinfo.dto.ActividadDTO;

public class ActividadesModel {

	public ActividadesModel() {
		// aquí luego podrás inicializar Database/DAO si quieres
	}

	public void crearActividad(ActividadDTO dto) {

		System.out.println("Actividad: " + dto.getNombreActividad());
		System.out.println("Instalación: " + dto.getInstalacion());
	}
}