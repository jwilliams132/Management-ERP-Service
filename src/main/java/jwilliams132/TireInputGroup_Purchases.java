package jwilliams132;

import java.math.BigDecimal;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class TireInputGroup_Purchases {

	private VBox controlGroupContainer;
	private ComboBox<String> skuNumberComboBox;
	private TextField quantityTextField;
	private TextField purchasePriceTextField;

	public TireInputGroup_Purchases() {

	}

	public TireInputGroup_Purchases(List<String> skuNumberList) {

		this.skuNumberComboBox = new ComboBox<>();
		this.quantityTextField = new TextField();
		this.purchasePriceTextField = new TextField();

		this.skuNumberComboBox.setItems(FXCollections.observableArrayList(skuNumberList));

		createControlGroupContainer();
	}

	public TireInputGroup_Purchases(List<String> skuNumberList,
			String skuNumber,
			int quantity,
			BigDecimal purchasePrice) {

		this.skuNumberComboBox = new ComboBox<>();
		this.quantityTextField = new TextField();
		this.purchasePriceTextField = new TextField();

		this.skuNumberComboBox.setItems(FXCollections.observableArrayList(skuNumberList));

		this.skuNumberComboBox.getSelectionModel().select(skuNumber);
		this.quantityTextField.setText(String.valueOf(quantity));
		this.purchasePriceTextField.setText(String.valueOf(purchasePrice));

		createControlGroupContainer();
	}

	// ====================================================================================================

	public void createControlGroupContainer() {

		HBox tireSKUInputBox = getNewTireTypeHBox();
		tireSKUInputBox.getChildren().addAll(getNewTireTypeLabel("Tire:  "),
				getNewTireTypeRegion(),
				this.skuNumberComboBox);

		HBox quantityInputBox = getNewTireTypeHBox();
		quantityInputBox.getChildren().addAll(getNewTireTypeLabel("Quantity:  "),
				getNewTireTypeRegion(),
				this.quantityTextField);

		HBox totalPriceInputBox = getNewTireTypeHBox();
		totalPriceInputBox.getChildren().addAll(getNewTireTypeLabel("Total Price:  "),
				getNewTireTypeRegion(),
				this.purchasePriceTextField);

		controlGroupContainer = new VBox(10);
		HBox.setHgrow(controlGroupContainer, Priority.ALWAYS);

		controlGroupContainer.getChildren().addAll(tireSKUInputBox,
				quantityInputBox,
				totalPriceInputBox,
				getNewTireTypeSeparator());
	}

	private HBox getNewTireTypeHBox() {

		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(hBox, Priority.ALWAYS);
		return hBox;
	}

	private Label getNewTireTypeLabel(String text) {

		Label label = new Label(text);
		label.getStyleClass().add("settings-item-title");
		label.minWidth(110);
		label.maxWidth(110);
		label.prefWidth(110);
		return label;
	}

	private Region getNewTireTypeRegion() {

		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		return region;
	}

	private HBox getNewTireTypeSeparator() {

		HBox hBox = new HBox(10);
		hBox.setPadding(new javafx.geometry.Insets(0, 20, 0, 20));

		Separator separator = new Separator();
		HBox.setHgrow(separator, Priority.ALWAYS);
		hBox.getChildren().add(separator);
		return hBox;
	}

	// ====================================================================================================

	public VBox getControlGroupContainer() {

		return controlGroupContainer;
	}

	public void setControlGroupContainer(VBox controlGroupContainer) {

		this.controlGroupContainer = controlGroupContainer;
	}

	// ====================================================================================================

	public ComboBox<String> getSkuNumberComboBox() {

		return skuNumberComboBox;
	}

	public void setSkuNumberComboBox(ComboBox<String> skuNumberComboBox) {

		this.skuNumberComboBox = skuNumberComboBox;
	}

	public String getSkuNumberValue() {

		return skuNumberComboBox.getSelectionModel().getSelectedItem();
	}

	// ====================================================================================================

	public TextField getQuantityTextField() {

		return quantityTextField;
	}

	public void setQuantityTextField(TextField quantityTextField) {

		this.quantityTextField = quantityTextField;
	}

	public Integer getQuantityValue() {

		try {

			return Integer.valueOf(quantityTextField.getText());
		} catch (NumberFormatException e) {

			System.err.println(
					"Quantity value invalid.  Please make the value '" + quantityTextField.getText() + "' a valid integer.");
			throw e;
		}
	}

	// ====================================================================================================

	public TextField getPurchasePriceTextField() {

		return purchasePriceTextField;
	}

	public void setPurchasePriceTextField(TextField totalPriceTextField) {

		this.purchasePriceTextField = totalPriceTextField;
	}

	public BigDecimal getPurchasePriceValue() {

		try {

			return new BigDecimal(purchasePriceTextField.getText());
		} catch (NumberFormatException e) {

			System.err.println(
					"Purchase Price value invalid.  Please make the value '" + purchasePriceTextField.getText()
							+ "' a valid integer.");
			throw e;
		}
	}
}
