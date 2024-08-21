package jwilliams132;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

public class Controller_Purchases {

	private Stored_Files_Manager storageManager;

	@FXML
	private BorderPane purchasesRoot;
	@FXML
	private TableView<Purchase> purchasesTableView;
	@FXML
	private TableColumn<Purchase, String> transactionIDColumn;
	@FXML
	private TableColumn<Purchase, LocalDateTime> timeColumn;
	@FXML
	private TableColumn<Purchase, String> skuNumberColumn;
	@FXML
	private TableColumn<Purchase, Integer> quantityBoughtColumn;
	@FXML
	private TableColumn<Purchase, BigDecimal> totalPriceColumn;

	@FXML
	private TabPane tabPane;
	@FXML
	private Label newPurchaseTransactionID;
	@FXML
	private TextField transactionIDTextField;
	@FXML
	private VBox tiresDisplay_New, tiresDisplay_Edit;
	private Button addTireTypeButton_New, addTireTypeButton_Edit;

	private List<TireInputGroup_Purchases> tireInputGroups_New = new ArrayList<TireInputGroup_Purchases>();
	private List<TireInputGroup_Purchases> tireInputGroups_Edit = new ArrayList<TireInputGroup_Purchases>();

	public void setup() {

		storageManager = Stored_Files_Manager.getInstance();

		bindColumnsToFields();

		purchasesTableView.setItems(storageManager.getPurchaseHistory());
		setRightAlignment(transactionIDColumn);
		setRightAlignment(quantityBoughtColumn);
		setRightAlignment(totalPriceColumn);

		addRightClickFunctionToSelectRowForEdit();
		addTextFieldListener(transactionIDTextField);
		addTabPaneListener();

		initializeNewTireButtons();
		addTireFieldsGroupToDisplayAndList(tireInputGroups_New, tiresDisplay_New);
		tiresDisplay_New.getChildren().add(addTireTypeButton_New);
	}

	@FXML
	public void handleConfirmPurchase() {

		int transactionId = storageManager.getNewTransactionID();
		for (TireInputGroup_Purchases group : tireInputGroups_New) {

			String skuNumber = group.getSkuNumberValue();
			int quantity;
			BigDecimal totalPrice;
			try {

				quantity = group.getQuantityValue();
				totalPrice = group.getPurchasePriceValue();
			} catch (Exception e) {

				// TODO throw an alert popup
				return;
			}

			Transaction purchaseToAdd = new Purchase(LocalDateTime.now(),
					transactionId,
					skuNumber,
					quantity,
					totalPrice);

			storageManager.getTransactionHistory().add(purchaseToAdd);
			storageManager.getPurchaseHistory().add((Purchase) purchaseToAdd);
		}
		tireInputGroups_New.clear();
		setNewOrderTransactionIDLabel();
		handleClearFields_New();
	}

	@FXML
	public void handleEditPurchase() {

		if (!transactionIDTextField.getStyleClass().contains("valid")) {

			System.err.println("Transaction ID is not valid."); // TODO make into an alert popup
			return;
		}

		int transactionID = Integer.parseInt(transactionIDTextField.getText());

		List<Purchase> sameIDPurchaseList = storageManager.getPurchaseHistory().stream()
				.filter(purchase -> purchase.getTransactionID() == transactionID)
				.collect(Collectors.toList());

		Set<String> newTiresList = tireInputGroups_Edit.stream()
				.map(group -> group.getSkuNumberValue())
				.collect(Collectors.toSet());

		Set<String> oldTiresList = sameIDPurchaseList.stream()
				.map(Purchase::getSkuNumber)
				.collect(Collectors.toSet());

		removeExtraStorageEntries(sameIDPurchaseList, newTiresList, oldTiresList);
		addNewStorageEntries(transactionID, sameIDPurchaseList, newTiresList, oldTiresList);

		updateExistingPurchaseObjects(sameIDPurchaseList);
		updateStorage(sameIDPurchaseList);

		storageManager.sortPurchaseListByID();
		storageManager.sortTransactionListByID();
	}

	private void removeExtraStorageEntries(List<Purchase> sameIDPurchaseList,
			Set<String> newTiresList,
			Set<String> oldTiresList) {

		for (String oldTire : oldTiresList) {

			if (!newTiresList.contains(oldTire)) {

				Purchase purchaseToRemove = sameIDPurchaseList.stream()
						.filter(purchase -> purchase.getSkuNumber().equals(oldTire))
						.findFirst()
						.orElse(null);
				sameIDPurchaseList.remove(purchaseToRemove);
				storageManager.removePurchaseFromList(purchaseToRemove);
				storageManager.removeTransactionFromList(purchaseToRemove);
			}
		}
	}

	private void addNewStorageEntries(int transactionID,
			List<Purchase> sameIDPurchaseList,
			Set<String> newTiresList,
			Set<String> oldTiresList) {

		Integer quantity;
		BigDecimal totalPrice;
		for (String newTire : newTiresList) {

			if (!oldTiresList.contains(newTire)) {

				try {

					quantity = tireInputGroups_Edit.stream()
							.filter(group -> group.getSkuNumberValue().equals(newTire))
							.findFirst()
							.orElse(null)
							.getQuantityValue();

					totalPrice = tireInputGroups_Edit.stream()
							.filter(group -> group.getSkuNumberValue().equals(newTire))
							.findFirst()
							.orElse(null)
							.getPurchasePriceValue();
				} catch (Exception e) {

					// TODO: throw an alert popup
					return;
				}
				Transaction purchaseToAdd = new Purchase(LocalDateTime.now(),
						transactionID,
						newTire,
						quantity,
						totalPrice);
				sameIDPurchaseList.add((Purchase) purchaseToAdd);
			}
		}
	}

	private void updateExistingPurchaseObjects(List<Purchase> sameIDPurchaseList) {

		for (Purchase sameIDPurchase : sameIDPurchaseList) {

			for (TireInputGroup_Purchases group : tireInputGroups_Edit) {

				if (sameIDPurchase.getSkuNumber().equals(group.getSkuNumberValue())) {

					try {

						sameIDPurchase.setQuantity(group.getQuantityValue());
						sameIDPurchase.setPurchasePrice(group.getPurchasePriceValue());
					} catch (Exception e) {

						// TODO: throw alert popup
					}
				}
			}
		}
	}

	private void updateStorage(List<Purchase> sameIDPurchaseList) {

		for (Purchase purchase : sameIDPurchaseList) {

			int purchaseIndexToEdit = storageManager.getPurchaseHistory().indexOf(purchase);
			if (purchaseIndexToEdit != -1)
				storageManager.getPurchaseHistory().set(purchaseIndexToEdit, purchase);
			else
				storageManager.getPurchaseHistory().add(purchase);

			int transactionIndexToEdit = storageManager.getTransactionHistory().indexOf(purchase);
			if (transactionIndexToEdit != -1)
				storageManager.getTransactionHistory().set(transactionIndexToEdit, purchase);
			else
				storageManager.getTransactionHistory().add(purchase);
		}
	}

	// ====================================================================================================

	@FXML
	public void handleClearFields_New() {

		tiresDisplay_New.getChildren().clear();
		addTireFieldsGroupToDisplayAndList(tireInputGroups_New, tiresDisplay_New);
		tiresDisplay_New.getChildren().add(addTireTypeButton_New);
	}

	@FXML
	public void handleClearFields_Edit() {

		transactionIDTextField.clear();
		tiresDisplay_Edit.getChildren().clear();
		addTireFieldsGroupToDisplayAndList(tireInputGroups_Edit, tiresDisplay_Edit);
		tiresDisplay_Edit.getChildren().add(addTireTypeButton_Edit);
	}

	// ====================================================================================================

	private void bindColumnsToFields() {

		timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		timeColumn.setCellFactory(column -> new TableCell<Purchase, LocalDateTime>() {

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

		transactionIDColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
		skuNumberColumn.setCellValueFactory(new PropertyValueFactory<>("skuNumber"));
		quantityBoughtColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
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

	private void addRightClickFunctionToSelectRowForEdit() {

		purchasesTableView.setRowFactory(tv -> {

			TableRow<Purchase> row = new TableRow<>();
			ContextMenu contextMenu = new ContextMenu();

			MenuItem editItem = new MenuItem("Edit");
			editItem.setOnAction(event -> {

				Purchase selectedPurchase = row.getItem();
				setEditPurchaseFields(selectedPurchase);
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

	private void setEditPurchaseFields(Purchase chosenTransactionItem) {

		transactionIDTextField.setText(String.valueOf(chosenTransactionItem.getTransactionID()));

		List<Purchase> transactionsToEdit = storageManager.getPurchaseHistory().stream()
				.filter(purchase -> purchase.getTransactionID() == chosenTransactionItem.getTransactionID())
				.collect(Collectors.toList());

		List<TireInputGroup_Purchases> groups = new ArrayList<TireInputGroup_Purchases>();
		List<String> skuNumbers = storageManager.getTireInventory().stream()
				.map(Tire::getSkuNumber)
				.collect(Collectors.toList());
		for (Purchase transaction : transactionsToEdit) {

			groups.add(new TireInputGroup_Purchases(skuNumbers,
					transaction.getSkuNumber(),
					transaction.getQuantity(),
					transaction.getPurchasePrice()));
		}
		tireInputGroups_Edit = groups;
		addRightClickFunctionToDeleteControlGroup(tireInputGroups_Edit);
		updateInputGroupsOnEditDisplay();
	}

	private void updateInputGroupsOnEditDisplay() {

		tiresDisplay_Edit.getChildren().clear();
		for (TireInputGroup_Purchases controlGroup : tireInputGroups_Edit) {

			tiresDisplay_Edit.getChildren().add(controlGroup.getControlGroupContainer());
		}
		tiresDisplay_Edit.getChildren().add(addTireTypeButton_Edit);
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

		Purchase attemptedPurchase = storageManager.getPurchaseHistory()
				.stream()
				.filter(purchase -> purchase.getTransactionID() == attemptedTransactionID)
				.findFirst()
				.orElse(null);
		if (attemptedPurchase == null) {

			setInputTextValidityColor(textField, Validity.NOT_FOUND);
			return;
		}
		setInputTextValidityColor(textField, Validity.VALID);
		setEditPurchaseFields(attemptedPurchase);
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

		newPurchaseTransactionID.setText(String.valueOf(storageManager.getNewTransactionID()));
	}

	// ====================================================================================================

	private void initializeNewTireButtons() {

		addTireTypeButton_New = new Button("+ Tire Type");
		addTireTypeButton_New.setOnAction(event -> {

			handleNewTireTypeButton_New();
		});

		addTireTypeButton_Edit = new Button("+ Tire Type");
		addTireTypeButton_Edit.setOnAction(event -> {

			handleNewTireTypeButton_Edit();
		});
	}

	private void handleNewTireTypeButton_New() {

		tiresDisplay_New.getChildren().remove(addTireTypeButton_New);
		addTireFieldsGroupToDisplayAndList(tireInputGroups_New, tiresDisplay_New);
		tiresDisplay_New.getChildren().add(addTireTypeButton_New);
	}

	private void handleNewTireTypeButton_Edit() {

		tiresDisplay_Edit.getChildren().remove(addTireTypeButton_Edit);
		addTireFieldsGroupToDisplayAndList(tireInputGroups_Edit, tiresDisplay_Edit);
		tiresDisplay_Edit.getChildren().add(addTireTypeButton_Edit);
	}

	private void addTireFieldsGroupToDisplayAndList(List<TireInputGroup_Purchases> groupList, VBox vbox) {

		TireInputGroup_Purchases controlGroup = new TireInputGroup_Purchases(storageManager.getTireInventory()
				.stream()
				.map(Tire::getSkuNumber)
				.collect(Collectors.toList()));
		groupList.add(controlGroup);
		vbox.getChildren().add(controlGroup.getControlGroupContainer());
		addRightClickFunctionToDeleteControlGroup(groupList);
	}

	private void addRightClickFunctionToDeleteControlGroup(List<TireInputGroup_Purchases> groups) {

		for (TireInputGroup_Purchases group : groups) {

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
}
