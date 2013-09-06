package com.jstrgames.monitor;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.svc.Service;
import com.jstrgames.monitor.svc.Service.Status;

public class ScheduledJob implements Job {
	private final static Logger LOG = LoggerFactory.getLogger(ScheduledJob.class);
	
	@Override
	public void execute(JobExecutionContext context) 
			throws JobExecutionException {
		LOG.debug("job starting...");
		
		Service service = (Service) context.getMergedJobDataMap().get("service");
		if(service.checkService()) {
			service.setStatus(Status.PASS);
		} else {
			service.setStatus(Status.FAIL);
		}
		LOG.debug("job completed...");
	}

}
