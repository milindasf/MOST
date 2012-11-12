package bpi.most.server.services.rest;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import bpi.most.server.services.rest.impl.DpResImpl;
import bpi.most.server.services.rest.impl.ZoneResImpl;

/**
 * 
 * @author harald
 *
 */
public class MostRestService extends Application{

	private static final Logger LOG = Logger.getLogger(MostRestService.class);
	
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> perRequest = new HashSet<Class<?>>();
	
	public MostRestService(@Context ServletContext sc) {
		LOG.debug("--> created MostRestService");
		perRequest.add(ZoneResImpl.class);
		perRequest.add(DpResImpl.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return perRequest;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
