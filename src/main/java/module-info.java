module jwilliams132 {

	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires com.fasterxml.jackson.databind;

	opens jwilliams132 to javafx.fxml;

	exports jwilliams132;
}
