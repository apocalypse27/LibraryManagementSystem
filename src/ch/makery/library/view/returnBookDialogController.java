package ch.makery.library.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import com.mysql.jdbc.JDBC4PreparedStatement;

import ch.makery.library.code.MainApp;
import ch.makery.library.model.listCheckedOutBooks;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class returnBookDialogController extends tabbedViewController{
	
	@FXML
	private TableView<listCheckedOutBooks> booksToBeReturnedTable;
	
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> books2ReturnCardIdColumn;
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> books2ReturnBNameColumn;
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> books2ReturnISBNColumn;	 
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> books2ReturnBookNameColumn;	 
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> books2ReturnDateOutColumn;
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> books2ReturnDueDateColumn; 
	
	 @FXML
	 private TextField searchTextField;
	 
	@FXML
	 private TextField returnCardIdField;
	 @FXML
	 private TextField returnBorrowerNameField;
	 @FXML
	 private TextField returnIsbnField;	 
	 @FXML
	 private TextField returnBookNameField;	 
	 @FXML
	 private TextField returnDateOutField;	 
	 @FXML
	 private TextField returnDueDateField;	 
	 private MainApp mainApp;
	 
	 
	 @FXML
	 private void initialize() {
		// Initialize the book table with the three columns.
		 books2ReturnCardIdColumn.setCellValueFactory(cellData -> cellData.getValue().getCardIdProperty());
		 books2ReturnISBNColumn.setCellValueFactory(cellData -> cellData.getValue().getIsbnProperty());
		 books2ReturnBookNameColumn.setCellValueFactory(cellData -> cellData.getValue().getbookNameProperty());
		 books2ReturnDateOutColumn.setCellValueFactory(cellData -> cellData.getValue().getDateOutProperty());
		 books2ReturnDueDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDueDateProperty());
		 books2ReturnBNameColumn.setCellValueFactory(cellData -> cellData.getValue().getBNameProperty());
		 // Listen for selection changes and show the person details when changed.
		 booksToBeReturnedTable.getSelectionModel().selectedItemProperty().addListener(
	                (observable, oldValue, newValue) -> showBookDetails(newValue));
		 
	 }
	 
	 /**
	     * Fills all text fields to show details about the person.
	     * If the specified person is null, all text fields are cleared.
	     * 
	     * @param person the person or null
	     */
	    private void showBookDetails(listCheckedOutBooks book) {
	        if (book != null) {
	            // Fill the labels with info from the person object.
	        	returnCardIdField.setText(book.getCardId());
	        	returnBorrowerNameField.setText(book.getBName());
	        	returnIsbnField.setText(book.getIsbn());
	        	returnBookNameField.setText(book.getbookName());
	        	returnDueDateField.setText(book.getDueDate());
	        	returnDateOutField.setText(book.getDateOut());
	        	}
	        else {
	            // Person is null, remove all the text.
	        	returnCardIdField.setText("");
	        	returnBorrowerNameField.setText("");
	        	returnIsbnField.setText("");
	        	returnBookNameField.setText("");
	        	returnDueDateField.setText("");
	        	returnDateOutField.setText("");
	      
	        }
	    }
	    
	    
	    @FXML
	    private void handelSearchButton() {
	    	String query;
	    	String searchText;
	    	ResultSet res;
	     	/*Clear the table*/
	       	for ( int i = 0; i<booksToBeReturnedTable.getItems().size(); i++) {
	       		booksToBeReturnedTable.getItems().clear();
	       	}
	    	dbHandler.getConnection();
	    	query="USE LIBRARY";
	    	dbHandler.myStmtExecuteQuery(query);
	    	searchText=searchTextField.getText();
	    	System.out.println(searchText);
	    	query="SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date"
	    			+ " FROM book as B,borrower as BO,book_loans as BL where Bo.card_Id=BL.Card_Id"
	    			+ " AND BL.ISBN=B.ISBN AND BL.Card_Id='"+searchText+"\'"+" AND DATE_IN IS NULL"+
	    			" UNION SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date FROM book as B,"
	    			+ "borrower as BO,book_loans as BL where Bo.card_Id=BL.Card_Id AND"
	    			+ " BL.ISBN=B.ISBN AND BO.BName LIKE '%"+searchText+"%'"+" AND DATE_IN IS NULL"+ 
	    			" UNION SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date FROM book as B,borrower as BO"
	    			+ ",book_loans as BL where Bo.card_Id=BL.Card_Id AND BL.ISBN=B.ISBN AND BL.ISBN='"+searchText+ "\'"+
	    			" AND DATE_IN is NULL";
	    	
	    		System.out.println(query);
		    	try
		    	{
		    	res=dbHandler.myStmtExecuteQuery(query);
		    	while(res.next())
		    	{
		    		MainApp.books2BeReturned.add(new listCheckedOutBooks(res.getString("Card_Id"),res.getString("BName"),
		    				res.getString("ISBN"),res.getString("Title"),res.getString("Date_Out"),res.getString("Due_Date"),
		    				null));
		    	}
		    	booksToBeReturnedTable.setItems(MainApp.getbooks2BeReturnedData());
	    	}catch(Exception e)
	    	{
	    		System.out.println("Some error in returning book");
	    	}	    	
	    }
	    
	   

	    @FXML
	    private void handleReturnButton()
	    {
	    	if(!returnIsbnField.getText().equals(""))
	    	{
	    		markBookReturned(returnIsbnField.getText()); 
				showInfoAlertWindow("Success","Book Returned Succesfully!");
	    		
	    	}
	    	else
	    	{
	    		callErrorAlert("Invalid ISBN","Please Correct the following","Enter a valid ISBN "
	    				+ "\n Select one of the books on the left");
	    	}
	    		 		  
	    	       
	    }
	   
	    public void markBookReturned(String ISBN) {
	    	String updateCode;
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Now use today date.
			String cD=sdf.format(c.getTime());			
			try {
				java.util.Date currentDate = sdf.parse(cD);
				java.sql.Date sqlCurrentDate = new java.sql.Date(currentDate.getTime());
				updateCode="Update book_loans set Date_In=? where ISBN=?";
				java.sql.PreparedStatement ps=dbHandler.conn.prepareStatement(updateCode);
				ps.setDate(1, sqlCurrentDate);
				ps.setString(2, ISBN);
				ps.executeUpdate();
				}catch(Exception e) {
			System.out.println("Some error while updating checkindate");
			
			}
		
	    }
	   
	    
}
