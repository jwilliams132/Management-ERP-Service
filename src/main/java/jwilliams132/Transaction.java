package jwilliams132;

import java.time.LocalDateTime;

public class Transaction {

	private LocalDateTime time;
	private String skuNumber;
	private int quantity;

	public Transaction() {

	}

	public Transaction(LocalDateTime time, String skuNumber, int quantity) {

		this.time = time;
		this.skuNumber = skuNumber;
		this.quantity = quantity;
	}

	public LocalDateTime getTime() {

		return time;
	}

	public void setTime(LocalDateTime time) {

		this.time = time;
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
