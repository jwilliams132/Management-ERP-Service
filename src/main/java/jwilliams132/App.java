package jwilliams132;

import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	public static void main(String[] args) {

		launch();
	}

	private Scene scene;
	private Stage window;
	private BorderPane root;
	private final String CSS_Styles = this.getClass().getResource("Element_Styles.css").toExternalForm();
	private final String CSS_Colors_Dark = this.getClass().getResource("Element_Colors_Dark.css").toExternalForm();

	@Override
	public void start(Stage primaryStage) {

		window = primaryStage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));

		try {

			root = loader.load();
			if (root == null) {

				throw new IOException("FXML root is null");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		root.getStylesheets().add(CSS_Styles);
		root.getStylesheets().add(CSS_Colors_Dark);

		scene = new Scene(root, 1600, 900); // 1300, 600

		window.setScene(scene);
		// window.setMaximized(true);
		window.setTitle("Management ERP Service");

		setWindowIcon();
		window.setOnCloseRequest(e -> window.close());
		window.show();
	}

	private void setWindowIcon() {

		InputStream stream = getClass().getResourceAsStream("Logo.jpg");
		if (stream != null) {

			window.getIcons().add(new Image(stream));
		} else {

			System.err.println("Failed to load the image stream");
		}
	}
}
