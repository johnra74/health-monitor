package com.jstrgames.monitor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jstrgames.monitor.svc.Service;
import com.jstrgames.monitor.svc.Service.Status;
import com.jstrgames.monitor.svc.impl.TestService;


public class TestScheduledJob {

	@Test
	public void testExecuteFailed() {
		JobExecutionContext context = mock(JobExecutionContext.class);
		JobDataMap map = mock(JobDataMap.class);
		Service service = new TestService(false);
		
		when(context.getMergedJobDataMap()).thenReturn(map);
		when(map.get("service")).thenReturn(service);
		
		try {
			ScheduledJob job = new ScheduledJob();
			job.execute(context);
			assertEquals(Status.FAIL, service.getStatus());
		} catch (JobExecutionException e) {
			fail("failed to execute job");
		}
		
	}
	
	@Test
	public void testExecutePass() {
		JobExecutionContext context = mock(JobExecutionContext.class);
		JobDataMap map = mock(JobDataMap.class);
		Service service = new TestService(true);
		
		when(context.getMergedJobDataMap()).thenReturn(map);
		when(map.get("service")).thenReturn(service);
		
		try {
			ScheduledJob job = new ScheduledJob();
			job.execute(context);
			assertEquals(Status.PASS, service.getStatus());
		} catch (JobExecutionException e) {
			fail("failed to execute job");
		}
	}

}
