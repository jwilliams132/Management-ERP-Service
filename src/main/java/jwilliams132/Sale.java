package jwilliams132;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sale extends Transaction {

	private Customer customer;
	private Price_Category priceCategory;
	private BigDecimal totalPrice;

	public Sale() {

	}

	public Sale(LocalDateTime time,
			int transactionID,
			String skuNumber,
			int quantity,
			Customer customer,
			Price_Category priceCategory,
			BigDecimal totalPrice) {

		super(time, transactionID, skuNumber, quantity);
		this.customer = customer;
		this.priceCategory = priceCategory;
		this.totalPrice = totalPrice;
	}

	public Customer getCustomer() {

		return customer;
	}

	public void setCustomer(Customer customer) {

		this.customer = customer;
	}

	public Price_Category getPriceCategory() {

		return priceCategory;
	}

	public void setPriceCategory(Price_Category priceCategory) {

		this.priceCategory = priceCategory;
	}

	public BigDecimal getTotalPrice() {

		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {

		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {

		return "Sale [customer=" + customer + ", priceCategory=" + priceCategory + ", totalPrice=" + totalPrice + "]";
	}

	public void print() {

		System.out.println();
		System.out.println("Sale  " + "=".repeat(50));
			System.out.println("--- Transaction ID:  " + getTransactionID());
			System.out.println("--------- Customer:  " + getCustomer());
			System.out.println("--------- Tire SKU:  " + getSkuNumber());
			System.out.println("--------- Quantity:  " + getQuantity());
			System.out.println("--- Price_Category:  " + getPriceCategory());
			System.out.println("------ Total Price:  " + getTotalPrice());
	}
}
