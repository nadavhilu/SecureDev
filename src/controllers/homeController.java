package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import containers.Event;
import containers.News;
import model.RetriveInformation;

public class homeController extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
    
    public homeController() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException
	{
		response.setContentType("text/html");

		List<Event> eventsList = RetriveInformation.RetriveHomeEvents();
		request.setAttribute("events",eventsList);
		List<News> newsList = RetriveInformation.RetriveHomeNews();
		request.setAttribute("news",newsList);

		//Disptching request

		RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
		if (dispatcher != null)
		{
			dispatcher.forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{		
		boolean searchNews,searchEvents;
		String searchContent = request.getParameter("myInput");
		List<News> news = new ArrayList<>();
		List<Event> events = new ArrayList<>();
		if(request.getParameter("news")!=null)
			searchNews = true;
		else
			searchNews = false;
		if(request.getParameter("events")!=null)
			searchEvents = true;
		else
			searchEvents = false;
		if(searchNews == true)
		{
			news = RetriveInformation.searchNews(searchContent);
		}
		if(searchEvents == true)
		{
			events = RetriveInformation.searchEvents(searchContent);
		}
		
		
		request.setAttribute("events",events);
	
		request.setAttribute("news",news);

		//Disptching request

		RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
		if (dispatcher != null)
		{
			dispatcher.forward(request, response);
		}
			
		
	}	
}
