package com.jstrgames.monitor.cfg;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.jstrgames.monitor.cfg.ServiceConfig;

public class TestServiceConfig {
	private static final String MOCK_SERVICENAME = "mock service";
	private static final String MOCK_CLASSNAME = "com.jstrgames.monitor.service.impl.MockService";
	private static final String MOCK_SCHEDULE = "0 0/5 * * * ?";
	private static final String MOCK_HOSTNAME = "localhost";	
	private static final int MOCK_PORT = 80;
	
	@Test
	public void testServiceConfig() {
		ServiceConfig cfg = mock(ServiceConfig.class);
		
		when(cfg.getHostname()).thenReturn(MOCK_HOSTNAME);
		when(cfg.getPort()).thenReturn(MOCK_PORT);
		when(cfg.getClassName()).thenReturn(MOCK_CLASSNAME);
		when(cfg.getSchedule()).thenReturn(MOCK_SCHEDULE);
		when(cfg.getServiceName()).thenReturn(MOCK_SERVICENAME);
		
		assertEquals(MOCK_HOSTNAME, cfg.getHostname());
		assertEquals(MOCK_PORT, cfg.getPort());
		assertEquals(MOCK_CLASSNAME, cfg.getClassName());
		assertEquals(MOCK_SCHEDULE, cfg.getSchedule());
		assertEquals(MOCK_SERVICENAME, cfg.getServiceName());
	}

}
