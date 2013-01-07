package bpi.most.server.security;

import bpi.most.server.services.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Servlet Filter implementation class AuthFilter
 * 
 * 
 * 
 */
public class AuthFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

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
			LOG.info("mocking user authentication!");
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
