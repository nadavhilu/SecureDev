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
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;

import containers.ForumData;
import model.Logger;
import model.threadPosts;

/**
 * Servlet implementation class ForumLoader
 */
@WebServlet("/ThreadController")
public class ThreadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ThreadController() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    threadPosts post = new threadPosts();

	/**
	 * doGet invoked when a thread is selected, it then takes the thread title for the request
	 * and sends it to threadPosts.loadPosts(threadTitle) to get all the posts related to the thread.
	 * The posts are saved in a list which is then sent to ForumThread.jsp to create the thread page.
	 * 
	 * The thread title is assigned to the session for future use if the user wants to reply.
	 * If a different thread is chosen, the assigned thread title changes accordingly.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		String threadTitle = request.getParameter("threadTitle");
		session.setAttribute("threadTitle", threadTitle);
		response.setContentType("text/html");

		List<ForumData> postsList = post.loadPosts(threadTitle);

		request.setAttribute("posts",postsList);

		//Disptching request

		RequestDispatcher dispatcher = request.getRequestDispatcher("/ForumThread.jsp");
		if (dispatcher != null)
		{
			dispatcher.forward(request, response);
		}
	}

	/**
	 * doPost is called when a user submits a reply to the currently viewed thread.
	 * It gets the thread title and the username from the session and sends them along the post 
	 * and current time to threadPosts.newPost method.
	 * 
	 * After saving the new post in the database, it then redirects to ThreadController again along 
	 * the current thread title to invoke doGet() and reload the current thread and let the user see
	 * his post added.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String threadTitle = (String)request.getSession().getAttribute("threadTitle");
		Integer id = Integer.parseInt(request.getParameter("id"));
		String postContent = null;
		if(id == 0)
		{
			if(request.getParameter("PostBody")!=null)
			{
				String creatingUser = (String)request.getSession().getAttribute("username");
				Timestamp creationTime =  new Timestamp(System.currentTimeMillis());
				postContent = request.getParameter("PostBody");

				post.newPost(threadTitle, creatingUser, creationTime, postContent);
				Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" have created a new post at the thread with the title: "+threadTitle, Level.INFO);
			}
		}
		else
		{
			postContent = request.getParameter("PostBody");

			post.editPost(id, postContent);
			Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" edited a post at the thread with the title: "+threadTitle, Level.INFO);
		}
		String postRedirect = "/ThreadController?threadTitle=" + threadTitle;
		response.sendRedirect(request.getContextPath() + postRedirect);
	}

}
