package com.jstrgames.monitor.cfg;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;

import com.jstrgames.monitor.svc.Service;
import com.jstrgames.monitor.svc.impl.HttpService;

public class TestConfigLoader {
	
	private static final String INVALID_JSON_MISSING_HOSTNAME = 
			"[{\"port\": 80, \"template.home\": \"src/main/resources/template\", \"services.json\": \"src/test/resources/services.json\"}]";
	private static final String INVALID_JSON_MISSING_PORT = 
			"[{\"hostname\": \"0.0.0.0\", \"template.home\": \"src/main/resources/template\", \"services.json\": \"src/test/resources/services.json\"}]";
	private static final String INVALID_JSON_MISSING_TEMPLATE = 
			"[{\"hostname\": \"0.0.0.0\", \"port\": 80, \"services.json\": \"src/test/resources/services.json\"}]";
	private static final String INVALID_JSON_MISSING_SERVICE = 
			"[{\"hostname\": \"0.0.0.0\", \"port\": 80, \"template.home\": \"src/main/resources/template\"}]";
	
	private static final String MOCK_SERVICE = "src/test/resources/services.json";
	private static final String MOCK_TEMPLATE = "src/main/resources/template";
	private static final String MOCK_HOSTNAME = "0.0.0.0";	
	private static final int MOCK_PORT = 80;
	
	private static final String MOCK_SVC_CLASSNAME = "com.jstrgames.monitor.service.impl.MockService";
	private static final String MOCK_SVC_SCHEDULE = "0 0/5 * * * ?";
	private static final String MOCK_SVC_HOSTNAME = "localhost";	
	private static final int MOCK_SVC_PORT = 80;
	
	private static final String VALID_JSON_SERVICE = 
			"[{\"services.json\":\"" + MOCK_SERVICE +
			"\", \"template.home\":\"" + MOCK_TEMPLATE + 
			"\",\"hostname\":\"" + MOCK_HOSTNAME + 
			"\",\"port\":" + MOCK_PORT + "}]";
	
	
	@Test
	public void testConfigLoader() {
		ConfigLoader cfgLoader = mock(ConfigLoader.class);
		
		Service mockService = mock(Service.class);
		when(mockService.getClassName()).thenReturn(MOCK_SVC_CLASSNAME);
		when(mockService.getSchedule()).thenReturn(MOCK_SVC_SCHEDULE);
		when(mockService.getHostname()).thenReturn(MOCK_SVC_HOSTNAME);
		when(mockService.getPort()).thenReturn(MOCK_SVC_PORT);		
		
		@SuppressWarnings("unchecked")
		List<Service> list = mock(List.class);
		when(list.size()).thenReturn(1);
		when(list.get(0)).thenReturn(mockService);
		
		when(cfgLoader.getServices()).thenReturn(list);
		
		assertEquals(1, list.size());
		
		Service testService = list.get(0); 
		assertEquals(MOCK_SVC_CLASSNAME, testService.getClassName());
		assertEquals(MOCK_SVC_SCHEDULE, testService.getSchedule());
		assertEquals(MOCK_SVC_HOSTNAME, testService.getHostname());
		assertEquals(MOCK_SVC_PORT, testService.getPort());
		
	}
	
	@Test
	public void testInvalidJsonConfigMissingHostname() {
		try {
			new ConfigLoader(INVALID_JSON_MISSING_HOSTNAME);
			fail("failed to throw configuration exception");
		} catch (ConfigurationException e) {
			// expect to throw exception
		}
	}
	
	@Test
	public void testInvalidJsonConfigMissingPort() {
		try {
			new ConfigLoader(INVALID_JSON_MISSING_PORT);
			fail("failed to throw configuration exception");
		} catch (ConfigurationException e) {
			// expect to throw exception
		}
	}
	
	@Test
	public void testInvalidJsonConfigMissingTemplate() {
		try {
			new ConfigLoader(INVALID_JSON_MISSING_TEMPLATE);
			fail("failed to throw configuration exception");
		} catch (ConfigurationException e) {
			// expect to throw exception
		}
	}

	@Test
	public void testInvalidJsonConfigMissingService() {
		try {
			new ConfigLoader(INVALID_JSON_MISSING_SERVICE);
			fail("failed to throw configuration exception");
		} catch (ConfigurationException e) {
			// expect to throw exception
		}
	}
	
	@Test
	public void testValidJsonService() {
		try {
			ConfigLoader cfgLoader = new ConfigLoader(VALID_JSON_SERVICE);
			// this will load the src/test/resources/services.json which contains 
			// a single json service call 
			List<Service> list = cfgLoader.getServices();
			assertEquals(1, list.size());
			
			Service svc = list.get(0);
			assertTrue(svc instanceof HttpService);
			assertEquals("0 0/15 * * * ?", svc.getSchedule());
			assertEquals("demo.jstrgames.com", svc.getHostname());
			assertEquals(80, svc.getPort());
			
		} catch (ConfigurationException e) {
			fail("should not throw configuration exception");
		}
	}
}
