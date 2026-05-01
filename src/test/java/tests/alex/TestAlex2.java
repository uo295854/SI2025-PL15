package tests.alex;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import giis.sisinfo.model.CancelarActividadAdminModel;
import giis.sisinfo.model.InscripcionActividadModel;
import giis.sisinfo.util.Database;

public class TestAlex2 {
	
	private int idSocio;
	private String nombreActividad;
	private String fechaInscripcion;
	InscripcionActividadModel model;
	

	@BeforeEach
	public void setup() {
		Database db = new Database();
		try {
			db.createDatabase(false);
			db.loadDatabase();
			
			idSocio = 1;
			nombreActividad = "Clases Pádel Iniciación"; //ID=2
			fechaInscripcion = "01-01-1990";
			model = new InscripcionActividadModel();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Test 1: Socio no existe")
	public void test1() {
		idSocio = 77777;
		assertFalse(model.inscribirSocioEnActividad(idSocio, nombreActividad, fechaInscripcion));	
	}
	
	@Test
	@DisplayName("Test 2: Socio no introducido")
	public void test2() {
		idSocio = (Integer) null;
		assertFalse(model.inscribirSocioEnActividad(idSocio, nombreActividad, fechaInscripcion));	
	}
	
	@Test
	@DisplayName("Test 3: Actividad no existe")
	public void test3() {
		nombreActividad = "ActividadInventada";
		assertFalse(model.inscribirSocioEnActividad(idSocio, nombreActividad, fechaInscripcion));	
	}
	
	@Test
	@DisplayName("Test 4: Actividad no introducida")
	public void test4() {
		nombreActividad = null;
		assertFalse(model.inscribirSocioEnActividad(idSocio, nombreActividad, fechaInscripcion));	
	}
	
	@Test
	@DisplayName("Test 5: Actividad cancelada")
	public void test5() {
		CancelarActividadAdminModel model2 = new CancelarActividadAdminModel();
		nombreActividad = "Clases Pádel Iniciación"; //ID=2
		model2.cancelarActividad(2);
		
		assertFalse(model.inscribirSocioEnActividad(idSocio, nombreActividad, fechaInscripcion));	
	}
	
	@Test
	@DisplayName("Test 6: Actividad no introducida")
	public void test6() {
		nombreActividad = "Conferencia Nutrición Deportiva"; //ID=1
		assertFalse(model.inscribirSocioEnActividad(idSocio, nombreActividad, fechaInscripcion));	
	}
	
	@Test
	@DisplayName("Test 7: Actividad llena")
	public void test7() {
		nombreActividad = "ActividadLlena"; 
		assertFalse(model.inscribirSocioEnActividad(idSocio, nombreActividad, fechaInscripcion));	
	}
	
	@Test
	@DisplayName("Test 8: Inscripcion Sin Fallos")
	public void test8() {
		assertTrue(model.inscribirSocioEnActividad(idSocio, nombreActividad, fechaInscripcion));	
	}
	
	
	
	
	
	
	
	

}