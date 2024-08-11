package jwilliams132;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Purchase extends Transaction {

	BigDecimal purchasePrice;

	public Purchase() {

	}

	public Purchase(LocalDateTime time,
			int transactionID,
			String skuNumber,
			int quantity,
			BigDecimal purchasePrice) {

		super(time, transactionID, skuNumber, quantity);
		this.purchasePrice = purchasePrice;
	}

	public BigDecimal getPurchasePrice() {

		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {

		this.purchasePrice = purchasePrice;
	}
}
