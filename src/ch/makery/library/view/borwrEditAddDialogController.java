package ch.makery.library.view;


import java.sql.ResultSet;

import com.mysql.jdbc.StringUtils;

import ch.makery.library.model.borrower;
import ch.makery.library.util.dbUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class borwrEditAddDialogController {
	@FXML
	private TextField ssnField;
	
	@FXML
	private TextField borrowerNameField;
	
	@FXML
	private TextField addressField;
	
	@FXML
	private TextField phoneField;
	
	private Stage dialogStage;
	private borrower borrower;
	private boolean okClicked = false;
	
	@FXML
    public dbUtils ConnectHandler=new dbUtils();
	
	 /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	phoneField.setPromptText("xxxxxxxxx");
    	ssnField.setPromptText("000000000");
    	borrowerNameField.setPromptText("Jane Doe");
    	addressField.setPromptText("20 StreetName City State");
    	
    }
    
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Sets the person to be edited in the dialog.
     * 
     * @param person
     */
    public void setBorrower(borrower borrower) {
        this.borrower = borrower;

        ssnField.setText(borrower.getSsn());
        borrowerNameField.setText(borrower.getBorrowerName());
        phoneField.setText(borrower.getPhone());
        addressField.setText(borrower.getAddress());
    }
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleAdd() {
        if (isInputValid()) {
            borrower.setSsn(ssnField.getText());
            borrower.setBorrowerName(borrowerNameField.getText());
            borrower.setPhone(phoneField.getText());
            borrower.setAddress(addressField.getText());
            
            okClicked = true;
            dialogStage.close();
        }
    }
    
    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }    

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
    	String errorMessage = "";

        if (ssnField.getText() == null || ssnField.getText().length() !=9||!StringUtils.isStrictlyNumeric(ssnField.getText())) {
            errorMessage += "Not a valid SSN entered! An SSN must be a 9 digit number\n"; 
        }
        
        if(ssnAlreadyExists(ssnField.getText() ))
        {
        	errorMessage += "This SSN already exists!\n";
        }
        if (borrowerNameField.getText() == null || borrowerNameField.getText().length() == 0) {
            errorMessage += "No valid Borrower Name entered!\n"; 
        }
        
        if (phoneField.getText() == null || phoneField.getText().length() !=10||!StringUtils.isStrictlyNumeric(phoneField.getText())) {
            errorMessage += "Not valid phone number. Must be a 10 digit num!\n"; 
        }
  
        if (addressField.getText() == null ||addressField.getText().length() == 0) {
            errorMessage += "Not a valid address!\n"; 
        } 

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
    }


        }
    private boolean ssnAlreadyExists(String ssnValue)
    {
    	boolean ssnAlreadyExists=false;
    	String query;
    	ResultSet res1;
    	ConnectHandler.getConnection();
    	query="USE library";
        res1=ConnectHandler.myStmtExecuteQuery(query);
    	try {
    		query="SELECT * From Borrower where ssn="+'\''+ssnValue+'\'';
    		res1=ConnectHandler.myStmtExecuteQuery(query);
    		if(res1.next())
    		{
    			ssnAlreadyExists=true;
    		}    	
    	}catch(Exception e)
    	{
    		System.out.println("Error while checking if this SSN already exists!");
    	}
    	return ssnAlreadyExists;
    }
        
}
