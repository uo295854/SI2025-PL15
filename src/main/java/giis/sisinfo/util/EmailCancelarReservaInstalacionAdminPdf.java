package giis.sisinfo.util;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import giis.sisinfo.dto.EmailCancelarReservaInstalacionAdminDTO;

public class EmailCancelarReservaInstalacionAdminPdf {

    public String generar(EmailCancelarReservaInstalacionAdminDTO a) throws IOException {
        String nombreArchivo = "aviso_cancelacion_reserva_" + System.currentTimeMillis() + ".pdf";
        String ruta = System.getProperty("user.home") + File.separator + nombreArchivo;

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.newLineAtOffset(60, 750);
                cs.showText("CORREO ENVIADO AL SOCIO AL CANCELAR SU RESERVA");

                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(0, -40);
                cs.showText("Estimado/a socio/a: " + a.getNombreSocio() + ".");

                cs.newLineAtOffset(0, -20);
                cs.showText("Le informamos que hemos tenido que cancelar su reserva el día "
                        + a.getFecha() + ", " + a.getDia() + ", con hora " + a.getHoraEntrada()
                        + " debido al siguiente motivo:");

                cs.newLineAtOffset(0, -30);
                escribirTextoMultilinea(cs, a.getMotivo(), 60, 640, 16);

                cs.newLineAtOffset(0, -50);
                cs.showText("Disculpe las molestias.");

                cs.newLineAtOffset(0, -20);
                cs.showText("Un saludo,");

                cs.newLineAtOffset(0, -20);
                cs.showText(a.getCentro() + ".");

                cs.newLineAtOffset(0, -50);
                cs.showText("Dirección:     " + limpiarTextoPdf(a.getDireccion()));

                cs.newLineAtOffset(0, -20);
                cs.showText("Municipio:     " + limpiarTextoPdf(a.getMunicipio()));

                cs.newLineAtOffset(0, -20);
                cs.showText("Provincia:     " + limpiarTextoPdf(a.getProvincia()));

                cs.newLineAtOffset(0, -20);
                cs.showText("Email:         " + limpiarTextoPdf(a.getEmailCentro()));

                cs.newLineAtOffset(0, -20);
                cs.showText("Teléfono:      " + limpiarTextoPdf(a.getTelefonoCentro()));

                cs.endText();
            }

            doc.save(ruta);
        }

        return ruta;
    }

    private void escribirTextoMultilinea(PDPageContentStream cs, String texto, float x, float y, float saltoLinea)
            throws IOException {

        if (texto == null || texto.trim().isEmpty()) {
            cs.newLineAtOffset(0, -saltoLinea);
            cs.showText("");
            return;
        }

        String[] lineas = texto.split("\\n");
        for (int i = 0; i < lineas.length; i++) {
            if (i == 0) {
                cs.showText(lineas[i]);
            } else {
                cs.newLineAtOffset(0, -saltoLinea);
                cs.showText(lineas[i]);
            }
        }
    }
    
    private String limpiarTextoPdf(String texto) {
        if (texto == null) {
            return "";
        }
        return texto
                .replace("\t", "    ")
                .replace("\r", "")
                .replace("\n", " ");
    }
}