package jwilliams132;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Stored_Files_Manager {

	private static Stored_Files_Manager instance;

	// get location from preferences.json. add settings option w/ directoryChooser.
	private final String FILE_PATH_TIRE_INVENTORY = "C:\\Users\\Jacob\\Desktop\\ERP Data\\Tires\\Tires.json";
	// get location from preferences.json. add settings option w/ directoryChooser.
	private final String FILE_PATH_CUSTOMER_LIST = "C:\\Users\\Jacob\\Desktop\\ERP Data\\Customers\\Customers.json";
	// get location from preferences.json. add settings option w/ directoryChooser.
	private final String FILE_PATH_TRANSACTIONS = "C:\\Users\\Jacob\\Desktop\\ERP Data\\Transactions\\Transactions.json";
	private JSON_Manager jsonManager = new JSON_Manager();

	private ObservableList<Tire> tireInventory = FXCollections.observableArrayList();
	private ObservableList<Customer> customerList = FXCollections.observableArrayList();
	private ObservableList<Transaction> transactionHistory = FXCollections.observableArrayList();
	private ObservableList<Sale> saleHistory = FXCollections.observableArrayList();
	private ObservableList<Purchase> purchaseHistory = FXCollections.observableArrayList();

	private Stored_Files_Manager() {

		loadTireInventoryFromSavedFile();
		loadCustomerListFromSavedFile();
		loadSampleTransactionData2();
		sortSaleListByID();
		sortPurchaseListByID();
		sortTransactionListByID();
	}

	public static Stored_Files_Manager getInstance() {

		if (instance == null) {
			// Lazy initialization: Create the instance only when needed
			instance = new Stored_Files_Manager();
		}
		return instance;
	}

	// ===========================================================================
	// load Lists from JSON
	// ===========================================================================

	public boolean loadTireInventoryFromSavedFile() {

		File inputFile = new File(FILE_PATH_TIRE_INVENTORY);
		try {

			tireInventory.setAll(jsonManager.parseJsonFile(inputFile, Tire[].class));
		} catch (IOException e) {

			System.err.println(e.getMessage());
			System.err.println("Issue loading the Tire Inventory JSON file.");
			return false;
		}
		return true;
	}

	public boolean loadTransactionHistoryFromSavedFile() {

		File inputFile = new File(FILE_PATH_TRANSACTIONS);
		try {

			transactionHistory.setAll(jsonManager.parseJsonFile(inputFile, Transaction[].class));
		} catch (IOException e) {

			System.err.println(e.getMessage());
			System.err.println("Issue loading the Tire History JSON file.");
			return false;
		}

		for (Transaction transaction : transactionHistory) {

			if (transaction instanceof Sale) {

				saleHistory.add((Sale) transaction);
			} else if (transaction instanceof Purchase) {

				purchaseHistory.add((Purchase) transaction);
			}
		}
		return true;
	}

	public boolean loadCustomerListFromSavedFile() {

		File inputFile = new File(FILE_PATH_CUSTOMER_LIST);
		try {

			customerList.setAll(jsonManager.parseJsonFile(inputFile, Customer[].class));
		} catch (IOException e) {

			System.err.println(e.getMessage());
			System.err.println("Issue loading the Customer List JSON file.");
			return false;
		}
		return true;
	}

	// ===========================================================================
	// save Lists to JSON
	// ===========================================================================

	public boolean saveTireInventoryToFile() {

		// Ensure FILE_PATH_TIRE_INVENTORY is the correct path for saving tire inventory
		return jsonManager.saveToJSON(FILE_PATH_TIRE_INVENTORY, false, tireInventory);
	}

	public boolean saveTransactionHistoryToFile() {

		// Ensure FILE_PATH_TIRE_HISTORY is the correct path for saving tire history
		return jsonManager.saveToJSON(FILE_PATH_TRANSACTIONS, false, transactionHistory);
	}

	public boolean saveCustomerListToFile() {

		return jsonManager.saveToJSON(FILE_PATH_CUSTOMER_LIST, false, customerList);
	}

	// ===========================================================================
	// Edit List entries
	// ===========================================================================

	public void addTransactionToList(Transaction transaction) {

		transactionHistory.add(transaction);
	}

	public void removeTransactionFromList(Transaction transaction) {

		transactionHistory.remove(transaction);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

	public void addSaleToList(Sale sale) {

		saleHistory.add(sale);
	}

	public void removeSaleFromList(Sale sale) {

		saleHistory.remove(sale);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

	public void addPurchaseToList(Purchase purchase) {

		purchaseHistory.add(purchase);
	}

	public void removePurchaseFromList(Purchase purchase) {

		purchaseHistory.remove(purchase);
	}

	// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

	public void addCustomerToCustomerList(Customer customer) {

		customerList.add(customer);
		saveCustomerListToFile();
	}

	public void removeCustomerToCustomerList(Customer customer) {

		customerList.remove(customer);
		saveCustomerListToFile();
	}

	// ===========================================================================
	// Sort Lists
	// ===========================================================================

	public void sortSaleListByID() {

		saleHistory.sort(Comparator.comparing(Sale::getTransactionID).reversed());
	}

	public void sortPurchaseListByID() {

		purchaseHistory.sort(Comparator.comparing(Purchase::getTransactionID).reversed());
	}

	public void sortTransactionListByID() {

		transactionHistory.sort(Comparator.comparing(Transaction::getTransactionID).reversed());
	}

	// ===========================================================================
	// get List properties
	// ===========================================================================

	public int getNewTransactionID() {

		int mostRecentSaleId = 0;
		for (Transaction transaction : transactionHistory) {
			mostRecentSaleId = transaction.getTransactionID() > mostRecentSaleId ? transaction.getTransactionID()
					: mostRecentSaleId;
		}
		return mostRecentSaleId + 1;
	}

	public Tire findTireBySkuNumber(String skuNumber) {

		return tireInventory.stream()
				.filter(tire -> tire.getSkuNumber().equals(skuNumber))
				.findFirst()
				.orElse(null); // or handle as needed if not found
	}

	public Customer getCustomerFromName(String name) {

		if (name != null) {

			Optional<Customer> selectedCustomer = customerList.stream()
					.filter(customer -> customer.getName().equals(name))
					.findFirst();

			if (selectedCustomer.isPresent()) {

				return selectedCustomer.get();
				// Do something with the customer instance
			} else {

				System.err.println("No matching customer found for name: " + name);
				return null;
			}
		} else {

			System.err.println("No name selected in ComboBox.");
			return null;
		}
	}

	public List<Transaction> getTransactionsHistoryOfTire(Tire tire) {

		List<Transaction> tireTransactions = new ArrayList<>();

		for (Transaction transaction : transactionHistory) {

			if (transaction.getSkuNumber().equals(tire.getSkuNumber()))
				tireTransactions.add(transaction);

		}
		return tireTransactions;
	}

	public List<Transaction> getTransactionsHistoryOfTire(String tireSku) {

		Tire tire = tireInventory.stream()
				.filter(var -> var.getSkuNumber().equals(tireSku))
				.findFirst()
				.orElse(null);

		List<Transaction> tireTransactions = new ArrayList<>();

		for (Transaction transaction : transactionHistory) {

			if (transaction.getSkuNumber().equals(tire.getSkuNumber()))
				tireTransactions.add(transaction);

		}
		return tireTransactions;
	}

	public List<Transaction> getRecentTransactions(int count) {

		return transactionHistory.stream()
				.map(Transaction::getTransactionID)
				.distinct()
				.sorted(Comparator.reverseOrder())
				.limit(count)
				.sorted()
				.flatMap(id -> transactionHistory.stream()
						.filter(t -> t.getTransactionID() == id))
				.collect(Collectors.toList());
	}

	public List<Transaction> getTransactionsAfterPointInTime(LocalDateTime time) {

		return transactionHistory.stream()
				.filter(var -> var.getTime().isAfter(time))
				.collect(Collectors.toList());
	}

	public List<Sale> getSalesAfterPointInTime(LocalDateTime time) {

		return saleHistory.stream()
				.filter(var -> var.getTime().isAfter(time))
				.collect(Collectors.toList());
	}

	public List<Purchase> getPurchasesAfterPointInTime(LocalDateTime time) {

		return purchaseHistory.stream()
				.filter(var -> var.getTime().isAfter(time))
				.collect(Collectors.toList());
	}
	// ===========================================================================
	// populate Lists with sample data
	// ===========================================================================

	private void loadSampleTransactionData2() {

		for (Tire tire : tireInventory)
			tire.setInventoryCount(0);

		Random random = new Random();
		Transaction transactionEntry = null;

		for (int transactionNumber = 0, s = 0,
				p = 0; /* (s < 40 || p < 40) */ transactionNumber < 200; transactionNumber++) {

			Customer customer = customerList.get(random.nextInt(customerList.size()));
			List<Integer> usedTireIndexes = new ArrayList<Integer>();

			int count = (int) tireInventory.stream()
					.filter(tire -> tire.getInventoryCount() == 0)
					.count();

			int saleOrPurchase = random.nextInt(tireInventory.size()) <= count ? 1 : 0;
			int tiresPerTransaction = random
					.nextInt(saleOrPurchase == 0 ? (tireInventory.size() - count) / 2 : tireInventory.size() / 2);
			// tiresPerTransaction = tiresPerTransaction < tirePerTransactionLimit
			// ? tiresPerTransaction
			// : random.nextInt(tirePerTransactionLimit);
			for (int j = 0; j < tiresPerTransaction; j++) {

				Price_Category category = null;

				int tireIndex = random.nextInt(tireInventory.size());

				while (usedTireIndexes.contains(tireIndex)
						|| (tireInventory.get(tireIndex).getInventoryCount() == 0 && saleOrPurchase == 0))
					tireIndex = random.nextInt(tireInventory.size());
				usedTireIndexes.add(tireIndex);
				Tire tire = tireInventory.get(tireIndex);

				int quantity = saleOrPurchase == 0
						? random.nextInt(tireInventory
								.get(tireIndex)
								.getInventoryCount()) + 1
						: random.nextInt(5) + 1;
				BigDecimal totalPrice = null;

				switch (random.nextInt(3)) {
					case 0:
						category = Price_Category.DEALER;
						totalPrice = tire.getDealerPrice().multiply(new BigDecimal(quantity));
						break;
					case 1:
						category = Price_Category.DEALER_20_PLUS;
						totalPrice = tire.getOver20PerOrderDealerPrice().multiply(new BigDecimal(quantity));
						break;
					case 2:
						category = Price_Category.SUGGESTED_RETAIL;
						totalPrice = tire.getSuggestedRetailPrice().multiply(new BigDecimal(quantity));
						break;
				}
				switch (saleOrPurchase) {
					case 0:
						transactionEntry = new Sale(LocalDateTime.now().minusDays(200 - transactionNumber),
								transactionNumber,
								tire.getSkuNumber(),
								quantity,
								customer,
								category,
								totalPrice);
						s++;
						break;
					case 1:
						transactionEntry = new Purchase(LocalDateTime.now().minusDays(200 - transactionNumber),
								transactionNumber,
								tire.getSkuNumber(),
								quantity,
								new BigDecimal(random.nextInt(1000)));
						p++;
						break;
				}

				tireInventory.get(tireIndex).setInventoryCount(tireInventory.get(tireIndex).getInventoryCount()
						+ (saleOrPurchase == 0 ? (-1 * quantity) : quantity));
				transactionHistory.add(transactionEntry);
			}
		}

		for (Transaction transaction : transactionHistory) {

			if (transaction instanceof Sale) {

				saleHistory.add((Sale) transaction);
			} else if (transaction instanceof Purchase) {

				purchaseHistory.add((Purchase) transaction);
			}
		}
	}

	// ===========================================================================
	// getters
	// ===========================================================================

	public ObservableList<Tire> getTireInventory() {

		return tireInventory;
	}

	public ObservableList<Transaction> getTransactionHistory() {

		return transactionHistory;
	}

	public ObservableList<Sale> getSaleHistory() {

		return saleHistory;
	}

	public ObservableList<Purchase> getPurchaseHistory() {

		return purchaseHistory;
	}

	public ObservableList<Customer> getCustomerList() {

		return customerList;
	}
}
