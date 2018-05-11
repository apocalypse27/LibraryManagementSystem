package ch.makery.library.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class listCheckedOutBooks {
	private final StringProperty cardId;
	private final StringProperty borrowerName;
	private final StringProperty isbn;
	private final StringProperty bookName;
	private final StringProperty dateOut;
	private final StringProperty dueDate;
	private final StringProperty fineAmount;
	
	public listCheckedOutBooks()
	{
		this(null,null,null,null,null,null,null);
	}
	public listCheckedOutBooks(String cardId,String borrowerName,String isbn,String bookName, String dateOut,String dueDate,String fineAmount)
	{
		this.cardId=new SimpleStringProperty(cardId);
		this.borrowerName=new SimpleStringProperty(borrowerName);
		this.isbn=new SimpleStringProperty(isbn);
		this.bookName=new SimpleStringProperty(bookName);
		this.dateOut=new SimpleStringProperty(dateOut);
		this.dueDate=new SimpleStringProperty(dueDate);	
		this.fineAmount=new SimpleStringProperty(fineAmount);
	}
	
	public StringProperty getBNameProperty()
	{
		return borrowerName;
	}
	public String getBName()
	{
		return borrowerName.get();
	}
	
	public StringProperty getCardIdProperty()
	{
		return cardId;
	}
	public String getCardId()
	{
		return cardId.get();
	}
	
    public StringProperty getIsbnProperty()
	{
		return isbn;
	}

    public String getIsbn()
	{
		return isbn.get();
	}
    
    public StringProperty getbookNameProperty()
  	{
  		return bookName;
  	}
    
    public String getbookName()
  	{
  		return bookName.get();
  	}
	
    public StringProperty getDateOutProperty()
  	{
  		return dateOut;
  	}
    
    public String getDateOut()
  	{
  		return dateOut.get();
  	}
    
    public StringProperty getDueDateProperty()
  	{
  		return dueDate;
  	}
    
    public String getDueDate()
  	{
  		return dueDate.get();
  	}
    
    public StringProperty getfineAmountProperty() {
    	return fineAmount;
    }
    
    public String getfineAmount() {
    	return fineAmount.get();
    }

}
