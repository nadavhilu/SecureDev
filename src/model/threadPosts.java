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

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import containers.ForumData;

public class threadPosts 
{
	public threadPosts()
	{
		super();
	}

	String threadName;
	String creatingUser;
	Timestamp postCreationTime;
	String postContent;
	Integer id;
	
	public void editPost(Integer ID, String PostBody)
	{
		Connection connection = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");			
			PreparedStatement preparedStatement;
			String sql = "UPDATE ThreadPosts  SET PostBody= ? WHERE id= ? ;";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, PostBody);
			preparedStatement.setInt(2, ID);
			preparedStatement.executeUpdate();
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

	public void newPost(String ThreadTitle, String CreatingUser, Timestamp CreationTime, String PostBody)
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

				PreparedStatement preparedStatement;
				String sql = "INSERT INTO ThreadPosts" + "(ThreadTitle, username, CreationDate, PostBody)" + "VALUES(?,?,?,?);";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, ThreadTitle);
				preparedStatement.setString(2, CreatingUser);
				preparedStatement.setTimestamp(3, CreationTime);
				preparedStatement.setString(4, post);
				preparedStatement.executeUpdate();
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

	public static Integer deletePost(String PostId) 			
	{
		Integer result=0;
		Connection connection = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "DELETE FROM ThreadPosts WHERE id = ?;";
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, PostId);
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
	
	public List<ForumData> loadPosts(String threadTitle)
	{
		Connection connection = null;
		ResultSet rs;
		List<ForumData> postsList = new ArrayList<ForumData>();
		
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "SELECT * FROM ThreadPosts WHERE ThreadTitle = ?"; //AND id = THREAD TABLE ID
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, threadTitle);
			preparedStatement.execute();
			rs = preparedStatement.getResultSet();
			
			creatingUser = rs.getString("username");
			postCreationTime = rs.getTimestamp("CreationDate");
			String creationTime = postCreationTime.toString().split("\\.")[0];
			postContent = rs.getString("PostBody");
			id = rs.getInt("id");
			
			ForumData data = new ForumData(id, threadTitle, creatingUser, creationTime, postContent);
			postsList.add(data);
			
			rs.next();
			while(rs.next())
			{
				id = rs.getInt("id");
				creatingUser = rs.getString("username");
				postCreationTime = rs.getTimestamp("CreationDate");
				creationTime = postCreationTime.toString().split("\\.")[0];
				postContent = rs.getString("PostBody");
				data = new ForumData(id, creatingUser, creationTime, postContent);
				
				postsList.add(data);
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
		
		return postsList;
	}
}
