package giis.sisinfo;

import javax.swing.SwingUtilities;

import giis.sisinfo.controller.MainController;
import giis.sisinfo.view.MainView;

public class MainApp {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainView view = new MainView();
			new MainController(view);
			view.setVisible(true);
		});
	}
}
