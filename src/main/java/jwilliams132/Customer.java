package jwilliams132;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {

	private final StringProperty company,
			name,
			phone,
			email,
			address;

	public Customer(String company,
			String name,
			String phone,
			String email,
			String address) {

		this.company = new SimpleStringProperty(company);
		this.name = new SimpleStringProperty(name);
		this.phone = new SimpleStringProperty(phone);
		this.email = new SimpleStringProperty(email);
		this.address = new SimpleStringProperty(address);
	}

	public String getCompany() {

		return company.get();
	}

	public void setCompany(String company) {

		this.company.set(company);
	}

	// Getter and setter for name
	public String getName() {

		return name.get();
	}

	public void setName(String name) {

		this.name.set(name);
	}

	public StringProperty nameProperty() {

		return name;
	}

	// Getter and setter for phone
	public String getPhone() {

		return phone.get();
	}

	public void setPhone(String phone) {

		this.phone.set(phone);
	}

	public StringProperty phoneProperty() {

		return phone;
	}

	// Getter and setter for email
	public String getEmail() {

		return email.get();
	}

	public void setEmail(String email) {

		this.email.set(email);
	}

	public StringProperty emailProperty() {

		return email;
	}

	// Getter and setter for address
	public String getAddress() {

		return address.get();
	}

	public void setAddress(String address) {

		this.address.set(address);
	}

	public StringProperty addressProperty() {

		return address;
	}

	@Override
	public String toString() {
		return "Customer [name=" + name + ", phone=" + phone + ", email=" + email + ", address=" + address + "]";
	}
}
