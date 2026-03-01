package giis.sisinfo.util;

public class Database extends DbUtil {

	public Database() {
		executeScript("src/main/resources/schema.sql");
		executeScript("src/main/resources/data.sql");
	}

	@Override
	public String getUrl() {
		return "jdbc:sqlite:" + System.getProperty("user.dir") + "/db/sisinfo.db";
	}
}