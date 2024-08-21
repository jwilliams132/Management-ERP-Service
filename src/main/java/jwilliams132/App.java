package jwilliams132;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javafx.application.Application;
import javafx.application.Platform;
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

	private final String CSS_Colors_Dark = this.getClass()
			.getResource("Element_Colors_Dark.css")
			.toExternalForm();
	private final String CSS_Colors_Light = this.getClass()
			.getResource("Element_Colors_Light.css")
			.toExternalForm();
	// private final String CSS_Colors_Reddish = this.getClass()
	// .getResource("Element_Colors_Reddish.css")
	// .toExternalForm();
	// private final String CSS_Colors_Greenish = this.getClass()
	// .getResource("Element_Colors_Greenish.css")
	// .toExternalForm();
	// private final String CSS_Colors_Blueish = this.getClass()
	// .getResource("Element_Colors_Bluish.css").toExternalForm();

	// private final String CSS_Font_Size_Small = this.getClass()
	// .getResource("Element_Font_Size_Small.css")
	// .toExternalForm();
	// private final String CSS_Font_Size_Medium = this.getClass()
	// .getResource("Element_Font_Size_Medium.css")
	// .toExternalForm();
	// private final String CSS_Font_Size_Large = this.getClass()
	// .getResource("Element_Font_Size_Large.css")
	// .toExternalForm();
	// private final String CSS_Font_Size_X_Large = this.getClass()
	// .getResource("Element_Font_Size_X_Large.css")
	// .toExternalForm();

	private String currentTheme = CSS_Colors_Dark; // Start with the dark theme

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

		Controller_Main mainController = loader.getController();
		mainController.setApp(this); // Pass this App instance to the controller

		scene = new Scene(root, 1600, 900); // 1300, 600
		Preferences_Manager preferences_Manager = new Preferences_Manager();
		applyTheme(preferences_Manager.loadPreferences("src\\main\\resources\\jwilliams132\\config.json",
				Preferences.class).getTheme());

		window.setScene(scene);
		// window.setMaximized(true);
		window.setTitle("Management ERP Service");

		setWindowIcon();
		window.setOnCloseRequest(e -> window.close());
		window.show();

		startCssFileWatcher();
	}

	private void setWindowIcon() {

		InputStream stream = getClass().getResourceAsStream("Logo.jpg");
		if (stream != null) {

			window.getIcons().add(new Image(stream));
		} else {

			System.err.println("Failed to load the image stream");
		}
	}

	private void startCssFileWatcher() {

		Path pathStyles = Paths.get("src\\main\\resources\\jwilliams132\\Element_Styles.css").toAbsolutePath();
		Path pathColors = Paths.get("src\\main\\resources\\jwilliams132\\Element_Colors_Dark.css").toAbsolutePath();

		Runnable cssWatcherTask = () -> {

			try {

				WatchService watchService = FileSystems.getDefault().newWatchService();
				pathStyles.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
				pathColors.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

				while (true) {

					WatchKey key = watchService.take();
					for (WatchEvent<?> event : key.pollEvents()) {

						if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY &&
								(event.context().toString().equals(pathStyles.getFileName().toString()) ||
										event.context().toString().equals(pathColors.getFileName().toString()))) {

							Platform.runLater(this::applyCSS);
						}
					}
					key.reset();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		};

		Thread cssWatcherThread = new Thread(cssWatcherTask);
		cssWatcherThread.setDaemon(true);
		cssWatcherThread.start();
	}

	private void applyCSS() {

		root.getStylesheets().clear();
		root.getStylesheets().add(CSS_Styles);
		root.getStylesheets().add(currentTheme);
	}

	public void applyTheme(Themes theme) {

		switch (theme) {

			case DARK:
				currentTheme = CSS_Colors_Dark;
				break;

			case LIGHT:
				System.out.println("current theme:  " + theme);
				currentTheme = CSS_Colors_Light;
				break;

			case BLUEISH:
				// currentTheme = CSS_Colors_Blueish;
				break;

			case GREENISH:
				// currentTheme = CSS_Colors_Greenish;
				break;

			case REDDISH:
				// currentTheme = CSS_Colors_Reddish;
				break;

			default:
				currentTheme = CSS_Colors_Dark;
				break;
		}
		applyCSS(); // Reapply the stylesheets with the new theme
	}
}
