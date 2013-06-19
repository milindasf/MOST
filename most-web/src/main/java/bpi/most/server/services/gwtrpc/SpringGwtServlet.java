package bpi.most.server.services.gwtrpc;

import bpi.most.service.api.AuthenticationService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * Spring Dependency Injection Support for GWT RPC Servlets.
 * Source taken from: http://blog.maxmatveev.com/2011/02/simple-spring-bean-autowiring-in-gwt.html
 *
 * @author Jakob Korherr
 */
public class SpringGwtServlet extends RemoteServiceServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        WebApplicationContextUtils.
                getRequiredWebApplicationContext(config.getServletContext()).
                getAutowireCapableBeanFactory().
                autowireBean(this);
   /*

        WebApplicationContextUtils.
                getRequiredWebApplicationContext(config.getServletContext()).
                getAutowireCapableBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
*/
    }

}
