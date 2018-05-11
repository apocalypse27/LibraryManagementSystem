package ch.makery.library.view;

import java.sql.ResultSet;

import ch.makery.library.model.paymentsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class payAsTotalController extends tabbedViewController{
	@FXML
	private TextField borwrSearchField;
	@FXML
	private TableView<paymentsModel> displayFineAmtTable;
	
	@FXML
	private TableColumn<paymentsModel,String> borIdColumn;
	@FXML
	private TableColumn<paymentsModel,String> borNameColumn;
	@FXML
	private TableColumn<paymentsModel,String> totFineAmtColumn;
	
	@FXML
	private TextField totFineAmtField;
	
	@FXML
	private TextField selectedBorIdField;
	
    public ObservableList<paymentsModel> borToDisplay=FXCollections.observableArrayList();
	 /*** Returns the data as an observable list of Persons.
	  * @return*/
	 public  ObservableList<paymentsModel> getBorDetails() {
	        return borToDisplay;
	    }
	
	@FXML
	public void initialize() {
		borIdColumn.setCellValueFactory(cellData -> cellData.getValue().getBorIdProperty());
		borNameColumn.setCellValueFactory(cellData -> cellData.getValue().getBorNameProperty());
		totFineAmtColumn.setCellValueFactory(cellData -> cellData.getValue().getFineAmtProperty());
		
		 // Listen for selection changes and show the person details when changed.
		displayFineAmtTable.getSelectionModel().selectedItemProperty().addListener(
	                (observable, oldValue, newValue) -> showBorDetails(newValue));
		
	}
	
	private void showBorDetails(paymentsModel bor)
	{
		if(bor!=null)
		{
			totFineAmtField.setText(bor.getFineAmt());
			selectedBorIdField.setText(bor.getBorId());
		}
		else
		{
			totFineAmtField.setText("");
			selectedBorIdField.setText("");
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
	public void handleBorSearchButton() {
		
		String searchText=borwrSearchField.getText();
		String updatCode;
		String query;
		ResultSet res;
		/*Clear the table*/
       	for ( int i = 0; i<displayFineAmtTable.getItems().size(); i++) {
       		displayFineAmtTable.getItems().clear();
       	}
       	dbHandler.getConnection();
       	query="USE LIBRARY";
       	dbHandler.myStmtExecuteQuery(query);
       	query="Select borrower.Card_Id,BName,SUM(Fine_Amt) as tot_fine FROM borrower,fines,book_loans where "
       			+ "book_loans.loan_id=fines.loan_id and borrower.card_id=book_loans.card_id and Date_In IS NOT NULL and "
       			+ "fines.paid="+false+" and BName LIKE '%"+searchText+"%' GROUP BY borrower.Card_Id "
       					+ "UNION Select borrower.Card_Id,BName,SUM(Fine_Amt) FROM borrower,fines,book_loans where "
       			+ "book_loans.loan_id=fines.loan_id and borrower.card_id=book_loans.card_id and Date_In IS NOT NULL and borrower.Card_Id='"
       			+searchText+"' AND fines.paid="+false+" GROUP BY borrower.Card_Id";
       	res=dbHandler.myStmtExecuteQuery(query);
       	try
       	{
       		while(res.next())
           	{
       	 	borToDisplay.add(new paymentsModel(res.getString("Card_Id"),res.getString("BName"),res.getString("tot_fine")));
           	}
       		displayFineAmtTable.setItems(getBorDetails());
       	 	
       	}catch(Exception e)
       	{
       		System.out.println("Error while searching for borrower");
       	}		
	}
	
	@FXML
	public void handlePayFineButton() {
		String selBorID;
		String seltotFineAmt;
		String query;
	    selBorID=selectedBorIdField.getText();
	    seltotFineAmt=totFineAmtField.getText();
	    float fineAmntFloat=Float.parseFloat(seltotFineAmt);
	    if(selBorID!="" && seltotFineAmt!="")
	    {
	    	String updateCode;
	    	float fineAmntPaid=getFineAmtPaid(fineAmntFloat);
	    	if(fineAmntPaid==fineAmntFloat)
	    	{
	    		updateCode="update fines,book_loans set Paid=true where card_id="+selBorID+" and book_loans.loan_id=fines.loan_id"
	    				+ " and date_in IS NOT NULL and paid=false";
	    		dbHandler.getConnection();
	    		query="USE LIBRARY";
	    		dbHandler.myStmtExecuteQuery(query);
	    		dbHandler.myStmtExecuteUpdate(updateCode);
	    		showInfoAlertWindow("Success","All fines paid succesfully!");
	    	}
			
	    }	
	}
	
	public float getFineAmtPaid(float fineToBePaid)
	{
		String fineAmntPaid = "0";  
		float finePaid;
		boolean enteredProperly=false;
		  if(fineToBePaid>0)
    	  {
    		  while(!enteredProperly) {
    			  /*Launch the Amount paid text box*/
	    		  try {		    			  
	    			  fineAmntPaid=showTextInputDialog("Fine Amnt Paid", "Enter the fine amount paid");
	    			  if(fineAmntPaid.equals("n/a")||Float.parseFloat(fineAmntPaid)<0)
	    			  {
	    				  callErrorAlert("Invalid Value","Please correct the following","You entered an invalid number");
	    			  }
	    			  else if(Float.parseFloat(fineAmntPaid)>fineToBePaid)
	    			  {
	    				  callErrorAlert("Invalid Value","Please correct the following","You don't have so much of fine to pay!!");
	    			  }
	    			  else if(Float.parseFloat(fineAmntPaid)<fineToBePaid)
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
    	  }
		  else
		  {
			  showInfoAlertWindow("No Fine","You don't have any fine to pay");
		  }
		  
		  finePaid=Float.parseFloat(fineAmntPaid);
		  return finePaid;
	}

}
