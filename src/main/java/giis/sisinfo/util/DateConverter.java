package giis.sisinfo.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

//clase creada para almacenar funciones que conviertan fechas en formato que nos da un DateChooser a otro formato
public class DateConverter {

	
	//convierte un texto en formato "EEE MMM dd HH:mm:ss z yyyy" que nos dan los dateSelector a un formato yyyy-MM-dd
    public static String convertirFecha(String textoFecha) {
        // Formato de la fecha inicial
        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern(
            "EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH
        );

        // Parseamos el string a ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(textoFecha, formatterEntrada);

        // Obtenemos solo la fecha
        LocalDate fecha = zonedDateTime.toLocalDate();

        // Formateamos a "yyyy-MM-dd"
        DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fecha.format(formatterSalida);
    }
    
    
    //convierte un texto en formato "EEE MMM dd HH:mm:ss z yyyy" que nos dan los dateSelector a un formato HH:mm
    public static String convertirHora(String textoHora) {
    	// Formato de la fecha inicial
        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern(
            "EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH
        );
        
        // Parseamos el string a ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(textoHora, formatterEntrada);
        
     // Obtenemos solo la fecha
        LocalTime hora = zonedDateTime.toLocalTime();
        
        DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("HH:mm");
        return hora.format(formatterSalida);
    }
    
    public static String mesTextoANumero(String mes) {
    	switch(mes) {
    		case "Enero": 		return "01";
    		case "Febrero": 	return "02";
    		case "Marzo": 		return "03";
    		case "Abril": 		return "04";
    		case "Mayo": 		return "05";
    		case "Junio": 		return "06";
    		case "Julio": 		return "07";
    		case "Agosto": 		return "08";
    		case "Septiembre": 	return "09";
    		case "Octubre": 	return "10";
    		case "Noviembre": 	return "11";
    		case "Diciembre": 	return "12";
    	}
    	return "00";
    }
}
