package jwilliams132;

import java.io.IOException;
import javafx.fxml.FXML;

public class Controller_Startup {

	@FXML
	private void switchToPrimary() throws IOException {

		App.setRoot("Startup");
	}
}
