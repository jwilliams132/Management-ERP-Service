package jwilliams132;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sale extends Transaction {

	private Customer customer;
	private Price_Category priceCategory;
	private BigDecimal price;

	public Sale() {

	}

	public Sale(LocalDateTime time,
			String skuNumber,
			int quantity,
			Customer customer,
			Price_Category priceCategory,
			BigDecimal price) {

		super(time, skuNumber, quantity);
		this.customer = customer;
		this.priceCategory = priceCategory;
		this.price = price;
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

	public BigDecimal getPrice() {

		return price;
	}

	public void setPrice(BigDecimal price) {
		
		this.price = price;
	}
}
