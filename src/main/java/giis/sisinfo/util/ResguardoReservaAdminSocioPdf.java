package giis.sisinfo.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import giis.sisinfo.dto.ResguardoReservaAdminSocioDTO;

public class ResguardoReservaAdminSocioPdf {
	
    public String generar(ResguardoReservaAdminSocioDTO r) throws IOException {
        String nombreArchivo = "resguardo_reserva_" + System.currentTimeMillis() + ".pdf";
        String ruta = System.getProperty("user.home") + File.separator + nombreArchivo;

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.newLineAtOffset(60, 750);
                cs.showText("RESGUARDO DE RESERVA");

                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(0, -30);
                cs.showText("DATOS DEL SOCIO");

                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(0, -20);
                cs.showText("Nombre: " + r.getNombreSocio());
                cs.newLineAtOffset(0, -15);
                cs.showText("Nº socio: " + r.getNumSocio());
                cs.newLineAtOffset(0, -15);
                cs.showText("Teléfono: " + r.getTelefono());
                cs.newLineAtOffset(0, -15);
                cs.showText("Email: " + r.getEmail());

                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(0, -30);
                cs.showText("DATOS DEL CENTRO");

                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(0, -20);
                cs.showText("Centro: " + r.getCentro());
                cs.newLineAtOffset(0, -15);
                cs.showText("Dirección: " + r.getDireccion());
                cs.newLineAtOffset(0, -15);
                cs.showText("Municipio: " + r.getMunicipio());
                cs.newLineAtOffset(0, -15);
                cs.showText("Provincia: " + r.getProvincia());

                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(0, -30);
                cs.showText("DATOS DE LA RESERVA");

                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(0, -20);
                cs.showText("Deporte: " + r.getDeporte());
                cs.newLineAtOffset(0, -15);
                cs.showText("Instalación: " + r.getInstalacion());
                cs.newLineAtOffset(0, -15);
                cs.showText("Fecha: " + r.getFecha().toString());
                cs.newLineAtOffset(0, -15);
                cs.showText("Horas: " + formatearHoras(r));
                cs.newLineAtOffset(0, -15);
                cs.showText("Cuota: " + r.getCuota() + " €");

                cs.newLineAtOffset(0, -30);
                if ("PENDIENTE".equals(r.getEstadoPago())) {
                    cs.showText("El importe se incluirá en el recibo mensual.");
                } else {
                    cs.showText("El importe ha quedado abonado.");
                }

                cs.newLineAtOffset(0, -20);
                cs.showText("Reserva confirmada. Gracias por utilizar nuestras instalaciones.");

                cs.endText();
            }

            doc.save(ruta);
        }

        return ruta;
    }

    private String formatearHoras(ResguardoReservaAdminSocioDTO r) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < r.getHoras().size(); i++) {
            LocalTime ini = r.getHoras().get(i);
            LocalTime fin = ini.plusHours(1);
            if (i > 0) sb.append(", ");
            sb.append(ini.format(fmt)).append("-").append(fin.format(fmt));
        }
        return sb.toString();
    }

}
