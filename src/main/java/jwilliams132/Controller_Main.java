package jwilliams132;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Controller_Main {

	private App app;

	@FXML
	private VBox navBar;
	@FXML
	private MenuBar menuBar;
	@FXML
	private BorderPane mainPane;

	private Controller_NavBar navController;
	private Controller_Accounting accountingController;
	private Controller_Customers customersController;
	private Controller_Dashboard dashboardController;
	private Controller_Inventory inventoryController;
	private Controller_Purchases purchasesController;
	private Controller_Reports reportsController;
	private Controller_Sales salesController;
	private Controller_Settings settingsController;

	private Stored_Files_Manager storageManager = new Stored_Files_Manager();

	private Display currentDisplay = Display.INVENTORY;

	@FXML
	private void initialize() {

		System.out.println(" _____                                                                   _____ \r\n" + //
				"( ___ )                                                                 ( ___ )\r\n" + //
				" |   |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|   | \r\n" + //
				" |   |                                                                   |   | \r\n" + //
				" |   |   _____ ____  ____    ____                                        |   | \r\n" + //
				" |   |  | ____|  _ \\|  _ \\  |  _ \\ _ __ ___   __ _ _ __ __ _ _ __ ___    |   | \r\n" + //
				" |   |  |  _| | |_) | |_) | | |_) | '__/ _ \\ / _` | '__/ _` | '_ ` _ \\   |   | \r\n" + //
				" |   |  | |___|  _ <|  __/  |  __/| | | (_) | (_| | | | (_| | | | | | |  |   | \r\n" + //
				" |   |  |_____|_| \\_\\_|     |_|   |_|  \\___/ \\__, |_|  \\__,_|_| |_| |_|  |   | \r\n" + //
				" |   |                                       |___/                       |   | \r\n" + //
				" |   |                                                                   |   | \r\n" + //
				" |___|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|___| \r\n" + //
				"(_____)                                                                 (_____)\n");
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("NavBar.fxml"));
			mainPane.setLeft(loader.load());
			navController = loader.getController();
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

			case ACCOUNTING:
				accountingController = loader.getController();
				accountingController.setStorageManager(storageManager);
				accountingController.setup();
				break;

			case CUSTOMERS:
				customersController = loader.getController();
				customersController.setStorageManager(storageManager);
				customersController.setup();
				break;

			case DASHBOARD:
				dashboardController = loader.getController();
				dashboardController.setStorageManager(storageManager);
				dashboardController.setup();
				break;

			case INVENTORY:
				inventoryController = loader.getController();
				inventoryController.setStorageManager(storageManager);
				inventoryController.setup();
				break;

			case PURCHASES:
				purchasesController = loader.getController();
				purchasesController.setStorageManager(storageManager);
				purchasesController.setup();
				break;

			case REPORTS:
				reportsController = loader.getController();
				reportsController.setStorageManager(storageManager);
				reportsController.setup();
				break;

			case SALES:
				salesController = loader.getController();
				salesController.setStorageManager(storageManager);
				salesController.setup();
				break;

			case SETTINGS:
				settingsController = loader.getController();
				settingsController.setApp(app);
				settingsController.setNavBarController(navController);
				settingsController.setStorageManager(storageManager);
				settingsController.setup();
				break;

			case STARTUP:
				loader.getController();
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + currentDisplay);
		}

		return true;
	}

	private String getFXMLFileName(Display display) {

		switch (currentDisplay) {

			case ACCOUNTING:
				return "Accounting.fxml";

			case CUSTOMERS:
				return "Customers.fxml";

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

	public void setApp(App app) {

        this.app = app;
    }

    public App getApp() {

        return app;
    }
}
