package giis.sisinfo.util;

public class Database extends DbUtil {

    private static final String SQL_SCHEMA = "src/main/resources/schema.sql";
    private static final String SQL_LOAD   = "src/main/resources/data.sql";

    // Para que no se recree sin querer muchas veces
    private static boolean databaseCreated = false;

    public Database() {
        // IMPORTANTE: aquí NO se ejecuta schema.sql ni data.sql
    }

    @Override
    public String getUrl() {
        return "jdbc:sqlite:" + System.getProperty("user.dir") + "/db/sisinfo.db";
    }

    /** Crea la BD (schema). Si onlyOnce=true, solo se ejecuta la primera vez. */
    public void createDatabase(boolean onlyOnce) {
        if (!databaseCreated || !onlyOnce) {
            executeScript(SQL_SCHEMA);
            databaseCreated = true;
        }
    }

    /** Carga datos (data.sql). */
    public void loadDatabase() {
        executeScript(SQL_LOAD);
    }
}