package bpi.most.server.services.opcua;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * ServiceLocator which delegates to configured Spring beans.
 */
public class ServiceLocator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(ServiceLocator.class);
    
	/**
	 * finding a service is delegated to Spring's {@link ApplicationContext}
	 */
	private static ApplicationContext appContext;
	
	private static ServletConfig config;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		//initialize springs ApplicationContext
		appContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		if (appContext == null){
			throw new ServletException("Could not start ServiceLocator because ApplicationContext is null!!");
		}
		
		this.config = config;
		String endpoint = config.getInitParameter("opc/endpoint");
		System.out.println("endpoint should be " + endpoint);
	}

	public static <T> T findService (Class<T> serviceClass){
		T service = null;
		
		if (appContext != null){
			service = appContext.getBean(serviceClass);
		}else{
			LOG.warn("Did not find any bean from type " + serviceClass.getName());
		}
		
		return service;
	}
}
