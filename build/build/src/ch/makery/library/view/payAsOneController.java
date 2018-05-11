package ch.makery.library.view;

import java.sql.ResultSet;

import ch.makery.library.code.MainApp;
import ch.makery.library.model.listCheckedOutBooks;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class payAsOneController extends tabbedViewController {
	@FXML
	private TableView<listCheckedOutBooks> paymentsTable;
	
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> paymentsCardIdColumn;
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> paymentsBNameColumn;
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> paymentsISBNColumn;	 
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> paymentsNameColumn;	 
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> paymentsDateOutColumn;
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> paymentsDueDateColumn; 
	 @FXML
	 private TableColumn<listCheckedOutBooks, String> paymentsfineAmountColumn;
	
	 @FXML
	 private TextField searchTextField;
	 
	@FXML
	 private TextField paymentsIdField;
	 @FXML
	 private TextField paymentsBorrowerNameField;
	 @FXML
	 private TextField paymentsIsbnField;	 
	 @FXML
	 private TextField paymentsBookNameField;	 
	 @FXML
	 private TextField paymentsDateOutField;	 
	 @FXML
	 private TextField paymentsDueDateField;	
	 @FXML
	 private TextField paymentOkToPay;
	 @FXML
	 private TextField paymentsFineAmountField;
	 @FXML
	 private ComboBox<String> paidUnpaidSelectionBox;
     public static ObservableList<listCheckedOutBooks> booksToCollectFine=FXCollections.observableArrayList();
     public static ObservableList<String> optionsList=FXCollections.observableArrayList("All","Unpaid","Paid");
	 private MainApp mainApp;
	 /*** Returns the data as an observable list of Persons.
	  * @return*/
	 public static ObservableList<listCheckedOutBooks> getBooksToCollectFine() {
	        return booksToCollectFine;
	    }
	 
	 @FXML
	 private void initialize() {
		// Initialize the book table with the three columns.
		 paymentsCardIdColumn.setCellValueFactory(cellData -> cellData.getValue().getCardIdProperty());
		 paymentsISBNColumn.setCellValueFactory(cellData -> cellData.getValue().getIsbnProperty());
		 paymentsNameColumn.setCellValueFactory(cellData -> cellData.getValue().getbookNameProperty());
		 paymentsDateOutColumn.setCellValueFactory(cellData -> cellData.getValue().getDateOutProperty());
		 paymentsDueDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDueDateProperty());
		 paymentsfineAmountColumn.setCellValueFactory(cellData -> cellData.getValue().getfineAmountProperty());
		 paymentsBNameColumn.setCellValueFactory(cellData -> cellData.getValue().getBNameProperty());
		 // Listen for selection changes and show the person details when changed.
		 paymentsTable.getSelectionModel().selectedItemProperty().addListener(
	                (observable, oldValue, newValue) -> showBookDetails(newValue));
		 
		 paidUnpaidSelectionBox.setValue("All");
		 paidUnpaidSelectionBox.setItems(optionsList);
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
	        	paymentsIdField.setText(book.getCardId());
	        	paymentsBorrowerNameField.setText(book.getBName());
	        	paymentsIsbnField.setText(book.getIsbn());
	        	paymentsBookNameField.setText(book.getbookName());
	        	paymentsDueDateField.setText(book.getDueDate());
	        	paymentsDateOutField.setText(book.getDateOut());
	        	paymentsFineAmountField.setText(book.getfineAmount());

	        } else {
	            // Person is null, remove all the text.
	        	paymentsIdField.setText("");
	        	paymentsBorrowerNameField.setText("");
	        	paymentsIsbnField.setText("");
	        	paymentsBookNameField.setText("");
	        	paymentsDueDateField.setText("");
	        	paymentsDateOutField.setText("");
	        	paymentsFineAmountField.setText("");
	        }
	    }
	    
	    @FXML
	    public void handleRefreshButton() {
	    	String updateCode;
	    	String query;
	    	dbHandler.getConnection();
	    	query="USE Library";
	    	dbHandler.myStmtExecuteQuery(query);
	    	/*update fine amount for books for which fine amount has not been paid yet*/
	    	updateCode="update fines,book_loans set fine_amt=0.25*DateDiff(CURDATE(),Due_Date)"
	    			+ " where book_loans.loan_id=fines.loan_id and Date_In IS NULL";
	    	dbHandler.myStmtExecuteUpdate(updateCode);
	    	updateCode="update fines,book_loans set fine_amt=0.25*DateDiff(Date_in,Due_Date) where "
	    			+ "book_loans.loan_id=fines.loan_id and date_in Is NOT NULL and paid="+false;
	    	dbHandler.myStmtExecuteUpdate(updateCode);
	    	/*Insert books which have gone past due date and aren't yet there in the fines table*/
	    	updateCode="insert into fines SELECT book_loans.loan_id,0.25*DATEDIFF(CURDATE(),Due_Date),0 from book_Loans"
	    			+ " where CURDATE()>Due_Date AND Date_IN IS NULL AND Loan_id NOT IN(Select Loan_Id from fines)";
	    	dbHandler.myStmtExecuteUpdate(updateCode);
	    	
	    }

	    @FXML
	    private void handelSearchButton() {
	    	String query;
	    	String searchText;
	    	ResultSet res;
	     	/*Clear the table*/
	       	for ( int i = 0; i<paymentsTable.getItems().size(); i++) {
	       		paymentsTable.getItems().clear();
	       	}
	    	dbHandler.getConnection();
	    	query="USE LIBRARY";
	    	dbHandler.myStmtExecuteQuery(query);
	    	searchText=searchTextField.getText();
	    	if(paidUnpaidSelectionBox.getValue().equals("Paid"))
	    	{
	    		query="SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date,"
		    			+ "F.Fine_Amt FROM book as B,borrower as BO,fines as F,book_loans as BL where Bo.card_Id=BL.Card_Id"
		    			+ " AND DATE_IN IS NOT NULL AND F.Loan_Id=BL.LOan_Id AND BL.ISBN=B.ISBN AND BL.Card_Id='"+searchText+"\'"+" AND Paid="+true+
		    			" UNION SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date,F.Fine_Amt FROM book as B,"
		    			+ "borrower as BO,fines as F,book_loans as BL where Bo.card_Id=BL.Card_Id AND F.Loan_Id=BL.LOan_Id AND"
		    			+ " DATE_IN IS NOT NULL AND BL.ISBN=B.ISBN AND BO.BName LIKE '%"+searchText+"%'"+" AND Paid="+true+ 
		    			" UNION SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date,F.Fine_Amt FROM book as B,borrower as BO"
		    			+ ",fines as F,book_loans as BL where Bo.card_Id=BL.Card_Id AND F.Loan_Id=BL.LOan_Id AND DATE_IN IS NOT NULL"
		    			+ " AND BL.ISBN=B.ISBN AND BL.ISBN='"+searchText+ "\'"+
		    			" AND Paid="+true; 
	    	}
	    	else if(paidUnpaidSelectionBox.getValue().equals("Unpaid"))
	    	{
	    		query="SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date,"
		    			+ "F.Fine_Amt FROM book as B,borrower as BO,fines as F,book_loans as BL where Bo.card_Id=BL.Card_Id"
		    			+ " AND DATE_IN IS NOT NULL AND F.Loan_Id=BL.LOan_Id AND BL.ISBN=B.ISBN AND BL.Card_Id='"+searchText+"\'"+" AND Paid="+false+
		    			" UNION SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date,F.Fine_Amt FROM book as B,"
		    			+ "borrower as BO,fines as F,book_loans as BL where Bo.card_Id=BL.Card_Id AND F.Loan_Id=BL.LOan_Id AND"
		    			+ " DATE_IN IS NOT NULL AND BL.ISBN=B.ISBN AND BO.BName LIKE '%"+searchText+"%'"+" AND Paid="+false+ 
		    			" UNION SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date,F.Fine_Amt FROM book as B,borrower as BO"
		    			+ ",fines as F,book_loans as BL where Bo.card_Id=BL.Card_Id AND F.Loan_Id=BL.LOan_Id AND DATE_IN IS NOT NULL"
		    			+ " AND BL.ISBN=B.ISBN AND BL.ISBN='"+searchText+ "\'"+
		    			" AND Paid="+false; 		    		
	    	}
	    	else
	    	{
	    		query="SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date,"
		    			+ "F.Fine_Amt FROM book as B,borrower as BO,fines as F,book_loans as BL where Bo.card_Id=BL.Card_Id"
		    			+ " AND DATE_IN IS NOT NULL AND F.Loan_Id=BL.LOan_Id AND BL.ISBN=B.ISBN AND BL.Card_Id='"+searchText+"\'"+
		    			" UNION SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date,F.Fine_Amt FROM book as B,"
		    			+ "borrower as BO,fines as F,book_loans as BL where Bo.card_Id=BL.Card_Id AND F.Loan_Id=BL.LOan_Id AND"
		    			+ " DATE_IN IS NOT NULL AND BL.ISBN=B.ISBN AND BO.BName LIKE '%"+searchText+"%'"+ 
		    			" UNION SELECT BL.Card_Id,BO.BName,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date,F.Fine_Amt FROM book as B,borrower as BO"
		    			+ ",fines as F,book_loans as BL where Bo.card_Id=BL.Card_Id AND F.Loan_Id=BL.LOan_Id AND DATE_IN IS NOT NULL"
		    			+ " AND BL.ISBN=B.ISBN AND BL.ISBN='"+searchText+ "\'"; 	
	    	}
	    	try
	    	{
	    		res=dbHandler.myStmtExecuteQuery(query);
		    	while(res.next())
		    	{
		    		booksToCollectFine.add(new listCheckedOutBooks(res.getString("Card_Id"),res.getString("BName"),
		    				res.getString("ISBN"),res.getString("Title"),res.getString("Date_Out"),res.getString("Due_Date"),
		    				res.getString("fine_Amt")));
		    	}
		    	paymentsTable.setItems(getBooksToCollectFine());
	    	}catch(Exception e)
	    	{
	    		System.out.println("Some error in searching of book in payment of fine");
	    	}	    	
	    }
	    
	    
	    @FXML
	    private void handlePayFineButton()
	    {    	
	    	String query;
	    	ResultSet res;
	    	String isbn=paymentsIsbnField.getText();
	    	if(paymentsIdField.getText()!="")
	    	{
	    	   	dbHandler.getConnection();
	    	   	query="USE Library";
	    	   	dbHandler.myStmtExecuteQuery(query);
	    	   	query="Select paid from fines,book_loans where fines.loan_id=book_loans.loan_id and card_id="+paymentsIdField.getText()+
	    	   			" AND ISBN='"+ isbn+"'";
	    	   	res=dbHandler.myStmtExecuteQuery(query);
	    	   	try
	    	   	{
	    	   		while(res.next())
		    	   	{
	    	   			if(res.getBoolean("paid")==false)
	    	   			{
	    	   				updateFinePayment();
	    	   			}
	    	   			else
	    	   			{
	    	   			  callErrorAlert("Error","You cannot pay fine for this book","Fine for this book has already been paid");
	    	   			}
		    	   	
		    	   	}
	    	   	}catch(Exception e)
	    	   	{
	    	   		System.out.println("Error in paying the fine");
	    	   	}
	    		
	    	}	    	
	    	else
	    	{
	    		callErrorAlert("Error","No Book Selected","Select a book");
	    	}    	
	    	
	    }
	    
	    private void updateFinePayment()
	    {
	    	  String fineAmnt=paymentsFineAmountField.getText();
	    	  String fineAmntPaid=null;
	    	  float amntPaidFloat;
	    	  boolean enteredProperly=false;
	    	  float fineAmntFloat=Float.parseFloat(fineAmnt);
	    	  
	    	  if(Float.parseFloat(fineAmnt)>0)
	    	  {
	    		  while(!enteredProperly) {
	    			  /*Launch the Amount paid text box*/
		    		  try {		    			  
		    			  fineAmntPaid=showTextInputDialog("Fine Amnt Paid", "Enter the fine amount paid");
		    			  if(fineAmntPaid.equals("n/a")||Float.parseFloat(fineAmntPaid)<0)
		    			  {
		    				  callErrorAlert("Invalid Value","Please correct the following","You entered an invalid number");
		    			  }
		    			  else if(Float.parseFloat(fineAmntPaid)>fineAmntFloat)
		    			  {
		    				  callErrorAlert("Invalid Value","Please correct the following","You don't have so much of fine to pay!!");
		    			  }
		    			  else if(Float.parseFloat(fineAmntPaid)<fineAmntFloat)
		    			  {
		    				  callErrorAlert("Invalid Value","Please correct the following","Amount you paid is less than what you are supposed to pay\n"
		    				  		+ "Cannot return the book without paying the full fine amount");
		    			  }
		    			  else
		    			  {
		    				  enteredProperly=true;
		    			  }
		    		  }catch(NumberFormatException e)
		    		  {
		    			  System.out.println("Entered number was a not a proper number");
		    			  callErrorAlert("Invalid Value","Please correct the following fields","You entered an invalid number");
		    			  
		    		  }	    			  
	    		  }
	    		  amntPaidFloat=Float.parseFloat(fineAmntPaid);	
	    		  if(amntPaidFloat==fineAmntFloat)
	    		  {
	    			  setFinePaid(paymentsIsbnField.getText());
	    			  showInfoAlertWindow("Success","Fine paid succesfully!");
	    			  
	    		  }
	    		  else if(amntPaidFloat<fineAmntFloat)
	    		  {
	    			  callErrorAlert("Insufficient Fine.","Please pay the correct fine Amount","Your"
	    			  		+ " fine amount is "+fineAmntFloat+" while you have only paid "+ amntPaidFloat);
	    		  }	    		 		  
	    	  }	      
	    }
	    
	    public void setFinePaid(String ISBN)
	    {
	    	String updateCode;
	    	String query;
	    	dbHandler.getConnection();
	    	query="USE Library";
	    	dbHandler.myStmtExecuteQuery(query);
	    	try {
	    		updateCode="update fines,book_loans set Paid=" +1+" where isbn='"+ISBN+"'"+ " AND book_loans.loan_id=fines.loan_id";
		        dbHandler.myStmtExecuteUpdate(updateCode);
	    	}catch(Exception e)
	    	{
	    		System.out.println("Error in updating the fine amount");
	    	}
	    	dbHandler.closeDbHandler();
	    }
	   
	    

}
