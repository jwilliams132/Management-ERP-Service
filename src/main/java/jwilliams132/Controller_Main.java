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

	private Controller_Customers customersController;
	private Controller_Dashboard dashboardController;
	private Controller_Inventory inventoryController;
	private Controller_Purchases purchasesController;
	private Controller_Reports reportsController;
	private Controller_Sales salesController;
	private Controller_Settings settingsController;
	private Controller_Startup startupController;

	private Stored_Files_Manager storageManager = new Stored_Files_Manager();

	private Display currentDisplay = Display.SALES;

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
		loadDisplayFXML(currentDisplay);
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

		String fxml = getFXMLFileName(currentDisplay);
		FXMLLoader loader;
		try {

			loader = new FXMLLoader(getClass().getResource(fxml));
			display = loader.load();
			mainPane.setCenter(display);

		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}

		switch (currentDisplay) {

			case CUSTOMERS:
				customersController = loader.getController();
				break;

			case DASHBOARD:
				dashboardController = loader.getController();
				break;

			case INVENTORY:
				inventoryController = loader.getController();
				inventoryController.setStorageManager(storageManager);
				inventoryController.setup();
				break;

			case PURCHASES:
				purchasesController = loader.getController();
				break;

			case REPORTS:
				reportsController = loader.getController();
				break;

			case SALES:
			System.out.println("Bitch");
				salesController = loader.getController();
				salesController.setStorageManager(storageManager);
				salesController.setup();
				break;

			case SETTINGS:
				settingsController = loader.getController();
				settingsController.setStorageManager(storageManager);
				settingsController.setup();
				break;

			case STARTUP:
				loader.getController();
				startupController = loader.getController();
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + currentDisplay);
		}

		return true;
	}

	private String getFXMLFileName(Display display) {

		switch (currentDisplay) {

			case CUSTOMERS:
				return "Customer.fxml";

			case DASHBOARD:
				return "Dashboard.fxml";

			case INVENTORY:
				return "Inventory.fxml";

			case PURCHASES:
				return "Purchases.fxml";

			case REPORTS:
				return "Reports.fxml";

			case SALES:
				return "Sales.fxml";

			case SETTINGS:
				return "Settings.fxml";

			case STARTUP:
				return "Startup.fxml";

			default:
				throw new IllegalArgumentException("Unexpected value: " + currentDisplay);
		}
	}
}
