module jwilliams132 {

	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires com.fasterxml.jackson.databind;
	requires javafx.graphics;
	requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
	requires javafx.base;

	exports jwilliams132;
    exports jwilliams132.dashboard; // Export the package containing your controllers

	opens jwilliams132 to javafx.fxml;
    opens jwilliams132.dashboard to javafx.fxml; // Open the dashboard package for FXML access


}
