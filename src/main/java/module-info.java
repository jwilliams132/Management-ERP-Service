module jwilliams132 {
    requires javafx.controls;
    requires javafx.fxml;

    opens jwilliams132 to javafx.fxml;
    exports jwilliams132;
}
