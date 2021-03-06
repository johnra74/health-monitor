package com.jstrgames.monitor.net;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.io.NIOOutputStream;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import com.jstrgames.monitor.cfg.ConfigLoader;

/**
 * this handler class is called by the embedded web server (grizzly)
 * when requests are made to retrieve "/services.json"
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 *
 */
public class HealthConfigHandler extends HttpHandler {
	
	private ConfigLoader cfgLoader;
	
	public HealthConfigHandler(ConfigLoader cfgLoader) {
		this.cfgLoader = cfgLoader;
	}

	@Override
	public void service(Request request, Response response) throws Exception {
		if(Method.GET.equals(request.getMethod())) {
			doGet(request, response);
		} else {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
		}
	}
	
	/**
	 * method to handle GET requests
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void doGet(Request request, Response response) throws Exception {
		NIOOutputStream out = response.createOutputStream();
		response.setStatus(HttpStatus.OK_200);
		out.write(cfgLoader.getServiceConfig().getBytes());
		out.flush();
		out.close();
	}
	
}
