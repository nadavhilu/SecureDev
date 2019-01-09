package controllers;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import model.Logger;
import model.Registration;
import model.ServerSideValidation;


@WebServlet("/upload")
public class RegistrationController extends HttpServlet{

	private static final long serialVersionUID = 1L;

	public RegistrationController()
	{
		super();
	}
	private static final String UPLOAD_DIRECTORY= "WebContent/resource/upload";
	private static final String PICTURE = "resource/upload/";
	private static final int THRESHOLD_SIZE = 1024*1024*2; //2MB
	private static final int MAX_FILE_SIZE = 1024*1024*2; //2MB
	private static final int MAX_REQUEST_SIZE = 1024*1024*4; //4MB
	private final static String goodPictureName = ".*((jpg)|(JPG)|(jpeg)|(JPEG)|(png)|(PNG))$";
	
	private boolean checkPicture(String pictureType)
	{
		Pattern pattern = Pattern.compile(goodPictureName);
		Matcher match = pattern.matcher(pictureType);
		if(match.find())
			return true;
		else
			return false;
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		
		//checks if the request actually contains upload file
		String username = null,password = null,name = null,lastName = null,phoneNumber = null,email = null,fileName = null;
		String result = "fail",filePath = null;
		File storeFile = null;
		byte[] salt = null;
		
		if(ServletFileUpload.isMultipartContent(request))
		{
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(THRESHOLD_SIZE);
			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(MAX_FILE_SIZE);
			upload.setSizeMax(MAX_REQUEST_SIZE);

			// constructs the directory path to store upload file
			//String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;
			String uploadPath = System.getProperty("user.dir") + File.separator + UPLOAD_DIRECTORY;
			// creates the directory if it does not exist
			File uploadDir = new File(uploadPath);
			if(!uploadDir.exists())
			{
				uploadDir.mkdir();
				
			}
			File[] filesList = uploadDir.listFiles();
			List<String> listOfFiles = new ArrayList<String>();
			for(File file : filesList)
			{
				if(file.isFile())
					listOfFiles.add(file.getName());
			}
			


			try {
				List<?> formItems = upload.parseRequest(new ServletRequestContext(request));
				@SuppressWarnings("rawtypes")
				Iterator iter = formItems.iterator();

				// iterates over form's fields
				while(iter.hasNext())
				{
					DiskFileItem item = (DiskFileItem)iter.next();
					// processes only fields that are not form fields
					if(!item.isFormField())
					{
						if(item.getName()!="")
						{
						
						
							if(checkPicture(item.getName().substring(item.getName().lastIndexOf("."))))
							{
								
								fileName = new File(item.getName()).getName();
								if(ServerSideValidation.checkPictureType(fileName))
								{
									
									String prefix = fileName.substring(0, fileName.lastIndexOf("."));
									String suffix = fileName.substring(fileName.lastIndexOf("."));
									if(listOfFiles.contains(fileName))
									{
										storeFile = File.createTempFile(prefix, suffix, uploadDir);
										filePath = storeFile.getPath();
									}
									else
									{
										filePath = uploadDir.getPath() + File.separator + fileName;
										storeFile = new File(filePath);
									} 
									// save the file on disk
									item.write(storeFile);
								}
							}
						}
						
						
					}
					else
					{
						String fieldName = item.getFieldName();
						String value = item.getString();

						switch(fieldName)
						{
						case "Username":
							username = value;
							break;
						case "Password":
							password = value;
							SecureRandom csprng = new SecureRandom();
							salt = new byte[32];
							csprng.nextBytes(salt);
							Sha256Hash hashedPassword = new Sha256Hash(password,salt);
							password=hashedPassword.toString();
							break;
						case "Name":
							name = value;
							break;
						case "Lastname":
							lastName = value;
							break;
						case "Email":
							email = value;
							break;
						case "Phonenumber":
							phoneNumber = value;
							break;
						}
					}
				}
			} catch (org.apache.tomcat.util.http.fileupload.FileUploadException e) {
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
			} catch (Exception e) {
				Logger.getInstance().write(e.getMessage(), Level.SEVERE);
			}
					
		}

		
		RequestDispatcher rd = null;

		Registration registration = new Registration();

		
		try {
			result = registration.register(username,password,salt,name,lastName,email,phoneNumber,filePath);
		} catch (IllegalStateException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		} catch (FileUploadException e) {
			Logger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
	
		if (result.equals("success")) 
		{
			Logger.getInstance().write(username+" has registered into the system", Level.INFO);
			response.sendRedirect(request.getContextPath() + "/login.jsp");
		} 
		else
		{
			storeFile.delete();
			request.getSession().setAttribute("registrationFailed", "visibility:visible");
			request.getSession().setAttribute("message", result);
			rd = request.getRequestDispatcher("/Registration.jsp");
			rd.forward(request, response);
		}


	}


}
