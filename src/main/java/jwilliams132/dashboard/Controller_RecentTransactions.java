package jwilliams132.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jwilliams132.Purchase;
import jwilliams132.Sale;
import jwilliams132.Stored_Files_Manager;
import jwilliams132.Transaction;

public class Controller_RecentTransactions {

	private Stored_Files_Manager storageManager = Stored_Files_Manager.getInstance();
	@FXML
	private ListView<Transaction> recentTransactionsList;
	@FXML
	private VBox transactionListView;

	public Controller_RecentTransactions() {

	}

	@FXML
	public void initialize() {

		List<Transaction> transactions = storageManager.getRecentTransactions(5);

		Map<Integer, List<Transaction>> groupedTransactions = transactions.stream()
				.collect(Collectors.groupingBy(Transaction::getTransactionID));

		groupedTransactions.forEach((key, value) -> {

			transactionListView.getChildren().add(getTransactionIDContainer(key, value));
		});
	}

	public VBox getTransactionIDContainer(Integer key, List<Transaction> value) {

		VBox container = new VBox();
		container.setAlignment(Pos.TOP_LEFT);
		container.getChildren().add(new Label(String.format("ID:  %3d", key)) {{
			getStyleClass().add("dashboard-transactionID");
		}});

		for (Transaction transaction : value) {
			System.out.println("container size:  " + container.getChildren().size());
			if (transaction instanceof Purchase) {

				System.out.println("purchase");
				Purchase purchase = (Purchase) transaction;
				container.getChildren()
						.add(new Label(String.format("\t%02d %s %04.00f", purchase.getQuantity(), purchase.getSkuNumber(),
								purchase.getPurchasePrice())) {
							{
								getStyleClass().add("dashboard-purchase");
							}
						});
			} else if (transaction instanceof Sale) {

				System.out.println("sale");
				Sale sale = (Sale) transaction;
				container.getChildren().add(new Label(
						String.format("\t%02d %s %04.00f", sale.getQuantity(), sale.getSkuNumber(), sale.getTotalPrice())) {
					{
						getStyleClass().add("dashboard-sale");
					}
				});
			}
		}
		System.out.println("Container size:  " + container.getChildren().size());
		return container;
	}
	// @FXML
	// public void initialize() {
	// // Fetch the list of recent transactions
	// List<Transaction> transactions = storageManager.getRecentTransactions(5);

	// // Group transactions by transaction ID
	// Map<Integer, List<Transaction>> groupedTransactions = transactions.stream()
	// .collect(Collectors.groupingBy(Transaction::getTransactionID));

	// // Create a list of strings for display
	// List<String> displayList = new ArrayList<>();

	// for (Map.Entry<Integer, List<Transaction>> entry :
	// groupedTransactions.entrySet()) {
	// StringBuilder sb = new StringBuilder();
	// sb.append("Transaction ID: ").append(entry.getKey()).append("\n");
	// for (Transaction transaction : entry.getValue()) {
	// if (transaction instanceof Purchase) {
	// Purchase purchase = (Purchase) transaction;
	// sb.append(" P: ")
	// .append(purchase.getSkuNumber())
	// .append(" - #: ")
	// .append(purchase.getQuantity())
	// .append(" - Total $: ")
	// .append(purchase.getPurchasePrice())
	// .append("\n");
	// } else if (transaction instanceof Sale) {
	// Sale sale = (Sale) transaction;
	// sb.append(" S: ")
	// .append(sale.getSkuNumber())
	// .append(" - #: ")
	// .append(sale.getQuantity())
	// .append(" - Total $: ")
	// .append(sale.getTotalPrice())
	// .append("\n");
	// } else {
	// sb.append(" Transaction: ")
	// .append(transaction.getSkuNumber())
	// .append(" - Quantity: ")
	// .append(transaction.getQuantity())
	// .append("\n");
	// }
	// }
	// displayList.add(sb.toString());
	// }

	// // Set items in the ListView
	// transactionListView.getItems().setAll(displayList);
	// }

	// @FXML
	// public void initialize2() {

	// // Fetch the recent transactions
	// List<Transaction> transactions = storageManager.getRecentTransactions(5);

	// // Set cell factory for custom display
	// recentTransactionsList.setCellFactory(new Callback<>() {

	// @Override
	// public ListCell<Transaction> call(ListView<Transaction> param) {

	// return new ListCell<>() {

	// @Override
	// protected void updateItem(Transaction item, boolean empty) {

	// super.updateItem(item, empty);
	// if (empty || item == null) {

	// setText(null);
	// } else {

	// if (item instanceof Purchase) {

	// Purchase purchase = (Purchase) item;
	// setText(String.format("Sale ID: %d - Purchase: %s - Qty: %d - Price: %.2f",
	// purchase.getTransactionID(), purchase.getSkuNumber(), purchase.getQuantity(),
	// purchase.getPurchasePrice()));
	// } else if (item instanceof Sale) {

	// Sale sale = (Sale) item;
	// setText(String.format("Sale ID: %d - Sale: %s - Qty: %d - Total: %.2f",
	// sale.getTransactionID(), sale.getSkuNumber(), sale.getQuantity(),
	// sale.getTotalPrice()));
	// } else {

	// setText(String.format("Transaction: %s - Qty: %d",
	// item.getSkuNumber(), item.getQuantity()));
	// }
	// }
	// }
	// };
	// }
	// });

	// // Set items in the ListView
	// recentTransactionsList.getItems().setAll(transactions);
	// }

}
