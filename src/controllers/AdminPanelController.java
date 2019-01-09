package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import containers.UserRolesInfo;
import model.Logger;
import model.RetriveInformation;
import shiro.SQLite.Roles.UserDaoImpl;

public class AdminPanelController extends HttpServlet{

	public AdminPanelController() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser.hasRole(UserDaoImpl.ADMIN_ROLE))
		{
			Integer size = Integer.parseInt(request.getParameter("size"));
	
			List<UserRolesInfo> users = new ArrayList<UserRolesInfo>();
			String username;
			boolean isAdmin,isModerator,isUser;
			for(Integer index = 0; index < size ; index++)
			{
				username = request.getParameter("username"+index);
				if(request.getParameter("user"+index)!=null)
				{
					if(request.getParameter("user"+index).equals("on"))
						isUser = true;
					else
						isUser = false;
				}
				else
					isUser = false;
				if(request.getParameter("moderator"+index)!=null)
				{
					if(request.getParameter("moderator"+index).equals("on"))
						isModerator = true;
					else
						isModerator = false;
				}
				else
					isModerator = false;
				if(request.getParameter("admin"+index)!=null)
				{
					if(request.getParameter("admin"+index).equals("on"))
						isAdmin = true;
					else
						isAdmin = false;
				}
				else
					isAdmin = false;
				Integer id = Integer.parseInt(request.getParameter("id"+index));
				users.add(new UserRolesInfo(id,username,isAdmin,isModerator,isUser));
			
			}
			for(Integer index = 0; index < users.size(); index++)
			{
				UserDaoImpl userRolesEdit = new UserDaoImpl();
				Set<String> roles = userRolesEdit.findRoles(users.get(index).getUsername());
				if(users.get(index).isAdmin())
				{
					if(!roles.contains(UserDaoImpl.ADMIN_ROLE))
					{
						userRolesEdit.correlationRoles(userRolesEdit.findByUsername(users.get(index).getUsername()).getId(), UserDaoImpl.ADMIN_ROLE_ID);
						Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" changed"+ users.get(index).getUsername()+"'s role to admin", Level.INFO);
					}
					if(!roles.contains(UserDaoImpl.MODERATOR_ROLE))
					{
						userRolesEdit.correlationRoles(userRolesEdit.findByUsername(users.get(index).getUsername()).getId(), UserDaoImpl.MODERATOR_ROLE_ID);
						Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" changed"+ users.get(index).getUsername()+"'s role to moderator", Level.INFO);
					}
					if(!roles.contains(UserDaoImpl.USER_ROLE))
					{
						userRolesEdit.correlationRoles(userRolesEdit.findByUsername(users.get(index).getUsername()).getId(), UserDaoImpl.USER_ROLE_ID);
						Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" changed"+ users.get(index).getUsername()+"'s role to user", Level.INFO);
					}
				}
				else
				{
					if(roles.contains(UserDaoImpl.ADMIN_ROLE))
					{
						userRolesEdit.uncorrelationRoles(userRolesEdit.findByUsername(users.get(index).getUsername()).getId(), UserDaoImpl.ADMIN_ROLE_ID);
						Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" downgraded"+ users.get(index).getUsername()+"'s from being an admin", Level.INFO);
					}
					
					if(users.get(index).isModerator())
					{
						if(!roles.contains(UserDaoImpl.MODERATOR_ROLE))
						{
							userRolesEdit.correlationRoles(userRolesEdit.findByUsername(users.get(index).getUsername()).getId(), UserDaoImpl.MODERATOR_ROLE_ID);
							Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" changed"+ users.get(index).getUsername()+"'s role to moderator", Level.INFO);
						}
						if(!roles.contains(UserDaoImpl.USER_ROLE))
						{
							userRolesEdit.correlationRoles(userRolesEdit.findByUsername(users.get(index).getUsername()).getId(), UserDaoImpl.USER_ROLE_ID);
							Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" changed"+ users.get(index).getUsername()+"'s role to user", Level.INFO);
						}
					}
					else
					{
						if(roles.contains(UserDaoImpl.MODERATOR_ROLE))
						{
							userRolesEdit.uncorrelationRoles(userRolesEdit.findByUsername(users.get(index).getUsername()).getId(), UserDaoImpl.MODERATOR_ROLE_ID);
							Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" downgraded"+ users.get(index).getUsername()+"'s role from moderator", Level.INFO);
						}
						if(users.get(index).isUser())
						{
							if(!roles.contains(UserDaoImpl.USER_ROLE))
							{
								userRolesEdit.correlationRoles(userRolesEdit.findByUsername(users.get(index).getUsername()).getId(), UserDaoImpl.USER_ROLE_ID);
								Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" changed"+ users.get(index).getUsername()+"'s role to user", Level.INFO);
							}
						}
						else
						{
							if(roles.contains(UserDaoImpl.USER_ROLE))
							{
								userRolesEdit.uncorrelationRoles(userRolesEdit.findByUsername(users.get(index).getUsername()).getId(), UserDaoImpl.USER_ROLE_ID);
								Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" downgraded"+ users.get(index).getUsername()+"'s role from user", Level.INFO);
							}
						}
					}
				}
				
				
				
				
			}
		}
		HttpSession session = request.getSession();
		session.removeAttribute("users");
	}

	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		Subject currentUser = SecurityUtils.getSubject();
		HttpSession session = request.getSession();
		session.removeAttribute("users");
		if(currentUser.hasRole(UserDaoImpl.ADMIN_ROLE))
		{
			List<UserRolesInfo> users = RetriveInformation.RetriveUserRolesInfo();
		
			session.setAttribute("users", users);
			RequestDispatcher rd = request.getRequestDispatcher("adminPanel.jsp");
			rd.forward(request, response);
			
		}
		else
		{
			request.setAttribute("ErrorShow", "visibility:visible");
			request.setAttribute("message", "you are not premited to edit the news you wanted to edit, you can ask a moderator or the creator of the news to edit them");
			response.sendRedirect(request.getContextPath() + "/homeController");
		}
			
				
	}
	
}
