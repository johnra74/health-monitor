package com.jstrgames.monitor.cfg;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;

import com.jstrgames.monitor.svc.Service;
import com.jstrgames.monitor.svc.impl.TestService;

public class TestConfigLoader {
	
	private static final String INVALID_JSON_MISSING_CLASSNAME = 
			"[{\"servicename\":\"mock service\",\"hostname\":\"localhost\",\"port\":80,\"schedule\":\"0 0/5 * * * ?\"}]";
	private static final String INVALID_JSON_MISSING_HOSTNAME = 
			"[{\"servicename\":\"mock service\",\"classname\":\"com.jstrgames.monitor.service.impl.MockService\",\"port\":80,\"schedule\":\"0 0/5 * * * ?\"}]";
	private static final String INVALID_JSON_MISSING_PORT = 
			"[{\"servicename\":\"mock service\",\"classname\":\"com.jstrgames.monitor.service.impl.MockService\",\"hostname\":\"localhost\",\"schedule\":\"0 0/5 * * * ?\"}]";
	private static final String INVALID_JSON_MISSING_SCHEDULE = 
			"[{\"servicename\":\"mock service\",\"classname\":\"com.jstrgames.monitor.service.impl.MockService\",\"hostname\":\"localhost\",\"port\":80}]";
	private static final String INVALID_JSON_MISSING_SERVICENAME = 
			"[{\"classname\":\"com.jstrgames.monitor.service.impl.MockService\",\"hostname\":\"localhost\",\"port\":80,\"schedule\":\"0 0/5 * * * ?\"}]";
	
	private static final String MOCK_SERVICENAME = "mock service";
	private static final String MOCK_CLASSNAME = "com.jstrgames.monitor.service.impl.MockService";
	private static final String MOCK_SCHEDULE = "0 0/5 * * * ?";
	private static final String MOCK_HOSTNAME = "localhost";	
	private static final int MOCK_PORT = 80;
	
	private static final String VALID_JSON_CLASSNAME = "com.jstrgames.monitor.svc.impl.TestService";
	private static final String VALID_JSON_SERVICE = 
			"[{\"servicename\":\"" + MOCK_SERVICENAME +
			"\", \"classname\":\"" + VALID_JSON_CLASSNAME + 
			"\",\"schedule\":\"" + MOCK_SCHEDULE +
			"\",\"hostname\":\"" + MOCK_HOSTNAME + 
			"\",\"port\":" + MOCK_PORT + "}]";
	
	
	@Test
	public void testConfigLoader() {
		ConfigLoader cfgLoader = mock(ConfigLoader.class);
		
		Service mockService = mock(Service.class);
		when(mockService.getClassName()).thenReturn(MOCK_CLASSNAME);
		when(mockService.getSchedule()).thenReturn(MOCK_SCHEDULE);
		when(mockService.getHostname()).thenReturn(MOCK_HOSTNAME);
		when(mockService.getPort()).thenReturn(MOCK_PORT);		
		
		@SuppressWarnings("unchecked")
		List<Service> list = mock(List.class);
		when(list.size()).thenReturn(1);
		when(list.get(0)).thenReturn(mockService);
		
		when(cfgLoader.getServices()).thenReturn(list);
		
		assertEquals(1, list.size());
		
		Service testService = list.get(0); 
		assertEquals(MOCK_CLASSNAME, testService.getClassName());
		assertEquals(MOCK_SCHEDULE, testService.getSchedule());
		assertEquals(MOCK_HOSTNAME, testService.getHostname());
		assertEquals(MOCK_PORT, testService.getPort());
	}
	
	@Test
	public void testInvalidJsonConfigMissingClassname() {
		try {
			ConfigLoader cfgLoader = new ConfigLoader(INVALID_JSON_MISSING_CLASSNAME);
			List<Service> list = cfgLoader.getServices();
			assertEquals(0, list.size());
		} catch (ConfigurationException e) {
			fail("should not throw configuration exception");
		}
	}

	@Test
	public void testInvalidJsonConfigMissingHostname() {
		try {
			ConfigLoader cfgLoader = new ConfigLoader(INVALID_JSON_MISSING_HOSTNAME);
			List<Service> list = cfgLoader.getServices();
			assertEquals(0, list.size());
		} catch (ConfigurationException e) {
			fail("should not throw configuration exception");
		}
	}
	
	@Test
	public void testInvalidJsonConfigMissingPort() {
		try {
			ConfigLoader cfgLoader = new ConfigLoader(INVALID_JSON_MISSING_PORT);
			List<Service> list = cfgLoader.getServices();
			assertEquals(0, list.size());
		} catch (ConfigurationException e) {
			fail("should not throw configuration exception");
		}
	}
	
	@Test
	public void testInvalidJsonConfigMissingSchedule() {
		try {
			ConfigLoader cfgLoader = new ConfigLoader(INVALID_JSON_MISSING_SCHEDULE);
			List<Service> list = cfgLoader.getServices();
			assertEquals(0, list.size());
		} catch (ConfigurationException e) {
			fail("should not throw configuration exception");
		}
	}

	@Test
	public void testInvalidJsonConfigMissingServiceName() {
		try {
			ConfigLoader cfgLoader = new ConfigLoader(INVALID_JSON_MISSING_SERVICENAME);
			List<Service> list = cfgLoader.getServices();
			assertEquals(0, list.size());
		} catch (ConfigurationException e) {
			fail("should not throw configuration exception");
		}
	}
	
	@Test
	public void testValidJsonService() {
		try {
			ConfigLoader cfgLoader = new ConfigLoader(VALID_JSON_SERVICE);
			List<Service> list = cfgLoader.getServices();
			assertEquals(1, list.size());
			
			Service svc = list.get(0);
			assertTrue(svc instanceof TestService);
			assertEquals(MOCK_SCHEDULE, svc.getSchedule());
			assertEquals(MOCK_HOSTNAME, svc.getHostname());
			assertEquals(MOCK_PORT, svc.getPort());
			
		} catch (ConfigurationException e) {
			fail("should not throw configuration exception");
		}
	}
}
