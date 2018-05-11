package ch.makery.library.view;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mysql.jdbc.JDBC4PreparedStatement;

import ch.makery.library.model.bookToCheckout;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CheckoutBooks extends bookSearchController{

    	 
	protected boolean readyToCheckout(bookToCheckout selectedBook) {
	    	String errorMessage="";
	    	dbHandler.getConnection();

	    	if(selectedBook.ISBN==null||selectedBook.ISBN.length()==0)
	    	{
	    		errorMessage+="Enter a valid ISBN value\n";
	    	}
	    	
	    	else if(selectedBook.card_Id==null||selectedBook.card_Id.length()==0)
	    	{
	    		errorMessage+="Enter a valid Card ID value\n";
	    	}
	    	else if (!dbHandler.checkExists("book", "isbn", selectedBook.ISBN)) {
	    		errorMessage+="This ISBN does not exist\n";
	    	}
	    	else if (!dbHandler.checkExists("borrower", "Card_Id", selectedBook.card_Id)) {
	    		errorMessage+="This borrower with Card Id "+ selectedBook.card_Id + " does not exist\n";
	    	}
	    	else if (!checkIsbnAvailable(selectedBook.ISBN))
	    	{
	    		errorMessage+="This book is already checked out. Not available currently!\n";
	    	}
	    	else if(checkedOutLimitBooks(selectedBook.card_Id,2))
	    	{
	    		errorMessage+="You have already checked out 3 books\n";
	    	}
	    	if(errorMessage.length()==0)
	    		return true;
	    	else {
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
	
	protected boolean checkoutNewBook(bookToCheckout selectedBook)
	{
		ResultSet res;
		String query;
		try {
			//boolean successUpdating;
			String updateCode;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Now use today date.
			String cD=sdf.format(c.getTime());
			java.util.Date currentDate = sdf.parse(cD);
			java.sql.Date sqlCurrentDate = new java.sql.Date(currentDate.getTime());
			
			c.add(Calendar.DATE, 14); // Adding 5 days
			String dD = sdf.format(c.getTime());
			java.util.Date dueDate = sdf.parse(dD);
			java.sql.Date sqlDueDate = new java.sql.Date(dueDate.getTime());
	
			
			dbHandler.getConnection();
			query="Use Library";
			res=dbHandler.myStmtExecuteQuery(query);
		
			updateCode="INSERT INTO book_loans(Loan_Id,Isbn,Card_id,Date_out,Due_Date,Date_In)"
					+ " VALUES(0,?,?,?,?,NULL)";
			java.sql.PreparedStatement ps=dbHandler.conn.prepareStatement(updateCode);
			ps.setString(1, selectedBook.ISBN);
			ps.setInt(2, Integer.parseInt(selectedBook.card_Id));
			ps.setDate(3, sqlCurrentDate);
			ps.setDate(4, sqlDueDate);
			System.out.println(((JDBC4PreparedStatement)ps).asSql());
			ps.executeUpdate();	
			dbHandler.closeDbHandler();	
			//listAllCheckoutBooks(selectedBook.card_Id);
			return true;
		}catch(Exception e)
		{
			System.out.println("Error while checking out book "+ selectedBook.ISBN);
			return false;
		}			
	}	
	
	protected boolean checkedOutLimitBooks(String cardId,int limit)
	{
		String query;
		boolean limitReached=false;
		ResultSet res;
		try {
			dbHandler.getConnection();
			query="USE Library";
			res=dbHandler.myStmtExecuteQuery(query);
			query="SELECT card_id,COUNT(*) FROM book_Loans where Date_Out IS NOT NULL AND DATE_IN IS NULL AND Card_ID="+ cardId+
				    " GROUP BY card_Id HAVING COUNT(*)>"+String.valueOf(limit);
			res=dbHandler.myStmtExecuteQuery(query);		
			if(res.next())
			{
				limitReached= true;
			}
			else
			{
				limitReached= false;
			}
		}catch(Exception e)
		{
			System.out.println("Error while checking if card ID has more than 3 books in his name");
		}
		return limitReached;
	}
	
	protected ResultSet listAllCheckoutBooks(String cardId)
	{
		String query;
		ResultSet res=null;
		/*Clear the table*/
		try {
			
			dbHandler.getConnection();
			query="Use Library";
			res=dbHandler.myStmtExecuteQuery(query);
			query="SELECT BL.card_id,B.ISBN,B.Title,BL.Date_Out,BL.Due_Date FROM BOOK_LOANS AS BL,BOOK AS B WHERE "
					+ "B.ISBN=BL.ISBN AND BL.Card_ID="+cardId+" AND Date_IN IS NULL";
			res=dbHandler.myStmtExecuteQuery(query);

			
		}catch(Exception e)
		{
			System.out.println("Error while printing out checked out books");
		}
		//dbHandler.closeDbHandler();
		return res;	
	}

}
