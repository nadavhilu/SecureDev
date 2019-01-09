package controllers;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import model.AddNews;
import model.EditProfile;
import model.EventHandle;
import model.Logger;
import model.RetriveInformation;
import model.threadPosts;
import shiro.SQLite.Roles.UserDaoImpl;

public class DeleationController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	public DeleationController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException
	{
		Integer success = 0;
		String DispacherLink=null;
		String idOfItem;

		
		String query = null;
		if(request.getQueryString()!=null)
		{
			query = request.getQueryString();
			idOfItem = query.substring(query.lastIndexOf("=")+1);
		}
		else
		{
		
			idOfItem = request.getParameter("id");
		}
		String logMessage = "have deleted the ";
		Subject currentUser=SecurityUtils.getSubject();
		String referrer = request.getHeader("referer");
		if(referrer.lastIndexOf("?")!=-1)
		{
			referrer = referrer.substring(0, referrer.indexOf("?"));
		}
		referrer = referrer.substring(referrer.lastIndexOf("/"));
		
		if(referrer!=null || idOfItem!=null)
		{
			
			switch (referrer)
			{
			case "/ThreadController":
			{
//				DispacherLink="/ThreadController?threadTitle="+RetriveInformation.getTitle(idOfItem,"ThreadPosts");
				DispacherLink="/ForumController";
				logMessage = logMessage+"post with the id: ";
				String CreatingUser=RetriveInformation.getUser(idOfItem,"ThreadPosts");
				if(currentUser.hasRole(UserDaoImpl.ADMIN_ROLE) || currentUser.getPrincipals().getPrimaryPrincipal().equals(CreatingUser))
					success=threadPosts.deletePost(idOfItem);
				break;
			}
			case "/News":
			{
				DispacherLink="/News";
				String CreatingUser=RetriveInformation.getUser(idOfItem,"tblNews");
				logMessage = logMessage+"news with the id: ";
				if(currentUser.hasRole(UserDaoImpl.ADMIN_ROLE) || currentUser.getPrincipals().getPrimaryPrincipal().equals(CreatingUser))
					success=AddNews.deleteNews(idOfItem);
				break;
			}
			case "/eventList":
			{
				DispacherLink="/eventList";
				String CreatingUser=RetriveInformation.getUser(idOfItem,"userEvents");
				logMessage = logMessage+"event with the id: ";
				if(currentUser.hasRole(UserDaoImpl.ADMIN_ROLE) || currentUser.getPrincipals().getPrimaryPrincipal().equals(CreatingUser))
					success=EventHandle.deleteEvent(idOfItem);
				break;
			}
			case "/AdminPanel":
			{
				DispacherLink="/AdminPanel";
				String CreatingUser=RetriveInformation.getUser(idOfItem,"tblusers");
				logMessage = logMessage+"user with the id: ";
				if(currentUser.hasRole(UserDaoImpl.ADMIN_ROLE) || currentUser.getPrincipals().getPrimaryPrincipal().equals(CreatingUser))
					success=EditProfile.deleteUser(idOfItem);
				break;
			}
			}
			if(success==0)
			{
				request.setAttribute("Error","delete failed! please try again");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/homeController");
				if (dispatcher != null)
				{
					dispatcher.forward(request, response);
				}			

			}
			else
			{
				Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" "+ logMessage + " " + idOfItem, Level.INFO);

				response.sendRedirect(request.getContextPath() + DispacherLink);
//				RequestDispatcher dispatcher = request.getRequestDispatcher(DispacherLink);
//				if (dispatcher != null)
//				{
//					dispatcher.forward(request, response);
//				}			
			}
		}
			else
			{
				request.setAttribute("ErrorShow", "visibility:visible");
				request.setAttribute("message", "Invalid request");
//				RequestDispatcher dispatcher = request.getRequestDispatcher("/homeController");
//				dispatcher.forward(request, response);
				response.sendRedirect(request.getContextPath() + "/homeController");
										
			}
		}

		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException 
		{		
			doGet(request, response);
		}	
	}
