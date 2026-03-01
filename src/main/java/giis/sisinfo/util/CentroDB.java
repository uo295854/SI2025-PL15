package giis.sisinfo.util;

public class CentroDB extends DbUtil {

    // OJO: ajusta la ruta donde quieras que se cree el .db
    // Recomendado: en la raíz del proyecto o en target/
    private static final String URL = "jdbc:sqlite:target/centro_deportivo.db";

    @Override
    public String getUrl() {
        return URL;
    }
}