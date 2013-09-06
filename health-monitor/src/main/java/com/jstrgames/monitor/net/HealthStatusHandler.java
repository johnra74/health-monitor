package com.jstrgames.monitor.net;

import java.util.List;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.io.NIOOutputStream;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import com.jstrgames.monitor.JobManager;
import com.jstrgames.monitor.cfg.ConfigLoader;
import com.jstrgames.monitor.svc.Service;
import com.jstrgames.monitor.tpl.WebTemplate;

public class HealthStatusHandler extends HttpHandler {
	
	private JobManager jobMgr;
	private ConfigLoader cfgLoader;
	
	public HealthStatusHandler(ConfigLoader cfgLoader, JobManager jobMgr) {
		this.cfgLoader = cfgLoader;
		this.jobMgr = jobMgr;
	}

	@Override
	public void service(Request request, Response response) throws Exception {
		if(Method.GET.equals(request.getMethod())) {
			doGet(request, response);
		} else {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
		}
	}
	
	public void doGet(Request request, Response response) throws Exception {
		NIOOutputStream out = response.createOutputStream();
		List<Service> svcList = jobMgr.getServices();
		WebTemplate template = new WebTemplate(this.cfgLoader.getTemplateDirectory(), svcList);
		response.setStatus(HttpStatus.OK_200);
		out.write(template.getHtml().getBytes());
		out.flush();
		out.close();
	}
	
}
