package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.apache.shiro.subject.Subject;

/*
 * 
 * class used to handle event events(adding, editing)
 * 
 * 
 */

public class EventHandle {

	public String eventAdd(String eventName, String eventDescription, Timestamp eventDate, Integer participants,String username, String location)
	{
		String result = "fail";


		Connection connection = null;
		try{
			Class.forName("org.sqlite.JDBC");
			//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "INSERT INTO userEvents" + "(username,eventTitle,happeningDate,userLimit,eventDescription,eventLocation)" + "VALUES(?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, eventName);
			preparedStatement.setTimestamp(3, eventDate);
			preparedStatement.setInt(4, participants);
			preparedStatement.setString(5, eventDescription);
			preparedStatement.setString(6, location);

			preparedStatement.executeUpdate();

			result = "success";
		}
		catch(Exception e)
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
			result = "exception while trying to add event";
		}
		try 
		{
			connection.close();
		} catch (SQLException e) 
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
			result = "exception while trying to close the connection";
		}
		return result;
	}

	public String editEvent(String eventName, String eventDescription, Timestamp eventDate, Integer participants,String username, String location, String oldTitle)
	{
		String result = "fail";				
		Connection connection = null;
		try{
			Class.forName("org.sqlite.JDBC");
			//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "UPDATE userEvents "
					+ "SET username = ? ,eventTitle = ? ,happeningDate = ? ,userLimit = ? ,eventDescription = ? ,eventLocation = ? "
					+ "WHERE eventTitle = ? ;";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, eventName);
			preparedStatement.setTimestamp(3, eventDate);
			preparedStatement.setInt(4, participants);
			preparedStatement.setString(5, eventDescription);
			preparedStatement.setString(6, location);
			preparedStatement.setString(7, oldTitle);

			preparedStatement.executeUpdate();

			result = "success";
		}
		catch(Exception e)
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
			result = "exception while trying to edit event";
		}
		try 
		{
			connection.close();
		} catch (SQLException e) 
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
			result = "exception while trying to close db";
		}
		return result;
	}

	public static Integer deleteEvent(String EventId) 			
	{
		Integer result=0;
		Connection connection = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "DELETE FROM userEvents WHERE id = ?;";
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, EventId);
			result=preparedStatement.executeUpdate();
		}		
		catch (SQLException e) 
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		catch (ClassNotFoundException e)
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}

		try 
		{
			connection.close();
		} 
		catch (SQLException e)
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}				
		return result;
	}

	public boolean participateInEvent(Subject currentUser, Integer event_id)
	{
		boolean result = false;				
		Connection connection = null;
		Integer user_id = 0;
		if(RetriveInformation.checkEventSpotsLeft(event_id) == true)
		{
			user_id = RetriveInformation.RetriveId((String)currentUser.getPrincipals().getPrimaryPrincipal());
			try{
				Class.forName("org.sqlite.JDBC");
				//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
				connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
				String sql = "INSERT INTO participatingUsers" + "(user_id,event_id)" + "VALUES(?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, user_id);
				preparedStatement.setInt(2, event_id);			
				preparedStatement.executeUpdate();

				String sql2 = "SELECT participatingUsers FROM userEvents WHERE id = ?;";
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.setInt(1, event_id);
				ResultSet rs = preparedStatement.executeQuery();
				Integer ParticipatingUsers = rs.getInt("participatingUsers");
				ParticipatingUsers = ParticipatingUsers + 1;

				String sql3 = "UPDATE userEvents SET participatingUsers = ? WHERE id = ?;";
				preparedStatement = connection.prepareStatement(sql3);
				preparedStatement.setInt(1, ParticipatingUsers);
				preparedStatement.setInt(2, event_id);
				preparedStatement.execute();		

				result = true;
			}
			catch(Exception e)
			{
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				result = false;
			}
			try 
			{
				connection.close();
			} catch (SQLException e) 
			{
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				result = false;
			}
		}
		return result;


	}

	public Integer unparticipateInEvent(Subject currentUser, Integer event_id)
	{
		Integer result = 0;				
		Connection connection = null;
		Integer user_id = 0;
		user_id = RetriveInformation.RetriveId((String)currentUser.getPrincipals().getPrimaryPrincipal());
		try{
			Class.forName("org.sqlite.JDBC");
			//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "DELETE FROM participatingUsers WHERE user_id = ? and event_id = ? ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user_id);
			preparedStatement.setInt(2, event_id);
			preparedStatement.execute();

			String sql2 = "SELECT participatingUsers FROM userEvents WHERE id = ?;";
			preparedStatement = connection.prepareStatement(sql2);
			preparedStatement.setInt(1, event_id);
			ResultSet rs = preparedStatement.executeQuery();
			Integer ParticipatingUsers = rs.getInt("participatingUsers");
			ParticipatingUsers = ParticipatingUsers - 1;

			String sql3 = "UPDATE userEvents SET participatingUsers = ? WHERE id = ?;";
			preparedStatement = connection.prepareStatement(sql3);
			preparedStatement.setInt(1, ParticipatingUsers);
			preparedStatement.setInt(2, event_id);
			preparedStatement.executeUpdate();

			result = preparedStatement.executeUpdate();


		}
		catch(Exception e)
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try 
		{
			connection.close();
		} catch (SQLException e) 
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		return result;


	}

}
