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
		loadSampleTransactionData();
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

	private void loadSampleTransactionData() {

		Random random = new Random();
		// List<Transaction> transactionHistory = new ArrayList<>();

		// Define control variables
		int maxTiresPerTransaction = 5;
		int maxQuantityPerTire = 3;
		int totalTransactions = 200;
		double saleWeight = 0.80; // 25% Sales, 75% Purchases

		for (int transactionNumber = 0; transactionNumber < totalTransactions; transactionNumber++) {

			int transactionID = transactionNumber;
			boolean isSale = random.nextDouble() < saleWeight;
			int tiresPerTransaction = random.nextInt(maxTiresPerTransaction) + 1;
			List<Integer> usedTireIndexes = new ArrayList<>();

			for (int j = 0; j < tiresPerTransaction; j++) {

				// Select a random tire, ensuring it's not already used in this transaction
				int tireIndex;
				do {

					tireIndex = random.nextInt(tireInventory.size());
				} while (usedTireIndexes.contains(tireIndex));

				usedTireIndexes.add(tireIndex);
				Tire tire = tireInventory.get(tireIndex);
				int currentInventory = tire.getInventoryCount();

				// Determine quantity based on sale or purchase
				int quantity = isSale ? random.nextInt(Math.min(maxQuantityPerTire, currentInventory) + 1)
						: random.nextInt(maxQuantityPerTire) + 1;

				if (isSale && quantity == 0)
					continue; // Skip if no tires to sell

				BigDecimal price = BigDecimal.ZERO;

				if (isSale) {

					// Randomly select a price category for Sales
					Price_Category category = Price_Category.values()[random.nextInt(Price_Category.values().length)];
					switch (category) {

						case DEALER:
							price = tire.getDealerPrice().multiply(new BigDecimal(quantity));
							break;

						case DEALER_20_PLUS:
							price = tire.getOver20PerOrderDealerPrice().multiply(new BigDecimal(quantity));
							break;

						case SUGGESTED_RETAIL:
							price = tire.getSuggestedRetailPrice().multiply(new BigDecimal(quantity));
							break;
					}
					transactionHistory
							.add(new Sale(LocalDateTime.now().minusDays(totalTransactions - transactionNumber),
									transactionID, tire.getSkuNumber(), quantity,
									customerList.get(random.nextInt(customerList.size())), category, price));
					tire.setInventoryCount(currentInventory - quantity);
				} else {

					price = new BigDecimal(random.nextInt(1000));
					transactionHistory
							.add(new Purchase(LocalDateTime.now().minusDays(totalTransactions - transactionNumber),
									transactionID, tire.getSkuNumber(), quantity, price));
					tire.setInventoryCount(currentInventory + quantity);
				}
			}
		}


		// Separate transactions into respective histories
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
