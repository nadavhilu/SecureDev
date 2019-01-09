package controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;

import containers.ForumData;
import model.Logger;
import model.forumThreads;

/**
 * Servlet implementation class ForumController
 */
@WebServlet("/ForumController")
public class ForumController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public ForumController() {
        super();
    }
    /**
     * A forumThreads object which contains interaction methods with the database.
     * See forumThreads class for more information.
     */
	private forumThreads thread = new forumThreads();
	
	
	/**
	 * doGet invoked when the user enters the forum.
	 * It calls forumThreads.loadThreads() method and stores the threads in a list
	 * it then sets the list in the request which is used by Forum.jsp 
	 * to place the threads in a table.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException
	{
		response.setContentType("text/html");

		List<ForumData> threadsList = thread.loadThreads();

		request.setAttribute("threads",threadsList);

		//Disptching request

		RequestDispatcher dispatcher = request.getRequestDispatcher("/Forum.jsp");
		if (dispatcher != null)
		{
			dispatcher.forward(request, response);
		}
	}
	
	/**
	 * doPost invoked when a new thread form is sent by CreateThread.jsp.
	 * It then calls forumThreads.newThread() method which stores the thread in the database.
	 * After storing the new thread, it redirects to newly created thread page
	 * by redirecting to ThreadController with the thread title.
	 */	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{		
		if( request.getParameter("ThreadTitle")!=null && request.getParameter("PostBody")!=null)
		{
			String threadTitle = (String) request.getParameter("ThreadTitle");
			String creatingUser = (String)request.getSession().getAttribute("username");
			Timestamp creationTime =  new Timestamp(System.currentTimeMillis());
			String postContent = request.getParameter("PostBody");

			thread.newThread(threadTitle, creatingUser, creationTime, postContent);	
			Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" have created a new Thread with the title: "+threadTitle, Level.INFO);

			String postRedirect = "/ThreadController?threadTitle=" + threadTitle;
			response.sendRedirect(request.getContextPath() + postRedirect);
		}
		else
		{
			response.sendRedirect(request.getContextPath() + "/ForumController");
		}
	}	
}
