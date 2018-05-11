package ch.makery.library.view;

import java.sql.ResultSet;
import ch.makery.library.util.dbUtils;
import ch.makery.library.code.MainApp;
import ch.makery.library.model.borrower;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class tabbedViewController {
	
	 protected Stage dialogStage;
	
    // Reference to the main application.
     private MainApp mainApp; 
 	
 
     @FXML
     public dbUtils dbHandler=new dbUtils();
	 /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {      
     }    
    
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewBorrower() {
        borrower tempborrower = new borrower();
        boolean successAddNewBorwr=false;
   	    Alert alert = new Alert(AlertType.INFORMATION);
   	    alert.initOwner(mainApp.getPrimaryStage());
        boolean okClicked = mainApp.showBorrowerEditAddDialog(tempborrower);
        if (okClicked) {
        	successAddNewBorwr=addNewBorrower2Db(tempborrower);  
        	if(successAddNewBorwr) {
           	 alert.setTitle("Success");
                alert.setHeaderText("Successful!");
                alert.setContentText("New Borrower Added Successfully");

                alert.showAndWait();
           }
           else
           {
           	 alert.setTitle("Error");
                alert.setHeaderText("Error in adding in new Borrower!");
                alert.setContentText("Unsuccesful in adding new borrower");

                alert.showAndWait();
           }
        }
        
    }    
    
    /**
     * Opens a dialog to enter the fine amount paid. If the user
     * clicks OK, it returns the fine amount paid by the borrower
     * 
     * @param the title to the input dialog box, the message to be displayed
     * @return amount paid by the borrower.**/
   public String showTextInputDialog(final String titleKey, final String messageKey)
   {

    final TextInputDialog inputDlg = new TextInputDialog("");
    inputDlg.initOwner(dialogStage);
    inputDlg.setTitle(titleKey);
    inputDlg.setContentText(messageKey);
    inputDlg.setHeaderText(null);
    return inputDlg.showAndWait().orElse("n/a");
    }
   public void showInfoAlertWindow(String header,String content)
   {
	   Alert alert = new Alert(AlertType.INFORMATION);
	   alert.setTitle(header);
	   alert.setHeaderText(null);
	   alert.setContentText(content);

	   alert.showAndWait();
   }
   
   public void callErrorAlert(String title,String headerText,String contentText)
   {
   	// Show the error message.
		  Alert alert = new Alert(AlertType.ERROR);
		  alert.initOwner(dialogStage);
		  alert.setTitle(title);
		  alert.setHeaderText(headerText);
		  alert.setContentText(contentText);
		  alert.showAndWait();
   }
   
  
    /**This method will Insert new borrower data into the database**/
    private boolean addNewBorrower2Db(borrower newBorrower)
    {
    	String updateCode;
    	String query;
    	ResultSet res1;
    	/*Make connection with the database*/
    	dbHandler.getConnection();
    	
    	try {
            
    		query="USE library";
            res1=dbHandler.myStmtExecuteQuery(query);
        	updateCode="INSERT INTO borrower VALUES(0,"+'\''+newBorrower.getSsn()+'\''+','+
        			'\''+newBorrower.getBorrowerName()+'\''+','+'\''+newBorrower.getAddress()+'\''+','
        			+'\''+newBorrower.getPhone()+'\''+')';
        	System.out.println(updateCode);
        	dbHandler.myStmtExecuteUpdate(updateCode);
        	return(true);
    	
    	}catch(Exception e) {
    		System.out.println("Error in adding new borrower");
    		return(false);
    	}

    }
  
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
     
    }
}
