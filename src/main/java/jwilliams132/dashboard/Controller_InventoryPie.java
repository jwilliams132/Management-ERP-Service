package jwilliams132.dashboard;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import jwilliams132.Stored_Files_Manager;
import jwilliams132.Tire;

public class Controller_InventoryPie {

	private Stored_Files_Manager storageManager = Stored_Files_Manager.getInstance();
	@FXML
	private PieChart pieChart;

	@FXML
	private void initialize() {

		setPieChartData(storageManager.getTireInventory());
		System.out.println("asdf");
	}

	private void setPieChartData(List<Tire> tireList) {

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

		for (Tire tire : tireList) {

			if (tire.getInventoryCount() == 0)
				continue;

			pieChartData.add(new PieChart.Data(tire.getSkuNumber(), tire.getInventoryCount()));
		}

		pieChart.setData(pieChartData);
	}
}
