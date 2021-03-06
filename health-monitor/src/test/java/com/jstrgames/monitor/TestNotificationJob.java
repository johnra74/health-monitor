package com.jstrgames.monitor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jstrgames.monitor.cfg.ConfigurationException;
import com.jstrgames.monitor.cfg.NotificationConfig;

public class TestNotificationJob {
	private final static Logger LOG = LoggerFactory.getLogger(TestNotificationJob.class);
		
	@Test
	public void testExecute() {
		JobExecutionContext context = mock(JobExecutionContext.class);
		NotificationConfig notifyCfg = mock(NotificationConfig.class);
		JobDataMap jobDataMap = mock(JobDataMap.class);
		JobManager jobMgr = mock(JobManager.class);
		Properties prop = mock(Properties.class);
		Message msg = mock(Message.class);
		
		when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
		when(jobDataMap.get("notifyCfg")).thenReturn(notifyCfg);
		when(jobDataMap.get("jobMgr")).thenReturn(jobMgr);
		when(notifyCfg.getMailProperty()).thenReturn(prop);
		when(notifyCfg.shouldSendNotification(jobMgr)).thenReturn(true);
		when(notifyCfg.getMailProperty()).thenReturn(prop);
		when(notifyCfg.isSmtpAuthEnabled()).thenReturn(false);
		
		try {
			when(notifyCfg.createMessage(any(Session.class), eq(jobMgr))).thenReturn(msg);
			Address[] recipients = new Address[1];
			Address[] mailfrom = new Address[1];
			recipients[0] = new InternetAddress("test@localhost", "test");
			mailfrom[0] = new InternetAddress("test@localhost", "test");	
			when(msg.getAllRecipients()).thenReturn(recipients);
			when(msg.getFrom()).thenReturn(mailfrom);
			when(msg.getSubject()).thenReturn("test");
			when(msg.getContent()).thenReturn("testing 1... 2... 3...");
			when(msg.getContentType()).thenReturn("text/plain");	
			
		} catch (ConfigurationException e) {
			fail("failed to setup mock message");
		} catch (UnsupportedEncodingException e) {
			fail("failed to setup mock address");
		} catch (MessagingException e) {
			fail("failed to setup mock message body");
		} catch (IOException e) {
			fail("failed to setup mock connection");
		}

		when(prop.get("mail.smtp.starttls.enable")).thenReturn(false);
		when(prop.get("mail.smtp.host")).thenReturn("localhost");
		when(prop.get("mail.smtp.port")).thenReturn(25);
		
		NotificationJob job = new NotificationJob();
		try {
			job.execute(context);
		} catch (JobExecutionException e) {
			fail("failed to execute job with mock context!");
		}
	}
	
	@Test
	public void testDontSendMail() {
		NotificationConfig cfg = mock(NotificationConfig.class);
		JobManager mgr = mock(JobManager.class);
				
		when(cfg.shouldSendNotification(mgr)).thenReturn(false);
		
		NotificationJob job = new NotificationJob();
		try {
			if(job.sendmail(cfg, mgr, null)) {
				fail("Failed. It shouldn't send email but did");
			}			
		} catch(Exception e) {
			fail("Failed. throw an unexpected exception");
		}
		
	}
	
	@Test
	public void testShouldSendMail() {
		NotificationConfig cfg = mock(NotificationConfig.class);
		JobManager mgr = mock(JobManager.class);
		Properties prop = mock(Properties.class);
		Message msg = mock(Message.class);
		
		when(cfg.shouldSendNotification(mgr)).thenReturn(true);
		when(cfg.getMailProperty()).thenReturn(prop);
		when(cfg.isSmtpAuthEnabled()).thenReturn(false);
		
		when(prop.get("mail.smtp.starttls.enable")).thenReturn(false);
		when(prop.get("mail.smtp.host")).thenReturn("localhost");
		when(prop.get("mail.smtp.port")).thenReturn(25);
		
		Address[] recipients = new Address[1];
		Address[] mailfrom = new Address[1];
		try {
			recipients[0] = new InternetAddress("test@localhost", "test");
			mailfrom[0] = new InternetAddress("test@localhost", "test");	
			when(msg.getAllRecipients()).thenReturn(recipients);
			when(msg.getFrom()).thenReturn(mailfrom);
			when(msg.getSubject()).thenReturn("test");
			when(msg.getContent()).thenReturn("testing 1... 2... 3...");
			when(msg.getContentType()).thenReturn("text/plain");	
			
			NotificationJob job = new NotificationJob();
			if(!job.sendmail(cfg, mgr, msg)) {
				fail("Failed to send email but did");
			}
		} catch (UnsupportedEncodingException e) {
			fail("Failed to setup to address");
		} catch (MessagingException e) {
			fail("Failed to mock to get to");
		} catch (IOException e) {
			fail("Failed to mock to get content");
		} catch( MailServerUnavailableException e ) {
			// just log as warn since test environment may not have valid SMTP server
			LOG.warn("Failed to send.. ignoring since build/user env may not haev SMTP configured");
		} catch(Exception e) {
			fail("Failed. throw an unexpected exception");
		}
		
	}

}
