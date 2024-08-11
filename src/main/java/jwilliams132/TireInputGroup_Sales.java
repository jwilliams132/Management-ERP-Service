package jwilliams132;

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

public class TireInputGroup_Sales {

	private VBox controlGroupContainer;
	private ComboBox<String> skuNumberComboBox;
	private TextField quantityTextField;
	private ComboBox<Price_Category> priceCategoryComboBox;

	public TireInputGroup_Sales() {

	}

	public TireInputGroup_Sales(List<String> skuNumberList) {

		this.skuNumberComboBox = new ComboBox<>();
		this.quantityTextField = new TextField();
		this.priceCategoryComboBox = new ComboBox<>();

		this.skuNumberComboBox.setItems(FXCollections.observableArrayList(skuNumberList));
		this.priceCategoryComboBox.setItems(FXCollections.observableArrayList(Price_Category.values()));

		createControlGroupContainer();
	}

	public TireInputGroup_Sales(List<String> skuNumberList,
			String skuNumber,
			int quantity,
			Price_Category priceCategory) {

		this.skuNumberComboBox = new ComboBox<>();
		this.quantityTextField = new TextField();
		this.priceCategoryComboBox = new ComboBox<>();

		this.skuNumberComboBox.setItems(FXCollections.observableArrayList(skuNumberList));
		this.priceCategoryComboBox.setItems(FXCollections.observableArrayList(Price_Category.values()));

		this.skuNumberComboBox.getSelectionModel().select(skuNumber);
		this.quantityTextField.setText(String.valueOf(quantity));
		this.priceCategoryComboBox.getSelectionModel().select(priceCategory);

		createControlGroupContainer();
	}

	public void createControlGroupContainer() {

		HBox tireSKUInputBox = getNewTireTypeHBox();
		tireSKUInputBox.getChildren().addAll(getNewTireTypeLabel("Tire:  "),
				getNewTireTypeRegion(),
				this.skuNumberComboBox);

		HBox quantityInputBox = getNewTireTypeHBox();
		quantityInputBox.getChildren().addAll(getNewTireTypeLabel("Quantity:  "),
				getNewTireTypeRegion(),
				this.quantityTextField);

		HBox priceCategoryInputBox = getNewTireTypeHBox();
		priceCategoryInputBox.getChildren().addAll(getNewTireTypeLabel("Price Category:  "),
				getNewTireTypeRegion(),
				this.priceCategoryComboBox);

		controlGroupContainer = new VBox(10);
		HBox.setHgrow(controlGroupContainer, Priority.ALWAYS);

		controlGroupContainer.getChildren().addAll(tireSKUInputBox,
				quantityInputBox,
				priceCategoryInputBox,
				getNewTireTypeSeparator());
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

	private HBox getNewTireTypeHBox() {

		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.CENTER_LEFT);
		HBox.setHgrow(hBox, Priority.ALWAYS);
		return hBox;
	}

	public VBox getControlGroupContainer() {

		return controlGroupContainer;
	}

	public void setControlGroupContainer(VBox controlGroupContainer) {

		this.controlGroupContainer = controlGroupContainer;
	}

	public ComboBox<String> getSkuNumberComboBox() {

		return skuNumberComboBox;
	}

	public void setSkuNumberComboBox(ComboBox<String> skuNumberComboBox) {

		this.skuNumberComboBox = skuNumberComboBox;
	}

	public String getSkuNumberValue() {

		return skuNumberComboBox.getSelectionModel().getSelectedItem();
	}

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
					"Quantity value invalid.  Please make the value '" + quantityTextField + "' a valid integer.");
			throw e;
		}
	}

	public ComboBox<Price_Category> getPriceCategoryComboBox() {

		return priceCategoryComboBox;
	}

	public void setPriceCategoryComboBox(ComboBox<Price_Category> priceCategoryComboBox) {

		this.priceCategoryComboBox = priceCategoryComboBox;
	}

	public Price_Category getPrice_CategoryValue() {

		return priceCategoryComboBox.getSelectionModel().getSelectedItem();
	}
}
