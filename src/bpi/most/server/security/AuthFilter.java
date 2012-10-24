package bpi.most.server.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import bpi.most.server.services.User;

/**
 * Servlet Filter implementation class AuthFilter
 * 
 * 
 * 
 */
public class AuthFilter implements Filter {

	 private boolean mockAuth = false;
	
    /**
     * Default constructor. 
     */
    public AuthFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		User mostUser = null;
		
		if (mockAuth){
			System.out.println("mocking user authentication!");
			mostUser = new User("mostsoc");
		}else{
			if (((HttpServletRequest)request).getUserPrincipal() instanceof MostUserPrincipal){
			MostUserPrincipal userPrincipal = (MostUserPrincipal) ((HttpServletRequest)request).getUserPrincipal();
				mostUser = userPrincipal.getUser();
			}
		}
		
		request.setAttribute("user", mostUser);

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		mockAuth = Boolean.parseBoolean(fConfig.getInitParameter("mockauth"));
	}

}
