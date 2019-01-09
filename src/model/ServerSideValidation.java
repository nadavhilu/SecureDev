package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerSideValidation 
{

	
	private final static String Goodnumber="\\d{10}$";
	private final static String goodPassword = "((((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9]))).{6,20})";
	private final static String goodEmail = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
	private final static int maxPictureSize = 1024*1024*2;
	private final static String goodPictureName = ".*((jpg)|(JPG)|(jpeg)|(JPEG)|(png)|(PNG))$";
	
	public static boolean checkUsername(String Username)
	{
		boolean flag = false;
		
		if(Username.length() > 3 && Username.length() < 20)
		{
			flag = true;
		}
		return flag;
	}
	
	public static boolean checkPassword(String Password)
	{
		boolean flag = false;
		Pattern pattern = Pattern.compile(goodPassword);
		Matcher match = pattern.matcher(Password);
		
		if(match.find())
		{
			flag = true;
		}
		return flag;
	}
	
	public static boolean checkName(String Name)
	{
		boolean flag = false;
		
		if(Name.length() < 21 && Name.length() > 1)
		{
			flag = true;
		}
		return flag;
	}
	
	public static boolean checkPhone(String PhoneNumber)
	{
		boolean flag = false;
		Pattern pattern = Pattern.compile(Goodnumber);
		Matcher match = pattern.matcher(PhoneNumber);
		
		if(match.find())
		{
			flag = true;
		}
		return flag;
	}
	
	public static boolean checkEmail(String Email)
	{
		boolean flag = false;
		Pattern pattern = Pattern.compile(goodEmail);
		Matcher match = pattern.matcher(Email);
		
		if(match.find())
		{
			flag = true;
		}
		return flag;
	}
	
	public static boolean checkPicture(String PictureName, byte[] Picture)
	{
		boolean flag = false;

		if(checkPictureType(PictureName) && checkPictureSize(Picture))
		{
			flag = true;
		}
		return flag;
	}
	
	public static boolean checkPictureType(String PictureName)
	{
		boolean flag = false;
		String pictureType = PictureName.substring(PictureName.indexOf("."));		
		Pattern pattern = Pattern.compile(goodPictureName);
		Matcher match = pattern.matcher(pictureType);

		if(match.find())
		{
			flag = true;
		}		
		return flag;
	}
	
	public static boolean checkPictureSize(byte[] Picture)
	{
		boolean flag = false;
		
		if(Picture.length < maxPictureSize)
		{
			flag = true;
		}
		return flag;
	}
}
