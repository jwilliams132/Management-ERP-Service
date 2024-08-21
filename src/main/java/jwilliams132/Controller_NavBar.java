package jwilliams132;

import java.util.ArrayList;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;
import org.kordamp.ikonli.materialdesign2.MaterialDesignR;
import org.kordamp.ikonli.materialdesign2.MaterialDesignV;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller_NavBar {

	@FXML
	private Button dashboardButton,
			salesButton,
			purchasesButton,
			inventoryButton,
			reportsButton,
			settingsButton,
			customersButton,
			accountingButton;

	List<Button> navButtons = new ArrayList<Button>();
	List<FontIcon> navIcons = new ArrayList<FontIcon>();
	private FontIcon dashboardIcon,
			salesIcon,
			purchasesIcon,
			inventoryIcon,
			reportsIcon,
			settingsIcon,
			customersIcon,
			accountingIcon;

	private Button activeButton;
	private FontIcon activeFontIcon;
	private Preferences_Manager preferences_Manager = new Preferences_Manager();
	private boolean isNavTitlesVisible;

	private Controller_Main mainController;

	public void setMainController(Controller_Main mainController) {

		this.mainController = mainController;
	}

	@FXML
	private void initialize() {

		navButtons.add(salesButton);
		navButtons.add(inventoryButton);
		navButtons.add(purchasesButton);
		navButtons.add(dashboardButton);
		navButtons.add(customersButton);
		navButtons.add(reportsButton);
		navButtons.add(settingsButton);
		navButtons.add(accountingButton);

		navIcons.add(dashboardIcon);
		navIcons.add(inventoryIcon);
		navIcons.add(salesIcon);
		navIcons.add(purchasesIcon);
		navIcons.add(accountingIcon);
		navIcons.add(customersIcon);
		navIcons.add(reportsIcon);
		navIcons.add(settingsIcon);

		dashboardIcon = new FontIcon(MaterialDesignV.VIEW_DASHBOARD_VARIANT_OUTLINE); 
		dashboardIcon.getStyleClass().add("nav-button");
		dashboardButton.setGraphic(dashboardIcon);

		inventoryIcon = new FontIcon(MaterialDesignP.PACKAGE_VARIANT_CLOSED);
		inventoryIcon.getStyleClass().add("nav-button");
		inventoryButton.setGraphic(inventoryIcon);

		salesIcon = new FontIcon(MaterialDesignC.CART); 
		salesIcon.getStyleClass().add("nav-button");
		salesButton.setGraphic(salesIcon);

		purchasesIcon = new FontIcon(MaterialDesignR.RECEIPT); 
		purchasesIcon.getStyleClass().add("nav-button");
		purchasesButton.setGraphic(purchasesIcon);

		accountingIcon = new FontIcon(MaterialDesignC.CASH_USD); 
		accountingIcon.getStyleClass().add("nav-button");
		accountingButton.setGraphic(accountingIcon);

		customersIcon = new FontIcon(MaterialDesignA.ACCOUNT_GROUP); 
		customersIcon.getStyleClass().add("nav-button");
		customersButton.setGraphic(customersIcon);

		reportsIcon = new FontIcon(MaterialDesignC.CHART_BAR_STACKED); 
		reportsIcon.getStyleClass().add("nav-button");
		reportsButton.setGraphic(reportsIcon);

		settingsIcon = new FontIcon(MaterialDesignC.COG); 
		settingsIcon.getStyleClass().add("nav-button");
		settingsButton.setGraphic(settingsIcon);

		handleSettingsChange();
	}

	public void handleSettingsChange() {

		isNavTitlesVisible = preferences_Manager
			.loadPreferences("src\\main\\resources\\jwilliams132\\config.json",
					Preferences.class)
			.isNavBarLabelsVisible();
			updateNavBarTitles();
	}

	public void updateNavBarTitles() {

		navButtons.forEach(button -> button.setPrefWidth(isNavTitlesVisible ? 230 : 60));
		if (isNavTitlesVisible) {

			dashboardButton.setText("  Dashboard");
			inventoryButton.setText("  Inventory");
			salesButton.setText("  Sales");
			purchasesButton.setText("  Purchases");
			accountingButton.setText("  Accounting");
			customersButton.setText("  Customers");
			reportsButton.setText("  Reports");
			settingsButton.setText("  Settings");

			
		} else {

			navButtons.forEach(button -> button.setText(""));
		}
	}

	private void handleButtonClick(Button button, FontIcon fontIcon) {

		if (activeButton != null) {

			activeButton.getStyleClass().remove("clicked");
			activeFontIcon.getStyleClass().remove("clicked");
		}
		if (button.equals(activeButton)) {

			activeButton = null;
			activeFontIcon = null;
			mainController.loadDisplayFXML(Display.STARTUP);
		} else {

			button.getStyleClass().add("clicked");
			activeButton = button;

			fontIcon.getStyleClass().add("clicked");
			activeFontIcon = fontIcon;
		}
	}

	@FXML
	private void handleDashboardButtonAction() {

		mainController.loadDisplayFXML(Display.DASHBOARD);
		handleButtonClick(dashboardButton, dashboardIcon);
	}

	@FXML
	private void handleInventoryButtonAction() {

		mainController.loadDisplayFXML(Display.INVENTORY);
		handleButtonClick(inventoryButton, inventoryIcon);
	}

	@FXML
	private void handleSalesButtonAction() {

		mainController.loadDisplayFXML(Display.SALES);
		handleButtonClick(salesButton, salesIcon);
	}

	@FXML
	private void handlePurchasesButtonAction() {

		mainController.loadDisplayFXML(Display.PURCHASES);
		handleButtonClick(purchasesButton, purchasesIcon);
	}

	@FXML
	private void handleAccountingButtonAction() {

		mainController.loadDisplayFXML(Display.ACCOUNTING);
		handleButtonClick(accountingButton, accountingIcon);
	}

	@FXML
	private void handleCustomersButtonAction() {

		mainController.loadDisplayFXML(Display.CUSTOMERS);
		handleButtonClick(customersButton, customersIcon);
	}

	@FXML
	private void handleReportsButtonAction() {

		mainController.loadDisplayFXML(Display.REPORTS);
		handleButtonClick(reportsButton, reportsIcon);
	}

	@FXML
	private void handleSettingsButtonAction() {

		mainController.loadDisplayFXML(Display.SETTINGS);
		handleButtonClick(settingsButton, settingsIcon);
	}
}
