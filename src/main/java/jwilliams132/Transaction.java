package jwilliams132;

import java.time.LocalDateTime;

public class Transaction {

	private LocalDateTime time;
	private int transactionID;
	private String skuNumber;
	private int quantity;

	public Transaction() {

	}

	public Transaction(LocalDateTime time,
			int transactionID,
			String skuNumber,
			int quantity) {

		this.time = time;
		this.transactionID = transactionID;
		this.skuNumber = skuNumber;
		this.quantity = quantity;
	}

	public LocalDateTime getTime() {

		return time;
	}

	public void setTime(LocalDateTime time) {

		this.time = time;
	}

	public int getTransactionID() {

		return transactionID;
	}

	public void setTransactionID(int transactionID) {

		this.transactionID = transactionID;
	}

	public String getSkuNumber() {

		return skuNumber;
	}

	public void setSkuNumber(String skuNumber) {

		this.skuNumber = skuNumber;
	}

	public int getQuantity() {

		return quantity;
	}

	public void setQuantity(int quantity) {

		this.quantity = quantity;
	}
}
