package com.jstrgames.monitor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import com.jstrgames.monitor.svc.Service;

public class TestJobManager {

	@Test
	public void testServiceStatus() {
		Scheduler scheduler = mock(Scheduler.class);
		@SuppressWarnings("unchecked")
		List<Service> list = mock(List.class);
		@SuppressWarnings("unchecked")
		Iterator<Service> iter = mock(Iterator.class);
		Service service = mock(Service.class);
		
		when(list.size()).thenReturn(1);
		when(list.get(0)).thenReturn(service);
		when(list.iterator()).thenReturn(iter);
		when(iter.next()).thenReturn(service);
				
		JobManager mgr;
		try {
			mgr = new JobManager(scheduler, list);
			mgr.start();
			List<Service> svcList = mgr.getServices();
			
			assertNotNull(svcList);
			assertEquals(1, svcList.size());
			assertEquals(list, svcList);
			
			when(service.getStatus()).thenReturn(Service.Status.PASS);
			assertEquals(Service.Status.PASS, svcList.get(0).getStatus());
			
		} catch (SchedulerException e) {
			fail("failed to initiate job manager");
		}
		
	}

}
