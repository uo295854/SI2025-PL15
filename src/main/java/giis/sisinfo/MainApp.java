package giis.sisinfo;

import giis.sisinfo.controller.LoginController;
import giis.sisinfo.model.LoginModel;
import giis.sisinfo.util.Database;
import giis.sisinfo.view.LoginView;

public class MainApp {
	public static void main(String[] args) {
		Database db = new Database();
		LoginView v = new LoginView();
		LoginModel m = new LoginModel(db);
		new LoginController(v, m, db);
	}
}