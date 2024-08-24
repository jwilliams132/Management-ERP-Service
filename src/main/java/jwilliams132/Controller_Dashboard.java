package jwilliams132;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Controller_Dashboard {

	private Stored_Files_Manager storageManager;

	@FXML
	private GridPane gridPane;

	public void setup() {
		
		storageManager = Stored_Files_Manager.getInstance();

		try {

			addCardToGrid("RecentTransactionsCard.fxml", 0, 0);
			addCardToGrid("ProfitOverview.fxml", 0, 1);
		} catch (IOException e) {

			System.err.println("Issue with loading the fxml.");
			e.printStackTrace();
		}
	}

	private void addCardToGrid(String fxmlFile, int columnIndex, int rowIndex) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jwilliams132/dashboard/" + fxmlFile));
        Pane card = loader.load();
        gridPane.add(card, columnIndex, rowIndex);
    }
}
