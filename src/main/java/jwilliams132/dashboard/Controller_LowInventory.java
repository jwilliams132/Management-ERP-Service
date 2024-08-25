package jwilliams132.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jwilliams132.Stored_Files_Manager;
import jwilliams132.Tire;

public class Controller_LowInventory {

	private Stored_Files_Manager storageManager = Stored_Files_Manager.getInstance();
	private final int limit = 10;

	@FXML
	private VBox root;

	@FXML
	private void initialize() {

		List<Tire> tiresUnderLimit = storageManager.getTireInventory().stream()
				.filter(tire -> tire.getInventoryCount() < limit)
				.collect(Collectors.toList());

		for (int index = 0; index < tiresUnderLimit.size(); index++) {
			addInventoryAlertLine(index, tiresUnderLimit.get(index));
		}
	}

	private void addInventoryAlertLine(int yIndex, Tire tire) {

		FontIcon icon = new FontIcon(MaterialDesignA.ALERT);
		icon.getStyleClass().add("low");
		if (tire.getInventoryCount() < limit / 2)
			icon.getStyleClass().add("very-low");

		Label label = new Label(tire.getSkuNumber());
		label.setGraphic(icon);
		root.getChildren().add(label);
	}
}
