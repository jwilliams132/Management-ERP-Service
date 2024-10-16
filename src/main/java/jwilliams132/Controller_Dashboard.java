package jwilliams132;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class Controller_Dashboard {

	private Map<Cards, Node> cards = new HashMap<>();

	private enum Cards {

		PROFIT_OVERVIEW,
		PURCHASE_OVERVIEW,
		SALES_OVERVIEW,
		LOW_INVENTORY,
		RECENT_TRANSACTIONS,
		INVENTORY_PIE,
		QUICK_ACTIONS,
		RECEIVABLES,
	}

	@FXML
	private GridPane gridPane;

	@FXML
	public void initialize() {

		loadCards();
	}

	public void setup() {

		gridPane.add(cards.get(Cards.PROFIT_OVERVIEW), 0, 0);
		gridPane.add(cards.get(Cards.SALES_OVERVIEW), 0, 1);
		gridPane.add(cards.get(Cards.PURCHASE_OVERVIEW), 0, 2);
		gridPane.add(cards.get(Cards.LOW_INVENTORY), 1, 0);
	}

	private void loadCards() {

		cards.put(Cards.INVENTORY_PIE, loadCard("InventoryPie"));
		cards.put(Cards.LOW_INVENTORY, loadCard("LowInventory"));
		cards.put(Cards.PROFIT_OVERVIEW, loadCard("ProfitOverview"));
		cards.put(Cards.PURCHASE_OVERVIEW, loadCard("PurchasesOverview"));
		cards.put(Cards.RECENT_TRANSACTIONS, loadCard("RecentTransactions"));
		cards.put(Cards.SALES_OVERVIEW, loadCard("SalesOverview"));
	}

	private Node loadCard(String var1) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/jwilliams132/dashboard/" + var1 + ".fxml"));
		try {

			return (Node) loader.load();
		} catch (IOException e) {

			System.err.println("Issue with loading the fxml.");
			return null;
		}
	}
}