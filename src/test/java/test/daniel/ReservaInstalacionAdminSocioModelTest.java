package test.daniel;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import giis.sisinfo.model.ReservaInstalacionAdminSocioModel;
import giis.sisinfo.util.Database;

public class ReservaInstalacionAdminSocioModelTest {
	private ReservaInstalacionAdminSocioModel model;
	private Database db;

	@BeforeEach
	void setUp() {

		model = new ReservaInstalacionAdminSocioModel();
		db = new Database();

		limpiarBD();
		//Se carga la base de datos
		cargarBaseDatos();
	}

	private void limpiarBD() {
		//Se resetea la base de datos
		db.executeUpdate("DELETE FROM Pago");
		db.executeUpdate("DELETE FROM Bloqueo_por_Actividad");
		db.executeUpdate("DELETE FROM Reserva_Instalacion");
		db.executeUpdate("DELETE FROM Inscripcion_Actividad");
		db.executeUpdate("DELETE FROM Actividad");
		db.executeUpdate("DELETE FROM PeriodoInscripcion");
		db.executeUpdate("DELETE FROM PeriodoOficial");
		db.executeUpdate("DELETE FROM Instalacion");
		db.executeUpdate("DELETE FROM Usuario");
		db.executeUpdate("DELETE FROM Socio");
	}

	private void cargarBaseDatos() {
		//Se inserta un socio que está tanto al corriente de pago como activo
		db.executeUpdate(
			"INSERT INTO Socio (id_socio, num_socio, nombre, apellidos, dni, email, telefono, estado, al_corriente_pago) "
			+ "VALUES (1, 1001, 'Ana', 'Lopez', '11454311A', 'ana@gmail.com', '640217181', 'ACTIVO', 1)");

		//Se inserta un socio que no está activo
		db.executeUpdate(
			"INSERT INTO Socio (id_socio, num_socio, nombre, apellidos, dni, email, telefono, estado, al_corriente_pago) "
			+ "VALUES (2, 1002, 'Julian', 'Perez', '23332422B', 'julian@gmail.com', '603272842', 'BAJA', 1)");

		//Se inserta un socio que no está al corriente de pago
		db.executeUpdate(
			"INSERT INTO Socio (id_socio, num_socio, nombre, apellidos, dni, email, telefono, estado, al_corriente_pago) "
			+ "VALUES (3, 1003, 'Marta', 'Garcia', '33557893C', 'marta@gmail.com', '640353633', 'ACTIVO', 0)");

		//Se inserta instalacion
		db.executeUpdate(
			"INSERT INTO Instalacion (id_instalacion, nombre_instalacion, tipo_deporte, tipo_instalacion, aforo_max, coste) "
			+ "VALUES (10, 'Pista Padel 1', 'Padel', 'CANCHA', 4, 8.0)");

		//Se inserta instalacion
		db.executeUpdate(
			"INSERT INTO Instalacion (id_instalacion, nombre_instalacion, tipo_deporte, tipo_instalacion, aforo_max, coste) "
			+ "VALUES (11, 'Pista Padel 2', 'Padel', 'CANCHA', 4, 8.0)");

		//Obligatorio insertar para después trabajar con actividades
		db.executeUpdate(
			"INSERT INTO PeriodoOficial (id_periodo_oficial, nombre, fecha_ini, fecha_fin) "
			+ "VALUES (1, 'SEPTIEMBRE', '2026-09-01', '2026-12-31')");

		//Obligatorio insertar para después trabajar con actividades
		db.executeUpdate(
			"INSERT INTO PeriodoInscripcion (id_periodo_inscripcion, nombre, descripcion, fecha_inicio_socio, fecha_fin_socio, fecha_fin_nosocio) "
			+ "VALUES (1, 'Periodo test', 'Periodo de prueba', '2026-01-01', '2026-12-15', '2026-12-31')");
	}

	@Test
	void CP1_ReservaValida() {
		LocalDate fecha = LocalDate.now().plusDays(1);
		LocalTime hora = LocalTime.of(12, 0);
		
		//Se crea una reserva en el día de mañana a las 12:00 - 13:00
		model.crearReserva(10, 1, fecha, List.of(hora), "PENDIENTE");
		
		//Se comprueba que se puede crear y que está ya en la base de datos
		assertEquals(1, ComprobarReservaConcreta(1, 10, fecha, hora));
		
		//Se comprueba que el pago asociado a la reserva se genera
		assertEquals(1, ComprobarPagoReserva(1, "PENDIENTE"));
	}

	@Test
	void CP2_SocioNoActivo() {
		LocalDate fecha = LocalDate.now().plusDays(1);
		LocalTime hora = LocalTime.of(12, 0);
		
		//Se coge la excepción que produce un socio registrado anteriormente que no está en estado Activo
		IllegalStateException e = assertThrows(IllegalStateException.class, () ->model.crearReserva(10, 2, fecha, List.of(hora), "PENDIENTE"));

		//Se comprueba que el mensaje de la excepción es el mismo
		assertEquals("Socio no autorizado para reservar.", e.getMessage());
		
		//Se comprueba que no se ha registrado ninguna reserva asociada a ese socio
		assertEquals(0, ComprobarReservaConcreta(2, 10, fecha, hora));
	}

	@Test
	void CP3_SocioNoAlCorriente() {
		LocalDate fecha = LocalDate.now().plusDays(1);
		LocalTime hora = LocalTime.of(12, 0);

		//Se coge la excepción que produce un socio registrado anteriormente que no está al corriente de pagos con el centro deportivo
		IllegalStateException e = assertThrows(IllegalStateException.class, () ->model.crearReserva(10, 3, fecha, List.of(hora), "PENDIENTE"));

		//Se comprueba que el mensaje de la excepción es el mismo
		assertEquals("Socio no autorizado para reservar.", e.getMessage());
		
		//Se comprueba que no se ha registrado ninguna reserva asociada a ese socio
		assertEquals(0, ComprobarReservaConcreta(3, 10, fecha, hora));
	}

	@Test
	void CP4_FechaSuperiorA15Dias() {
		LocalDate fecha = LocalDate.now().plusDays(16);
		LocalTime hora = LocalTime.of(12, 0);

		//Se coge la excepción que se produce cuando se intenta reservar una instalación para más de 15 días
		IllegalStateException e = assertThrows(IllegalStateException.class, () ->model.crearReserva(10, 1, fecha, List.of(hora), "PENDIENTE"));

		//Se comprueba que el mensaje de la excepción es el mismo
		assertEquals("No se puede reservar con más de 15 días de antelación.", e.getMessage());
		
		//Se comprueba que no se ha registrado ninguna reserva asociada a ese socio
		assertEquals(0, ComprobarReservaConcreta(1, 10, fecha, hora));
	}

	@Test
	void CP5_InstalacionOcupadaPorReserva() {
		LocalDate fecha = LocalDate.now().plusDays(1);
		LocalTime hora = LocalTime.of(12, 0);
		
		//Se inserta una reserva para que ocupe la misma franja horaria
		insertarReservaActiva(1, 10, 1, fecha, hora);

		//Se coge la excepción que se produce al intentar crear una reserva en la misma hora en la misma instalacion
		IllegalStateException e = assertThrows(IllegalStateException.class, () ->model.crearReserva(10, 1, fecha, List.of(hora), "PENDIENTE"));

		//Se comprueba que el mensaje de la excepción es el mismo
		assertEquals("La franja horaria ya no está disponible.", e.getMessage());
	}

	@Test
	void CP6_InstalacionBloqueadaPorActividad() {
		LocalDate fecha = LocalDate.now().plusDays(1);
		LocalTime hora = LocalTime.of(12, 0);
		
		//Se inserta una actividad que ocupe la instalacion a una hora
		db.executeUpdate(
			"INSERT INTO Actividad (id_actividad, id_periodo_oficial, id_periodo_inscripcion, id_instalacion, nombre, tipo, aforo, dias, duracion, fecha_inicio, fecha_fin, descripcion, cuota_socio, cuota_nosocio, estado) "
			+ "VALUES (200, 1, 1, 10, 'Clase Padel Test', 'DEPORTIVA', 10, 1, 60, ?, ?, 'Actividad de prueba', 0, 0, 'ACTIVA')",fecha.toString(),fecha.toString());
		
		//Se inserta una actividad que ocupe la instalacion a una hora (reserva)
		db.executeUpdate(
			"INSERT INTO Bloqueo_por_Actividad (id_bloqueo, id_actividad, datetime_ini, datetime_fin, estado_reserva) "
			+ "VALUES (300, 200, ?, ?, 'RESERVADO')",fecha.atTime(hora).toString().replace('T', ' '),fecha.atTime(13, 0).toString().replace('T', ' '));

		//Se coge la excepción de no poder reservar a esa hora por culpa de la actividad
		IllegalStateException e = assertThrows(IllegalStateException.class, () ->model.crearReserva(10, 1, fecha, List.of(hora), "PENDIENTE"));

		//Se comprueba que el mensaje de la excepción es el mismo
		assertEquals("La franja horaria ya no está disponible.", e.getMessage());
		
		//Se comprueba que no se ha creado la reserva
		assertEquals(0, ComprobarReservaConcreta(1, 10, fecha, hora));
	}

	@Test
	void CP7_SociosuperaTresHorasDia() {
		LocalDate fecha = LocalDate.now().plusDays(1);

		//Se insertan dos horas de reservas para un socio
		insertarReservaActiva(1, 10, 1, fecha, LocalTime.of(9, 0));
		insertarReservaActiva(2, 11, 1, fecha, LocalTime.of(10, 0));

		//Al intentar añadir dos horas más, se supera el límite diario de tres horas y salta una excepción que se recoge
		IllegalStateException e = assertThrows(IllegalStateException.class, () ->model.crearReserva(10,1,fecha,List.of(LocalTime.of(12, 0), LocalTime.of(13, 0)),"PENDIENTE"));

		//Se comprueba que el mensaje de la excepción es el mismo
		assertEquals("No puedes reservar más de 3 horas al día ", e.getMessage());
		
		//Se comprueba que no se han creado las nuevas reservas
		assertEquals(0, ComprobarReservaConcreta(1, 10, fecha, LocalTime.of(12, 0)));
		assertEquals(0, ComprobarReservaConcreta(1, 10, fecha, LocalTime.of(13, 0)));
	}

	//Método auxiliar para insertar una reserva para determinados casos de prueba
	private void insertarReservaActiva(int idReserva, int idInstalacion, int idSocio, LocalDate fecha, LocalTime hora) {
		db.executeUpdate(
			"INSERT INTO Reserva_Instalacion (id_reservains, id_instalacion, id_socio, datetime_ini, datetime_fin, estado) "
			+ "VALUES (?, ?, ?, ?, ?, 'ACTIVA')",idReserva,idInstalacion,idSocio,fecha.atTime(hora).toString().replace('T', ' '),
			fecha.atTime(hora.plusHours(1)).toString().replace('T', ' '));
	}

	
	//Métodos auxiliares para comprobar que una reserva concreta existe o no y un pago existe o no creados en la base de datos
	
	private long ComprobarReservaConcreta(int idSocio, int idInstalacion, LocalDate fecha, LocalTime hora) {
		String ini = fecha.atTime(hora).toString().replace('T', ' ');
		String fin = fecha.atTime(hora.plusHours(1)).toString().replace('T', ' ');

		String sql = "SELECT COUNT(*) FROM Reserva_Instalacion "
				+ "WHERE id_socio=? "
				+ "AND id_instalacion=? "
				+ "AND datetime_ini=? "
				+ "AND datetime_fin=?";

		return ((Number) db.executeQueryArray(sql, idSocio, idInstalacion, ini, fin).get(0)[0]).longValue();
	}
	
	private long ComprobarPagoReserva(int idSocio, String estadoPago) {
		String sql = "SELECT COUNT(*) FROM Pago "
				+ "WHERE id_socio=? "
				+ "AND concepto='RESERVA' "
				+ "AND estado=?";

		return ((Number) db.executeQueryArray(sql, idSocio, estadoPago).get(0)[0]).longValue();
	}
}
