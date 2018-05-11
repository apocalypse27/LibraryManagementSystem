package ch.makery.library.util;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class dbUtils {
	
	public Connection conn;
	
	public dbUtils()
	{
		conn=null;
	}
	
	public Connection getConnection()
	{
		try {
			this.conn= DriverManager.getConnection("jdbc:mysql://localhost/?useSSL=false&user=root&password=jeetajaagta27");
			System.out.println("Connection successful");

		}catch(Exception e) {
			System.out.println("Connection not successful");
		}
		return(this.conn);
	}
	
	public ResultSet myStmtExecuteQuery(String query)
	{	
		
		ResultSet res=null;
		try {
			Statement stmt=conn.createStatement();
			System.out.println(query);
			res=stmt.executeQuery(query);
			
		}catch(Exception e)
		{
			System.out.println("Some Error while executing query" + query);
		}
		return(res);		
	}
	
	public boolean myStmtExecuteUpdate(String updateCode)
	{
		try {
			Statement stmt=conn.createStatement();
			System.out.println(updateCode);
			stmt.executeUpdate(updateCode);
			return true;
			
		}catch(Exception e)
		{
			System.out.println("Some Error while executing update " + updateCode);
			return false;
		}
	}
	
	public ResultSet myPsExecuteQuery(String query,String[] setStrings)
	{
		ResultSet res=null;
		try {
			java.sql.PreparedStatement ps=conn.prepareStatement(query);
			for(int i=1;i<=setStrings.length;i++)
			{
				ps.setString(i, setStrings[i-1]);
			}
			
			ps.executeQuery();
			
		}catch(Exception e)
		{
			System.out.println("Some error while executing query in PS " + query);
		}		
		return res;		
	}
	
	public boolean checkExists(String tableName,String colName,String value)
	{
		
		boolean retValue=false;
		String query;
		ResultSet res;
		try {
			query="USE library";
			res=myStmtExecuteQuery(query);
			query="SELECT * FROM " + tableName+ " WHERE " + colName+ "=" + '\''+value+'\'';
			res=myStmtExecuteQuery(query);
			if(res.next())
				return true;
			else
				return false;
			
		}catch(Exception e)
		{
			System.out.println("Error while checking if" + value + " exists in "+ tableName);
			return false;
		}
	}
	
	public void closeDbHandler()
	{
		try {
			conn.close();
			System.out.println("Closed DB Connection");
		}catch(Exception e)
		{
			System.out.println("Error while closing the connection");
		}
		
	}
	


}
