package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import org.apache.commons.fileupload.FileUploadException;



@MultipartConfig

public class Registration{

	public Registration()
	{
		super();
	}
	
	private boolean validateInput(String username, String password, String name, String lastName,
			String email, String phoneNumber, String pictureName )
	{
		boolean flag = true;
		
		if(ServerSideValidation.checkUsername(username) == false)
		{
			flag = false;
		}
		else if(ServerSideValidation.checkPassword(password) == false)
		{
			flag = false;
		}
		else if(ServerSideValidation.checkName(name) == false)
		{
			flag = false;
		}
		else if(ServerSideValidation.checkName(lastName) == false)
		{
			flag = false;
		}		
		else if(ServerSideValidation.checkPhone(phoneNumber) == false)
		{
			flag = false;
		}		
		else if(ServerSideValidation.checkEmail(email) == false)
		{
			flag = false;
		}		
		else if(pictureName!=null)
			if(ServerSideValidation.checkPictureType(pictureName)== false)
			{
				flag = false;
			}
			
		return flag;
	}
	
	
	public String register(String username, String password,byte[] salt, String name, String lastName, String email, String phoneNumber,String picturePath ) throws IllegalStateException, IOException, ServletException, FileUploadException 
	{
		
		
		String result = "Registration failed,please try again";
		if(validateInput(username, password, name, lastName, email, phoneNumber, picturePath))
		{
			String pictureName = picturePath.substring(picturePath.lastIndexOf("\\"));
			Connection connection = null;
			try{
				Class.forName("org.sqlite.JDBC");

				//~~~~~~~~~~~~~~~Change The path of the SQLite database.~~~~~~~~~~~~~~~~~~~~~~
				connection = DriverManager.getConnection("jdbc:sqlite:resource/db.sqlite");
				String sql = "INSERT INTO tblusers" + "(username,password,salt,Name,lastName,email,phone_number,picture,pictureName)" + "VALUES(?,?,?,?,?,?,?,?,?)";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				preparedStatement.setBytes(3, salt);
				preparedStatement.setString(4, name);
				preparedStatement.setString(5, lastName);
				preparedStatement.setString(6, email);
				preparedStatement.setString(7, phoneNumber);
				preparedStatement.setString(8, picturePath);
				preparedStatement.setString(9, pictureName);

				preparedStatement.executeUpdate();

				
				int id = 0;
				String rolesSql = "SELECT id FROM tblusers where username=?";
				preparedStatement = connection.prepareStatement(rolesSql);
				preparedStatement.setString(1, username);
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next())
					id = rs.getInt("id");
				
				rolesSql = "INSERT INTO sys_users_roles (user_id,role_id) VALUES(?,3)";
				preparedStatement = connection.prepareStatement(rolesSql);
				preparedStatement.setInt(1, id);
				preparedStatement.executeUpdate();
				
				result = "success";
			}
			catch(Exception e)
			{
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				result = "exception while trying to register";
			}
			try 
			{
				connection.close();
			} catch (SQLException e) 
			{
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				result = "exception while trying to close connection";
			}
		}
		else
		{
			result = "Invalid Input";
		}
		
		
		
		
		return result;
	  
		
	}
	
	
	
}
