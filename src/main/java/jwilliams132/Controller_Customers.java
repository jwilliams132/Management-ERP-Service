package jwilliams132;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class Controller_Customers {

	private Stored_Files_Manager storageManager;

	@FXML
	private TableView<Customer> customersTableView;
	@FXML
	private TableColumn<Customer, String> companyColumn, nameColumn, phoneColumn, emailColumn, addressColumn;
	@FXML
	private HBox customerInputBox;
	private Button newCustomerButton;
	private TextField companyInput,
			nameInput,
			phoneInput,
			emailInput,
			addressInput;

	public void setup() {

		bindColumnsToFields();
		customersTableView.setItems(storageManager.getCustomerList());

		initializeAddCustomerButton();
	}

	// ====================================================================================================

	private void bindColumnsToFields() {

		companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
	}

	// ====================================================================================================

	private void initializeAddCustomerButton() {

		newCustomerButton = new Button("Add New Customer");
		newCustomerButton.setOnAction(event -> handleAddCustomer());
		customerInputBox.getChildren().add(newCustomerButton);
	}

	@FXML
	private void handleAddCustomer() {

		companyInput = new TextField();
		companyInput.setPrefWidth(220);
		companyInput.setPromptText("Enter Company Name...");
		addSearchBarListener(companyInput);

		nameInput = new TextField();
		nameInput.setPrefWidth(140);
		nameInput.setPromptText("Enter Rep. Name...");
		addSearchBarListener(nameInput);

		phoneInput = new TextField();
		phoneInput.setPrefWidth(106);
		phoneInput.setPromptText("Enter Phone...");
		addSearchBarListener(phoneInput);

		emailInput = new TextField();
		emailInput.setPrefWidth(230);
		emailInput.setPromptText("Enter Email...");
		addSearchBarListener(emailInput);

		addressInput = new TextField();
		addressInput.setPrefWidth(305);
		addressInput.setPromptText("Enter Address...");
		addSearchBarListener(addressInput);

		Button confirm = new Button("Confirm");
		confirm.setOnAction(event -> handleConfirm());

		customerInputBox.getChildren().clear();
		customerInputBox.getChildren().addAll(companyInput,
				nameInput,
				phoneInput,
				emailInput,
				addressInput,
				confirm);

	}

	private void addSearchBarListener(TextField textField) {

		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			handleSearchTextChanged(textField, oldValue, newValue);
		});
	}

	private void handleSearchTextChanged(TextField textField, String oldValue, String newValue) {

		boolean doesMatch = false;
		if (textField == phoneInput) {

			doesMatch = newValue.matches("\\d{3}-\\d{3}-\\d{4}") || newValue.length() == 0;
			setInputTextValidityColor(textField, doesMatch ? Validity.VALID : Validity.INVALID);

			if ((newValue.length() == 3 && oldValue.length() == 2)
					|| (newValue.length() == 7 && oldValue.length() == 6))
				textField.setText(newValue + "-");

		}
		if (textField == emailInput) {

			doesMatch = newValue.matches("^(?!\\.)[\\w\\-_.]*[^.]@\\w+\\.\\w+[^.\\W]$") || newValue.length() == 0;
			setInputTextValidityColor(textField, doesMatch ? Validity.VALID : Validity.INVALID);
		}
	}

	private void setInputTextValidityColor(TextField textField, Validity validity) {

		textField.getStyleClass().removeAll("valid", "invalid", "not-found");
		switch (validity) {

			case VALID:
				textField.getStyleClass().add("valid");
				break;

			case INVALID:
				textField.getStyleClass().add("invalid");
				break;

			case NOT_FOUND:
				textField.getStyleClass().add("not-found");
				break;

			case DEFAULT:
				break;
		}
	}

	private void handleConfirm() {

		if (phoneInput.getStyleClass().contains("invalid")) {

			System.out.println("phone is invalid"); // TODO throw an alert popup
			return;
		}
		if (emailInput.getStyleClass().contains("invalid")) {

			System.out.println("email is invalid"); // TODO throw an alert popup
			return;
		}
		storageManager.addCustomerToCustomerList(new Customer(companyInput.getText(), nameInput.getText(),
				phoneInput.getText(), emailInput.getText(), addressInput.getText()));
		customerInputBox.getChildren().clear();
		initializeAddCustomerButton();
	}

	// ====================================================================================================+

	public void setStorageManager(Stored_Files_Manager storageManager) {

		this.storageManager = storageManager;
	}
}
