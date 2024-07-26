package jwilliams132;

import java.io.IOException;
import javafx.fxml.FXML;

public class Controller_Main {

    @FXML
    private void switchToSecondary() throws IOException {
		
        App.setRoot("Main");
    }
}
