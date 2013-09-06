package com.jstrgames.monitor.svc;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.jstrgames.monitor.rule.FailedRuleException;
import com.jstrgames.monitor.svc.Service;

public class TestService {

	@Test
	public void testFailService() {
		Service service = mock(Service.class);
		
		// test fail
		try {
			when(service.isValidService()).thenReturn(false);
			assertFalse(service.isValidService());
		} catch (FailedRuleException e) {
			fail("failed rule test");
		}
	}

	@Test
	public void testPassService() {
		Service service = mock(Service.class);
		
		// test success
		try {
			when(service.isValidService()).thenReturn(true);
			assertTrue(service.isValidService());
		} catch (FailedRuleException e) {
			fail("failed rule test");
		}
	}
}
