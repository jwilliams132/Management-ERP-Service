package jwilliams132;

public class Controller_Reports {

	private Stored_Files_Manager storageManager;

	/**
	 * types of reports:
	 * Customer based history - list of all sales to specific customer
	 * 
	 * Current Pricing Sheet - like a pricing menu to print and give to customers
	 * - - (learn to use dependency for Word doc manipulation)
	 * 
	 * 
	 */

	public void setup() {
		
	}

	public void setStorageManager(Stored_Files_Manager storageManager) {

		this.storageManager = storageManager;
	}
}
