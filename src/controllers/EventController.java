package controllers;

import javax.servlet.http.HttpServlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import containers.Event;
import model.EventHandle;
import model.Logger;
import model.RetriveInformation;


public class EventController extends HttpServlet{

	private static final long serialVersionUID = 1L;

	private boolean validateEvent(HttpServletRequest request)
	{
		String check;
		check=request.getParameter("eventTitle");
		if(check!=null)
		{
			check.trim();
			if(check.equals(""))
				return false;
		}
		check=request.getParameter("dateDay");
		if(check!=null)
		{
			try{
				if(!(Integer.parseInt(check)>0))
				{
					return false;
				}
			}
			catch(Exception e)
			{
				return false;
			}
		}
		check=request.getParameter("dateMonth");
		if(check!=null)
		{
			try{
				if(!((Integer.parseInt(check)>0)&&(Integer.parseInt(check)<13)))
				{
					return false;
				}
			}
			catch(Exception e)
			{
				return false;
			}
		}
		check=request.getParameter("dateYear");
		if(check!=null)
		{

			try{
				if(!(Integer.parseInt(check)>(Calendar.getInstance().get(Calendar.YEAR) - 1900)))
				{
					return false;
				}
			}
			catch(Exception e)
			{
				return false;
			}
		}
		check=request.getParameter("timeHour");
		if(check!=null)
		{
			try{
				if(!(Integer.parseInt(check)>0 && Integer.parseInt(check)<25))
				{
					return false;
				}
			}
			catch(Exception e)
			{
				return false;
			}
		}
		check=request.getParameter("timeMinutes");
		if(check!=null)
		{
			try{
				if(!(Integer.parseInt(check)>-1 && Integer.parseInt(check)<60))
				{
					return false;
				}
			}
			catch(Exception e)
			{
				return false;
			}
		}
		check=request.getParameter("eventDescription");
		if(check!=null)
		{
			check.trim();
			if(check.equals(""))
				return false;
		}
		check=request.getParameter("participants");
		if(check!=null)
		{
			try{
				if(!(Integer.parseInt(check)>0))
				{
					return false;
				}
			}
			catch(Exception e)
			{
				return false;
			}
		}
		check=request.getParameter("location");
		if(check!=null)
		{
			check.trim();
			if(check.equals(""))
				return false;
		}
		return true;
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{

		HttpSession session = request.getSession();
		String referrer = request.getHeader("referer");
		Subject currentUser = SecurityUtils.getSubject();
		referrer = referrer.substring(referrer.lastIndexOf("/"));
		referrer = java.net.URLDecoder.decode(referrer,"UTF-8");
		String eventTitle = request.getParameter("eventTitle");
		if(referrer!=null && validateEvent(request))
		{
			if((referrer.equals("/createEvent.jsp")&&(currentUser.isPermitted("create_event")) )
					||
					((referrer.equals("/eventList?eventTitle=" + eventTitle))&&(currentUser.isPermitted("edit_event"))) )
			{
				;
				String eventName,eventDescription;
				eventName =  request.getParameter("eventTitle");
				String day =  request.getParameter("dateDay");
				String month =  request.getParameter("dateMonth");
				String year =  request.getParameter("dateYear");
				String hours =  request.getParameter("timeHour");
				String minutes =  request.getParameter("timeMinutes");
				eventDescription =  request.getParameter("eventDescription");
				Integer participants = Integer.parseInt( request.getParameter("participants"));
				String eventLocation = request.getParameter("location");

				Date date;
				java.sql.Timestamp sqlDate = null;
				date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try {
					date = dateFormat.parse(year+"-"+month+"-"+day +" " + hours+":"+minutes);
					sqlDate = new java.sql.Timestamp(date.getTime());
				} catch (ParseException e) {
					Logger.getInstance().write(e.getMessage(), Level.SEVERE);
				}

				EventHandle newEvent = new EventHandle();
				String result;

				if(referrer.substring(referrer.lastIndexOf("/")).equals("/createEvent.jsp"))
				{
					/*
					 * 
					 * in case we add new event
					 * 
					 */
					result = newEvent.eventAdd(eventName, eventDescription, sqlDate, participants,(String) session.getAttribute("username"),eventLocation);
					if(result.equals("success"))
					{
						Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" has created a new Event", Level.INFO);
					}
					else
					{
						Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" has tried to create an event and failed doing so", Level.INFO);
					}
				}
				else
				{
					/*
					 * 
					 * in case we want to edit an event
					 * 
					 */
					result = newEvent.editEvent(eventName, eventDescription, sqlDate, participants,(String) session.getAttribute("username"),eventLocation, (String)session.getAttribute("eventTitle"));

					if(result.equals("success"))
					{
						Logger.getInstance().write((String)SecurityUtils.getSubject().getPreviousPrincipals().getPrimaryPrincipal()+" has updated an Event titled "+eventName, Level.INFO);
					}
					else
					{
						Logger.getInstance().write((String)SecurityUtils.getSubject().getPreviousPrincipals().getPrimaryPrincipal()+" has tried to update the event titled "+eventName+" and faied doing so", Level.INFO);
					}
				}

				RequestDispatcher rd = null;

				if(result.equals("success"))
				{

					response.sendRedirect(request.getContextPath() + "/eventList");

				}
				else
				{
					request.setAttribute("message", result);
					rd = request.getRequestDispatcher("/createEvent.jsp");
					rd.forward(request, response);
				}


			}
			else
			{
				/*
				 * 
				 * in case update or create failed, return to events list.
				 * 
				 */
				RequestDispatcher rd = null;
				rd = request.getRequestDispatcher("/eventsList.jsp");
				rd.forward(request, response);				
			}	
		}
		else
		{
			request.setAttribute("ErrorShow", "visibility:visible");
			request.setAttribute("message", "Wrong event Parameters");
			response.sendRedirect(request.getContextPath() + "/homeController");
		}

	} 

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		/*
		 * 
		 * creating a list of events
		 * 
		 */
		HttpSession session = request.getSession();
		String referrer = request.getHeader("referer");
		String eventId = request.getParameter("eventId");
		
		
		RequestDispatcher rd = null;

		if(referrer!=null)
		{
			
			if(referrer.contains("/eventList?")&&!request.getRequestURI().equals("/SecureDev/eventList"))
			{
				if(request.getQueryString()!=null)
				{
					String query = request.getQueryString();
					if(query.equals("quit"))
					{
						EventHandle eventHandle = new EventHandle();
						String check = referrer.substring(referrer.lastIndexOf("=")+1);
						if(eventHandle.unparticipateInEvent(SecurityUtils.getSubject(), Integer.parseInt(referrer.substring(referrer.lastIndexOf("=")+1)))==0)
						{
							request.setAttribute("Error", "you failed to unparticipate in the event you want");
							Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" tried to unparticipate themselves from the event with the id: "+check+" and failed", Level.INFO);
							rd = request.getRequestDispatcher("/homeController");
							rd.forward(request, response);
							
						}
						else
						{
							Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" unparticipated themselves from the event with the id: "+check, Level.INFO);
							response.sendRedirect(request.getContextPath() + "/eventList");
						}
					}
				}
				else
				{
					
					referrer = referrer.substring(referrer.lastIndexOf("/"));
					Integer eventId2 = Integer.parseInt(referrer.substring(referrer.indexOf("=")+1));
					referrer = referrer.substring(0,referrer.lastIndexOf("?"));
					EventHandle eventHandle = new EventHandle();
					eventHandle.participateInEvent(SecurityUtils.getSubject(), eventId2);
					Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" decided to participate in the event with the id: "+eventId2, Level.INFO);
					response.sendRedirect(request.getContextPath() + "/eventList");

					//participate
				}
				
				
			}
			else
			{
				
				if(request.getParameter("eventId") != null && !referrer.contains("Deleation"))
				{
					if(SecurityUtils.getSubject().isAuthenticated())
					{
						boolean participating = RetriveInformation.isParticipating(RetriveInformation.RetriveId((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()), Integer.parseInt(eventId));
						request.setAttribute("participating", participating);
					}
					session.setAttribute("eventId", eventId);
					Event eventDetails = RetriveInformation.RetriveEvent(eventId);
					request.setAttribute("event", eventDetails);
					
					rd = request.getRequestDispatcher("/eventDetails.jsp");
					
					
				}
				else
				{
					if(request.getQueryString()!=null && !referrer.contains("Deleation"))
					{
						String query = request.getQueryString();
						if(query.equals("quit"))
						{
							EventHandle eventHandle = new EventHandle();
							if(eventHandle.unparticipateInEvent(SecurityUtils.getSubject(), Integer.parseInt(query))==0)
							{
								
								request.setAttribute("Error", "you failed to unparticipate in the event you want");
								rd = request.getRequestDispatcher("homeController");
							}
						}
					}
					else
					{
						List<Event> upcomingEvents = RetriveInformation.loadEvents(RetriveInformation.upcomingEvents());
						request.setAttribute("list", upcomingEvents);
	
						List<Event> passedEvents = RetriveInformation.loadEvents(RetriveInformation.oldEvents());
						request.setAttribute("oldList", passedEvents);
	
						rd = request.getRequestDispatcher("/eventsList.jsp");
					}
				}
				rd.forward(request, response);
			}
		}
	}

}
