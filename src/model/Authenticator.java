package model;


import java.sql.*;


public class Authenticator {
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * Authentication function is responsible to perform the connection to the database.
	 * In addition, the function is responsible for the authentication process.
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */  
/*	private final static String goodPassword = "(((?=.*\\d)((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9]))).{6,20})";
	
	private boolean checkUsername(String username)
	{
		if(username.length() > 3 && username.length() < 20)
			return true;
		else
			return false;
	}
	
	private boolean checkPassword(String password)
	{
		Pattern pattern = Pattern.compile(goodPassword);
		Matcher match = pattern.matcher(password);
		if(match.find())
			return true;
		else
			return false;
	} */
	
			
	public String authenticate(String username, String password) 
	{
		if( ServerSideValidation.checkUsername(username) && ServerSideValidation.checkPassword(password))
		{
			
			Connection c = null;
			try{
				Class.forName("org.sqlite.JDBC");

				//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
				c = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
				String sql = "select * from tblusers where username=? and password=?;";
				PreparedStatement preparedStatement = c.prepareStatement(sql);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				ResultSet rs = preparedStatement.executeQuery();

				c.close();
				if (rs.next()) 
				{
					return "success";
				} 
				else
				{
					return "Bad username or password, please try again";
				}				
			}
			catch(Exception e)
			{
				return "SQL ERROR";
			}
		}
		else
			return "Don't mess with the server";
	}
}