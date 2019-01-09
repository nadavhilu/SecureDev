package controllers;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class VerifyCsrfTokenFilter implements Filter 
{
	@Override
	public void init(FilterConfig config) throws ServletException {
		// If you have any <init-param> in web.xml, then you could get them
		// here by config.getInitParameter("name") and assign it as field.
	}

	private static final int THRESHOLD_SIZE = 1024*1024*2; //2MB
	private static final int MAX_FILE_SIZE = 1024*1024*2; //2MB
	private static final int MAX_REQUEST_SIZE = 1024*1024*4; //4MB

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException 
	{
		HttpServletRequest innerRequest = (HttpServletRequest) request;
		HttpServletResponse innerResponse = (HttpServletResponse) response;
		/*
		 * checks if the csrf token is in the request and it matches the token generated  
		 * for this users session.
		 */
		if(innerRequest.getMethod().toLowerCase().equals("get")==true)
		{
			chain.doFilter(request, response);
		}
		else
		{
			
			if(innerRequest.getSession()!=null)
			{

				String requestToken=innerRequest.getParameter("anti-csrf");
				String SessionToken=null;
				if(innerRequest.getSession().getAttribute("csrf")!=null)
					SessionToken=innerRequest.getSession().getAttribute("csrf").toString();
				else
				{
					innerRequest.getSession().setAttribute("csrfError", "failed");
					innerResponse.sendRedirect(innerRequest.getContextPath() + "/login.jsp");
				}
				if (requestToken!=null && requestToken.equals(SessionToken)) {
					chain.doFilter(request, response); 
				} else {
					innerRequest.getSession().setAttribute("csrfError", "failed");
					innerResponse.sendRedirect(innerRequest.getContextPath() + "/login.jsp");
				}

			}
		}
	}


	@Override
	public void destroy() {
		// If you have assigned any expensive resources as field of
		// this Filter class, then you could clean/close them here.
	}
	
	public static boolean checkCsrfToken(String requestToken, HttpSession session)
	{
		String sessionToken = null;
		if(session.getAttribute("csrf")!=null)
		{
			sessionToken = session.getAttribute("csrf").toString();
		}
		else
		{
			session.setAttribute("csrfError", "failed");
			return false;
		}
		if(requestToken!=null && requestToken.equals(sessionToken))
			return true;
		else
		{
			session.setAttribute("csrfError", "failed");
			return false;
		}
		
	}

}