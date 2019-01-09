package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import containers.User;

public class Profile {
	
	private final static String DEFAULT_PICTURE = "resource/upload/default_avatar.jpg";

	public User buildProfile(String username)
	{
		Connection c = null;
		User currentUser;
		try{
			Class.forName("org.sqlite.JDBC");
			String picturePath;
			//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
			c = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "select * from tblusers where username=?;";
			PreparedStatement preparedStatement = c.prepareStatement(sql);
			preparedStatement.setString(1, username);
			
			ResultSet rs = preparedStatement.executeQuery();
			String password = rs.getString("password");
			String name = rs.getString("Name");
			String lastName = rs.getString("lastName");
			String email = rs.getString("email");
			String phoneNumber = rs.getString("phone_number");
	
			String pictureName = rs.getString("pictureName");
			if(pictureName != null)
				picturePath = "resource/upload/" + pictureName;
			else
				picturePath = DEFAULT_PICTURE;
			currentUser = new User(username,password,name,lastName,email,phoneNumber,picturePath );
			c.close();
		  if (rs.next()) 
		  {
			return currentUser;
		  } 
		  
	   }
		catch(Exception e)
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
			return null;
		}
		return null;
		
	}
}
