package jwilliams132.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import jwilliams132.Sale;
import jwilliams132.Stored_Files_Manager;

public class Controller_SalesOverview {
	
	private Stored_Files_Manager storageManager = Stored_Files_Manager.getInstance();
	@FXML
	private Label valueLabel;
	@FXML
	private Button overall, pastMonth, pastSixMonths;

	private List<Button> categories = new ArrayList<Button>();

	@FXML
	public void initialize() {

		categories.add(overall);
		categories.add(pastMonth);
		categories.add(pastSixMonths);

		updateButtonStyles(overall);
		updateValueLabel(
				calculateProfit(
						storageManager.getSaleHistory()));
	}

	@FXML
	public void handleCategoryButtons(ActionEvent event) {

		Button source = (Button) event.getSource();
		updateButtonStyles(source);

		if (source == overall) {

			updateValueLabel(
					calculateProfit(
							storageManager.getSaleHistory()));
		} else if (source == pastMonth) {

			updateValueLabel(
					calculateProfit(
							storageManager.getSalesAfterPointInTime(LocalDateTime.now().minusMonths(1))));
		} else if (source == pastSixMonths) {

			updateValueLabel(
					calculateProfit(
							storageManager.getSalesAfterPointInTime(LocalDateTime.now().minusMonths(6))));
		}
	}

	private void updateButtonStyles(Button targetButton) {

		categories.forEach(button -> button.getStyleClass().remove("dashboard-card-selected-category"));
		targetButton.getStyleClass().add("dashboard-card-selected-category");
	}

	public BigDecimal calculateProfit(List<Sale> sales) {

		BigDecimal profit = BigDecimal.ZERO;

		profit = sales.stream()
				.map(sale -> sale.getTotalPrice())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return profit;
	}

	public void updateValueLabel(BigDecimal value) {

		valueLabel.setText(String.format("$ %,5.0f", value));
	}
}
