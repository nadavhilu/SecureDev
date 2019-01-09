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

public class SameOrigenFilter implements Filter 
{
	@Override
	public void init(FilterConfig config) throws ServletException {
		// If you have any <init-param> in web.xml, then you could get them
		// here by config.getInitParameter("name") and assign it as field.
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException 
	{
		HttpServletRequest innerRequest = (HttpServletRequest) request;
		HttpServletResponse innerResponse = (HttpServletResponse) response;
		/*
		 * checks if the request and response came from our website and no where else  
		 */
		String refererHeader=innerRequest.getHeader("referer");
		if(refererHeader==null)
		{
			innerRequest.getSession().setAttribute("sameOrigenError", "failed");
			innerResponse.sendRedirect(innerRequest.getContextPath() + "/login.jsp");			
		}
		else
		{

			String[] refererParts =refererHeader.split("/");
			if(refererParts[0].equals("http:")&& refererParts[2].equals("localhost:8080"))
			{
				chain.doFilter(request, response);
			}
			else
			{
				innerRequest.getSession().setAttribute("sameOrigenError", "failed");
				innerResponse.sendRedirect(innerRequest.getContextPath() + "/login.jsp");
			}
		}
	}


	@Override
	public void destroy() {
		// If you have assigned any expensive resources as field of
		// this Filter class, then you could clean/close them here.
	}

}