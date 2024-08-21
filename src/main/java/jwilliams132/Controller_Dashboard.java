package jwilliams132;

public class Controller_Dashboard {

	private Stored_Files_Manager storageManager;

	public void setup() {
		
		storageManager = Stored_Files_Manager.getInstance();
	}
}
