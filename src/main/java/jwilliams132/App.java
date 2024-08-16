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

		scene = new Scene(root, 1600, 900); // 1300, 600
		if (!root.getStylesheets().add(CSS_Styles))
			System.err.println("Element_Styles.css was not added correctly.");
		if (!root.getStylesheets().add(CSS_Colors_Dark))
			System.err.println("Element_Colors_Dark.css was not added correctly.");

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

	private void reloadStylesheets() {
		root.getStylesheets().clear();
		root.getStylesheets().add(CSS_Styles);
		root.getStylesheets().add(CSS_Colors_Dark);
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

							Platform.runLater(this::reloadStylesheets);
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
}
