package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.shiro.subject.Subject;

public class CreateEventList {

	public static List<String> EventList(Subject currentUser)
	{
		String username = (String)currentUser.getPrincipals().getPrimaryPrincipal();
		List<String> eventList = new ArrayList<String>();
		Connection connection = null;
		if(currentUser.hasRole("moderator"))
		{
			try{
				Class.forName("org.sqlite.JDBC");
				//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
				connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
				String sql = "SELECT eventTitle FROM userEvents";
				
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				
				while(rs.next())
				{
					eventList.add(rs.getString("eventTitle"));
				}

				
			}
			catch(Exception e)
			{
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
			}
		}
		else
		{
			try{
				Class.forName("org.sqlite.JDBC");
				//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
				connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
				String sql = "SELECT eventTitle FROM userEvents where CreatingUser=?";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, username); 
				ResultSet rs = preparedStatement.executeQuery();
				while(rs.next())
				{
					eventList.add(rs.getString("eventTitle"));
				}

				
			}
			catch(Exception e)
			{
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
			}
		}
		
		
		try 
		{
			connection.close();
		} catch (SQLException e) 
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		return eventList;
	}
}
