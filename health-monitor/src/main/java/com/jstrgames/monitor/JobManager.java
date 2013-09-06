package com.jstrgames.monitor;

import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.svc.Service;

public class JobManager {
	private final static Logger LOG = LoggerFactory.getLogger(JobManager.class);
			
	private final List<Service> serviceList;
	private final Scheduler scheduler;
	
	public JobManager(Scheduler scheduler, final List<Service> list) throws SchedulerException {
		this.serviceList = list;		
		this.scheduler = scheduler;		
	}
	
	public void start() {
		for(Service service : this.serviceList) {
			CronTrigger trigger = newTrigger()
				    .withSchedule(cronSchedule(service.getSchedule()))
				    .build();
			try {
				JobDataMap jobDataMap = new JobDataMap();
				jobDataMap.put("service", service);
				JobDetail job = newJob(ScheduledJob.class)
								   .withDescription(service.getServiceName())
								   .setJobData(jobDataMap)
								   .build();				
				
				this.scheduler.scheduleJob(job, trigger);
			} catch (SchedulerException e) {
				LOG.error("Failed to schedule job", e);
			} 
		}
		
		try {
			this.scheduler.start();
		} catch (SchedulerException e) {
			LOG.error("Failed to start schedule", e);
		}
	}
	
	public List<Service> getServices() {
		return this.serviceList;
	}

}
