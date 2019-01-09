package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;

import org.apache.commons.fileupload.FileUploadException;

public class EditProfile {
	private final static String DEFAULT_PICTURE = "resource/upload/default_avatar.jpg";
	private final static int USERNAME = 1;
	private final static int PASSWORD = 2;
	private final static int SALT = 3;
	private final static int NAME = 4;
	private final static int LASTNAME = 5;
	private final static int EMAIL = 6;
	private final static int PHONENUMBER = 7;
	private final static int PICTUREPATH = 8;
	private final static int PICTURENAME = 9;

	private String buildSQLStatement(String username, String password, String name, String lastName, String email, String phoneNumber, String picturePath, String pictureName, List<Integer> values)
	{
		String sQLStatement = null;
		if(username!=null)
		{
			sQLStatement = "username= ? ";
			values.add(USERNAME);
		}
		if(password!=null)
		{
			if(sQLStatement!=null)
			{
				sQLStatement += ", ";
				sQLStatement += "password= ? ";
			}
			else
				sQLStatement = "password= ? ";
			values.add(PASSWORD);
			sQLStatement += ", ";
			sQLStatement += "salt= ? ";
			values.add(SALT);
		}
		
		if(name!=null)
		{
			if(sQLStatement!=null)
			{
				sQLStatement += ", ";
				sQLStatement += "Name= ? ";
			}
			else
				sQLStatement = "Name= ? ";
			values.add(NAME);
		}
		if(lastName!=null)
		{
			if(sQLStatement!=null)
			{
				sQLStatement += ", ";
				sQLStatement += "lastName= ? ";
			}
			else
				sQLStatement = "lastName= ? ";
			values.add(LASTNAME);
		}
		if(email!=null)
		{
			if(sQLStatement!=null)
			{
				sQLStatement += ", ";
				sQLStatement += "email= ? ";
			}
			else
				sQLStatement = "email= ? ";
			values.add(EMAIL);
		}
		if(phoneNumber!=null)
		{
			if(sQLStatement!=null)
			{
				sQLStatement += ", ";
				sQLStatement += "phone_number= ? ";
			}
			else
				sQLStatement = "phone_number= ? ";
			values.add(PHONENUMBER);
		}
		if(picturePath!=null)
		{
			if(sQLStatement!=null)
			{
				sQLStatement += ", ";
				sQLStatement += "picture= ? ";
			}
			else
				sQLStatement = "picture= ? ";
			values.add(PICTUREPATH);
		}
		if(pictureName!=null)
		{
			if(sQLStatement!=null)
			{
				sQLStatement += ", ";
				sQLStatement += "pictureName= ? ";
			}
			else
				sQLStatement = "pcitureName= ? ";
			values.add(PICTURENAME);
		}
		return sQLStatement;
	}

	private boolean validateInput(String username, String password, String name, String lastName, String email, String phoneNumber, String pictureName )
	{
		if(username!=null)
		{
			if(ServerSideValidation.checkUsername(username)== false)
				return false;
		}
		if(password!=null)
		{
			if(ServerSideValidation.checkPassword(password)==false)
				return false;
		}
		if(name!=null)
		{
			if(ServerSideValidation.checkName(name)==false)
				return false;
		}
		if(lastName!=null)
		{
			if(ServerSideValidation.checkName(lastName)==false)
				return false;
		}
		if(email!=null)
		{
			if(ServerSideValidation.checkEmail(email)==false)
				return false;
		}
		if(phoneNumber!=null)
		{
			if(ServerSideValidation.checkPhone(phoneNumber)==false)
				return false;
		}
		if(pictureName!=null)
		{
			if(ServerSideValidation.checkPictureType(pictureName)==false)
				return false;
		}

		return true;

	}

	public String EditUserProfile(String oldUsername, String username, String password,byte[] salt, String name, String lastName, String email, String phoneNumber,String picturePath, String pictureName ) throws IllegalStateException, IOException, ServletException, FileUploadException
	{

		Connection c = null;
		/*
		 * 
		 * List of the parameters that should be included in the sql statement to update in the database
		 * 
		 */
		List<Integer> values = new ArrayList<Integer>();

		String sqlStatement = buildSQLStatement( username,  password,  name,  lastName,  email,  phoneNumber,  picturePath,  pictureName,values);
		if(validateInput( username, password, name, lastName, email, phoneNumber, pictureName ))
		{


			try{
				Class.forName("org.sqlite.JDBC");

				//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
				c = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
				/*
				 * 
				 * we add the statement that we prepared to the sql String, so that we can add to the statement the amount of feilds we need to add
				 * 
				 */
				String sql = "UPDATE tblusers  SET " + sqlStatement + " WHERE username= ? ;";
				PreparedStatement preparedStatement = c.prepareStatement(sql);
				/*
				 * 
				 * a for loop that runs the amount of possibly changed values, and sets the right value at it's right place if it should be there
				 * 
				 */
				int index;
				for(index=0; index < values.size(); index++)
				{
					switch(values.get(index))
					{
					case USERNAME:
						preparedStatement.setString(index+1, username);
						break;
					case PASSWORD:
						preparedStatement.setString(index+1, password);
						break;
					case SALT:
						preparedStatement.setBytes(index+1, salt);
						break;
					case NAME:
						preparedStatement.setString(index+1, name);
						break;
					case LASTNAME:
						preparedStatement.setString(index+1, lastName);
						break;
					case EMAIL:
						preparedStatement.setString(index+1, email);
						break;
					case PHONENUMBER:
						preparedStatement.setString(index+1, phoneNumber);
						break;
					case PICTUREPATH:
						preparedStatement.setString(index+1, picturePath);
						break;
					case PICTURENAME:
						preparedStatement.setString(index+1, pictureName);
						break;
					}

				}
				/*
				 * 
				 * makes sure we update the right user
				 * 
				 */
				preparedStatement.setString(index+1, oldUsername);

				preparedStatement.executeUpdate();

				if(pictureName != null)
					picturePath = "resource/upload/" + pictureName;
				else
					picturePath = DEFAULT_PICTURE;

				c.close();
				return "sucess";

			}
			catch(Exception e)
			{
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				return "exception while trying to edit profile";
			}
		}
		else
			return "the parameters you entered are not premited";

	}
	public static Integer deleteUser(String UserId) 			
	{
		Integer result=0;
		Connection connection = null;
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
			String sql = "DELETE FROM tblusers WHERE id = ?;";
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, UserId);
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
