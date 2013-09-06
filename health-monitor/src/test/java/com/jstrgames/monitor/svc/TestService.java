package com.jstrgames.monitor.svc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.jstrgames.monitor.svc.Service;

public class TestService {

	@Test
	public void testFailService() {
		Service service = mock(Service.class);
		
		// test success
		when(service.checkService()).thenReturn(false);
		
		assertFalse(service.checkService());
	}

	@Test
	public void testPassService() {
		Service service = mock(Service.class);
		
		// test success
		when(service.checkService()).thenReturn(true);
		
		assertTrue(service.checkService());
	}
}
