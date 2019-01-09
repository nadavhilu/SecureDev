package controllers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import containers.News;
import model.AddNews;
import model.Logger;
import model.RetriveInformation;


public class NewsController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public NewsController()
	{
		super();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String referrer = request.getHeader("referer");
		HttpSession session = request.getSession();
		Subject currentUser = SecurityUtils.getSubject();
		RequestDispatcher rd = null;
		boolean visible = false;
		if(referrer!=null && request.getParameter("title")!=null && request.getParameter("news")!=null)
		{
			String title, news,addOrEdit = null;
			Integer id = 0;
 			title = (String) request.getParameter("title");
			news = (String) request.getParameter("news");
			if(referrer.substring((referrer.lastIndexOf("/"))).equals("/addNews.jsp"))
			{
				if(currentUser.isPermitted("create_news"))
				{
					session.setAttribute("title", title);
					addOrEdit = "add";
				}
				else
				{
					
					request.setAttribute("message", "you are not premited to create news");
					rd = request.getRequestDispatcher("/homeConroller");
				}
			}
			else
			{
				if(currentUser.isPermitted("edit_news"))
				{
					id =  Integer.parseInt(request.getParameter("id"));
					addOrEdit = "edit";
					if(request.getParameter("visible")!=null)
						visible = true;
				}
				else
				{
					request.setAttribute("message", "you are not premited to edit the news you wanted to edit, you can ask a moderator or the creator of the news to edit them");
					rd = request.getRequestDispatcher("/homeConroller");
				}
			}

			AddNews newNews = new AddNews();
			String result = newNews.newNews(title, news, (String)request.getSession().getAttribute("username"),addOrEdit, (String)session.getAttribute("newsTitle"), id,visible);

			if(result.equals("success"))
			{
				if(addOrEdit.equals("add"))
					Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" added news to the system with the title: "+title, Level.INFO);
				else
					Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" edited the news that was with the title "+(String)session.getAttribute("newsTitle")+ "to the news with the title: "+title , Level.INFO);
				
				this.doGet(request, response);
			}
			else
			{
				if(addOrEdit.equals("add"))
					Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" tried to add news to the system", Level.INFO);
				else
					Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" tried to edit the news with the title: "+title, Level.INFO);
				request.setAttribute("message", result);
				rd = request.getRequestDispatcher("/addNews.jsp");
				rd.forward(request, response);
			}						
		}
		else
		{
			this.doGet(request, response);
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		Subject currentUser = SecurityUtils.getSubject();
		RequestDispatcher rd = null;
		if(request.getParameter("newsId")!=null)
		{
			String newsId = request.getParameter("newsId");
			session.setAttribute("newsId", newsId);
			response.setContentType("text/html");
			RetriveInformation info = new RetriveInformation();
			News news = info.NewsToEdit(newsId);

			request.setAttribute("news", news);
			rd = request.getRequestDispatcher("/editNews.jsp");
			rd.forward(request, response);
		}
		else
		{
			RetriveInformation info = new RetriveInformation();
			List<News>news = info.RetriveNews(SecurityUtils.getSubject());
			request.setAttribute("news", news);

			rd = request.getRequestDispatcher("/news.jsp");
			rd.forward(request, response);
		}
	}
}
