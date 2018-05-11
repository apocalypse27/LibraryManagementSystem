package ch.makery.library.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class borrower {
	
	private final StringProperty cardId;
	private final StringProperty ssn;
	private final StringProperty borrowerName;
	private final StringProperty address;
	private final StringProperty phone;
	
	public borrower()
	{
		this(null,null,null,null,null);
	}
	
	public borrower(String cardId,String ssn,String borrowerName,String address, String phone)
	{
		this.cardId=new SimpleStringProperty(cardId);
		this.ssn=new SimpleStringProperty(ssn);
		this.borrowerName=new SimpleStringProperty(borrowerName);
		this.address=new SimpleStringProperty(address);
		this.phone=new SimpleStringProperty(phone);
	}
	
	public StringProperty getcardIdProperty()
	{
		return cardId;
	}
	
	public StringProperty getSsnProperty()
	{
		return ssn;
	}
	
	public StringProperty getborrowerNameProperty()
	{
		return borrowerName;
	}
	
	public StringProperty getAddressProperty()
	{
		return address;
	}
	
	public StringProperty getPhoneProperty() {
		return phone;
	}	
	
	public String getSsn()
	{
		return ssn.get();
	}
	
	public String getBorrowerName()
	{
		return borrowerName.get();
	}
	
	public String getAddress()
	{
		return address.get();
	}
	
	public String getPhone()
	{
		return phone.get();
	}
	
	public void setSsn(String ssn)
	{
		this.ssn.set(ssn);
	}
	
	public void setBorrowerName(String borrowerName)
	{
		this.borrowerName.set(borrowerName);
	}
	
	public void setAddress(String address)
	{
		this.address.set(address);
	}
	
	public void setPhone(String phone)
	{
		this.phone.set(phone);
	}

}
