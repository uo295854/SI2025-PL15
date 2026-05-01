package tests.alex;

import giis.sisinfo.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


import giis.sisinfo.dto.ActividadDTO;
import giis.sisinfo.model.PlanificarActividadesModel;

public class TestAlex1 {
	
	public ActividadDTO actividadDefault () {
		String instalacionNombre = "Pista Pádel 1";
		String periodoInscripcionNombre = "Inscripción Septiembre";
		String tipoUi = "CULTURAL";
		int aforo = 12;
		String fechaInicio = "2025-09-01";
		String fechaFin = "2025-09-01";
		int duracionMinutos = 120;
		double cuotaSocio = 3.0;
		double cuotaNoSocio = 6.0;
		Map<String, String[]> horariosPorDia = new HashMap<>();
		horariosPorDia.put("Lunes",    new String[] { "10:00", "11:30" });
		
		return new ActividadDTO(periodoInscripcionNombre, instalacionNombre, tipoUi, aforo, horariosPorDia, duracionMinutos, periodoInscripcionNombre, fechaInicio, fechaFin, cuotaSocio, cuotaNoSocio, periodoInscripcionNombre);
		
	}
	
	@BeforeEach
	public void setup() {
		Database db = new Database();
		try {
			db.createDatabase(false);
			db.loadDatabase();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Test 1: Instalacion No existe")
	public void test1() {
		ActividadDTO actividad = actividadDefault();
		actividad.setInstalacionNombre("NombreInventado");
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 2: Instalacion No introducida")
	public void test2() {
		ActividadDTO actividad = actividadDefault();
		actividad.setInstalacionNombre(null);
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 3: Periodo Inscripción No Existe")
	public void test3() {
		ActividadDTO actividad = actividadDefault();
		actividad.setPeriodoInscripcionNombre("PeriodoInventado");
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 4: Tipo de Actividad Invalido")
	public void test4() {
		ActividadDTO actividad = actividadDefault();
		actividad.setTipoUi("TipoInventado");
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
//			e.printStackTrace();
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 5: Aforo = 0 (Invalido)")
	public void test5() {
		ActividadDTO actividad = actividadDefault();
		actividad.setAforo(0);
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 6: Aforo < 0 (Invalido)")
	public void test6() {
		ActividadDTO actividad = actividadDefault();
		actividad.setAforo(-20);
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 7: FechaInicio>FechaFin")
	public void test7() {
		ActividadDTO actividad = actividadDefault();
		actividad.setFechaFin("2025-09-20");
		actividad.setFechaInicio("2025-09-22");
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 8: FechaInicio no introducida")
	public void test8() {
		ActividadDTO actividad = actividadDefault();
		actividad.setFechaFin("2025-09-20");
		actividad.setFechaInicio(null);
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
//			e.printStackTrace();
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 9: FechaFin no introducida")
	public void test9() {
		ActividadDTO actividad = actividadDefault();
		actividad.setFechaFin(null);
		actividad.setFechaInicio("2025-09-22");
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
//			e.printStackTrace();
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 10: Duracion = 0")
	//este test falla porque no se comprueba si la duracion de la actividad es 0
	public void test10() {
		ActividadDTO actividad = actividadDefault();
		actividad.setDuracionMinutos(0);
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
//			e.printStackTrace();
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 11: Duracion < 0")
	//este test falla porque no se comprueba si la duracion de la actividad es positiva
	public void test11() {
		ActividadDTO actividad = actividadDefault();
		actividad.setDuracionMinutos(-10);
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
//			e.printStackTrace();
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 12: CuotaSocio < 0")
	public void test12() {
		ActividadDTO actividad = actividadDefault();
		actividad.setCuotaSocio(-10);
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
//			e.printStackTrace();
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 13: CuotaNoSocio < 0")
	public void test13() {
		ActividadDTO actividad = actividadDefault();
		actividad.setCuotaNoSocio(-20);
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(false);
		} catch (Exception e) {
//			e.printStackTrace();
			assertTrue(true);
		}
	}
	
	@Test
	@DisplayName("Test 14: TipoActividad = DEPORTIVA")
	public void test14() {
		ActividadDTO actividad = actividadDefault();
		actividad.setTipoUi("DEPORTIVA");
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Test 15: TipoActividad = CLASE")
	public void test15() {
		ActividadDTO actividad = actividadDefault();
		actividad.setTipoUi("CLASE");
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	
	@Test
	@DisplayName("Test 16: TipoActividad = CAMPEONATO")
	public void test16() {
		ActividadDTO actividad = actividadDefault();
		actividad.setTipoUi("CAMPEONATO");
		
		PlanificarActividadesModel model = new PlanificarActividadesModel();
		try {
			model.crearActividad(actividad);
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
