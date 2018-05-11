package ch.makery.library.view;

import java.sql.ResultSet;

import ch.makery.library.model.bookSearch;
import ch.makery.library.model.bookToCheckout;
import ch.makery.library.model.listCheckedOutBooks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class bookSearchController extends tabbedViewController{
	@FXML
	private TableView<bookSearch> bookSearchtable;
	@FXML
	private TableView<listCheckedOutBooks> checkedOutBooksTable;
	
	@FXML
	private TableColumn<bookSearch,String> ISBNColumn;
	@FXML
	private TableColumn<bookSearch,String> BookNameColumn;
	@FXML
	private TableColumn<bookSearch,String> AuthorNameColumn;
	@FXML
	private TableColumn<bookSearch,String> AvailableColumn;
	
	@FXML 
	private TableColumn<listCheckedOutBooks, String> checkedOutCardIdColumn;
	@FXML
	private TableColumn<listCheckedOutBooks,String> checkedOutISBNColumn;
	
	@FXML
	private TableColumn<listCheckedOutBooks,String> checkedOutBookNameColumn;
	@FXML
	private TableColumn<listCheckedOutBooks,String> checkedOutDateOutColumn;
	@FXML
	private TableColumn<listCheckedOutBooks,String> checkedOutDueDateColumn;
	
	 @FXML
	 private TextField bookSearchTextField;

	 @FXML
	 private TextField checkoutISBNField;
	 @FXML
	 private TextField checkoutCardIdField;
		
   final ObservableList<bookSearch> bookSearchData=FXCollections.observableArrayList();

   final ObservableList<listCheckedOutBooks> CheckoutBooksData=FXCollections.observableArrayList();
   
   @FXML
	 private void initialize() {
		  // Initialize the book table with the three columns.
	        ISBNColumn.setCellValueFactory(cellData -> cellData.getValue().getIsbnProperty());
	        BookNameColumn.setCellValueFactory(cellData -> cellData.getValue().getBookNameProperty());
	        AuthorNameColumn.setCellValueFactory(cellData -> cellData.getValue().getAuthorNameProperty());
	        AvailableColumn.setCellValueFactory(cellData -> cellData.getValue().getAvailableProperty());
	        
	        // Initialize the book table with the three columns.
	        checkedOutCardIdColumn.setCellValueFactory(cellData -> cellData.getValue().getCardIdProperty());
	        checkedOutISBNColumn.setCellValueFactory(cellData -> cellData.getValue().getIsbnProperty());
	        checkedOutBookNameColumn.setCellValueFactory(cellData -> cellData.getValue().getbookNameProperty());
	        checkedOutDateOutColumn.setCellValueFactory(cellData -> cellData.getValue().getDateOutProperty());
	        checkedOutDueDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDueDateProperty());
	        // Listen for selection changes and show the person details when changed.
	        bookSearchtable.getSelectionModel().selectedItemProperty().addListener(
		                (observable, oldValue, newValue) -> showIsbnDetails(newValue));
	 }
   
   /**
    * Fills all text fields to show details about the person.
    * If the specified person is null, all text fields are cleared.
    * 
    * @param person the person or null
    */
   private void showIsbnDetails(bookSearch book) {
       if (book != null) {
           // Fill the labels with info from the person object.
    	   checkoutISBNField.setText(book.getISBN());
       } else {
           // Person is null, remove all the text.
           checkoutISBNField.setText("");     
       }
   }
   
   @FXML
   private void handleSearchButton(){
   	String query;
   	String updateCode;
   	ResultSet res1,res2;

   	
   	/*Clear the table*/
   	for ( int i = 0; i<bookSearchtable.getItems().size(); i++) {
   		bookSearchtable.getItems().clear();
   	}
   	/*Make connection with the database*/
   	dbHandler.getConnection();
       query="USE library";
       res1=dbHandler.myStmtExecuteQuery(query);
       /*********Section to retrieve ISBN,book Name and Availability of searched string **********/
       String searchString=bookSearchTextField.getText();	
       updateCode="CREATE VIEW MATCHED_BOOKS AS SELECT B.isbn,B.Title,A.Name from book as B,book_author as BA,author AS A where B.ISBN=BA.ISBN "
					+ "AND BA.author_id=A.author_id AND B.Title LIKE '" + '%'+searchString+'%'+ '\'' +
					" UNION SELECT B.isbn,B.Title,A.Name from book as B,book_author as BA, author AS A where B.ISBN=BA.ISBN "
					+"AND BA.author_id=A.author_id AND A.Name LIKE '" + '%'+searchString+'%' + '\'' +
					" UNION SELECT B.isbn,B.Title,A.Name from book as B,book_author as BA,author AS A where B.ISBN=BA.ISBN"
					+ " AND BA.author_id=A.author_id AND B.ISBN LIKE '"+ '%'+searchString+'%'+'\'';
       dbHandler.myStmtExecuteUpdate(updateCode);
       query="Select * from MATCHED_BOOKS";
       res1=dbHandler.myStmtExecuteQuery(query);
       try {
       	while(res1.next())
       	{
			    boolean isAvailable=checkIsbnAvailable(res1.getString("isbn"));
			    /*Search for this string in res2*/
			    if(isAvailable)			    
			    {
			    	bookSearchData.add(new bookSearch(res1.getString("isbn"),res1.getString("Title"),res1.getString("Name").replaceAll("&amp;", ","),"AVAILABLE"));					  
			    }
			    else
			    {
			    	bookSearchData.add(new bookSearch(res1.getString("isbn"),res1.getString("Title"),res1.getString("Name"),"NOT AVAILABLE"));
					  
			    }				   
			}
       	for(int i=0;i<10;i++)
       	{
       		 System.out.println(bookSearchData.get(i).getISBN());
       	}
       	
       	
	  }catch(Exception e)
	  {
		  System.out.println("");
	  }
       bookSearchtable.setItems(bookSearchData);
       updateCode="DROP VIEW MATCHED_BOOKS";
       dbHandler.myStmtExecuteUpdate(updateCode);
       dbHandler.closeDbHandler();
   }
  
   protected boolean checkIsbnAvailable(String isbn)
	{
		String query;
		boolean isAvailable;
		ResultSet res;
		try {			
			query="SELECT BOOK_LOANS.isbn from BOOK_LOANS WHERE isbn=?";
		    java.sql.PreparedStatement ps=dbHandler.conn.prepareStatement(query);
		    ps.setString(1,isbn );
		    res=ps.executeQuery();
		    if(!res.next())
		    	isAvailable=true;
		    else
		    {
		    	query="SELECT BOOK_Loans.isbn from book_loans where isbn=? AND Date_Out IS NULL AND DATE_IN IS NOT NULL";
		    	ps=dbHandler.conn.prepareStatement(query);
			    ps.setString(1,isbn );
			    res=ps.executeQuery();
			    if(res.next())
			    	isAvailable=true;
			    else
			    	isAvailable=false;
		    }
		    return isAvailable;			
		}catch(Exception e)
		{
			System.out.println("Error while checking for availability of books");
			isAvailable= false;
		}
		dbHandler.closeDbHandler();
		return(isAvailable);
		
	}
   
   @FXML
   private void handleCheckoutBook()
   {
   	bookToCheckout selectedBook=new bookToCheckout();
   	CheckoutBooks newProcessCheckingOut=new CheckoutBooks();
   	boolean successfulCheckout=false;
   	Alert alert = new Alert(AlertType.INFORMATION);
   	ResultSet res;
   	
   	selectedBook.ISBN=checkoutISBNField.getText();
   	selectedBook.card_Id=checkoutCardIdField.getText();
   	if(newProcessCheckingOut.readyToCheckout(selectedBook))
   	{
   		successfulCheckout =newProcessCheckingOut.checkoutNewBook(selectedBook);
   		if(successfulCheckout)
   		{
   			alert.setTitle("Success");
               alert.setHeaderText("Successful!");
               alert.setContentText("Book Checked Out Successfully");

               alert.showAndWait();
   		}
   		else
           {
           	 alert.setTitle("Error");
                alert.setHeaderText("Some error while checking out the book!");
                alert.setContentText("Unable to Checkout Book");

                alert.showAndWait();
           }
   	}
   	/*List out all checked Out books*/
	res=newProcessCheckingOut.listAllCheckoutBooks(selectedBook.card_Id);
	System.out.println("Got results");
	
	try {
		/*Clear the table*/
    	for ( int i = 0; i<checkedOutBooksTable.getItems().size(); i++) {
    		checkedOutBooksTable.getItems().clear();
    	}
		while(res.next())
    	{
    		CheckoutBooksData.add(new listCheckedOutBooks(res.getString("Card_Id"),res.getString("ISBN"),res.getString("Title"),
    				res.getString("date_Out"),res.getString("Due_Date"),null,null));
    	}
		   // Add observable list data to the table
		checkedOutBooksTable.setItems(CheckoutBooksData);
	}catch(Exception e)
	{
		System.out.println("Error while checking out books in main");
	}
   }
	
	

}
