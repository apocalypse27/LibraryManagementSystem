package ch.makery.library.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class paymentsModel {
	private final StringProperty borId;
	private final StringProperty borName;
	private final StringProperty fineAmt;
	
	public paymentsModel()
	{
		this(null,null,null);
	}
	public paymentsModel(String borId,String borName,String fineAmt)
	{
		this.borId=new SimpleStringProperty(borId);
		this.borName=new SimpleStringProperty(borName);
		this.fineAmt=new SimpleStringProperty(fineAmt);
	}
	
	public StringProperty getBorIdProperty()
	{
		return borId;
	}
	
	public StringProperty getBorNameProperty() {
		return borName;
	}
	public StringProperty getFineAmtProperty() {
		return fineAmt;
	}
	public String getBorId()
	{
		return borId.get();
	}
	public String getBorName()
	{
		return borName.get();
	}
	public String getFineAmt() {
		return fineAmt.get();
	}

}
