package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import containers.Event;
import containers.News;
import containers.User;
import containers.UserRolesInfo;
import shiro.SQLite.Roles.UserDaoImpl;

/*
 * 
 * a class to be used to retrive information from the server, Still need to move few functions here
 * 
 */


public class RetriveInformation {

	private static int homeAmount=3;
	
	public static String upcomingEvents()
	{
		String sqlQuary = "SELECT * FROM userEvents WHERE happeningDate >= ? ORDER BY happeningDate asc";
		return sqlQuary;
	}
	
	public static String oldEvents()
	{
		String sqlQuary = "SELECT * FROM userEvents WHERE happeningDate < ? ORDER BY happeningDate desc";
		return sqlQuary;
	}
	
	public static List<Event> loadEvents(String sqlQuary)
	{

		Connection connection = null;
		ResultSet rs;
		List<Event> eventsList = new ArrayList<Event>();
		
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			Date now = new Date();
			Timestamp sqlDate = new Timestamp(now.getTime());
			String sql = sqlQuary;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setTimestamp(1, sqlDate);
			
			rs = preparedStatement.executeQuery();
			Event event;
			while(rs.next())
			{
				event = new Event();
				String eventId = rs.getString("id");
				String link = "href=\"eventList?eventId=" + eventId + "\"";
				String eventTitle = rs.getString("eventTitle");
				event.setEventTitle(eventTitle);
				event.setHappeningDate(rs.getTimestamp("happeningDate"));
				event.setLink(link);
				event.setParticipating(rs.getInt("participatingUsers"));
				event.setUserLimit(rs.getInt("userLimit"));
				eventsList.add(event);
			}			
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
		return eventsList;		
	}
	
	public static Event RetriveEvent(String id)
	{
		Connection connection = null;
		Event eventToEdit = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sqlStatement = "SELECT * from userEvents where id = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, id);
			ResultSet result = preparedStatement.executeQuery();
			if(result.next())
			{
				eventToEdit = new Event(result.getInt("id"),result.getString("username"), result.getString("eventTitle"), result.getTimestamp("happeningDate"),
						result.getInt("userLimit"), result.getInt("participatingUsers"), result.getString("eventDescription"), result.getString("eventLocation"));
			}
		} 
		catch (ClassNotFoundException e) 
		{
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		} 
		catch (SQLException e) 
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
		
		return eventToEdit;
	}

	public static List<Event> RetriveHomeEvents()
	{
		Connection connection = null;
		List<Event> eventsList = new ArrayList<Event>();
		try {
			Class.forName("org.sqlite.JDBC");
			String sqlStatement = "SELECT * from userEvents where happeningDate>? limit ?";
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			java.util.Date date = new java.util.Date();
			java.sql.Timestamp sqlDate=new java.sql.Timestamp(date.getTime());
			preparedStatement.setTimestamp(1, sqlDate);
			preparedStatement.setInt(2, getHomeAmount());
			ResultSet result = preparedStatement.executeQuery();
			
			Event homeEvent;
			while(result.next())
			{
				homeEvent = new Event();
				String eventId = result.getString("id");
				String EventLink = "href=\"eventList?eventId=" + eventId + "\"";
				String EventTitle = result.getString("eventTitle");
				homeEvent.setEventTitle(EventTitle);
				homeEvent.setHappeningDate(result.getTimestamp("happeningDate"));
				homeEvent.setLink(EventLink);
				eventsList.add(homeEvent);
			}

		} catch (ClassNotFoundException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try {
			connection.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}

		return eventsList;
	}

	public List<News> RetriveNews(Subject currentUser)
	{
		Connection connection = null;
		ResultSet result = null;
		List<News> news = new ArrayList<News>();
		String sqlStatement;
		try {
			Class.forName("org.sqlite.JDBC");
			if(currentUser.hasRole("Moderator"))
			{
				sqlStatement = "SELECT * from tblNews";
				connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
				PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
				result = preparedStatement.executeQuery();
			}			
			else
			{
				sqlStatement = "SELECT * from tblNews where visible=?";
				connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
				PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
				preparedStatement.setBoolean(1, true);
				result = preparedStatement.executeQuery();
			}
			while(result.next())
			{
				String Link = "href=\"News?newsId=" + result.getString("id") + "\"";
				news.add(new News(result.getInt("id"),result.getString("username"),result.getString("title"),result.getString("news"),result.getBoolean("visible"), Link));  			
			}

		} catch (ClassNotFoundException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try {
			connection.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}

		return news;
	}

	public static List<News> RetriveHomeNews()
	{
		Connection connection = null;
		ResultSet result = null;
		List<News> news = new ArrayList<News>();
		String sqlStatement;
		try {
			Class.forName("org.sqlite.JDBC");
			sqlStatement = "SELECT * from tblNews where visible='1' order by id desc limit ?";
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, getHomeAmount());
			result = preparedStatement.executeQuery();

			while(result.next())
			{
				String Link = "href=\"News?newsId=" + result.getString("id") + "\"";
				news.add(new News(result.getInt("id"),result.getString("username"),result.getString("title"),result.getString("news"),result.getBoolean("visible"), Link));  			
			}

		} catch (ClassNotFoundException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try {
			connection.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}

		return news;
	}


	public News NewsToEdit(String id)
	{
		Connection connection = null;
		ResultSet result = null;
		News news = null;
		String sqlStatement;
		try {
			Class.forName("org.sqlite.JDBC");

			sqlStatement = "SELECT * from tblNews where id=?";
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, id);
			result = preparedStatement.executeQuery();

			while(result.next())
			{
				String Link = "href=\"News?newsId=" + result.getString("id") + "\"";
				news= new News(result.getInt("id"),result.getString("username"),result.getString("title"),result.getString("news"),result.getBoolean("visible"), Link);  			
			}

		} catch (ClassNotFoundException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try {
			connection.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}

		return news;
	}
	
	public static List<UserRolesInfo> RetriveUserRolesInfo()
	{
		Connection connection = null;
		ResultSet result = null;
		List<UserRolesInfo> usersInfo = new ArrayList<UserRolesInfo>();
		String sqlStatement;
		try {
			Class.forName("org.sqlite.JDBC");
			
			sqlStatement = "SELECT id,username,role_id from tblusers as u, sys_users_roles as ur where u.id=ur.user_id";
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			result = preparedStatement.executeQuery();
			
			String user = result.getString("username");
			int count = 0;
			Integer id = result.getInt("id");
			while(result.next())
			{
				if(user.equals(result.getString("username")))
				{
					count++;
				}
				else
				{
					switch(count)
					{
						case 1:
							usersInfo.add(new UserRolesInfo(id,user,false,false,true));
							break;
						case 2:
							usersInfo.add(new UserRolesInfo(id,user,false,true,true));
							break;
						case 3:
							usersInfo.add(new UserRolesInfo(id,user,true,true,true));
							break;
					}
					count = 1;
					user = result.getString("username");
					id = result.getInt("id");
				}			
			}
			switch(count)
			{
				case 1:
					usersInfo.add(new UserRolesInfo(id,user,false,false,true));
					break;
				case 2:
					usersInfo.add(new UserRolesInfo(id,user,false,true,true));
					break;
				case 3:
					usersInfo.add(new UserRolesInfo(id,user,true,true,true));
					break;
			}
			
		} catch (ClassNotFoundException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		try {
			connection.close();
		} catch (SQLException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		
		return usersInfo;
	}
	
	public static List<Event> loadEvents()
	{

		Connection connection = null;
		ResultSet rs;
		List<Event> eventsList = new ArrayList<Event>();
		
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			Date now = new Date();
			Timestamp sqlDate = new Timestamp(now.getTime());
			String sql = "SELECT * FROM userEvents WHERE happeningDate >= ? ORDER BY happeningDate desc";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setTimestamp(1, sqlDate);
			
			rs = preparedStatement.executeQuery();
			
			while(rs.next())
			{
				String link = "href=\"EventController?Id=" + rs.getString("id") + "\"";
				eventsList.add(new Event(rs.getInt("id"),rs.getString("username"),rs.getString("eventTitle"),rs.getTimestamp("happeningDate"),
						rs.getInt("userLimit"),rs.getInt("participatingUsers"),rs.getString("eventDescription"),rs.getString("eventLocation"),link));
			}			
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
		return eventsList;
		
	}
	
	public static int getHomeAmount() {
		return homeAmount;
	}

	public static void setHomeAmount(int homeAmount) {
		RetriveInformation.homeAmount = homeAmount;
	}
	

	public static String getUser(String idOfItem, String tableName) 
	{
		Connection connection = null;
		ResultSet result = null;
		String username=null;
		String sqlStatement = null;
		try {
			Class.forName("org.sqlite.JDBC");
			switch(tableName)
			{
				case "tblNews":
					sqlStatement = "SELECT username from tblNews where id=?";
					break;
				case "userEvents":
					sqlStatement = "SELECT username from userEvents where id=?";
					break;
				case "tblusers":
					sqlStatement = "SELECT username from tblusers where id=?";
					break;
				case "ThreadPosts":
					sqlStatement = "SELECT username from ThreadPosts where id=?";
					break;
			}
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, idOfItem);
			result = preparedStatement.executeQuery();

			if(result.next())
			{
				username = result.getString("username");
			}
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
		return username;
	}

	public static String getTitle(String idOfItem, String tableName) {
		Connection connection = null;
		ResultSet result = null;
		String title=null;
		String sqlStatement;
		try {
			Class.forName("org.sqlite.JDBC");
			sqlStatement = "SELECT title from ? where id=?";
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, tableName);
			preparedStatement.setString(2, idOfItem);
			result = preparedStatement.executeQuery();

			if(result.next())
			{
				title = result.getString("title");
			}
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
		return title;
	}

	public static Integer RetriveId(String username) {
		
		Connection connection = null;
		ResultSet result = null;
		Integer id=null;
		String sqlStatement;
		try {
			Class.forName("org.sqlite.JDBC");
			sqlStatement = "SELECT id from tblusers where username=?";
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setString(1, username);
			result = preparedStatement.executeQuery();

			if(result.next())
			{
				id = Integer.parseInt(result.getString("id"));
			}
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
		return id;
	}
	
	public static User retriveProfile(Integer id)
	{
		Connection connection = null;
		ResultSet result = null;
		User user = null;
		String sqlStatement;
		try {
			Class.forName("org.sqlite.JDBC");
			sqlStatement = "SELECT * from tblusers where id=?";
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, id);
			result = preparedStatement.executeQuery();

			if(result.next())
			{
				user = new User(result.getString("username"),result.getString("password"),result.getString("Name"),result.getString("lastName"),
						result.getString("email"),result.getString("phone_number"),result.getString("picture"));
			}
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
		return user;
	}
	
	public static boolean isParticipating(Integer userId, Integer eventId)
	{
		Connection connection = null;
		ResultSet resultSet = null;
		boolean result=true;
		String sqlStatement;
		try {
			Class.forName("org.sqlite.JDBC");
			sqlStatement = "SELECT * from participatingUsers where user_id=? and event_id=?";
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, eventId);
			resultSet = preparedStatement.executeQuery();

			if(resultSet.next())
			{
				result = false;
			}
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
	
	public static List<News> searchNews(String searchValue)
	{
		Connection connection = null;
		ResultSet result;
		List<News> newsList = new ArrayList<News>();
		String sql;
		
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			if(SecurityUtils.getSubject().hasRole(UserDaoImpl.MODERATOR_ROLE))
				sql = "SELECT * FROM tblNews WHERE title LIKE ?";
			else
				sql = "SELECT * FROM tblNews WHERE title LIKE ? and visible='1'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,"%"+ searchValue +"%");
			
			result = preparedStatement.executeQuery();
			
			while(result.next())
			{
				String link = "href=\"News?newsId=" + result.getString("id") + "\"";
				newsList.add(new News(result.getInt("id"),result.getString("username"),result.getString("title"),result.getString("news"),
						result.getBoolean("visible"),link));
			}			
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
		return newsList;
	}
	
	public static List<Event> searchEvents(String searchValue)
	{

		Connection connection = null;
		ResultSet result;
		List<Event> eventsList = new ArrayList<Event>();
		
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");


			String sql = "SELECT * FROM userEvents WHERE eventTitle like ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,"%"+ searchValue +"%");
			
			result = preparedStatement.executeQuery();
			
			while(result.next())
			{
				String link = "href=\"EventController?Id=" + result.getString("id") + "\"";
				eventsList.add(new Event(result.getInt("id"),result.getString("username"),result.getString("eventTitle"),result.getTimestamp("happeningDate"),
						result.getInt("userLimit"),result.getInt("participatingUsers"),result.getString("eventDescription"),result.getString("eventLocation"),link));
			}			
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
		return eventsList;
		
	}
	
	public static boolean checkEventSpotsLeft(Integer event_id)
	{
		boolean result = false;				
		Connection connection = null;

		try{
			Class.forName("org.sqlite.JDBC");
			//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "SELECT participatingUsers, userLimit FROM userEvents WHERE id = ?;";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, event_id);
			ResultSet rs = preparedStatement.executeQuery();
			
			Integer ParticipatingUsers = rs.getInt("participatingUsers");
			Integer UserLimit = rs.getInt("userLImit");

			if(ParticipatingUsers < UserLimit)
			{
				result = true;
			}
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
