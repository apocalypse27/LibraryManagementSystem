package ch.makery.library.code;

import java.io.IOException;
import java.util.Optional;

import ch.makery.library.model.borrower;
import ch.makery.library.model.listCheckedOutBooks;
import ch.makery.library.view.borwrEditAddDialogController;
import ch.makery.library.view.tabbedViewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;

    public static ObservableList<listCheckedOutBooks> books2BeReturned=FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage=primaryStage;
		this.primaryStage.setTitle("Library Management System");
		
		initRootLayout();
		showTabbedBookView();
		
	}
    /**
     * Returns the data as an observable list of Persons. 
     * @return
     */
    public static ObservableList<listCheckedOutBooks> getbooks2BeReturnedData() {
        return books2BeReturned;
    }
	
	
	public void initRootLayout()
	{
		try {
			/*Load the content from fxml file*/
			FXMLLoader loader=new FXMLLoader(getClass().getResource("../view/rootLayout.fxml"));
			//loader.setLocation(MainApp.class.getResource("../view/rootLayout.fxml"));
			rootLayout=(BorderPane)loader.load();
			
			Scene scene=new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		}catch (IOException e) {
            e.printStackTrace();
        }	
	}
	
	public void showTabbedBookView()
	{
		try {		
			FXMLLoader loader=new FXMLLoader(getClass().getResource("../view/tabbedView2.fxml"));			
			//loader.setLocation(MainApp.class.getResource("../view/tabbedView2.fxml"));
			AnchorPane tabbedView=(AnchorPane) loader.load();	
			
			rootLayout.setCenter(tabbedView);
			
			tabbedViewController controller=loader.getController();
			controller.setMainApp(this);
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}		
	}
	
	 /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     * 
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showBorrowerEditAddDialog(borrower borrower) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
        	FXMLLoader loader=new FXMLLoader(getClass().getResource("../view/addNewBorrower.fxml"));
            //loader.setLocation(MainApp.class.getResource("../view/addNewBorrower.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Borrower");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            borwrEditAddDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBorrower(borrower);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
 
	
	  /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


	public static void main(String[] args) {
		launch(args);
	}
}
