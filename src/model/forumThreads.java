package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import containers.ForumData;

import org.owasp.validator.html.*; // Import AntiSamy

public class forumThreads 
{
	public forumThreads()
	{
		super();
	}
	
	private String threadName;
	private String creatingUser;
	private Timestamp creationTime;
	private Integer id;
	/**
	 * newThread(
	 * @param ThreadTitle
	 * @param CreatingUser
	 * @param CreationTime
	 * @param PostBody)
	 * called by ForumController doPost() to take care of the submitted new thread form.
	 * 
	 * Handles inserting a new thread and the first post to the database using 2 PreparedStatement:
	 * threadStatement handles inserting to ForumThreads table
	 * and postStatement handles inserting to ThreadPosts table.
	 * @throws PolicyException 
	 * @throws ScanException 
	 */
	public void newThread(String ThreadTitle, String CreatingUser, Timestamp CreationTime, String PostBody) 			
	{
		Connection connection = null;
		String POLICY_FILE_LOCATION = "WebContent/WEB-INF/antisamy-slashdot-1.4.4.xml";
		String dirtyInput = PostBody;
		
		try
		{
			Policy policy = Policy.getInstance(POLICY_FILE_LOCATION);
			AntiSamy as = new AntiSamy(); // Create AntiSamy object
			CleanResults cr = as.scan(dirtyInput, policy, AntiSamy.SAX); // Scan dirtyInput
			String post = cr.getCleanHTML();
			
			try
			{
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");

				PreparedStatement threadStatement;
				PreparedStatement postStatement;

				String sql = "INSERT INTO ForumThreads" + "(ThreadTitle, Username, CreationDate)" + "VALUES(?,?,?);";
				threadStatement = connection.prepareStatement(sql);
				threadStatement.setString(1, ThreadTitle);
				threadStatement.setString(2, CreatingUser);
				threadStatement.setTimestamp(3, CreationTime);
				threadStatement.executeUpdate();

				String sql2 = "INSERT INTO ThreadPosts" + "(ThreadTitle, Username, CreationDate, PostBody)" + "VALUES(?,?,?,?);";
				postStatement = connection.prepareStatement(sql2);
				postStatement.setString(1, ThreadTitle);
				postStatement.setString(2, CreatingUser);
				postStatement.setTimestamp(3, CreationTime);
				postStatement.setString(4, post);
				postStatement.executeUpdate();
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
		}
		catch (PolicyException | ScanException e) 
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}	

	}

	public boolean deleteThread(String ThreadId) 			
	{
		boolean result=false;
		Connection connection = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "DELETE FROM ForumThreads WHERE id = ?;";
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, ThreadId);
			result=preparedStatement.execute();
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

	/**
	 * loadThreads() is called by ForumController doGet() when the user requests the Forum page.
	 * 
	 * It retrieves the data from ForumThreads table using PreparedStatement and ResultSet
	 * and then puts the data in a List which is returned to the calling method and sent to Forum.jsp
	 * in the request.
	 * 
	 * Each thread title is changed to a hyperlink directing to ThreadController
	 * with the thread's title assigned to threadTitle attribute in the request.
	 */
	public List<ForumData> loadThreads()
	{
		Connection connection = null;
		ResultSet rs;
		List<ForumData> threadsList = new ArrayList<ForumData>();
		
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "SELECT * FROM ForumThreads";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);			
			preparedStatement.execute();
			rs = preparedStatement.getResultSet();
			
			ForumData data;
			while(rs.next())
			{
				threadName = rs.getString("ThreadTitle");
				id = rs.getInt("id");
				String threadLink = "href=\"ThreadController?threadTitle=" + threadName + "&threadID=" + id + "\"";
				
				creatingUser = rs.getString("username");
				creationTime = rs.getTimestamp("CreationDate");
				String s = creationTime.toString().split("\\.")[0];
				
				data = new ForumData(id, threadName, creatingUser, s, threadLink);
				threadsList.add(data);
			}
			rs.close();
			preparedStatement.close();			
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
		
		return threadsList;
	}
}
