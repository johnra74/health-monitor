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

/**
 * This manager class will initiate quartz-scheduler for all
 * jobs specified in the constructor 
 * 
 * @author Johnathan Ra
 * @company JSTR Games, LLC
 */
public class JobManager {
	private final static Logger LOG = LoggerFactory.getLogger(JobManager.class);
			
	private final List<Service> serviceList;
	private final Scheduler scheduler;
	
	public JobManager(Scheduler scheduler, final List<Service> list) throws SchedulerException {
		this.serviceList = list;		
		this.scheduler = scheduler;		
		this.setupServices();
	}
	
	/**
	 * method will initiate scheduler 
	 */
	public void start() {	
		try {
			this.scheduler.start();			
		} catch (SchedulerException e) {
			LOG.error("Failed to start schedule", e);
		}
	}
	
	/**
	 * method will retrieve all scheduled services 
	 * 
	 * @return
	 */
	public List<Service> getServices() {
		return this.serviceList;
	}
	
	/**
	 * helper method to setup all services based on specified
	 * cron schedule
	 */
	private void setupServices() {
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
	}

}
