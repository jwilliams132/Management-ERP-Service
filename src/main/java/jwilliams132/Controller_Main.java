package jwilliams132;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Controller_Main {

	@FXML
	private VBox navBar;
	@FXML
	private MenuBar menuBar;
	@FXML
	private BorderPane mainPane;

	private Display currentDisplay = Display.STARTUP;

	@FXML
	private void initialize() {

		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NavBar.fxml"));
			mainPane.setLeft(loader.load());
			Controller_NavBar navController = loader.getController();
			navController.setMainController(this);
		} catch (IOException e) {
			
			System.err.println("Issue with loading the NavBar fxml.");
			e.printStackTrace();
		}
	}

	@FXML
	public void openFiles() {

	}

	@FXML
	public void updateInfo() {

	}

	@FXML
	public void saveFiles() {

	}

	@FXML
	public void exportExcel() {

	}

	public boolean loadDisplayFXML(Display currentDisplay) {

		// Display previousDisplay = this.currentDisplay;
		this.currentDisplay = currentDisplay;
		Node display = null;

		String fxml;
		switch (currentDisplay) {

			case STARTUP:
				fxml = "Startup.fxml";
				break;

			case DASHBOARD:
				fxml = "Dashboard.fxml";
				break;

			case SALES:
				fxml = "Sales.fxml";
				break;

			case PURCHASES:
				fxml = "Purchases.fxml";
				break;

			case INVENTORY:
				fxml = "Inventory.fxml";
				break;

			case REPORTS:
				fxml = "Reports.fxml";
				break;

			case SETTINGS:
				fxml = "Settings.fxml";
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + currentDisplay);
		}

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));

			display = loader.load();
			mainPane.setCenter(display);

		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}
		return true;
	}
}
