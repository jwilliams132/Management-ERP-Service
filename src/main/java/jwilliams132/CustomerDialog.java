package jwilliams132;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class CustomerDialog extends Dialog<Customer> {

    private final TextField companyField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField phoneField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField addressField = new TextField();

    public CustomerDialog() {

        setTitle("Add New Customer");
        setHeaderText("Enter customer details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new javafx.scene.control.Label("Company:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new javafx.scene.control.Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new javafx.scene.control.Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new javafx.scene.control.Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {

            if (dialogButton == ButtonType.OK) {

                return new Customer(
                        companyField.getText(),
                        nameField.getText(),
                        phoneField.getText(),
                        emailField.getText(),
                        addressField.getText());
            }
            return null;
        });
    }
}
