

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import giis.sisinfo.model.InscripcionActividadModel;
import giis.sisinfo.util.Database;

public class InscripcionActividadModelTest {

	private Database db;
	private InscripcionActividadModel model;

	private static final String ACTIVIDAD_TEST = "Actividad Test Inscripcion";
	private static final String ACTIVIDAD_INEXISTENTE = "Actividad Que No Existe";

	@BeforeEach
	public void setUp() {
		db = new Database();
		db.createDatabase(false);
		db.loadDatabase();

		model = new InscripcionActividadModel();

		crearDatosBaseParaTests();
	}

	private void crearDatosBaseParaTests() {
		db.executeUpdate("""
			INSERT OR IGNORE INTO Socio 
				(num_socio, nombre, apellidos, dni, email, telefono, estado, al_corriente_pago)
			VALUES
				(9001, 'Socio', 'Test Uno', '90000001A', 'socio1@test.com', '600000001', 'ACTIVO', 1),
				(9002, 'Socio', 'Test Dos', '90000002B', 'socio2@test.com', '600000002', 'ACTIVO', 1)
		""");

		db.executeUpdate("""
			INSERT OR IGNORE INTO Actividad (
				id_periodo_oficial,
				id_periodo_inscripcion,
				id_instalacion,
				nombre,
				tipo,
				aforo,
				dias,
				duracion,
				fecha_inicio,
				fecha_fin,
				descripcion,
				cuota_socio,
				cuota_nosocio
			)
			VALUES (
				(SELECT id_periodo_oficial FROM PeriodoOficial WHERE nombre = 'SEPTIEMBRE'),
				(SELECT id_periodo_inscripcion FROM PeriodoInscripcion WHERE nombre = 'Inscripción Septiembre'),
				(SELECT id_instalacion FROM Instalacion WHERE nombre_instalacion = 'Sala Multiusos'),
				?,
				'CLASE',
				5,
				1,
				60,
				'2025-09-20',
				'2025-09-20',
				'Actividad creada para pruebas unitarias',
				12.0,
				20.0
			)
		""", ACTIVIDAD_TEST);
	}

	@Test
	public void testInscribirSocioNoInscritoCreaInscripcionActivaYPagoPendiente() {
		int idSocio = getIdSocioPorDni("90000001A");

		boolean resultado = model.inscribirSocioEnActividad(idSocio, ACTIVIDAD_TEST, "2025-09-20");

		assertTrue(resultado, "La inscripción debería realizarse correctamente.");
		assertEquals("ACTIVA", getEstadoInscripcion(idSocio, ACTIVIDAD_TEST),
				"La inscripción creada debería quedar en estado ACTIVA.");
		assertEquals(1, countInscripciones(idSocio, ACTIVIDAD_TEST),
				"No debería crearse más de una inscripción.");
		assertEquals(1, countPagosPendientesInscripcion(idSocio, ACTIVIDAD_TEST),
				"Debería crearse un pago pendiente asociado a la inscripción.");
	}

	@Test
	public void testInscribirSocioYaInscritoDevuelveFalseYNoCreaDuplicado() {
		int idSocio = getIdSocioPorDni("90000001A");

		boolean primerResultado = model.inscribirSocioEnActividad(idSocio, ACTIVIDAD_TEST, "2025-09-20");
		boolean segundoResultado = model.inscribirSocioEnActividad(idSocio, ACTIVIDAD_TEST, "2025-09-20");

		assertTrue(primerResultado, "La primera inscripción debería realizarse correctamente.");
		assertFalse(segundoResultado, "La segunda inscripción del mismo socio debería rechazarse.");
		assertEquals(1, countInscripciones(idSocio, ACTIVIDAD_TEST),
				"No debería crearse una inscripción duplicada.");
	}

	@Test
	public void testInscribirEnActividadInexistenteDevuelveFalseYNoCreaInscripcionNiPago() {
		int idSocio = getIdSocioPorDni("90000001A");

		boolean resultado = model.inscribirSocioEnActividad(idSocio, ACTIVIDAD_INEXISTENTE, "2025-09-20");

		assertFalse(resultado, "La inscripción en una actividad inexistente debería rechazarse.");
		assertEquals(0, countInscripciones(idSocio, ACTIVIDAD_INEXISTENTE),
				"No debería crearse inscripción para una actividad inexistente.");
		assertEquals(0, countPagosPendientesInscripcion(idSocio, ACTIVIDAD_INEXISTENTE),
				"No debería crearse pago para una actividad inexistente.");
	}

	@Test
	public void testCancelarInscripcionExistenteCancelaInscripcionYPago() {
		int idSocio = getIdSocioPorDni("90000001A");

		boolean inscrito = model.inscribirSocioEnActividad(idSocio, ACTIVIDAD_TEST, "2025-09-20");
		boolean cancelado = model.cancelarInscripcionYPromover(idSocio, ACTIVIDAD_TEST);

		assertTrue(inscrito, "La inscripción inicial debería realizarse correctamente.");
		assertTrue(cancelado, "La cancelación debería realizarse correctamente.");
		assertEquals("CANCELADA", getUltimoEstadoInscripcionIncluyendoCanceladas(idSocio, ACTIVIDAD_TEST),
				"La inscripción debería quedar en estado CANCELADA.");
		assertEquals(1, countPagosPendientesInscripcion(idSocio, ACTIVIDAD_TEST),
				"El pago no se modifica tras cancelar la inscripción.");
	}

	@Test
	public void testCancelarInscripcionInexistenteDevuelveFalseYNoModificaBD() {
		int idSocio = getIdSocioPorDni("90000001A");

		int inscripcionesAntes = countTodasLasInscripciones();
		int pagosAntes = countTodosLosPagos();

		boolean resultado = model.cancelarInscripcionYPromover(idSocio, ACTIVIDAD_TEST);

		assertFalse(resultado, "No debería poder cancelarse una inscripción inexistente.");
		assertEquals(inscripcionesAntes, countTodasLasInscripciones(),
				"No debería modificarse el número de inscripciones.");
		assertEquals(pagosAntes, countTodosLosPagos(),
				"No debería modificarse el número de pagos.");
	}

	@Test
	public void testGetEstadoInscripcionSocioInscritoDevuelveActiva() {
		int idSocio = getIdSocioPorDni("90000001A");

		model.inscribirSocioEnActividad(idSocio, ACTIVIDAD_TEST, "2025-09-20");

		assertEquals("ACTIVA", model.getEstadoInscripcionSocio(idSocio, ACTIVIDAD_TEST),
				"El estado consultado debería ser ACTIVA.");
	}

	@Test
	public void testGetEstadoInscripcionSocioNoInscritoDevuelveNull() {
		int idSocio = getIdSocioPorDni("90000002B");

		assertNull(model.getEstadoInscripcionSocio(idSocio, ACTIVIDAD_TEST),
				"Un socio no inscrito no debería tener estado de inscripción.");
	}

	@Test
	public void testEstaYaInscritoTrasCancelarDevuelveFalse() {
		int idSocio = getIdSocioPorDni("90000001A");

		model.inscribirSocioEnActividad(idSocio, ACTIVIDAD_TEST, "2025-09-20");
		model.cancelarInscripcionYPromover(idSocio, ACTIVIDAD_TEST);

		assertFalse(model.estaYaInscrito(idSocio, ACTIVIDAD_TEST, "2025-09-20"),
				"Una inscripción CANCELADA no debería contar como inscripción activa.");
	}

	private int getIdSocioPorDni(String dni) {
		List<Object[]> rows = db.executeQueryArray("""
			SELECT id_socio
			FROM Socio
			WHERE dni = ?
		""", dni);

		assertFalse(rows.isEmpty(), "Debe existir el socio con DNI " + dni);
		return ((Number) rows.get(0)[0]).intValue();
	}

	private String getEstadoInscripcion(int idSocio, String nombreActividad) {
		List<Object[]> rows = db.executeQueryArray("""
			SELECT ia.estado
			FROM Inscripcion_Actividad ia
			JOIN Actividad a ON ia.id_actividad = a.id_actividad
			WHERE ia.id_socio = ?
			  AND a.nombre = ?
			  AND ia.estado IN ('ACTIVA', 'CANCELADA')
			ORDER BY ia.id_inscripcion DESC
			LIMIT 1
		""", idSocio, nombreActividad);

		if (rows.isEmpty()) {
			return null;
		}
		return (String) rows.get(0)[0];
	}

	private String getUltimoEstadoInscripcionIncluyendoCanceladas(int idSocio, String nombreActividad) {
		return getEstadoInscripcion(idSocio, nombreActividad);
	}

	private int countInscripciones(int idSocio, String nombreActividad) {
		List<Object[]> rows = db.executeQueryArray("""
			SELECT COUNT(*)
			FROM Inscripcion_Actividad ia
			JOIN Actividad a ON ia.id_actividad = a.id_actividad
			WHERE ia.id_socio = ?
			  AND a.nombre = ?
		""", idSocio, nombreActividad);

		return ((Number) rows.get(0)[0]).intValue();
	}

	private int countPagosPendientesInscripcion(int idSocio, String nombreActividad) {
		List<Object[]> rows = db.executeQueryArray("""
			SELECT COUNT(*)
			FROM Pago p
			JOIN Actividad a ON p.id_actividad = a.id_actividad
			WHERE p.id_socio = ?
			  AND a.nombre = ?
			  AND p.concepto = 'INSCRIPCION'
			  AND p.estado = 'PENDIENTE'
		""", idSocio, nombreActividad);

		return ((Number) rows.get(0)[0]).intValue();
	}

	private int countPagosCanceladosInscripcion(int idSocio, String nombreActividad) {
		List<Object[]> rows = db.executeQueryArray("""
			SELECT COUNT(*)
			FROM Pago p
			JOIN Actividad a ON p.id_actividad = a.id_actividad
			WHERE p.id_socio = ?
			  AND a.nombre = ?
			  AND p.concepto = 'INSCRIPCION'
			  AND p.estado = 'CANCELADO'
		""", idSocio, nombreActividad);

		return ((Number) rows.get(0)[0]).intValue();
	}

	private int countTodasLasInscripciones() {
		List<Object[]> rows = db.executeQueryArray("""
			SELECT COUNT(*)
			FROM Inscripcion_Actividad
		""");

		return ((Number) rows.get(0)[0]).intValue();
	}

	private int countTodosLosPagos() {
		List<Object[]> rows = db.executeQueryArray("""
			SELECT COUNT(*)
			FROM Pago
		""");

		return ((Number) rows.get(0)[0]).intValue();
	}
}