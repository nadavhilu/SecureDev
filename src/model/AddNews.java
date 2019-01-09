package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class AddNews {
	
	public AddNews()
	{
		super();
	}
	
	public String newNews(String title,String news, String username, String addOrEdit, String oldTitle,Integer id,Boolean visible)
	{
		String result = "something went wrong, try again";
		if(addOrEdit != null)
		{						
			Connection connection = null;
						
			try 
			{
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
				PreparedStatement preparedStatement;
				if(addOrEdit == "add")
				{
					String sql = "INSERT INTO tblNews" + "(username,title,news)" + "VALUES(?,?,?);";
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, username);
					preparedStatement.setString(2, title);
					preparedStatement.setString(3, news);
				}
				else
				{
					String sql = "UPDATE tblNews  SET title= ?, news= ?, visible=? WHERE id=? ;";
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, title);
					preparedStatement.setString(2, news);					
					preparedStatement.setBoolean(3, visible);
					preparedStatement.setInt(4, id);
				}
				
				preparedStatement.executeUpdate();
				result = "success";
				
			} catch (SQLException e) {
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				result = "exception while trying to insert to db";
			} catch (ClassNotFoundException e) {
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				result = "clss not found exception";
			}
			try {
				connection.close();
			} catch (SQLException e) {
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				result = "connection close exception";
			}									
		}
		return result;		
	}
	public static int deleteNews(String NewsId) 			
	{
		int result=0;
		Connection connection = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "DELETE FROM tblNews WHERE id = ?;";
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, NewsId);
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

}
