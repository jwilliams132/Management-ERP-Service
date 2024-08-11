package jwilliams132;

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

	@FXML
	private ScrollPane settingsScrollPane;

	@FXML
	private VBox settingsDetailView;

	@FXML
	private TreeView<String> categoryTreeView;

	@FXML
	private TextField searchBar;

	@FXML
	private CheckBox navBarDescriptionsBox, secondBox, thirdBox, fourthBox;

	@FXML
	private ComboBox<String> themeComboBox;

	public void setup() {
		
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
	}

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

	@FXML
	public void handleComboBoxAction(ActionEvent event) {

		ComboBox<?> source = (ComboBox<?>) event.getSource();
		if (source == themeComboBox) {

			System.out.println("ComboBox 1 selected: " + themeComboBox.getValue());
			// Add your specific action for comboBox1
		} // else if (source == comboBox2) {

		// }
	}

	@FXML
	public void handleCheckBoxAction(ActionEvent event) {

		CheckBox source = (CheckBox) event.getSource();
		if (source == navBarDescriptionsBox) {

			System.out.println("Checkbox 1 selected: " + navBarDescriptionsBox.isSelected());
			// Add your specific action for checkBox1
		} else if (source == secondBox) {

			System.out.println("Checkbox 2 selected: " + secondBox.isSelected());
			// Add your specific action for checkBox2
		} else if (source == thirdBox) {

			System.out.println("Checkbox 3 selected: " + thirdBox.isSelected());
			// Add your specific action for checkBox3
		} else if (source == fourthBox) {

			System.out.println("Checkbox 4 selected: " + fourthBox.isSelected());
			// Add your specific action for checkBox4
		}
	}

	public void setStorageManager(Stored_Files_Manager storageManager) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setStorageManager'");
	}
}
