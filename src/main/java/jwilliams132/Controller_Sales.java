package jwilliams132;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.LocalDateTimeStringConverter;

public class Controller_Sales {

	private Stored_Files_Manager storageManager;

	@FXML
	private BorderPane salesRoot;
	@FXML
	private TableView<Sale> salesTableView;
	@FXML
	private TableColumn<Sale, Integer> transactionIdColumn;
	@FXML
	private TableColumn<Sale, LocalDateTime> timeColumn;
	@FXML
	private TableColumn<Sale, String> customerNameColumn;
	@FXML
	private TableColumn<Sale, String> skuNumberColumn;
	@FXML
	private TableColumn<Sale, Integer> quantitySoldColumn;
	@FXML
	private TableColumn<Sale, Price_Category> priceCategoryColumn;
	@FXML
	private TableColumn<Sale, BigDecimal> totalPriceColumn;

	@FXML
	private TabPane tabPane;
	@FXML
	private Label newOrderTransactionID;
	@FXML
	private TextField transactionIDTextField;
	@FXML
	private ComboBox<String> customerComboBox_New, customerComboBox_Edit;
	@FXML
	private VBox tiresFieldsBox_New, tiresFieldsBox_Edit;
	private Button newTireTypeButton_New, newTireTypeButton_Edit;

	private List<TireInputGroup_Sales> tireInputGroups_New = new ArrayList<TireInputGroup_Sales>();
	private List<TireInputGroup_Sales> tireInputGroups_Edit = new ArrayList<TireInputGroup_Sales>();

	public void setup() {

		bindColumnsToFields();

		salesTableView.setItems(storageManager.getSaleHistory());
		setRightAlignment(transactionIdColumn);
		setRightAlignment(quantitySoldColumn);
		setRightAlignment(totalPriceColumn);

		populateCustomerComboBox(customerComboBox_New);
		populateCustomerComboBox(customerComboBox_Edit);

		addRightClickFunctionToSelectRowForEdit();
		addTextFieldListener(transactionIDTextField);
		addTabPaneListener();

		initializeNewTireButtons();

		addTireFieldsGroupToDisplayAndList(tireInputGroups_New, tiresFieldsBox_New);
		tiresFieldsBox_New.getChildren().add(newTireTypeButton_New);
	}

	@FXML
	private void handleSubmitOrder() {

		int transactionID = storageManager.getNewTransactionID();
		Customer chosenCustomer = storageManager.getCustomerFromName(customerComboBox_New.getValue());
		if (chosenCustomer == null)
			return;

		for (TireInputGroup_Sales group : tireInputGroups_New) {

			String skuNumber = group.getSkuNumberValue();
			Price_Category category = group.getPrice_CategoryValue();
			int quantity;
			try {

				quantity = group.getQuantityValue();
			} catch (NumberFormatException e) {

				// TODO throw an alert popup
				return;
			}

			Transaction saleToAdd = new Sale(LocalDateTime.now(),
					transactionID,
					skuNumber,
					quantity,
					chosenCustomer,
					category,
					storageManager.findTireBySkuNumber(skuNumber)
							.getPriceForCategory(category)
							.multiply(new BigDecimal(quantity)));

			storageManager.getTransactionHistory().add(saleToAdd);
			storageManager.getSaleHistory().add((Sale) saleToAdd);

		}
		tireInputGroups_New.clear();
		setNewOrderTransactionIDLabel();
		handleClearFields_New();
	}

	@FXML
	private void handleChangeOrder() {

		if (!transactionIDTextField.getStyleClass().contains("valid")) {

			System.err.println("Transaction ID is not Valid."); // TODO make into an alert popup
			return;
		}

		int transactionID = Integer.parseInt(transactionIDTextField.getText());
		String chosenCustomer = customerComboBox_Edit.getSelectionModel().getSelectedItem();

		List<Sale> sameIDSaleList = storageManager.getSaleHistory().stream()
				.filter(sale -> sale.getTransactionID() == transactionID)
				.collect(Collectors.toList());

		Set<String> newTiresList = tireInputGroups_Edit.stream()
				.map(group -> group.getSkuNumberValue())
				.collect(Collectors.toSet());

		Set<String> oldTiresList = sameIDSaleList.stream()
				.map(Sale::getSkuNumber)
				.collect(Collectors.toSet());

		removeExtraStorageEntries(sameIDSaleList, newTiresList, oldTiresList);
		addNewStorageEntries(transactionID, chosenCustomer, sameIDSaleList, newTiresList, oldTiresList);

		updateExistingSaleObjects(sameIDSaleList);
		updateStorage(sameIDSaleList);

		storageManager.sortSaleListByID();
		storageManager.sortTransactionListByID();
	}

	private void removeExtraStorageEntries(List<Sale> sameIDSaleList,
			Set<String> newTiresList,
			Set<String> oldTiresList) {

		for (String oldTire : oldTiresList) {

			if (!newTiresList.contains(oldTire)) {

				// Remove entry from transactionHistory & saleHistory
				Sale saleToRemove = sameIDSaleList.stream()
						.filter(sale -> sale.getSkuNumber().equals(oldTire))
						.findFirst()
						.orElse(null);
				sameIDSaleList.remove(saleToRemove);
				storageManager.removeSaleFromList(saleToRemove);
				storageManager.removeTransactionFromList(saleToRemove);
			}
		}
	}

	private void addNewStorageEntries(int transactionID,
			String chosenCustomer,
			List<Sale> sameIDSaleList,
			Set<String> newTiresList,
			Set<String> oldTiresList) {

		Integer quantity;
		Customer customer;
		Price_Category category;
		for (String newTire : newTiresList) {

			if (!oldTiresList.contains(newTire)) {

				try {

					quantity = tireInputGroups_Edit.stream().filter(group -> group.getSkuNumberValue().equals(newTire))
							.findFirst().orElse(null).getQuantityValue();
				} catch (NumberFormatException e) {

					// TODO throw an alert popup
					return;
				}
				customer = storageManager.getCustomerList().stream()
						.filter(var -> var.getName().equals(chosenCustomer)).findFirst().orElse(null);
				category = tireInputGroups_Edit.stream()
						.filter(group -> group.getSkuNumberValue().equals(newTire)).findFirst().orElse(null)
						.getPrice_CategoryValue();

				// add entry to transactionHistory & saleHistory
				Transaction saleToAdd = new Sale(LocalDateTime.now(),
						transactionID,
						newTire,
						quantity,
						customer,
						category,
						storageManager.findTireBySkuNumber(newTire)
								.getPriceForCategory(category)
								.multiply(new BigDecimal(quantity)));

				sameIDSaleList.add((Sale) saleToAdd);
			}
		}
	}

	private void updateExistingSaleObjects(List<Sale> sameIDSaleList) {

		// for every Sale in the list of same ID Sale objects
		for (Sale sameIDSale : sameIDSaleList) {

			// find the matching input group
			for (TireInputGroup_Sales inputGroup : tireInputGroups_Edit) {

				if (sameIDSale.getSkuNumber().equals(inputGroup.getSkuNumberValue())) {

					// set the Sale fields with the inputGroup values
					try {

						sameIDSale.setQuantity(inputGroup.getQuantityValue());
					} catch (NumberFormatException e) {

						// TODO throw alert popup
					}
					sameIDSale.setPriceCategory(inputGroup.getPrice_CategoryValue());
					sameIDSale.setTotalPrice(storageManager.findTireBySkuNumber(inputGroup.getSkuNumberValue())
							.getPriceForCategory(inputGroup.getPrice_CategoryValue())
							.multiply(new BigDecimal(inputGroup.getQuantityValue())));
				}
			}
		}
	}

	private void updateStorage(List<Sale> chosenTransactionList) {

		for (Sale sale : chosenTransactionList) {

			int saleIndexToEdit = storageManager.getSaleHistory().indexOf(sale);
			if (saleIndexToEdit != -1)
				storageManager.getSaleHistory().set(saleIndexToEdit, sale);
			else
				storageManager.getSaleHistory().add(sale);

			int transactionIndexToEdit = storageManager.getTransactionHistory().indexOf(sale);
			if (transactionIndexToEdit != -1)
				storageManager.getTransactionHistory().set(transactionIndexToEdit, sale);
			else
				storageManager.getTransactionHistory().add(sale);
		}
	}

	// ====================================================================================================

	@FXML
	private void handleClearFields_New() {

		customerComboBox_New.getSelectionModel().clearSelection();
		tiresFieldsBox_New.getChildren().clear();
		addTireFieldsGroupToDisplayAndList(tireInputGroups_New, tiresFieldsBox_New);
		tiresFieldsBox_New.getChildren().add(newTireTypeButton_New);
	}

	@FXML
	private void handleClearFields_Edit() {

		transactionIDTextField.clear();
		customerComboBox_Edit.getSelectionModel().clearSelection();
		tiresFieldsBox_Edit.getChildren().clear();
		addTireFieldsGroupToDisplayAndList(tireInputGroups_New, tiresFieldsBox_Edit);
		tiresFieldsBox_Edit.getChildren().add(newTireTypeButton_Edit);
	}

	@FXML
	private void handleNewCustomerButton() {

		CustomerDialog dialog = new CustomerDialog();
		dialog.showAndWait().ifPresent(customer -> {

			storageManager.addCustomerToCustomerList(customer);
		});
		populateCustomerComboBox(customerComboBox_New);
	}

	// ====================================================================================================
	
	private void bindColumnsToFields() {

		timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		timeColumn.setCellFactory(column -> new TableCell<Sale, LocalDateTime>() {

			private final LocalDateTimeStringConverter converter = new LocalDateTimeStringConverter();

			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {

				super.updateItem(item, empty);
				if (empty)
					setText(null);
				else
					setText(converter.toString(item));

			}
		});

		transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
		skuNumberColumn.setCellValueFactory(new PropertyValueFactory<>("skuNumber"));
		quantitySoldColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomer().nameProperty());
		priceCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("priceCategory"));
		totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
	}

	// ====================================================================================================

	private <S, T> void setRightAlignment(TableColumn<S, T> column) {

		column.setCellFactory(new Callback<TableColumn<S, T>, TableCell<S, T>>() {

			@Override
			public TableCell<S, T> call(TableColumn<S, T> param) {

				return new TableCell<S, T>() {

					@Override
					protected void updateItem(T item, boolean empty) {

						super.updateItem(item, empty);
						if (empty || item == null) {

							setText(null);
						} else {

							setText(item.toString());
						}
						setAlignment(Pos.CENTER_RIGHT);
					}
				};
			}
		});
	}

	// ====================================================================================================

	private void populateCustomerComboBox(ComboBox<String> customerComboBox) {

		customerComboBox.setItems(
				FXCollections.observableArrayList(
						storageManager.getCustomerList()
								.stream()
								.map(Customer::getName)
								.collect(Collectors.toList())));
	}

	// ====================================================================================================

	private void addRightClickFunctionToSelectRowForEdit() {

		salesTableView.setRowFactory(tv -> {

			TableRow<Sale> row = new TableRow<>();
			ContextMenu contextMenu = new ContextMenu();

			MenuItem editItem = new MenuItem("Edit");
			editItem.setOnAction(event -> {

				Sale selectedSale = row.getItem();
				setEditOrderFields(selectedSale);
				tabPane.getSelectionModel().select(1);
			});
			contextMenu.getItems().add(editItem);

			// Only show context menu for non-empty rows
			row.contextMenuProperty().bind(Bindings.when(row.itemProperty().isNotNull())
					.then(contextMenu)
					.otherwise((ContextMenu) null));

			return row;
		});
	}

	private void setEditOrderFields(Sale chosenTransactionItem) {

		transactionIDTextField.setText(String.valueOf(chosenTransactionItem.getTransactionID()));
		customerComboBox_Edit.getSelectionModel().select(chosenTransactionItem.getCustomer().getName());

		List<Sale> transactionsToEdit = storageManager.getSaleHistory().stream()
				.filter(sale -> sale.getTransactionID() == chosenTransactionItem.getTransactionID())
				.collect(Collectors.toList());

		List<TireInputGroup_Sales> controlGroups = new ArrayList<TireInputGroup_Sales>();
		List<String> skuNumbers = storageManager.getTireInventory()
				.stream()
				.map(Tire::getSkuNumber)
				.collect(Collectors.toList());
		for (Sale transaction : transactionsToEdit) {

			controlGroups.add(new TireInputGroup_Sales(skuNumbers, transaction.getSkuNumber(),
					transaction.getQuantity(), transaction.getPriceCategory()));
		}
		tireInputGroups_Edit = controlGroups;
		addRightClickFunctionToDeleteControlGroup(tireInputGroups_Edit);
		updateInputGroupsOnEditDisplay();
	}

	private void updateInputGroupsOnEditDisplay() {

		tiresFieldsBox_Edit.getChildren().clear();
		for (TireInputGroup_Sales controlGroup : tireInputGroups_Edit) {

			tiresFieldsBox_Edit.getChildren().add(controlGroup.getControlGroupContainer());
		}
		tiresFieldsBox_Edit.getChildren().add(newTireTypeButton_Edit);
		addRightClickFunctionToDeleteControlGroup(tireInputGroups_Edit);
	}

	// ====================================================================================================

	private void addTextFieldListener(TextField textField) {

		textField.textProperty().addListener((observable, oldValue, newValue) -> {

			handleTransactionIDTextChanged(textField, oldValue, newValue);
		});
	}

	private void handleTransactionIDTextChanged(TextField textField, String oldValue, String newValue) {

		int attemptedTransactionID;
		if (newValue.equals("")) {

			setInputTextValidityColor(textField, Validity.DEFAULT);
			return;
		}
		if (oldValue.equals(newValue))
			return;

		try {

			attemptedTransactionID = Integer.parseInt(newValue);
		} catch (NumberFormatException e) {

			setInputTextValidityColor(textField, Validity.INVALID);
			return;
		}

		Sale attemptedSale = storageManager.getSaleHistory()
				.stream()
				.filter(sale -> sale.getTransactionID() == attemptedTransactionID)
				.findFirst()
				.orElse(null);
		if (attemptedSale == null) {

			setInputTextValidityColor(textField, Validity.NOT_FOUND);
			return;
		}
		setInputTextValidityColor(textField, Validity.VALID);
		setEditOrderFields(attemptedSale);
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

	// ====================================================================================================

	private void addTabPaneListener() {

		tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {

			if (newTab != null)
				setNewOrderTransactionIDLabel();

			if (newTab == tabPane.getTabs().get(0)) {

				tabPane.getStyleClass().remove("edit-tab-open");
				tabPane.getStyleClass().add("new-tab-open");
			}
			if (newTab == tabPane.getTabs().get(1)) {

				tabPane.getStyleClass().remove("new-tab-open");
				tabPane.getStyleClass().add("edit-tab-open");
			}
		});
	}

	private void setNewOrderTransactionIDLabel() {

		newOrderTransactionID.setText(String.valueOf(storageManager.getNewTransactionID()));
	}

	// ====================================================================================================

	private void initializeNewTireButtons() {

		newTireTypeButton_New = new Button("+ Tire Type");
		newTireTypeButton_New.setOnAction(event -> {

			handleNewTireTypeButton_New();
		});

		newTireTypeButton_Edit = new Button("+ Tire Type");
		newTireTypeButton_Edit.setOnAction(event -> {

			handleNewTireTypeButton_Edit();
		});
	}

	private void handleNewTireTypeButton_New() {

		tiresFieldsBox_New.getChildren().remove(newTireTypeButton_New);
		addTireFieldsGroupToDisplayAndList(tireInputGroups_New, tiresFieldsBox_New);
		tiresFieldsBox_New.getChildren().add(newTireTypeButton_New);
	}

	private void handleNewTireTypeButton_Edit() {

		tiresFieldsBox_Edit.getChildren().remove(newTireTypeButton_Edit);
		addTireFieldsGroupToDisplayAndList(tireInputGroups_Edit, tiresFieldsBox_Edit);
		tiresFieldsBox_Edit.getChildren().add(newTireTypeButton_Edit);
	}

	private void addTireFieldsGroupToDisplayAndList(List<TireInputGroup_Sales> groupList, VBox vbox) {

		TireInputGroup_Sales controlGroup = new TireInputGroup_Sales(storageManager.getTireInventory()
				.stream()
				.map(Tire::getSkuNumber)
				.collect(Collectors.toList()));
		groupList.add(controlGroup);
		vbox.getChildren().add(controlGroup.getControlGroupContainer());
		addRightClickFunctionToDeleteControlGroup(groupList);
	}

	private void addRightClickFunctionToDeleteControlGroup(List<TireInputGroup_Sales> groups) {

		for (TireInputGroup_Sales group : groups) {

			ContextMenu contextMenu = new ContextMenu();
			MenuItem deleteItem = new MenuItem("Delete");

			contextMenu.getItems().add(deleteItem);
			VBox vbox = group.getControlGroupContainer(); // Assuming you have a method to get the VBox
			vbox.setOnContextMenuRequested(event -> contextMenu.show(vbox, event.getScreenX(), event.getScreenY()));

			// Handle delete action
			deleteItem.setOnAction(e -> {

				// Remove the VBox from the UI
				((Pane) vbox.getParent()).getChildren().remove(vbox);

				// Optionally remove it from the editTireFieldsList if needed
				groups.remove(group);
			});
		}
	}

	// ====================================================================================================

	public void setStorageManager(Stored_Files_Manager storageManager) {

		this.storageManager = storageManager;
	}
}
