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


public class NoCacheFilter implements Filter 
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
         * Adds the required headers to prevent the browser from caching 
         * any secured page mapped to the filter.
         */
        innerResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        innerResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        innerResponse.setDateHeader("Expires", 0); // Proxies.
        
        HttpSession session = innerRequest.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
        	innerResponse.sendRedirect(innerRequest.getContextPath() + "/login.jsp"); // No logged-in user found, so redirect to login page.
        } else {
            chain.doFilter(request, response); // Logged-in user found, so just continue request.
        }
    }

  
    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }

}