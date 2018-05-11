package ch.makery.library.model;

import javafx.beans.property.SimpleStringProperty;

public class bookToCheckout {
	public String ISBN;
	public String card_Id;
	public String Available;
	
    public bookToCheckout()
    {
    	this.ISBN=null;
    	this.card_Id=null;
    	this.Available=null;
    }
	public bookToCheckout(String isbn,String card_id,String available)
	{
		this.ISBN=isbn;
		this.card_Id=card_id;
		this.Available=available;
	}
}
