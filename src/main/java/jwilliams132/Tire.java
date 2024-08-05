package jwilliams132;

import java.math.BigDecimal;

public class Tire {

	private ModelType model;
	private String skuNumber;
	private String tireSize;
	private Integer inventoryCount;
	private BigDecimal over20PerOrderDealerPrice,
			dealerPrice,
			suggestedRetailPrice;

	public Tire(ModelType model,
			String skuNumber,
			String tireSize,
			Integer inventoryCount,
			BigDecimal over20PerOrderDealerPrice,
			BigDecimal dealerPrice,
			BigDecimal suggestedRetailPrice) {

		this.model = model;
		this.skuNumber = skuNumber;
		this.tireSize = tireSize;
		this.inventoryCount = inventoryCount;
		this.over20PerOrderDealerPrice = over20PerOrderDealerPrice;
		this.dealerPrice = dealerPrice;
		this.suggestedRetailPrice = suggestedRetailPrice;
	}

	public Tire() {

	}

	public ModelType getModel() {

		return model;
	}

	public void setModel(ModelType model) {

		this.model = model;
	}

	public String getSkuNumber() {

		return skuNumber;
	}

	public void setSkuNumber(String skuNumber) {

		this.skuNumber = skuNumber;
	}

	public String getTireSize() {

		return tireSize;
	}

	public void setTireSize(String tireSize) {

		this.tireSize = tireSize;
	}

	public Integer getInventoryCount() {

		return inventoryCount;
	}

	public void setInventoryCount(Integer count) {

		this.inventoryCount = count;
	}

	public BigDecimal getOver20PerOrderDealerPrice() {

		return over20PerOrderDealerPrice;
	}

	public void setOver20PerOrderDealerPrice(BigDecimal over20PerOrderDealerPrice) {

		this.over20PerOrderDealerPrice = over20PerOrderDealerPrice;
	}

	public BigDecimal getDealerPrice() {

		return dealerPrice;
	}

	public void setDealerPrice(BigDecimal dealerPrice) {

		this.dealerPrice = dealerPrice;
	}

	public BigDecimal getSuggestedRetailPrice() {

		return suggestedRetailPrice;
	}

	public void setSuggestedRetailPrice(BigDecimal suggestedRetailPrice) {

		this.suggestedRetailPrice = suggestedRetailPrice;
	}
}
