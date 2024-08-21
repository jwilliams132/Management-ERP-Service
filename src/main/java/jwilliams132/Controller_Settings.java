package jwilliams132;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

public class Controller_Settings {

	private App app;
	private Controller_NavBar navBarController;
	private Stored_Files_Manager storageManager;
	private Preferences_Manager preferences_Manager = new Preferences_Manager();
	private Preferences preferences;

	@FXML
	private ScrollPane settingsScrollPane;

	@FXML
	private VBox settingsDetailView;

	@FXML
	private TreeView<String> categoryTreeView;

	@FXML
	private TextField searchBar;

	@FXML
	private CheckBox navBarDescriptionsBox;

	@FXML
	private ComboBox<Themes> themeComboBox;

	@FXML
	private ComboBox<FontSizes> fontSizeComboBox;

	public void setup() {

		preferences = preferences_Manager.loadPreferences("src\\main\\resources\\jwilliams132\\config.json",
		Preferences.class);

		// Initialize the TreeView with categories
		TreeItem<String> rootItem = new TreeItem<>("Settings");
		rootItem.setExpanded(true);

		// Example categories
		TreeItem<String> general = new TreeItem<>("General");
		TreeItem<String> appearance = new TreeItem<>("Appearance");

		// Adding all categories to the root
		rootItem.getChildren().add(general);
		rootItem.getChildren().add(appearance);

		// Set root item to TreeView
		categoryTreeView.setRoot(rootItem);

		addSearchBarListener(searchBar);
		setComboBoxValues();
	}

	// ====================================================================================================

	private void setComboBoxValues() {
		
		this.themeComboBox.setItems(FXCollections.observableArrayList(Themes.values()));
		this.themeComboBox.getSelectionModel().select(preferences.getTheme());

		this.fontSizeComboBox.setItems(FXCollections.observableArrayList(FontSizes.values()));
		this.fontSizeComboBox.getSelectionModel().select(preferences.getFontSize());
	}

	// ====================================================================================================

	private void addSearchBarListener(TextField searchBar) {

		searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
			handleSearchTextChanged(searchBar, oldValue, newValue);
		});
	}

	private void handleSearchTextChanged(TextField textField, String oldValue, String newValue) {

		if (textField == searchBar) {

			System.out.println("SearchBar 1 text changed from " + oldValue + " to " + newValue);
			// Add your specific action for searchBar1
		} // else if (searchBar == searchBar2) {

		// System.out.println("SearchBar 2 text changed from " + oldValue + " to " +
		// newValue);
		// // Add your specific action for searchBar2
		// }
	}

	// ====================================================================================================

	@FXML
	public void handleComboBoxAction(ActionEvent event) {

		ComboBox<?> source = (ComboBox<?>) event.getSource();
		if (source == themeComboBox) {

			preferences.setTheme(themeComboBox.getValue());
			updateConfigFile();
			app.applyTheme(themeComboBox.getValue());
		} else if (source == fontSizeComboBox) {

			preferences.setFontSize(fontSizeComboBox.getValue());
			updateConfigFile();
		}
	}

	// ====================================================================================================

	@FXML
	public void handleCheckBoxAction(ActionEvent event) {

		CheckBox source = (CheckBox) event.getSource();
		if (source == navBarDescriptionsBox) {

			preferences.setNavBarLabelsVisible(navBarDescriptionsBox.isSelected());
			updateConfigFile();
			navBarController.handleSettingsChange();
		}
		// else if (source == secondBox) {}
	}

	// ====================================================================================================

	public void updateConfigFile() {

		preferences_Manager.savePreferences("src\\main\\resources\\jwilliams132\\config.json", preferences,
				Preferences.class);
	}

	// ====================================================================================================

	public void setNavBarController(Controller_NavBar navBarController) {

		this.navBarController = navBarController;
	}
	public void setStorageManager(Stored_Files_Manager storageManager) {

		this.storageManager = storageManager;
	}

	public void setApp(App app) {

        this.app = app;
    }
}
