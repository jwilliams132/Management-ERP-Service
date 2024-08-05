module jwilliams132 {

	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires com.fasterxml.jackson.databind;
	requires javafx.graphics;
	requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
	requires javafx.base;

	opens jwilliams132 to javafx.fxml;

	exports jwilliams132;
}
