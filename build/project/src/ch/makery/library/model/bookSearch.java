package ch.makery.library.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class bookSearch {
	private final StringProperty isbn;
	private final StringProperty bookName;
	private final StringProperty authorName;
	private final StringProperty Available;
	
	/*Default Constructor*/
	public bookSearch()
	{
		this(null,null,null,null);
	}
	
	public bookSearch(String isbn,String bookName, String authorName,String Available)
	{
		this.isbn=new SimpleStringProperty(isbn);
		this.bookName=new SimpleStringProperty(bookName);
		this.authorName=new SimpleStringProperty(authorName);
		this.Available=new SimpleStringProperty(Available);		
	}
	

	
	
	public StringProperty getIsbnProperty()
	{
		return isbn;
	}
	
	public StringProperty getBookNameProperty() {
		return bookName;
	}
	
	public StringProperty getAuthorNameProperty()
	{
		return authorName;
	}
	
	public StringProperty getAvailableProperty()
	{
		return Available;
	}

    public String getISBN() {
    	return isbn.get();
    }
	
	
}
