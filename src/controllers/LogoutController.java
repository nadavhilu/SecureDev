package controllers;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;

import model.Logger;

public class LogoutController extends HttpServlet
{
	private static final long serialVersionUID = 1L;  
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException 
    {

        HttpSession session=request.getSession(); 
        Logger.getInstance().write((String)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()+" got out of the system", Level.INFO);
        session.setAttribute("username", null);
        session.invalidate(); 
        
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException 
    {
    	this.doPost(request, response);
    }

}
