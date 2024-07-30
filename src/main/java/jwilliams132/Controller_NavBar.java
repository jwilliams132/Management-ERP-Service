package jwilliams132;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.kordamp.ikonli.materialdesign2.*;

public class Controller_NavBar {

	@FXML
	private Button dashboardButton,
			salesButton,
			purchasesButton,
			inventoryButton,
			reportsButton,
			settingsButton;

	List<Button> navButtons = new ArrayList<Button>();
	List<FontIcon> navIcons = new ArrayList<FontIcon>();
	private FontIcon dashboardIcon,
			salesIcon,
			purchasesIcon,
			inventoryIcon,
			reportsIcon,
			settingsIcon;

	private Button activeButton;
	private FontIcon activeFontIcon;
	private boolean isNavTitlesVisible = true;

	private Controller_Main mainController;

    public void setMainController(Controller_Main mainController) {

        this.mainController = mainController;
    }

	@FXML
	private void initialize() {

		navButtons.add(salesButton);
		navButtons.add(purchasesButton);
		navButtons.add(dashboardButton);
		navButtons.add(inventoryButton);
		navButtons.add(reportsButton);
		navButtons.add(settingsButton);

		navIcons.add(dashboardIcon);
		navIcons.add(salesIcon);
		navIcons.add(purchasesIcon);
		navIcons.add(inventoryIcon);
		navIcons.add(reportsIcon);
		navIcons.add(settingsIcon);
		
		// Create icons and apply style classes
        dashboardIcon = new FontIcon(MaterialDesignV.VIEW_DASHBOARD_VARIANT_OUTLINE); // Use Material Design icon
        dashboardIcon.getStyleClass().add("nav-button");
        dashboardButton.setGraphic(dashboardIcon);

        salesIcon = new FontIcon(MaterialDesignC.CART); // Use Material  icon
        salesIcon.getStyleClass().add("nav-button");
        salesButton.setGraphic(salesIcon);

        purchasesIcon = new FontIcon(MaterialDesignR.RECEIPT); // Use Material  icon
        purchasesIcon.getStyleClass().add("nav-button");
        purchasesButton.setGraphic(purchasesIcon);

        inventoryIcon = new FontIcon(MaterialDesignP.PACKAGE_VARIANT_CLOSED); // Use Material  icon
        inventoryIcon.getStyleClass().add("nav-button");
        inventoryButton.setGraphic(inventoryIcon);

        reportsIcon = new FontIcon(MaterialDesignC.CHART_BAR_STACKED); // Use Material  icon
        reportsIcon.getStyleClass().add("nav-button");
        reportsButton.setGraphic(reportsIcon);

        settingsIcon = new FontIcon(MaterialDesignT.TUNE); // Use Material  icon
        settingsIcon.getStyleClass().add("nav-button");
        settingsButton.setGraphic(settingsIcon);

		navButtons.forEach(button -> button.setPrefWidth(isNavTitlesVisible ? 230 : 75));
		updateNavBarTitles();
	}

	private void updateNavBarTitles() {

		if (isNavTitlesVisible) {

			dashboardButton.setText("  Dashboard");
			salesButton.setText("  Sales");
			purchasesButton.setText("  Purchases");
			inventoryButton.setText("  Inventory");
			reportsButton.setText("  Reports");
			settingsButton.setText("  Settings");
		}

		Set<Button> navButtons = new TreeSet<Button>();

		// dashboardButton
		// salesButton
		// purchasesButton
		// inventoryButton
		// accountingButton
		// reportsButton
		// settingsButton
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
		System.out.println("Dashboard button clicked!");
		// Add your action handling code here
	}

	@FXML
	private void handleSalesButtonAction() {

		mainController.loadDisplayFXML(Display.SALES);
		handleButtonClick(salesButton, salesIcon);
		System.out.println("Sales button clicked!");
		// Add your action handling code here
	}

	@FXML
	private void handlePurchasesButtonAction() {

		mainController.loadDisplayFXML(Display.PURCHASES);
		handleButtonClick(purchasesButton, purchasesIcon);
		System.out.println("Purchases button clicked!");
		// Add your action handling code here
	}

	@FXML
	private void handleInventoryButtonAction() {

		mainController.loadDisplayFXML(Display.INVENTORY);
		handleButtonClick(inventoryButton, inventoryIcon);
		System.out.println("Inventory button clicked!");
		// Add your action handling code here
	}
	@FXML
	private void handleReportsButtonAction() {

		mainController.loadDisplayFXML(Display.REPORTS);
		handleButtonClick(reportsButton, reportsIcon);
		System.out.println("Reports button clicked!");
		// Add your action handling code here
	}

	@FXML
	private void handleSettingsButtonAction() {

		mainController.loadDisplayFXML(Display.SETTINGS);
		handleButtonClick(settingsButton, settingsIcon);
		System.out.println("Settings button clicked!");
		// Add your action handling code here
	}
}
