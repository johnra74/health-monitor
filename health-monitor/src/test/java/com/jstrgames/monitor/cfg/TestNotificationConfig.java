package com.jstrgames.monitor.cfg;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jstrgames.monitor.JobManager;

public class TestNotificationConfig {
	private final static String EXPECTED_MISSING_HOSTNAME_MSG = "missing smtp.hostname";
	private final static String EXPECTED_MISSING_PORT_MSG = "missing smtp.port";
	private final static String EXPECTED_MISSING_MAIL_FROM_MSG = "missing mail.from";
	private final static String EXPECTED_MISSING_MAIL_TO_MSG = "missing mail.to";
	private final static String EXPECTED_MISSING_MAIL_SUBJECT_MSG = "missing mail.subject";
	private final static String EXPECTED_MISSING_SCHEDULE_MSG = "missing notify.schedule";
	private final static String EXPECTED_MISSING_PASSWORD_MSG = "missing smtp.password";
	
	private final static String EXPECTED_MAIL_ADDRESS = "mock@localhost";
	private final static String EXPECTED_MAIL_NAME = "Mr Spock";

	private Map<String,Object> map;
	private Map<String,String> mailMap;		
	private List<Map<String,String>> mailMapList;
	private Iterator<Map<String,String>> mailMapIter;
	private JobManager jobMgr;
	
	@Before
	@SuppressWarnings("unchecked")
	public void setupMock() {		
		map = mock(Map.class);
		mailMap = mock(Map.class);		
		mailMapList = mock(List.class);
		mailMapIter = mock(Iterator.class);
		jobMgr = mock(JobManager.class);
		
		when(map.get(NotificationConfig.NOTIFICATION_MAIL_FROM)).thenReturn(mailMap);
		when(map.get(NotificationConfig.NOTIFICATION_MAIL_TO)).thenReturn(mailMapList);
		
		when(mailMapList.size()).thenReturn(1);
		when(mailMapList.iterator()).thenReturn(mailMapIter);
		
		when(mailMapIter.hasNext()).thenReturn(true).thenReturn(false);
		when(mailMapIter.next()).thenReturn(mailMap);
		
		when(mailMap.get(NotificationConfig.NOTIFICATION_MAIL_EMAIL)).thenReturn(EXPECTED_MAIL_ADDRESS);
		when(mailMap.get(NotificationConfig.NOTIFICATION_MAIL_NAME)).thenReturn(EXPECTED_MAIL_NAME);
		
		when(jobMgr.getSuccessCount()).thenReturn(10);
		when(jobMgr.getTotalCount()).thenReturn(10);
	}	
	
	@After
	public void tearDownMock() {
		map = null;
		mailMap = null;		
		mailMapList = null;
		mailMapIter = null;
		jobMgr = null;
	}
	
	@Test
	public void testMapValidationMissingHostname() {
		try {
			new NotificationConfig(map, "");
			fail("Failed to throw exception!");
		} catch (ConfigurationException e) {
			assertEquals(EXPECTED_MISSING_HOSTNAME_MSG, e.getMessage());
		}
	}
	
	@Test
	public void testMapValidationMissingPort() {
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_HOSTNAME))
			.thenReturn(true);		
		try {
			new NotificationConfig(map, "");
			fail("Failed to throw exception!");
		} catch (ConfigurationException e) {
			assertEquals(EXPECTED_MISSING_PORT_MSG, e.getMessage());
		}
	}
	
	@Test
	public void testMapValidationMissingMailFrom() {
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_HOSTNAME))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_PORT))
			.thenReturn(true);		
		try {
			new NotificationConfig(map, "");
			fail("Failed to throw exception!");
		} catch (ConfigurationException e) {
			assertEquals(EXPECTED_MISSING_MAIL_FROM_MSG, e.getMessage());
		}
	}
	
	@Test
	public void testMapValidationMissingMailTo() {
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_HOSTNAME))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_PORT))
			.thenReturn(true);		
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_FROM))
			.thenReturn(true);		
		try {
			new NotificationConfig(map, "");
			fail("Failed to throw exception!");
		} catch (ConfigurationException e) {
			assertEquals(EXPECTED_MISSING_MAIL_TO_MSG, e.getMessage());
		}
		
	}
	
	@Test
	public void testMapValidationMissingSubject() {
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_HOSTNAME))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_PORT))
			.thenReturn(true);		
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_FROM))
			.thenReturn(true);	
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_TO))
			.thenReturn(true);		
		try {
			new NotificationConfig(map, "");
			fail("Failed to throw exception!");
		} catch (ConfigurationException e) {
			assertEquals(EXPECTED_MISSING_MAIL_SUBJECT_MSG, e.getMessage());
		}
	}
	
	@Test
	public void testMapValidationMissingSchedule() {		
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_HOSTNAME))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_PORT))
			.thenReturn(true);		
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_FROM))
			.thenReturn(true);	
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_TO))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_SUBJECT))
			.thenReturn(true);		
		try {
			new NotificationConfig(map, "");
			fail("Failed to throw exception!");
		} catch (ConfigurationException e) {
			assertEquals(EXPECTED_MISSING_SCHEDULE_MSG, e.getMessage());
		}
	}
	
	@Test
	public void testMapValidationMissingPassword() {
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_HOSTNAME))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_PORT))
			.thenReturn(true);		
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_FROM))
			.thenReturn(true);	
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_TO))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_SUBJECT))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_USERNAME))
			.thenReturn(true);		
		when(map.get(NotificationConfig.NOTIFICATION_SMTP_USERNAME))
			.thenReturn("mock.username");
		try {
			new NotificationConfig(map, "");
			fail("Failed to throw exception!");
		} catch (ConfigurationException e) {
			assertEquals(EXPECTED_MISSING_PASSWORD_MSG, e.getMessage());
		}		
	}
	
	@Test
	public void testMapValidationPass() {
		setupValidConfig();
		try {
			NotificationConfig notifyCfg = new NotificationConfig(map, "");
			assertTrue(notifyCfg.isSmtpAuthEnabled());
		} catch (ConfigurationException e) {
			fail("Failed to throw exception!");
		}
	}
	
	@Test
	public void testShouldSendNotification_SettingDefault() {
		setupValidConfig();
		
		try {
			NotificationConfig notifyCfg = new NotificationConfig(map, "");			
			// check default no notification and success
			assertFalse(notifyCfg.shouldSendNotification(jobMgr));
		} catch (ConfigurationException e) {
			fail("Failed to throw exception!");
		}
	}
	
	@Test
	public void testShouldSendNotification_SettingOn() {
		setupValidConfig();
		// mock failure but notification is off
		when(jobMgr.getSuccessCount()).thenReturn(9);
		// mock failure and notification is on
		when(map.containsKey(NotificationConfig.NOTIFICATION_ONFAILURE)).thenReturn(true);
		when(map.get(NotificationConfig.NOTIFICATION_ONFAILURE)).thenReturn(true);	
		
		try {
			NotificationConfig notifyCfg = new NotificationConfig(map, "");	
			notifyCfg = new NotificationConfig(map, "");	
			assertTrue(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should be true
			assertTrue(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should continue be true
			assertTrue(notifyCfg.shouldSendNotification(jobMgr));
		} catch (ConfigurationException e) {
			fail("Failed to throw exception!");
		}
	}
	
	@Test
	public void testShouldSendNotification_SettingOnButOnFirstOnly() {
		setupValidConfig();
		// mock failure but notification is off
		when(jobMgr.getSuccessCount()).thenReturn(9);
		// mock failure and notification is on
		when(map.containsKey(NotificationConfig.NOTIFICATION_ONFAILURE)).thenReturn(true);
		when(map.get(NotificationConfig.NOTIFICATION_ONFAILURE)).thenReturn(true);	
		// mock failure and notification is on and on first only
		when(map.containsKey(NotificationConfig.NOTIFICATION_ONLYFIRST)).thenReturn(true);
		when(map.get(NotificationConfig.NOTIFICATION_ONLYFIRST)).thenReturn(true);	
		
		try {
			NotificationConfig notifyCfg = new NotificationConfig(map, "");	
			notifyCfg = new NotificationConfig(map, "");	
			assertTrue(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should be false
			assertFalse(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should continue be false even on change
			when(jobMgr.getSuccessCount()).thenReturn(8);
			assertFalse(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should be false
			assertFalse(notifyCfg.shouldSendNotification(jobMgr));
			when(jobMgr.getSuccessCount()).thenReturn(10);
			// always send email when back to success
			assertTrue(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should be false
			assertFalse(notifyCfg.shouldSendNotification(jobMgr));
		} catch (ConfigurationException e) {
			fail("Failed to throw exception!");
		}
	}
	
	@Test
	public void testShouldSendNotification_SettingOnButOnFirstOnlyAndOnChange() {
		setupValidConfig();
		// mock failure but notification is off
		when(jobMgr.getSuccessCount()).thenReturn(9);
		// mock failure and notification is on
		when(map.containsKey(NotificationConfig.NOTIFICATION_ONFAILURE)).thenReturn(true);
		when(map.get(NotificationConfig.NOTIFICATION_ONFAILURE)).thenReturn(true);	
		// mock failure and notification is on and on first only
		when(map.containsKey(NotificationConfig.NOTIFICATION_ONLYFIRST)).thenReturn(true);
		when(map.get(NotificationConfig.NOTIFICATION_ONLYFIRST)).thenReturn(true);	
		// mock failure and notification is on and on first only and changes to status
		when(map.containsKey(NotificationConfig.NOTIFICATION_ONCHANGE)).thenReturn(true);
		when(map.get(NotificationConfig.NOTIFICATION_ONCHANGE)).thenReturn(true);	
		try {
			NotificationConfig notifyCfg = new NotificationConfig(map, "");	
			notifyCfg = new NotificationConfig(map, "");
			assertTrue(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should be false
			assertFalse(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should be true on change
			when(jobMgr.getSuccessCount()).thenReturn(7);
			assertTrue(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should be false
			assertFalse(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should continue to be false
			assertFalse(notifyCfg.shouldSendNotification(jobMgr));
			when(jobMgr.getSuccessCount()).thenReturn(10);
			// always send email when back to success
			assertTrue(notifyCfg.shouldSendNotification(jobMgr));
			// subsequence call should be false
			assertFalse(notifyCfg.shouldSendNotification(jobMgr));
		} catch (ConfigurationException e) {
			fail("Failed to throw exception!");
		}
	}

	private void setupValidConfig() {
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_HOSTNAME))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_PORT))
			.thenReturn(true);		
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_FROM))
			.thenReturn(true);	
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_TO))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_MAIL_SUBJECT))
			.thenReturn(true);
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_USERNAME))
			.thenReturn(true);		
		when(map.get(NotificationConfig.NOTIFICATION_SMTP_USERNAME))
			.thenReturn("mock.username");
		when(map.containsKey(NotificationConfig.NOTIFICATION_SMTP_PASSWORD))
			.thenReturn(true);		
		when(map.get(NotificationConfig.NOTIFICATION_SMTP_PASSWORD))
			.thenReturn("mock.password");
		when(map.containsKey(NotificationConfig.NOTIFICATION_SCHEDULE))
			.thenReturn(true);
	}
	
}
