package giis.sisinfo.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

	private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
	private static final DateTimeFormatter SLASH = DateTimeFormatter.ofPattern("d/M/uuuu"); // 1/9/2026

	private DateUtil() {}

	/**
	 * Acepta "yyyy-MM-dd" o "d/M/yyyy" (como en tu wireframe).
	 * Devuelve LocalDate o lanza IllegalArgumentException.
	 */
	public static LocalDate parseFlexible(String text) {
		if (text == null) throw new IllegalArgumentException("Fecha vacía");
		String t = text.trim();
		if (t.isEmpty()) throw new IllegalArgumentException("Fecha vacía");

		// intenta ISO
		try { return LocalDate.parse(t, ISO); }
		catch (DateTimeParseException ignored) {}

		// intenta d/M/yyyy
		try { return LocalDate.parse(t, SLASH); }
		catch (DateTimeParseException ignored) {}

		throw new IllegalArgumentException("Formato de fecha inválido: " + text + " (usa yyyy-MM-dd o d/M/yyyy)");
	}

	public static String toIso(LocalDate d) {
		return d.format(ISO);
	}
}